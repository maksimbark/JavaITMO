package ru.ifmo.bot.data.exception;

public class DAOException extends Exception {
  String message;

  public DAOException(String str) {
    message = str;
  }

  public String toString() {
    return ("Custom Exception Occurred: " + message);
  }
}
