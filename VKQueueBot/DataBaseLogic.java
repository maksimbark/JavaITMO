import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseLogic {
  private static final String SRV_URL = "jdbc:mysql://" + GetPrivateData.accessData.get("dataBaseAddr") + "/" +
          GetPrivateData.accessData.get("dataBaseName") + "?serverTimezone=Europe/Moscow";
  private static final String SQL_GET = "SELECT * FROM queue_main";


  private Connection DBConnect() throws CustomException {
    Connection conn = null;

    try {
      conn = DriverManager.getConnection(
              SRV_URL,
              GetPrivateData.accessData.get("dataBaseLogin"),
              GetPrivateData.accessData.get("dataBasePassword")
      );

      if (conn == null) {
        System.out.println("Нет соединения с БД!");
        throw (new CustomException("Нет соединения с БД!"));
      }


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  public List<String> DBGet() {
    List<String> queue = new ArrayList<String>();
    try {
      Connection conn = DBConnect();

      PreparedStatement ps = conn.prepareStatement(SQL_GET);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        queue.add(rs.getString("ID"));
      }
      ps.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return queue;
  }

  public void DBInsert(long ID) {
    try {
      Connection conn = DBConnect();

      PreparedStatement ps = conn.prepareStatement("INSERT INTO queue_main(ID) VALUES (?)");
      ps.setLong(1, ID);
      ps.executeUpdate();
      ps.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void DBDel(String ID) {
    try {
      Connection conn = DBConnect();
      PreparedStatement ps = conn.prepareStatement("DELETE FROM queue_main WHERE ID=?");
      ps.setString(1, ID);
      ps.executeUpdate();
      ps.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
