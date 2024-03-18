package ctrl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.dao.PurchaseOrderDAO;
import model.MainPageModel;


/**
 * Servlet implementation class Orders
 */
@WebServlet("/Orders")
public class Orders extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private static final String MODEL = "model";
    private static final String review_form = "review_form";
    private static final String addReview = "addReview";
    private static final String viewReview = "viewReview";
    private static final String bookID = "bookID";
    private static final String AUTHOR = "AUTHOR";
    private static final String TITLE = "TITLE";
    private static final String BOOKS_IN_ORDER = "BOOKS_IN_ORDER";
    
    private static final String USER_ORDERS = "USER_ORDERS";
    private static final String rate = "rate";
    private static final String title = "title";
    private static final String body = "body";
    private static final String epoch = "epoch";
    
    private static final String ONE_STAR = "ONE_STAR";
    private static final String TWO_STAR = "TWO_STAR";
    private static final String THREE_STAR = "THREE_STAR";
    private static final String FOUR_STAR = "FOUR_STAR";
    private static final String FIVE_STAR = "FIVE_STAR";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Orders() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
    	super.init(config);

	    ServletContext context = getServletContext();
	    
	    try {
	    	MainPageModel model = MainPageModel.getInstance();
		    
		    context.setAttribute( MODEL, model);
	    }
	    catch (Exception e) {
	    	System.out.println("ERROR initializeing main page model!");
	    }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println(request.getParameterNames().toString());
		HttpSession h = request.getSession();
		
		ServletContext context = getServletContext();
		MainPageModel model = (MainPageModel) context.getAttribute(MODEL);
		
		Customer customer = (Customer) h.getAttribute("customer");
		
		System.out.println("\n\nCustomer ID: "+customer.getId() + "\n\n");
		
		// customer just submitted a review - add review to database and go back to customer's specific order
		if (request.getParameter(review_form) != null) {

			try {
				System.out.println("\t1. get epoch! this_epoch = "+h.getAttribute(epoch));
				long this_epoch = (long) h.getAttribute(epoch);
				System.out.println("\t1. get epoch!");
				request.setAttribute(epoch, this_epoch);
				
				System.out.println("\t2. get rank!");
				int rank = Integer.parseInt(request.getParameter(rate));
				
				System.out.println("\t3. get title!");
				String this_title = request.getParameter(title);
				
				System.out.println("\t4. get body!");
				String this_body = request.getParameter(body);
				
				System.out.println("\t5. get book id!");
				String book_id = request.getParameter(bookID);
				
				// add review 
				System.out.println("About to add review!");
				model.addReview(customer, this_title, this_body, rank, book_id);
				
				System.out.println("Review was successfully submitted!");
				
				String html = loadIndividualOrder(model, customer, this_epoch);
				request.setAttribute(BOOKS_IN_ORDER, html);
				
				
				h.setAttribute("customer", customer);
				System.out.println("\n\nCustomer ID: "+customer.getId() + "\n\n");
				
				request.getRequestDispatcher("html/ProdOrderView.jspx").forward(request, response);
				
			} catch (Exception e) {
				System.out.println("There was a problem going back form user submitted review to specific order page! " +e.getMessage());
			}
		}
		
		// user selects a specific order to view
		else if (request.getParameter(epoch)!= null) {
			try {
			    Long this_epoch = Long.parseLong(request.getParameter(epoch));
				h.setAttribute(epoch, this_epoch);

				String html = loadIndividualOrder(model, customer, this_epoch);
				request.setAttribute(BOOKS_IN_ORDER, html);
			
				
				h.setAttribute("customer", customer);
				System.out.println("\n\nCustomer ID: "+customer.getId() + "\n\n");
				
				
				request.getRequestDispatcher("html/ProdOrderView.jspx").forward(request, response);
			} catch (Exception e) {
				System.out.println("There was a problem going back form orders to a specific order page! " +e.getMessage());
			}
			
		// user selects adding/ editing/ reviewing a review	
		} else if (request.getParameter(viewReview) != null || request.getParameter(addReview) != null) {
			try {
				String book_id = request.getParameter(bookID);
				
				if (request.getParameter(viewReview) != null) {
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
				
				String title = request.getParameter(TITLE);
				String author = request.getParameter(AUTHOR);
				request.setAttribute(bookID, book_id);
				request.setAttribute(TITLE, title);
				request.setAttribute(AUTHOR, author);
				request.setAttribute("redirectTo", "Orders");

				
				h.setAttribute("customer", customer);
				System.out.println("\n\nCustomer ID: "+customer.getId() + "\n\n");
				
				
				request.getRequestDispatcher("html/Review.jspx").forward(request, response);
			
			} catch (Exception e) {
				System.out.println("There was a problem going back form a specific order page to writing its review! " +e.getMessage());
			}
		} else {
			try {
				String html = loadPage(customer,  model);
				request.setAttribute(USER_ORDERS, html);
				
				request.getRequestDispatcher("html/Orders.jspx").forward(request, response);
			} catch (Exception e) {
				System.out.println("Error loading user's orders! "+e.getMessage());
			}
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
	 * This method loads all the orders for this customer
	 * 
	 * @param customer
	 * @param model
	 * @return
	 */
	private String loadPage(Customer customer, MainPageModel model) {
		
		PurchaseOrder[] orders = null;
		String html = "";
		
		try {
			orders = customer.getPurchaseOrders();
			
			System.out.println("orders: "+orders.length);
			Iterator iterator;
			double cost;
			int numBooks;
			Map<Book, Integer> b;
			
			for (int i = 0; i < orders.length; i ++) {
				cost = 0; numBooks = 0;
				b = orders[i].getBooks();
				iterator = b.entrySet().iterator(); 
				
				while (iterator.hasNext()) {
					
					Map.Entry me = (Map.Entry) iterator.next(); 
					Book b1=(Book)me.getKey();
					cost += b1.getPrice();
					numBooks += (int) me.getValue();
				}
				
				cost = (double)(Math.ceil(cost*100.0))/100.0;
				
				html += 
				 	  "						<tr class=\"entry\">\n"
				 	  + "						<th>"+orders[i].getStatus()+"</th>\n"
				 	  + "						<th> $"+cost+"</th>\n"
				 	  + "						<th>"+numBooks+"</th>\n"
				 	  + "						<th><button style=\"width:90%;height:90%;\" id=\""+epoch+"\" name=\""+epoch+"\" value=\""+orders[i].getCreatedAtEpoch()+"\">view order</button></th>\n"
				 	  + "					</tr>";
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("An error occured getting the orders. "+e.getMessage());
		}
			
		return html;
	}
	
	/**
	 * this method loads all the books associated with the specified order
	 * 
	 * @param model
	 * @param customer
	 * @param epoch
	 * @return
	 * @throws Exception
	 */
	private String loadIndividualOrder(MainPageModel model, Customer customer, Long epoch) throws Exception {
		String html = "";
		PurchaseOrder order = null;
		Iterator iterator = null;
		
		order = customer.getPurchaseOrderByCreatedAtEpoch(epoch);
		System.out.println("order = "+order);
		
		Map<Book, Integer> books = order.getBooks();
		iterator = books.entrySet().iterator(); 
		
		while (iterator.hasNext()) {
			
			Map.Entry me = (Map.Entry) iterator.next(); 
			Book b=(Book)me.getKey();
			int numBooks = (int) me.getValue();
			String book_id = b.getId().toString();
			
			String review = ""; 

			if (model.didCustomerAddReview(customer, book_id)) 

				review += "<p style=\"margin-left:0px;\"><button id=\""+viewReview+"\" name=\""+viewReview+"\" class=\"button "+viewReview+"\" type=\""+viewReview+"\">view your review</button></p>\n";
			else
				review += "<p style=\"margin-left:0px;\"><button id=\""+addReview+"\" name=\""+addReview+"\" class=\"button "+addReview+"\"  type=\""+addReview+"\">write a review</button></p>\n";
			
			html += 
					"		<FORM action=\"/Orders\" method=\"Post\">\n"
					+ "			<div class=\"row\">\n"
					+ "				<div class=\"column_1_3\">\n"
					+ "					<img class=\"prod_img\" style=\"float:center;height:100%;\" src=\"/res/book_images/covers/"+b.getCover()+"\" alt=\"search\" /> \n"
					+ "				</div>\n"
					+ "				<div class=\"column_1_3\">\n"
					+ "					<div class=\"row\">\n"
					+ "						<h2>"+b.getTitle()+", "+b.getPublishYear()+"</h2> <h3>by "+b.getAuthor()+"</h3>\n"
					+ "					</div>\n"
					+ "					<div class=\"row\" >\n"
					+ "						ISBN: "+b.getISBN()+" <BR />\n"
					+ "						Quantity: "+numBooks+"<BR/>\n"
					+ "						<p style=\"color:red;font-weight:bold;margin-left:0px;\">$"+b.getPrice()+"</p>\n"
					+ "					</div>\n"
					+ "					<div class=\"row\">\n"
					+ "						<input type=\"hidden\" id=\""+TITLE+"\" name=\""+TITLE+"\" value=\""+b.getTitle()+"\"></input>\n"
					+ "						<input type=\"hidden\" id=\""+AUTHOR+"\" name=\""+AUTHOR+"\" value=\""+b.getAuthor()+"\"></input>\n"
					+ "						<input type=\"hidden\" id=\""+bookID+"\" name=\""+bookID+"\" value=\""+b.getId()+"\"></input>\n"
					+ "						" + review
					+ "					</div>\n"
					+ "				</div>\n"
					+ "			</div>\n"
					+ "		</FORM>";
		}
			
		return html;
	}
}
