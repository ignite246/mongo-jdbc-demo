package com.rahul.projects.JdbcWithMongo.services;

import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.rahul.projects.JdbcWithMongo.utils.MongoDBConnection;
import org.bson.Document;

import java.sql.Timestamp;

public class CrudServices {

    private final MongoDatabase database;

    public CrudServices() {
        database = MongoDBConnection.getDBConnection();
    }

    public InsertOneResult saveStudent(String name, Integer roll, String city, String state, String mother, String father) {

        MongoCollection<Document> collection = database.getCollection("students");
        Document document = new Document();
        document.put("student_name", name);
        document.put("student_roll", roll);

        //Address
        final Document addressDetails = new Document();
        addressDetails.put("city", city);
        addressDetails.put("state", state);
        document.put("student_address", addressDetails);

        //Parents
        final Document parentsDetails = new Document();
        parentsDetails.put("mother", mother);
        parentsDetails.put("father", father);
        document.put("parents_details",parentsDetails);

        document.put("creation_date", new Timestamp(System.currentTimeMillis()));
        final InsertOneResult insertOneResult = collection.insertOne(document);
        return insertOneResult;
    }

    public UpdateResult updateVeryFirstStudentByName(String oldName, String newName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document query = new Document();
        query.put("student_name", oldName);

        Document newDocument = new Document();
        newDocument.put("student_name", newName);

        Document updateObject = new Document();
        updateObject.put("$set", newDocument);

        final UpdateResult updateResult = collection.updateOne(query, updateObject);
        return updateResult;
    }

    public UpdateResult updateManyStudentsByName(String oldName, String newName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document query = new Document();
        query.put("student_name", oldName);

        Document newDocument = new Document();
        newDocument.put("student_name", newName);

        Document updateObject = new Document();
        updateObject.put("$set", newDocument);

        final UpdateResult updateResult = collection.updateMany(query, updateObject);

        return updateResult;
    }

    public void findStudentsByName(String studentName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document searchQuery = new Document();
        searchQuery.put("student_name", studentName);
        FindIterable<Document> cursor = collection.find(searchQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }
    }

    public void deleteStudentsByName(String studentName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document searchQuery = new Document();
        searchQuery.put("student_name", studentName);

        collection.deleteOne(searchQuery);
    }


    public void findStudentCreatedOnGivenDates(Timestamp timestamp){
        MongoCollection<Document> collection = database.getCollection("students");
        Document query = new Document();
        query.put("creation_date",timestamp);


        final FindIterable<Document> cursor = collection.find(query);
        System.out.println("Records found :: "+cursor.showRecordId(true));
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }

    }
}
