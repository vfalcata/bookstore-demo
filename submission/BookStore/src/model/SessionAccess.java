package model;

import javax.servlet.http.HttpSession;

import data.beans.Cart;
import data.beans.Customer;
import data.beans.SiteUser;
import data.beans.Visitor;

public class SessionAccess {
	
	private static final String CUSTOMER = "customer";
	private static final String VISITOR = "visitor";
	
	
	private SessionAccess()
	{
		
	}
	
	public static Cart getCart(SiteUser user)
	{
		return user.getCart();
	}
	
	public static Customer getCustomer(HttpSession session)
	{
		return (Customer) session.getAttribute(CUSTOMER);
	}
	
	public static Visitor getVisitor(HttpSession session)
	{
		return (Visitor) session.getAttribute(VISITOR);
	}
	
	public static boolean isCustomerInSession(HttpSession session)
	{
		return (session.getAttribute(CUSTOMER) != null);
	}
	
	public static void setVisitor(HttpSession session, Visitor visitor)
	{
		session.setAttribute(VISITOR, visitor);
	}
	
	public static void setCustomer(HttpSession session, Customer customer)
	{
		session.setAttribute(CUSTOMER, customer);
	}
	
}
