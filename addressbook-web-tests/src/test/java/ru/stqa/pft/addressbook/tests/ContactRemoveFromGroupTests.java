package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import static org.testng.AssertJUnit.assertFalse;


public class ContactRemoveFromGroupTests extends TestBase {

  @BeforeMethod
  public void ensurePrecondition(){
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test2"));
    }

    if (app.db().contacts().size() == 0) {
      app.goTo().contactPage();
      app.contact().create(new ContactData()
              .withFirstname("Petr").withLastname("Petrovich").withAddress("St. Petersburg")
              .withHomePhone("245-98-12").withMobilePhone("89601652303").withWorkPhone("2488808")
              .withEmail1("petr1@mail.ru").withEmail2("petr2@mail.ru").withEmail3("petr3@mail.ru"));
      app.goTo().contactPage();
    }

    if (app.db().contactWithGroups().size() == 0) {
      Contacts before = app.db().contactsWithoutGroups();
      Groups groups = app.db().groups();
      GroupData group = groups.iterator().next();
      app.goTo().contactPage();
      app.contact().selectContactWithoutGroup(before.iterator().next());
      app.contact().selectGroupForAdded(group);
      app.contact().addContactToGroup();
    }
  }

  @Test
  public void testRemovingContactFromGroup() {
    ContactData before = app.db().contactInGroup();
    GroupData group = before.getGroups().iterator().next();
    app.goTo().contactPage();
    app.contact().selectGroupFromFilter(group);
    app.contact().selectContact(before);
    app.contact().removeContactFromGroup();
    ContactData after = app.db().contactById(before.getId());

    assertFalse(after.getGroups().contains(group));    // проверяем отсутствие группы у контакта
    verifyContactListInUI();
  }

}
