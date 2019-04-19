package ru.ifmo.bot.domain;

import ru.ifmo.bot.data.SqlBaseDAO;
import ru.ifmo.bot.domain.entities.Message;
import ru.ifmo.bot.domain.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class BotLogic {
  public static void MainLogicStart() {
    SqlBaseDAO Base = new SqlBaseDAO();

    VKAPI myVk = new VKAPI();
    boolean running = true;

    while (running) {
      try {
        ArrayList<Message> newMessages = myVk.getNewMessages();
        for (Message message : newMessages) {
          System.out.println(message.message);
          System.out.println(message.from);

          switch (message.message) {
            case "/jqueue": {
              List<String> queue = Base.getFromDB();
              for (String s : queue) {
                System.out.println(s);
              }
              myVk.sendCurrentQueue(queue, "Show");
              break;
            }
            case "/jenqueue": {
              Base.insertIntoDB(new Person(message.from));
              List<String> queue = Base.getFromDB();
              for (String s : queue) {
                System.out.println(s);
              }
              myVk.sendCurrentQueue(queue, "Add");
              break;
            }
            case "/jdequeue": {
              List<String> queue = Base.getFromDB();
              Base.deleteFromDB(new Person(queue.get(0)));
              myVk.sendCurrentQueue(Base.getFromDB(), "Del");
              break;
            }
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
