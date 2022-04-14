import java.sql.*;
public class MysqlCon{
//   private Connection con;
//   private String url, userName, pwd;

  public Connection connect() {

  	 Connection con = null;

  	   try {
  		   Class.forName("com.mysql.cj.jdbc.Driver");
  	   }
  	   catch (ClassNotFoundException e) {
  		   System.out.println("Impossible de charger le pilote jdbc");
  	   }

  	   System.out.println("connexion a la base de données");

  	   try {
  	         String DBurl = "jdbc:mysql://localhost:3306/xml_tp";
  	         con = DriverManager.getConnection(DBurl,"univ_user","password");
  	         System.out.println("connexion réussie");
  	   }
  	   catch (SQLException e) {
  		   System.out.println("Connection à la base de données impossible");
  	   }

  	   return con;
     }


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

  public void doStatement(String sqlString){
	try {
		Connection con = connect();
		Statement stmt = con.createStatement();
		String rqString = sqlString; 
		stmt.executeUpdate(rqString);
	} 
	catch (SQLException e) {
		e.printStackTrace();
	}
  }
}
