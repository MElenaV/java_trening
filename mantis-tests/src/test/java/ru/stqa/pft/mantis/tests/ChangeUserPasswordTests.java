package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;
import ru.stqa.pft.mantis.model.Users;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class ChangeUserPasswordTests extends TestBase {

  @BeforeMethod
  public void startMailServer(){
    app.mail().start();
  }



  @Test
  public void testChangeUserPassword() throws Exception {
    // Авторизуемся администратором, переходим на страницу управления пользователями, выбираем пользователя/заданного пользователя и нажимаем кнопку "Сбросить пароль"
    app.sessionHelper().login("Administrator", "root");
    app.sessionHelper().usersList();
    Users userList = app.db().users();  // получаем список всех пользователей из бд за исключением администратора

    UserData selectedUser = userList.iterator().next();
    String emailFromDb = selectedUser.getEmail();
    String usernameFromDb = selectedUser.getUsername();
//     UserData selectedUser = app.db().userByEmail("user1619380707449@localhost");  // берем заданного пользователя
    app.sessionHelper().selectUser(Integer.valueOf(selectedUser.getId()));
    app.sessionHelper().changePassword();


    //Ждём письмо, извлекаем ссылку для изменения пароля, изменяем пароль
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
    //String confirmationLink = app.sessionHelper().findConfirmationLink(mailMessages,  userName + "@localhost");
    String confirmationLink = app.sessionHelper().findConfirmationLink(mailMessages, emailFromDb);
    app.sessionHelper().changePassword(confirmationLink, "passwordNew");
    app.sessionHelper().updateUserPassword(confirmationLink, usernameFromDb, "passwordNew");

    //Авторизуемся по http протоколу юзером с новым паролем
    HttpSession httpSession = app.newSession();
    assertTrue(httpSession.login(usernameFromDb, "passwordNew"));
  }

  @AfterMethod(alwaysRun = true)
  public void stopMailServer(){
    app.mail().stop();
  }

}
