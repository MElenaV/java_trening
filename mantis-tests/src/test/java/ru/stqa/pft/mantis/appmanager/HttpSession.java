package ru.stqa.pft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpSession {
  private CloseableHttpClient httpClient;
  private ApplicationManager app;

    public HttpSession(ApplicationManager app){    // конструктор HttpSession принимает на вход объект типа ApplicationManager для того, чтобы не нужно было думать какие данные ApplicationManager должен передать помощнику (он передаёт ссылку на самого себя). Помощник знает, кто является его менеджером и когда ему нужна информация, он обращается к менеджеру. Также реализовано получение некоторых св-в прочитанных из конфигурационного файла
      this.app = app;    // В HttpSession передали ссылку на ApplicationManager, запомнили её
      httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();    // внутри создается новый Client/новая сессия для работы по протоколу HTTP (объект, который будет отправлять запросы на сервер)
    }    // используем шаблон проектирования builder (вытягивание методов в цепочку - FloatInterface); в созданном объекте устанавливается стратегия перенаправления setRedirectStrategy (если её не устновить получим ответ 302 и должны будем сами перенаправление обрабатывать), чтобы httpClient автоматически выполнял все перенаправления - это можно сделать установив ему такую стратегию перенаправления - LaxRedirectStrategy()

    public boolean login(String username, String password) throws Exception {
      HttpPost post = new HttpPost(app.getProperty("web.baseUrl")+"/login.php");    // через ссылку app помощник имеет возможность обращаться к менеджеру; в этой строчке создаётся будущий запрос
      List<NameValuePair> params = new ArrayList<>();
      params.add(new BasicNameValuePair(username, "username"));
      params.add(new BasicNameValuePair(password, "password"));
      params.add(new BasicNameValuePair("securesession", "on"));
      params.add(new BasicNameValuePair("return", "index.php"));
      post.setEntity(new UrlEncodedFormEntity(params));    // добавленные параметры упаковываются в соответствии с определенными правилами и помещаются в заранее созданный запрос post.setEntity
      CloseableHttpResponse response = httpClient.execute(post); // отправка запроса и помещение в переменную response
      String body = getTextForm(response);    // получаем текст ответа с помощью вспомогательной ф-ии getTextForm (получаем исходный код html)
      return body.contains(String.format("<span class=\"italic\">%s</span>", username));    // проверяем действительно ли пользователь вошел
    }

    private String getTextForm(CloseableHttpResponse response) throws Exception{
      try{
        return EntityUtils.toString(response.getEntity());
      }
      finally {
        response.close();
      }
    }

    public boolean isLoggedInAs(String username) throws Exception {    // определяет какой пользователь сейчас залогинен
      HttpGet get = new HttpGet(app.getProperty("webBaseUrl") + "/index.php");
      CloseableHttpResponse response = httpClient.execute(get);
      String body = getTextForm(response);
      return body.contains(String.format("<span class=\"italic\">%s</span>", username));
    }
  }
