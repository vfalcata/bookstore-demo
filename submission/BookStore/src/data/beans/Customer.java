package data.beans;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.beans.Book.Builder;
import data.beans.IdObject.IdObjectBuilder;
import data.schema.UserTypes;

public class Customer extends SiteUser{	
	
	private String givenName;
	private  String surName;
	private  Address address;
	private  String userName;
	private  String email;
	private  String password;
	private  Review[] reviews;
	private  long createdAtEpoch;
	private CreditCard creditCard;
	private boolean _isLoggedOn;
	private boolean _isWithinReview;
	
	public static final String userType=UserTypes.CUSTOMER;
	
	
	
	public String getUserType() {
		return this.userType;
	}
	
	public boolean isWithinReview() {
		return this._isWithinReview;
	}
	
	private Customer() {
	}

	public String getGivenName() {
		return givenName;
	}

	public String getSurName() {
		return surName;
	}
	
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public Address getAddress() {
		return address;
	}

	public String getUserName() {
		return userName;
	}
	
	public String getEmail() {
		return email;
	}
	public boolean isLoggedOn() {
		return _isLoggedOn;
	}
	
	public String getPassword() {
		return this.password;
	}


	public List<Review> getReviews() {
		return Arrays.asList(this.reviews);
	}
	
	public void addReview(Review review) {
		Review[] reviews=new Review[this.reviews.length+1];
		for(int i=0;i<this.reviews.length;i++) {
			reviews[i]=this.reviews[i];
		}
		reviews[this.reviews.length]=review;
		this.reviews=reviews;
	}
	
	public void addReviews(Review[] reviews) {
		Review[] appendReviews=new Review[this.reviews.length+reviews.length];
		for(int i=0;i<this.reviews.length;i++) {
			appendReviews[i]=this.reviews[i];
		}
		for(int i=0;i<reviews.length;i++) {
			appendReviews[i+this.reviews.length]=reviews[i];
		}

		this.reviews=appendReviews;
	}
	

	public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
		PurchaseOrder[] purchaseOrders=new PurchaseOrder[this.purchaseOrders.length+1];
		for(int i=0;i<this.purchaseOrders.length;i++) {
			purchaseOrders[i]=this.purchaseOrders[i];
		}
		purchaseOrders[this.purchaseOrders.length]=purchaseOrder;
		this.purchaseOrders=purchaseOrders;
	}

	
	public long getCreatedAtEpoch() {
		return this.createdAtEpoch;
	}

	


	public boolean isReviewByCustomer(Review review) {
		return true;
	}
	

	
	public boolean isCartByCustomer(Cart cart) {
		return true;
	}

	public static class Builder extends IdObjectBuilder<Builder>{
		
		private String email;
		private String givenName;
		private String surName;
		private String userName;
		private String password;
		private Review[] reviews;
		private Cart cart;
		private PurchaseOrder[] purchaseOrders;
		private boolean isLoggedOn;
		private String streetNumber;
		private String street;
		private String postalCode;
		private String province;
		private String country;
		private String city;
		private boolean _isLoggedOn;
		private String creditCardType;
		private String creditCardNumber;
		private String creditCardExpiry;
		private String creditCardCVV2;

		private  long createdAtEpoch;
		private boolean _isWithinReview;
		
		public Builder withinReview() {
			this._isWithinReview=true;
			return this;
		}
		

		public Builder(){
			super();
			this.id=id;
			this.givenName="";
			this.surName="";
			this.userName="";
			this.password="";
			this.email="";
			this.reviews=new Review[]{};
			this.cart=new Cart.Builder().withId(this.id).build();
			this.purchaseOrders=new PurchaseOrder[] {};
			this.createdAtEpoch=0;
			this._isWithinReview=false;
			this.streetNumber="";
			this.street="";
			this.city="";
			this.province="";
			this.country="";
			this.postalCode="";
			this._isLoggedOn=false;
			this.creditCardCVV2="";
			this.creditCardNumber="";
			this.creditCardExpiry="";
			this.creditCardType="";
		}
		
		
		public Builder(Customer customer){
			super(customer);

			this.id=customer.id;
			this.givenName=customer.givenName;
			this.surName=customer.surName;
			this.userName=customer.userName;
			this.password=customer.password;
			this.reviews=customer.reviews;
			this.cart=customer.cart;
			this.email=customer.email;
			this.purchaseOrders=customer.purchaseOrders;
			this.createdAtEpoch=customer.createdAtEpoch;
			this._isWithinReview=customer._isWithinReview;
			this.streetNumber=customer.getAddress().getNumber();
			this.street=customer.getAddress().getStreet();
			this.city=customer.getAddress().getCity();
			this.province=customer.getAddress().getProvince();
			this.country=customer.getAddress().getCountry();
			this.postalCode=customer.getAddress().getPostalCode();
			this._isLoggedOn=customer._isLoggedOn;
			this.creditCardCVV2=customer.getCreditCard().getCreditCardCVV2();
			this.creditCardNumber=customer.getCreditCard().getCreditCardNumber();
			this.creditCardExpiry=customer.getCreditCard().getCreditCardExpiry();
			this.creditCardType=customer.getCreditCard().getCreditCardType();
			
		}
		public Builder withCreatedAtEpoch(long createdAtEpoch){
			this.createdAtEpoch=createdAtEpoch;
			return this;
		}
		public Builder withCreatedAtEpoch(String createdAtEpoch) {
			this.createdAtEpoch=Long.parseLong(createdAtEpoch);
			return this;
		}


		public Builder withGivenName(String givenName){
			this.givenName=givenName;
			return this;
		}

		public Builder withSurName(String surName){
			this.surName=surName;
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
		
		
		public Builder withEmail(String email){
			this.email=email;
			return this;
		}
		
		public Builder withUserName(String userName){
			this.userName=userName;
			return this;
		}
		



		public Builder withPassword(String password){
			this.password=password;
			return this;
		}

		public Builder withReviews(Review[] reviews){
			this.reviews=reviews;
			return this;
		}
		
		public Builder withLoggedOn(){
			this._isLoggedOn=true;
			return this;
		}
		
		public Builder withReviews(List<Review> reviews){
			Review[] reviewsArr = new Review[reviews.size()];
			return withReviews(reviews.toArray(reviewsArr));
		}
		
		public Builder withReviews(Review review){
			this.reviews=new Review[] {review};
			return this;
		}
		
		public Builder withAdditionalReview(Review review){
			Review[] reviewAppend = new Review[this.reviews.length+1];
			for(int i=0;i<this.reviews.length;i++) {
				reviewAppend[i]=this.reviews[i];
			}
			reviewAppend[this.reviews.length+1]=review;
			this.reviews=reviewAppend;
			return this;
		}	

		public Builder withCart(Cart cart){
			this.cart=cart;
			return this;
		}

		public Builder withPurchaseOrders(PurchaseOrder[] purchaseOrders){
			this.purchaseOrders=purchaseOrders;
			return this;
		}

		public Builder withPurchaseOrders(List<PurchaseOrder> purchaseOrders){
			PurchaseOrder[] purchaseOrderArr = new PurchaseOrder[purchaseOrders.size()];
			return withPurchaseOrders(purchaseOrders.toArray(purchaseOrderArr));
		}
		public Builder withPurchaseOrders(PurchaseOrder purchaseOrders){
			this.purchaseOrders=new PurchaseOrder[] {purchaseOrders};
			return this;
		}
		public Builder withAdditionalPurchaseOrder(PurchaseOrder purchaseOrder){
			PurchaseOrder[] purchaseOrdersAppend = new PurchaseOrder[this.purchaseOrders.length+1];
			for(int i=0;i<this.purchaseOrders.length;i++) {
				purchaseOrdersAppend[i]=this.purchaseOrders[i];
			}
			purchaseOrdersAppend[this.purchaseOrders.length+1]=purchaseOrder;
			this.purchaseOrders=purchaseOrdersAppend;
			return this;
		}
		
		public Builder withLoginState(boolean isLoggedOn){
			this.isLoggedOn=isLoggedOn;
			return this;
		}
		
		public Builder withCreditCard(CreditCard creditCard){
			this.creditCardCVV2=creditCard.getCreditCardCVV2();
			this.creditCardNumber=creditCard.getCreditCardNumber();
			this.creditCardExpiry=creditCard.getCreditCardExpiry();
			this.creditCardType=creditCard.getCreditCardType();
			return this;
		}

		public Customer build(){
			Address address =new Address.Builder().withCountry(country).withNumber(streetNumber).withCity(city).withPostalCode(postalCode).withProvince(province).withStreet(street).build();
			CreditCard creditCard = new CreditCard.Builder().withCreditCardType(creditCardType).withCreditCardNumber(creditCardNumber).withCreditCardExpiry(creditCardExpiry).withCreditCardCVV2(creditCardCVV2).build();
			Customer customer=new Customer();
			customer.id=this.id==null?new Id(""):this.id;
			customer.givenName=this.givenName==null?"":this.givenName;
			customer.surName=this.surName==null?"":this.surName;
			customer.address=address;
			customer.userName=this.userName==null?"":this.userName;
			customer.password=this.password==null?"":this.password;
			customer.reviews=this.reviews==null?new Review[0]:this.reviews;
			customer.cart=this.cart==null?new Cart.Builder().withId(this.id).build():this.cart;
			customer.purchaseOrders=this.purchaseOrders==null?new PurchaseOrder[0]:this.purchaseOrders;
			customer._isLoggedOn=this._isLoggedOn;
			customer.email=this.email==null?"":this.email;
			customer.createdAtEpoch=this.createdAtEpoch;
			customer._isWithinReview=this._isWithinReview;
			customer.creditCard=creditCard;
			return customer;
		}

	}

	@Override
	public String toJson() {
		
		String purchaseOrdersJson="\"purchaseOrders\": [";
		if (this.purchaseOrders!=null && purchaseOrders.length>0) {
			for(PurchaseOrder purchaseOrder:this.purchaseOrders) {
				purchaseOrdersJson+=purchaseOrder.toJson()+",";
			}
			purchaseOrdersJson=purchaseOrdersJson.substring(0, purchaseOrdersJson.length() - 1);				
				
		}
		purchaseOrdersJson+="]";
		
		String reviewsJson="\"reviews\": [";
		if(!isWithinReview()) {			
			if (this.reviews!=null && reviews.length!=0) {
				for(Review review:this.reviews) {
					reviewsJson+=review.toJson()+",";
				}
				reviewsJson=reviewsJson.substring(0, reviewsJson.length() - 1);				
			}			
		}
		reviewsJson+="]";
		
		
		return "{"+Bean.jsonMapVarChar("id",this.id.toString())+","+
		Bean.jsonMapVarChar("givenName",this.givenName)+","+
		Bean.jsonMapVarChar("surName",this.surName)+","+
		Bean.jsonMapVarChar("userName",this.userName)+","+
		Bean.jsonMapVarChar("email",this.email)+","+
		Bean.jsonMapVarChar("password",this.password)+","+
		Bean.jsonMapNumber("createdAtEpoch",Long.toString(createdAtEpoch))+","+
		Bean.jsonMapNumber("address",this.address.toJson())+","+
		Bean.jsonMapNumber("creditCard",this.creditCard.toJson())+","+
		Bean.jsonMapNumber("cart",this.cart.toJson())+","+
		purchaseOrdersJson+","+
		reviewsJson+
		"}";
	}
}
