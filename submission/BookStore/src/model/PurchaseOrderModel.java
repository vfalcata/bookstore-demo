package model;

import javax.servlet.http.HttpServletRequest;

import data.beans.Address;
import data.beans.CreditCard;
import data.dao.BookDAO;
import data.dao.CustomerDAO;
import data.dao.PurchaseOrderDAO;
import data.dao.ReviewDAO;
import data.dao.VisitorDAO;

public class PurchaseOrderModel {

	public static PurchaseOrderModel instance;
	private PurchaseOrderDAO purchaseOrderDAO;
	
	public boolean isCustomerLoggedIn(HttpServletRequest request) {
		return true;
		
	}
	
	

	public CreditCard validateCreditCard(CreditCard creditCard) {
		String creditCardType=creditCard.getCreditCardType().toLowerCase().equals("visa")||creditCard.getCreditCardType().toLowerCase().equals("mastercard")?creditCard.getCreditCardType():"";
		String creditCardNumber=creditCard.getCreditCardNumber().matches("[0-9]{16}")?creditCard.getCreditCardNumber():"";
		String creditCardExpiry=creditCard.getCreditCardExpiry().matches("[0-9]{2}-[0-9]{4}")?creditCard.getCreditCardExpiry():"";
		String creditCardCVV2=creditCard.getCreditCardCVV2().matches("[0-9]{3}")?creditCard.getCreditCardCVV2():"";
		return new CreditCard.Builder().withCreditCardType(creditCardType).withCreditCardExpiry(creditCardExpiry).withCreditCardNumber(creditCardNumber).withCreditCardCVV2(creditCardCVV2).build();
	}
	
	public Address validateAddress(Address address) {
		boolean isProvince=address.getProvince().toLowerCase().equals("ontario")||
				address.getProvince().toLowerCase().equals("british columbia")||
				address.getProvince().toLowerCase().equals("quebec")||
				address.getProvince().toLowerCase().equals("alberta")||
				address.getProvince().toLowerCase().equals("Manitoba")||
				address.getProvince().toLowerCase().equals("New Brunswick")||
				address.getProvince().toLowerCase().equals("Newfoundland")||
				address.getProvince().toLowerCase().equals("Northwest Territories")||
				address.getProvince().toLowerCase().equals("Nova Scotia")||
				address.getProvince().toLowerCase().equals("Nunavut")||
				address.getProvince().toLowerCase().equals("Prince Edward Island")||
				address.getProvince().toLowerCase().equals("Saskatchewan")||
				address.getProvince().toLowerCase().equals("Yukon");
		String number=address.getNumber();
		String street=address.getStreet();
		String city=!address.getCity().matches("[0-9]+")?address.getCity():"";
		String province=isProvince?address.getProvince():"";
		String country=address.getCountry().toLowerCase().equals("canada")?address.getCountry():"";
		String postalCode=address.getPostalCode().toUpperCase().replaceAll("^A-Za-z0-9", "").matches("[A-Z]{1}[0-9]{1}[A-Z]{1}[0-9]{1}[A-Z]{1}[0-9]{1}")?address.getPostalCode():"";

		return new Address.Builder().withCity(city).withNumber(number).withStreet(street).withCountry(country).withPostalCode(postalCode).withProvince(province).build();
	}
	
	public static PurchaseOrderModel getInstance()throws ClassNotFoundException{
		if (instance==null) {
			instance =new PurchaseOrderModel();
		}			
		return instance;
	}
}
