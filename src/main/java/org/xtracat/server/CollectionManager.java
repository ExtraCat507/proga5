package org.xtracat.server;

import org.xtracat.datatypes.BandsCollection;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.storage.Serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class CollectionManager { // Receiver (исполнитель)
    BandsCollection bandsCollection;
    String filename;
    Serializer sz;

    public CollectionManager(String filename) {
        this.sz = new Serializer();
        this.filename = filename;
        this.bandsCollection = sz.load(filename, this);
    }

    public void add(MusicBand p) {
        this.bandsCollection.add(p);
    }

    public LinkedList<MusicBand> getList() {
        return bandsCollection.getMusicBands();
    }

    public BandsCollection getBandsCollection() {
        return bandsCollection;
    }

    public void clearCollection() {
        this.bandsCollection.clear();
    }

    public int changeById(long id, MusicBand correcterBand) {
        int previousBandIndex = findById(id);
        this.bandsCollection.changeById(previousBandIndex, correcterBand);
        return 0;
    }

    public int findById(long id) {    // id in the strucure -> id in the LinkedList
        for (int i = 0; i < bandsCollection.getNumOfElements(); i++) {
            MusicBand band = bandsCollection.getMusicBands().get(i);
            if (band.getId() == id) return i;
        }
        return -1;
    }

    public int removeById(long id) {
        int index = findById(id);
        this.bandsCollection.removeByIndex(index);
        return 0;
    }

    public int removeByIndex(int id) {
        int callback = this.bandsCollection.removeByIndex(id);
        return callback;
    }


    public int removeLast() {
        int callback = this.bandsCollection.removeLast();
        return callback;
    }

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

    public long getNewId() {
        return bandsCollection.getNewId();
    }
}
