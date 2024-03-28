package com.rahul.projects.JdbcWithMongo;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.rahul.projects.JdbcWithMongo.services.CrudServices;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class App {
    public static void main(String[] args) {

          final CrudServices crudServices = new CrudServices();
//          for(int i=1; i<=50; i++) {
//             crudServices.saveStudent("Rahul"+i, 51+i,"Ranchi"+i,"Jharkhand"+i,"Mother"+i,"Father"+i);
//          }
         //System.out.println("Saved :: "+savedRecord);


       //final UpdateResult updateResult = crudServices.updateManyStudentsByName("Rahul", "SharmaJi");
        //System.out.println("Number of records updated :: "+updateResult.getModifiedCount());


        //crudServices.findStudentsByName("Rahul");
        //2024-02-06T19:12:59.997+00:00

        Date date = new Date("2024-02-06T19:12:59.997+00:00");

        // getting the timestamp object
        Timestamp ts = new Timestamp(date.getTime());

        // using SimpleDateFormat class,we can format the
        // time-stamp according to ourselves
        // getting the timestamp upto sec
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        sdf.format(ts);
        crudServices.findStudentCreatedOnGivenDates(ts);


    }
}
