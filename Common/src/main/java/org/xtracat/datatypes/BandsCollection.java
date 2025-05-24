package org.xtracat.datatypes;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@JsonIgnoreProperties(ignoreUnknown = true) // Игнорирует неизвестные поля
@JacksonXmlRootElement(localName = "BandsCollection")
public class BandsCollection {
    @JacksonXmlElementWrapper(localName = "musicBands")
    @JacksonXmlProperty(localName = "musicBand")
    private final List<MusicBand> musicBands;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime initializationDate;

    public BandsCollection() {
        musicBands = new CopyOnWriteArrayList<>();
        initializationDate = ZonedDateTime.now();
    }

    public BandsCollection(ZonedDateTime initializationDate) {
        musicBands = new CopyOnWriteArrayList<>();
        this.initializationDate = initializationDate;
    }

    public void setInitializationDate(ZonedDateTime timestamp){
        initializationDate = timestamp;
    }


    //@JsonIgnore
    public List<MusicBand> getMusicBands() {
        return this.musicBands;
    }

    @JsonIgnore
    public int getNumOfElements() {
        return musicBands.size();
    }

    public ZonedDateTime getInitializationDate() {
        return initializationDate;
    }

    public void add(MusicBand p) {
        this.musicBands.add(p);
    }

    /**
    @returns Id of deleted music band
    **/
    public long removeByIndex(int index) {
        if (index < 0 || index >= getNumOfElements()) {
            return -1;
        }
        MusicBand band = musicBands.get(index);
        musicBands.remove(index);
        System.out.println(band.getId());
        return band.getId();
    }

    public long removeLast() {
        if (getNumOfElements() == 0) {
            return -1;
        }
        //System.out.println("removing at " + (numOfElements-1));
        long callback = this.removeByIndex(getNumOfElements() - 1);
        return callback;
    }

    public void clear(String userlog) {
        for(MusicBand band : musicBands){
            if (band.getAuthor().equals(userlog)){
                musicBands.remove(band);
            }
        }
        //musicBands.clear();
    }

    public void changeById(int index, MusicBand correcterBand) {
        musicBands.get(index).setGenre(correcterBand.getGenre()); // и так до победного
        musicBands.get(index).setName(correcterBand.getName()); // и так до победного
        musicBands.get(index).setCoordinates(correcterBand.getCoordinates());
        musicBands.get(index).setNumberOfParticipants(correcterBand.getNumberOfParticipants());
        musicBands.get(index).setSinglesCount(correcterBand.getSinglesCount());
        musicBands.get(index).setLabel(correcterBand.getLabel()); // и так до победного
    }

    public void shuffle() {
        Collections.shuffle(this.musicBands);
    }

    @Override
    public String toString() {
        return "Коллекция типов: " + MusicBand.class + "\n" + "Дата инициализации:" + this.initializationDate + "\n" + "Количество элементов: " + getNumOfElements() + "\n";
    }


    public void validate() throws IllegalArgumentException {
        if (musicBands == null || initializationDate == null) {
            throw new IllegalArgumentException();
        }
        if(initializationDate == null){
            throw new IllegalArgumentException();
        }
        HashSet<Long> ids = new HashSet<>();
        Iterator<MusicBand> iter = musicBands.iterator();
        while (iter.hasNext()) {
            MusicBand band = iter.next();
            try {
                band.validate();
            } catch (Exception e) {
                iter.remove();
                continue;
            }

//            if (ids.contains(band.getId()) || band.getId() > currentId) {
//                throw new IllegalArgumentException();
//            }

            if (ids.contains(band.getId()) || band.getId() == null) {
                iter.remove();
                continue;
            }


            ids.add(band.getId());
        }


    }
}
