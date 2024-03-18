package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import data.beans.Book;
import data.beans.Customer;
import data.beans.Review;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.dao.BookDAO;
import data.dao.CustomerDAO;
import data.dao.PurchaseOrderDAO;
import data.dao.ReviewDAO;
import data.dao.VisitorDAO;


public class MainPageModel {
	
	public static MainPageModel instance;
	private BookDAO book;
	private CustomerDAO user;
	private PurchaseOrderDAO order;
	private ReviewDAO review;
	private VisitorDAO visitor;
	
	public MainPageModel() {};
	
	/**
	 * Get the book instance of a specific book id
	 * 
	 * @param prodID - unique id of book
	 * @return a book instance
	 * @throws Exception if id not found or not unique
	 */
	public Book getBookByID (String prodID) throws Exception{
		
		List<Book> b = book.newQueryRequest()
				 .includeAllAttributesInResultFromSchema()
				 .queryAttribute()
				 .whereBook()
				 .isBook(prodID)
				 .executeQuery()
				 .executeCompilation()
				 .compileBooks();
		
		System.out.println(b.size());
		
		if (b.size() == 0)
			throw new Exception("ID not found in database");
		else if (b.size() > 1)
			throw new Exception("There seem to be ID duplicaes. Aborting!");
		
		return b.get(0);
	}
	
	/**
	 * Get the number of books in each category
	 * 
	 * @return
	 * 		a map containing the genre of the book and the number of books in that genre
	 */
	public Map <String, Integer> getBestSellerBooksByCategory () {
		return book.getCountPerCategory();
	}
	
	/**
	 * Get the 20 most recommended books in a specific category
	 * 
	 * @param category - a specific genre
	 * @return
	 * 		a list of most recommended books in that category
	 */
	public List<Book> getBooksInThisCategory (String category) {
		return  book.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereBookCategory()
				.varCharEquals(category)
				.queryAttribute()
				.whereBookRating()
				.withDescendingOrderOf()
				.withResultLimit(20)
				.executeQuery()
				.executeCompilation()
				.compileBooks();
	}
	
	/**
	 * Get a list of the next reviews to display to the user
	 * 
	 * @param prodID - the unique id of a book
	 * @param currentNumReviews - current number of displayed reviews 
	 * @return
	 * 		a list of new reviews to load
	 */
	public Book getReviewsForThisBook (String prodID, boolean isRestrict, int restrict) {
		
		int num = 0;
		
		if (isRestrict) 
			num = restrict; 
		else
			num = Integer.MAX_VALUE;
		
		List<Book> b = book.newQueryRequest()
			.includeAllAttributesInResultFromSchema()
			.queryAttribute()
			.whereBook()
			.isBook(prodID)
			.queryReview()
			.includeAllAttributesInResultFromSchema()
			.queryAttribute()
			.whereReviewRating()
			.withResultLimit(num)
			.withDescendingOrderOf()
			.executeQuery()
			.executeCompilation()
			.compileBooks();
		
		if (b.size() == 0)
			System.out.println("Couln't find book id: "+prodID + " in Book");
		
		return b.get(0);
	}
	
	/**
	 * Finds books where the input appears in their title, author's name, description or category
	 * 
	 * 
	 * @param input - input in the search bar
	 * @return
	 */
	public List<Book>  prepSearchResult (String input) {
		
		int maxNum = book.getNumberBooks();
			
		List<Book> b= book.newQueryRequest()
						.includeAllAttributesInResultFromSchema()
						.queryAttribute()
						.whereBookCategory()
						.varCharContainsIgnoreCase(input)
						.withResultLimit(book.getNumberBooks())
						.executeQuery().
						executeCompilation().
						compileBooks();
						
		List<Book> b2 = book.newQueryRequest()
						.includeAllAttributesInResultFromSchema()		
						.queryAttribute()
						.whereBookAuthor()
						.varCharContainsIgnoreCase(input)
						.withResultLimit(maxNum)
						.executeQuery().
						executeCompilation().
						compileBooks();
		
		List<Book> b3 = book.newQueryRequest()
						.includeAllAttributesInResultFromSchema()	
						.queryAttribute()
						.whereBookTitle()
						.varCharContainsIgnoreCase(input)
						.withResultLimit(maxNum)
						.executeQuery().
						executeCompilation().
						compileBooks();
		List<Book> b4 = book.newQueryRequest()
						.includeAllAttributesInResultFromSchema()	
						.queryAttribute()
						.whereBookDescription()
						.varCharContainsIgnoreCase(input)
						.withResultLimit(maxNum)
						.executeQuery().
						executeCompilation().
						compileBooks();
		
		b.addAll(b2);
		b.addAll(b3);
		b.addAll(b4);
		Set<Book> targetSet = new HashSet<>(b);
		
		List<Book> b0 = new ArrayList<Book>(targetSet);
		return b0;
	}
	
	/**
	 * Adds a review of the customer
	 * 
	 * @param customer
	 * @param title
	 * @param body
	 * @param rate
	 * @param book_id
	 * @throws Exception
	 */
	public void addAnonymousReview (SiteUser vis, String title, String body, int rate, String book_id) throws Exception{
		Book b;
		
		try {
			b = getBookByID(book_id);
		} catch (Exception e) {
			throw new Exception("ERROR: book was not found in the database! "+ e.getMessage());
		}

		// need to insert information
		try {
			review.newUpdateRequest()
				.requestNewReviewInsertion(vis, b)
				.insertReviewWithTitle(title)
				.insertReviewWithBody(body)
				.insertReviewWithRating(rate)
				.executeReviewInsertion();
			
		} catch (Exception e) {
			throw new Exception("There was an error adding the review: " + e.getMessage() + " " + e.getCause());
		}
	}
	
	/**
	 * Adds a review of the customer
	 * 
	 * @param customer
	 * @param title
	 * @param body
	 * @param rate
	 * @param book_id
	 * @throws Exception
	 */
	public void addReview (SiteUser customer, String title, String body, int rate, String book_id) throws Exception{
		Book b;
		
		try {
			b = getBookByID(book_id);
		} catch (Exception e) {
			throw new Exception("ERROR: book was not found in the database! "+ e.getMessage());
		}
		
		List<Customer> c = user.newQueryRequest()
						.includeAllAttributesInResultFromSchema()
						.queryAttribute()
						.whereCustomer()
						.isCustomer((Customer) customer)
						.executeQuery()
						.executeCompilation()
						.compileCustomers();
		
		System.out.println("\n\nc.length = "+c.size()+"\n");
		
		// need to update information
		if (c.size() == 1 && this.didCustomerAddReview(c.get(0), book_id)) {
			try {
				
				List<Review> this_review = getReview(c.get(0),  book_id);
				
				System.out.println("\n\n this review = "+this_review.size()+"\n");
				
				review.newUpdateRequest()
					.requestUpdateReview(c.get(0), this_review.get(0))
					.updateReviewBody(body)
					.updateReviewRating(rate)
					.updateReviewTitle(title)
					.executeUpdate();

				System.out.println("\n\n Done updating review! \n");
				
			} catch (Exception e) {
				throw new Exception("There was an error adding the review: " + e.getMessage() + " " + e.getCause());
			}
		}
		// need to insert information
		else {
			try {
			
				review.newUpdateRequest()
					.requestNewReviewInsertion((SiteUser)customer, b)
					.insertReviewWithTitle(title)
					.insertReviewWithBody(body)
					.insertReviewWithRating(rate)
					.executeReviewInsertion();
				
			} catch (Exception e) {
				throw new Exception("There was an error adding the review: " + e.getMessage() + " " + e.getCause());
			}
		}
	}
	
	/**
	 * Checks if the customer reviewed the product previously or not
	 * 
	 * @param customer
	 * @param book_id
	 * @return
	 * @throws Exception
	 */
	public boolean didCustomerAddReview (Customer customer, String book_id) throws Exception {
		
		List<Book> b = review.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereReviewCustomer()
				.isCustomer(customer)
				.queryBook()
				.queryAttribute()
				.whereBook()
				.isBook(book_id)
				.executeQuery()
				.executeCompilation()
				.compileBooks();
		
		if (b.size() > 1)
			throw new Exception ("At most 1 book can be associated with a user review!");
		
		return (b.size() == 1);
	}
	
	/**
	 * Gived the reviewID, it returns the Review object which identifies 
	 * with that id
	 * 
	 * @param customer
	 * @param bookID
	 * @return
	 * @throws Exception 
	 */
	public List<Review> getReview (Customer customer, String bookID) throws Exception {
			
		Book b = this.getBookByID(bookID);
		
		try {
			List<Customer> s = review.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereReviewBook()
				.isBook(b)
				.queryAttribute()
				.whereReviewCustomer()
				.isCustomer(customer)
				.withResultLimit(Integer.MAX_VALUE)
				.executeQuery()
				.executeCompilation()
				.compileCustomers();
			
			System.out.println("num reviews found = "+s.size());
			
			return s.get(0).getReviews();
		} catch (Exception e) {
			throw new Exception ("ERROR: review by this username for this book was not found!");
		}
	}
	
	/**
	 * Given only a username, which is unique, find the customer
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public Customer getUserByUsername (SiteUser s) throws Exception{
		try {
			List<Customer> c =user.newQueryRequest()
							.includeAllAttributesInResultFromSchema()
							.queryAttribute()
							.whereCustomer()
							.isCustomer((Customer) s)
							.withResultLimit(Integer.MAX_VALUE)
							.executeQuery()
							.executeCompilation()
							.compileCustomers();

			return c.get(0);
		} catch (Exception e) {
			throw new Exception("ERROR retriving customer using siteUser");
		}
	}
	
	public Customer getUser (String username, String passwd) {
		return user.loginCustomer(username, passwd);
	}
	
	/**
	 * Updates the user entry 
	 * 
	 * @param username
	 * @param passwd
	 * @param customer
	 * @param name
	 * @param last_name
	 * @param email
	 * @param street
	 * @param street_num
	 * @param city
	 * @param province
	 * @param country
	 * @param postal_code
	 * @throws Exception
	 */
	public void updateUserInfo (String username, String passwd, 
								Customer customer, String name,
								String last_name, String email,
								String street, String street_num, 
								String city, String province, 
								String country, String postal_code) throws Exception {
		
		if (username.isEmpty())
			throw new Exception("username cannot be empty");
		
		if (passwd.isEmpty())
			throw new Exception("password cannot be empty");
		
		if (email.isEmpty())
			throw new Exception("email cannot be empty");
		
		if (name.isEmpty())
			throw new Exception("given name cannot be empty");
		
		List<Customer> c = user.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereCustomerUserName()
				.varCharEquals(username)
				.executeQuery()
				.executeCompilation()
				.compileCustomers();
		
		// username doesn't exist or stays the same for the same user
		if (c.size() == 0 || c.size() == 1 && c.get(0).getId().isEqual(customer.getId())) {
			user.newUpdateRequest()
			.requestUpdateExistingCustomer(customer)
			.updateCustomerUserName(username)
			.executeUpdate();
			
		} else {
			throw new Exception ("Problem updating information - "
					+ "there exists a username in the database with the same "
					+ "usrname. Try a different name!");
		}
		
		c = user.newQueryRequest()
			.includeAllAttributesInResultFromSchema()
			.queryAttribute()
			.whereCustomerEmail()
			.varCharEquals(email)
			.executeQuery()
			.executeCompilation()
			.compileCustomers();
		
		if (c.size() == 0 || c.size() == 1 && c.get(0).getId().isEqual(customer.getId())) {
			user.newUpdateRequest()
			.requestUpdateExistingCustomer(customer)
			.updateCustomerEmail(email)
			.executeUpdate();
		} else 
			throw new Exception ("Problem updating information - "
					+ "this email already exists in the database. Try a different one or login with "
					+ "the other account!");		
		try {
			user.newUpdateRequest()
				.requestUpdateExistingCustomer(customer)
				.updateCustomerCity(city)
				.updateCustomerCountry(country)
				.updateCustomerGivenName(name)
				.updateCustomerPassword(passwd)
				.updateCustomerPostalCode(postal_code)
				.updateCustomerProvince(province)
				.updateCustomerStreet(street)
				.updateCustomerStreetNumber(street_num)
				.updateCustomerSurName(last_name)
				.executeUpdate()
				;
		} catch (Exception e) {
			throw new Exception("Troble updating the information! " + e.getMessage());
		}
	}
	
	public Visitor getVisitor(HttpServletRequest request) {
		return visitor.getVisitor(request);
	}
	
	//getInstance will return that ONE instance of the pattern 
	//with the the DAO objects initialized..
	public static MainPageModel getInstance()throws ClassNotFoundException{
		if (instance==null) {
			instance =new MainPageModel();
			instance.book = new BookDAO();
			instance.review = new ReviewDAO();
			instance.user = new CustomerDAO();
			instance.visitor = new VisitorDAO();
		}	
		
		return instance;
	}
}
