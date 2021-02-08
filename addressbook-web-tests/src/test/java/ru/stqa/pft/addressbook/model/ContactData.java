package ru.stqa.pft.addressbook.model;

public class ContactData {
  private final String firstname;
  private final String lastname;
  private final String address;
  private final String phoneHome;
  private final String email;

  public ContactData(String firstname, String lastname, String address, String phoneHome, String email) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.address = address;
    this.phoneHome = phoneHome;
    this.email = email;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getAddress() {
    return address;
  }

  public String getPhoneHome() {
    return phoneHome;
  }

  public String getEmail() {
    return email;
  }
}
