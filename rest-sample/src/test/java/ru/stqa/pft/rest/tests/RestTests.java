package ru.stqa.pft.rest.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.rest.model.Issue;
import java.io.IOException;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;

public class RestTests extends TestBase {

  @Test
  @BeforeMethod
  public void checkIfIssueIsFixed() throws IOException {
    skipIfNotFixed(1045);
  }

  @Test
  public void testCreateIssue() throws IOException {
    Set<Issue> oldIssues = app.rest().getIssues();  // получаем множество объектов типа Issue
    Issue newIssue = new Issue().withSubject("Test issue2").withDescription("New test issue2");
    int issueId = app.rest().createIssue(newIssue);
    oldIssues.add(newIssue.withId(issueId));
    Set<Issue> newIssues = app.rest().getIssues();
    assertEquals(newIssues, oldIssues);
  }
}
