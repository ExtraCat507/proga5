package org.xtracat.datatypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true) // Пропускает невалидные объекты
//@JacksonXmlRootElement(localName = "musicBand")
public class MusicBand implements Comparable, Serializable {
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    private Integer singlesCount; //Поле не может быть null, Значение поля должно быть больше 0
    private MusicGenre genre; //Поле может быть null
    private Label label; //Поле может быть null
    private String author;


    public MusicBand(
            @JsonProperty("name") String name,
            @JsonProperty("coordinates") Coordinates coordinates,
            @JsonProperty("creationDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") ZonedDateTime creationDate,
            @JsonProperty("numberOfParticipants") Long numberOfParticipants,
            @JsonProperty("singlesCount") int singlesCount,
            @JsonProperty("genre") MusicGenre genre,
            @JsonProperty("label") Label label) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.singlesCount = singlesCount;
        this.genre = genre;
        this.label = label;
    }



    @Override
    public int compareTo(Object o) { // сравнение по количеству синглов
        if (o.getClass() != MusicBand.class) {
            throw new ClassCastException();
        }

        if (this.singlesCount > ((MusicBand) o).singlesCount) {
            return 1;
        } else if (this.singlesCount < ((MusicBand) o).singlesCount) {
            return -1;
        } else {
            //if(this.name < ((MusicBand) o).name)
            return 0;
        }

        //return 0;
    }

    public Long setId(Long id){
        this.id = id;
        return id;
    }

    @Override
    public String toString() {
        if (label == null) {
            return "Music band: " + this.name + "\n" + "Кол-во участников: " + this.numberOfParticipants + "\n" + "Жанр: " + this.genre + "\n" + "Кол-во синглов: " + this.singlesCount + "\n" + "id: " + this.id +"\n"+ coordinates + "\n" + "Label: null" + "\n________________\n";
        }
        return "Music band: " + this.name + "\n" + "Кол-во участников: " + this.numberOfParticipants + "\n" + "Жанр: " + this.genre + "\n" + "Кол-во синглов: " + this.singlesCount + "\n" + "id: " + this.id + "\n" + coordinates  + "\n" + label + "\n________________\n";
    }


    public void validate() throws IllegalArgumentException, NullPointerException {
        if (name.isEmpty() || numberOfParticipants <= 0 || singlesCount <= 0 || creationDate == null) {
            throw new IllegalArgumentException();
        }
        if(genre == MusicGenre.INVALID){
            throw new IllegalArgumentException();
        }
        coordinates.validate();
        if (label != null) {
            label.validate();
        }

    }


    public Long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Integer getSinglesCount() {
        return singlesCount;
    }

    public void setSinglesCount(Integer singlesCount) {
        this.singlesCount = singlesCount;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

