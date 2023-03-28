package com.backspace.nosql;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.backspace.rdbms.SchemaDetails;
import com.backspace.service.CommonService;
import com.backspace.utils.UdmConstants;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class NoSQLRepositoryProducer {

	private static final Logger LOGGER = Logger.getLogger(NoSQLRepositoryProducer.class.getName());

	@Inject
	CommonService commonService;

	@Inject
	RoutingContext routingContext;

	private Map<String, SchemaDetails> schemaDetailsMap = new HashMap<>();

	@Produces
	@RequestScoped
	public NoSQLRepository createNoSQLRepository() {
		String tenantId = routingContext.request().getHeader("tenant");
		SchemaDetails schemaDetails = schemaDetailsMap.get(tenantId);
		if (schemaDetails == null) {
			Optional<SchemaDetails> optionalSchemaDetails = commonService.getSchemaDetails(tenantId);
			if (optionalSchemaDetails.isEmpty()) {
				LOGGER.info(String.join(" :: ", "No SchemaDetails Found", tenantId));
			}
			schemaDetails = optionalSchemaDetails.get();
			schemaDetailsMap.put(tenantId, schemaDetails);
		}

		if (UdmConstants.MONGO.getMessage().equalsIgnoreCase(schemaDetails.getNosqlKind())) {

			ConnectionString connectionString = new ConnectionString(schemaDetails.getNosqlUrl());
			MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
					.applyConnectionString(connectionString)
					.codecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
							CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())))
					.build();
			MongoClient mongoClient = MongoClients.create(mongoClientSettings);
			MongoDatabase database = mongoClient.getDatabase(tenantId);
			return new MongoDBRepository(database);
		} else {
			// To do for different NOSQL DB
			return null;
		}
	}
}
