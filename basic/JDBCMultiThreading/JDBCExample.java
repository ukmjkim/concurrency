/* How to run the program */
/* export CLASSPATH=.:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/mysql-connector-java-5.1.44-bin.jar; java JDBCExample */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCExample {

  public static void main(String[] argv) {

  System.out.println("-------- MySQL JDBC Connection Testing ------------");

  try {
    Class.forName("com.mysql.jdbc.Driver");
  } catch (ClassNotFoundException e) {
    System.out.println("Where is your MySQL JDBC Driver?");
    e.printStackTrace();
    return;
  }

  System.out.println("MySQL JDBC Driver Registered!");
  Connection connection = null;

  try {
    connection = DriverManager
    .getConnection("jdbc:mysql://serpstat:3306/serpstat?autoReconnect=true&useSSL=false", "serpstat", "statserp");

  } catch (SQLException e) {
    System.out.println("Connection Failed! Check output console");
    e.printStackTrace();
    return;
  }

  if (connection != null) {
    System.out.println("You made it, take control your database now!");
  } else {
    System.out.println("Failed to make connection!");
  }
  }
}

