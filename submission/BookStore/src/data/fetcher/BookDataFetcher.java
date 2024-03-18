package data.fetcher;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import data.beans.Book;
import data.beans.Id;
import data.beans.Review;
import data.dao.ReviewDAO;
import data.query.Query;
import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.DataSchema;
import data.schema.ReviewSchema;

public class BookDataFetcher extends DataFetcher<Book>{

	private  BookSchema schema=new BookSchema();

	public BookDataFetcher(Map<String, Set<String>> attributesToIncludInResults) {
		super(attributesToIncludInResults);
	}
	
	protected Entry<Id,Book> resultSetFromIdToBook(ResultSet resultSet){
		Book book=resultSetToBean(resultSet);
		return new AbstractMap.SimpleEntry<Id,Book>(book.getId(), book);
	}

	@Override
	public Book resultSetToBean(ResultSet resultSet) {
		boolean isRequestAllAttributes=this.attributesToIncludInResults.get(schema.tableName()).isEmpty();
		String prefix = isReferenceQuery()?schema.tableName()+Query.referenceSeparator:"";
		Book book = new Book.Builder().build();		
				
		try {

			book = new Book.Builder().withId(new Id(resultSet.getString(prefix+BookSchema.ID))).withISBN(resultSet.getString(prefix+BookSchema.ISBN)).build();		
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.AUTHOR)) {
				book = new Book.Builder(book).withAuthor(resultSet.getString(prefix+schema.AUTHOR)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CATEGORY)) {
				book = new Book.Builder(book).withCategory(resultSet.getString(prefix+schema.CATEGORY)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.COVER)) {
				book = new Book.Builder(book).withCover(new File(resultSet.getString(prefix+schema.COVER))).build();
			}
			
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.DESCRIPTION)) {
				book = new Book.Builder(book).withDescription(resultSet.getString(prefix+schema.DESCRIPTION)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.SERIES)) {
				book = new Book.Builder(book).withSeries(resultSet.getString(prefix+schema.SERIES)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.TITLE)) {
				book = new Book.Builder(book).withTitle(resultSet.getString(prefix+schema.TITLE)).build();
			}
			
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.PRICE)) {
				book = new Book.Builder(book).withPrice(resultSet.getDouble(prefix+schema.PRICE)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.AMOUNT_SOLD)) {
				book = new Book.Builder(book).withAmountSold(resultSet.getInt(prefix+schema.AMOUNT_SOLD)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.PUBLISH_YEAR)) {
				book = new Book.Builder(book).withPublishYear(resultSet.getInt(prefix+schema.PUBLISH_YEAR)).build();
			}
			
			if(isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.RATING)) {
				book = new Book.Builder(book).withRating(resultSet.getDouble(prefix+schema.RATING)).build();
			}					
			
			return book;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.err.println("Warning empty book, since resultSet could not produce book object");
		return book;
		
	}
	


	



}


