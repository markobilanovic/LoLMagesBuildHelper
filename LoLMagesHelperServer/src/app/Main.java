package app;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controller.MatchesImporter;

import db.DBConnection;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LoadMatches();
		
		
	}
	
	private static void LoadMatches()
	{
		MatchesImporter mi = new MatchesImporter();
		
	}
	
	
	//Not using SQL
	private void DBManipulation()
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		Statement stmt = null;
		try {
			conn = DBConnection.getConnection();

			
			//**************INSERT ROW
		/*	String insertTableSQL = "INSERT INTO CHAMPIONS"
					+ "(ID, NAME) VALUES"
					+ "(?,?)";
			preparedStatement = conn.prepareStatement(insertTableSQL);
			preparedStatement.setInt(1, 1);
			preparedStatement.setString(2, "test");
			// execute insert SQL stetement
			preparedStatement .executeUpdate();
			*/
			
			
			//**************CREATE TABLE
		/*	
			String createTableSQL = "CREATE TABLE CHAMPIONS("
					+ "ID integer NOT NULL, "
					+ "NAME VARCHAR(30) NOT NULL"
					+ ")";
			preparedStatement = conn.prepareStatement(createTableSQL);
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
			*/
			
			//**************SELECT
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT ID, NAME FROM CHAMPIONS";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
			}
			stmt.close();
			conn.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		/*	if (stmt != null) {
				stmt.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			conn.close();
			*/
		}
	}
	

}
