package ru.stqa.pft.rest.tests;

import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.rest.appmanager.ApplicationManager;
import ru.stqa.pft.rest.model.Issue;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.testng.SkipException;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class TestBase {

  protected static final ApplicationManager app
          = new ApplicationManager();

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
  }

  private Executor getExecutor() {
    return  Executor.newInstance()
            .auth(app.getProperty("rest.adminlogin"), "");
  }

  private boolean  isIssueOpen(int issueId) throws IOException {
    String issueJson = getExecutor().execute(Request
            .Get(app.getProperty("rest.bugifyUrlApi") + "/issues/" + issueId + ".json")).returnContent().asString();
    JsonElement parsed = new JsonParser().parse(issueJson);
    JsonElement jIssues = parsed.getAsJsonObject().get("issues");  // по ключу извлекаем нужную часть
    Set<Issue> issues =  new Gson().fromJson(jIssues, new TypeToken<Set<Issue>>() {}.getType()); // преобразуем полученные данные в множество объектов типа Issue и затем их возвращаем
    Issue issue = issues.stream().filter(Issue -> Objects.equals(issueId, Issue.getId())).findFirst().get();
    if ((issue.getStateName().equals("Resolved")) || (issue.getStateName().equals("Closed"))) {
      return false;
    }
    return true;
  }

  public void skipIfNotFixed(int issueId) throws IOException {
    if (isIssueOpen(issueId)) {
      throw new SkipException("Ignored because of issue " + issueId);
    }
  }
}
