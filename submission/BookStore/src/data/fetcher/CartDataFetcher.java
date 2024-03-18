package data.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.Visitor;
import data.query.Query;
import data.schema.CartSchema;

public class CartDataFetcher  extends DataFetcher<Cart>{

	public CartDataFetcher(Map<String, Set<String>> attributesToIncludInResults) {
		super(attributesToIncludInResults);
	}


	private CartSchema schema=new CartSchema(); 
	

	protected Entry<Customer,Cart> resultSetFromCustomerToCart(ResultSet resultSet){
		Cart cart=resultSetToBean(resultSet);
		Customer customer = new Customer.Builder().withId(cart.getId()).build();
		return new AbstractMap.SimpleEntry<Customer,Cart>(customer, cart);
	}
	
	protected Entry<Visitor,Cart> resultSetFromVisitorToCart(ResultSet resultSet){
		Cart cart=resultSetToBean(resultSet);
		Visitor visitor = new Visitor.Builder().withId(cart.getId()).build();
		return new AbstractMap.SimpleEntry<Visitor,Cart>(visitor, cart);
	}
	
	
	protected Entry<Book,Cart> resultSetFromBookToCart(ResultSet resultSet){
		Cart cart=resultSetToBean(resultSet);
		Book book=cart.getBooks().keySet().toArray(new Book[1])[0];
		return new AbstractMap.SimpleEntry<Book,Cart>(book, cart);
	}
	
	@Override
	public Cart resultSetToBean(ResultSet resultSet) {
		String prefix = isReferenceQuery()?schema.tableName()+Query.referenceSeparator:"";
				
		try {
			
			Book book = new Book.Builder().withId(new Id(resultSet.getString(prefix+schema.BOOK))).build();
		
			
			return new Cart.Builder()
					.withId(new Id(resultSet.getString(prefix+schema.ID)))
					.withBookAmount(book,resultSet.getInt(prefix+schema.AMOUNT))
					.build();	
						
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.err.println("Warning empty book, since resultSet could not produce book object");
		return new Cart.Builder().build();
		
	}

}
