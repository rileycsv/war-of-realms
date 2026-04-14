package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.*;

public class DatabaseIO {
   /*
      NOTE:
      The only available inputs for 'unitName' are:
      -Archers
      -Pikement
      -Cavalry
      -Infantry
      
      The only available inputs for 'value' are:
      -Movement_Speed
      -Attack_Range
      -UnitHealth
   */
   public int getValue(String unitName, String value) {
      //Creating the unit value that is returned
      int returnedValue = 0;
      //This line connects with the database in this file
      try(Connection connection = DriverManager.getConnection("jdbc:sqlite:UnitValues.db")) {
         //This creates a statement object
         Statement statement = connection.createStatement();
         statement.setQueryTimeout(1);
         //This is the select statement
         ResultSet rs = statement.executeQuery("select " + value + " from Units where Unit_Name = '" + unitName + "'");
         
         while(rs.next()) {
            returnedValue = rs.getInt(1);  
         }
         return returnedValue;
      }
      catch(SQLException e) {
         System.out.printf("SQL ERROR: %s%n", e.getMessage());
         return -1;
      }
   }
}