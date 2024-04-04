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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CrudServices {

    private final MongoDatabase database;

    public CrudServices() {
        database = MongoDBConnection.getDBConnection();
    }


    public void findRecordsWithMultipleCondition(String dateStr) throws ParseException {
        MongoCollection<Document> collection = database.getCollection("students");
        List<Document> criteriaList = new ArrayList<>();  //Updated :: Command failed with error 2 (BadValue): '$and must be an array'
        criteriaList.add(new Document("gender", new Document("$eq","Male")));
        criteriaList.add(new Document("age", new Document("$gt", 25)));

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").parse(dateStr);
        Document query1 = new Document("created_on",new Document("$lt", date));
        Document query2 = new Document("last_updated_on",new Document("$lt", date));
        List<Document> dateCriteriaLists = new ArrayList<>();
        dateCriteriaLists.add(query1);
        dateCriteriaLists.add(query2);
        criteriaList.add(new Document("$and", dateCriteriaLists));

        Document finalQueryWithAND = new Document();
        finalQueryWithAND.put("$and",criteriaList);

        final FindIterable<Document> documents = collection.find(finalQueryWithAND);

        System.out.println("== Records with age > 25 and gender 'Male' And CreationDate or UpdatedDate > 2024-02-25 09:28:04.401 UTC ==");
        try (final MongoCursor<Document> cursorIterator = documents.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }

    }


    //Older records created before the given dates
    public void findStudentCreatedOnBeforeGivenDates(String dateStr) throws ParseException {
        //creation_date : 2024-03-21T18:30:00.000+00:00
        String expDate = "2024-02-25 09:28:04.401 UTC";
        MongoCollection<Document> collection = database.getCollection("students");

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").parse(expDate);
        System.out.println("Date :: "+date);

        Document query = new Document();
        query.put("created_on", new Document("$lte", date));

        final FindIterable<Document> cursor = collection.find(query);
        System.out.println("===== Records created on before date :: "+date);
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }

    }

    //Older records last updated before the given dates
    public void findStudentLastUpdatedOnBeforeGivenDates(String dateStr) throws ParseException {
        //creation_date : 2024-03-21T18:30:00.000+00:00
        String expDate = "2024-02-25 09:28:04.401 UTC";
        MongoCollection<Document> collection = database.getCollection("students");

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ").parse(expDate);
        System.out.println("Date :: "+date);

        Document query = new Document();
        query.put("last_updated_on", new Document("$lte", date));

        final FindIterable<Document> cursor = collection.find(query);
        System.out.println("====== Records last updated on before date :: "+expDate);
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
            }
        }

    }



    public void saveStudent(Map<String, Object> record) {

        MongoCollection<Document> collection = database.getCollection("students");
        Document document = new Document();
        document.put("firstname", record.get("firstname"));
        document.put("lastname", record.get("lastname"));
        document.put("age", record.get("age"));
        document.put("gender", record.get("gender"));

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

        document.put("created_on", new Timestamp(System.currentTimeMillis()));
        document.put("last_updated_on", new Timestamp(System.currentTimeMillis()));
        final InsertOneResult insertOneResult = collection.insertOne(document);
        System.out.println("ID of record inserted :: " + insertOneResult.getInsertedId());

    }

    public void updatedGenderOfSpecificStudents() {
        MongoCollection<Document> collection = database.getCollection("students");

        //db.COLLECTION_NAME.update({SELECTION_CRITERIA}, {$set:{UPDATED_DATA}}

        //db.contributor.find({name: {$in: ["Amit", "Suman"]}}).pretty()

        Document selectionCriteria = new Document();
        selectionCriteria.put("firstname", new Document("$in", List.of("Kamini", "Sonali")));

        Document updateQuery = new Document();
        updateQuery.put("$set", new Document("gender", "Female"));

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

        Document updateObject = new Document();
        updateObject.put("$set", new Document("student_name",newName));

        final UpdateResult updateResult = collection.updateOne(query, updateObject);
        return updateResult;
    }

    public UpdateResult updateManyStudentsByName(String oldName, String newName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document selectionCriteria = new Document();
        selectionCriteria.put("student_name", oldName);

        Document updateObject = new Document();
        updateObject.put("$set", new Document("student_name",newName));

        final UpdateResult updateResult = collection.updateMany(selectionCriteria, updateObject);

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

    public void deleteStudentsByName(String studentName) {
        MongoCollection<Document> collection = database.getCollection("students");
        Document searchQuery = new Document();
        searchQuery.put("student_name", studentName);

        collection.deleteOne(searchQuery);
    }

}
