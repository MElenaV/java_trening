package ru.stqa.pft.mantis.appmanager;

import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MailHelper {

  private ApplicationManager app;
  private final Wiser wiser;

  public MailHelper(ApplicationManager app) {
    this.app = app;
    wiser = new Wiser();   // при инициализации создается объект типа Wiser; Wiser - это и есть почтовый сервер
  }

  public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException {
    // count - это количество писем, которые должны прийти; timeout - время ожидания
    long start = System.currentTimeMillis();  // запоминаем текущее время
    while (System.currentTimeMillis() < start + timeout) {    // проверяем, что новое текущее время не превышает момент старта в timeout (время еще не истекло)
      if (wiser.getMessages().size() >= count) {    // проверяем, что почты пришло достаточно
        return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());  // прекращаем ожидание: преобразование реальных объектов в модельные (не зависят от деталей реализации)
      }
      try {
        Thread.sleep(10000);// подождать 1000 милесекунд
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    throw new Error("No mail:(");
  }

  public static MailMessage toModelMail(WiserMessage m) { // Mantis присылает письма в обычном текстовом формате; он отправляет два висьма: администратору о регистрации нового юзера и юзеру со ссылкой о продолжении регистрации
    try {
      MimeMessage mm = m.getMimeMessage();
      MailMessage mailMessage = new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
      return mailMessage;  // берем первого получателя из списка (мы знаем, что он там один)
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void start() {    // запуск почтового сервера
    wiser.start();
  }

  public void stop() {    // остановка почтового сервера
    wiser.stop();
  }
}
