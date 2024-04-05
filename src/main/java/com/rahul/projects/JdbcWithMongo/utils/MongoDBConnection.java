package com.rahul.projects.JdbcWithMongo.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    public static MongoDatabase getDBConnection() {
        MongoClient mongoClient;
        MongoDatabase database = null;
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("college_db");
            return database;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return database;
    }
}
