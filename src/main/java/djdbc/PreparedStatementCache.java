package djdbc;

import java.sql.*;
import java.util.HashMap;

final class PreparedStatementCache {

  private final HashMap<String, PreparedStatement> map;

  PreparedStatementCache() {
    this.map = new HashMap<String, PreparedStatement>();
  }

  PreparedStatement get(String sql, PreparedStatementFactory preparedStatementFactory) throws SQLException {
    final PreparedStatement cached = map.get(sql);
    if (cached == null) {
      final PreparedStatement created = preparedStatementFactory.create(sql);
      map.put(sql, created);
      return created;
    } else {
      return cached;
    }
  }

}
