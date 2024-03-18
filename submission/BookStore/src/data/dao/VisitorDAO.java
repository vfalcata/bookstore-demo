package data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.beans.Review;
import data.beans.Visitor;
import data.dao.CartDAO.BookStoreCartQuery;
import data.dao.CustomerDAO.BookStoreCustomerQuery;
import data.dao.CustomerDAO.CustomerNumberQuery;
import data.dao.CustomerDAO.CustomerVarCharQuery;
import data.dao.PurchaseOrderDAO.BookStorePurchaseOrderQuery;
import data.dao.ReviewDAO.ReviewAttributeAccess;
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
public class VisitorDAO implements DAO{
	private VisitorSchema visitorSchema;
	public VisitorDAO(){
		this.visitorSchema=new VisitorSchema();
	}
	
	@Override
	public UpdateVisitor newUpdateRequest() {
		return new UpdateVisitor();
	}
	
	public Visitor getVisitor(HttpServletRequest request) {
		List<Visitor> visitors=newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereVisitor()
				.isVisitor(request.getSession().getId())
				.queryCart()
				.includeAllAttributesInResultFromSchema()
				.queryBook()
				.includeAllAttributesInResultFromSchema()
				.queryReview()
				.includeAllAttributesInResultFromSchema()
				.executeQuery()
				.compileVisitors(); 
		Visitor visitor=null;
		if(visitors.size()>0) {
			for(Visitor visitorQuery:visitors) {
				visitor=visitorQuery;
			}
		}else {
			visitor=newUpdateRequest().executeInsertNewVisitor(request);
		}
		 return visitor;
	}
	
	@Override
	public BookStoreVisitorQuery newQueryRequest(){
		BookStoreVisitorQuery bookStoreVisitorQuery= new BookStoreVisitorQuery(visitorSchema);
		VisitorAttributeAccess visitorAttributeAccess= new VisitorAttributeAccess();
		bookStoreVisitorQuery.setAttribute(visitorAttributeAccess);
		visitorAttributeAccess.setBookStoreVisitorQuery(bookStoreVisitorQuery);
		return bookStoreVisitorQuery;
	}
	public class BookStoreVisitorQuery extends BookStoreQuery<BookStoreVisitorQuery,VisitorAttributeAccess>{
		public BookStoreVisitorQuery(BookStoreVisitorQuery query, DataSchema dataSchema){
			super(query,  dataSchema);
		}
		public BookStoreVisitorQuery(DataSchema dataSchema){
			super(dataSchema);
//			this.dataAccessRequests.
		}
		public BookStoreVisitorQuery includeVisitorCreatedAtEpochInResult(){
			if(!this.attributesToIncludInResults.containsKey(visitorSchema.tableName())) this.attributesToIncludInResults.put(visitorSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(visitorSchema.tableName()).add(visitorSchema.ID);
			return this;
		}

		private void includeKeyInResults() {
			if(!this.attributesToIncludInResults.containsKey(visitorSchema.tableName())) this.attributesToIncludInResults.put(visitorSchema.tableName(), new HashSet<String>());
			if(!this.attributesToIncludInResults.get(visitorSchema.tableName()).contains(visitorSchema.ID)) this.attributesToIncludInResults.get(visitorSchema.tableName()).add(visitorSchema.ID);
			if(!this.attributesToIncludInResults.get(visitorSchema.tableName()).contains(visitorSchema.USER_TYPE)) this.attributesToIncludInResults.get(visitorSchema.tableName()).add(visitorSchema.USER_TYPE);
		}
		

		public BookStoreCartQuery queryCart() {

			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(VisitorSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);
			

			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData);
		}

		
	}
	public class VisitorVarCharQuery extends BookStoreVarCharQuery<VisitorVarCharQuery,VisitorAttributeAccess,BookStoreVisitorQuery>{
		private VisitorAttributeAccess visitorAttributeAccess;
		VisitorVarCharQuery(BookStoreVisitorQuery bookStoreVisitorQuery, String currentAttributeAccess){
			super(bookStoreVisitorQuery,new VisitorSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		VisitorVarCharQuery(BookStoreVisitorQuery bookStoreVisitorQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreVisitorQuery,new VisitorSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public VisitorAttributeAccess queryVisitorAttribute(){
			return visitorAttributeAccess;
		}
		public BookStoreCartQuery queryCart() {

			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(VisitorSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);

			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData);
		}

	}
	public class VisitorNumberQuery extends BookStoreNumberQuery<VisitorNumberQuery,VisitorAttributeAccess,BookStoreVisitorQuery>{
		private VisitorAttributeAccess visitorAttributeAccess;
		VisitorNumberQuery(BookStoreVisitorQuery bookStoreVisitorQuery, String currentAttributeAccess){
			super(bookStoreVisitorQuery,new VisitorSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		VisitorNumberQuery(BookStoreVisitorQuery bookStoreVisitorQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreVisitorQuery,new VisitorSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public VisitorAttributeAccess queryVisitorAttribute(){
			return visitorAttributeAccess;
		}
		public BookStoreCartQuery queryCart() {

			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(VisitorSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);

			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData);
		}

	}
	
	public class VisitorKeyQuery extends BookStoreQuery<VisitorKeyQuery,VisitorAttributeAccess>{
		private VisitorAttributeAccess visitorAttributeAccess;
		VisitorKeyQuery(BookStoreVisitorQuery bookStoreVisitorQuery, String currentAttributeAccess){
			super(bookStoreVisitorQuery,new VisitorSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		VisitorKeyQuery(BookStoreVisitorQuery bookStoreVisitorQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreVisitorQuery,new VisitorSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public VisitorAttributeAccess queryVisitorAttribute(){
			return visitorAttributeAccess;
		}
		public BookStoreCartQuery queryCart() {

			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(VisitorSchema.ID)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CartSchema().tableName()+this.referenceOperator+CartSchema.ID)
					.build()
					);

			return new CartDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData);
		}
		
		public VisitorKeyQuery isVisitor(Visitor visitor) {

			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(visitorSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(visitor.getId().toString())
					.build()
					);
			return  this;
		}
		
		private VisitorKeyQuery isVisitor(HttpServletRequest request) {

			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(visitorSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(request.getSession().getId())
					.build()
					);
			return  this;
		}
		
		private VisitorKeyQuery isVisitor(String visitorId) {

			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(visitorSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(visitorId)
					.build()
					);
			return  this;
		}

	}
	
	public class VisitorAttributeAccess extends AttributeAccess<BookStoreVisitorQuery>{
		protected BookStoreVisitorQuery bookStoreVisitorQuery;
		protected VisitorAttributeAccess() {
		}
		
		protected void setBookStoreVisitorQuery(BookStoreVisitorQuery bookStoreVisitorQuery){
			this.bookStoreVisitorQuery=bookStoreVisitorQuery;
		}
		public VisitorKeyQuery whereVisitor(){
			VisitorKeyQuery visitorKeyQuery= new VisitorKeyQuery(this.bookStoreVisitorQuery,VisitorSchema.ID);
			visitorKeyQuery.setAttribute(this);
			return visitorKeyQuery;
		}
		
		public VisitorNumberQuery whereVisitorCreatedAtEpoch() {
			VisitorNumberQuery visitorNumberQuery= new VisitorNumberQuery(this.bookStoreVisitorQuery,VisitorSchema.CREATED_AT_EPOCH);
			visitorNumberQuery.setAttribute(this);
			return visitorNumberQuery;
		}
		
		public VisitorNumberQuery whereVisitorLastAccessedAtEpoch() {
			VisitorNumberQuery visitorNumberQuery= new VisitorNumberQuery(this.bookStoreVisitorQuery,VisitorSchema.LAST_ACCESSED_AT_EPOCH);
			visitorNumberQuery.setAttribute(this);
			return visitorNumberQuery;
		}
	}
}