package ru.stqa.pft.rest.appmanager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import ru.stqa.pft.rest.model.Issue;
import java.io.IOException;
import java.util.Set;

public class RestHelper {

  private ApplicationManager app;

  public RestHelper(ApplicationManager app) {
    this.app = app;
  }

  public Set<Issue> getIssues() throws IOException {
    String json =getExecutor().execute(Request.Get(app.getProperty("rest.bugifyUrlApi") + "/issues.json"))
            .returnContent().asString();
    JsonElement parsed = new JsonParser().parse(json);
    JsonElement issues = parsed.getAsJsonObject().get("issues");  // по ключу извлекаем нужную часть
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {}.getType()); // преобразуем полученные данные в множество объектов типа Issue и затем их возвращаем
  }

  private Executor getExecutor() {
    return Executor.newInstance().auth(app.getProperty("rest.adminlogin"), "");
  }

  public int createIssue(Issue newIssue) throws IOException {
    String json = getExecutor().execute(Request.Post(app.getProperty("rest.bugifyUrlIssues"))
            .bodyForm(new BasicNameValuePair("subject", newIssue.getSubject()),
                    new BasicNameValuePair("description", newIssue.getDescription())))
            .returnContent().asString();
    JsonElement parsed = new JsonParser().parse(json);
    return parsed.getAsJsonObject().get("issue_id").getAsInt();  // возвращается идентификатор созданного баг-репорта
  }
}
