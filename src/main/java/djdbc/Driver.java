package djdbc;

import java.sql.*;

/**
 * For known databases the {@link #standard} instance of this interface will suffice.
 * However, you're free to implement your own in cases, when it doesn't.
 */
public interface Driver {
  /**
   * Checks whether the exception represents a transaction conflict.
   * Is used for automatic retrying of the conflicting transactions.
   */
  boolean detectTransactionConflict(SQLException exception);

  /**
   * A standard implementation of the Driver interface,
   * which should do for most databases.
   */
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
