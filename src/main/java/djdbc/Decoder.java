package djdbc;

import java.sql.*;
import java.sql.Statement;

public interface Decoder<result> {
  PreparedStatementFactory getPreparedStatementFactory(Connection connection);
  void execute(Statement statement, String sql) throws SQLException;
  result decode(Statement statement) throws SQLException;

  Decoder<Void> noResult =
    new Decoder<Void>() {
      @Override
      public PreparedStatementFactory getPreparedStatementFactory(Connection connection) {
        return new PreparedStatementFactory.Standard(connection);
      }
      @Override
      public void execute(Statement statement, String sql) throws SQLException {
        statement.execute(sql);
      }
      @Override
      public Void decode(Statement statement) throws SQLException {
        return null;
      }
    };

  Decoder<Integer> rowsAffected =
    new Decoder<Integer>() {
      @Override
      public PreparedStatementFactory getPreparedStatementFactory(Connection connection) {
        return new PreparedStatementFactory.Standard(connection);
      }
      @Override
      public void execute(Statement statement, String sql) throws SQLException {
        statement.execute(sql);
      }
      @Override
      public Integer decode(Statement statement) throws SQLException {
        return statement.getUpdateCount();
      }
    };

  abstract class Rows<result> implements Decoder<result> {
    @Override
    public PreparedStatementFactory getPreparedStatementFactory(Connection connection) {
      return new PreparedStatementFactory.Standard(connection);
    }
    @Override
    final public void execute(Statement statement, String sql) throws SQLException {
      statement.execute(sql);
    }
    @Override
    final public result decode(Statement statement) throws SQLException {
      final ResultSet resultSet = statement.getResultSet();
      try {
        return run(resultSet);
      } finally {
        resultSet.close();
      }
    }
    public abstract result run(ResultSet resultSet) throws SQLException;
  }

  abstract class GeneratedKeys<result> implements Decoder<result> {
    @Override
    public PreparedStatementFactory getPreparedStatementFactory(Connection connection) {
      return new PreparedStatementFactory.GeneratedKeys(connection);
    }
    @Override
    final public void execute(Statement statement, String sql) throws SQLException {
      statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
    }
    @Override
    final public result decode(Statement statement) throws SQLException {
      final ResultSet resultSet = statement.getGeneratedKeys();
      try {
        return run(resultSet);
      } finally {
        resultSet.close();
      }
    }
    public abstract result run(ResultSet resultSet) throws SQLException;
  }

}
