package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePrecondition(){
    if (app.db().contacts().size() == 0) {
      app.goTo().contactPage();
      app.contact().create(new ContactData()
              .withFirstname("Petr").withLastname("Petrovich").withAddress("St. Petersburg").withHomePhone("245-98-12").withGroup("test2"), true);
    }
  }

  @Test
  public void testContactModification() {
    Contacts before = app.db().contacts();
    ContactData modifiedContact = before.iterator().next();
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId()).withFirstname("Ivan").withLastname("Ivanovich").withHomePhone("89301456532").withAddress("Moscow");
    app.goTo().contactPage();
    app.contact().modify(contact, false);
    Contacts after = app.db().contacts();

    assertThat(after.size(), equalTo(before.size()));
   ContactData expectedAfterModification = contact
            .withHomePhone(contact.getHomePhone())
            .withMobilePhone(contact.getMobilePhone())
            .withWorkPhone(contact.getWorkPhone())
            .withEmail1(contact.getEmail1())
            .withEmail2(contact.getEmail2())
            .withEmail3(contact.getEmail3());
    Contacts expected = before.without(modifiedContact).withAdded(expectedAfterModification);
    assertThat(after, equalTo(expected));
//    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
  }

}
