package com.rahul.projects.JdbcWithMongo;

import com.rahul.projects.JdbcWithMongo.services.CrudServices;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {

    static final CrudServices crudServices = new CrudServices();

    public static void main(String[] args) {
        // createRecord();
        // updateGender();
        // getStudentsInAgeRange();
        getStudentsCreatedOnGivenDate();
        //datetimeInGMT();

    }

    public static void updateGender() {
        crudServices.updatedGenderOfSpecificStudents();
    }

    public static void getStudentsInAgeRange() {
        final List<Document> studentsWithinAgeRange = crudServices.getStudentsWithinAgeRange(25, 27);
        for (Document document : studentsWithinAgeRange) {
            System.out.println(document);
        }
    }

    public static void getStudentsCreatedOnGivenDate() {

        //2024-03-22T19:15:58.934+00:00
        final Date date = new Date();
        final List<Document> studentsWithinAgeRange = crudServices.getStudentsCreatedOnDate(String.valueOf(date));
        for (Document document : studentsWithinAgeRange) {
            System.out.println(document);
        }
    }


    public static void createRecord() {
        Map<String, Object> record = new HashMap<>();
        record.put("firstname", "Kamini");
        record.put("lastname", "Singh");
        record.put("age", 20);
        record.put("city", "Kondapur");
        record.put("state", "UttarPradesh");
        record.put("mother", "Jaya");
        record.put("father", "Amitabh");

        crudServices.saveStudent(record);

    }

    public static void datetimeInGMT(){
        // Get the current date and time in UTC
        Instant instant = Instant.now();

        // Set the time zone to GMT
        ZoneId zone = ZoneId.of("GMT");

        // Format the date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateTime = instant.atZone(zone).format(formatter);

        System.out.println("Current date and time (GMT): " + dateTime); //2024-03-29T21:18:11.031Z


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));

    }

    public void test2(){
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.add(Calendar.MILLISECOND, -currentDateTime.getTimeZone().getOffset(currentDateTime.getTimeInMillis()));
        Date date2 = currentDateTime.getTime();
        System.out.println("Current GMT time in India :: "+date2); //Fri Mar 29 20:57:30 IST 2024 (GMT time hain ye)

    }
}
