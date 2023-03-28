package com.backspace.rdbms;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import com.backspace.service.CommonService;
import com.backspace.utils.UdmConstants;
import com.mysql.cj.jdbc.MysqlDataSource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.mariadb.jdbc.MariaDbDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantConnectionResolver;
import io.quarkus.runtime.util.ExceptionUtil;

@PersistenceUnitExtension
@ApplicationScoped
@Transactional
public class CustomConnectionResolver implements TenantConnectionResolver {

	private static final Logger LOGGER = Logger.getLogger(CustomConnectionResolver.class.getName());

	@ConfigProperty(name = "quarkus.datasource.jdbc.url")
	String adminSchemaUrl;

	@ConfigProperty(name = "quarkus.datasource.username")
	String adminSchemaUserName;

	@ConfigProperty(name = "quarkus.datasource.password")
	String adminSchemaPassword;

	private Map<String, ConnectionProvider> connectionProviders = new HashMap<>();

	@Inject
	CommonService commonService;

	@Override
	@ActivateRequestContext
	public ConnectionProvider resolve(String tenantId) {

		if (UdmConstants.BACKSPACE.getMessage().equalsIgnoreCase(tenantId)) {
			LOGGER.info(String.join(" :: ", "Choosing default data source", tenantId));
			SchemaDetails schemaDetails = SchemaDetails.builder().url(adminSchemaUrl).username(adminSchemaUserName)
					.password(adminSchemaPassword).kind("mysql").build();
			Optional<DataSource> dataSource = getDatasource(schemaDetails);
			return new DataSourceConnectionProvider(dataSource.get());
		}
		ConnectionProvider oldConnectionProvider = connectionProviders.get(tenantId);
		if (oldConnectionProvider == null) {
			Connection connection = null;

			try {
//				connection = DriverManager.getConnection(adminSchemaUrl, adminSchemaUserName, adminSchemaPassword);
//				PreparedStatement statement = connection
//						.prepareStatement("SELECT * FROM SchemaDetails WHERE tenant = ?");
//				statement.setString(1, tenantId);
//				ResultSet resultSet = statement.executeQuery();
//				if (resultSet.next()) {
//					SchemaDetails schemaDetails = SchemaDetails.builder().url(resultSet.getString("url"))
//							.username(resultSet.getString("username")).password(resultSet.getString("password"))
//							.kind(resultSet.getString("kind")).build();
//					optinalSchemaDetails = Optional.ofNullable(schemaDetails);
//				}
				Optional<SchemaDetails> optinalSchemaDetails = commonService.getSchemaDetails(tenantId);
				if (optinalSchemaDetails.isEmpty()) {
					LOGGER.info(String.join(" :: ", "No Schema details found exception", tenantId));
				}

				Optional<DataSource> dataSource = getDatasource(optinalSchemaDetails.get());
				if (dataSource.isEmpty()) {
					LOGGER.info(String.join(" :: ", "Data source is empty", tenantId));
				}
				ConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource.get());
				connectionProviders.put(tenantId, connectionProvider);
				return connectionProvider;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, String.join(" :: ", "Exception occured in tenant resolve action",
						ExceptionUtil.generateStackTrace(e), tenantId));
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE,
								String.join(" :: ", "Exception occured while closing admin database connection",
										ExceptionUtil.generateStackTrace(e)));
					}
				}
			}

		}

		return oldConnectionProvider;

	}

	public Optional<DataSource> getDatasource(SchemaDetails schemaDetails) {
		try {

			if (UdmConstants.MARIADB.getMessage().equalsIgnoreCase(schemaDetails.getKind())) {
				MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
				mariaDbDataSource.setUrl(schemaDetails.getUrl());
				mariaDbDataSource.setPassword(schemaDetails.getPassword());
				mariaDbDataSource.setUser(schemaDetails.getUsername());
				return Optional.ofNullable(mariaDbDataSource);
			} else if (UdmConstants.POSTGRESQL.getMessage().equalsIgnoreCase(schemaDetails.getKind())) {
				PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
				pgSimpleDataSource.setUrl(schemaDetails.getUrl());
				pgSimpleDataSource.setPassword(schemaDetails.getPassword());
				pgSimpleDataSource.setUser(schemaDetails.getUsername());
				return Optional.ofNullable(pgSimpleDataSource);
			} else {
				MysqlDataSource mysqlDataSource = new MysqlDataSource();
				mysqlDataSource.setUrl(schemaDetails.getUrl());
				mysqlDataSource.setPassword(schemaDetails.getPassword());
				mysqlDataSource.setUser(schemaDetails.getUsername());
				return Optional.ofNullable(mysqlDataSource);
			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, String.join(" :: ", "Exception occured while creating datasource",
					ExceptionUtil.generateStackTrace(e)));
		}
		return Optional.empty();
	}

}
