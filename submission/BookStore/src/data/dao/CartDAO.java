package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.Review;
import data.beans.User;
import data.dao.BookDAO.BookAttributeAccess;
import data.dao.BookDAO.BookStoreBookQuery;
import data.dao.CartDAO.BookStoreCartQuery;
import data.dao.CartDAO.CartObjectQuery;
import data.dao.CustomerDAO.BookStoreCustomerQuery;
import data.dao.PurchaseOrderDAO.BookStorePurchaseOrderQuery;
import data.dao.PurchaseOrderDAO.PurchaseOrderAttributeAccess;
import data.dao.PurchaseOrderDAO.PurchaseOrderObjectQuery;
import data.dao.ReviewDAO.BookStoreReviewQuery;
import data.dao.VisitorDAO.BookStoreVisitorQuery;
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
import data.schema.VisitorSchema;
public class CartDAO implements DAO{
	private CartSchema cartSchema;
	public CartDAO(){
		this.cartSchema=new CartSchema();
	}
	@Override
	public UpdateCart newUpdateRequest() {
		return new UpdateCart();
	}
	
	@Override
	public BookStoreCartQuery newQueryRequest(){
		BookStoreCartQuery bookStoreCartQuery= new BookStoreCartQuery(cartSchema);
		CartAttributeAccess cartAttributeAccess= new CartAttributeAccess();
		bookStoreCartQuery.setAttribute(cartAttributeAccess);
		cartAttributeAccess.setBookStoreCartQuery(bookStoreCartQuery);
		bookStoreCartQuery.includeKeyInResults();
		return bookStoreCartQuery;
	}
	public class BookStoreCartQuery extends BookStoreQuery<BookStoreCartQuery,CartAttributeAccess>{
		public BookStoreCartQuery(BookStoreCartQuery query, DataSchema dataSchema){
			super(query,  dataSchema);
			
		}
		public BookStoreCartQuery(DataSchema dataSchema){
			super(dataSchema);
		}
		
		private void includeKeyInResults() {
			if(!this.attributesToIncludInResults.containsKey(cartSchema.tableName())) this.attributesToIncludInResults.put(cartSchema.tableName(), new HashSet<String>());
			if(!this.attributesToIncludInResults.get(cartSchema.tableName()).contains(cartSchema.ID)) this.attributesToIncludInResults.get(cartSchema.tableName()).add(cartSchema.ID);
			if(!this.attributesToIncludInResults.get(cartSchema.tableName()).contains(cartSchema.BOOK)) this.attributesToIncludInResults.get(cartSchema.tableName()).add(cartSchema.BOOK);
			if(!this.attributesToIncludInResults.get(cartSchema.tableName()).contains(cartSchema.AMOUNT)) this.attributesToIncludInResults.get(cartSchema.tableName()).add(cartSchema.AMOUNT);

		}
	
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.BOOK)
					.withDataAccessParameterPrefix("=")
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
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStoreVisitorQuery queryVisitor() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new VisitorSchema().tableName()+this.referenceOperator+VisitorSchema.ID)
					.build()
					);
			return new VisitorDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	public class CartVarCharQuery extends BookStoreVarCharQuery<CartVarCharQuery,CartAttributeAccess,BookStoreCartQuery>{
		private CartAttributeAccess cartAttributeAccess;
		
		CartVarCharQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess){
			super(bookStoreCartQuery,new CartSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		CartVarCharQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreCartQuery,new CartSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public CartAttributeAccess queryCartAttribute(){
			return cartAttributeAccess;
		}
		
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.BOOK)
					.withDataAccessParameterPrefix("=")
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
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStoreVisitorQuery queryVisitor() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new VisitorSchema().tableName()+this.referenceOperator+VisitorSchema.ID)
					.build()
					);
			return new VisitorDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	
	public class CartNumberQuery extends BookStoreNumberQuery<CartNumberQuery,CartAttributeAccess,BookStoreCartQuery>{
		private CartAttributeAccess cartAttributeAccess;
		CartNumberQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess){
			super(bookStoreCartQuery,new CartSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		CartNumberQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreCartQuery,new CartSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public CartAttributeAccess queryCartAttribute(){
			return cartAttributeAccess;
		}
		
		
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.BOOK)
					.withDataAccessParameterPrefix("=")
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
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStoreVisitorQuery queryVisitor() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new VisitorSchema().tableName()+this.referenceOperator+VisitorSchema.ID)
					.build()
					);
			return new VisitorDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	
	public abstract class CartObjectQuery<T extends CartObjectQuery> extends BookStoreQuery<T,CartAttributeAccess>{
		protected CartAttributeAccess cartAttributeAccess;

		CartObjectQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess){
			super(bookStoreCartQuery,new CartSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		CartObjectQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreCartQuery,new CartSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public CartAttributeAccess queryCartAttribute(){
			return cartAttributeAccess;
		}
		
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.BOOK)
					.withDataAccessParameterPrefix("=")
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
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		public BookStoreVisitorQuery queryVisitor() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new VisitorSchema().tableName()+this.referenceOperator+VisitorSchema.ID)
					.build()
					);
			return new VisitorDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}
	
	
	public class CartCustomerQuery extends CartObjectQuery<CartCustomerQuery>{


		CartCustomerQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess) {
			super(bookStoreCartQuery, currentAttributeAccess);
		}

		CartCustomerQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreCartQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public CartCustomerQuery isCustomer(Customer customer) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(CartSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(customer.getId().toString())
					.build()
					);
			return  this;
		}
		
	}
	
	public class CartKeyQuery extends CartObjectQuery<CartKeyQuery>{

		CartKeyQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess) {
			super(bookStoreCartQuery, currentAttributeAccess);
		}

		CartKeyQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreCartQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public CartKeyQuery isCart(Cart cart) {
		this.addDataAccessString(new DataAccessString.Builder()
				.withTableName(this.dataSchema.tableName())
				.withReferenceOperator(this.referenceOperator)
				.withAttributeName(CartSchema.ID)
				.withDataAccessParameterPrefix("="+"'")
				.withDataAccessParameterSuffix("'")
				.withDataAccessParameter(cart.getId().toString())
				.build()
				);
		return  this;
		}	
	}
	
	public class CartBookQuery extends CartObjectQuery<CartBookQuery>{

		CartBookQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess) {
			super(bookStoreCartQuery, currentAttributeAccess);
		}

		CartBookQuery(BookStoreCartQuery bookStoreCartQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreCartQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public CartBookQuery isBook(Book book) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(PurchaseOrderSchema.BOOK)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(book.getId().toString())
					.build()
					);
			return  this;
		}	
	}
	
	public class CartAttributeAccess extends AttributeAccess<BookStoreCartQuery>{
		protected BookStoreCartQuery bookStoreCartQuery;
		protected CartAttributeAccess() {
		}
		protected void setBookStoreCartQuery(BookStoreCartQuery bookStoreCartQuery){
			this.bookStoreCartQuery=bookStoreCartQuery;
		}
		
		public CartKeyQuery whereCart(){
			CartKeyQuery cartKeyQuery= new CartKeyQuery(this.bookStoreCartQuery,CartSchema.ID);
			cartKeyQuery.setAttribute(this);
			return cartKeyQuery;
		}
		
		
		public CartNumberQuery whereCartBookAmount(){
			CartNumberQuery  cartNumberQuery= new CartNumberQuery(this.bookStoreCartQuery,CartSchema.AMOUNT);
			cartNumberQuery.setAttribute(this);
			return cartNumberQuery;
		}
		
		public CartBookQuery whereCartBook(){
			CartBookQuery cartBookQuery= new CartBookQuery(this.bookStoreCartQuery,CartSchema.BOOK);
			cartBookQuery.setAttribute(this);
			return cartBookQuery;
		}
		
		public CartCustomerQuery whereCartCustomer(){
			CartCustomerQuery cartCustomerQuery= new CartCustomerQuery(this.bookStoreCartQuery,CartSchema.ID);
			cartCustomerQuery.setAttribute(this);
			return cartCustomerQuery;
		}
	}
}