package com.rahul.projects.JdbcWithMongo;

import com.rahul.projects.JdbcWithMongo.services.CrudServices;
import org.bson.Document;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {

    static final CrudServices crudServices = new CrudServices();

    public static void main(String[] args) throws ParseException {
        //testCreateRecord();
        // testUpdateGender();
        // testGetStudentsInAgeRange();
        // datetimeInGMT();
        // testFindStudentCreatedOnBeforeGivenDates();
        // testFindStudentLastUpdatedOnBeforeGivenDates();
        //testFindRecordsWithMultipleCondition();
       // getFemaleStudents();
        //getStudentsAsGenderGroup();
        //getDocWithAgeGroupAndCountOfStudents_Only_Females();
        getDocWithAgeGroupAndCountOfStudents_Also_Count_Only_Females();

    }

    //=================Aggregate==============================================
    public static void getFemaleStudents() {
        crudServices.getFemaleStudents();
    }

    public static void getStudentsAsGenderGroup(){
        crudServices.makeGenderGroup();
    }

    public static void getDocWithAgeGroupAndCountOfStudents_Only_Females(){
        crudServices.docWithAgeGroupAndCountOfStudents_Only_Females();
    }

    public static void getDocWithAgeGroupAndCountOfStudents_Also_Count_Only_Females(){
        crudServices.docWithAgeGroupAndCountOfStudents_Also_Count_Only_Females();
    }


    //=========================================================================


    /*
    Find records:
    Gender must be Male
    Age must be greater than 25
    Last updated date or created date must be greater than "2024-03-04T05:45:01.758+00:00"
     */
    public static void testFindRecordsWithMultipleCondition() throws ParseException {
        String expDate = "2024-02-04T05:44:31.182+00:00"; //Unparseable date: "2024-02-04T05:44:31.182+00:00"
        expDate = "2024-02-25 09:28:04.401 UTC";

        crudServices.findRecordsWithMultipleCondition(expDate);
    }

    public static void testCreateRecord() {
        Map<String, Object> record = new HashMap<>();
        record.put("firstname", "Manisha");
        record.put("lastname", "Singh");
        record.put("age", 20);
        record.put("gender", "Female");
        record.put("city", "Patna");
        record.put("state", "Bihar");
        record.put("mother", "Bharat");
        record.put("father", "Seetha");
        crudServices.saveStudent(record);
    }

    public static void testFindStudentCreatedOnBeforeGivenDates() throws ParseException {
        crudServices.findStudentCreatedOnBeforeGivenDates(null);

    }

    public static void testFindStudentLastUpdatedOnBeforeGivenDates() throws ParseException {
        crudServices.findStudentLastUpdatedOnBeforeGivenDates(null);

    }

    public static void testGetStudentsInAgeRange() {
        final List<Document> studentsWithinAgeRange = crudServices.getStudentsWithinAgeRange(25, 27);
        for (Document document : studentsWithinAgeRange) {
            System.out.println(document);
        }
    }

    public static void testUpdateGender() {
        crudServices.updatedGenderOfSpecificStudents();
    }


    public static void datetimeInGMT() {
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

    public void test2() {
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.add(Calendar.MILLISECOND, -currentDateTime.getTimeZone().getOffset(currentDateTime.getTimeInMillis()));
        Date date2 = currentDateTime.getTime();
        System.out.println("Current GMT time in India :: " + date2); //Fri Mar 29 20:57:30 IST 2024 (GMT time hain ye)

    }
}
