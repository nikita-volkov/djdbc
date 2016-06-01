package fjdbc.demodb;

import fj.data.Option;
import fjdbc.*;
import fjdbc.drivers.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public final class DB {

  private final Pool pool;

  public DB(Pool pool) {
    this.pool = pool;
  }

  public static DB h2() {
    return new DB(new Pool("jdbc:h2:mem:test", 6, H2Driver.i));
  }

  public static DB postgresql() {
    return new DB(new Pool("jdbc:postgresql:postgres", 6, PostgresqlDriver.i));
  }

  public void createSchema() throws SQLException {
    pool.execute(Transactions.createSchema, TransactionIsolation.serializable);
  }

  public void dropSchema() throws SQLException {
    pool.execute(Transactions.dropSchema, TransactionIsolation.serializable);
  }

  public void transfer(int sourceID, int targetID, BigDecimal amount) throws SQLException {
    pool.execute(Transactions.transfer(sourceID, targetID, amount), TransactionIsolation.serializable);
  }

  public void simulateConflict(int sourceID, int targetID) throws SQLException {
    pool.execute(Transactions.conflictSimulation(sourceID, targetID), TransactionIsolation.serializable);
  }

  public Option<BigDecimal> getBalance(int id) throws SQLException {
    return pool.execute(Statements.getBalance, id);
  }

  public int createAccount(BigDecimal balance) throws SQLException {
    return pool.execute(Statements.createAccount, balance);
  }

  public boolean deleteAccount(int id) throws SQLException {
    return pool.execute(Statements.deleteAccount, id) >= 1;
  }

}
