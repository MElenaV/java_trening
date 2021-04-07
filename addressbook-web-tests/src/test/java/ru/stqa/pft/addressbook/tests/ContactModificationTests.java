package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePrecondition(){
    if (app.db().contacts().size() == 0) {
      app.goTo().contactPage();
      Groups groups = app.db().groups();
      app.contact().create(new ContactData()
              .withFirstname("Petr").withLastname("Petrovich").withAddress("St. Petersburg")
              .withHomePhone("245-98-12").withMobilePhone("89601652303").withWorkPhone("2488808")
              .withEmail1("petr1@mail.ru").withEmail2("petr2@mail.ru").withEmail3("petr3@mail.ru").inGroup(groups.iterator().next()), true);
    }
  }

  @Test
  public void testContactModification() {
    Contacts before = app.db().contacts();
    ContactData modifiedContact = before.iterator().next();
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId()).withFirstname("Ivan").withLastname("Ivanovich").withAddress("Moscow").withHomePhone("89301456532");
    app.goTo().contactPage();
    app.contact().modify(contact, false);
    Contacts after = app.db().contacts();

    assertThat(after.size(), equalTo(before.size()));
   ContactData expectedAfterModification = contact
            .withMobilePhone(modifiedContact.getMobilePhone())
            .withWorkPhone(modifiedContact.getWorkPhone())
            .withEmail1(modifiedContact.getEmail1())
            .withEmail2(modifiedContact.getEmail2())
            .withEmail3(modifiedContact.getEmail3());
    Contacts expected = before.without(modifiedContact).withAdded(expectedAfterModification);
    assertThat(after, equalTo(expected));
//    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
    verifyContactListInUI();
  }

}
