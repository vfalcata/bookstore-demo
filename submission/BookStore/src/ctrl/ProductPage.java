package ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Visitor;
import data.beans.Review;
import data.beans.Visitor;
import model.MainPageModel;

/**
 * Servlet implementation class ProductPage
 */
@WebServlet("/ProductPage")
public class ProductPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String MODEL = "model";

	private static final String TITLE = "TITLE";
	private static final String AUTHOR = "AUTHOR";
	private static final String YEAR = "YEAR";
	private static final String COVER = "COVER";
	private static final String PRICE = "PRICE";
	private static final String RATING = "RATING";
	private static final String CATEGORY = "CATEGORY";
	private static final String ISBN = "ISBN";
	
	private static final String bookID = "bookID";
	
	private static final String VIWE_SOME_REVIEWS = "VIWE_SOME_REVIEWS";
	
    private static final String ONE_STAR = "ONE_STAR";
    private static final String TWO_STAR = "TWO_STAR";
    private static final String THREE_STAR = "THREE_STAR";
    private static final String FOUR_STAR = "FOUR_STAR";
    private static final String FIVE_STAR = "FIVE_STAR";
    
    private static final String rate = "rate";
    private static final String title = "title";
    private static final String body = "body";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductPage() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    @Override
    public void init(ServletConfig config) throws ServletException { 
    	super.init(config);

	    ServletContext context = getServletContext();
	    
	    try {
		    MainPageModel model = MainPageModel.getInstance();
		    
		    context.setAttribute(MODEL, model);
	    }
	    catch (Exception e) {
	    	System.out.println("ERROR initializeing main page model!");
	    }
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		MainPageModel model = (MainPageModel) context.getAttribute(MODEL);
		
		HttpSession h = request.getSession();
		
		//----------------------------------------------------------------
		//---------------------- Adds product to cart  -------------------
		if (request.getParameter("addProduct") != null ) {
			String book_id = request.getParameter(bookID);
			request.setAttribute(bookID, book_id);
			
			// ------ customer ------
			if (h.getAttribute("customer") != null) {
				
				this.addProductCustomer(book_id, request, model, h);
			
			// ------ visitor ------
			} else {
				
				this.addProductVisitor(book_id, request, model, h);
			}
			
			try {
				completeProductLoad( request,  model,  book_id);
				request.getRequestDispatcher("html/ProductPage.jspx").forward(request, response);
			} catch (Exception e) { 
				System.out.println("Problem loading the page after addig to cart! " + e.getMessage()); 
			}
			
		//----------------------------------------------------------------
		//---------------- redirect user to add a review  ----------------
		} else if (request.getParameter("addReview") != null) {
			
			String book_id;
			
			if (request.getParameter(bookID) != null)
				book_id = request.getParameter(bookID);
			else
				book_id = (String) h.getAttribute(bookID);

			try {
			
				// if a user is loged in - set attributes to match customer's previous review if applicable
				this.setReviewIfNeeded(h, book_id, model, request);	
			
				Book book = model.getBookByID(book_id);
			
				String title = book.getTitle();
				String author = book.getAuthor();
	
				request.setAttribute(bookID, book_id);
				request.setAttribute(TITLE, title);
				request.setAttribute(AUTHOR, author);
				
				request.setAttribute("redirectTo", "ProductPage");
				request.getRequestDispatcher("html/Review.jspx").forward(request, response);
				
			} catch (Exception e) {
				request.setAttribute(bookID, book_id);

				try {
					completeProductLoad( request,  model,  book_id);
					request.getRequestDispatcher("html/ProductPage.jspx").forward(request, response);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		//----------------------------------------------------------------
		//------------------ initial loading of the page  ----------------
		else if (request.getParameter(bookID) != null) {

			String book_id =  request.getParameter(bookID);
			h.setAttribute(bookID, book_id);
			try {
				
				// if user just submitted a review- it would handle it
				this.addReview(h, book_id, model, request);
				
				request.setAttribute(bookID, book_id);
				
				completeProductLoad( request,  model,  book_id);
				
			}catch (Exception e) {
				
				request.setAttribute("review_success", "ERROR adding the review: "+e.getMessage());
				request.setAttribute(bookID, book_id);
				try {
					completeProductLoad( request,  model,  book_id);
					
				} catch (Exception e1) {
					System.out.println("An error occured." + e1.getMessage());
				}
				System.out.println("An error occured." + e.getMessage());
			}
			
			request.getRequestDispatcher("html/ProductPage.jspx").forward(request, response);
		
		//----------------------------------------------------------------
		//----- page was refreshed - need to set attributes again  -------
		} else if (h.getAttribute(bookID) != null) {
			try {
				String id =  (String) h.getAttribute(bookID);
				request.setAttribute(bookID, id);
				h.setAttribute(bookID, id);
				
				completeProductLoad( request,  model,  id);
			
			} catch (Exception e) {
				System.out.println("An error occured." + e.getMessage());
			}
			
			request.getRequestDispatcher("html/ProductPage.jspx").forward(request, response);
		
		//----------------------------------------------------------------
		//------------------------- An ERROR  ----------------------------
		} else  {
			System.out.println("ID = "+request.getParameter(bookID));
			System.out.println("An error occured. Could have come here only if pressed 'load more reviews'");
			
			//request.getRequestDispatcher("html/ProductPage.jspx").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * sets the attribite of book to be seen in the product page
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	private void setAttributes (HttpServletRequest request, MainPageModel model, String id) throws Exception{
		Book b = model.getBookByID(id);
		
		double this_rate = (double)((int)(Math.ceil(b.getRating() * 100)))/100;
		
		request.setAttribute(TITLE, b.getTitle());
		request.setAttribute(AUTHOR, b.getAuthor());
		System.out.println("year of "+b.getId() + " is " + b.getPublishYear());
		request.setAttribute(YEAR, b.getPublishYear());
		request.setAttribute(COVER, b.getCover());
		request.setAttribute(PRICE, b.getPrice());
		request.setAttribute(RATING, this_rate);
		request.setAttribute(ISBN, b.getISBN());
		request.setAttribute(CATEGORY, b.getCategory());	
		request.setAttribute("DESC", b.getDescription());
	}
	
	/**
	 * Pulls out the top 10 reviews for a specific book
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private String getTop10Reviews(HttpServletRequest request, MainPageModel model, String id) throws Exception {
		
		String html = "";
		Book b = null;
		
		try {
			b = model.getReviewsForThisBook (id, true, 10);
			
		} catch (Exception e) {
			return "<p> This book has no reviews </p>";
		}
		
		Review[] r = b.getReviews();

		
		System.out.println("number of reviews= " + r.length);
		
		int numPages = (int) Math.ceil( (double) r.length / 10);
		double this_rate;
		
		for (int i = 0; i < r.length; i ++) {
			
			String tmpLine = "";
			
			this_rate = (double)((int)(Math.ceil(r[i].getRating() * 100)))/100;
			
			if (r[i].getUserType().equals("CUSTOMER")) {
				Customer customer = model.getUserByUsername(r[i].getSiteUser());
				
				tmpLine = "					<p> <img class=\"user_image\" style=\"float:left;width:30px;height:30px;vertical-align:center;\" src=\"/res/user_logo.png\" /> "+customer.getSurName() + ", "+ customer.getGivenName() + " " + this_rate+ " / 5 </p>";
			} else {
				tmpLine = "					<p> <img class=\"user_image\" style=\"float:left;width:30px;height:30px;vertical-align:center;\" src=\"/res/user_logo.png\" /> <i> site visitor </i> " + this_rate + " / 5 </p>";
			}
				
			html +=   "				<div class=\"review_row\" style=\"margin-top:50px;\">\n"
					+ tmpLine
					+ "					<div class=\"container_title  \">"+r[i].getTitle()+"</div>\n"
					+ "					<p>\n"
					+ "						"+r[i].getBody()
					+ "					</p>\n"
					+ "				</div>\n";
		}
		
		return html;
	}
	
	/**
	 * loads the page of a product based in the book id
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	private void completeProductLoad(HttpServletRequest request, MainPageModel model, String id) throws Exception {
		String html = getTop10Reviews(request, model, id);
		
		setAttributes (request,  model,  id);
		
		request.setAttribute(VIWE_SOME_REVIEWS, html);
	}
	
	/**
	 * Adds the selected product to the customer's cart
	 * 
	 * @param book_id
	 * @param request
	 * @param model
	 * @param h
	 */
	private void addProductCustomer(String book_id, HttpServletRequest request, MainPageModel model, HttpSession h){
	
		try {				
			Customer customer = (Customer) h.getAttribute("customer");
			Cart cart = customer.getCart();
			
			if (!cart.isBookInCart(model.getBookByID(book_id)))
				cart.addBookAmount(model.getBookByID(book_id), 1);
			
			h.setAttribute("customer", customer);
			
			request.setAttribute("success", "Product added successfully");
		} catch (Exception e) {
			request.setAttribute("success","ERROR - Failed to add book to customer cart! "+e.getMessage());
		}
	}
	
	/**
	 * Adds the selected product to the visitor's cart
	 * 
	 * @param book_id
	 * @param request
	 * @param model
	 * @param h
	 */
	private void addProductVisitor(String book_id, HttpServletRequest request, MainPageModel model, HttpSession h){
		try {
			Visitor visitor;
			if (h.getAttribute("visitor") == null) 
				 visitor = model.getVisitor(request);
			else
				visitor = (Visitor) h.getAttribute("visitor");
			
			Cart cart = visitor.getCart();

			if (!cart.isBookInCart(model.getBookByID(book_id)))
				cart.addBookAmount(model.getBookByID(book_id), 1);
			
			h.setAttribute("visitor", visitor);
		
			request.setAttribute("success", "Product added successfully");
		} catch (Exception e) {
			request.setAttribute("success", "ERROR - Failed to add book to visitor's cart! "+e.getMessage());
		}
	}
	
	/**
	 * sets the review attributes if customer reviewed product before.
	 * It also prevents visitors to review the same book more than once
	 * 
	 * @param h
	 * @param book_id
	 * @param model
	 * @param request
	 * @throws Exception
	 */
	private void setReviewIfNeeded(HttpSession h, String book_id, MainPageModel model, HttpServletRequest request) throws Exception {

		Customer customer = (Customer) h.getAttribute("customer");
		
		try {
			if (customer != null) {
				// give customer option to update his review or check it out
				if (model.didCustomerAddReview(customer, book_id)) {
					List<Review> review = model.getReview(customer, book_id);
					Review r = review.get(0);
					
					int rating_number = r.getRating();
					
					if (rating_number == 1)
						request.setAttribute(ONE_STAR, 1);
					else if (rating_number == 2)
						request.setAttribute(TWO_STAR, 2);
					else if (rating_number == 3)
						request.setAttribute(THREE_STAR, 3);
					else if (rating_number == 4)
						request.setAttribute(FOUR_STAR, 4);
					else if (rating_number == 5)
						request.setAttribute(FIVE_STAR, 5);
					
					
					request.setAttribute(title, r.getTitle());
					request.setAttribute(body, r.getBody()); 
				}
			} else {

				if (h.getAttribute("visitor_add_review") != null) {
					
					Set<String> rev = (HashSet<String>) h.getAttribute("visitor_add_review");
					int size = rev.size();
					rev.add(book_id);
					
					// no id was added - it is a duplicate
					if (rev.size() == size) {
						request.setAttribute("review_success", "visitors can add only one review per book per session!");
						throw new Exception ();
					}
				}
			}
		} catch (Exception e) {
			//request.setAttribute("review_success", "There was a problem going back form a specific order page to writing its review!" + e.getMessage());
			throw new Exception ();
		}	
	}
	
	/**
	 * Adds the input from the user to the database, and adjusts feedback message accordingly
	 * 
	 * @param h
	 * @param book_id
	 * @param model
	 * @param request
	 * @throws Exception
	 */
	private void addReview (HttpSession h, String book_id, MainPageModel model, HttpServletRequest request) throws Exception{
		//----------------------------------------------------------------
		//---------------- user just submitted a review  -----------------
		if (request.getParameter("review_form") != null) {

			int rank = Integer.parseInt(request.getParameter(rate));
			
			String this_title = request.getParameter(title);
			
			String this_body = request.getParameter(body);
			
			// update database with this user
			if (h.getAttribute("customer") != null) {

				Customer customer = (Customer) h.getAttribute("customer");
				
				// add review 
				model.addReview(customer, this_title, this_body, rank, book_id);
			} 
			// update database anonymously
			else {
				// add review  anonymously
				
				Visitor visitor;
				if (h.getAttribute("visitor") == null) 
					 visitor = model.getVisitor(request);
				else
					visitor = (Visitor) h.getAttribute("visitor");
				
				model.addAnonymousReview(visitor, this_title, this_body, rank, book_id);
				
				Set<String> rev;
				if (h.getAttribute("visitor_add_review") == null) {
					rev = new HashSet<String>();
					rev.add(book_id);
				} else {
					rev = (HashSet<String>) h.getAttribute("visitor_add_review");
				}
					
				h.setAttribute("visitor_add_review", rev);
			}
			
			request.setAttribute("review_success", "review was added successfully");
		}
	}
}
