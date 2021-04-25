package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
  private final Properties properties;
  private WebDriver wd;

  private String browser;
  private RegistrationHelper registrationHelper;
  private FtpHelper ftp;
  public MailHelper mailHelper;
  private JamesHelper jamesHelper;

  public ApplicationManager(String browser) {

    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(String.format("src/test/resources/%s.properties", target)));
  }

  public void stop() {
    if (wd != null) {
      wd.quit();
    }
  }

  public HttpSession newSession(){    // инициализация помощника при каждом обращении (в момент конструирования создается экземпляр помощника)
    return new HttpSession(this);
  }

  public String getProperty(String key) {    // в качестве параметра принимает имя свойства, которое нужно извлечь
    return properties.getProperty(key);
  }

  public RegistrationHelper registration() {    // каждый раз создавать экземпляр класса регистрации необходимости нет, поэтому делаем ленивую инициализацию регистрации (переносить в метод init смысла нет, т.к. там будет запускаться браузер, а мы от этого ушли)
    if (registrationHelper == null) {
      registrationHelper = new RegistrationHelper(this);    // в качестве параметра передаём this (ссылку на ApplicationManager); manager нанимает помощника и передаёт ему ссылку на самого себя);
    }
    return registrationHelper;
 }

 public FtpHelper ftp() {   // ленивая инициализация (возвращает объект типа FTPHelper в том случае, если объект уже инициализирован; если нет - инициализируем)
  if (ftp == null) {
    ftp = new FtpHelper(this);
  }
  return ftp;
 }


  public WebDriver getDriver() {
    if (wd == null) {    // ленивая инициализация (реальная инициализация браузера только, когда к нему кто-то обратится): проверяем инициализирвоан ли браузер
      if (browser.equals(BrowserType.FIREFOX)){
        wd = new FirefoxDriver();
      } else if (browser.equals(BrowserType.CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(BrowserType.IE)) {
        wd = new InternetExplorerDriver();
      }
      wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      wd.get(properties.getProperty("web.baseUrl"));
    }
    return wd;
  }

  public MailHelper mail() {   // ленивая инициализация (возвращает объект типа MailHelper в том случае, если объект уже инициализирован; если нет - инициализируем)
    if (mailHelper == null) {
      mailHelper = new MailHelper(this);
    }
    return mailHelper;
  }

  public JamesHelper james(){
    if (jamesHelper == null) {
      jamesHelper = new JamesHelper(this);
    }
    return jamesHelper;
  }
}
