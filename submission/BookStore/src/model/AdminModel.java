package model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.beans.Book;
import data.beans.Customer;
import data.dao.BookDAO;
import data.dao.CustomerDAO;
import data.dao.PurchaseOrderDAO;

public class AdminModel {
	private static AdminModel instance;
	private BookDAO book;
	private PurchaseOrderDAO orders;
	private CustomerDAO customers;
	
	public AdminModel() {
		
	}
	
	public static AdminModel getInstance() {
		if (instance == null) {
			instance = new AdminModel();
			instance.book = new BookDAO();
			instance.orders = new PurchaseOrderDAO();
			instance.customers = new CustomerDAO();
		}
		return instance;
	}
	
	public LocalDateTime oldestSale() {		
		Long dateLong = orders.getOldestOrder();
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.systemDefault());
		System.out.println(dateLong);
		System.out.println(date);

		return date;
	}
	
	public LocalDateTime latestSale() {
		Long dateLong = orders.getLatestOrder();
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.systemDefault());
		System.out.println(dateLong);
		System.out.println(date);
		return date;
	}
	
	public Map<String, Map<Book, Integer>> listOfBooksSold(LocalDateTime start, LocalDateTime end) {
		LocalDateTime currMonth = start;
		Map<String, Map<Book, Integer>> report = new LinkedHashMap<String, Map<Book, Integer>>();
		Map<Book, Integer> currMonthBooks = new LinkedHashMap<Book, Integer>();
		Map<String, Integer> currMonthBooksID;
		List<Book> b = null;
		do {
			System.out.println(currMonth);
			currMonthBooksID = listOfBooksSoldMonth(currMonth);
			String period = currMonth.getMonth().toString() + " " + currMonth.getYear();
			for (Map.Entry<String, Integer> entry : currMonthBooksID.entrySet()) {
				b =  this.book.newQueryRequest()
						 .includeAllAttributesInResultFromSchema()
						 .queryAttribute()
						 .whereBook()
						 .isBook(entry.getKey())
						 .executeQuery()
						 .executeCompilation()
						 .compileBooks();
				
				if (b.size() == 0)
					System.out.println("Book with ID: \'" + entry.getKey() + "\' Not Found");
				else if (b.size() > 1)
					System.out.println("Retrieved multiple books with given ISBN: \'" + entry.getKey() + "\' please providd this output to site admin: info@bookstore.ca");
				else {
					currMonthBooks.put(b.get(0), entry.getValue());
				}
				
			}
			
			report.put(period, new LinkedHashMap<Book, Integer>(currMonthBooks));
			currMonth = currMonth.with(TemporalAdjusters.firstDayOfNextMonth());
		} while (currMonth.isBefore(end) || currMonth.isEqual(end));

		return report;
		
	}
	
	private Map<String, Integer> listOfBooksSoldMonth(LocalDateTime month) {
		
		Map<String, Integer> bookOrders = new LinkedHashMap<String, Integer>();
		
		LocalDateTime startOfMonth = month;
		LocalDateTime endOfMonth = month.with(TemporalAdjusters.lastDayOfMonth());
		endOfMonth = LocalDateTime.of(endOfMonth.toLocalDate(), LocalTime.MAX);
		long startOfMonthEpoch = startOfMonth.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long endOfMonthEpoch = endOfMonth.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
		System.out.println(startOfMonthEpoch + " " + endOfMonthEpoch);
		
		bookOrders = orders.getOrdersInDateRange(startOfMonthEpoch, endOfMonthEpoch);
		
		System.out.println(startOfMonth + " -> " + endOfMonth);	
		
		return bookOrders;
		
	}
	
	public Map<Book, Integer> getTop10BooksAlltime() {
		
		Map<Book, Integer> report = new LinkedHashMap<Book, Integer>();
		Map<String, Integer> books = orders.getTopBooks(orders.getOldestOrder());
		
		List<Book> b = null;
		
		for (Entry<String, Integer> entry : books.entrySet()) {
			b =  this.book.newQueryRequest()
					 .includeAllAttributesInResultFromSchema()
					 .queryAttribute()
					 .whereBook()
					 .isBook(entry.getKey())
					 .executeQuery()
					 .executeCompilation()
					 .compileBooks();
			
			if (b.size() == 0)
				System.out.println("Book with ID: \'" + entry.getKey() + "\' Not Found");
			else if (b.size() > 1)
				System.out.println("Retrieved multiple books with given ISBN: \'" + entry.getKey() + "\' please providd this output to site admin: info@bookstore.ca");
			else {
				report.put(b.get(0), entry.getValue());
			}
		}
		
		return report;
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Double> buyingStats() {
		Map<String, Map<String, Integer>> orderList = orders.getCustomerBookOrderCount();
		Map<String, Double> result = new LinkedHashMap<String, Double>();
		
		List<Customer> c = null;
		List<Book> b = null;
		
		Double totalSpent = 0.0;
		
		for (Entry<String, Map<String, Integer>> orderEntry : orderList.entrySet()) {
			System.out.println(orderEntry.getKey());
			for (Entry<String, Integer> bookEntry : orderEntry.getValue().entrySet()) {
				b =  this.book.newQueryRequest()
						 .includeAllAttributesInResultFromSchema()
						 .queryAttribute()
						 .whereBook()
						 .isBook(bookEntry.getKey())
						 .executeQuery()
						 .executeCompilation()
						 .compileBooks();
				
				if (b.size() == 0)
					System.out.println("Book with ID: \'" + bookEntry.getKey() + "\' Not Found");
				else if (b.size() > 1)
					System.out.println("Retrieved multiple books with given ISBN: \'" + bookEntry.getKey() + "\' please providd this output to site admin: info@bookstore.ca");
				else {
					totalSpent += b.get(0).getPrice() * bookEntry.getValue();
					
				}
			}
			
			result.put(orderEntry.getKey(), totalSpent);
			totalSpent = 0.0;
		}
		
		for (Entry<String, Double> bookEntry : result.entrySet()) {
			System.out.println("\t" + bookEntry.getKey() + " " + bookEntry.getValue());
		}		
		return result;		
	}	
}
