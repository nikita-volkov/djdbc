package djdbc.demodb;

import fj.*;
import fj.data.*;
import djdbc.*;
import djdbc.Statement;

import java.lang.Void;
import java.math.BigDecimal;
import java.sql.*;

import static fj.P.p;

final class Statements {

  final static Statement<Void, Void> createAccountTable =
    new Statement.Unpreparable<Void>(
      "create table account (id serial not null, balance numeric not null, primary key (id))",
      Decoder.noResult
    );

  final static Statement<Void, Void> dropAccountTable =
    new Statement.Unpreparable<Void>(
      "drop table account",
      Decoder.noResult
    );

  final static Statement<P2<Integer, BigDecimal>, Integer> modifyBalance =
    new Statement.Preparable<P2<Integer, BigDecimal>, Integer>(
      "update account set balance = balance + ? where id = ?",
      new Encoder<P2<Integer, BigDecimal>>() {
        @Override
        public void encodeParams(PreparedStatement preparedStatement, P2<Integer, BigDecimal> params) throws SQLException {
          preparedStatement.setBigDecimal(1, params._2());
          preparedStatement.setInt(2, params._1());
        }
      },
      Decoder.rowsAffected
    );

  final static Statement<Integer, Option<BigDecimal>> getBalance =
    new Statement.Preparable<Integer, Option<BigDecimal>>(
      "select balance from account where id = ?",
      new Encoder<Integer>() {
        @Override
        public void encodeParams(PreparedStatement preparedStatement, Integer params) throws SQLException {
          preparedStatement.setInt(1, params);
        }
      },
      new Decoder.Rows<Option<BigDecimal>>() {
        @Override
        public Option<BigDecimal> run(ResultSet resultSet) throws SQLException {
          if (resultSet.next()) {
            return Option.some(resultSet.getBigDecimal(1));
          } else {
            return Option.none();
          }
        }
      }
    );

  final static Statement<Void, List<P2<Integer, BigDecimal>>> listAccounts =
    new Statement.Preparable<Void, List<P2<Integer, BigDecimal>>>(
      "select id, balance from account",
      Encoder.noParams,
      new Decoder.Rows<List<P2<Integer, BigDecimal>>>() {
        @Override
        public List<P2<Integer, BigDecimal>> run(ResultSet resultSet) throws SQLException {
          List<P2<Integer, BigDecimal>> rows = List.nil();
          while (resultSet.next()) {
            rows = List.cons(p(resultSet.getInt(1), resultSet.getBigDecimal(2)), rows);
          }
          return rows;
        }
      }
    );

  final static Statement<BigDecimal, Integer> createAccount =
    new Statement.Preparable<BigDecimal, Integer>(
      "insert into account (balance) values (?)",
      new Encoder<BigDecimal>() {
        @Override
        public void encodeParams(PreparedStatement preparedStatement, BigDecimal params) throws SQLException {
          preparedStatement.setBigDecimal(1, params);
        }
      },
      new Decoder.GeneratedKeys<Integer>() {
        @Override
        public Integer run(ResultSet resultSet) throws SQLException {
          resultSet.next();
          return resultSet.getInt(1);
        }
      }
    );

  final static Statement<Integer, Integer> deleteAccount =
    new Statement.Preparable<Integer, Integer>(
      "delete from account where id = ?",
      new Encoder<Integer>() {
        @Override
        public void encodeParams(PreparedStatement preparedStatement, Integer integer) throws SQLException {
          preparedStatement.setInt(1, integer);
        }
      },
      Decoder.rowsAffected
    );

}
