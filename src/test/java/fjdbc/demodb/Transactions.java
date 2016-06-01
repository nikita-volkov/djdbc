package fjdbc.demodb;

import fjdbc.*;

import java.math.BigDecimal;
import java.sql.SQLException;

import static fj.P.p;

public final class Transactions {

  public static final Transaction<Void> createSchema =
    new Transaction<Void>() {
      @Override
      public Void run(TransactionContext context) throws SQLException {
        context.execute(Statements.createAccountTable);
        return null;
      }
    };

  public static final Transaction<Void> dropSchema =
    new Transaction<Void>() {
      @Override
      public Void run(TransactionContext context) throws SQLException {
        context.execute(Statements.dropAccountTable);
        return null;
      }
    };

  public static Transaction<Void> transfer(final int sourceID, final int targetID, final BigDecimal amount) {
    return new Transaction<Void>() {
      @Override
      public Void run(TransactionContext context) throws SQLException {
        context.execute(Statements.modifyBalance, p(sourceID, amount.negate()));
        context.execute(Statements.modifyBalance, p(targetID, amount));
        return null;
      }
    };
  }

  /**
   * Needed for simulation of serialization conflicts.
   */
  public static Transaction<Void> conflictSimulation(final int sourceID, final int targetID) {
    return new Transaction<Void>() {
      @Override
      public Void run(TransactionContext context) throws SQLException {
        for (int i = 0; i < 100; i++) {
          context.execute(transfer(sourceID, targetID, new BigDecimal(1)));
        }
        return null;
      }
    };
  }


}
