package org.xtracat.dao;

import org.xtracat.datatypes.Coordinates;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.slf4j.Logger;
import org.xtracat.logger.SingletonLogger;
import org.xtracat.usershit.PasswordRecord;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = SingletonLogger.getLogger();
    private final Connection conn;

    public DatabaseManager(String url, String user, String pass) throws SQLException {
        this.conn = DriverManager.getConnection(url, user, pass);
        logger.info("");
    }

    public  DatabaseManager(String url, Properties info) throws SQLException {
        this.conn = DriverManager.getConnection(url,info);
        logger.info("Opened database connection successfully");
    }

    // ---------- MusicBand CRUD ----------

    public MusicBand add(MusicBand band) throws SQLException {
        // 1) Сначала вставляем Coordinates
        long coordId = insertCoordinates(band.getCoordinates());
        // 2) Потом вставляем Label (если есть)
        Long labelId = null;
        if (band.getLabel() != null) {
            labelId = insertLabel(band.getLabel());
        }
        // 3) Вставляем сам MusicBand
        String sql = """
            INSERT INTO music_band(
              name, coordinates_id, creation_date,
              number_of_participants, singles_count, genre,
              label_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, band.getName());
            ps.setLong(2, coordId);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setLong(4, band.getNumberOfParticipants());
            ps.setLong(5, band.getSinglesCount());
            ps.setObject(6, band.getGenre().name(), Types.OTHER);
            if (labelId != null) ps.setLong(7, labelId);
            else ps.setNull(7, Types.BIGINT);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long id = rs.getLong(1);
                band.setId(id);
                band.setCreationDate(ZonedDateTime.from(LocalDate.now()));
                return band;
            } else {
                throw new SQLException("Insert music_band failed, no ID obtained.");
            }
        }
    }

    public boolean update(MusicBand band) throws SQLException {
        final String selectSql = "SELECT coordinates_id, label_id FROM music_band WHERE id = ?";
        final String updateSql =
                "UPDATE music_band SET " +
                        "  name = ?, " +
                        "  coordinates_id = ?, " +
                        "  number_of_participants = ?, " +
                        "  singles_count = ?, " +
                        "  genre = ?, " +
                        "  label_id = ? " +
                        "WHERE id = ?";

        boolean originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql)
        ) {
            // 1. Получаем старые foreign keys
            long oldCoordId;
            Long oldLabelId;
            selectStmt.setLong(1, band.getId());
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("MusicBand with id=" + band.getId() + " not found");
                }
                oldCoordId = rs.getLong("coordinates_id");
                long tmpLabel = rs.getLong("label_id");
                oldLabelId = rs.wasNull() ? null : tmpLabel;
            }

            // 2. Вставляем новые Coordinates и получаем новый ID
            long newCoordId = insertCoordinates(band.getCoordinates());

            // 3. Вставляем новый Label (если задан) и получаем новый ID
            Long newLabelId = null;
            if (band.getLabel() != null) {
                newLabelId = insertLabel(band.getLabel());
            }

            // 4. Готовим и выполняем UPDATE
            updateStmt.setString(1, band.getName());
            updateStmt.setLong(2, newCoordId);
            updateStmt.setLong(3, band.getNumberOfParticipants());
            updateStmt.setLong(4, band.getSinglesCount());
            updateStmt.setObject(5, band.getGenre().name(), Types.OTHER);
            if (newLabelId != null) {
                updateStmt.setLong(6, newLabelId);
            } else {
                updateStmt.setNull(6, Types.BIGINT);
            }
            updateStmt.setLong(7, band.getId());

            int affected = updateStmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Update failed, no rows affected");
            }

            // 5. Удаляем старые записи в coordinates и label
            deleteCoordinatesById(oldCoordId);
            if (oldLabelId != null) {
                deleteLabelById(oldLabelId);
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            conn.rollback();
            logger.error(ex.getMessage());
            return false;
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }

    // Вспомогательные методы удаления
    private void deleteCoordinatesById(long coordId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM coordinates WHERE id = ?"
        )) {
            ps.setLong(1, coordId);
            ps.executeUpdate();
        }
    }

    private void deleteLabelById(long labelId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM label WHERE id = ?"
        )) {
            ps.setLong(1, labelId);
            ps.executeUpdate();
        }
    }


    // remove last через метод getMaxId
    public boolean remove(long id) throws SQLException {
        // Удалим запись из music_band — благодаря ON DELETE CASCADE удалит и зависимости
        String sql = "DELETE FROM music_band WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public long getMaxId() throws SQLException {
        String sql = "SELECT MAX(id) FROM music_band";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                long maxId = rs.getLong(1);
                return rs.wasNull() ? 0 : maxId;
            } else {
                throw new SQLException("Failed to retrieve max ID from music_band");
            }
        }
    }

    // ---------- User methods ----------

    // проверка на то есть ли такой логин -> возрат
    public boolean register(String login, PasswordRecord passwordRecord) throws SQLException {
        String sql = """
            INSERT INTO app_user(login, salt, password_hash)
            VALUES (?, ?, ?)
        """;
        String hmm = """
                SELECT id
                FROM app_user
                WHERE login = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement check = conn.prepareStatement(hmm)) {
            check.setString(1,login);
            ResultSet rss = check.executeQuery();
            System.out.println(rss);
            if(rss.next()){//есть записи с таким логином - плохо!
                logger.info("User tries to create an existing login");
                return false;
            }
            ps.setString(1, login);
            ps.setString(2, passwordRecord.salt());
            ps.setBytes(3, passwordRecord.hash());
            logger.info("User registered + " + login);
            return ps.executeUpdate() == 1;
        }catch (SQLException e){
            logger.error("Fail on registration");
            throw new SQLException("Database error");
        }
    }

    public PasswordRecord getProof(String login) {
        String sql = """
            SELECT salt, password_hash
            FROM app_user
            WHERE login = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PasswordRecord(rs.getString("salt"),rs.getBytes("password_hash"));
            } else {
                logger.error("Insufficient login");
                return new PasswordRecord(null,null);
            }
        }catch (SQLException e){
            logger.error(e.getMessage());
            return null;
        }
    }

    // ---------- Helpers for Coordinates & Label ----------

    private long insertCoordinates(Coordinates c) throws SQLException {
        String sql = "INSERT INTO coordinates(x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, c.getX());
            ps.setInt(2, c.getY());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            else throw new SQLException("Insert coordinates failed.");
        }
    }

    private long insertLabel(Label l) throws SQLException {
        String sql = "INSERT INTO label(bands, sales) VALUES (?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, l.getBands());
            ps.setDouble(2,l.getSales());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            else throw new SQLException("Insert label failed.");
        }
    }

}
