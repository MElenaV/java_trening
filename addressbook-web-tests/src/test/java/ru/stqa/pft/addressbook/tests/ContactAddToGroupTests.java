package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import static org.testng.AssertJUnit.assertTrue;

public class ContactAddToGroupTests extends TestBase {

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

    if (app.db().contactsWithoutGroups().size() == 0) {
      app.goTo().contactPage();
      app.contact().create(new ContactData()
              .withFirstname("Petr").withLastname("Petrovich").withAddress("St. Petersburg")
              .withHomePhone("245-98-12").withMobilePhone("89601652303").withWorkPhone("2488808")
              .withEmail1("petr1@mail.ru").withEmail2("petr2@mail.ru").withEmail3("petr3@mail.ru"));
    }
  }

  @Test
  public void testAddingContactToGroup() {
    ContactData before = app.db().contactWithoutGroup();
    Groups groups = app.db().groups();
    GroupData group = groups.iterator().next();
    app.goTo().contactPage();
    app.contact().selectContactWithoutGroup(before);
    app.contact().selectGroupForAdded(group);
    app.contact().addContactToGroup();
    ContactData after = app.db().contactById(before.getId());

    assertTrue(after.getGroups().contains(group)); // проверяем наличие группы у контакта
    verifyContactListInUI();

  }


}
