package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.beans.Address;
import data.beans.CreditCard;
import data.beans.Customer;
import model.UserAuthenticationModel;

/**
 * Servlet implementation class Register
 */
@WebServlet(urlPatterns={"/Register", "/Register/*"})
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserAuthenticationModel UAuthModel;
	final String SIGNIN_TARGET = "/html/SignIn.jspx";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
    	UAuthModel = UserAuthenticationModel.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String givenName = request.getParameter("givenName");
		String surname = request.getParameter("surname");
		
		String number = request.getParameter("number");
		String street = request.getParameter("street");
		String city = request.getParameter("city");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String postalCode = request.getParameter("postalCode");
		
		String cardType = request.getParameter("cardType");
		String ccNumber = request.getParameter("ccNumber");
		String cvv = request.getParameter("cvv");
		String expiry = request.getParameter("expiry");
		
		response.setContentType("application/text");
		PrintWriter out = response.getWriter();
		
		Address address = new Address.Builder()
				.withNumber(number)
				.withStreet(street)
				.withCity(city)
				.withProvince(province)
				.withCountry(country)
				.withPostalCode(postalCode)
				.build();
		
		CreditCard cCard = new CreditCard.Builder()
				.withCreditCardType(cardType)
				.withCreditCardNumber(ccNumber)
				.withCreditCardCVV2(cvv)
				.withCreditCardExpiry(expiry)
				.build();
		
		Customer customer = new Customer.Builder()
				.withEmail(email)
				.withGivenName(givenName)
				.withSurName(surname)
				.withUserName(username)
				.withPassword(password)
				.withAddress(address)
				.withCreditCard(cCard)
				.build();
		
		//validate variables
		List<String> errors = UAuthModel.validateRegister(customer);
		String responseText = "";
		if(!errors.isEmpty() || UAuthModel.isUserRegistered(username))
		{
			response.setStatus(403);
			errors.add(0, "Username already exists!");
			responseText = errors.toString();
		}
		else
		{
			
			UAuthModel.registerUser(customer);
			
			if(UAuthModel.isUserRegistered(username))
			{
				//continue to sign in page
				//request.getRequestDispatcher(SIGNIN_TARGET).forward(request, response);
				//return;
			}
			else
			{
				response.setStatus(403);
				responseText = "Something went wrong. Please Try again later.";
				//error
			}
			
		}
		
		out.printf(responseText);
		out.flush();
		
	}

}
