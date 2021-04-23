package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {

  private final ApplicationManager app;
  private FTPClient ftp;

  public FtpHelper(ApplicationManager app) {
    this.app = app;
    ftp = new FTPClient();    // FTPClient устанавливает соединения, передаёт файлы и другие ф-ии
  }

  public void upload (File file, String target, String backup) throws IOException {    // загружает новый конфигурационный файл, при этом старый файл временно переименовавывет
// file - локальный файл, который должен быть загружен; target - имя удаленного файла, куда всё загружается; backup - имя резервной копии, если удаленный файл уже существует
    ftp.connect(app.getProperty("ftp.host"));    // устанавливаем соединение с сервером
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    ftp.deleteFile(backup);    // удаляем, на всякий случай, предыдущую резервную копию
    ftp.rename(target, backup);   // переименовываем удаленный файл и делаем резервную копию
    ftp.enterLocalPassiveMode();    // включается пассивный режим передачи данных
    ftp.storeFile(target, new FileInputStream(file));    // передаётся локальный файл (из него делается InputStream); FileInputStream - чтение бинарных данных - побайтовое; данные читаются из локального файла, передаётся на удаленную машину и там сохранятеся в удаленном файле, который называется target
    ftp.disconnect();    // разрываем соединение
  }

  public void restore (String backup, String target) throws IOException {    // восстанавливает старый конфигурационный файл
    ftp.connect(app.getProperty("ftp.host"));
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    ftp.deleteFile(target);   // удаляется тот файл, который мы загрузили
    ftp.rename(backup, target);   // восстанавливается оригинальный файл из резервной копии
    ftp.disconnect();
  }



}
