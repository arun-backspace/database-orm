package com.backspace.rdbms;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class Repo implements PanacheRepositoryBase<Gift, Long> {

}
