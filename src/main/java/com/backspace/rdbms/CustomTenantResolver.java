package com.backspace.rdbms;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.backspace.utils.UdmConstants;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;

@PersistenceUnitExtension
@RequestScoped
public class CustomTenantResolver implements TenantResolver {

	@Inject
	RoutingContext routingContext;

	@Override
	public String getDefaultTenantId() {
		// TODO Auto-generated method stub
		return UdmConstants.BACKSPACE.getMessage();
	}

	@Override
	public String resolveTenantId() {
		// TODO Auto-generated method stub
		return routingContext.request().getHeader(UdmConstants.TENANT.getMessage());
	}

}
