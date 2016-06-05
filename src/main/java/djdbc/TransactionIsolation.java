package djdbc;

import java.sql.Connection;

public enum TransactionIsolation {
  readUncommitted(Connection.TRANSACTION_READ_UNCOMMITTED),
  readCommitted(Connection.TRANSACTION_READ_COMMITTED),
  repeatableRead(Connection.TRANSACTION_REPEATABLE_READ),
  serializable(Connection.TRANSACTION_SERIALIZABLE);

  final int jdbcIsolation;
  TransactionIsolation(int jdbcIsolation) {this.jdbcIsolation = jdbcIsolation;}
  /**
   * Picks the maximum isolation level.
   */
  public static TransactionIsolation max(TransactionIsolation head, TransactionIsolation... tail) {
    TransactionIsolation acc = head;
    for (TransactionIsolation current : tail) {
      if (current.compareTo(acc) >= 1) {
        acc = current;
      }
    }
    return acc;
  }
}
