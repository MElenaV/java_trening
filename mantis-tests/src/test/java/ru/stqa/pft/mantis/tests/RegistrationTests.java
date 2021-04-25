package ru.stqa.pft.mantis.tests;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class RegistrationTests extends TestBase {

 // @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testRegistration() throws Exception {
    long now = System.currentTimeMillis(); // currentTimeMillis - ф-ия возвращает текущее время в милисекундах
    String user = String.format("user%s", now);
    String password = "password";
    String email = String.format("user%s@localhost", now);
    app.james().createUser(user, password);    // создаем пользователя на внешнем почтовом сервере
    app.registration().start(user, email);    // выполняется первая часть регистрации
    //List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);  //  закомментировали, т.к. письмо будем получать не из встроенного почтового сервера
    List<MailMessage> mailMessages = app.james().waitForMail(user, password, 60000);  // получаем письмо с внешнего почтового сервера
    String confirmationLink = findConfirmationLink(mailMessages, email); // находим письмо, которое отправлено на этот адрес и извлекаем из него ссылку
    app.registration().finish(confirmationLink, password);
    assertTrue(app.newSession().login(user, password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get(); // найти среди всех писем то, которое отправлено на нужный адрес; используем ф-ию filter, котороя на вход принимает предикат mailMessages, а возвращает boolean значение с проверкой); в результате фильтрации только те значения, которые отправлены по нужному адресу
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);  // возвращает тот кусок текста, который соответствует построенному регулярному выражению
  }

//  @AfterMethod (alwaysRun = true)    // alwaysRun = true - чтобы тестовый почтовый сервер останавливался даже тогда, когда тест завершался не успешно
  public void stopMailServer() {
    app.mail().stop();
  }


}
