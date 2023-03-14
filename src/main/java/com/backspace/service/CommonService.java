package com.backspace.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import com.backspace.rdbms.SchemaDetails;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.util.ExceptionUtil;

@ApplicationScoped
public class CommonService {

	private static final Logger LOGGER = Logger.getLogger(CommonService.class.getName());

	@ConfigProperty(name = "quarkus.datasource.jdbc.url")
	String adminSchemaUrl;

	@ConfigProperty(name = "quarkus.datasource.username")
	String adminSchemaUserName;

	@ConfigProperty(name = "quarkus.datasource.password")
	String adminSchemaPassword;

	public Optional<SchemaDetails> getSchemaDetails(String tenantId) {
		try {
			Connection connection = DriverManager.getConnection(adminSchemaUrl, adminSchemaUserName,
					adminSchemaPassword);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM SchemaDetails WHERE tenant = ?");
			statement.setString(1, tenantId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				SchemaDetails schemaDetails = SchemaDetails.builder().url(resultSet.getString("url"))
						.username(resultSet.getString("username")).password(resultSet.getString("password"))
						.kind(resultSet.getString("kind")).tenant(resultSet.getString("tenant"))
						.nosqlUrl(resultSet.getString("nosqlUrl")).nosqlKind(resultSet.getString("nosqlKind"))
						.nosqlPassword(resultSet.getString("nosqlPassword"))
						.nosqlUsername(resultSet.getString("nosqlUsername")).build();
				Optional<SchemaDetails> optinalSchemaDetails = Optional.ofNullable(schemaDetails);
				return optinalSchemaDetails;
			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, String.join(" :: ", "Exception occured while getting schema details",
					ExceptionUtil.generateStackTrace(e), tenantId));
		}
		return Optional.empty();
	}
}
