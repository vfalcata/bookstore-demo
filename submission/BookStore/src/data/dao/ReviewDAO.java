package data.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import data.beans.Bean;
import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.beans.Review;
import data.beans.User;
import data.beans.Visitor;
import data.dao.BookDAO.BookStoreBookQuery;
import data.dao.CustomerDAO.BookStoreCustomerQuery;
import data.dao.PurchaseOrderDAO.BookStorePurchaseOrderQuery;
import data.dao.PurchaseOrderDAO.PurchaseOrderAttributeAccess;
import data.dao.PurchaseOrderDAO.PurchaseOrderBookQuery;
import data.dao.PurchaseOrderDAO.PurchaseOrderObjectQuery;
import data.dao.PurchaseOrderDAO.PurchaseOrderCustomerQuery;
import data.dao.ReviewDAO.BookStoreReviewQuery;
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
public class ReviewDAO implements DAO{
	private ReviewSchema reviewSchema;
	public ReviewDAO(){
		this.reviewSchema=new ReviewSchema();
	}
	
	public int getReviewCount(Id id) {
		return getReviewCount(id.toString());
	}
	
	public int getReviewCount(Customer customer) {
		return getReviewCount(customer.getId().toString());
	}
	public int getReviewCount(String id){
		String queryString="SELECT COUNT(BOOK) AS REVIEW_COUNT FROM REVIEW where SITE_USER='"+id+"' GROUP BY SITE_USER";
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			DataSource dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			resultSet= preparedStatement.executeQuery();
			while(resultSet.next()) {				
				return resultSet.getInt("REVIEW_COUNT");
			}			
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
		return 0;
	}
	
	@Override
	public UpdateReview newUpdateRequest() {
		return new UpdateReview();
	}
	
	@Override
	public BookStoreReviewQuery newQueryRequest(){
		BookStoreReviewQuery bookStoreReviewQuery= new BookStoreReviewQuery(reviewSchema);
		ReviewAttributeAccess reviewAttributeAccess= new ReviewAttributeAccess();
		bookStoreReviewQuery.setAttribute(reviewAttributeAccess);
		reviewAttributeAccess.setBookStoreReviewQuery(bookStoreReviewQuery);
		return bookStoreReviewQuery;
	}
	
	public class BookStoreReviewQuery extends BookStoreQuery<BookStoreReviewQuery,ReviewAttributeAccess>{
		public BookStoreReviewQuery(BookStoreReviewQuery query, DataSchema dataSchema){
			super(query,  dataSchema);
		}
		public BookStoreReviewQuery(DataSchema dataSchema){
			super(dataSchema);
		}
		public BookStoreReviewQuery includeReviewBodyInResult(){
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.put(reviewSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.BODY);				
			includeKeyInResults();
			return this;
		}
		public BookStoreReviewQuery includeReviewTitleInResult(){
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.put(reviewSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.TITLE);
			includeKeyInResults();
			return this;
		}
		public BookStoreReviewQuery includeReviewRatingInResult(){
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.put(reviewSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.RATING);
				includeKeyInResults();
				return this;
		}
		
		public BookStoreReviewQuery includeReviewCreatedAtEpochInResult(){
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.put(reviewSchema.tableName(), new HashSet<String>());
				this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.CREATED_AT_EPOCH);
				includeKeyInResults();
				return this;
		}

		public BookStoreReviewQuery excludeReviewBodyInResult(){
			if(this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.get(reviewSchema.tableName()).remove(reviewSchema.BODY);
			return this;
		}
		public BookStoreReviewQuery excludeReviewTitleInResult(){
			if(this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.get(reviewSchema.tableName()).remove(reviewSchema.TITLE);
			return this;
		}
		public BookStoreReviewQuery excludeReviewRatingInResult(){
			if(this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.get(reviewSchema.tableName()).remove(reviewSchema.RATING);
			return this;
		}
		
		public BookStoreReviewQuery excludeReviewCreatedAtEpochInResult(){
			if(this.attributesToIncludInResults.containsKey(reviewSchema.tableName())) this.attributesToIncludInResults.get(reviewSchema.tableName()).remove(reviewSchema.CREATED_AT_EPOCH);
			return this;
		}
		
		public BookStoreBookQuery queryBooks() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.BOOK)
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
					.withAttributeName(ReviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		private void includeKeyInResults() {
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName()) && !this.attributesToIncludInResults.get(reviewSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(reviewSchema.tableName()).contains(reviewSchema.BOOK))
			this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.BOOK);
			
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName()) && !this.attributesToIncludInResults.get(reviewSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(reviewSchema.tableName()).contains(reviewSchema.SITE_USER))
			this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.SITE_USER);
			
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName()) && !this.attributesToIncludInResults.get(reviewSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(reviewSchema.tableName()).contains(reviewSchema.USER_TYPE))
			this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.USER_TYPE);
			
			if(!this.attributesToIncludInResults.containsKey(reviewSchema.tableName()) && !this.attributesToIncludInResults.get(reviewSchema.tableName()).isEmpty() && !this.attributesToIncludInResults.get(reviewSchema.tableName()).contains(reviewSchema.NAME))
			this.attributesToIncludInResults.get(reviewSchema.tableName()).add(reviewSchema.NAME);

		}
	}
	public class ReviewVarCharQuery extends BookStoreVarCharQuery<ReviewVarCharQuery,ReviewAttributeAccess,BookStoreReviewQuery>{
		private ReviewAttributeAccess reviewAttributeAccess;
		ReviewVarCharQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess){
			super(bookStoreReviewQuery,new ReviewSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		ReviewVarCharQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreReviewQuery,new ReviewSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public ReviewAttributeAccess queryReviewAttribute(){
			return reviewAttributeAccess;
		}
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.BOOK)
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
					.withAttributeName(ReviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}

	}
	public class ReviewNumberQuery extends BookStoreNumberQuery<ReviewNumberQuery,ReviewAttributeAccess,BookStoreReviewQuery>{
		private ReviewAttributeAccess reviewAttributeAccess;
		ReviewNumberQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess){
			super(bookStoreReviewQuery,new ReviewSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		ReviewNumberQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreReviewQuery,new ReviewSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		public ReviewAttributeAccess queryReviewAttribute(){
			return reviewAttributeAccess;
		}
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.BOOK)
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
					.withAttributeName(ReviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}

	}
	
	
	public abstract class ReviewObjectQuery<T extends ReviewObjectQuery> extends BookStoreQuery<T,ReviewAttributeAccess>{
		protected ReviewAttributeAccess reviewAttributeAccess;

		ReviewObjectQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess){
			super(bookStoreReviewQuery,new ReviewSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		ReviewObjectQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess,PageRequestMetaData pageRequestMetaData){
			super(bookStoreReviewQuery,new ReviewSchema());
			this.currentAttributeAccess=currentAttributeAccess;
			this.pageRequestMetaData=pageRequestMetaData;
		}
		
		public ReviewAttributeAccess queryReviewAttributeAccess(){
			return reviewAttributeAccess;
		}
		
		public BookStoreBookQuery queryBook() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.BOOK)
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
					.withAttributeName(ReviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new CustomerSchema().tableName()+this.referenceOperator+CustomerSchema.ID)
					.build()
					);
			return new CustomerDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
	}	
	
	public class ReviewSiteUserQuery extends ReviewObjectQuery<ReviewSiteUserQuery>{

		ReviewSiteUserQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess) {
			super(bookStoreReviewQuery, currentAttributeAccess);
		}
		ReviewSiteUserQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreReviewQuery, currentAttributeAccess, pageRequestMetaData);
		}

		public ReviewSiteUserQuery isCustomer(Customer customer) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(customer.getId().toString())
					.build()
					);
			return  this;
		}
		
		public ReviewSiteUserQuery isVisitor(HttpServletRequest request) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(request.getSession().getId())
					.build()
					);
			return  this;
		}

	}
	
	public class ReviewBookQuery extends ReviewObjectQuery<ReviewBookQuery>{
		ReviewBookQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess) {
			super(bookStoreReviewQuery, currentAttributeAccess);
		}
		
		ReviewBookQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreReviewQuery, currentAttributeAccess, pageRequestMetaData);
		}


		public ReviewBookQuery isBook(Book book) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(ReviewSchema.BOOK)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(book.getId().toString())
					.build()
					);
			return  this;
		}
	}
	
	public class ReviewKeyQuery extends ReviewObjectQuery<ReviewKeyQuery>{
		ReviewKeyQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess) {
			super(bookStoreReviewQuery, currentAttributeAccess);
		}
		
		ReviewKeyQuery(BookStoreReviewQuery bookStoreReviewQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreReviewQuery, currentAttributeAccess, pageRequestMetaData);
		}


		public ReviewKeyQuery isReview(Review review) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(reviewSchema.SITE_USER)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(review.getSiteUser().getId().toString())
					.build()
					);

			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(reviewSchema.BOOK)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(review.getBook().getId().toString())
					.build()
					);
			return  this;
		}
	}
	
	public class ReviewAttributeAccess extends AttributeAccess<BookStoreReviewQuery>{
		protected BookStoreReviewQuery bookStoreReviewQuery;
		protected ReviewAttributeAccess() {
		}
		protected void setBookStoreReviewQuery(BookStoreReviewQuery bookStoreReviewQuery){
			this.bookStoreReviewQuery=bookStoreReviewQuery;
		}
		
		public ReviewKeyQuery whereReview(){
			ReviewKeyQuery reviewKeyQuery= new ReviewKeyQuery(this.bookStoreReviewQuery,ReviewSchema.SITE_USER);
			reviewKeyQuery.setAttribute(this);
			return reviewKeyQuery;
		}
		
		public ReviewVarCharQuery whereReviewBody(){
			ReviewVarCharQuery reviewVarCharQuery= new ReviewVarCharQuery(this.bookStoreReviewQuery,ReviewSchema.BODY);
			reviewVarCharQuery.setAttribute(this);
			return reviewVarCharQuery;
		}
		public ReviewVarCharQuery whereReviewTitle(){
			ReviewVarCharQuery reviewVarCharQuery= new ReviewVarCharQuery(this.bookStoreReviewQuery,ReviewSchema.TITLE);
			reviewVarCharQuery.setAttribute(this);
			return reviewVarCharQuery;
		}
		public ReviewNumberQuery whereReviewRating(){
			ReviewNumberQuery reviewNumberQuery= new ReviewNumberQuery(this.bookStoreReviewQuery,ReviewSchema.RATING);
			reviewNumberQuery.setAttribute(this);
			return reviewNumberQuery;
		}
		
		public ReviewNumberQuery whereReviewCreatedAtEpoch(){
			ReviewNumberQuery reviewNumberQuery= new ReviewNumberQuery(this.bookStoreReviewQuery,ReviewSchema.CREATED_AT_EPOCH);
			reviewNumberQuery.setAttribute(this);
			return reviewNumberQuery;
		}
		
		public ReviewBookQuery whereReviewBook(){
			ReviewBookQuery reviewBookQuery= new ReviewBookQuery(this.bookStoreReviewQuery,ReviewSchema.BOOK);
			reviewBookQuery.setAttribute(this);
			return reviewBookQuery;
		}
		
		public ReviewSiteUserQuery whereReviewCustomer(){
			ReviewSiteUserQuery reviewCustomerQuery= new ReviewSiteUserQuery(this.bookStoreReviewQuery,PurchaseOrderSchema.ID);
			reviewCustomerQuery.setAttribute(this);
			return reviewCustomerQuery;
		}
	}
}