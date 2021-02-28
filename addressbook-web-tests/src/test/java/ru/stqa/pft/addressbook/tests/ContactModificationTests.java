package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    app.getNavigationHelper().gotoContactPage();
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Petr", "Petrovich", "St. Petersburg", "245-98-12", "petr@mail.ru", "test1"), true);
    }
    app.getContactHelper().selectContact();
    app.getContactHelper().initContactModification();
    app.getContactHelper().fillContactForm(new ContactData("Ivan", "Ivanovich", "Moscow", "89301456532", "ivan@mail.ru", null), false);
    app.getContactHelper().submitContactModification();
    app.getNavigationHelper().gotoContactPage();
  }

}
