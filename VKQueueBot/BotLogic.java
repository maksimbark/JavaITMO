import java.util.ArrayList;
import java.util.List;

public class BotLogic {
  public static void MainLogicStart() {
    GetPrivateData.ini();
    DataBaseLogic Base = new DataBaseLogic();

    VKAPI myVk = new VKAPI();
    while (true) {
      try {
        ArrayList<Message> newMessages = myVk.getNewMessages();
        for (Message message : newMessages) {
          System.out.println(message.message);
          System.out.println(message.from);

          if (message.message.equals("/jqueue")) {
            List<String> queue = Base.DBGet();
            for (String s : queue) {
              System.out.println(s);
            }
            myVk.sendCurrentQueue(queue, "Show");
          } else if (message.message.equals("/jenqueue")) {
            Base.DBInsert(message.from);
            List<String> queue = Base.DBGet();
            for (String s : queue) {
              System.out.println(s);
            }
            myVk.sendCurrentQueue(queue, "Add");
          } else if (message.message.equals("/jdequeue")) {
            List<String> queue = Base.DBGet();
            Base.DBDel(queue.get(0));
            myVk.sendCurrentQueue(Base.DBGet(), "Del");
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
