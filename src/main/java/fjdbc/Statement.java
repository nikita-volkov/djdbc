package fjdbc;

import java.sql.*;

/**
 * Statement specification.
 */
public interface Statement<params, result> {

  result run(ExtendedConnection connection, params params) throws SQLException;

  final class Instances {
    public static <result> Statement<Void, result> nonParametric(final String sql, final Decoder<result> decoder, boolean preparable) {
      if (preparable) {
        return new Statement<Void, result>() {
          @Override
          public result run(ExtendedConnection connection, Void aVoid) throws SQLException {
            final PreparedStatement preparedStatement =
              connection.preparedStatementCache.get(sql, decoder.getPreparedStatementFactory(connection.jdbcConnection));
            preparedStatement.execute();
            return decoder.decode(preparedStatement);
          }
        };
      } else {
        return new Statement<Void, result>() {
          @Override
          public result run(ExtendedConnection connection, Void aVoid) throws SQLException {
            final java.sql.Statement statement = connection.jdbcConnection.createStatement();
            decoder.execute(statement, sql);
            return decoder.decode(statement);
          }
        };
      }
    }
    public static <params, result> Statement<params, result> parametric(final String sql, final Encoder<params> encoder, final Decoder<result> decoder) {
      return new Statement<params, result>() {
        @Override
        public result run(ExtendedConnection connection, params params) throws SQLException {
          final PreparedStatement preparedStatement =
            connection.preparedStatementCache.get(sql, decoder.getPreparedStatementFactory(connection.jdbcConnection));
          encoder.encodeParams(preparedStatement, params);
          preparedStatement.execute();
          return decoder.decode(preparedStatement);
        }
      };
    }
  }
}

