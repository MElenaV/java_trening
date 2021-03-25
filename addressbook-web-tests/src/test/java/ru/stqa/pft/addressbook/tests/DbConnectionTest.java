package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.sql.*;

public class DbConnectionTest {

  @Test
  public void testDbConnection() {
    Connection conn = null;

    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook?user=root&password=");
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select group_id,group_name, group_header, group_footer from group_list");// Do something with the Connection
      Groups groups = new Groups();
      while (rs.next()) {
        groups.add(new GroupData().withId(rs.getInt("group_id")).withName(rs.getString("group_name"))
                .withHeader(rs.getString("group_header"))
                .withFooter(rs.getString("group_footer")));
      }
      rs.close(); // не собираемся читать больше никаие данные, освобождаем память
      st.close(); // не собираемся выполнять больше никаие запросы
      conn.close(); // закрываем соединение с бд

      System.out.println(groups);

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }
}
