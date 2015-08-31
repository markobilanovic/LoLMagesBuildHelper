package db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public static Connection getConnection() throws URISyntaxException, SQLException {
	    URI dbUri = new URI("postgres://tdfyyhsxtxwdgc:Fw20o6RBHa9_84t1nUBo0n91fT@ec2-54-204-40-96.compute-1.amazonaws.com:5432/dtmgrkif1rso");
	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	    return DriverManager.getConnection(dbUrl, username, password);
	}
}
