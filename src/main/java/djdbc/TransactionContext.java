package djdbc;

import java.sql.SQLException;

public final class TransactionContext {
  private final ExtendedConnection connection;
  TransactionContext(ExtendedConnection connection) {this.connection = connection;}
  /**
   * Execute another transaction as part of the current transaction.
   */
  public <params, result> result execute(Transaction<params, result> transaction, params params) throws SQLException {
    return transaction.run(this, params);
  }
  public <params, result> result execute(Statement<params, result> statement, params params) throws SQLException {
    return statement.run(connection, params);
  }
  public <result> result execute(Statement<Void, result> statement) throws SQLException {
    return execute(statement, null);
  }
}
