package djdbc;

import java.sql.*;

public interface Driver {
  void initialize();
  /**
   * Checks whether the exception represents a transaction conflict.
   * Is used for automatic retrying of the conflicting transactions.
   */
  boolean detectTransactionConflict(SQLException exception);
}
