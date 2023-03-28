package com.backspace.nosql;

import java.util.List;

import org.bson.Document;

public interface NoSQLRepository {

	public abstract void save(Object data);

	public abstract Object findById(String id);

	public abstract List<Object> findAll();

	public abstract Document findOne(String collectionName, Document query);

	public abstract void insertOne(String collectionName, Document document);

	public abstract void updateOne(String collectionName, Document query, Document update);

	public abstract void deleteOne(String collectionName, Document query);

	public abstract List<Document> findAll(String collectionName, Document query);
}
