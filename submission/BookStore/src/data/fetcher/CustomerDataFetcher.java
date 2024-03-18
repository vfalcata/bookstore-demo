package data.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.dao.ReviewDAO;
import data.query.Query;
import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.CustomerSchema;
import data.schema.ReviewSchema;

public class CustomerDataFetcher extends DataFetcher<Customer>{
	
	public CustomerDataFetcher(Map<String, Set<String>> attributesToIncludInResults) {
		super(attributesToIncludInResults);
	}



	private CustomerSchema schema=new CustomerSchema(); 
	
	protected Entry<Id,Customer> resultSetFromIdToCustomer(ResultSet resultSet){
		Customer customer=resultSetToBean(resultSet);
		return new AbstractMap.SimpleEntry<Id,Customer>(customer.getId(), customer);
	}
	


	@Override
	public Customer resultSetToBean(ResultSet resultSet) {
		boolean isRequestAllAttributes=this.attributesToIncludInResults.get(schema.tableName()).isEmpty();
		String prefix = isReferenceQuery()?schema.tableName()+Query.referenceSeparator:"";

		Customer customer = new Customer.Builder().build();

		
				
		try {

			customer = new Customer.Builder().withId(new Id(resultSet.getString(prefix+schema.ID))).build();		
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.GIVENNAME)) {
				customer = new Customer.Builder(customer).withGivenName(resultSet.getString(prefix+schema.GIVENNAME)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.SURNAME)) {
				customer = new Customer.Builder(customer).withSurName(resultSet.getString(prefix+schema.SURNAME)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.USERNAME)) {
				customer = new Customer.Builder(customer).withUserName(resultSet.getString(prefix+schema.USERNAME)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.PASSWORD)) {
				customer = new Customer.Builder(customer).withPassword(resultSet.getString(prefix+schema.PASSWORD)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.EMAIL)) {
				customer = new Customer.Builder(customer).withEmail(resultSet.getString(prefix+schema.EMAIL)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.STREET_NUMBER)) {
				customer = new Customer.Builder(customer).withStreetNumber(resultSet.getString(prefix+schema.STREET_NUMBER)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.STREET)) {
				customer = new Customer.Builder(customer).withStreet(resultSet.getString(prefix+schema.STREET)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.PROVINCE)) {
				customer = new Customer.Builder(customer).withProvince(resultSet.getString(prefix+schema.PROVINCE)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CITY)) {
				customer = new Customer.Builder(customer).withCity(resultSet.getString(prefix+schema.CITY)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.POSTAL_CODE)) {
				customer = new Customer.Builder(customer).withPostalCode(resultSet.getString(prefix+schema.POSTAL_CODE)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.COUNTRY)) {
				customer = new Customer.Builder(customer).withCountry(resultSet.getString(prefix+schema.COUNTRY)).build();
			}
			
			//
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD)) {
				customer = new Customer.Builder(customer).withCreditCardType(resultSet.getString(prefix+schema.CREDIT_CARD)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD_NUMBER)) {
				customer = new Customer.Builder(customer).withCreditCardNumber(resultSet.getString(prefix+schema.CREDIT_CARD_NUMBER)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD_EXPIRY)) {
				customer = new Customer.Builder(customer).withCreditCardExpiry(resultSet.getString(prefix+schema.CREDIT_CARD_EXPIRY)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD_CVV2)) {
				customer = new Customer.Builder(customer).withCreditCardCVV2(resultSet.getString(prefix+schema.CREDIT_CARD_CVV2)).build();
			}
			

			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREATED_AT_EPOCH)) {
				customer = new Customer.Builder(customer).withCreatedAtEpoch(resultSet.getLong(prefix+schema.CREATED_AT_EPOCH)).build();
			}
			
			
			
			return customer;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.err.println("Warning empty customer, since resultSet could not produce customer object");
		return customer;
		
	}
	
}
