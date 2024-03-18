package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class DataUpdate {

	protected void sendUpdateToDatabase(String updateString) {
		
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		try {
			DataSource dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(updateString);
			preparedStatement.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}finally {
			if(connection!= null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(preparedStatement!=null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected int checkDatabaseResultSet(String attributeName,String queryString) {
		
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		int count=0;
		try {
			DataSource dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				count=resultSet.getInt(attributeName);
			}
			
			
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}finally {
			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
			if(preparedStatement!=null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection!= null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}
	
	protected String surroundWithQuotes(String word) {
		String result=word==null||word.isEmpty()?"NULL":"'"+word+"'";
		return result;
	}
	
}
