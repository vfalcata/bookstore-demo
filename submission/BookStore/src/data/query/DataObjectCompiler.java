package data.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import data.beans.Bean;
import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.dao.DAO;
import data.fetcher.BookDataFetcher;
import data.fetcher.CartDataFetcher;
import data.fetcher.CustomerDataFetcher;
import data.fetcher.PurchaseOrderDataFetcher;
import data.fetcher.ReviewDataFetcher;
import data.fetcher.VisitorDataFetcher;
import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.CustomerSchema;
import data.schema.PurchaseOrderSchema;
import data.schema.ReviewSchema;
import data.schema.VisitorSchema;

public class DataObjectCompiler {
	String queryString;
	Map<String,Set<String>> attributesIncludedInResults;
	private final String bookTableName = new BookSchema().tableName();
	private final  String customerTableName  = new CustomerSchema().tableName();
	private final  String reviewTableName = new ReviewSchema().tableName();
	private final  String purchaseOrderTableName = new PurchaseOrderSchema().tableName();
	private final  String cartTableName = new CartSchema().tableName();
	private final  String visitorTableName = new VisitorSchema().tableName();
	private String buildOrder;
	private static final String DELIMITER= ">";
	private Map<Id,Book>   bookResults;
	private Map<String,Review>   reviewResults;
	private Map<Id,List<Review>>   customerReviewResults;
	private Map<Id,List<Review>>  visitorReviewResults;
	private Map<Id,List<Review>>   bookReviewResults;
	private Map<Id,Visitor>   visitorResults;
	private Map<Id,Customer>   customerResults;
	private Map<Id,Cart>   cartResults;
	private Map<Id,Map<Long,PurchaseOrder>>   purchaseOrderResults;
	private BookDataFetcher bookDataFetcher;
	private CartDataFetcher cartDataFetcher;
	private CustomerDataFetcher customerDataFetcher;
	private PurchaseOrderDataFetcher purchaseOrderDataFetcher;
	private VisitorDataFetcher visitorDataFetcher;
	private ReviewDataFetcher reviewDataFetcher;
	private Map<Id,List<Review>>   customerToBooksWithReviews;
	private Map<Id,List<Review>>   customerToReviewsWithBooks;
	private List<Visitor> compileVisitorResults = new LinkedList<Visitor>();
	private void writeBuildOrder(String name) {
		this.buildOrder+=!buildOrder.contains(name)?name+DELIMITER:"";
	}
	
	private boolean hasBuildOrder(String ...names) {
		String order="";
		for(String name:names) {
			order+=name+DELIMITER;
		}	
		return this.buildOrder.contains(order);
	}
	

	private boolean isLastBuildStep(String name) {	
		return this.buildOrder.endsWith(name+DELIMITER);
	}
	
	private boolean isFirstBuildStep(String name) {	
		return this.buildOrder.startsWith(name);
	}
	
	public DataObjectCompiler(String queryString,Map<String,Set<String>> attributesIncludedInResults) {
		this.compileCustomerResults=new LinkedList<Customer>();
		this.compileBookResults=new LinkedList<Book>();
		this.visitorReviewResults=new LinkedHashMap<Id, List<Review>>();
		this.queryString=queryString;
		this.attributesIncludedInResults=attributesIncludedInResults;
		this.buildOrder="";
		this.bookResults=new LinkedHashMap<Id, Book>();
		this.reviewResults=  new LinkedHashMap<String, Review>();
		this.visitorResults= new LinkedHashMap<Id, Visitor>();
		this.customerResults= new LinkedHashMap<Id, Customer>();
		this.cartResults= new LinkedHashMap<Id, Cart>();
		this.purchaseOrderResults= new LinkedHashMap<Id, Map<Long,PurchaseOrder>>();
		this.customerReviewResults=new LinkedHashMap<Id, List<Review>>();
		this.bookReviewResults= new LinkedHashMap<Id, List<Review>>();
		bookDataFetcher=new BookDataFetcher(this.attributesIncludedInResults);
		cartDataFetcher=new CartDataFetcher(this.attributesIncludedInResults);
		customerDataFetcher=new CustomerDataFetcher(this.attributesIncludedInResults);
		purchaseOrderDataFetcher=new PurchaseOrderDataFetcher(this.attributesIncludedInResults);
		visitorDataFetcher= new VisitorDataFetcher(this.attributesIncludedInResults);
		reviewDataFetcher= new ReviewDataFetcher(this.attributesIncludedInResults);
	}
	
	protected List<Review> compileReviewsFromBook(Book book){
		List<Review> results = new ArrayList<Review>();		
		return results;
	}
	
	protected List<Review> compileReviewsFromCustomer(Customer customer){
		List<Review> results = new ArrayList<Review>();		
		return results;
	}

	
	private List<Customer> compileCustomerResults;
	public List<Customer> compileCustomers(){
		List<Customer> results = new ArrayList<Customer>();
		for(Entry<Id,Customer> entry:this.customerResults.entrySet()) {
			List<PurchaseOrder> customerPo=new ArrayList<PurchaseOrder>();
			if(this.purchaseOrderResults.containsKey(entry.getKey())) {
				for(Entry<Long,PurchaseOrder> purchaseOrderEntry:this.purchaseOrderResults.get(entry.getKey()).entrySet()) {
					
					Map<Book,Integer> poWithReviews = new LinkedHashMap<Book, Integer>();
					for(Entry<Book,Integer> poBook:purchaseOrderEntry.getValue().getBooks().entrySet()) {
						Book book =poBook.getKey();
						
						if(this.bookResults.containsKey(poBook.getKey().getId())) {
							book=new Book.Builder(this.bookResults.get(poBook.getKey().getId())).build();
						}
						
						if(this.bookReviewResults.containsKey(poBook.getKey().getId())) {
							book=new Book.Builder(book).withReviews(this.bookReviewResults.get(poBook.getKey().getId())).build();
						}
						poWithReviews.put(book, poBook.getValue());
					}
					customerPo.add(new PurchaseOrder.Builder(purchaseOrderEntry.getValue()).withBooks(poWithReviews).build());
				}
			}
			
			List<Review> customerReviews=new ArrayList<Review>();
			if(this.customerReviewResults.containsKey(entry.getKey())) {
				for(Review review:this.customerReviewResults.get(entry.getKey())) {
					Book book = new Book.Builder(review.getBook()).build();
					if(this.bookResults.containsKey(review.getBook().getId())) {
						book=this.bookResults.get(review.getBook().getId());
						book = new Book.Builder(book).withInReview().build();
					}
					customerReviews.add(new Review.Builder(review).withBook(book).withinCustomer().build());
				}				
			}
			
			Cart customerCart=new Cart.Builder().withId(entry.getKey()).build();
			if(this.cartResults.containsKey(entry.getKey())) {
				customerCart=new Cart.Builder(this.cartResults.get(entry.getKey())).build();
				Map<Book,Integer> cartWithReviews = new LinkedHashMap<Book, Integer>();
				for(Entry<Book,Integer> bookEntry:customerCart.getBooks().entrySet()) {
					Book book =bookEntry.getKey();
					if(this.bookResults.containsKey(bookEntry.getKey().getId())) {
						book=new Book.Builder(this.bookResults.get(bookEntry.getKey().getId())).build();
					}
					
					if(this.bookReviewResults.containsKey(bookEntry.getKey().getId())) {
						book=new Book.Builder(book).withReviews(this.bookReviewResults.get(bookEntry.getKey().getId())).build();
					}
					cartWithReviews.put(book, bookEntry.getValue());
				}
				customerCart=new Cart.Builder(customerCart).withBooks(cartWithReviews).build();
				
			}

			
			results.add(new Customer.Builder(entry.getValue())
					.withCart(customerCart)
					.withPurchaseOrders(customerPo)
					.withReviews(customerReviews)
					.build()
					);
		}
		this.compileCustomerResults=results;

		return results;
	}
	
	private List<SiteUser> compileSiteUserResults;
	
	public List<SiteUser> compileSiteUsers(){
		List<SiteUser> visitorSiteUsers = this.compileVisitorResults.stream().map(visitor -> (SiteUser)visitor).collect(Collectors.toCollection(LinkedList::new));
		List<SiteUser> customerSiteUsers = this.compileCustomerResults.stream().map(customer -> (SiteUser)customer).collect(Collectors.toCollection(LinkedList::new));
		this.compileSiteUserResults=new LinkedList<SiteUser>();
		this.compileSiteUserResults.addAll(customerSiteUsers);
		this.compileSiteUserResults.addAll(visitorSiteUsers);
		return this.compileSiteUserResults;
	}
	
	public List<Visitor> compileVisitors(){
		List<Visitor> results = new ArrayList<Visitor>();
		for(Entry<Id,Visitor> entry:this.visitorResults.entrySet()) {
			List<PurchaseOrder> visitorPo=new LinkedList<PurchaseOrder>();
			if(this.purchaseOrderResults.containsKey(entry.getKey())) {
				for(Entry<Long,PurchaseOrder> purchaseOrderEntry:this.purchaseOrderResults.get(entry.getKey()).entrySet()) {
					
					Map<Book,Integer> poWithReviews = new LinkedHashMap<Book, Integer>();
					for(Entry<Book,Integer> poBook:purchaseOrderEntry.getValue().getBooks().entrySet()) {
						Book book =poBook.getKey();
						
						if(this.bookResults.containsKey(poBook.getKey().getId())) {
							book=new Book.Builder(this.bookResults.get(poBook.getKey().getId())).build();
						}
						
						if(this.bookReviewResults.containsKey(poBook.getKey().getId())) {
							book=new Book.Builder(book).withReviews(this.bookReviewResults.get(poBook.getKey().getId())).build();
						}
						poWithReviews.put(book, poBook.getValue());
					}
					visitorPo.add(new PurchaseOrder.Builder(purchaseOrderEntry.getValue()).withBooks(poWithReviews).build());
				}
			}
			
			
			Cart visitorCart=new Cart.Builder().withId(entry.getKey()).build();
			if(this.cartResults.containsKey(entry.getKey())) {
				visitorCart=new Cart.Builder(this.cartResults.get(entry.getKey())).build();
				Map<Book,Integer> cartWithReviews = new LinkedHashMap<Book, Integer>();
				for(Entry<Book,Integer> bookEntry:visitorCart.getBooks().entrySet()) {
					Book book =bookEntry.getKey();
					if(this.bookResults.containsKey(bookEntry.getKey().getId())) {
						book=new Book.Builder(this.bookResults.get(bookEntry.getKey().getId())).build();
					}
					
					if(this.bookReviewResults.containsKey(bookEntry.getKey().getId())) {
						book=new Book.Builder(book).withReviews(this.bookReviewResults.get(bookEntry.getKey().getId())).build();
					}
					cartWithReviews.put(book, bookEntry.getValue());
				}
				visitorCart=new Cart.Builder(visitorCart).withBooks(cartWithReviews).build();

			}
			
			
			results.add(new Visitor.Builder(entry.getValue())
					.withCart(visitorCart)
					.withPurchaseOrders(visitorPo)
					.build()
					);
		}
		this.compileVisitorResults=results;
		return results;

	}

	private List<Book> compileBookResults;
	public List<Book> compileBooks(){
		if(this.compileBookResults!=null && !compileBookResults.isEmpty()) {
			return compileBookResults;
		}
		List<Book> results = new ArrayList<Book>();
		
		Map<Id,Book> compiledBookResults = new LinkedHashMap<Id, Book>(this.bookResults);
		for(Entry<Id,Book> entry:bookResults.entrySet()) {
			List<Review> reviews=new LinkedList<Review>();
			if(bookReviewResults.containsKey(entry.getKey())) {
				reviews=bookReviewResults.get(entry.getKey());
			}
			Map<Id,List<Review>> reviewOfBooks=new LinkedHashMap<Id, List<Review>>();
			
			for(Review review:reviews) {
				Id bookId=entry.getKey();

				if(!reviewOfBooks.containsKey(bookId) || reviewOfBooks.get(bookId)==null) {
					reviewOfBooks.put(bookId, new LinkedList<Review>());
				}
				SiteUser siteUser=null;
				if(review.isReviewByACustomer()) {
					siteUser=(SiteUser)new Customer.Builder((Customer)review.getSiteUser()).withinReview().build();
				}else {
					siteUser=(SiteUser)new Visitor.Builder((Visitor)review.getSiteUser()).build();
				}

				if(customerResults.containsKey(review.getSiteUser().getId())) {
					siteUser =new Customer.Builder(customerResults.get(review.getSiteUser().getId())).withinReview().build();
				}
				
				reviewOfBooks.get(bookId).add(new Review.Builder(review).withSiteUser(siteUser).withinBook().build());
				
			}

			compiledBookResults.replace(entry.getKey(), new Book.Builder(entry.getValue()).withReviews(reviewOfBooks.get(entry.getKey())).build());	
			
		}
		for(Entry<Id,Book> entry:compiledBookResults.entrySet()) {
			results.add(entry.getValue());
		}
		this.compileBookResults=results;
		return results;
	}

	
	public String getCompiledBooksJson() {
		List<Bean> beans = new LinkedList<Bean>();
		for(Book book : compileBookResults) {
			beans.add(book);
		}

		return getBeanJson("books",beans);
	}
	
	public String getCompiledCustomersJson() {
		List<Bean> beans = new LinkedList<Bean>();
		for(Customer customer : compileCustomerResults) {
			beans.add(customer);
		}

		return getBeanJson("customers",beans);
	}

	
	public String getCompiledVisitorsJson() {
		List<Bean> beans = new LinkedList<Bean>();
		for(Visitor visitor : compileVisitorResults) {
			beans.add(visitor);
		}

		return getBeanJson("visitors",beans);
	}

	
	public String getPurchaseOrderJson() {
		List<Book> compiledBooks=compileBooks();
		Map<Book,List<Customer>> booksToCustomer = new LinkedHashMap<Book,List<Customer>>();
	
			
			
			for(Book book: compiledBooks) {
				for(Customer customer:compileCustomerResults) {

					for(PurchaseOrder purchaseOrder:customer.getPurchaseOrders()) {
						if(purchaseOrder.isBookInPurchaseOrder(book)) {
							if(!booksToCustomer.containsKey(book)) {
								booksToCustomer.put(book, new LinkedList<Customer>());
							}
							if(!booksToCustomer.get(book).contains(customer)) {
								booksToCustomer.get(book).add(customer);
							}
						}

					}
				}
			}			
		String result= "{"+"\""+"purchaseOrders"+"\""+":"+"[";
		for(Book book :compiledBooks) {
			result+="{\"book\":"+ book.toJson()+",";
			result+="\"amountSold\":"+ book.getAmountSold()+",";
			result+="\"rating\":"+book.getRating()+",";
			if(!booksToCustomer.isEmpty()) {
				result+="\"customers\":[";
				if(booksToCustomer.containsKey(book)) {
					for(Customer customer:booksToCustomer.get(book)) {
						List<PurchaseOrder> pos=new LinkedList<PurchaseOrder>();
						for(PurchaseOrder purchaseOrder:customer.getPurchaseOrders()) {
							if(purchaseOrder.isBookInPurchaseOrder(book)) {
								pos.add(purchaseOrder);
							}
						}
						result+=new Customer.Builder(customer).withPurchaseOrders(pos).build().toJson()+",";
					}
					result=result.substring(0,result.length()-1);
					result+="],";
				}
			}

		}
		result=result.substring(0,result.length()-1);
		result+="}]}";
		return result;
	}
	
	private String getBeanJson(String beanType,List<Bean> beans) {
		String result= "{"+"\""+beanType+"\""+":"+"[";
		int count=0;
		for(Bean bean:beans) {
			count++;
			result+=bean.toJson();
			if(count<beans.size()) {
				result+=",";
			}
		}
		result+= "]"+"}";
		return result;
	};
	

	DataObjectCompiler thenBuildBooks() {
	if(!attributesIncludedInResults.containsKey(bookTableName)) return this;
	writeBuildOrder(bookTableName);
	return this;			
	}
	
	
	DataObjectCompiler thenBuildReview() {
		if(!attributesIncludedInResults.containsKey(reviewTableName)) return this;
		writeBuildOrder(reviewTableName);
		return this;
	}
	
	DataObjectCompiler thenBuildCustomer() {
		if(!attributesIncludedInResults.containsKey(customerTableName)) return this;
		writeBuildOrder(customerTableName);
		return this;
	}
	
	DataObjectCompiler thenBuildVisitors() {
		if(!attributesIncludedInResults.containsKey(visitorTableName)) return this;
		writeBuildOrder(visitorTableName);
		return this;
	}
	
	DataObjectCompiler thenBuildCarts() {
		if(!attributesIncludedInResults.containsKey(cartTableName)) return this;
		writeBuildOrder(cartTableName);
		return this;
	}
	
	DataObjectCompiler thenBuildPurchaseOrder() {
		if(!attributesIncludedInResults.containsKey(purchaseOrderTableName)) return this;
		writeBuildOrder(purchaseOrderTableName);
		return this;
	}
	private List<String> getBuildOrderAsList(){		
		return Arrays.asList(this.buildOrder.split(DELIMITER));
	}
	
	public 	DataObjectCompiler executeCompilation(){
		Queue<String> buildQueue = new LinkedList<String>(getBuildOrderAsList());
		DataSource dataSource = null;
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		Map<Id,Cart>   cartResultsCurrent= new LinkedHashMap<Id, Cart>();

		try {
			dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");

			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			resultSet= preparedStatement.executeQuery();
			while(resultSet.next()) {
				if(attributesIncludedInResults.containsKey(bookTableName)) {
					Book book=bookDataFetcher.resultSetToBean(resultSet);
					if(!bookResults.containsKey(book.getId()))
					bookResults.put(book.getId(), book);
				}
				if(attributesIncludedInResults.containsKey(customerTableName)) {
					Customer customer=customerDataFetcher.resultSetToBean(resultSet);
					if(!customerResults.containsKey(customer.getId()))
					customerResults.put(customer.getId(), customer);
				}
				if(attributesIncludedInResults.containsKey(visitorTableName)) {
					Visitor visitor=visitorDataFetcher.resultSetToBean(resultSet);
					if(!visitorResults.containsKey(visitor.getId()))
					visitorResults.put(visitor.getId(), visitor);					
				}
				if(attributesIncludedInResults.containsKey(cartTableName)) {
					Cart cart=cartDataFetcher.resultSetToBean(resultSet);
					if(this.cartResults.containsKey(cart.getId())) {
						for(Entry<Book,Integer> cartEntry:cart.getBooks().entrySet()) {
							if(!this.cartResults.get(cart.getId()).isBookInCart(cartEntry.getKey())) {
								this.cartResults.get(cart.getId()).addBookAmount(cartEntry.getKey(), cartEntry.getValue());

							}
						}
					}else {
						this.cartResults.put(cart.getId(), cart);
					}			
				}
				
				if(attributesIncludedInResults.containsKey(purchaseOrderTableName)) {
					PurchaseOrder purchaseOrder=purchaseOrderDataFetcher.resultSetToBean(resultSet);
					Book[] resultBook=new Book[1];
					if(this.purchaseOrderResults.get(purchaseOrder.getId())==null)
						this.purchaseOrderResults.put(purchaseOrder.getId(), new LinkedHashMap<Long, PurchaseOrder>());
					if(!this.purchaseOrderResults.get(purchaseOrder.getId()).containsKey(purchaseOrder.getCreatedAtEpoch())) {
						this.purchaseOrderResults.get(purchaseOrder.getId()).put(purchaseOrder.getCreatedAtEpoch(), purchaseOrder);
					}else {
						for(Entry<Book,Integer> poEntry:purchaseOrder.getBooks().entrySet()) {
							this.purchaseOrderResults.get(purchaseOrder.getId()).get(purchaseOrder.getCreatedAtEpoch()).addBookAmount(poEntry.getKey(),poEntry.getValue());	
						}						
					}					
				}
				
				if(attributesIncludedInResults.containsKey(reviewTableName)) {
					Review review=reviewDataFetcher.resultSetToBean(resultSet);
					Id reviewBookId =review.getBook().getId();
					Id reviewSiteUserId=review.getSiteUser().getId();

					if(review.isReviewByACustomer() ) {
						if(!customerReviewResults.containsKey(reviewSiteUserId)||customerReviewResults.get(reviewSiteUserId)==null) {
							customerReviewResults.put(reviewSiteUserId, new LinkedList<Review>());
						}
						customerReviewResults.get(reviewSiteUserId).add(review);
						if(!customerResults.containsKey(reviewSiteUserId)) {
							this.customerResults.put(reviewSiteUserId, new Customer.Builder().withId(reviewSiteUserId).withUserName(review.getName()).build());
						}
					}
					if(!review.isReviewByACustomer() ) {
						if(!visitorReviewResults.containsKey(reviewSiteUserId)||visitorReviewResults.get(reviewSiteUserId)==null) {
							visitorReviewResults.put(reviewSiteUserId, new LinkedList<Review>());
						}
						visitorReviewResults.get(reviewSiteUserId).add(review);
					}
					
					if(!bookReviewResults.containsKey(reviewBookId)||bookReviewResults.get(reviewBookId)==null) {
						bookReviewResults.put(reviewBookId, new LinkedList<Review>());
					}
					bookReviewResults.get(reviewBookId).add(review);
					
				}
				
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
		return this;

		
	}
	

	
	protected List<Cart> buildCarts() {
		List<Cart> result= new ArrayList<Cart>();
		return result;
	}
	protected List<Customer> buildCustomers() {
		List<Customer> result= new ArrayList<Customer>();
		return result;
	}
	protected List<Visitor> buildVisitors() {
		List<Visitor> result= new ArrayList<Visitor>();
		return result;
	}
	protected List<PurchaseOrder> buildPurchaseOrders() {
		List<PurchaseOrder> result= new ArrayList<PurchaseOrder>();
		return result;
	}
	protected List<Review> buildReviews() {
		List<Review> result= new ArrayList<Review>();
		return result;
	}	
}
