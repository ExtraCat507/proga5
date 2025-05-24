//package org.xtracat.storage;
//
//import com.fasterxml.jackson.databind.DatabindException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import org.xtracat.datatypes.BandsCollection;
//import org.xtracat.CollectionManager;
//import com.fasterxml.jackson.datatype.jsr310.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;
//@Deprecated
//public class Serializer {
//    private final XmlMapper xmlMapper;
//
//    public Serializer() {
//        xmlMapper = new XmlMapper();
//        xmlMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        xmlMapper.registerModule(new JavaTimeModule());
//        xmlMapper.addHandler(new CustomProblemHandler());
//        xmlMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false); // 🔹 Пропускаем ошибки парсинга классов
//
//    }
//
//    public int save(String filename, CollectionManager cm) {
//        File file = new File(filename);
//        file.setWritable(true);
//        file.setReadable(true);
//
//        // Проверяем доступ на запись
//        if (!file.canWrite()) {
//            System.out.println("В файл невозможно записать - права доступа не позволяют");
//            return -1;
//        }
//
//        try {
//            BandsCollection bands = cm.getBandsCollection();
//            xmlMapper.writeValue(file, bands);
//            return 0;
//        }
//        catch (DatabindException e){
//            return -1;
//        }
//        catch (IOException e) {
//            System.out.println("Ошибка при записи в файл: " + e.getMessage());
//            return -1;
//        }
//    }
//
//    public BandsCollection load(String filename, CollectionManager cm) {
//        File file = new File(filename);
//        if (!file.exists() || !file.canRead()) {
//            System.out.println("Такого файла не существует или нет нужных прав доступа\nСоздать файл? (y/n):");
//            Scanner sc = new Scanner(System.in);
//            if (sc.next().equalsIgnoreCase("y")) {
//                return new BandsCollection();
//            }
//            System.exit(0);
//        }
//        if (file.isDirectory()) {
//            System.out.println("Это папка, а не файл");
//            System.exit(0);
//        }
//        try {
//            BandsCollection bandsCollection = xmlMapper.readValue(file, BandsCollection.class);
//            bandsCollection.validate(); // Удаляем невалидные элементы
//            return bandsCollection;
//        } catch (IOException | IllegalArgumentException e) {
//            //System.out.println(e.getMessage());
//            System.out.println("Файл с некорректной структурой, он будет перезаписан");
//            return new BandsCollection();
//        }
//    }
//}
