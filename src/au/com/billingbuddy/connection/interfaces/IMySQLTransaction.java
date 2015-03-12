package au.com.billingbuddy.connection.interfaces;

import java.sql.Connection;

import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;

public interface IMySQLTransaction {
	public void start() throws MySQLTransactionException;
	public void undo() throws MySQLTransactionException;
	public void end() throws MySQLTransactionException;
	public Connection getConection() throws MySQLTransactionException;
}
