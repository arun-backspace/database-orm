package com.backspace.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.backspace.nosql.NoSQLRepository;

import org.bson.Document;

@Path("/nosql")
public class NoSqlController {

	@Inject
	NoSQLRepository noSQLRepository;

	@POST
	public void doSomething() {
		Document dd = new Document();
		dd.append("id", 1);
		noSQLRepository.insertOne("testsql", dd);
		System.out.println(noSQLRepository);
		// noSQLRepository
	}

	@GET
	public List<Document> getSomething() {
		return noSQLRepository.findAll("testsql", null);
		// System.out.println(noSQLRepository);
		// noSQLRepository
	}
}
