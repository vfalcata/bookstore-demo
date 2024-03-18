package data.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.util.Map.Entry;

import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.schema.CartSchema;
import data.schema.CustomerSchema;

public class UpdateCart extends DataUpdate{
	
	UpdateCart() {
		
	}
	public void executeClearCart(SiteUser siteUser){
		if(siteUser.getCart().isEmpty()) return;
		String update="DELETE FROM CART WHERE ID='"+siteUser.getId().toString()+"'";
		sendUpdateToDatabase(update);
		
	}

	
	public void executeCartBookDeletion(SiteUser siteUser,Book book){
		if(book==null ||book.getId().isEmpty() ||!siteUser.getCart().isBookInCart(book)) return;
		String update="DELETE FROM CART WHERE ID='"+siteUser.getId().toString()+"' AND BOOK='"+book.getId().toString()+"'";
		sendUpdateToDatabase(update);
		
	}
	

	

	public void executeCartInsertion(SiteUser siteUser,Book book, int amount){
		if(book==null ||book.getId().isEmpty() ||!siteUser.getCart().isBookInCart(book) || amount <=0) return;
		String update ="INSERT INTO CART (ID,BOOK ,AMOUNT )	VALUES 	"+
				"('"+siteUser.getId().toString()+"','"+book.getId().toString()+"','"+siteUser.getUserType()+"',"+Integer.toString(amount)+")";
		sendUpdateToDatabase(update);
		
	}
	

	
	public void executeCartUpdate(SiteUser siteUser,Book book, int amount) {
		
		if(book==null ||book.getId().isEmpty() ||!siteUser.getCart().isBookInCart(book) || amount <=0) {
			return;
		}else {
			String update = "UPDATE CART SET AMOUNT=" + Integer.toString(amount) + " WHERE ID='"+siteUser.getId().toString()+"' AND BOOK='"+book.getId().toString()+"'";		
			sendUpdateToDatabase(update);
		}
		

	}
	

	
	

	
	
}
