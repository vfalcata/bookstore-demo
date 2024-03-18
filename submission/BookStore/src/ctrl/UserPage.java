package ctrl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.beans.Address;
import data.beans.Customer;
import model.MainPageModel;

/**
 * Servlet implementation class UserPage
 */
@WebServlet("/UserPage")
public class UserPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String MODEL = "model";
	
	private static final String NAME = "name";
	private static final String USERNAME = "username";
	private static final String LAST_NAME = "last_name";
	private static final String EMAIL = "email";
	private static final String STREET = "street";
	private static final String STREET_NUMBER = "street_number";
	private static final String POSTAL_CODE = "postal_code";
	private static final String CITY = "city";
	private static final String PROVINCE = "province";
	private static final String COUNTRY = "country";
	private static final String PASSWORD = "password";
	
	private static final String update_information = "update_information";
	private static final String errorMsg="errorMsg";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserPage() {
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		HttpSession h = request.getSession();
		
		ServletContext context = getServletContext();
		MainPageModel model = (MainPageModel) context.getAttribute(MODEL);
		
		Customer customer = (Customer) h.getAttribute("customer");
		
		String this_username = customer.getUserName();
		String passwd = customer.getPassword();
		String this_name = customer.getGivenName();
		String this_last_name =  customer.getSurName();
		String this_email = customer.getEmail();
		Address address =  customer.getAddress();
		
		if (request.getParameter(update_information) != null) {
			
			try {
				String street_num = (String) request.getParameter(STREET_NUMBER);
				String postal_code = (String) request.getParameter(POSTAL_CODE);
				String city = (String) request.getParameter(CITY);
				String province = (String) request.getParameter(PROVINCE);
				String country = (String) request.getParameter(COUNTRY);
				String street = (String) request.getParameter(STREET);
				
				String email = (String) request.getParameter(EMAIL);
				String username = (String) request.getParameter(USERNAME);
				String surname = (String) request.getParameter(LAST_NAME);
				String name = (String) request.getParameter(NAME);
				String password = (String) request.getParameter(PASSWORD);
				
				if (password != null && password.equals("********"))
					password = customer.getPassword();
				
				model.updateUserInfo(username, password, customer, name, surname, email, 
									 street, street_num, city, province, country, postal_code);
				
				customer = model.getUser(username, passwd); // will update the current instance in the session
				h.setAttribute("customer", customer);
				
				String msg = "Personal information was successfully updated! You can continue browsing the store";
				request.setAttribute(USERNAME, customer.getUserName());
				request.setAttribute(NAME, customer.getGivenName());
				request.setAttribute(LAST_NAME, customer.getSurName());
				request.setAttribute(EMAIL, customer.getEmail());
				request.setAttribute(STREET, customer.getAddress().getStreet());
				request.setAttribute(STREET_NUMBER, customer.getAddress().getNumber());
				request.setAttribute(POSTAL_CODE, customer.getAddress().getPostalCode());
				request.setAttribute(CITY, customer.getAddress().getCity());
				request.setAttribute(PROVINCE, customer.getAddress().getProvince());
				request.setAttribute(COUNTRY, customer.getAddress().getCountry());
				request.setAttribute("SuccessMsg", msg);
				
				request.getRequestDispatcher("html/UserPage.jspx").forward(request, response);
			
				
			} catch (Exception e) {
				
				System.out.println("An error occured - " +e.getMessage() + " \n\t" +e.getCause() );
				
				request.setAttribute(USERNAME, this_username);
				request.setAttribute(NAME, this_name);
				request.setAttribute(LAST_NAME, this_last_name);
				request.setAttribute(EMAIL, this_email);
				request.setAttribute(STREET, address.getStreet());
				request.setAttribute(STREET_NUMBER, address.getNumber());
				request.setAttribute(POSTAL_CODE, address.getPostalCode());
				request.setAttribute(CITY, address.getCity());
				request.setAttribute(PROVINCE, address.getProvince());
				request.setAttribute(COUNTRY, address.getCountry());
				
				request.setAttribute(errorMsg, e.getMessage());
				request.getRequestDispatcher("html/UserPage.jspx").forward(request, response);
			}
		} else {
			request.setAttribute(USERNAME, this_username);
			request.setAttribute(NAME, this_name);
			request.setAttribute(LAST_NAME, this_last_name);
			request.setAttribute(EMAIL, this_email);
			request.setAttribute(STREET, address.getStreet());
			request.setAttribute(STREET_NUMBER, address.getNumber());
			request.setAttribute(POSTAL_CODE, address.getPostalCode());
			request.setAttribute(CITY, address.getCity());
			request.setAttribute(PROVINCE, address.getProvince());
			request.setAttribute(COUNTRY, address.getCountry());
			
			request.getRequestDispatcher("html/UserPage.jspx").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

}
