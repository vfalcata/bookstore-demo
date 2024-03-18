package model;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpSession;

import data.beans.Customer;
import data.dao.CartDAO;
import data.dao.CustomerDAO;

public class UserAuthenticationModel {

	private static UserAuthenticationModel instance;
	CustomerDAO customerDAO;
	CartDAO cartDAO;
	
	private UserAuthenticationModel()
	{
		customerDAO = new CustomerDAO();
		cartDAO = new CartDAO();
	}
	
	public static UserAuthenticationModel getInstance()
	{
		if(instance == null)
		{
			instance = new UserAuthenticationModel();
		}
		
		return instance;
	}
	
	
	public List<String> validateSignIn(String username, String password)
	{
		List<String> errors = new ArrayList<String>();
		
		if(username.length() <= 0)
		{
			errors.add("Username cannot be empty");
		}
		if(password.length() <= 0)
		{
			errors.add("Password cannot be empty");
		}
		return errors;
	}
	
	public List<String> validateRegister(Customer customer)
	{
		List<String> errors = new ArrayList<String>();
		
		if(customer.getUserName().length() <= 0)
		{
			errors.add("Username cannot be empty");
		}
		if(customer.getPassword().length() <= 0)
		{
			errors.add("Password cannot be empty");
		}
		if(customer.getEmail().length() <= 0)
		{
			errors.add("Email cannot be empty");
		}
		if(customer.getGivenName().length() <= 0)
		{
			errors.add("Given name cannot be empty");
		}
		if(customer.getSurName().length() <= 0)
		{
			errors.add("Surname cannot be empty");
		}
		
		if(customer.getAddress().getNumber().length() <= 0)
		{
			errors.add("Street number cannot be empty");
		}
		if(customer.getAddress().getStreet().length() <= 0)
		{
			errors.add("Street cannot be empty");
		}
		if(customer.getAddress().getCity().length() <= 0)
		{
			errors.add("City cannot be empty");
		}
		if(customer.getAddress().getProvince().length() <= 0)
		{
			errors.add("Province name cannot be empty");
		}
		if(customer.getAddress().getCountry().length() <= 0)
		{
			errors.add("Country cannot be empty");
		}
		if(customer.getAddress().getPostalCode().length() <= 0)
		{
			errors.add("Postal Code cannot be empty");
		}
		if(customer.getCreditCard().getCreditCardNumber().length() <= 0)
		{
			errors.add("Credit card number cannot be empty");
		}
		if(customer.getCreditCard().getCreditCardType().length() <= 0)
		{
			errors.add("Credit card type cannot be empty");
		}
		if(customer.getCreditCard().getCreditCardCVV2().length() <= 0)
		{
			errors.add("CVV name cannot be empty");
		}
		if(customer.getCreditCard().getCreditCardExpiry().length() <= 0)
		{
			errors.add("Expiry cannot be empty");
		}
		
		return errors;
	}
	
	public Customer logUserIn(String username, String password)
	{
		return customerDAO.loginCustomer(username, password);
	}
	
	public void logUserOut(HttpSession session)
	{
		session.setAttribute("customer", null);
	}
	
	public void registerUser(Customer newCustomer)
	{
		customerDAO.newUpdateRequest()
			.requestNewCustomerInsertion()
			.insertCustomerWithGivenName(newCustomer.getGivenName())
			.insertCustomerWithSurName(newCustomer.getSurName())
			.insertCustomerWithUserName(newCustomer.getUserName())
			.insertCustomerWithPassWord(newCustomer.getPassword())
			.insertCustomerWithEmail(newCustomer.getEmail())
			.insertCustomerWithStreetNumber(newCustomer.getAddress().getNumber())
			.insertCustomerWithStreet(newCustomer.getAddress().getStreet())
			.insertCustomerWithPostalCode(newCustomer.getAddress().getPostalCode())
			.insertCustomerWithCity(newCustomer.getAddress().getCity())
			.insertCustomerWithProvince(newCustomer.getAddress().getProvince())
			.insertCustomerWithCountry(newCustomer.getAddress().getCountry())
			.executeCustomerInsertion();
		
	}
	
	public boolean isUserRegistered(String username)
	{
		boolean isRegistered = false;
		
		List<Customer> customers = customerDAO.newQueryRequest()
			.includeCustomerUserNameInResult()
			.queryAttribute()
			.whereCustomerUserName()
			.varCharEquals(username)
			.executeQuery()
			.executeCompilation()
			.compileCustomers();
		
		if(!customers.isEmpty())
		{
			isRegistered = true;
		}
		
		return isRegistered;
	}
	
	
}
