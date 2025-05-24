package org.xtracat.server;

import org.xtracat.datatypes.Coordinates;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.datatypes.MusicGenre;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class MusicBandBuilder {

    public MusicBandBuilder() {
    }

    public MusicBand build(String name, Coordinates coordinates, ZonedDateTime creationDate,
                           Long numberOfParticipants, Integer singlesCount, MusicGenre genre, Label label) {
        MusicBand band = new MusicBand(name, coordinates, creationDate , numberOfParticipants, singlesCount, genre, label);
        band.validate();
        return band;
    }
}
