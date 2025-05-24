package org.xtracat.dao;

import org.xtracat.datatypes.*;
import org.slf4j.Logger;
import org.xtracat.logger.SingletonLogger;
import org.xtracat.server.CoordinatesBuilder;
import org.xtracat.server.LabelBuilder;
import org.xtracat.server.MusicBandBuilder;
import org.xtracat.usershit.PasswordRecord;
import org.xtracat.usershit.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.logging.Handler;

public class DatabaseManager {
    private static final Logger logger = SingletonLogger.getLogger();
    private final Connection conn;

    public DatabaseManager(String url, String user, String pass) throws SQLException {
        this.conn = DriverManager.getConnection(url, user, pass);
        logger.info("Opened database connection successfully");
    }

    public  DatabaseManager(String url, Properties info) throws SQLException {
        this.conn = DriverManager.getConnection(url,info);
        logger.info("Opened database connection successfully");
    }

    public MusicBand add(MusicBand band) throws SQLException {
        boolean originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {

            long coordId = insertCoordinates(band.getCoordinates());

            Long labelId = null;
            if (band.getLabel() != null) {
                labelId = insertLabel(band.getLabel());
            }

            String sql = """
                INSERT INTO music_band(
                  name, coordinates_id, creation_date,
                  number_of_participants, singles_count, genre,
                  label_id, user_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?,?)
                RETURNING id, creation_date
            """;
            String hmm = """
                    SELECT id
                    FROM app_user
                    WHERE login = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement check = conn.prepareStatement(hmm)) {
                ps.setString(1, band.getName());
                ps.setLong(2, coordId);
                ps.setDate(3, Date.valueOf(LocalDate.now()));
                ps.setLong(4, band.getNumberOfParticipants());
                ps.setLong(5, band.getSinglesCount());
                ps.setObject(6, band.getGenre().name(), Types.OTHER);
                if (labelId != null) ps.setLong(7, labelId);
                else ps.setNull(7, Types.BIGINT);
                check.setString(1, band.getAuthor());
                ResultSet rss = check.executeQuery();
                if(rss.next()) {
                    ps.setLong(8, rss.getLong(1));
                }else{
                    logger.error("Insert music_band failed, unknown user.");
                    throw new SQLException("Insert music_band failed, unknown user.");
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    band.setId(id);
                    Timestamp t = rs.getTimestamp(2);
                    band.setCreationDate(t.toInstant().atZone(ZoneId.systemDefault()));
                    conn.commit();
                    logger.info("Added new band to database {}",band);
                    return band;
                } else {
                    throw new SQLException("Insert music_band failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            logger.error(e.getMessage());
            return null;
        }finally {
            conn.setAutoCommit(true);
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
                        "  label_id = ?, " +
                        "  user_id = ?" +
                        "WHERE id = ?";
        String hmm = """
                SELECT id
                FROM app_user
                WHERE login = ?
                """;
        boolean originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                PreparedStatement check = conn.prepareStatement(hmm)
        ) {

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

            long newCoordId = insertCoordinates(band.getCoordinates());

            Long newLabelId = null;
            if (band.getLabel() != null) {
                newLabelId = insertLabel(band.getLabel());
            }

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
            check.setString(1, band.getAuthor());
            ResultSet rss = check.executeQuery();
            updateStmt.setLong(7,rss.getLong(1));
            updateStmt.setLong(8, band.getId());

            int affected = updateStmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Update failed, no rows affected");
            }

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


    // remove last через метод getMaxId(лол нет)
    public boolean remove(long id, String user) throws SQLException {

        String sqll = """
                SELECT login 
                FROM app_user
                WHERE id = ?
                """;

        String sql = "DELETE FROM music_band" +
                "JOIN app_user ON app_user.id = music_band.user_id" +
                "WHERE id = ? AND login = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);
        PreparedStatement ps2 = conn.prepareStatement(sqll)) {
            ps.setString(2,user);
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
            //System.out.println(rss);
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
        String sql = "INSERT INTO label(bands, sales) VALUES (?,?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, l.getBands());
            ps.setDouble(2,l.getSales());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            else throw new SQLException("Insert label failed.");
        }
    }

    public BandsCollection getAll() throws SQLException {
        String selectDateSql = "SELECT initialization_date FROM collection_properties LIMIT 1";
        ZonedDateTime initDate;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectDateSql)) {
            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("initialization_date");
                try {
                    initDate = ts.toInstant().atZone(ZoneId.systemDefault());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    initDate = resetInitializationDate();
                    return new BandsCollection(initDate);
                }
            } else {
                // записей нет
                initDate = resetInitializationDate();
                return new BandsCollection(initDate);
            }
        }


        BandsCollection collection = new BandsCollection(initDate);


        String sql = """
        SELECT mb.id AS mb_id, mb.name AS mb_name,
               mb.creation_date AS mb_date, mb.number_of_participants,
               mb.singles_count, mb.genre,
               c.id AS c_id, c.x AS c_x, c.y AS c_y,
               l.id AS l_id, l.bands AS l_bands, l.sales AS l_sales,
               usr.login
        FROM music_band mb
        JOIN coordinates c ON mb.coordinates_id = c.id
        LEFT JOIN label l      ON mb.label_id       = l.id
        JOIN app_user usr ON usr.id = mb.user_id
        ORDER BY mb.id
    """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                long bandId = rs.getLong("mb_id");
                try {
                    // coordinates
                    long coordsId = rs.getLong("c_id");
                    long x = rs.getLong("c_x");
                    int y = rs.getInt("c_y");
                    Coordinates coords = new CoordinatesBuilder().build(x, y);

                    // label (может быть null)
                    Label label = null;
                    long lId = rs.getLong("l_id");
                    if (!rs.wasNull()) {
                        long lbands = rs.getLong("l_bands");
                        double lsales = rs.getDouble("l_sales");
                        label = new LabelBuilder().build(lbands, lsales);
                    }

                    String name = rs.getString("mb_name");
                    Timestamp mbTs = rs.getTimestamp("mb_date");
                    ZonedDateTime creationDate = mbTs.toInstant().atZone(ZoneId.systemDefault());
                    long numPart = rs.getLong("number_of_participants");
                    int singles = rs.getInt("singles_count");
                    MusicGenre genre = MusicGenre.valueOf(rs.getString("genre"));

                    MusicBand band = new MusicBandBuilder()
                            .build(name, coords, creationDate, numPart, singles, genre, label);
                    band.setId(bandId);
                    band.setAuthor(rs.getString("login"));
                    collection.getMusicBands().add(band);

                } catch (IllegalArgumentException e) {
                    logger.error("Illegal Argument in processing band with id {}",bandId);

                    deleteBandAndDeps(rs.getLong("mb_id"));
                }
            }
        }

        try {
            collection.validate();
        } catch (IllegalArgumentException e) {
            logger.error("Illegal Argument on final validation");
            initDate = resetInitializationDate();
            return new BandsCollection(initDate);
        }

        // 6. Возвращаем валидную коллекцию
        return collection;
    }

    public void resetTables() {
        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM music_band");
                stmt.execute("DELETE FROM coordinates");
                stmt.execute("DELETE FROM label");
                stmt.execute("DELETE FROM collection_properties");
            }
        } catch (SQLException e) {
            logger.error("During full reset(db) something went wrong");
        }
    }


    private ZonedDateTime resetInitializationDate() throws SQLException {

        resetTables();

        ZonedDateTime now = ZonedDateTime.now();
        String insertSql = "INSERT INTO collection_properties(initialization_date) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setTimestamp(1, Timestamp.from(now.toInstant()));
            ps.executeUpdate();
        }
        return now;
    }

    // Удалить одну запись band + связанные записи
    private void deleteBandAndDeps(long bandId) throws SQLException {
        // получаем связанные FK
        String sel = "SELECT coordinates_id, label_id FROM music_band WHERE id = ?";
        long cid, lid;
        try (PreparedStatement ps = conn.prepareStatement(sel)) {
            ps.setLong(1, bandId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return;
                cid = rs.getLong("coordinates_id");
                lid = rs.getLong("label_id");
                if (rs.wasNull()) lid = -1;
            }
        }
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM music_band WHERE id = ?")) {
            ps.setLong(1, bandId);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM coordinates WHERE id = ?")) {
            ps.setLong(1, cid);
            ps.executeUpdate();
        }

        if (lid != -1) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM label WHERE id = ?")) {
                ps.setLong(1, lid);
                ps.executeUpdate();
            }
        }
    }

    public void clear(String login) {
        String hmm = """
                    SELECT id
                    FROM app_user
                    WHERE login = ?
                    """;
        String select = """
                SELECT id
                FROM music_band
                WHERE user_id = ?
                """;
        long id;
        try(PreparedStatement check = conn.prepareStatement(hmm);
        PreparedStatement sel = conn.prepareStatement(select)){
            check.setString(1,login);
            ResultSet rs = check.executeQuery();
            if(rs.next()){
                id = rs.getLong(1);
            }else{return;}
            sel.setLong(1,id);
            ResultSet resultSet = sel.executeQuery();
            while(resultSet.next()){
                deleteBandAndDeps(id);
                conn.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
