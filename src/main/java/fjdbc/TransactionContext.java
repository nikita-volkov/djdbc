package fjdbc;

import java.sql.SQLException;

public final class TransactionContext {
  private final ExtendedConnection connection;
  TransactionContext(ExtendedConnection connection) {this.connection = connection;}
  /**
   * Execute another transaction as part of the current transaction.
   */
  public <result> result execute(Transaction<result> transaction) throws SQLException {
    return transaction.run(this);
  }
  public <params, result> result execute(Statement<params, result> statement, params params) throws SQLException {
    return statement.run(connection, params);
  }
  public <result> result execute(Statement<Void, result> statement) throws SQLException {
    return execute(statement, null);
  }
}
