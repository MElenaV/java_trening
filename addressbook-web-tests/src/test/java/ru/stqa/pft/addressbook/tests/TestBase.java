package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Listeners(MyTestListener.class)    // testng, когда будет выполнять тесты, увидит аннатацию,подключит этот плагин и будет к нему обращаться в нужный момент времени
public class TestBase {

  Logger logger = LoggerFactory.getLogger(TestBase.class);

  protected static final ApplicationManager app
          = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

  @BeforeSuite
  public void setUp(ITestContext context) throws Exception {
    app.init();
    context.setAttribute("app", app); // помещаем эту ссылку на объект ApplicationManager в context; затем MyTestListener сможет её от туда извлечь и снять скриншот
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.stop();
  }

  @BeforeMethod
  public void logTestStart(Method m, Object[] p) {
    logger.info("Start test " + m.getName() + " with parameters " + Arrays.asList(p));
  }

  @AfterMethod(alwaysRun = true)
  public void logTestStop(Method m) {
    logger.info("Stop test " + m.getName());
  }

  public void verifyGroupListInUI() {
    if (Boolean.getBoolean("verifyUI")) {    // эта функция получает системное свойсто с заданным именем и автомаически преобразет её в boolean величину
      Groups dbGroups = app.db().groups();
      Groups uiGroups = app.group().all();
      assertThat(uiGroups, equalTo(dbGroups.stream()
              .map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))    // функциональное программирование: ко всем элементам коллекции (данным загруженным из БД) применяем ф-ию, которая их упрощает. Stream и прим-ем ко всем элементам ф-ию map (анонимная ф-ия, которая принимает на вход группу, а на выходе новый объект типа GroupData с идентификатором таким же как у преобразуемого объекта и именем).
              .collect(Collectors.toSet())));    // Затем собираем всё при помощи коллектора toSet()
    }
  }

  public void verifyContactListInUI() {
    if (Boolean.getBoolean("verifyUI")) {
      Contacts dbContacts = app.db().contacts();
      Contacts uiContacts = app.contact().all();
      assertThat(uiContacts, equalTo(dbContacts.stream()
              .map((c) -> new ContactData().withId(c.getId()).withLastname(c.getLastname()).withFirstname(c.getFirstname()).withAddress(c.getAddress()))
//                      .withEmail1(c.getEmail1()).withEmail2(c.getEmail2()).withEmail3(c.getEmail3())
//                      .withHomePhone(c.getHomePhone()).withWorkPhone(c.getWorkPhone()).withMobilePhone(c.getMobilePhone()))
              .collect(Collectors.toSet())));
    }
  }

}

