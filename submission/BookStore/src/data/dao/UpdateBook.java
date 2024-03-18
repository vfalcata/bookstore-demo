package data.dao;


import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.dao.exceptions.UpdateDBFailureException;
import data.schema.BookSchema;

public class UpdateBook extends DataUpdate{
	
	public BookUpdater requestUpdateBook(Book book) {
		return new BookUpdater(book);
	}
	
	
	public InsertBookTitle requestNewBookInsertion() {
		return new InsertBookTitle(new Book.Builder().build());
	}

	public class InsertBookTitle extends BookInsert{
		private InsertBookTitle(Book book){
			super(book);
		}
		
		public InsertBookSeries insertBookWithTitle(String title){			
			return new InsertBookSeries(new Book.Builder(book).withTitle(surroundWithQuotes(title)).build());
		}
	}
	

	public class InsertBookSeries extends BookInsert{
		private InsertBookSeries(Book book){
			super(book);
		}
		
		public InsertBookCategory insertBookWithSeries(String series){
			return new InsertBookCategory(new Book.Builder(book).withSeries(surroundWithQuotes(series)).build());
		}
	}
	
	public class InsertBookCategory extends BookInsert {
		private InsertBookCategory(Book book) {
			super(book);
		}

		public InsertBookAuthor insertBookWithCategory(String category){
			return new InsertBookAuthor(new Book.Builder(book).withCategory(surroundWithQuotes(category)).build());
		}
	}
	
	public class InsertBookAuthor extends BookInsert{
		private InsertBookAuthor(Book book) {
			super(book);
		}

		public InsertBookDescription insertBookWithAuthor(String author){
			return new InsertBookDescription(new Book.Builder(book).withAuthor(surroundWithQuotes(author)).build());
		}
	}
	
	public class InsertBookDescription extends BookInsert{
		private InsertBookDescription(Book book){
			super(book);
		}
		
		public InsertBookPublishYear insertBookWithDescription(String description){
			return new InsertBookPublishYear(new Book.Builder(book).withDescription(surroundWithQuotes(description)).build());
		}
	}
	
	public class InsertBookPublishYear extends BookInsert{
		private InsertBookPublishYear(Book book){
			super(book);
		}
		
		public InsertBookCover insertBookWithPublishYear(int publishYear){
			
			return new InsertBookCover(new Book.Builder(book).withPublishYear(publishYear).build());
		}
	}
	
	
	
	public class InsertBookCover extends BookInsert{
		private InsertBookCover(Book book) {
			super(book);
		}

		public InsertBookPrice insertBookWithCover(File cover){
			File bookCover = cover==null?new File(""):cover;
			return new InsertBookPrice(new Book.Builder(book).withCover(bookCover).build());

		}
		
		public InsertBookPrice insertBookWithCover(String cover){
			File bookCover = cover==null||cover.isEmpty()?new File(""):new File(cover);
			return new InsertBookPrice(new Book.Builder(book).withCover(bookCover).build());e
		}
	}
	

	
	
	public class InsertBookPrice extends BookInsert{
		private InsertBookPrice(Book book) {
			super(book);
		}

		public InsertBookISBN insertBookWithPrice(double price){
			return new InsertBookISBN(new Book.Builder(book).withPrice(price).build());

		}
	}
	
	public class InsertBookISBN extends BookInsert{
		private InsertBookISBN(Book book) {
			super(book);
		}

		public ExecuteBookInsertion insertBookWithISBN(String ISBN){
			return new ExecuteBookInsertion(new Book.Builder(book).withISBN(surroundWithQuotes(ISBN)).build());

		}
	}
	
	public class ExecuteBookInsertion extends BookInsert{
		private ExecuteBookInsertion(Book book) {
			super(book);
		}

		public Book executeBookInsertion() {
			String idInput=book.getISBN()+book.getPublishYear();
			String id =UUID.nameUUIDFromBytes(idInput.getBytes()).toString();
			String update ="INSERT INTO BOOK (ID,TITLE ,SERIES ,DESCRIPTION ,CATEGORY,AUTHOR,COVER,ISBN ,PUBLISH_YEAR,PRICE)	VALUES 	"+
					"("+id+","+book.getTitle()+","+book.getSeries()+","+book.getDescription()+","+book.getCategory()+","+book.getAuthor()+","+book.getCover().getName()+","+book.getISBN()+","+Integer.toString(book.getPublishYear())+","+Double.toString(book.getPrice())+")";
			sendUpdateToDatabase(update);
			
			String check="SELECT COUNT(*) AS BOOK_COUNT FROM BOOK WHERE ID='"+id+"'";

			return new Book.Builder(book).withId(new Id(id)).build();
		}
	}
	
	abstract class BookInsert extends DataUpdate{
		Book book;
		BookInsert(Book book){
			this.book=book;
		}
	}
	
	public class BookUpdater extends DataUpdate{
		Map<String,String> updateRequest;
		private Book book;
		private BookSchema bookSchema = new BookSchema();
		private BookUpdater(Book book){
			this.updateRequest=new LinkedHashMap<String, String>();
			this.book=book;
		}
		public BookUpdater updateBookTitle(String title){
			this.updateRequest.put(bookSchema.TITLE, surroundWithQuotes(title));
			book= new Book.Builder(book).withTitle(title).build();
			return this;
			
		}
		
		public BookUpdater updateBookSeries(String series){
			this.updateRequest.put(bookSchema.SERIES, surroundWithQuotes(series));
			book= new Book.Builder(book).withSeries(series).build();
			return this;
		}
		
		public BookUpdater updateBookCategory(String category){
			this.updateRequest.put(bookSchema.CATEGORY, surroundWithQuotes(category));
			book= new Book.Builder(book).withCategory(category).build();
			return this;
		}
		
		
		public BookUpdater updateBookDescription(String description){
			this.updateRequest.put(bookSchema.DESCRIPTION, surroundWithQuotes(description));
			book= new Book.Builder(book).withDescription(description).build();
			return this;
		}
		
		public BookUpdater updateBookAuthor(String author){
			this.updateRequest.put(bookSchema.AUTHOR, surroundWithQuotes(author));
			book= new Book.Builder(book).withAuthor(author).build();
			return this;
		}
		
		public BookUpdater updateBookCover(File cover){
			this.updateRequest.put(bookSchema.COVER, surroundWithQuotes(cover.getName()));
			book= new Book.Builder(book).withCover(cover).build();
			return this;
		}
		
		public BookUpdater updateBookISBN(String ISBN){
			this.updateRequest.put(bookSchema.ISBN, surroundWithQuotes(ISBN));
			book= new Book.Builder(book).withISBN(ISBN).build();
			return this;
		}
		
		
		public BookUpdater updateBookPrice(double price){
			this.updateRequest.put(bookSchema.PRICE, Double.toString(price));
			book= new Book.Builder(book).withPrice(price).build();
			return this;
		}
		
		public BookUpdater updateBookPublishYear(int publishYear){
			this.updateRequest.put(bookSchema.PUBLISH_YEAR, Integer.toString(publishYear));
			book= new Book.Builder(book).withPublishYear(publishYear).build();
			return this;
		}
		
		public Book executeUpdate()  {
			String update = "UPDATE BOOK SET ";
			String check=" SELECT COUNT(*) AS BOOK_COUNT FROM BOOK WHERE ";
			String and=" AND ";
			for(Entry<String,String> entry:this.updateRequest.entrySet()) {
				update+=entry.getKey()+"="+entry.getValue()+",";
				if(!entry.getValue().equals("NULL")) {
					check+=entry.getKey()+"="+entry.getValue() + and;	
				}
			}
			update=update.substring(0,update.length()-1);
			update+=" WHERE ID='"+book.getId().toString()+"'";
			sendUpdateToDatabase(update);
			check=check+" ID='"+book.getId().toString()+"'";
 
			return book;
		}
	}

}
