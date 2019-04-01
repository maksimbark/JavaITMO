import java.sql.*;
import java.util.ArrayList;

public class DataBaseLogic {
  Connection conn = null;

  private void DBConnect() throws SQLException {

    try {
      conn = DriverManager.getConnection(
              "jdbc:mysql://your.server.ru/dbName?serverTimezone=Europe/Moscow",
              "login", "password");

      if (conn == null) {
        System.out.println("Нет соединения с БД!");
        System.exit(0);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<String> DBGet() throws SQLException {
    ArrayList<String> queue = new ArrayList<String>();
    try {


      DBConnect();

      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM queue_main");



      while (rs.next()) {
        queue.add(rs.getString("ID"));
      }

      /**
       * stmt.close();
       * При закрытии Statement автоматически закрываются
       * все связанные с ним открытые объекты ResultSet
       */
      stmt.close();

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
    return queue;
  }

  public void DBInsert(Long ID) throws SQLException {
    try {
      DBConnect();
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("INSERT INTO queue_main(ID) VALUES ('"+ID.toString()+"')");

      /**
       * stmt.close();
       * При закрытии Statement автоматически закрываются
       * все связанные с ним открытые объекты ResultSet
       */
      stmt.close();

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }

  public void DBDel(String ID) throws SQLException {
    try {
      DBConnect();
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("DELETE FROM queue_main WHERE ID='"+ID+"'");


      /**
       * stmt.close();
       * При закрытии Statement автоматически закрываются
       * все связанные с ним открытые объекты ResultSet
       */
      stmt.close();

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }

}
