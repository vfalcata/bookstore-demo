package data.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.validation.Schema;
import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.dao.BookDAO.BookAttributeAccess;
import data.dao.BookDAO.BookKeyQuery;
import data.dao.BookDAO.BookNumberQuery;
import data.dao.BookDAO.BookStoreBookQuery;
import data.dao.CartDAO.BookStoreCartQuery;
import data.dao.PurchaseOrderDAO.BookStorePurchaseOrderQuery;
import data.dao.ReviewDAO.BookStoreReviewQuery;
import data.fetcher.CustomerDataFetcher;
import data.query.AttributeAccess;
import data.query.BookStoreNumberQuery;
import data.query.BookStoreQuery;
import data.query.BookStoreVarCharQuery;
import data.query.DataAccessString;
import data.query.PageRequestMetaData;
import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.CustomerSchema;
import data.schema.DataSchema;
import data.schema.PurchaseOrderSchema;
import data.schema.ReviewSchema;
import data.schema.UserTypes;
public class CustomerDAO implements DAO{
	private CustomerSchema customerSchema;
	public CustomerDAO(){
		this.customerSchema=new CustomerSchema();
	}
	@Override
	public UpdateCustomer newUpdateRequest() {
		return new UpdateCustomer();
	}
	
	public Customer loginCustomer(String userName, String passWord) {
		Customer customer = new Customer.Builder().withUserName(userName).build();
		
		String queryString="SELECT *  FROM CUSTOMER WHERE USERNAME='"+userName+"' AND PASSWORD='"+passWord+"'" ;
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			DataSource dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			resultSet= preparedStatement.executeQuery();
			if(!resultSet.next()) return new Customer.Builder().withUserName(userName).build();
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Customer.Builder().withUserName(userName).build();	
		}finally {

			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			if(preparedStatement!=null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection!= null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}


		}

		List<Customer> customers=
		newQueryRequest()
		.includeAllAttributesInResultFromSchema()
		.queryAttribute()
		.whereCustomerUserName()
		.varCharEquals(userName)
		.queryAttribute()
		.whereCustomerPassword()
		.varCharEquals(passWord)
		.executeQuery()
		.executeCompilation()
		.compileCustomers();
		customer=new Customer.Builder().withUserName(userName).build();	
		if(customers.size()>0) {
			for(Customer customerInQuery:customers) {

				customer=customerInQuery;
			}
		}else {
			return new Customer.Builder().withUserName(userName).build();	
		}
		int reviewCount = new ReviewDAO().getReviewCount(customer)+1;
		int poCount = new PurchaseOrderDAO().getPurchaseOrderRowCount(customer)+1;
		int limit=reviewCount*poCount*60;
		List<Customer> customersCart= newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryCart()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereCartCustomer()
				.isCustomer(customer)
				.queryBook()
				.includeAllAttributesInResultFromSchema()
				.withResultLimit(50)
				.executeQuery()
				.executeCompilation()
				.compileCustomers();
		if(customersCart.size()>0) {
		
			for(Customer customerInCartQuery:customersCart) {
				 
				 customer=new Customer.Builder(customer).withCart(customerInCartQuery.getCart()).build();
			}			
		}
		List<Customer> customersPurchaseOrder= newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryPurchaseOrder()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.wherePurchaseOrderCustomer()
				.isCustomer(customer)
				.queryBook()
				.includeAllAttributesInResultFromSchema()
				.withResultLimit(limit)
				.executeQuery()
				.executeCompilation()
				.compileCustomers();
		if(customersPurchaseOrder.size()>0) {
			for(Customer customerInPOQuery:customersPurchaseOrder) {
				customer=new Customer.Builder(customer).withPurchaseOrders(customerInPOQuery.getPurchaseOrders()).build();
			}	
		}

		List<Customer> customersReviews= newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryReview()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereReviewCustomer()
				.isCustomer(customer)
				.queryBook()
				.includeAllAttributesInResultFromSchema()
				.withResultLimit(limit)
				.executeQuery()
				.executeCompilation()
				.compileCustomers();
		if(customersReviews.size()>0) {
			for(Customer customerInReviewQuery:customersReviews) {
				customer=new Customer.Builder(customer).withReviews(customerInReviewQuery.getReviews()).build();			
			}			
		}
		
		return new Customer.Builder(customer).withLoggedOn().build();
	}
	
	@Override
	public BookStoreCustomerQuery newQueryRequest(){
		BookStoreCustomerQuery bookStoreCustomerQuery= new BookStoreCustomerQuery(customerSchema);
		CustomerAttributeAccess customerAttributeAccess= new CustomerAttributeAccess();
		bookStoreCustomerQuery.setAttribute(customerAttributeAccess);
		customerAttributeAccess.setBookStoreCustomerQuery(bookStoreCustomerQuery);
		return bookStoreCustomerQuery;
	}
	public class BookStoreCustomerQuery extends BookStoreQuery<BookStoreCustomerQuery,CustomerAttributeAccess>{
		public BookStoreCustomerQuery(BookStoreCustomerQuery query, DataSchema dataSchema){
			super(query,  dataSchema);
		}
		public BookStoreCustomerQuery(DataSchema dataSchema){
			super(dataSchema);
		}
		public BookStoreCustomerQuery includeCustomerGivenNameInResult(){
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.GIVENNAME);
			includeKeyInResults();
			return this;
		}
		public BookStoreCustomerQuery includeCustomerSurNameInResult(){
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.SURNAME);
			includeKeyInResults();
			return this;
		}
		public BookStoreCustomerQuery includeCustomerUserNameInResult(){
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.USERNAME);
			return this;
		}
		public BookStoreCustomerQuery includeCustomerEmailInResult(){
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.EMAIL);
				includeKeyInResults();
				return this;
		}
		public BookStoreCustomerQuery includeCustomerPasswordInResult(){
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.PASSWORD);
				includeKeyInResults();
				return this;
		}
		
		
		public BookStoreCustomerQuery includeCustomerCreatedAtEpochInResult() {
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.CREATED_AT_EPOCH);
			includeKeyInResults();
			return this;
		}
		
		public BookStoreCustomerQuery includeCustomerCreditCardInResult() {
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.CREDIT_CARD);
			
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.CREDIT_CARD_NUMBER);
			
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.CREDIT_CARD_EXPIRY);
			
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.CREDIT_CARD_CVV2);
			includeKeyInResults();
			return this;
		}
		public BookStoreCustomerQuery excludeCustomerCreditCardInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.CREDIT_CARD);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.CREDIT_CARD_NUMBER);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.CREDIT_CARD_EXPIRY);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.CREDIT_CARD_CVV2);
			return this;
		}

		public BookStoreCustomerQuery includeCustomerAddressInResult() {
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.STREET_NUMBER);
			
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.STREET);
			
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.POSTAL_CODE);
			
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.CITY);
			
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.PROVINCE);
			
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.put(customerSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.COUNTRY);
			includeKeyInResults();
			return this;
		}
		
		public BookStoreCustomerQuery excludeCustomerAddressInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.STREET_NUMBER);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.STREET);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.POSTAL_CODE);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.CITY);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.PROVINCE);
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.COUNTRY);
			return this;
		}
		
		
		
		public BookStoreCustomerQuery excludeCustomerCreatedAtEpochInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.CREATED_AT_EPOCH);
			return this;
		}
		
		public BookStoreCustomerQuery excludeCustomerGivenNameInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.GIVENNAME);
			return this;
		}
		public BookStoreCustomerQuery excludeCustomerSurNameInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.SURNAME);
			return this;
		}
		public BookStoreCustomerQuery excludeCustomerUserNameInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.USERNAME);
			return this;
		}
		public BookStoreCustomerQuery excludeCustomerEmailInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.EMAIL);
			return this;
		}
		public BookStoreCustomerQuery excludeCustomerPasswordInResult(){
			if(this.attributesToIncludInResults.containsKey(customerSchema.tableName())) this.attributesToIncludInResults.get(customerSchema.tableName()).remove(customerSchema.PASSWORD);
			return this;
		}
		
		private void includeKeyInResults() {
			if(!this.attributesToIncludInResults.containsKey(customerSchema.tableName()) && !this.attributesToIncludInResults.get(customerSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(customerSchema.tableName()).contains(customerSchema.ID))
			this.attributesToIncludInResults.get(customerSchema.tableName()).add(customerSchema.ID);


		}
		
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.SITE_USER)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
	}
		
		public BookStoreCartQuery queryCart() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);
			
			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStorePurchaseOrderQuery queryPurchaseOrder() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new PurchaseOrderSchema().tableName()+this.referenceOperator+PurchaseOrderSchema.ID)
					.build()
					);
			return new PurchaseOrderDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	public class CustomerVarCharQuery extends BookStoreVarCharQuery<CustomerVarCharQuery,CustomerAttributeAccess,BookStoreCustomerQuery>{
		private CustomerAttributeAccess customerAttributeAccess;
		CustomerVarCharQuery(BookStoreCustomerQuery bookStoreCustomerQuery, String currentAttributeAccess){
			super(bookStoreCustomerQuery,new CustomerSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		CustomerVarCharQuery(BookStoreCustomerQuery bookStoreCustomerQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreCustomerQuery,new CustomerSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public CustomerAttributeAccess queryCustomerAttribute(){
			return customerAttributeAccess;
		}
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.SITE_USER)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
	}
		
		public BookStoreCartQuery queryCart() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);
			
			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStorePurchaseOrderQuery queryPurchaseOrder() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new PurchaseOrderSchema().tableName()+this.referenceOperator+PurchaseOrderSchema.ID)
					.build()
					);
			return new PurchaseOrderDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}

	}
	public class CustomerNumberQuery extends BookStoreNumberQuery<CustomerNumberQuery,CustomerAttributeAccess,BookStoreCustomerQuery>{
		private CustomerAttributeAccess customerAttributeAccess;
		CustomerNumberQuery(BookStoreCustomerQuery bookStoreCustomerQuery, String currentAttributeAccess){
			super(bookStoreCustomerQuery,new CustomerSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		CustomerNumberQuery(BookStoreCustomerQuery bookStoreCustomerQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreCustomerQuery,new CustomerSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public CustomerAttributeAccess queryCustomerAttribute(){
			return customerAttributeAccess;
		}		
		
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.SITE_USER)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
	}
		
		public BookStoreCartQuery queryCart() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);
			
			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStorePurchaseOrderQuery queryPurchaseOrder() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new PurchaseOrderSchema().tableName()+this.referenceOperator+PurchaseOrderSchema.ID)
					.build()
					);
			return new PurchaseOrderDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	
	public class CustomerKeyQuery extends BookStoreQuery<CustomerKeyQuery,CustomerAttributeAccess>{
		private CustomerAttributeAccess customerAttributeAccess;
		CustomerKeyQuery(BookStoreCustomerQuery bookStoreCustomerQuery, String currentAttributeAccess){
			super(bookStoreCustomerQuery,new CustomerSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		CustomerKeyQuery(BookStoreCustomerQuery bookStoreCustomerQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreCustomerQuery,new CustomerSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public CustomerAttributeAccess queryCustomerAttribute(){
			return customerAttributeAccess;
		}		
		
		public CustomerKeyQuery isCustomer(Customer customer) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(customer.getId().toString())
					.build()
					);
			return  this;
		}
	

		
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.SITE_USER)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
	}
		
		public BookStoreCartQuery queryCart() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);
			
			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStorePurchaseOrderQuery queryPurchaseOrder() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CustomerSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new PurchaseOrderSchema().tableName()+this.referenceOperator+PurchaseOrderSchema.ID)
					.build()
					);
			return new PurchaseOrderDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	public class CustomerAttributeAccess extends AttributeAccess<BookStoreCustomerQuery>{
		protected BookStoreCustomerQuery bookStoreCustomerQuery;
		protected CustomerAttributeAccess() {
		}
		protected void setBookStoreCustomerQuery(BookStoreCustomerQuery bookStoreCustomerQuery){
			this.bookStoreCustomerQuery=bookStoreCustomerQuery;
		}
		public CustomerKeyQuery whereCustomer(){
			CustomerKeyQuery customerKeyQuery= new CustomerKeyQuery(this.bookStoreCustomerQuery,CustomerSchema.ID);
			customerKeyQuery.setAttribute(this);
			return customerKeyQuery;
		}
		
		public CustomerVarCharQuery whereCustomerGivenName(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.GIVENNAME);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		public CustomerVarCharQuery whereCustomerSurName(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.SURNAME);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		public CustomerVarCharQuery whereCustomerUserName(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.USERNAME);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}

		public CustomerVarCharQuery whereCustomerPassword(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.PASSWORD);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		public CustomerVarCharQuery whereCustomerEmail(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.EMAIL);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		public CustomerVarCharQuery whereCustomerStreet(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.STREET);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		public CustomerVarCharQuery whereCustomerStreetNumber(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.STREET_NUMBER);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		
		public CustomerVarCharQuery whereCustomerPostalCode(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.POSTAL_CODE);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		
		public CustomerVarCharQuery whereCustomerCity(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.CITY);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		public CustomerVarCharQuery whereCustomerProvince(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.PROVINCE);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		public CustomerVarCharQuery whereCustomerCountry(){
			CustomerVarCharQuery customerVarCharQuery= new CustomerVarCharQuery(this.bookStoreCustomerQuery,CustomerSchema.COUNTRY);
			customerVarCharQuery.setAttribute(this);
			return customerVarCharQuery;
		}
		
		
		public CustomerNumberQuery whereCustomerCreatedAtEpoch() {
			CustomerNumberQuery customerNumberQuery= new CustomerNumberQuery(this.bookStoreCustomerQuery,CustomerSchema.CREATED_AT_EPOCH);
			customerNumberQuery.setAttribute(this);
			return customerNumberQuery;
		}
	}

}