package model;

import java.util.List;

import data.beans.Book;
import data.beans.PurchaseOrder;
import data.dao.BookDAO;
import data.dao.CustomerDAO;
import data.dao.PurchaseOrderDAO;
import data.query.DataObjectCompiler;

public class RestModel {
	
	private static RestModel instance;
	private BookDAO book;
	private PurchaseOrderDAO orders;
	
	public RestModel() {
		
	}
	
	public static RestModel getInstance() {
		if (instance == null) {
			instance = new RestModel();
			instance.book = new BookDAO();
			instance.orders = new PurchaseOrderDAO();
		}
		return instance;
	}
	
	/**
	 * Get the book instance of a specific book id
	 * 
	 * @param prodID - unique id of book
	 * @return a JSON String containing the information of the book, or error messages.
	 */
	public String getBookByISBN_JSON (String prodID) throws Exception {
		
		List<Book> b = this.getBookByISBN(prodID);

		return createBookMessage(b, prodID);
		
	}
	
	public String getOrdersByISBN_JSON (String prodID) throws Exception {
		String orderString = this.getOrdersByISBN(prodID);
		
		return orderString;
	}
	
	public List<Book> getBookByISBN (String prodID) throws Exception {

		List<Book> b = this.book.newQueryRequest()
				 .includeAllAttributesInResultFromSchema()
				 .queryAttribute()
				 .whereBookISBN()
				 .varCharEquals(prodID)
				 .executeQuery()
				 .executeCompilation()
				 .compileBooks();

		return b;

	}
	
	public String getOrdersByISBN (String prodID) throws Exception {

		String orderString = "";
		DataObjectCompiler docCust = null;
		
		//Retrieve the book based on it's ISBN
		List<Book> book = this.getBookByISBN(prodID);
		
		//Would like to save the orders in this list 
		List<PurchaseOrder> po;
		
		//check size of book list and return appropriate response
		if (book.size() == 0)
			orderString = this.create404Message("Book with ISBN: \'" + prodID + "\' Not Found");

		if (book.size() > 1)
			orderString = this.create403Message("Retrieved multiple books with given ISBN: \'" + prodID + "\' please providd this output to site admin: info@bookstore.ca");
		else {
					
			docCust = new CustomerDAO().newQueryRequest()
					.includeAllAttributesInResultFromSchema()
					.queryPurchaseOrder()
					.includeAllAttributesInResultFromSchema()
					.queryBook()
					.includeAllAttributesInResultFromSchema()
					.queryAttribute()
					.whereBookISBN()
					.varCharEquals(prodID)
					.executeQuery()
					.executeCompilation();
			
			docCust.compileCustomers();
			
			orderString = docCust.getPurchaseOrderJson();
			
		}
		return orderString;
	}
	
	public String createBookMessage(List<Book> b, String prodID) {
		String bookString  = "";
		
		if (b.size() == 0)
			bookString = this.create404Message("Book with ISBN: \'" + prodID + "\' Not Found");
		else if (b.size() > 1)
			bookString = this.create403Message("Retrieved multiple books with given ISBN: \'" + prodID + "\' please providd this output to site admin: info@bookstore.ca");
		else 
			bookString = b.get(0).toJson();

		return bookString;
	}
	
	public String create404Message(String msg) {
		String bookString = "{\n"
				+ "\"error\": {\n"
				+ " \"errors\": [\n"
				+ "  {\n"
				+ "   \"domain\": \"global\",\n"
				+ "   \"reason\": \"notFound\",\n"
				+ "   \"message\": \"" + msg + "\"\n"
				+ "  }\n"
				+ " ],\n"
				+ " \"code\": 404,\n"
				+ " \"message\": \"Not Found\"\n"
				+ " }\n"
				+ "}";
		
		return bookString;
	}
	
	public String create403Message(String msg) {
		String bookString = "{\n"
				+ " \"error\": {\n"
				+ "  \"errors\": [\n"
				+ "   {\n"
				+ "    \"domain\": \"global\",\n"
				+ "    \"reason\": \"forbidden\",\n"
				+ "    \"message\": \"" + msg + "\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"code\": 403,\n"
				+ "  \"message\": \"Forbidden\"\n"
				+ " }\n"
				+ "}";
		
		return bookString;
	}
	
}