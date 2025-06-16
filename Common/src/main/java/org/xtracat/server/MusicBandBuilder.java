package org.xtracat.server;

import org.xtracat.datatypes.Coordinates;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.datatypes.MusicGenre;
import org.xtracat.datatypes.MusicLabel;

import java.util.ArrayList;
import java.util.List;

import java.time.ZonedDateTime;

public class MusicBandBuilder {

    private String name;
    private Coordinates coordinates;
    private Long numberOfParticipants;
    private Integer singlesCount;
    private MusicGenre genre;
    private MusicLabel musicLabel;

    public MusicBandBuilder() {
    }

    public MusicBandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MusicBandBuilder setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public MusicBandBuilder setNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
        return this;
    }

    public MusicBandBuilder setSinglesCount(Integer singlesCount) {
        this.singlesCount = singlesCount;
        return this;
    }

    public MusicBandBuilder setGenre(MusicGenre genre) {
        this.genre = genre;
        return this;
    }

    public MusicBandBuilder setMusicLabel(MusicLabel musicLabel) {
        this.musicLabel = musicLabel;
        return this;
    }


    public MusicBand build() {
        try {
            MusicBand band = new MusicBand(
                    this.name,
                    this.coordinates,
                    ZonedDateTime.now(), // Auto-generated creation date
                    this.numberOfParticipants,
                    this.singlesCount,
                    this.genre,
                    this.musicLabel
            );
            band.validate();
            return band;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error building MusicBand: " + e.getMessage());
            return null;
        }
    }


    public MusicBand build(String name, Coordinates coordinates, ZonedDateTime creationDate,
                           Long numberOfParticipants, Integer singlesCount, MusicGenre genre, MusicLabel musicLabel) {
        try {
            MusicBand band = new MusicBand(name, coordinates, creationDate, numberOfParticipants, singlesCount, genre, musicLabel);
            band.validate();
            return band;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error building MusicBand: " + e.getMessage());
            return null;
        }
    }

    public boolean validate() throws IllegalArgumentException {
        List<String> errorMessages = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            errorMessages.add("name (must not be null or empty)");
        }
        if (coordinates == null) {
            errorMessages.add("coordinates (must not be null)");
        } else {
            if (coordinates.getX() == null) {
                errorMessages.add("coordinates.x (must not be null)");
            }
            if (coordinates.getY() <= -490) {
                errorMessages.add("coordinates.y (must be > -490)");
            }
        }

        if (numberOfParticipants == null) {
            errorMessages.add("numberOfParticipants (must not be null)");
        } else if (numberOfParticipants <= 0) {
            errorMessages.add("numberOfParticipants (must be > 0)");
        }

        if (singlesCount == null) {
            errorMessages.add("singlesCount (must not be null)");
        } else if (singlesCount <= 0) {
            errorMessages.add("singlesCount (must be > 0)");
        }

        if (genre == null) {
            errorMessages.add("genre (must not be null)");
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException("Validation failed. Invalid fields: " + String.join("; ", errorMessages));
        }

        return true;
    }


    public void setLabel(MusicLabel label) {
        this.musicLabel = label;
    }
}