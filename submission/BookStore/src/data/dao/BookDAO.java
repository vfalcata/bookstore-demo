package data.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import data.query.AttributeAccess;
import data.query.BookStoreNumberQuery;
import data.query.BookStoreQuery;
import data.query.BookStoreVarCharQuery;
import data.query.DataAccessString;
import data.query.PageRequestMetaData;
import data.query.Query;
import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.beans.Visitor;
import data.dao.CartDAO.BookStoreCartQuery;
import data.dao.CartDAO.CartAttributeAccess;
import data.dao.CustomerDAO.BookStoreCustomerQuery;
import data.dao.ReviewDAO.BookStoreReviewQuery;
import data.dao.VisitorDAO.BookStoreVisitorQuery;
import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.DataSchema;
import data.schema.PurchaseOrderSchema;
import data.schema.ReviewSchema;
import data.schema.UserTypes;
import data.schema.VisitorSchema;

public class BookDAO implements DAO{

	private BookSchema bookSchema;
	public BookDAO() {
		this.bookSchema=new BookSchema();
	}
	
	@Override
	public UpdateBook newUpdateRequest() {
		return new UpdateBook();
	}

	@Override
	public BookStoreBookQuery newQueryRequest() {
		BookStoreBookQuery bookStoreQuery= new BookStoreBookQuery(bookSchema);
		BookAttributeAccess bookAttributeAccess= new BookAttributeAccess();
		bookStoreQuery.setAttribute(bookAttributeAccess);
		bookAttributeAccess.setBookQuery(bookStoreQuery);
		return bookStoreQuery;
	}
	
	public int getNumberBooks() {
		int total=0;
		for(Entry<String,Integer> entry: getCountPerCategory().entrySet()) {
			total=total+entry.getValue();
		}
		return total;
	}
	
	
	
	public Map<String, Integer> getCountPerCategory(){
		String queryString="SELECT COUNT(CATEGORY) AS CATEGORY_COUNT, CATEGORY FROM BOOK GROUP BY CATEGORY";
		Map<String, Integer> results= new LinkedHashMap<String, Integer>();
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			DataSource dataSource=(DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			connection= dataSource.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			resultSet= preparedStatement.executeQuery();
			while(resultSet.next()) {				
				results.put(resultSet.getString("CATEGORY"), resultSet.getInt("CATEGORY_COUNT"));
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

	public class BookStoreBookQuery extends BookStoreQuery<BookStoreBookQuery,BookAttributeAccess>{

		public BookStoreBookQuery(BookStoreBookQuery query, DataSchema dataSchema) {
			super(query, dataSchema);
		}
		
		public BookStoreBookQuery(DataSchema dataSchema) {
			super(dataSchema);
		}
		


		public BookStoreBookQuery includeBookTitleInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.TITLE);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookDescriptionInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.DESCRIPTION);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookCategoryInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.CATEGORY);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookAuthorInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.AUTHOR);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookCoverInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.COVER);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookPublishYearInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.PUBLISH_YEAR);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookRatingInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.RATING);
			includeKeyInResults();
			return this;
		}

		public BookStoreBookQuery includeBookPriceInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.PRICE);
			includeKeyInResults();
			return this;
		}
		public BookStoreBookQuery includeBookAmountSoldInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(), new HashSet<String>());
			this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.AMOUNT_SOLD);
			includeKeyInResults();
			return this;
		}

		public BookStoreBookQuery excludeBookTitleInResult() {
			if(this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.TITLE);
			return this;
		}
		
		public BookStoreBookQuery excludeBookDescriptionInResult() {
			if(this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.DESCRIPTION);		
			return this;
		}
		public BookStoreBookQuery excludeBookCategoryInResult() {
			if(this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.CATEGORY);
			return this;
		}
		public BookStoreBookQuery excludeBookAuthorInResult() {
			if(this.attributesToIncludInResults.containsKey(bookSchema.tableName()))this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.AUTHOR);
			return this;
		}
		public BookStoreBookQuery excludeBookCoverInResult() {
			if(this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.COVER);
			return this;
		}
		public BookStoreBookQuery excludeBookPublishYearInResult() {
			if(this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.PUBLISH_YEAR);
			return this;
		}
		public BookStoreBookQuery excludeBookRatingInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.RATING);
			return this;
		}

		public BookStoreBookQuery excludeBookPriceInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.PRICE);
			return this;
		}
		public BookStoreBookQuery excludeBookAmountSoldInResult() {
			if(!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.get(bookSchema.tableName()).remove(bookSchema.TITLE);
			return this;
		}
		
		
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.BOOK)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
		private void includeKeyInResults() {
			if(this.attributesToIncludInResults.get(bookSchema.tableName())==null||!this.attributesToIncludInResults.containsKey(bookSchema.tableName())) this.attributesToIncludInResults.put(bookSchema.tableName(),new LinkedHashSet<String>());
			if(!this.attributesToIncludInResults.get(bookSchema.tableName()).isEmpty() || !this.attributesToIncludInResults.get(bookSchema.tableName()).contains(bookSchema.ID)) {
				this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.ID);	
				this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.ISBN);
				this.attributesToIncludInResults.get(bookSchema.tableName()).add(bookSchema.PRICE);
			}
		}
	}
	
	
	public class BookNumberQuery extends BookStoreNumberQuery<BookNumberQuery,BookAttributeAccess,BookStoreBookQuery>{
		private BookAttributeAccess bookAttributeAccess;
		
		BookNumberQuery(BookStoreBookQuery bookStoreQuery,String currentAttributeAccess) {
			super(bookStoreQuery, new BookSchema());
			this.currentAttributeAccess=currentAttributeAccess;
		}
		
		BookNumberQuery(BookStoreBookQuery bookStoreQuery,String currentAttributeAccess, PageRequestMetaData pageRequestMetaData) {
			super(bookStoreQuery, new BookSchema());
			this.pageRequestMetaData=pageRequestMetaData;
			this.currentAttributeAccess=currentAttributeAccess;
		}
		
		public BookAttributeAccess queryBookAttribute() {		
			return bookAttributeAccess;
		}
		
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.BOOK)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);
		}
		
	}
	

	public class BookVarCharQuery extends BookStoreVarCharQuery<BookVarCharQuery,BookAttributeAccess,BookStoreBookQuery>{
		private BookAttributeAccess bookAttributeAccess;
		private BookDAO bookDAO;
		
		BookVarCharQuery(BookStoreBookQuery bookStoreQuery, String currentAttributeAccess) {
			super(bookStoreQuery, new BookSchema());
			this.currentAttributeAccess=currentAttributeAccess;

		}
		
		BookVarCharQuery(BookStoreBookQuery bookStoreQuery, String currentAttributeAccess, PageRequestMetaData pageRequestMetaData) {
			super(bookStoreQuery, new BookSchema());
			this.pageRequestMetaData=pageRequestMetaData;
			this.currentAttributeAccess=currentAttributeAccess;

		}
		
		public BookAttributeAccess queryBookAttribute() {		
			return bookAttributeAccess;
		}
		
		
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.BOOK)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);


		}
		
	}
	

	public class BookKeyQuery extends BookStoreQuery<BookKeyQuery,BookAttributeAccess>{

		private BookAttributeAccess bookAttributeAccess;
		private BookDAO bookDAO;	

		BookKeyQuery(BookStoreBookQuery bookStoreQuery, String currentAttributeAccess){
			super(bookStoreQuery,new BookSchema());
		}
		BookKeyQuery(BookStoreBookQuery bookStoreQuery, String currentAttributeAccess, PageRequestMetaData pageRequestMetaData){
			super(bookStoreQuery,new BookSchema());
		}
		
		public BookAttributeAccess queryBookAttribute() {		
			return bookAttributeAccess;
		}
		public BookStoreReviewQuery queryReview() {
			this.tableJoins.add(
					new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.ID)
					.withDataAccessParameterPrefix("=")
					.withDataAccessParameterSuffix("")
					.withDataAccessParameter(new ReviewSchema().tableName()+this.referenceOperator+ReviewSchema.BOOK)
					.build()
					);
			return new ReviewDAO().newQueryRequest().setAttributesToIncludInResults(attributesToIncludInResults).setDataAccessRequestsConjunction(this.dataAccessRequestsConjunction).setDataAccessRequestsDisjunction(this.dataAccessRequestsDisjunction).setPageRequestMetaData(pageRequestMetaData).addTableJoins(tableJoins);

		}
		
		
		public BookKeyQuery isBook(Book book) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(book.getId().toString())
					.build()
					);
			return  this;
		}
		
		public BookKeyQuery isBook(String idString) {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.ID)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(idString)
					.build()
					);
			return  this;
		}
	}
	
	public class BookCategoryQuery extends BookVarCharQuery{
		private BookAttributeAccess bookAttributeAccess;
		private BookDAO bookDAO;
		BookCategoryQuery(BookStoreBookQuery bookStoreQuery, String currentAttributeAccess) {
			super(bookStoreQuery, currentAttributeAccess);
		}
		BookCategoryQuery(BookStoreBookQuery bookStoreQuery, String currentAttributeAccess,
				PageRequestMetaData pageRequestMetaData) {
			super(bookStoreQuery, currentAttributeAccess, pageRequestMetaData);
		}
		
		public BookCategoryQuery isBookCategoryArtists() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_ARTISTS)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryBusiness() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_BUSINESS)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryComics() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_COMICS)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryAdventure() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_ADVENTURE)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryDystopia() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_DYSTOPIA)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryEntertainment() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_ENTERTAINMENT)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryHistorical() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_HISTORICAL)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryHorror() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_HORROR)
					.build()
					);
			return this;
		}
		public BookCategoryQuery isBookCategoryRomance() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_ROMANCE)
					.build()
					);
			return this;
		}
		
		public BookCategoryQuery isBookCategorySciFi() {
			this.addDataAccessString(new DataAccessString.Builder()
					.withTableName(this.dataSchema.tableName())
					.withReferenceOperator(this.referenceOperator)
					.withAttributeName(BookSchema.CATEGORY)
					.withDataAccessParameterPrefix("="+"'")
					.withDataAccessParameterSuffix("'")
					.withDataAccessParameter(BookSchema.BOOK_CATEGORY_SCIENCE_FICTION)
					.build()
					);
			return this;
		}
		
	}
	
	public class BookAttributeAccess extends AttributeAccess<BookStoreBookQuery>{

		protected BookStoreBookQuery bookStoreBookQuery;
		protected BookAttributeAccess() {
			
		}
		
		protected void setBookQuery(BookStoreBookQuery bookStoreQuery) {			
			this.bookStoreBookQuery=bookStoreQuery;
		}
		
		public BookKeyQuery whereBook() {
			BookKeyQuery bookKeyQuery= new BookKeyQuery(this.bookStoreBookQuery,BookSchema.ID);
			bookKeyQuery.setAttribute(this);
			return bookKeyQuery;
		}
		
		public BookVarCharQuery whereBookTitle() {
			BookVarCharQuery bookVarCharQuery= new BookVarCharQuery(this.bookStoreBookQuery,BookSchema.TITLE);
			bookVarCharQuery.setAttribute(this);
			return bookVarCharQuery;
		}
		
		public BookVarCharQuery whereBookDescription() {
			BookVarCharQuery bookVarCharQuery= new BookVarCharQuery(this.bookStoreBookQuery,BookSchema.DESCRIPTION);
			bookVarCharQuery.setAttribute(this);
			return bookVarCharQuery;		
		}
		

		
		public BookCategoryQuery whereBookCategory() {
			BookCategoryQuery bookCategoryQuery= new BookCategoryQuery(this.bookStoreBookQuery,BookSchema.CATEGORY,this.bookStoreBookQuery.getPageData());
			bookCategoryQuery.setAttribute(this);
			return bookCategoryQuery;			
		}
		
		public BookVarCharQuery whereBookAuthor() {
			BookVarCharQuery bookVarCharQuery= new BookVarCharQuery(this.bookStoreBookQuery,BookSchema.AUTHOR);
			bookVarCharQuery.setAttribute(this);
			return bookVarCharQuery;
			
		}
		
		public BookVarCharQuery whereBookISBN() {
			BookVarCharQuery bookVarCharQuery= new BookVarCharQuery(this.bookStoreBookQuery,BookSchema.ISBN,this.bookStoreBookQuery.getPageData());
			bookVarCharQuery.setAttribute(this);
			return bookVarCharQuery;
		}
		
		public BookNumberQuery whereBookPublishYear() {
			BookNumberQuery bookNumberQuery= new BookNumberQuery(this.bookStoreBookQuery,BookSchema.PUBLISH_YEAR);
			bookNumberQuery.setAttribute(this);
			return bookNumberQuery;			
		}
		
		public BookNumberQuery whereBookRating() {
			BookNumberQuery bookNumberQuery= new BookNumberQuery(this.bookStoreBookQuery,BookSchema.RATING);
			bookNumberQuery.setAttribute(this);
			return bookNumberQuery;
		}
		
		public BookNumberQuery whereBookPrice() {
			BookNumberQuery bookNumberQuery= new BookNumberQuery(this.bookStoreBookQuery,BookSchema.PRICE);
			bookNumberQuery.setAttribute(this);
			return bookNumberQuery;
		}
		
		public BookNumberQuery whereBookAmountSold() {
			BookNumberQuery bookNumberQuery= new BookNumberQuery(this.bookStoreBookQuery,BookSchema.AMOUNT_SOLD);
			bookNumberQuery.setAttribute(this);
			return bookNumberQuery;
		}		
	}
}
