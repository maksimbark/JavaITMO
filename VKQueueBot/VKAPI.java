import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import java.net.URLEncoder;

public class VKAPI {

  private String server;
  private String key;
  private String ts;

  public VKAPI() {
    try {
      getLongPoll();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // HTTP GET request
  @NotNull
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
    String url = "https://api.vk.com/method/groups.getLongPollServer?group_id=61469205&access_token=your_token&v=5.92";
    String response = sendGet(url);
    //parse JSON
    JSONParser parser = new JSONParser();

    Object myParser = parser.parse(response);
    JSONObject jsonObj = (JSONObject) myParser;
    JSONObject responseVK = (JSONObject) jsonObj.get("error");
    if (responseVK != null) {
      throw new CustomException("Vk returned ERROR: " + responseVK.get("error_msg"));
    }
    responseVK = (JSONObject) jsonObj.get("response");
    server = (String) responseVK.get("server");
    key = (String) responseVK.get("key");
    ts = (String) responseVK.get("ts");

    System.out.println("LongPoll server saved successfully");

  }

  public ArrayList<Message> getNewMessages() throws Exception {

    String url = server + "?act=a_check&key=" + key + "&ts=" + ts + "&wait=25";
    String response = sendGet(url);

    JSONParser parser = new JSONParser();

    Object myParser = parser.parse(response);
    JSONObject jsonObj = (JSONObject) myParser;
    JSONObject responseVK = (JSONObject) jsonObj.get("error");
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

  public void sendCurrentQueue(ArrayList<String> text, String type) {

    String url = "https://api.vk.com/method/users.get?access_token=your_token&v=5.92&user_ids=";
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

      JSONParser parser = new JSONParser();

      Object myParser = parser.parse(names);
      JSONObject jsonObj = (JSONObject) myParser;
      JSONObject responseVK = (JSONObject) jsonObj.get("error");
      if (responseVK != null) {
        throw new CustomException("Vk returned ERROR: " + responseVK.get("error_msg"));
      }

      List<JSONObject> messages = (List<JSONObject>) jsonObj.get("response");
      for (JSONObject m : messages) {
        String name = (String) m.get("first_name");
        forSending = forSending + (String) m.get("first_name") + " " + (String) m.get("last_name") + "\n";
      }
      url = "https://api.vk.com/method/messages.send?access_token=your_token&v=5.38&peer_id=2000000002&message=" + URLEncoder.encode(forSending);
      sendGet(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
