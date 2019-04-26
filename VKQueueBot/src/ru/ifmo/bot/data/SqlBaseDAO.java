package ru.ifmo.bot.data;

import ru.ifmo.bot.config.PrivateDataStorage;
import ru.ifmo.bot.data.exception.DAOException;
import ru.ifmo.bot.domain.entities.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlBaseDAO {

  private static final String SRV_URL = String.format("jdbc:mysql://%s/%s?serverTimezone=Europe/Moscow",
          PrivateDataStorage.accessData.get("dataBaseAddr"),PrivateDataStorage.accessData.get("dataBaseName"));

  private static final String SQL_GET = "SELECT * FROM queue_main";
  private static final String SQL_INSERT = "INSERT INTO queue_main(ID) VALUES (?)";
  private static final String SQL_DELETE = "DELETE FROM queue_main WHERE ID=?";

  private static Connection requestConnection() throws SQLException {
    return DriverManager.getConnection(
            SRV_URL,
            PrivateDataStorage.accessData.get("dataBaseLogin"),
            PrivateDataStorage.accessData.get("dataBasePassword")
    );
  }

  public List<String> getFromDB() throws DAOException {
    try (Connection conn = requestConnection();

         PreparedStatement ps = conn.prepareStatement(SQL_GET);
         ResultSet rs = ps.executeQuery()) {
      List<String> queue = new ArrayList<>();
      while (rs.next()) {
        queue.add(rs.getString("ID"));
      }
      return queue;
    } catch (SQLException e) {
      throw new DAOException("Проблема при вставке в базу данных");
    }
  }

  public void insertIntoDB(Person person) throws DAOException {
    try (Connection conn = requestConnection();
         PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
      ps.setLong(1, person.id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DAOException("Проблема при вставке в базу данных");
    }
  }


  public void deleteFromDB(Person person) throws DAOException {
    try (Connection conn = requestConnection();
         PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
      ps.setLong(1, person.id);
      ps.executeUpdate();

    } catch (SQLException e) {
      throw new DAOException("Проблема при удалении из базы данных");
    }
  }

}
