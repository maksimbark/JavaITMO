package ru.ifmo.bot.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.ifmo.bot.config.PrivateDataStorage;
import ru.ifmo.bot.data.exception.DAOException;
import ru.ifmo.bot.domain.entities.Message;

import java.util.ArrayList;
import java.util.List;

import java.net.URLEncoder;

class VKAPI {

  private final String baseURL = "https://api.vk.com/method/";
  private final String usersGetMethod = "users.get?";
  private final String messagesSendMethod = "messages.send?";
  private final String getLongPollMethod = "groups.getLongPollServer?";
  private final String accessToken = PrivateDataStorage.accessData.get("accessToken");
  private final String peerID = PrivateDataStorage.accessData.get("peer_id");

  private String server;
  private String key;
  private String ts;

  VKAPI() {

    try {
      getLongPoll();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @NotNull

  private JSONObject JsonParse(String names) throws Exception {
    JSONParser parser = new JSONParser();

    Object myParser = parser.parse(names);
    JSONObject jsonObj = (JSONObject) myParser;
    JSONObject responseVK = (JSONObject) jsonObj.get("error");
    if (responseVK != null) {
      throw new DAOException("Vk returned ERROR: " + responseVK.get("error_msg"));
    }
    return jsonObj;

  }

  private String sendGet(String url) throws Exception {


    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // optional default is GET
    con.setRequestMethod("GET");

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    return response.toString();

  }

  private void getLongPoll() throws Exception {
    String url = baseURL + getLongPollMethod + "access_token=" + accessToken + "&group_id=61469205&v=5.92";
    String response = sendGet(url);
    //parse JSON
    JSONObject jsonObj = JsonParse(response);
    JSONObject responseVK = (JSONObject) jsonObj.get("response");
    server = (String) responseVK.get("server");
    key = (String) responseVK.get("key");
    ts = (String) responseVK.get("ts");

    System.out.println("LongPoll server saved successfully");

  }

  ArrayList<Message> getNewMessages() throws Exception {

    String url = server + "?act=a_check&key=" + key + "&ts=" + ts + "&wait=25";
    String response = sendGet(url);
    JSONObject jsonObj = JsonParse(response);
    JSONObject responseVK = (JSONObject) jsonObj.get("failed");
    if (responseVK != null) {
      getLongPoll();
      return new ArrayList<Message>();
    }
    ts = (String) jsonObj.get("ts");
    System.out.println("new ts: " + ts);

    List<JSONObject> messages = (List<JSONObject>) jsonObj.get("updates");
    //myParser = parser.parse(();
    ArrayList<Message> forReturn = new ArrayList<Message>();

    for (JSONObject currMsg : messages) {
      System.out.println(currMsg);
      JSONObject messageObject = (JSONObject) currMsg.get("object");
      Message message = new Message();
      message.message = (String) messageObject.get("text");
      message.from = (Long) messageObject.get("from_id");
      forReturn.add(message);
    }

    return forReturn;
  }

  void sendCurrentQueue(List<String> text, String type) {

    String url = baseURL + usersGetMethod + "access_token=" + accessToken + "&v=5.92&user_ids=";
    for (String t : text) {
      url = url + t + ",";
    }

    try {
      String forSending = "";
      if (type.equals("Show")) {
        forSending = "Текущая очередь: \n";
      } else if (type.equals("Add")) {
        forSending = "Вы добавлены. Текущая очередь: \n";
      } else if (type.equals("Del")) {
        forSending = "Вы удалены. Текущая очередь: \n";
      }

      String names = sendGet(url);
      System.out.println(names);
      JSONObject jsonObj = JsonParse(names);
      List<JSONObject> messages = (List<JSONObject>) jsonObj.get("response");
      for (JSONObject m : messages) {
        String name = (String) m.get("first_name");
        forSending = forSending + (String) m.get("first_name") + " " + (String) m.get("last_name") + "\n";
      }
      url = baseURL + messagesSendMethod + "access_token=" + accessToken + "&v=5.38&peer_id=" + peerID + "&message=" + URLEncoder.encode(forSending);
      sendGet(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}