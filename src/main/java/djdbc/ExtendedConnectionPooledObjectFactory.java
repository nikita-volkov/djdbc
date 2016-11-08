package djdbc;

import org.apache.commons.pool2.*;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.DriverManager;

final class ExtendedConnectionPooledObjectFactory extends BasePooledObjectFactory<ExtendedConnection> {
  private final String url;
  private final String username;
  private final String password;
  ExtendedConnectionPooledObjectFactory(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }
  @Override
  public ExtendedConnection create() throws Exception {
    return new ExtendedConnection(DriverManager.getConnection(url, username, password));
  }
  @Override
  public void destroyObject(PooledObject<ExtendedConnection> p) throws Exception {
    p.getObject().jdbcConnection.close();
  }
  @Override
  public PooledObject<ExtendedConnection> wrap(ExtendedConnection connection) {
    return new DefaultPooledObject<ExtendedConnection>(connection);
  }
}