package djdbc;

import java.sql.*;
import java.sql.Statement;

/**
 * A specification of how to decode the results of a statement.
 * You shouldn't directly implement this interface,
 * instead use one of the provided final or abstract default implementations.
 */
public interface Decoder<result> {
  PreparedStatementFactory getPreparedStatementFactory(Connection connection);
  void execute(Statement statement, String sql) throws SQLException;
  result decode(Statement statement) throws SQLException;

  /**
   * A decoder for statements, which produce no results.
   */
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

  /**
   * A decoder for statements, which affect multiple rows.
   * Results in the amount of affected rows.
   */
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

  /**
   * A decoder for SELECT statements,
   * which requires the user to specify the result-set parser.
   */
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
    /**
     * Implementation of the rows result-set parser.
     */
    public abstract result run(ResultSet resultSet) throws SQLException;
  }

  /**
   * A decoder for INSERT statements,
   * which have a side-effect of automatically generating values for some columns.
   * Specifies a decoder for those values.
   */
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
    /**
     * Implementation of the generated key result-set parser.
     */
    public abstract result run(ResultSet resultSet) throws SQLException;
  }

}
