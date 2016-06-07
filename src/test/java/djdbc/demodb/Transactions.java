package djdbc.demodb;

import fj.*;
import djdbc.*;

import java.lang.Void;
import java.math.BigDecimal;
import java.sql.SQLException;

import static fj.P.p;

final class Transactions {

  static final Transaction<Void, Void> createSchema =
    new Transaction.Serializable<Void, Void>() {
      @Override
      public Void run(TransactionContext context, Void aVoid) throws SQLException {
        context.execute(Statements.createAccountTable);
        return null;
      }
    };

  static final Transaction<Void, Void> dropSchema =
    new Transaction.Serializable<Void, Void>() {
      @Override
      public Void run(TransactionContext context, Void aVoid) throws SQLException {
        context.execute(Statements.dropAccountTable);
        return null;
      }
    };

  static final Transaction<P3<Integer, Integer, BigDecimal>, Void> transfer =
    new Transaction.Serializable<P3<Integer, Integer, BigDecimal>, Void>() {
      @Override
      public Void run(TransactionContext context, P3<Integer, Integer, BigDecimal> params) throws SQLException {
        context.execute(Statements.modifyBalance, p(params._1(), params._3().negate()));
        context.execute(Statements.modifyBalance, p(params._2(), params._3()));
        return null;
      }
    };

  /**
   * Needed for simulation of serialization conflicts.
   */
  static final Transaction<P2<Integer, Integer>, Void> conflictSimulation =
    new Transaction<P2<Integer, Integer>, Void>() {
      @Override
      public TransactionIsolation getIsolation() {
        return transfer.getIsolation();
      }
      @Override
      public Void run(TransactionContext context, P2<Integer, Integer> params) throws SQLException {
        for (int i = 0; i < 100; i++) {
          context.execute(transfer, p(params._1(), params._2(), new BigDecimal(1)));
        }
        return null;
      }
    };

}
