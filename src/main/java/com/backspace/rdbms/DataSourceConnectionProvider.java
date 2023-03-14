package com.backspace.rdbms;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public class DataSourceConnectionProvider implements ConnectionProvider {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;

	public DataSourceConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public boolean isUnwrappableAs(Class aClass) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> aClass) {
		return null;
	}

	
}
