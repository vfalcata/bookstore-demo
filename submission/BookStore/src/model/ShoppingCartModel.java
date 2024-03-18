package model;

import java.util.Map;

import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.SiteUser;
import data.dao.CartDAO;
import data.dao.CustomerDAO;

public class ShoppingCartModel {
	
	private static ShoppingCartModel instance;
	CustomerDAO customerDAO;
	CartDAO cartDAO;
	
	private ShoppingCartModel()
	{
		customerDAO = new CustomerDAO();
		cartDAO = new CartDAO();
	}
	
	public static ShoppingCartModel getInstance()
	{
		if(instance == null)
		{
			instance = new ShoppingCartModel();
		}
		
		return instance;
	}
	
	public double getTotalPrice(Cart cart)
	{
		double totalPrice = 0;
		
		for(Map.Entry<Book, Integer> entry : cart.getBooks().entrySet())
		{
			totalPrice += entry.getKey().getPrice() * entry.getValue();
		}
		
		return totalPrice;
		
	}
	
	public void updateBookQuantity(SiteUser user, String isbn, int newQuantity)
	{
		
		for(Book b : user.getCart().getBooks().keySet())
		{
			if(b.getISBN().equals(isbn))
			{
				user.getCart().getBooks().put(b, newQuantity);
				cartDAO.newUpdateRequest()
				.executeCartUpdate(user, b, newQuantity);
				break;
			}
		}
	}
	
	public void removeBook(SiteUser user, String isbn)
	{
		for(Book b : user.getCart().getBooks().keySet())
		{
			if(b.getISBN().equals(isbn))
			{
				user.getCart().getBooks().remove(b);
				cartDAO.newUpdateRequest()
				.executeCartBookDeletion(user, b);
				break;
			}
		}
	}
	
	public void addAllFromCart(SiteUser fromUser, SiteUser toUser)
	{
		//visitor
		Map<Book, Integer> fromBooks = fromUser.getCart().getBooks();
		//customer
		Map<Book, Integer> toBooks = toUser.getCart().getBooks();
		for(Map.Entry<Book, Integer> entry : fromBooks.entrySet())
		{
			//add quantity
			if(toBooks.entrySet().contains(entry))
			{
				Book b = entry.getKey();
				int i = entry.getValue() + toBooks.get(b);
				toBooks.put(b, i);
				cartDAO.newUpdateRequest()
				.executeCartUpdate(toUser, b, i);
			}
			//add book
			else
			{
				Book b = entry.getKey();
				toBooks.put(b, fromBooks.get(b));
				cartDAO.newUpdateRequest()
				.executeCartInsertion(toUser, b, fromBooks.get(b));
			}
		}
		
	}
	
	
}
