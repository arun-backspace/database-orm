package com.backspace.rdbms;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/test")
public class Controller {

	@Inject
	Repo repo;

	@POST
	@Transactional
	public Response postRecord(Gift gift) {
		repo.persist(gift);
		return Response.ok().build();
	}

	@GET
	public Response getRecord() {
		return Response.ok(repo.findAll().list()).build();
	}
}
