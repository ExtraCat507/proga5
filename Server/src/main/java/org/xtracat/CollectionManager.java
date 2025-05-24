package org.xtracat;

import org.slf4j.Logger;
import org.xtracat.dao.DatabaseManager;
import org.xtracat.dao.SingletonDAO;
import org.xtracat.datatypes.BandsCollection;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.logger.SingletonLogger;
import org.xtracat.usershit.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionManager { // Receiver (исполнитель)
    BandsCollection bandsCollection;
    DatabaseManager dao = SingletonDAO.getDao();
    private static final Logger logger = SingletonLogger.getLogger();


    public CollectionManager() {
        //this.bandsCollection = sz.load(filename, this);
        try {
            this.bandsCollection = dao.getAll();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            this.bandsCollection = new BandsCollection();
            dao.resetTables();
        }
    }

    public void add(MusicBand p) {
        Long id = null;
        try {
            id = dao.add(p).getId();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        p.setId(id);
        //System.out.println("VSE NORM BROOOOOOOOOO");
        this.bandsCollection.add(p);
    }

    public List<MusicBand> getList() {
        return bandsCollection.getMusicBands();
    }

    public BandsCollection getBandsCollection() {
        return bandsCollection;
    }

    public void clearCollection(User user) {
        this.bandsCollection.clear(user.login());
        System.out.println(this.bandsCollection.getMusicBands());
        dao.clear(user.login());
    }

    public int changeById(long id, MusicBand correcterBand) {
        int previousBandIndex = findById(id);
        correcterBand.setId(id);
        try {
            dao.update(correcterBand);
            this.bandsCollection.changeById(previousBandIndex, correcterBand);
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }

    public int findById(long id) {    // id in the strucure -> id in the LinkedList
        for (int i = 0; i < bandsCollection.getNumOfElements(); i++) {
            MusicBand band = bandsCollection.getMusicBands().get(i);
            if (band.getId() == id) return i;
        }
        return -1;
    }

    public int removeById(long id, String login) {
        int index = findById(id);
        try {
            if(dao.remove(id,login)){
                this.bandsCollection.removeByIndex(index);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    public long removeByIndex(int index, String login) {
        long id = this.bandsCollection.removeByIndex(index);
        try {
            dao.remove(id,login);
        } catch (SQLException e) {
            return -1;
        }
        return id;
    }


//    public long removeLast() {
//        long id = this.bandsCollection.removeLast();
//        try {
//            dao.remove(id);
//        } catch (SQLException e) {
//            return -1;
//        }
//        return id;
//    }

    public void shuffle() {
        this.bandsCollection.shuffle();
    }


    public int countGreaterThanNumberOfParticipants(int number) {
        int result = 0;
        for (MusicBand band : this.getList()) {
            if (band.getNumberOfParticipants() > number) {
                result++;
            }
        }
        return result;
    }


    public ArrayList<MusicBand> filterGreaterThanLabel(Label label) {
        ArrayList<MusicBand> result = new ArrayList<>(this.bandsCollection.getNumOfElements());
        for (MusicBand band : this.getList()) {
            if (band.getLabel() == null) continue;
            if (band.getLabel().compareTo(label) == 1) {
                result.add(band);
            }
        }
        return result;
    }

    public long[] printFieldAscendingNumberOfParticipants() {
        int n = this.bandsCollection.getNumOfElements();
        long[] result = new long[n];
        for (int i = 0; i < n; i++) {
            MusicBand band = this.getList().get(i);
            result[i] = (band.getNumberOfParticipants());
        }
        Arrays.sort(result);
        return result;
    }

}
