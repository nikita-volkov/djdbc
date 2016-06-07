package djdbc;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.*;
import java.sql.*;

public class Pool implements Closeable {

  private final GenericObjectPool<ExtendedConnection> pool;
  private final Driver driver;

  public Pool(String url, int size, Driver driver) {
    this.driver = driver;
    this.pool = new GenericObjectPool<ExtendedConnection>(new ExtendedConnectionPoolFactory(url));
    this.pool.setMaxTotal(size);
  }
  private ExtendedConnection getConnection() throws SQLException {
    try {
      return pool.borrowObject();
    } catch (SQLException e) {
      throw e;
    } catch (Exception e) {
      throw new Error("Unexpected exception", e);
    }
  }
  private void putConnection(ExtendedConnection connection) {
    try {
      pool.returnObject(connection);
    } catch (Exception e) {
      throw new Error("Unexpected exception", e);
    }
  }
  public <params, result> result execute(Transaction<params, result> transaction, params params) throws SQLException {
    ExtendedConnection connection = getConnection();
    Connection jdbcConnection = connection.jdbcConnection;
    try {
      final TransactionContext context = new TransactionContext(connection);
      jdbcConnection.setAutoCommit(false);
      jdbcConnection.setTransactionIsolation(transaction.getIsolation().jdbcIsolation);
      while (true) {
        try {
          result result = transaction.run(context, params);
          jdbcConnection.commit();
          jdbcConnection.setAutoCommit(true);
          return result;
        } catch (SQLException e) {
          jdbcConnection.rollback();
          if (!driver.detectTransactionConflict(e)) {
            jdbcConnection.setAutoCommit(true);
            throw e;
          }
        }
      }
    } finally {
      putConnection(connection);
    }
  }
  public <result> result execute(Transaction<Void, result> transaction) throws SQLException {
    return execute(transaction, null);
  }
  public <params, result> result execute(Statement<params, result> statement, params params) throws SQLException {
    ExtendedConnection connection = getConnection();
    try {
      return statement.run(connection, params);
    } finally {
      putConnection(connection);
    }
  }
  public <result> result execute(Statement<Void, result> statement) throws SQLException {
    return execute(statement, null);
  }
  @Override
  public void close() throws IOException {
    pool.close();
  }
}
