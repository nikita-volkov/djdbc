package djdbc;

import java.io.*;
import java.sql.*;

/**
 * A pool of connections to the database.
 */
public class Pool implements Closeable {

  private final ExtendedConnectionPool pool;
  private final Driver driver;

  /**
   * Instantiate the pool with a custom driver.
   * @param url The URL of the database to establish connections too
   * @param size The size of the pool
   * @param driver A custom implementation of the driver
   */
  public Pool(String url, int size, Driver driver) {
    this.pool = new ExtendedConnectionPool(url, size);
    this.driver = driver;
  }
  /**
   * Instantiate the pool using the standard driver.
   * @param url The URL of the database to establish connections too
   * @param size The size of the pool
   */
  public Pool(String url, int size) {
    this(url, size, Driver.standard);
  }
  /**
   * Execute a transaction,
   * while automatically retrying it in case of a serialization conflict.
   */
  public <params, result> result execute(Transaction<params, result> transaction, params params) throws SQLException {
    ExtendedConnection connection = pool.getConnection();
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
      pool.putConnection(connection);
    }
  }
  /**
   * Execute a transaction, which doesn't take any parameters.
   */
  public <result> result execute(Transaction<Void, result> transaction) throws SQLException {
    return execute(transaction, null);
  }
  /**
   * Execute a single statement.
   */
  public <params, result> result execute(Statement<params, result> statement, params params) throws SQLException {
    ExtendedConnection connection = pool.getConnection();
    try {
      return statement.run(connection, params);
    } finally {
      pool.putConnection(connection);
    }
  }
  /**
   * Execute a single statement, which doesn't take any parameters.
   */
  public <result> result execute(Statement<Void, result> statement) throws SQLException {
    return execute(statement, null);
  }
  /**
   * Close all the resources associated with this pool:
   * all the connections, statements, results-sets and etc.
   */
  @Override
  public void close() throws IOException {
    pool.close();
  }
}
