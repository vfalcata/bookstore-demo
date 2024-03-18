package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.SiteUser;
import data.beans.User;
import data.dao.BookDAO.BookStoreBookQuery;
import data.dao.CartDAO.BookStoreCartQuery;
import data.dao.CartDAO.CartAttributeAccess;
import data.dao.CartDAO.CartBookQuery;
import data.dao.CartDAO.CartCustomerQuery;
import data.dao.CartDAO.CartNumberQuery;
import data.dao.CartDAO.CartObjectQuery;
import data.dao.CartDAO.CartVarCharQuery;
import data.dao.CustomerDAO.BookStoreCustomerQuery;
import data.dao.CustomerDAO.CustomerVarCharQuery;
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
import data.schema.UserTypes;

public class PurchaseOrderDAO implements DAO{
	private PurchaseOrderSchema purchaseOrderSchema;
	public PurchaseOrderDAO(){
		this.purchaseOrderSchema=new PurchaseOrderSchema();
	}
	
	public Map<Long,Integer> getPurchaseOrderCountByCreatedAtEpoch(Id id){
		return getPurchaseOrderCountByCreatedAtEpoch(id.toString());
	}
	
	public Map<Long,Integer> getPurchaseOrderCountByCreatedAtEpoch(Customer customer){
		return getPurchaseOrderCountByCreatedAtEpoch(customer.getId().toString());
	}
	
	public Integer getPurchaseOrderRowCount(Customer customer){
		return getPurchaseOrderRowCount(customer.getId().toString());
	}
	
	public Integer getPurchaseOrderRowCount(Id id){
		return getPurchaseOrderRowCount(id.toString());
	}
	public Integer getPurchaseOrderRowCount(String id){
		int result=0;
		for(Entry<Long,Integer> entry :getPurchaseOrderCountByCreatedAtEpoch(id).entrySet()) {
			result=result+entry.getValue();
		}
		return result+10;
	}
	

	/**
	 * @return the date of the oldest order in epoch time
	 */
	public Long getOldestOrder() {
		String queryString="SELECT MIN(CREATED_AT_EPOCH) FROM PURCHASE_ORDER WHERE STATUS!='DENIED'";
		Long results = (long) -1;
		
		try {
			DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			Connection con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while(rs.next()) {
				results = rs.getLong(1);
			}
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}
	
	public Long getLatestOrder() {
		String queryString="SELECT MAX(CREATED_AT_EPOCH) FROM PURCHASE_ORDER WHERE STATUS!='DENIED'";
		Long results = (long) -1;
		
		try {
			DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			Connection con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while(rs.next()) {
				results = rs.getLong(1);
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public Map<String, Integer> getOrdersInDateRange(Long start, Long end) {	
		
		String queryString="SELECT SUM(AMOUNT) AS AMOUNT, BOOK FROM PURCHASE_ORDER WHERE STATUS != 'DENIED' AND CREATED_AT_EPOCH BETWEEN " + start + " AND " + end + " GROUP BY BOOK ORDER BY AMOUNT DESC";
		Map<String, Integer> results = new LinkedHashMap<String, Integer>();
		
		Connection con = null;
		
		try {
			DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while(rs.next()) {
				results.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		
		return results;
		
	}
	
	public Map<String, Integer> getTopBooks(Long start) {
		String queryString="SELECT SUM(AMOUNT) AS AMOUNT, BOOK FROM PURCHASE_ORDER WHERE STATUS != 'DENIED' AND CREATED_AT_EPOCH > " + start + " GROUP BY BOOK ORDER BY AMOUNT DESC";
		Map<String, Integer> results = new LinkedHashMap<String, Integer>();
		int count = 0;
		
		Connection con = null;
		
		try {
			DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while(rs.next() && count < 10) {
				results.put(rs.getString(2), rs.getInt(1));
				count++;
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return results;
	}
	
	public Map<String, Map<String, Integer>> getCustomerBookOrderCount() {
		
		Map<String, Map<String, Integer>> orders = new LinkedHashMap<String, Map<String, Integer>>();
		Map<String, Integer> books = new LinkedHashMap<String, Integer>();
		
		String queryString="SELECT ID, BOOK, SUM(AMOUNT) FROM PURCHASE_ORDER GROUP BY BOOK, ID";
		
		String cust = "";
		String book = "";
		Integer num = 0;
		
		Connection con = null;
		
		try {
			DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while(rs.next()) {
				
				cust = rs.getString(1);
				book = rs.getString(2);
				num  = rs.getInt(3);
				
				if (orders.containsKey(cust)){
					orders.get(cust).put(book, num);
				} else {
					books.put(book, num);
					orders.put(cust, new LinkedHashMap<String, Integer>(books));
					books.clear();
				}

			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		
		return orders;
		
	}
	
	public Map<Long,Integer> getPurchaseOrderCountByCreatedAtEpoch(String id){
		String queryString="SELECT COUNT(CREATED_AT_EPOCH) AS PO_COUNT, CREATED_AT_EPOCH FROM PURCHASE_ORDER where ID='"+id+"' GROUP BY ID, CREATED_AT_EPOCH ";
		Map<Long,Integer> results= new LinkedHashMap<Long,Integer>();
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			DataSource dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			resultSet= preparedStatement.executeQuery();
			while(resultSet.next()) {				
				results.put(resultSet.getLong("CREATED_AT_EPOCH"), resultSet.getInt("PO_COUNT"));
			}
			return results;

			
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
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
		return results;
	}
	
	@Override
	public UpdatePurchaseOrder newUpdateRequest() {
		return new UpdatePurchaseOrder();
	}
	
	@Override
	public BookStorePurchaseOrderQuery newQueryRequest(){
		BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery= new BookStorePurchaseOrderQuery(purchaseOrderSchema);
		PurchaseOrderAttributeAccess purchaseOrderAttributeAccess= new PurchaseOrderAttributeAccess();
		bookStorePurchaseOrderQuery.setAttribute(purchaseOrderAttributeAccess);
		purchaseOrderAttributeAccess.setBookStorePurchaseOrderQuery(bookStorePurchaseOrderQuery);
		return bookStorePurchaseOrderQuery;
	}
	public class BookStorePurchaseOrderQuery extends BookStoreQuery<BookStorePurchaseOrderQuery,PurchaseOrderAttributeAccess>{
		public BookStorePurchaseOrderQuery(BookStorePurchaseOrderQuery query, DataSchema dataSchema){
			super(query,  dataSchema);
		}
		public BookStorePurchaseOrderQuery(DataSchema dataSchema){
			super(dataSchema);
		}
		public BookStorePurchaseOrderQuery includePurchaseOrderStatusInResult(){
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.put(purchaseOrderSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.STATUS);
			includeKeyInResults();
			return this;
		}
		
		public BookStorePurchaseOrderQuery includePurchaseOrderAmountInResult(){
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.put(purchaseOrderSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.AMOUNT);
			includeKeyInResults();
			return this;
		}
		
		public BookStorePurchaseOrderQuery includeCustomerEmailInResult(){
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.put(purchaseOrderSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.EMAIL);
				includeKeyInResults();
				return this;
		}
		
		public BookStorePurchaseOrderQuery includeSiteUserAddressInResult() {
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.put(purchaseOrderSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.STREET_NUMBER);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.STREET);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.POSTAL_CODE);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.CITY);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.PROVINCE);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.COUNTRY);
			includeKeyInResults();
			return this;
		}
		
		public BookStorePurchaseOrderQuery excludeSiteUserAddressInResult(){
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.STREET_NUMBER);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.STREET);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.POSTAL_CODE);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.CITY);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.PROVINCE);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.COUNTRY);
			return this;
		}
		
		public BookStorePurchaseOrderQuery includePurchaseOrderCreditCardInResult() {
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.put(purchaseOrderSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.CREDIT_CARD);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.CREDIT_CARD_NUMBER);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.CREDIT_CARD_EXPIRY);
			
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.CREDIT_CARD_CVV2);
			includeKeyInResults();
			return this;
		}
		public BookStorePurchaseOrderQuery excludePurchaseOrderCreditCardInResult(){
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.CREDIT_CARD);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.CREDIT_CARD_NUMBER);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.CREDIT_CARD_EXPIRY);
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.CREDIT_CARD_CVV2);
			return this;
		}

		public BookStorePurchaseOrderQuery excludePurchaseOrderAmountInResult(){
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.AMOUNT);
			return this;
		}
		
		public BookStorePurchaseOrderQuery excludePurchaseOrderEmailInResult(){
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.EMAIL);
			return this;
		}
		
		public BookStorePurchaseOrderQuery excludePurchaseOrderStatusInResult(){
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.STATUS);
			return this;
		}
		
		public BookStorePurchaseOrderQuery excludePurchaseOrderBookISBN(){
			if(this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName())) this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).remove(purchaseOrderSchema.STATUS);
			return this;
		}

		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.BOOK)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new BookSchema().tableName()+this.referenceOperator+BookSchema.ID)
					.build()
					);
			return new BookDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		public BookStoreCustomerQuery queryCustomer() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		private void includeKeyInResults() {
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName()) && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).contains(purchaseOrderSchema.ID))
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.ID);
			
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName()) && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).contains(purchaseOrderSchema.CREATED_AT_EPOCH))
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.CREATED_AT_EPOCH);
			
			
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName()) && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).contains(purchaseOrderSchema.BOOK))
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.BOOK);

			
			if(!this.attributesToIncludInResults.containsKey(purchaseOrderSchema.tableName()) && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).contains(purchaseOrderSchema.ISBN))
			this.attributesToIncludInResults.get(purchaseOrderSchema.tableName()).add(purchaseOrderSchema.ISBN);
		}		
	}

	public class PurchaseOrderVarCharQuery extends BookStoreVarCharQuery<PurchaseOrderVarCharQuery,PurchaseOrderAttributeAccess,BookStorePurchaseOrderQuery>{
		private PurchaseOrderAttributeAccess purchaseOrderAttributeAccess;
		PurchaseOrderVarCharQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess){
			super(bookStorePurchaseOrderQuery,new PurchaseOrderSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		PurchaseOrderVarCharQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStorePurchaseOrderQuery,new PurchaseOrderSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public PurchaseOrderAttributeAccess queryPurchaseOrderAttribute(){
			return purchaseOrderAttributeAccess;
		}
		public BookStoreBookQuery queryBook() {
			return new BookDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData);		
		}
		public BookStoreCustomerQuery queryCustomer() {
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData);
		}


	}
	public class PurchaseOrderNumberQuery extends BookStoreNumberQuery<PurchaseOrderNumberQuery,PurchaseOrderAttributeAccess,BookStorePurchaseOrderQuery>{
		private PurchaseOrderAttributeAccess purchaseOrderAttributeAccess;
		PurchaseOrderNumberQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess){
			super(bookStorePurchaseOrderQuery,new PurchaseOrderSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		PurchaseOrderNumberQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStorePurchaseOrderQuery,new PurchaseOrderSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public PurchaseOrderAttributeAccess queryPurchaseOrderAttribute(){
			return purchaseOrderAttributeAccess;
		}
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.BOOK)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new BookSchema().tableName()+this.referenceOperator+BookSchema.ID)
					.build()
					);
			return new BookDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		public BookStoreCustomerQuery queryCustomer() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	
	public abstract class PurchaseOrderObjectQuery<T extends PurchaseOrderObjectQuery> extends BookStoreQuery<T,PurchaseOrderAttributeAccess>{
		protected PurchaseOrderAttributeAccess purchaseOrderAttributeAccess;

		PurchaseOrderObjectQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess){
			super(bookStorePurchaseOrderQuery,new PurchaseOrderSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		PurchaseOrderObjectQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStorePurchaseOrderQuery,new PurchaseOrderSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public PurchaseOrderAttributeAccess queryPurchaseOrderAttribute(){
			return purchaseOrderAttributeAccess;
		}
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.BOOK)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new BookSchema().tableName()+this.referenceOperator+BookSchema.ID)
					.build()
					);
			return new BookDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		public BookStoreCustomerQuery queryCustomer() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	
	
	public class PurchaseOrderCustomerQuery extends PurchaseOrderObjectQuery<PurchaseOrderCustomerQuery>{
		PurchaseOrderCustomerQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess);
			// TODO Auto-generated constructor stub
		}
		PurchaseOrderCustomerQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public PurchaseOrderCustomerQuery isCustomer(Customer customer) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(customer.getId().toString())
					.build()
					);
			return  this;
		}
		
		public PurchaseOrderCustomerQuery isSiteUser(SiteUser siteUser) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(siteUser.getId().toString())
					.build()
					);
			return  this;
		}
	}
	
	public class PurchaseOrderBookQuery extends PurchaseOrderObjectQuery<PurchaseOrderBookQuery>{
		PurchaseOrderBookQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess);
		}
		PurchaseOrderBookQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public PurchaseOrderBookQuery isBook(Book book) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(book.getId().toString())
					.build()
					);
			return  this;
		}
		
		public PurchaseOrderBookQuery isBookISBN(Book book) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ISBN)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(book.getISBN())
					.build()
					);
			return  this;
		}
		
		public PurchaseOrderBookQuery isBookISBN(String ISBN) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ISBN)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(ISBN)
					.build()
					);
			return  this;
		}


	}
	public class PurchaseOrderUserTypeQuery extends PurchaseOrderObjectQuery<PurchaseOrderUserTypeQuery>{
		PurchaseOrderUserTypeQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess);
		}
		PurchaseOrderUserTypeQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess, pageRequestMetaData);
			// TODO Auto-generated constructor stub
		}

		public PurchaseOrderUserTypeQuery isPurchaseOrderUserTypeCustomer() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(UserTypes.CUSTOMER+"'")
					.build()
					);
			return  this;
		}
		
		public PurchaseOrderUserTypeQuery isPurchaseOrderUserTypeVisitor() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(UserTypes.VISITOR+"'")
					.build()
					);
			return  this;
		}

	}
	public class PurchaseOrderStatusQuery extends PurchaseOrderObjectQuery<PurchaseOrderStatusQuery>{
		PurchaseOrderStatusQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess);
		}
		PurchaseOrderStatusQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public PurchaseOrderStatusQuery isProcessed() {			
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.STATUS)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(PurchaseOrderSchema.PROCESSED_STATUS)
					.build()
					);
			return this;
		}
		
		public PurchaseOrderStatusQuery isOrdered() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.STATUS)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(PurchaseOrderSchema.ORDERED_STATUS)
					.build()
					);
			return this;
		}
		public PurchaseOrderStatusQuery isShipped() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.STATUS)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(PurchaseOrderSchema.SHIPPED_STATUS)
					.build()
					);
			return this;
		}
		public PurchaseOrderStatusQuery isDelivered() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.STATUS)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(PurchaseOrderSchema.DELIVERED_STATUS)
					.build()
					);
			return this;
		}
		public PurchaseOrderStatusQuery isDenied() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.STATUS)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(PurchaseOrderSchema.DENIED_STATUS)
					.build()
					);
			return this;
		}
	}
	
	public class PurchaseOrderKeyQuery extends PurchaseOrderObjectQuery<PurchaseOrderKeyQuery>{
		PurchaseOrderKeyQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess);
		}
		PurchaseOrderKeyQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStorePurchaseOrderQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public PurchaseOrderKeyQuery isPurchaseOrder(PurchaseOrder purchaseOrder) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(purchaseOrderSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(purchaseOrder.getId().toString())
					.build()
					);

			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(purchaseOrderSchema.CREATED_AT_EPOCH)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(Long.toString(purchaseOrder.getCreatedAtEpoch()))
					.build()
					);
			return  this;
		}

	}
	public class PurchaseOrderAttributeAccess extends AttributeAccess<BookStorePurchaseOrderQuery>{
		protected BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery;
		protected PurchaseOrderAttributeAccess() {
		}
		protected void setBookStorePurchaseOrderQuery(BookStorePurchaseOrderQuery bookStorePurchaseOrderQuery){
			this.bookStorePurchaseOrderQuery=bookStorePurchaseOrderQuery;
		}
		public PurchaseOrderKeyQuery wherePurchaseOrder(){
			PurchaseOrderKeyQuery purchaseOrderKeyQuery= new PurchaseOrderKeyQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.ID);
			purchaseOrderKeyQuery.setAttribute(this);
			return purchaseOrderKeyQuery;
		}
		
		public PurchaseOrderStatusQuery wherePurchaseOrderStatus(){
			PurchaseOrderStatusQuery purchaseOrderStatusQuery= new PurchaseOrderStatusQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.STATUS);
			purchaseOrderStatusQuery.setAttribute(this);
			return purchaseOrderStatusQuery;
		}
		
		
		public PurchaseOrderNumberQuery wherePurchaseOrderAmount(){
			PurchaseOrderNumberQuery purchaseOrderNumberQuery= new PurchaseOrderNumberQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.AMOUNT);
			purchaseOrderNumberQuery.setAttribute(this);
			return purchaseOrderNumberQuery;
		}
		
		public PurchaseOrderNumberQuery wherePurchaseOrderCreatedAtEpoch(){
			PurchaseOrderNumberQuery purchaseOrderNumberQuery= new PurchaseOrderNumberQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.CREATED_AT_EPOCH);
			purchaseOrderNumberQuery.setAttribute(this);
			return purchaseOrderNumberQuery;
		}
		
		
		public PurchaseOrderBookQuery wherePurchaseOrderBook(){
			PurchaseOrderBookQuery purchaseOrderBookQuery= new PurchaseOrderBookQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.BOOK);
			purchaseOrderBookQuery.setAttribute(this);
			return purchaseOrderBookQuery;
		}
		
		public PurchaseOrderCustomerQuery wherePurchaseOrderCustomer(){
			PurchaseOrderCustomerQuery purchaseOrderUserQuery= new PurchaseOrderCustomerQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.ID);
			purchaseOrderUserQuery.setAttribute(this);
			return purchaseOrderUserQuery;
		}
		//
		public PurchaseOrderVarCharQuery wherePurchaseOrderEmail(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.EMAIL);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}
		
		public PurchaseOrderVarCharQuery wherePurchaseOrderStreet(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.STREET);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}
		
		public PurchaseOrderVarCharQuery wherePurchaseOrderStreetNumber(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.STREET_NUMBER);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}
		
		
		public PurchaseOrderVarCharQuery wherePurchaseOrderPostalCode(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.POSTAL_CODE);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}
		
		
		public PurchaseOrderVarCharQuery wherePurchaseOrderCity(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.CITY);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}
		
		public PurchaseOrderVarCharQuery wherePurchaseOrderProvince(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.PROVINCE);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}
		
		public PurchaseOrderVarCharQuery wherePurchaseOrderCountry(){
			PurchaseOrderVarCharQuery purchaseOrderVarCharQuery= new PurchaseOrderVarCharQuery(this.bookStorePurchaseOrderQuery,PurchaseOrderSchema.COUNTRY);
			purchaseOrderVarCharQuery.setAttribute(this);
			return purchaseOrderVarCharQuery;
		}		
	}
}