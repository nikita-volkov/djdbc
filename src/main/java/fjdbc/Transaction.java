package fjdbc;

import java.sql.SQLException;

/**
 * A composable transaction.
 * It is abstracted away from the notion of database connections or
 * any other kind of resources.
 * All you deal with is the structure of the transaction and that's it.
 *
 * @param <result> The result
 */
public interface Transaction<result> {
  /**
   * Implementation of the transaction.
   * The implementation of this method must not contain any
   * effects except for those that the {@code context} parameter provides.
   * The reason is that the code in this method may get executed multiple times
   * in case concurrent transaction conflicts arise.
   */
  result run(TransactionContext context) throws SQLException;
}
