package data.dao;


import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;

import data.beans.Book;
import data.beans.CreditCard;
import data.beans.Customer;
import data.beans.Id;
import data.dao.exceptions.UpdateDBFailureException;
import data.fetcher.CustomerDataFetcher;
import data.schema.BookSchema;
import data.schema.CustomerSchema;
import data.schema.UserTypes;

public class UpdateCustomer extends DataUpdate{	
	
	UpdateCustomer(){
		
	}
	
	public CustomerUpdater requestUpdateExistingCustomer(Customer customer) {
		return new CustomerUpdater(customer);
	}
	
	public void requestUpdateCustomerCreditCard(Customer customer) {
		
	}
	
	public class InsertCustomerCreditCardType extends CustomerCreditCardInsert{

		private InsertCustomerCreditCardType (Customer customer,CreditCard creditCard) {
			super(customer,creditCard);
		}
		
		public InsertCustomerCreditCardNumber insertCustomerWithCreditCardType(String creditCardType){
			return new InsertCustomerCreditCardNumber(customer, new CreditCard.Builder(creditCard).withCreditCardType(creditCardType).build());
		}
		
	}	
	public class InsertCustomerCreditCardNumber extends CustomerCreditCardInsert{

		private InsertCustomerCreditCardNumber (Customer customer,CreditCard creditCard) {
			super(customer,creditCard);
		}
		
		public InsertCustomerCreditCardExpiry insertCustomerWithCreditCardNumber(String creditCardNumber){
			return new InsertCustomerCreditCardExpiry(customer, new CreditCard.Builder(creditCard).withCreditCardNumber(creditCardNumber).build());
		}
		
	}	
	public class InsertCustomerCreditCardExpiry extends CustomerCreditCardInsert{

		private InsertCustomerCreditCardExpiry (Customer customer,CreditCard creditCard) {
			super(customer,creditCard);
		}
		
		public InsertCustomerCreditCardCVV2 insertCustomerCreditCardExpiry(String creditCardExpiry){
			return new InsertCustomerCreditCardCVV2(customer, new CreditCard.Builder(creditCard).withCreditCardExpiry(creditCardExpiry).build());
		}
		
	}	
	public class InsertCustomerCreditCardCVV2 extends CustomerCreditCardInsert{

		private InsertCustomerCreditCardCVV2 (Customer customer,CreditCard creditCard) {
			super(customer,creditCard);
		}
		
		public ExecuteCustomerCreditCardInsert insertCustomerWithCreditCardCVV2(String creditCardCVV2){
			return new ExecuteCustomerCreditCardInsert(customer, new CreditCard.Builder(creditCard).withCreditCardCVV2(creditCardCVV2).build());
		}
		
	}
	
	public class ExecuteCustomerCreditCardInsert extends CustomerCreditCardInsert{

		ExecuteCustomerCreditCardInsert(Customer customer, CreditCard creditCard) {
			super(customer, creditCard);
		}
		
		public 	void executeCustomerCreditCardInsertion(){
			if(customer==null|| customer.getId()==null|| customer.getId().isEmpty()||
					creditCard==null || creditCard.getCreditCardCVV2()==null || creditCard.getCreditCardType()==null || creditCard.getCreditCardNumber()==null || creditCard.getCreditCardType()==null||
					 creditCard.getCreditCardCVV2().isEmpty() || creditCard.getCreditCardType().isEmpty()|| creditCard.getCreditCardNumber().isEmpty() || creditCard.getCreditCardType().isEmpty()) {
				System.err.println("warning, requested insertion of credit card with empty or null values, insertion failed");
				return;
			}

			String update = "UPDATE CUSTOMER SET ";
			update+=CustomerSchema.CREDIT_CARD+"='"+creditCard.getCreditCardType()+"', ";
			update+=CustomerSchema.CREDIT_CARD_NUMBER+"='"+creditCard.getCreditCardNumber()+"', ";
			update+=CustomerSchema.CREDIT_CARD_EXPIRY+"='"+creditCard.getCreditCardExpiry()+"', ";
			update+=CustomerSchema.CREDIT_CARD_CVV2+"='"+creditCard.getCreditCardCVV2()+"' ";
			update=update.substring(0,update.length()-1);
			update+=" WHERE ID='"+customer.getId().toString()+"'";
			sendUpdateToDatabase(update);			
		}
		
	}
	
	public InsertCustomerGivenName requestNewCustomerInsertion() {
		return new InsertCustomerGivenName(new Customer.Builder().build());
	}

	abstract class CustomerInsert extends DataUpdate{
		protected Customer customer;
		CustomerInsert(Customer customer){
			this.customer=customer;
		}
	}
	
	abstract class CustomerCreditCardInsert extends DataUpdate{
		protected Customer customer;
		protected CreditCard creditCard;
		CustomerCreditCardInsert(Customer customer,CreditCard creditCard){
			this.creditCard=creditCard;
		}
	}
	
	public class InsertCustomerGivenName extends CustomerInsert{
		private InsertCustomerGivenName(Customer customer) {
			super(customer);
		}
		public InsertCustomerSurname  insertCustomerWithGivenName(String givenName){
			return new InsertCustomerSurname(new Customer.Builder(customer).withGivenName(surroundWithQuotes(givenName)).build());
		}
		
	}
	
	public class InsertCustomerSurname extends CustomerInsert{

		private InsertCustomerSurname(Customer customer) {
			super(customer);
		}
		
		public InsertCustomerUserName  insertCustomerWithSurName(String surName){
			return new InsertCustomerUserName(new Customer.Builder(customer).withSurName(surroundWithQuotes(surName)).build());
		}
		
	}
	public class InsertCustomerUserName extends CustomerInsert{

		private InsertCustomerUserName (Customer customer) {
			super(customer);
		}
		
		public InsertCustomerPassWord  insertCustomerWithUserName(String userName){
			return new InsertCustomerPassWord(new Customer.Builder(customer).withUserName(surroundWithQuotes(userName)).build());
		}
		
	}
	public class InsertCustomerPassWord extends CustomerInsert{

		private InsertCustomerPassWord(Customer customer) {
			super(customer);
		}
		
		public InsertCustomerEmail  insertCustomerWithPassWord(String password){
			return new InsertCustomerEmail(new Customer.Builder(customer).withPassword(surroundWithQuotes(password)).build());
		}
		
	}	
	public class InsertCustomerEmail extends CustomerInsert{

		private InsertCustomerEmail (Customer customer) {
			super(customer);
		}
		
		public InsertCustomerStreetNumber  insertCustomerWithEmail(String email){
			return new InsertCustomerStreetNumber(new Customer.Builder(customer).withEmail(surroundWithQuotes(email)).build());
		}
		
	}	
	public class InsertCustomerStreetNumber extends CustomerInsert{

		private InsertCustomerStreetNumber (Customer customer) {
			super(customer);
		}
		
		public InsertCustomerStreet  insertCustomerWithStreetNumber(String streetNumber){
			return new InsertCustomerStreet(new Customer.Builder(customer).withStreetNumber(surroundWithQuotes(streetNumber)).build());
		}
		
	}	
	public class InsertCustomerStreet extends CustomerInsert{

		private InsertCustomerStreet (Customer customer) {
			super(customer);
		}
		
		public InsertCustomerPostalCode  insertCustomerWithStreet(String street){
			return new InsertCustomerPostalCode(new Customer.Builder(customer).withStreet(surroundWithQuotes(street)).build());
		}
		
	}	
	public class InsertCustomerPostalCode extends CustomerInsert{

		private InsertCustomerPostalCode (Customer customer) {
			super(customer);
		}
		
		public InsertCustomerCity  insertCustomerWithPostalCode(String postalCode){
			return new InsertCustomerCity(new Customer.Builder(customer).withPostalCode(surroundWithQuotes(postalCode)).build());
		}
		
	}	
	public class InsertCustomerCity extends CustomerInsert{

		private InsertCustomerCity (Customer customer) {
			super(customer);
		}
		
		public InsertCustomerProvince insertCustomerWithCity(String city){
			return new InsertCustomerProvince(new Customer.Builder(customer).withCity(surroundWithQuotes(city)).build());
		}
		
	}	
	public class InsertCustomerProvince extends CustomerInsert{

		private InsertCustomerProvince (Customer customer) {
			super(customer);
		}
		public InsertCustomerCountry insertCustomerWithProvince(String province){
			return new InsertCustomerCountry(new Customer.Builder(customer).withProvince(surroundWithQuotes(province)).build());
		}
	}	
	public class InsertCustomerCountry extends CustomerInsert{

		private InsertCustomerCountry (Customer customer) {
			super(customer);
		}
		
		public ExecuteCustomerInsert insertCustomerWithCountry(String country){
			return new ExecuteCustomerInsert(new Customer.Builder(customer).withCountry(surroundWithQuotes(country)).build());
		}
		
	}	
	public class ExecuteCustomerInsert extends CustomerInsert{

		private ExecuteCustomerInsert (Customer customer) {
			super(customer);
		}
		public 	Customer executeCustomerInsertion() {

		    String epoch =Long.toString(Instant.now().getEpochSecond());
			String id =UUID.nameUUIDFromBytes(customer.getUserName().getBytes()).toString();
			String userTablesUpdate = "INSERT INTO SITE_USER (ID,USER_TYPE) VALUES ('"+id+"','"+UserTypes.CUSTOMER+"')";
			sendUpdateToDatabase(userTablesUpdate);
			String update="INSERT INTO CUSTOMER (ID,GIVENNAME,SURNAME,USERNAME,PASSWORD ,EMAIL,STREET_NUMBER,STREET,POSTAL_CODE,CITY,PROVINCE,COUNTRY,CREATED_AT_EPOCH) VALUES "+
					"('"+id+"',"+customer.getGivenName()+","+customer.getSurName()+","+customer.getUserName()+","+customer.getPassword()+","+customer.getEmail()+","+
					customer.getAddress().getNumber()+","+customer.getAddress().getStreet()+","+customer.getAddress().getPostalCode()+","+customer.getAddress().getCity()+","+customer.getAddress().getProvince()+","+customer.getAddress().getCountry()+","+epoch+")";
			System.out.println("up cust req: "+update);
			sendUpdateToDatabase(update);
			String check="SELECT COUNT() CUST_COUNT FROM CUSTOMER WHERE ID='"+id+"' AND EMAIL='"+customer.getEmail()+"' AND USERNAME='"+customer.getUserName()+"' AND PASSWORD='"+customer.getPassword()+"'";

			return new Customer.Builder(customer).withId(new Id(id)).build();
			
		}
	}
	
	public class CustomerUpdater extends DataUpdate{
		Map<String,String> updateRequest;
		private Customer customer;
		private CustomerSchema customerSchema = new CustomerSchema();
		private CustomerUpdater(Customer customer){
			this.updateRequest=new LinkedHashMap<String, String>();
			this.customer=customer;
		}
		public CustomerUpdater updateCustomerGivenName(String givenName){
			this.updateRequest.put(customerSchema.GIVENNAME, surroundWithQuotes(givenName));
			customer = new Customer.Builder(customer).withGivenName(givenName).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerSurName(String surName){
			this.updateRequest.put(customerSchema.SURNAME, surroundWithQuotes(surName));
			customer = new Customer.Builder(customer).withSurName(surName).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerUserName(String userName){
			this.updateRequest.put(customerSchema.USERNAME, surroundWithQuotes(userName));
			customer = new Customer.Builder(customer).withUserName(userName).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerPassword(String password){
			this.updateRequest.put(customerSchema.PASSWORD, surroundWithQuotes(password));
			customer = new Customer.Builder(customer).withPassword(password).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerEmail(String email){
			this.updateRequest.put(customerSchema.EMAIL, surroundWithQuotes(email));
			customer = new Customer.Builder(customer).withEmail(email).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerStreetNumber(String streetNumber){
			this.updateRequest.put(customerSchema.STREET_NUMBER, surroundWithQuotes(streetNumber));
			customer = new Customer.Builder(customer).withStreetNumber(streetNumber).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerStreet(String street){
			this.updateRequest.put(customerSchema.STREET, surroundWithQuotes(street));
			customer = new Customer.Builder(customer).withStreet(street).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerPostalCode(String postalCode){
			this.updateRequest.put(customerSchema.POSTAL_CODE, surroundWithQuotes(postalCode));
			customer = new Customer.Builder(customer).withPostalCode(postalCode).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerCity(String city){
			this.updateRequest.put(customerSchema.CITY, surroundWithQuotes(city));
			customer = new Customer.Builder(customer).withCity(city).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerProvince(String province){
			this.updateRequest.put(customerSchema.PROVINCE, surroundWithQuotes(province));
			customer = new Customer.Builder(customer).withProvince(province).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerCountry(String country){
			this.updateRequest.put(customerSchema.COUNTRY, surroundWithQuotes(country));
			customer = new Customer.Builder(customer).withCountry(country).build();
			return this;
			
		}
		
		
		
		public CustomerUpdater updateCustomerCreditCardType(String creditCardType){
			this.updateRequest.put(customerSchema.CREDIT_CARD, surroundWithQuotes(creditCardType));
			customer = new Customer.Builder(customer).withCreditCardType(creditCardType).build();
			return this;
			
		}
		public CustomerUpdater updateCustomerCreditCardNumber(String creditCardNumber){
			this.updateRequest.put(customerSchema.CREDIT_CARD_NUMBER, surroundWithQuotes(creditCardNumber));
			customer = new Customer.Builder(customer).withCreditCardNumber(creditCardNumber).build();
			return this;
			
		}		
		
		public CustomerUpdater updateCustomerCreditCardExpiry(String creditCardExpiry){
			this.updateRequest.put(customerSchema.CREDIT_CARD_EXPIRY, surroundWithQuotes(creditCardExpiry));
			customer = new Customer.Builder(customer).withCreditCardExpiry(creditCardExpiry).build();
			return this;
			
		}		
		
		public CustomerUpdater updateCustomerCreditCardCvv2(String cvv2){
			this.updateRequest.put(customerSchema.CREDIT_CARD_CVV2, surroundWithQuotes(cvv2));
			customer = new Customer.Builder(customer).withCreditCardCVV2(cvv2).build();
			return this;
			
		}
		
		
		public Customer executeUpdate() {
			String update = "UPDATE CUSTOMER SET ";
			String check="SELECT COUNT(*) AS CUST_COUNT FROM CUSTOMER WHERE ";
			String and=" AND ";
			for(Entry<String,String> entry:this.updateRequest.entrySet()) {
				update+=entry.getKey()+"="+entry.getValue() + ",";
				if(!entry.getValue().equals("NULL")) {
					check+=entry.getKey()+"="+entry.getValue() + and;	
				}
				
			}
			check=check+" ID='"+customer.getId().toString()+"'";
			update=update.substring(0,update.length()-1);
			update+=" WHERE ID='"+customer.getId().toString()+"'";
			System.out.println(check);
			sendUpdateToDatabase(update);	
			return customer;
		}
	}	
}
