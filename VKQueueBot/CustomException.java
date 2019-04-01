public class CustomException extends Exception {
  String message;

  CustomException(String str) {
    message = str;
  }

  public String toString() {
    return ("Custom Exception Occurred: " + message);
  }
}
