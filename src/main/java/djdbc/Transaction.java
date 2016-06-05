package djdbc;

import java.sql.SQLException;

/**
 * A composable transaction.
 * It is abstracted away from the notion of database connections or
 * any other kind of resources.
 * All you deal with is the structure of the transaction and that's it.
 * It also abstracts away from the concurrent transaction conflicts,
 * also known as serialization errors,
 * by automatically retrying the transaction when they arise.
 *
 * @param <params> The input parameters
 * @param <result> The result
 */
public interface Transaction<params, result> {
  /**
   * The isolation level of this transaction.
   *
   * If this transaction encapsulates other transactions,
   * it is recommended to use
   * {@link TransactionIsolation#max(TransactionIsolation, TransactionIsolation...)}
   * to merge the isolation levels of all nested transactions
   * by picking the highest.
   */
  TransactionIsolation getIsolation();
  /**
   * The body of the transaction.
   * The implementation of this method must not contain any
   * effects except for those that the {@code context} parameter provides.
   * The reason is that the code in this method may get executed multiple times
   * in case concurrent transaction conflicts arise
   * (e.g., SQL State "40001" of PostgreSQL).
   */
  result run(TransactionContext context, params params) throws SQLException;

  /**
   * A helper class for construction of transactions from
   * an iterable of void statements.
   */
  final class VoidStatements implements Transaction<Void, Void> {
    private final TransactionIsolation isolation;
    private final Iterable<Statement<Void, Void>> statements;
    public VoidStatements(TransactionIsolation isolation, Iterable<Statement<Void, Void>> statements) {
      this.isolation = isolation;
      this.statements = statements;
    }
    @Override
    public TransactionIsolation getIsolation() {
      return isolation;
    }
    @Override
    public Void run(TransactionContext context, Void aVoid) throws SQLException {
      for (Statement<Void, Void> statement : statements) {
        context.execute(statement);
      }
      return null;
    }
  }

  /**
   * A helper for construction of transactions with the "serializable" isolation level.
   */
  abstract class Serializable<params, result> implements Transaction<params, result> {
    @Override
    final public TransactionIsolation getIsolation() {
      return TransactionIsolation.serializable;
    }
  }

}
