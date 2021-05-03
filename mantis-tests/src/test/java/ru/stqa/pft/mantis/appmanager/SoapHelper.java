package ru.stqa.pft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.stqa.pft.mantis.model.Issue;
import ru.stqa.pft.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

  private ApplicationManager app;

  public SoapHelper (ApplicationManager app) {
    this.app = app;
  }

  public Set<Project> getProject() throws RemoteException, MalformedURLException, ServiceException { // у каждого объекта будет свой идентфиикатор, поэтому множества; нет необходимости говорить о порядке объектов; getProject() - возвращает множество модельных объектов
    MantisConnectPortType mc = getMantisConnect();
    ProjectData[] projects = mc.mc_projects_get_user_accessible(app.getProperty("soap.adminlogin"), app.getProperty("soap.adminPassword"));// получить список проектов, к которому пользователь имеет доступ
    return Arrays.asList(projects).stream()
            .map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName()))
            .collect(Collectors.toSet()); // преобразуем полученные данные в модельные объекты  (ко всем элементам потока применяем ф-ию map, которая из объектов типа ProjectData строит новый объект типа Project с определенным идентификатором и именем; после чего новый объект типа Project собираем и возвращаем получившиеся множества)
  }

  private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
    MantisConnectPortType mc = new MantisConnectLocator()
            .getMantisConnectPort(new URL(app.getProperty("soap.mantisUrl")));
    return mc;
  }


  public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
    MantisConnectPortType mc = getMantisConnect();
    String[] categories = mc.mc_project_get_categories(app.getProperty("soap.adminlogin"), app.getProperty("soap.adminPassword"), BigInteger.valueOf(issue.getProject().getId()));
    IssueData issueData = new IssueData();
    issueData.setSummary(issue.getSummary());
    issueData.setDescription(issue.getDescription());
    issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
    issueData.setCategory(categories[0]);
    BigInteger issueId = mc.mc_issue_add(app.getProperty("soap.adminlogin"), app.getProperty("soap.adminPassword"), issueData);
    IssueData createdIssueData = mc.mc_issue_get(app.getProperty("soap.adminlogin"), app.getProperty("soap.adminPassword"), issueId);
    return new Issue().withId(createdIssueData.getId().intValue())
            .withSummary(createdIssueData.getSummary()).withDescription(createdIssueData.getDescription())
            .withProject(new Project().withId(createdIssueData.getProject().getId().intValue())
            .withName(createdIssueData.getProject().getName()));
  }

  public IssueData getIssue(int issueId) throws MalformedURLException, ServiceException, RemoteException {
    MantisConnectPortType mc = getMantisConnect();
    return mc.mc_issue_get(app.getProperty("soap.adminlogin"), app.getProperty("soap.adminPassword"), BigInteger.valueOf(issueId));
  }
}
