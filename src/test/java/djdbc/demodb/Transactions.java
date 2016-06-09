package djdbc.demodb;

import fj.*;
import djdbc.*;

import java.lang.Void;
import java.math.BigDecimal;
import java.sql.SQLException;

import static fj.P.p;

final class Transactions {

  static final Transaction<Void, Void> createSchema =
    new Transaction<Void, Void>() {
      @Override
      public TransactionIsolation getIsolation() {
        return TransactionIsolation.serializable;
      }
      @Override
      public Void run(TransactionContext context, Void aVoid) throws SQLException {
        context.execute(Statements.createAccountTable);
        return null;
      }
    };

  static final Transaction<Void, Void> dropSchema =
    new Transaction<Void, Void>() {
      @Override
      public TransactionIsolation getIsolation() {
        return TransactionIsolation.serializable;
      }
      @Override
      public Void run(TransactionContext context, Void aVoid) throws SQLException {
        context.execute(Statements.dropAccountTable);
        return null;
      }
    };

  static final Transaction<P3<Integer, Integer, BigDecimal>, Void> transfer =
    new Transaction<P3<Integer, Integer, BigDecimal>, Void>() {
      @Override
      public TransactionIsolation getIsolation() {
        return TransactionIsolation.serializable;
      }
      @Override
      public Void run(TransactionContext context, P3<Integer, Integer, BigDecimal> params) throws SQLException {
        context.execute(Statements.modifyBalance, p(params._1(), params._3().negate()));
        context.execute(Statements.modifyBalance, p(params._2(), params._3()));
        return null;
      }
    };

  /**
   * Quite far-fetched, but needed for simulation of serialization conflicts, for testing.
   */
  static final Transaction<P4<Integer, Integer, BigDecimal, Integer>, Void> transferMultipleTimes =
    new Transaction<P4<Integer, Integer, BigDecimal, Integer>, Void>() {
      @Override
      public TransactionIsolation getIsolation() {
        return transfer.getIsolation();
      }
      @Override
      public Void run(TransactionContext context, P4<Integer, Integer, BigDecimal, Integer> params) throws SQLException {
        for (int i = 0; i < params._4(); i++) {
          context.execute(transfer, p(params._1(), params._2(), params._3()));
        }
        return null;
      }
    };

}
