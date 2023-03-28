package com.backspace.repo;

import javax.enterprise.context.ApplicationScoped;

import com.backspace.model.Case;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class CaseRepository implements PanacheRepositoryBase<Case, Long> {

}
