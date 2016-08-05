package djdbc;

import java.sql.*;

public interface Encoder<params> {

  void encodeParams(PreparedStatement preparedStatement, params params) throws SQLException;

  class NoParams implements Encoder<Void> {
    public static final NoParams i = new NoParams();
    @Override
    public void encodeParams(PreparedStatement preparedStatement, Void aVoid) throws SQLException {}
  }

}
