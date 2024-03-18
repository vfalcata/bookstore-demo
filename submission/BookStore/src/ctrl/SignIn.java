package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.beans.Customer;
import data.beans.Visitor;
import model.UserAuthenticationModel;
import model.MainPageModel;
import model.SessionAccess;
import model.ShoppingCartModel;

/**
 * Servlet implementation class Login
 */
@WebServlet(urlPatterns={"/SignIn", "/SignIn/*"})
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserAuthenticationModel UAuthModel;
	ShoppingCartModel shoppingModel;
	final String MAIN_PAGE_TARGET = "/MainPage";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignIn() {
        UAuthModel = UserAuthenticationModel.getInstance();
        shoppingModel = ShoppingCartModel.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n\n\t\tLogin out\n\n");
		UAuthModel.logUserOut(request.getSession());
		
		try {
			HttpSession session = request.getSession();
			MainPageModel model;
			model = MainPageModel.getInstance();
			session.setAttribute("visitor", model.getVisitor(request));
		} catch (ClassNotFoundException e) {
			
		}
		
		response.sendRedirect(MAIN_PAGE_TARGET); // redirects to main page
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		if (request.getParameter("cameFromPage") != null) {
			System.out.println("\t\tcameFromPage="+request.getParameter("cameFromPage"));
			this.doDelete(request, response);
		} else {
			HttpSession session = request.getSession();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			Visitor visitor = (Visitor) session.getAttribute("visitor");
			
			response.setContentType("application/text");
			PrintWriter out = response.getWriter();
			//validate variables
			List<String> errors = UAuthModel.validateSignIn(username, password);
			String responseText = "";
			if(!errors.isEmpty())
			{
				response.setStatus(403);
				responseText = errors.toString();
			}
			else
			{
				//log user in
				Customer customer = UAuthModel.logUserIn(username, password);
				if(customer.isLoggedOn())
				{
					session.setAttribute("customer", customer);
					//transfer cart
					if(visitor != null)
					{
						shoppingModel.addAllFromCart(visitor, customer);
					}
					//go to main page
					
					//response.sendRedirect(MAIN_PAGE_TARGET);
					//return;
					//request.getRequestDispatcher(MAIN_PAGE_TARGET).forward(request, response);
				}
				//non existent user
				else
				{
					response.setStatus(403);
					responseText = "Invalid Credentials!";
					
				}
			}
			out.printf(responseText);
			out.flush();
		}
	}

}
