package djdbc.demodb;

import fj.data.Option;
import djdbc.*;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;

import static fj.P.p;

public final class DB implements Closeable {

  private final Pool pool = new Pool("jdbc:h2:mem:test", 6);

  public void createSchema() throws SQLException {
    pool.execute(Transactions.createSchema);
  }

  public void dropSchema() throws SQLException {
    pool.execute(Transactions.dropSchema);
  }

  public void transfer(int sourceID, int targetID, BigDecimal amount) throws SQLException {
    pool.execute(Transactions.transfer, p(sourceID, targetID, amount));
  }

  public void simulateConflict(int sourceID, int targetID) throws SQLException {
    pool.execute(Transactions.conflictSimulation, p(sourceID, targetID));
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

  @Override
  public void close() throws IOException {
    pool.close();
  }
}
