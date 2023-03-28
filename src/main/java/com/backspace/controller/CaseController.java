package com.backspace.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.backspace.model.Case;
import com.backspace.repo.CaseRepository;

@Path("/case")
public class CaseController {

	@Inject
	CaseRepository caseRepository;

	@POST
	@Transactional
	public Response postRecord(Case caseData) {
		caseRepository.persist(caseData);
		return Response.ok().build();
	}

	@GET
	public Response getRecord() {
		return Response.ok(caseRepository.findAll().list()).build();
	}
}
