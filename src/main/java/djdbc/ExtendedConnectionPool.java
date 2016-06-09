package djdbc;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.*;
import java.sql.SQLException;

/**
 * An adapted wrapper over the API of "commons-pool2".
 * Abstracts over the peculiarities of dealing with it.
 */
final class ExtendedConnectionPool implements Closeable {

  private final GenericObjectPool<ExtendedConnection> pool;

  ExtendedConnectionPool(String url, int size) {
    this.pool = new GenericObjectPool<ExtendedConnection>(new ExtendedConnectionPooledObjectFactory(url));
    this.pool.setMaxTotal(size);
  }

  ExtendedConnection getConnection() throws SQLException {
    try {
      return pool.borrowObject();
    } catch (SQLException e) {
      throw e;
    } catch (Exception e) {
      throw new Error("Unexpected exception", e);
    }
  }

  void putConnection(ExtendedConnection connection) {
    try {
      pool.returnObject(connection);
    } catch (Exception e) {
      throw new Error("Unexpected exception", e);
    }
  }

  @Override
  public void close() throws IOException {
    pool.close();
  }

}
