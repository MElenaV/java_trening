package ru.stqa.pft.mantis.appmanager;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.stqa.pft.mantis.model.UserData;
import ru.stqa.pft.mantis.model.Users;

import java.util.List;

public class DbHelper {

  private final SessionFactory sessionFactory;
  public DbHelper(){
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();
    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
  }

  public Users allUsers() {     // получаем всех пользователей системы, включая администратора
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<UserData> result = session.createQuery( "from UserData" ).list();
    for (UserData user : result) {
      System.out.println(user);
    }
    session.getTransaction().commit();
    session.close();
    return new Users(result);
  }

  public Users users() {     // получаем всех пользователей системы, кроме администратора
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<UserData> result = session.createQuery( "from UserData where id <> 1" ).list();
    for (UserData user : result) {
      System.out.println(user);
    }
    session.getTransaction().commit();
    session.close();
    return new Users(result);
  }


}
