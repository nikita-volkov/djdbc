package fjdbc;

import java.sql.*;

public interface Encoder<params> {

  void encodeParams(PreparedStatement preparedStatement, params params) throws SQLException;

  Encoder<Void> noParams =
    new Encoder<Void>() {
      @Override
      public void encodeParams(PreparedStatement preparedStatement, Void aVoid) throws SQLException {

      }
    };

}
