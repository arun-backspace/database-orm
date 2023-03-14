package com.backspace.nosql;

import java.util.List;

import org.bson.Document;

public abstract class NoSQLRepository {

	abstract void save(Object data);

	abstract Object findById(String id);

	abstract List<Object> findAll();

	abstract Document findOne(String collectionName, Document query);

	abstract void insertOne(String collectionName, Document document);

	abstract void updateOne(String collectionName, Document query, Document update);

	abstract void deleteOne(String collectionName, Document query);

	abstract List<Document> findAll(String collectionName, Document query);
}
