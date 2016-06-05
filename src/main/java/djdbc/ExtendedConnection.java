package djdbc;

import java.sql.Connection;

final class ExtendedConnection {
  final Connection jdbcConnection;
  final PreparedStatementCache preparedStatementCache;
  ExtendedConnection(Connection jdbcConnection) {
    this.jdbcConnection = jdbcConnection;
    this.preparedStatementCache = new PreparedStatementCache();
  }
}
