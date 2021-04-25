package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.telnet.TelnetClient;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {
  private ApplicationManager app;
  private TelnetClient telnet;
  private InputStream in;
  private PrintStream out;

  private Session mailSession;
  private Store store;
  private String mailserver;


  public JamesHelper(ApplicationManager app) {
    this.app = app;
    telnet = new TelnetClient();
    mailSession = Session.getDefaultInstance(System.getProperties());   // создаем почтовую сессию
  }

  public boolean doesUserExist(String name) {    // метод для проверки существования пользователя
    initTelnetSession();
    write("verify " + name);
    String result = readUntil("exist");
    closeTelnetSession();
    return result.trim().equals("User " + name + " exist");
  }

  public void createUser(String name, String passwd) {
    initTelnetSession();    // устанавливается соединение по протоколу Telnet
    write("adduser " + name + " " + passwd);    // пишем команду
    String result = readUntil("User " + name + " added");    // ждем, когда на консоль появится такой текст
    closeTelnetSession();    // разрываем соединение
  }

  public void deleteUser(String name) {
    initTelnetSession();
    write("deluser " + name);
    String result = readUntil("User " + name + " deleted");
    closeTelnetSession();
  }

  private void initTelnetSession() {
    mailserver = app.getProperty("mailserver.host");
    int port = Integer.parseInt(app.getProperty("mailserver.port"));
    String login = app.getProperty("mailserver.adminlogin");
    String password = app.getProperty("mailserver.adminpassword");

    try {
      telnet.connect(mailserver, port);    // устанавливаем соединие с почтовым сервером
      in = telnet.getInputStream();    // берем входной поток (исп-ся для того, чтобы что-то читать - те данные, которые TelnetClient отправляет нам)
      out = new PrintStream( telnet.getOutputStream() );    // берем выходной поток (исп-ся для того, чтобы что-то писать - отправлять команды TelnetClient)

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    readUntil("Login id:");
    write("");
    readUntil("Password:");
    write("");

    readUntil("Login id:");
    write(login);
    readUntil("Password:");
    write(password);

    readUntil("Welcome " + login + ". HELP for a list of commands");
  }

  private String readUntil(String pattern) {
    try {
      char lastChar = pattern.charAt(pattern.length() - 1);
      StringBuffer sb = new StringBuffer();
      char ch = (char) in.read();    // посимвольно читаются данные из входного потока (то, что выводит на консоль сервер) и сравниваются с заданным шаблоном
      while (true) {
        System.out.print(ch);
        sb.append(ch);
        if (ch == lastChar) {
          if (sb.toString().endsWith(pattern)) {
            return sb.toString();
          }
        }
        ch = (char) in.read();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void write(String value) {
    try {
      out.println(value);
      out.flush();
      System.out.println(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void closeTelnetSession() {
    write("quit");
  }

  public void drainEmail(String username, String password) throws MessagingException { // удлаяет все письма, полученные определенным пользователем (очищает почтовый ящик)
    Folder inbox = openInbox(username, password);
    for (Message message : inbox.getMessages()) {
      message.setFlag(Flags.Flag.DELETED, true);  // каждое сообщение помечается флагом DELETED
    }
    closeFolder(inbox);
  }

  private void closeFolder(Folder folder) throws MessagingException {
    folder.close(true);  // удаляем все письма, помеченные к удалению
    store.close();    // закрываем соединение с почтовым сервером
  }

  private Folder openInbox(String username, String password) throws MessagingException {  // открываем почтовый ящик
    store = mailSession.getStore("pop3");  // берем почтовую сессию, которая была создана в конструкторе при создании этого помощника, сообщаем, что хотим исп-ть pop3 для доступа к хранилищу почты
    store.connect(mailserver, username, password);  // устанавливаем соединение по протоколу pop3
    Folder folder = store.getDefaultFolder().getFolder("INBOX");  // получаем доступ к папке INBOX
    folder.open(Folder.READ_WRITE);
    return folder;
  }

  public List<MailMessage> waitForMail(String username, String password, long timeout) throws MessagingException {
    long now = System.currentTimeMillis();
    while (System.currentTimeMillis() < now + timeout) {
      List<MailMessage> allMail = getAllMail(username, password);  // пытаемся получить всю почту
      if (allMail.size() > 0) {
        return allMail;  // возвращаем список писем, если есть хотя бы одно письмо
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    throw new Error("No mail :(");
  }

  public List<MailMessage> getAllMail(String username, String password) throws MessagingException {  // извлекаем сообщения из почтового ящика и превращает их в модельные объекты типа MailMessage
    Folder inbox = openInbox(username, password);  // открываем почтовый ящик, т.к. это по правилу работы с почтовым протоколом pop3
    List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream().map((m) -> toModelMail(m)).collect(Collectors.toList());  // преобразование в модельные объекты: список писем превращаем в поток, применяем ф-ию, которая превращает в модельные объекты и превращает обратно в список
    closeFolder(inbox);    // закрываем почтовый ящик
    return messages;
  }

  public static MailMessage toModelMail(Message m) {  // преобразование реальных писем в модельные
    try {
      return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent()); // получаем список адресов, берём первый адрес, получаем содержимого письма и преобразуем в строку, затем по полученным данным строим модельный объект
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
