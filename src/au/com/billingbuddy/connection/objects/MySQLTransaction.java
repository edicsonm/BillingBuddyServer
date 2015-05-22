package au.com.billingbuddy.connection.objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.interfaces.IMySQLTransaction;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;

public class MySQLTransaction implements IMySQLTransaction {
	
	private Connection connection;
	
	public MySQLTransaction() throws MySQLTransactionException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					ConfigurationSystem.getKey("urlConnection"),
					ConfigurationSystem.getKey("userDB"),
					ConfigurationSystem.getKey("passwordDB"));
			connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}
	
	public void start() throws MySQLTransactionException {
		try {
			this.connection.setAutoCommit(false);
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}
	
	public void autoCommit(boolean autoCommit ) throws MySQLTransactionException {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}

	public void undo() throws MySQLTransactionException {
		try {
			this.connection.rollback();
			this.connection.close();
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}
	
	public void rollback() throws MySQLTransactionException {
		try {
			System.out.println("Rollback sobre la transaccion ... ");
			this.connection.rollback();
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}

	public void end() throws MySQLTransactionException {
		try {
			this.connection.commit();
			this.connection.close();
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}
	
	public void commit() throws MySQLTransactionException {
		try {
			System.out.println("Commit sobre la transaccion ... ");
			this.connection.commit();
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}
	
	public void close() throws MySQLTransactionException {
		try {
			this.connection.close();
		} catch (SQLException e) {
			MySQLTransactionException exception = new MySQLTransactionException(e);
			throw exception;
		}
	}

	public Connection getConection() throws MySQLTransactionException {
		return this.connection;
	}

	
}
