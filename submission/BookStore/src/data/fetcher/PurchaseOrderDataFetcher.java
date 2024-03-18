package data.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import data.beans.Address;
import data.beans.Book;
import data.beans.Cart;
import data.beans.CreditCard;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Visitor;
import data.query.Query;
import data.schema.CartSchema;
import data.schema.PurchaseOrderSchema;

public class PurchaseOrderDataFetcher  extends DataFetcher<PurchaseOrder>{




	public PurchaseOrderDataFetcher(Map<String, Set<String>> attributesToIncludInResults) {
		super(attributesToIncludInResults);
	}



	private PurchaseOrderSchema schema=new PurchaseOrderSchema(); 
	
	@Override
	public PurchaseOrder resultSetToBean(ResultSet resultSet) {
		String prefix = isReferenceQuery()?schema.tableName()+Query.referenceSeparator:"";
		PurchaseOrder purchaseOrder = new PurchaseOrder.Builder().build();
		boolean isRequestAllAttributes=this.attributesToIncludInResults.get(schema.tableName()).isEmpty();
		
				
		try {
			
			
			Book book = new Book.Builder().withId(new Id(resultSet.getString(prefix+schema.BOOK))).withISBN(resultSet.getString(prefix+schema.ISBN)).build();
			
			purchaseOrder= new PurchaseOrder.Builder(purchaseOrder)
					.withId(new Id(resultSet.getString(prefix+schema.ID)))
					.withCreatedAtEpoch(resultSet.getLong(prefix+schema.CREATED_AT_EPOCH))
					.withBookAndAmount(book,resultSet.getInt(prefix+schema.AMOUNT))
					.build();
			Address address = new Address.Builder().build();
			CreditCard creditCard = new CreditCard.Builder().build();
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.EMAIL)) {
				purchaseOrder = new PurchaseOrder.Builder(purchaseOrder).withEmail(resultSet.getString(prefix+schema.EMAIL)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.STATUS)) {
				purchaseOrder = new PurchaseOrder.Builder(purchaseOrder).withStatus(resultSet.getString(prefix+schema.STATUS)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.STREET_NUMBER)) {
				address = new Address.Builder(address).withNumber(resultSet.getString(prefix+schema.STREET_NUMBER)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.STREET)) {
				address = new Address.Builder(address).withStreet(resultSet.getString(prefix+schema.STREET)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.PROVINCE)) {
				address = new Address.Builder(address).withProvince(resultSet.getString(prefix+schema.PROVINCE)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CITY)) {
				address = new Address.Builder(address).withCity(resultSet.getString(prefix+schema.CITY)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.POSTAL_CODE)) {
				address = new Address.Builder(address).withPostalCode(resultSet.getString(prefix+schema.POSTAL_CODE)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.COUNTRY)) {
				address = new Address.Builder(address).withCountry(resultSet.getString(prefix+schema.COUNTRY)).build();
			}
			
			//
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD)) {
				creditCard = new CreditCard.Builder(creditCard).withCreditCardType(resultSet.getString(prefix+schema.CREDIT_CARD)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD_NUMBER)) {
				creditCard = new CreditCard.Builder(creditCard).withCreditCardNumber(resultSet.getString(prefix+schema.CREDIT_CARD_NUMBER)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD_EXPIRY)) {
				creditCard = new CreditCard.Builder(creditCard).withCreditCardExpiry(resultSet.getString(prefix+schema.CREDIT_CARD_EXPIRY)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREDIT_CARD_CVV2)) {
				creditCard = new CreditCard.Builder(creditCard).withCreditCardCVV2(resultSet.getString(prefix+schema.CREDIT_CARD_CVV2)).build();
			}
			

			return new PurchaseOrder.Builder(purchaseOrder).withAddress(address).withCreditCard(creditCard).build();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.err.println("Warning empty purchase order, since resultSet could not produce purchase order object");
		return new PurchaseOrder.Builder().build();
		
	}

}