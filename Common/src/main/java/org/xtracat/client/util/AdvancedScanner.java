package org.xtracat.client.util;

import org.xtracat.datatypes.Coordinates;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.datatypes.MusicGenre;
import org.xtracat.server.CollectionManager;
import org.xtracat.server.CoordinatesBuilder;
import org.xtracat.server.LabelBuilder;
import org.xtracat.server.MusicBandBuilder;

import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * Класс-строитель объека MusicBand
 */
public class AdvancedScanner {

    static Scanner sc;

    public AdvancedScanner(Scanner sc) {
        AdvancedScanner.sc = sc;
    }

    public static Long enterLong(String message, long min, boolean canBeNull) {
        return enterLong(message, min, Long.MAX_VALUE, canBeNull);
    }

    public static Long enterLong(String message, long min) {
        return enterLong(message, min, Long.MAX_VALUE, false);
    }

    public static Long enterLong(String message, long min, long max) {
        return enterLong(message, min, max, false);
    }

    /**
     * Получает от пользователя число Long и валидирует его
     *
     * @return Введенное значение в заданных рамках
     * @params message Сообщение пользователю, выводится перед тем, как считать ответ
     */
    public static Long enterLong(String message, long min, long max, boolean canBeNull) {
        while (true) {
            System.out.println(message);
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty() && canBeNull) return null;
                if (input.isEmpty() && !canBeNull) {
                    System.out.println("Значение не может быть null! Попробуйте снова:");
                    continue;
                }
                long value = Long.parseLong(input);
                if (value >= min && value <= max) return value;
                System.out.println("Значение должно быть в пределах от " + min + " до " + max + "!");
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа! Попробуйте снова:");
            }
        }
    }


    public static Integer enterInteger(String message, int min) {
        return enterInteger(message, min, Integer.MAX_VALUE);
    }

    public static Integer enterInteger(String message, int min, int max) {
        while (true) {
            System.out.println(message);
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.println("Значение должно быть в пределах от " + min + " до " + max + "!");
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа! Попробуйте снова:");
            }
        }
    }

    public static Double enterDouble(String message, double min, boolean minExclusive) {
        return enterDouble(message, min, Double.MAX_VALUE, false, minExclusive);
    }

    public static Double enterDouble(String message, double min, double max, boolean canBeNull, boolean minExclusive) {
        while (true) {
            System.out.println(message);
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty() && canBeNull) return null;
                if (input.isEmpty() && !canBeNull) {
                    System.out.println("Значение не может быть null! Попробуйте снова:");
                    continue;
                }
                double value = Double.parseDouble(input);
                if (minExclusive && value > min && value <= max) return value;
                if (!minExclusive && value >= min && value <= max) return value;
                System.out.println("Значение должно быть в пределах от " + min + " до " + max + "!");
                if (minExclusive) {
                    System.out.println("Причем нижняя граница не включена");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа! Попробуйте снова:");
            }
        }
    }

    /**
     * Взаимодействует с пользователем и создает объект MusicBand
     */
    public MusicBand createMusicBand() {
        System.out.println("Введите название музыкальной группы:");
        String name;
        while (true) {
            name = sc.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("Название не может быть пустым! Попробуйте снова:");
        }

        Coordinates coordinates = enterCoordinates();
        Long numberOfParticipants = enterLong("Введите количество участников:", 1);
        Integer singlesCount = enterInteger("Введите количество синглов:", 1);
        MusicGenre genre = enterMusicGenre();
        Label label = enterLabel();
        MusicBandBuilder builder = new MusicBandBuilder();
        return builder.build(name, coordinates, ZonedDateTime.now(), numberOfParticipants, singlesCount, genre, label);
    }

    public MusicBand createMusicBandInScript() {
        try {
            String name = sc.nextLine();
            Coordinates coordinates = enterCoordinatesInScript();
            Long numberOfParticipants = sc.nextLong();
            Integer singlesCount = sc.nextInt();
            MusicGenre genre = enterMusicGenreInScript();
            Label label = enterLabelInScript();
            MusicBandBuilder builder = new MusicBandBuilder();
            return builder.build(name, coordinates, ZonedDateTime.now(), numberOfParticipants, singlesCount, genre, label);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверные данные при вводе MusicBand(или одного из полей)");
            throw new IllegalArgumentException();
        }


    }

    private MusicGenre enterMusicGenreInScript() {
        try {
            String input = sc.next();
            if (input.isEmpty()) return null;
            if (input.equalsIgnoreCase("INVALID")) {
                throw new IllegalArgumentException();
            }
            return MusicGenre.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Неверные данные при вводе MusicGenre");
            throw new IllegalArgumentException();
        }

    }

    public Label enterLabelInScript() {
        try {
            long bands = sc.nextLong();
            double sales = sc.nextDouble();
            LabelBuilder builder = new LabelBuilder();
            return builder.build(bands, sales);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверные данные при вводе Label");
            throw new IllegalArgumentException();
        }

    }

    private Coordinates enterCoordinatesInScript() {
        long x = sc.nextLong();
        int y = sc.nextInt();
        CoordinatesBuilder builder = new CoordinatesBuilder();

        return builder.build(x, y);


    }

    public Coordinates enterCoordinates() {
        System.out.println("Введите координаты:");
        long x = enterLong("Введите X (макс. 517):", Long.MIN_VALUE, 517);
        int y = enterInteger("Введите Y (макс. 822):", Integer.MIN_VALUE, 822);
        return new Coordinates(x, y);
    }

    public Label enterLabel() {
        System.out.println("Введите данные о лейбле (оставьте пустым для null):");
        try {
            long bands = enterLong("Введите количество групп на лейбле:", 0, true);
            double sales = enterDouble("Введите объем продаж (должно быть больше 0):", 0, true);
            return new Label(bands, sales);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public MusicGenre enterMusicGenre() {
        System.out.println("Выберите жанр (оставьте пустым для null):");
        for (MusicGenre genre : MusicGenre.values()) {
            System.out.println(genre);
        }
        while (true) {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) return null;
            if(input.equalsIgnoreCase("INVALID")){
                continue;
            }
            try {
                return MusicGenre.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный жанр! Попробуйте снова:");
            }
        }
    }


}
