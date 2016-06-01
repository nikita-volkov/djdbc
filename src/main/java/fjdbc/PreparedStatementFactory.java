package fjdbc;

import java.sql.*;

interface PreparedStatementFactory {
  PreparedStatement create(String sql) throws SQLException;
  final class Standard implements PreparedStatementFactory {
    private final Connection connection;
    public Standard(Connection connection) {this.connection = connection;}
    @Override
    public PreparedStatement create(String sql) throws SQLException {
      return connection.prepareStatement(sql);
    }
  }
  final class GeneratedKeys implements PreparedStatementFactory {
    private final Connection connection;
    public GeneratedKeys(Connection connection) {this.connection = connection;}
    @Override
    public PreparedStatement create(String sql) throws SQLException {
      return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    }
  }
}
