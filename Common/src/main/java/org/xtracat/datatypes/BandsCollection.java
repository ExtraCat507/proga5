package org.xtracat.datatypes;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


@JsonIgnoreProperties(ignoreUnknown = true) // Игнорирует неизвестные поля
@JacksonXmlRootElement(localName = "BandsCollection")
public class BandsCollection {
    @JacksonXmlElementWrapper(localName = "musicBands")
    @JacksonXmlProperty(localName = "musicBand")
    private final LinkedList<MusicBand> musicBands;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private final ZonedDateTime initializationDate;
    private Long currentId;

    public BandsCollection() {
        musicBands = new LinkedList<>();
        initializationDate = ZonedDateTime.now();
        currentId = 0L;
    }

    public Long getCurrentId(){
        return this.currentId;
    }

    //@JsonIgnore
    public LinkedList<MusicBand> getMusicBands() {
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

    public int removeByIndex(int index) {
        if (index < 0 || index >= getNumOfElements()) {
            return -1;
        }
        musicBands.remove(index);
        return 0;
    }

    public int removeLast() {
        if (getNumOfElements() == 0) {
            return -1;
        }
        //System.out.println("removing at " + (numOfElements-1));
        int callback = this.removeByIndex(getNumOfElements() - 1);
        return callback;
    }

    public void clear() {
        musicBands.clear();
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

    @JsonIgnore
    public Long getNewId() {
        return ++currentId;
    }

    public void validate() throws IllegalArgumentException {
        if (musicBands == null || initializationDate == null) {
            throw new IllegalArgumentException();
        }
        if(currentId == null){
            currentId = 0L;
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
            if (band.getId() > currentId) {
                currentId = band.getId();
            }


            ids.add(band.getId());
        }


    }
}
