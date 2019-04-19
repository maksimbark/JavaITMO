package ru.ifmo.bot.domain.entities;

public class Person {
  public final Long id;

  public Person(String value) {
    id = Long.parseLong(value);
  }

  public Person(Long value) {
    id = value;
  }

}
