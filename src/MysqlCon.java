import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
public class MysqlCon{

/** 
 * Connection à la base de donnée 
 * @return Connection
 */
  public Connection connect() {
	Properties prop = new Properties();
	try {
		prop.load(new FileInputStream("config.properties"));
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	String url = prop.getProperty("url");
	String port = prop.getProperty("port");
	String username = prop.getProperty("username");
	String password = prop.getProperty("password");

  	 Connection con = null;

  	   try {
  		   Class.forName("com.mysql.cj.jdbc.Driver");
  	   }
  	   catch (ClassNotFoundException e) {
  		   System.out.println("Impossible de charger le pilote jdbc");
  	   }

  	   System.out.println("connexion a la base de données");

  	   try {
  	         String DBurl = "jdbc:mysql://localhost:"+port+"/"+url;
  	         con = DriverManager.getConnection(DBurl,username,password);
  	         System.out.println("connexion réussie");
  	   }
  	   catch (SQLException e) {
  		   System.out.println("Connection à la base de données impossible");
  	   }

  	   return con;
     }


  
  /** 
   * @param sqlString
   * @return ResultSet
   */
  public  ResultSet doRequest(String sqlString) {
    ResultSet results = null;
    Connection con = connect();
    try {
      Statement stmt = con.createStatement();
      results = stmt.executeQuery(sqlString);
   } catch (SQLException e) {
      e.printStackTrace();
   }
    return results;
  }

  
  /** 
   * @param sqlString
   */
  public void doStatement(String sqlString){
	  String[] requestsList = sqlString.split("\n");
	try {
		Connection con = connect();
		Statement stmt = con.createStatement();
		for (int i = 0; i < requestsList.length; i++) {
			stmt.executeUpdate(requestsList[i]);
		}
	} 
	catch (SQLException e) {
		e.printStackTrace();
	}
  }
}
