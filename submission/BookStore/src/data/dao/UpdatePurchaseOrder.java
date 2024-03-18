package data.dao;

import java.sql.SQLException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import data.beans.Address;
import data.beans.Book;
import data.beans.CreditCard;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.SiteUser;
import data.dao.exceptions.UpdateDBFailureException;
import data.fetcher.CustomerDataFetcher;
import data.schema.CustomerSchema;
import data.schema.PurchaseOrderSchema;

public class UpdatePurchaseOrder extends DataUpdate {
	
	UpdatePurchaseOrder(){
		
	}
	
	public void executeUpdatePurchaseOrderStatusToShipped(Customer customer, PurchaseOrder purchaseOrder) {
		executeUpdatePurchaseOrderStatusTo(customer,purchaseOrder,PurchaseOrderSchema.SHIPPED_STATUS);
	}
	public void executeUpdatePurchaseOrderStatusToProcessed(Customer customer, PurchaseOrder purchaseOrder) {
		executeUpdatePurchaseOrderStatusTo(customer,purchaseOrder,PurchaseOrderSchema.PROCESSED_STATUS);
	}
	public void executeUpdatePurchaseOrderStatusToDenied(Customer customer, PurchaseOrder purchaseOrder) {
		executeUpdatePurchaseOrderStatusTo(customer,purchaseOrder,PurchaseOrderSchema.DENIED_STATUS);
	}
	public void executeUpdatePurchaseOrderStatusToDelivered(Customer customer, PurchaseOrder purchaseOrder) {
		executeUpdatePurchaseOrderStatusTo(customer,purchaseOrder,PurchaseOrderSchema.DELIVERED_STATUS);
	}
	public void executeUpdatePurchaseOrderStatusToOrdered(Customer customer, PurchaseOrder purchaseOrder) {
		executeUpdatePurchaseOrderStatusTo(customer,purchaseOrder,PurchaseOrderSchema.ORDERED_STATUS);
	}
	
	private void executeUpdatePurchaseOrderStatusTo(Customer customer, PurchaseOrder purchaseOrder,String newStatus) {
		if(customer==null || purchaseOrder==null || newStatus==null || purchaseOrder.isEmpty()|| 
				!newStatus.equals(PurchaseOrderSchema.ORDERED_STATUS)|| !newStatus.equals(PurchaseOrderSchema.PROCESSED_STATUS)|| 
				!newStatus.equals(PurchaseOrderSchema.SHIPPED_STATUS)|| !newStatus.equals(PurchaseOrderSchema.DELIVERED_STATUS) ||
				!newStatus.equals(PurchaseOrderSchema.DENIED_STATUS)) return;
		String update = "UPDATE PURCHASE_ORDER SET ";
		update+=" STATUS='"+newStatus+"' ";
		update+="WHERE ID='"+customer.getId().toString()+"' AND CREATED_AT_EPOCH="+Long.toString(purchaseOrder.getCreatedAtEpoch());
		sendUpdateToDatabase(update);
	}
	
	public PurchaseOrder insertPurchaseOrder(Customer customer, String email, CreditCard creditCard,Address address) {

		if(customer.getCart()==null ||customer.getId().isEmpty() ||customer.getCart().isEmpty()||creditCard==null ||creditCard.isEmpty() || address==null || address.isEmpty()) return new PurchaseOrder.Builder().withId(customer.getId()).build();
		String epoch =Long.toString(Instant.now().getEpochSecond());
		Map<Book,Integer> cartBooks = customer.getCart().getBooks();

		String update ="INSERT INTO PURCHASE_ORDER (ID,BOOK,ISBN,EMAIL,STREET_NUMBER,STREET,POSTAL_CODE,CITY,PROVINCE,COUNTRY,STATUS,AMOUNT,CREATED_AT_EPOCH,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2)	VALUES ";
		for(Entry<Book,Integer> entry:customer.getCart().getBooks().entrySet()) {
			if(!entry.getKey().hasId()) continue;
			update+="('"+customer.getId().toString()+"','"+entry.getKey().getId().toString()+"','"+entry.getKey().getISBN()+"','"+
					email+"','"+
					address.getNumber()+"','"+
					address.getStreet()+"','"+
					address.getPostalCode()+"','"+
					address.getCity()+"','"+
					address.getProvince()+"','"+
					address.getCountry()+"','"+			
					PurchaseOrderSchema.ORDERED_STATUS+"',"+Integer.toString(entry.getValue())+","+epoch+",'"
			+creditCard.getCreditCardType()+"','"+creditCard.getCreditCardNumber()+"','"+creditCard.getCreditCardExpiry()+"','"+creditCard.getCreditCardCVV2()+"'),";
		}
		update=update.substring(0,update.length()-1);
		System.out.println("PO UPDATE: "+update);
		sendUpdateToDatabase(update);
		int entryCount=customer.getCart().getBooks().keySet().size();
		System.out.println(update);
		String check = "SELECT COUNT(*) AS PO_COUNT FROM PURCHASE_ORDER WHERE ID='"+customer.getId().toString()+"'";

		new UpdateCart().executeClearCart(customer);
		customer.getCart().clearCart();
		return new PurchaseOrder.Builder().withId(customer.getId()).withBooks(cartBooks).withCreatedAtEpoch(Long.parseLong(epoch)).build();
	}
	
	public PurchaseOrder insertPurchaseOrder(Customer customer)  {
		return insertPurchaseOrder(customer,customer.getEmail(),customer.getCreditCard(),customer.getAddress());				
	}

	
}
