package com.rahul.projects.JdbcWithMongo.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.rahul.projects.JdbcWithMongo.utils.MongoDBConnection;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrudServices {

    private final MongoDatabase database;

    public CrudServices() {
        database = MongoDBConnection.getDBConnection();
    }

    public void saveStudent(Map<String, Object> record) {

        MongoCollection<Document> collection = database.getCollection("students");
        Document document = new Document();
        document.put("firstname", record.get("firstname"));
        document.put("lastname", record.get("lastname"));
        document.put("age", record.get("age"));

        //Address
        final Document addressDetails = new Document();
        addressDetails.put("city", record.get("city"));
        addressDetails.put("state", record.get("state"));
        document.put("address", addressDetails);

        //Parents
        final Document parentsDetails = new Document();
        parentsDetails.put("mother", record.get("mother"));
        parentsDetails.put("father", record.get("father"));
        document.put("parents", parentsDetails);

        document.put("creation_date", new Timestamp(System.currentTimeMillis()));
        final InsertOneResult insertOneResult = collection.insertOne(document);
        System.out.println("ID of record inserted :: " + insertOneResult.getInsertedId());

    }

    public void updatedGenderOfSpecificStudents() {
        MongoCollection<Document> collection = database.getCollection("students");

        //db.COLLECTION_NAME.update({SELECTION_CRITERIA}, {$set:{UPDATED_DATA}}

        //db.contributor.find({name: {$in: ["Amit", "Suman"]}}).pretty()

        Document selectionCriteria = new Document();
        selectionCriteria.put("firstname", new Document("$in", List.of("Rahul", "Kundan", "Piyush", "Aman", "Mohit")));

        Document updateQuery = new Document();
        updateQuery.put("$set", new Document("gender", "Male"));

        final UpdateResult updateResult = collection.updateMany(selectionCriteria, updateQuery);
        System.out.println("Updated count :: " + updateResult.getModifiedCount());

    }


    //Fetching students in given age range
    public List<Document> getStudentsWithinAgeRange(Integer lower, Integer higher) {
        MongoCollection<Document> collection = database.getCollection("students");

        List<Document> records = new ArrayList<>();

        Document selectionCriteria = new Document();
        selectionCriteria.put("age", new Document("$gte", lower).append("$lte", higher));

        final FindIterable<Document> cursor = collection.find(selectionCriteria);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                final Document record = cursorIterator.next();
                records.add(record);
            }
        }
        System.out.println("Total records (documents) :: " + records.size());
        return records;
    }

    //Fetching students in created on given date
    public List<Document> getStudentsCreatedOnDate(String creationDate) {
        String string = "2024-03-21T18:30:00.000+00:00";
        final Instant instant = Instant.parse(string);
        System.out.println("Parsed time :: "+instant);

        final List<Document> allStudents = getAllStudents();
        System.out.println("All :: "+allStudents.size());
        System.out.println(allStudents.get(0).get("creation_date"));

        final List<Document> response = allStudents.stream()
                .filter(student ->
                 student.get("creation_date").equals(instant))
                .collect(Collectors.toList());

        System.out.println("Filtered :: "+response.size());
        return response;
    }

    public List<Document> getAllStudents() {

        List<Document> records = new ArrayList<>();

        MongoCollection<Document> collection = database.getCollection("students");
        final MongoCursor<Document> cursorIterator = collection.find().cursor();
        while (cursorIterator.hasNext()) {
            final Document record = cursorIterator.next();
            records.add(record);
        }
        return records;
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
        System.out.println("Records found :: " + cursor.showRecordId(true));

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }
    }

    public void findStudentsRollGreaterThan(Integer roll) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document searchField = new Document();
        Document searchCondition = new Document();
        searchCondition.put("$gte", roll);
        searchField.put("student_roll", searchCondition);

        FindIterable<Document> cursor = collection.find(searchField);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }
    }

    //$gte

    public void deleteStudentsByName(String studentName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document searchQuery = new Document();
        searchQuery.put("student_name", studentName);

        collection.deleteOne(searchQuery);
    }


    public void findStudentCreatedOnGivenDates(Timestamp timestamp) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document query = new Document();
        query.put("creation_date", timestamp);

        final FindIterable<Document> cursor = collection.find(query);
        System.out.println("Records found :: " + cursor.showRecordId(true));
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }

    }
}
