package fjdbc;

import org.apache.commons.pool2.*;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.DriverManager;

final class ExtendedConnectionPoolFactory extends BasePooledObjectFactory<ExtendedConnection> {
  private final String url;
  ExtendedConnectionPoolFactory(String url) {
    this.url = url;
  }
  @Override
  public ExtendedConnection create() throws Exception {
    return new ExtendedConnection(DriverManager.getConnection(url));
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