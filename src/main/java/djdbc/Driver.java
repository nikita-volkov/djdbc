package djdbc;

import java.sql.*;

public interface Driver {
  /**
   * Checks whether the exception represents a transaction conflict.
   * Is used for automatic retrying of the conflicting transactions.
   */
  boolean detectTransactionConflict(SQLException exception);

  Driver standard =
    new Driver() {
      /**
       * Implements the test according to the SQL standard.
       * https://en.wikibooks.org/wiki/Structured_Query_Language/SQLSTATE
       */
      @Override
      public boolean detectTransactionConflict(SQLException exception) {
        return exception.getSQLState().equals("40001");
      }
    };
}
