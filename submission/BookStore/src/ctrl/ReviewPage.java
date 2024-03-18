package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.beans.Book;
import data.beans.Customer;
import data.beans.Review;
import model.MainPageModel;

/**
 * Servlet implementation class ReviewPage
 */
@WebServlet("/ReviewPage")
public class ReviewPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String CUSTOMER = "customer";
	private static final String VISITOR = "visitor";
	
	private static final String MODEL = "model";
	
	private static final String bookID = "bookID";
	
	private static final String VIWE_ALL_REVIEWS= "VIWE_ALL_REVIEWS";
	private static final String NUM_REVIEWS_FOUND = "NUM_REVIEWS_FOUND";
	private static final String TITLE = "TITLE";
	private static final String AUTHOR = "AUTHOR";
	private static final String YEAR = "YEAR";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewPage() {
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
	    	System.out.println("ERROR initializeing review page model!");
	    }
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext context = getServletContext();
		MainPageModel model = (MainPageModel) context.getAttribute(MODEL);
		
		HttpSession h = request.getSession();
		
		if (request.getParameter(bookID)!=null) {
			
			try {
			
				String id =  request.getParameter(bookID);
				h.setAttribute(bookID, id);
				setAttributes ( request,  model,  id);
				String html = getAllReviewsForBook(request, id, model);
				
				
				request.setAttribute(VIWE_ALL_REVIEWS, html);
			
			} catch (Exception e) {
				request.setAttribute(NUM_REVIEWS_FOUND, "0");
				System.out.println("An error occured." + e.getMessage());
			}

		// used reload the review page (send user back from where it came from)
		} else if (h.getAttribute(bookID) != null) {
			try {
				
				String id =  (String) h.getAttribute(bookID);
				h.setAttribute(bookID, id);
				setAttributes ( request,  model,  id);
				String html = getAllReviewsForBook(request, id, model);
				
				request.setAttribute(VIWE_ALL_REVIEWS, html);
			
			} catch (Exception e) {
				request.setAttribute(NUM_REVIEWS_FOUND, "0");
				System.out.println("An error occured." + e.getMessage());
			}
		
		// ERROR - should nott be here
		} else  {
			System.out.println("ID = "+request.getParameter(bookID));
			System.out.println("An error occured. Could have come here only if pressed 'load more reviews'");
		}
		
		request.getRequestDispatcher("html/ProdReviewPage.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * This method returns html tags of reviews for book with id 'id', and sets the request scope attributes
	 * 
	 * @param request http request
	 * @param id	book id
	 * @param model to communicate with the appropriate databases
	 * @return
	 * @throws Exception
	 */
	private String getAllReviewsForBook(HttpServletRequest request, String id, MainPageModel model) throws Exception {
		String html = "";
		
		Book b = model.getReviewsForThisBook (id, false, 0);
		
		if (b == null) {
			request.setAttribute(NUM_REVIEWS_FOUND, "0");
			return "<p> This book has no reviews </p>";
		}

		Review[] r = b.getReviews();
		
		int numPages = (int) Math.ceil( (double) r.length / 10);
		
		
		for (int i = 0; i < r.length; i ++) {
			
			String tmpLine = "";
			
			if (r[i].getUserType().equals("CUSTOMER")) {
				Customer customer = model.getUserByUsername(r[i].getSiteUser());
				
				tmpLine = "					<p> <img class=\"user_image\" style=\"float:left;width:30px;height:30px;vertical-align:center;\" src=\"/res/user_logo.png\" /> "+customer.getSurName() + ", "+ customer.getGivenName() + " " + r[i].getRating() + " / 5 </p>";
			} else {
				tmpLine = "					<p> <img class=\"user_image\" style=\"float:left;width:30px;height:30px;vertical-align:center;\" src=\"/res/user_logo.png\" /> <i> site visitor </i> " + r[i].getRating() + " / 5 </p>";
			}
			
			html +=   "				<div class=\"review_row\" style=\"margin-top:50px;\">\n"
					+ tmpLine
					+ "					<div class=\"container_title  \">"+r[i].getTitle()+"</div>\n"
					+ "					<p>\n"
					+ "						"+r[i].getBody()
					+ "					</p>\n"
					+ "				</div>\n";
					
			
		}
		
		if (r.length == 0)
			request.setAttribute(NUM_REVIEWS_FOUND, "0");
		else
			request.setAttribute(NUM_REVIEWS_FOUND, r.length);

		return html;
	}
	
	/**
	 * Sets the attributes related to review of a book
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	private void setAttributes (HttpServletRequest request, MainPageModel model, String id) throws Exception{
		Book b = model.getBookByID(id);
		
		request.setAttribute(TITLE, b.getTitle());
		request.setAttribute(AUTHOR, b.getAuthor());
		request.setAttribute(YEAR, b.getPublishYear());
		
	}
}
