import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {

    DataBaseLogic Base = new DataBaseLogic();

    VKAPI myVk = new VKAPI();
    while (true) {
      try {
        ArrayList<Message> newMessages = myVk.getNewMessages();
        for (Message message : newMessages) {
          System.out.println(message.message);
          System.out.println(message.from);

          if (message.message.equals("/jqueue")) {
            ArrayList<String> queue = Base.DBGet();
            for (String s : queue) {
              System.out.println(s);
            }
            myVk.sendCurrentQueue(queue, "Show");
          } else if (message.message.equals("/jenqueue")) {
            Base.DBInsert(message.from);
            ArrayList<String> queue = Base.DBGet();
            for (String s : queue) {
              System.out.println(s);
            }
            myVk.sendCurrentQueue(queue, "Add");
          } else if (message.message.equals("/jdequeue")) {

            ArrayList<String> queue = Base.DBGet();
            String delete="";
            for (String s : queue) {
              delete = s;
            }
            Base.DBDel(delete);
            queue = Base.DBGet();
            myVk.sendCurrentQueue(queue, "Del");
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }
}
