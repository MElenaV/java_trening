package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.mantis.appmanager.HttpSession;

import static org.testng.AssertJUnit.assertTrue;

public class LoginTests extends TestBase {

  @Test
  public void testLogin() throws Exception{
    HttpSession httpSession = app.newSession();
    assertTrue(httpSession.login("administrator", "root"));
    assertTrue(httpSession.isLoggedInAs("administrator"));

  }
}
