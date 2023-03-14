package com.backspace.nosql;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDBRepository extends NoSQLRepository {

	private MongoDatabase database;

	@Inject
	public MongoDBRepository(MongoDatabase database) {
		this.database = database;
	}

	public Document findOne(String collectionName, Document query) {
		// Query the database
		database.getCollection(collectionName).find(query);
		return null;
	}

	public void insertOne(String collectionName, Document document) {
		// Insert a document into the database
		database.getCollection(collectionName).insertOne(document);
	}

	public void updateOne(String collectionName, Document query, Document update) {
		// Update a document in the database
		database.getCollection(collectionName).updateOne(query, update);
	}

	public void deleteOne(String collectionName, Document query) {
		// Delete a document from the database
		database.getCollection(collectionName).deleteOne(query);
	}

	public List<Document> findAll(String collectionName, Document query) {

		List<Document> dataList = new ArrayList<>();
		MongoCursor<Document> cursor = database.getCollection(collectionName).find().cursor();
		while (cursor.hasNext()) {
			dataList.add(cursor.next());
		}
		return dataList;
	}

	@Override
	void save(Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	Object findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	List<Object> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
