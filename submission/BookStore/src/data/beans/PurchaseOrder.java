package data.beans;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import data.beans.Customer.Builder;
import data.beans.IdObject.IdObjectBuilder;

public class PurchaseOrder extends IdObject {
	private String status;
	private Map<Book,Integer> books;	
	private long createdAtEpoch;
	private Address address;
	private CreditCard creditCard;
	private String email;
	private Customer customer;
	private boolean _isWithinCustomer;
	

	public boolean isWithinCustomer() {
		return this._isWithinCustomer;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void addBookAmount(Book book,int amount){
	this.books.put(book, amount);
	}
	
	public boolean isBookInPurchaseOrder(Book book) {
		boolean result=false;
		for(Entry<Book,Integer> entry :this.books.entrySet()) {
			if(book.getId().equals(entry.getKey().getId())) result=true;
		}
		return result;		
	}
	

	public Map<Book, Integer> getBooks() {
		return books;
	}
	
	public long getCreatedAtEpoch() {
		return this.createdAtEpoch;
	}
	
	@Override
	public boolean equals(Object object) {
		PurchaseOrder other = (PurchaseOrder)object;
		return other.getId().isEqual(this.id) && other.createdAtEpoch==this.createdAtEpoch;		
	}
	
	public boolean isPurchaseOrderByCustomer(Customer customer) {
		return customer.getId().equals(this.id);
		
	}
	
	public boolean isEmpty() {
		return books.isEmpty() || createdAtEpoch==0;
	}
	

	public int numberOfBook(Book book) {
		return isBookInPurchaseOrder(book)?this.books.get(book):0;		
	}
	
	public void combineBooks(Map<Book,Integer> books) {
		for(Entry<Book,Integer> entry:books.entrySet()) {
			if(!this.books.containsKey(entry.getKey())) {
				this.books.put(entry.getKey(), entry.getValue());
			}else {
				int currentCount = this.books.get(entry.getKey());
				this.books.remove(entry.getKey());
				this.books.put(entry.getKey(), currentCount+entry.getValue());
			}
		}
	}
	
	
	
	public static class Builder extends IdObjectBuilder<Builder>{
		private String status;
		private Map<Book,Integer> books;	
		private long createdAtEpoch;
		private String email;
		private String creditCardType;
		private String creditCardNumber;
		private String creditCardExpiry;
		private String creditCardCVV2;
		private String streetNumber;
		private String street;
		private String postalCode;
		private String province;
		private String country;
		private String city;
		private Customer customer;
		private boolean _isWithinCustomer;
		
		public Builder withInCustomer() {
			this._isWithinCustomer=true;
			return this;
		}
		

		public Builder(PurchaseOrder purchaseOrder){
			this.status=purchaseOrder.status;
			this.books=purchaseOrder.books;
			this.createdAtEpoch=purchaseOrder.createdAtEpoch;
			this.id=purchaseOrder.getId();
			this.creditCardCVV2=purchaseOrder.creditCard.getCreditCardCVV2();
			this.creditCardNumber=purchaseOrder.creditCard.getCreditCardNumber();
			this.creditCardExpiry=purchaseOrder.creditCard.getCreditCardExpiry();
			this.creditCardType=purchaseOrder.creditCard.getCreditCardType();
			this.streetNumber=purchaseOrder.address.getNumber();
			this.street=purchaseOrder.address.getStreet();
			this.postalCode=purchaseOrder.address.getPostalCode();
			this.province=purchaseOrder.address.getProvince();
			this.country=purchaseOrder.address.getCountry();
			this.city=purchaseOrder.address.getCity();
			this.email=purchaseOrder.email;
			this.customer=purchaseOrder.customer;
			this._isWithinCustomer=purchaseOrder._isWithinCustomer;

		}

		public Builder(){
			this.status="";
			this.books=new LinkedHashMap<Book, Integer>();
			this.createdAtEpoch=0;
			this.id=new Id("");
			this.creditCardCVV2="";
			this.creditCardNumber="";
			this.creditCardExpiry="";
			this.creditCardType="";
			this.streetNumber="";
			this.street="";
			this.postalCode="";
			this.province="";
			this.country="";
			this.city="";
			this.email="";
			this.customer=new Customer.Builder().build();
		}
		
		public Builder withCreditCard(CreditCard creditCard){
			this.creditCardCVV2=creditCard.getCreditCardCVV2();
			this.creditCardNumber=creditCard.getCreditCardNumber();
			this.creditCardExpiry=creditCard.getCreditCardExpiry();
			this.creditCardType=creditCard.getCreditCardType();
			return this;
		}

		public Builder withAddress(Address address){
			this.streetNumber=address.getNumber();
			this.street=address.getStreet();
			this.postalCode=address.getPostalCode();
			this.province=address.getProvince();
			this.country=address.getCountry();
			this.city=address.getCity();
			return this;
		}		

		public Builder withStatus(String status){
			this.status=status;
			return this;
		}
		
		public Builder withCreatedAtEpoch(long createdAtEpoch){
			this.createdAtEpoch=createdAtEpoch;
			return this;
		}

		public Builder withBooks(Map<Book,Integer> books){
			this.books=books;
			return this;
		}
		
		public Builder withBookAndAmount(Book book,int amount){
			this.books.put(book, amount);
			return this;
		}
		public Builder withCreditCardType(String creditCardType) {
			this.creditCardType=creditCardType;
			return this;
		}
		public Builder withCreditCardNumber(String creditCardNumber ) {
			this.creditCardNumber=creditCardNumber;
			return this;
		}
		public Builder withCreditCardExpiry(String creditCardExpiry) {
			this.creditCardExpiry=creditCardExpiry;
			return this;
		}

		public Builder withCreditCardCVV2(String creditCardCVV2) {
			this.creditCardCVV2=creditCardCVV2;
			return this;
		}
		
		public Builder withStreetNumber(String streetNumber){
			this.streetNumber=streetNumber;
			return this;
		}
		
		public Builder withStreet(String street){
			this.street=street;
			return this;
		}
		public Builder withPostalCode(String postalCode){
			this.postalCode=postalCode;
			return this;
		}
		public Builder withProvince(String province){
			this.province=province;
			return this;
		}
		
		public Builder withCountry(String country){
			this.country=country;
			return this;
		}
		
		public Builder withCity(String city){
			this.city=city;
			return this;
		}
		
		public Builder withEmail(String email){
			this.email=email;
			return this;
		}
		public Builder withCustomer(Customer customer){
			this.customer=customer;
			this.id=customer.id;
			return this;
		}

		public PurchaseOrder build(){
			PurchaseOrder purchaseOrder=new PurchaseOrder();			
			purchaseOrder.status=this.status;
			purchaseOrder.books=this.books;
			purchaseOrder.customer=this.customer;
			purchaseOrder.createdAtEpoch=this.createdAtEpoch;
			purchaseOrder.email=this.email;
			purchaseOrder.address=new Address.Builder().withCountry(country).withNumber(streetNumber).withCity(city).withPostalCode(postalCode).withProvince(province).withStreet(street).build();
			purchaseOrder.creditCard= new CreditCard.Builder().withCreditCardType(creditCardType).withCreditCardNumber(creditCardNumber).withCreditCardExpiry(creditCardExpiry).withCreditCardCVV2(creditCardCVV2).build();
			purchaseOrder.id=this.id;
			purchaseOrder._isWithinCustomer=this._isWithinCustomer;
			return purchaseOrder;
		}

	}

	@Override
	public String toJson() {
		String customerJson=isWithinCustomer()?Bean.jsonMapNumber("customer","{}"):Bean.jsonMapNumber("customer",customer.toJson());		
		String booksJson="\"books\": [";
		if (this.books!=null && !this.books.isEmpty()) {
			for(Entry<Book,Integer> entry:this.books.entrySet()) {
					booksJson+="{\"amount\":"+Integer.toString(entry.getValue())+",";
					booksJson+="\"book\":";
					booksJson+=entry.getKey().toJson()+"},";
			}
			booksJson=booksJson.substring(0, booksJson.length() - 1);			
		}
		booksJson+="]";
		double total=0;
		for(Entry<Book,Integer> entry:getBooks().entrySet()) {
			total=entry.getKey().getPrice()*entry.getValue()+total;
		}
		return "{"+Bean.jsonMapVarChar("id",this.id.toString())+","+
				Bean.jsonMapVarChar("email",this.email)+","+
				Bean.jsonMapNumber("total", String.format("%.2f", total))+","+
				Bean.jsonMapNumber("createdAtEpoch",Long.toString(this.createdAtEpoch))+","+
				Bean.jsonMapVarChar("status",this.status)+","+
				Bean.jsonMapNumber("address",this.address.toJson())+","+
				Bean.jsonMapNumber("creditCard",this.creditCard.toJson())+","+
				booksJson+","+
				customerJson+
				"}";				
	}
}
