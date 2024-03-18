package dao;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.beans.Visitor;
import data.dao.BookDAO;
import data.dao.CartDAO;
import data.dao.CustomerDAO;
import data.dao.PurchaseOrderDAO;
import data.dao.ReviewDAO;
import data.dao.VisitorDAO;

public class BookStoreDAOTests {

	@Test
	void bookDAOVarCharQueryProducesCorrectQuery() {
		BookDAO bookDAO = new BookDAO();
		String query =bookDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeBookTitleInResult()
				.excludeBookCoverInResult()
				.includeBookTitleInResult()
				.queryAttribute()
				.whereBookDescription()
				.varCharContains("descrip")
				.queryAttribute()
				.whereBookTitle()
				.varCharContains("title")
				.varCharEndsWith("another")
				.queryAttribute()
				.whereBookISBN()
				.varCharContains("isbn")
				.withAscendingOrderOf()
				.queryAttribute()
				.whereBookCategory()
				.varCharStartsWith("cat")
				.varCharEquals("egory")
				.withPageNumber(2)
				.withResultLimit(35)
				.getQueryString();
		String expectedResult="SELECT PUBLISH_YEAR,AMOUNT_SOLD,PRICE,ISBN,DESCRIPTION,RATING,CATEGORY,TITLE,ID,AUTHOR FROM BOOK  WHERE DESCRIPTION like '%descrip%' AND TITLE like '%title%' AND TITLE like '%another' AND ISBN like '%isbn%' AND CATEGORY like 'cat%' AND CATEGORY='egory' ORDER BY ISBN ASC OFFSET 70 ROWS  FETCH NEXT 35 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO varchar contains did not generate the correct query string");

	}
	
	@Test
	void bookDAONumberQueryProducesCorrectQuery() {
		BookDAO bookDAO = new BookDAO();
		String query =bookDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeBookTitleInResult()
				.excludeBookCoverInResult()
				.includeBookTitleInResult()
				.queryAttribute()
				.whereBookPublishYear()
				.numberAtLeast("2000")
				.queryAttribute()
				.whereBookPrice()
				.numberBetween("1", "99")
				.queryAttribute()
				.whereBookAmountSold()
				.numberAtMost("22")
				.withAscendingOrderOf()
				.withPageNumber(3)
				.withResultLimit(36)
				.getQueryString();
		String expectedResult="SELECT PUBLISH_YEAR,AMOUNT_SOLD,PRICE,ISBN,DESCRIPTION,RATING,CATEGORY,TITLE,ID,AUTHOR FROM BOOK  WHERE PUBLISH_YEAR >= 2000 AND PRICE >= 1 AND PRICE <= 99 AND AMOUNT_SOLD <= 22 ORDER BY AMOUNT_SOLD ASC OFFSET 108 ROWS  FETCH NEXT 36 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO number query did not generate the correct query string");
	}
	@Test
	void bookDAOConjunctionDisjunctionProducesCorrectQuery() {
		BookDAO bookDAO = new BookDAO();
		String query =bookDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeBookTitleInResult()
				.excludeBookCoverInResult()
				.includeBookTitleInResult()
				.queryAttribute()
				.whereBookDescription()
				.varCharContains("descrip")
				.queryAttribute()
				.whereBookTitle()
				.varCharContains("title")
				.varCharContains("another")
				.queryAttribute()
				.whereBookISBN()
				.queryAsDisjunction()
				.varCharContains("isbn")
				.withAscendingOrderOf()
				.queryAttribute()
				.whereBookCategory()
				.queryAsDisjunction()
				.varCharContains("cat")
				.varCharContains("egory")
				.withPageNumber(2)
				.withResultLimit(35)
				.getQueryString();
		String expectedResult="SELECT PUBLISH_YEAR,AMOUNT_SOLD,PRICE,ISBN,DESCRIPTION,RATING,CATEGORY,TITLE,ID,AUTHOR FROM BOOK  WHERE (DESCRIPTION like '%descrip%' AND TITLE like '%title%' AND TITLE like '%another%') AND (ISBN like '%isbn%' OR CATEGORY like '%cat%' OR CATEGORY like '%egory%') ORDER BY ISBN ASC OFFSET 70 ROWS  FETCH NEXT 35 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO varchar contains did not generate the correct query string");

	}

	@Test
	void customerDAOVarCharQueryProducesCorrectQuery() {
		CustomerDAO customerDAO = new CustomerDAO();
		String query =customerDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeCustomerPasswordInResult()
				.queryAttribute()
				.whereCustomerEmail()
				.varCharContains("email")
				.queryAttribute()
				.whereCustomerGivenName()
				.varCharContains("name con")
				.varCharEndsWith("anem end")
				.queryAttribute()
				.whereCustomerPostalCode()
				.varCharContains("postal")
				.withAscendingOrderOf()
				.queryAttribute()
				.whereCustomerStreet()
				.varCharStartsWith("street")
				.varCharEquals("street=")
				.withPageNumber(2)
				.withResultLimit(35)
				.getQueryString();
		String expectedResult="SELECT SURNAME,CREDIT_CARD_NUMBER,STREET_NUMBER,CREATED_AT_EPOCH,STREET,EMAIL,CREDIT_CARD,CREDIT_CARD_CVV2,CITY,COUNTRY,POSTAL_CODE,USERNAME,PROVINCE,ID,CREDIT_CARD_EXPIRY,GIVENNAME FROM CUSTOMER  WHERE EMAIL like '%email%' AND GIVENNAME like '%name con%' AND GIVENNAME like '%anem end' AND POSTAL_CODE like '%postal%' AND STREET like 'street%' AND STREET='street=' ORDER BY POSTAL_CODE ASC OFFSET 70 ROWS  FETCH NEXT 35 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO varchar contains did not generate the correct query string");

	}
	

	@Test
	void reviewDAOVarCharQueryProducesCorrectQuery() {
		ReviewDAO reviewDAO = new ReviewDAO();
		String query =reviewDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeReviewBodyInResult()
				.queryAttribute()
				.whereReviewBody()
				.varCharContains("body contains")
				.queryAttribute()
				.whereReviewTitle()
				.varCharContains("title")
				.varCharEndsWith("another")
				.queryAttribute()
				.whereReviewBody()
				.varCharContains("body contains")
				.withAscendingOrderOf()
				.queryAttribute()
				.whereReviewTitle()
				.varCharStartsWith("title")
				.varCharEquals("tt")
				.withPageNumber(2)
				.withResultLimit(43)
				.getQueryString();
		String expectedResult="SELECT RATING,BOOK,CREATED_AT_EPOCH,CUSTOMER,TITLE FROM REVIEW  WHERE BODY like '%body contains%' AND TITLE like '%title%' AND TITLE like '%another' AND BODY like '%body contains%' AND TITLE like 'title%' AND TITLE='tt' ORDER BY BODY ASC OFFSET 86 ROWS  FETCH NEXT 43 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO varchar contains did not generate the correct query string");

	}
	
	@Test
	void reviewDAONumberQueryProducesCorrectQuery() {
		ReviewDAO reviewDAO = new ReviewDAO();
		String query =reviewDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeReviewBodyInResult()
				.queryAttribute()
				.whereReviewRating()
				.numberAtLeast("3")
				.numberAtMost("5")
				.queryAttribute()
				.whereReviewCreatedAtEpoch()
				.numberBetween("10000", "9999999")
				.withAscendingOrderOf()
				.withPageNumber(3)
				.withResultLimit(88)
				.getQueryString();
		String expectedResult="SELECT RATING,BOOK,CREATED_AT_EPOCH,CUSTOMER,TITLE FROM REVIEW  WHERE RATING >= 3 AND RATING <= 5 AND CREATED_AT_EPOCH >= 10000 AND CREATED_AT_EPOCH <= 9999999 ORDER BY CREATED_AT_EPOCH ASC OFFSET 264 ROWS  FETCH NEXT 88 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO number query did not generate the correct query string");
	}

	
	@Test
	void purchaseOrderDAOVarCharQueryProducesCorrectQuery() {
		PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
		String query =purchaseOrderDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludePurchaseOrderAmountInResult()
				.queryAttribute()
				.wherePurchaseOrderCreatedAtEpoch()
				.numberAtLeast("1")
				.numberAtMost("3")
				.queryAttribute()
				.wherePurchaseOrderAmount()
				.numberBetween("5", "100")
				.withAscendingOrderOf()
				.withPageNumber(2)
				.withResultLimit(43)
				.getQueryString();
		String expectedResult="SELECT BOOK,CREATED_AT_EPOCH,ID FROM PURCHASE_ORDER  WHERE CREATED_AT_EPOCH >= 1 AND CREATED_AT_EPOCH <= 3 AND AMOUNT >= 5 AND AMOUNT <= 100 ORDER BY AMOUNT ASC OFFSET 86 ROWS  FETCH NEXT 43 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO varchar contains did not generate the correct query string");

	}

	
	@Test
	void visitorDAOQueryProducesCorrectQuery() {
		VisitorDAO visitorDAO = new VisitorDAO();
		String query =visitorDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.queryAttribute()
				.whereVisitorCreatedAtEpoch()
				.numberAtLeast("3")
				.numberAtMost("5")
				.queryAttribute()
				.whereVisitorLastAccessedAtEpoch()
				.numberBetween("10000", "9999999")
				.withAscendingOrderOf()
				.withPageNumber(2)
				.withResultLimit(73)
				.getQueryString();
		String expectedResult="SELECT ID FROM VISITOR  WHERE CREATED_AT_EPOCH >= 3 AND CREATED_AT_EPOCH <= 5 AND LAST_ACCESSED_AT_EPOCH >= 10000 AND LAST_ACCESSED_AT_EPOCH <= 9999999 ORDER BY LAST_ACCESSED_AT_EPOCH ASC OFFSET 146 ROWS  FETCH NEXT 73 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO number query did not generate the correct query string");
	}
	
	
	@Test
	void bookDAOReferenceEmptyResultIncludeHasCorrectQuery() {
		BookDAO bookDAO = new BookDAO();
		String query =bookDAO.newQueryRequest()
				.queryReview()
				.queryCustomer()
				.getQueryString();
		String expectedResult="SELECT BOOK.ID AS BOOK_ID,BOOK.TITLE AS BOOK_TITLE,BOOK.DESCRIPTION AS BOOK_DESCRIPTION,BOOK.CATEGORY AS BOOK_CATEGORY,BOOK.AUTHOR AS BOOK_AUTHOR,BOOK.PRICE AS BOOK_PRICE,BOOK.COVER AS BOOK_COVER,BOOK.PUBLISH_YEAR AS BOOK_PUBLISH_YEAR,BOOK.ISBN AS BOOK_ISBN,BOOK.RATING AS BOOK_RATING,BOOK.AMOUNT_SOLD AS BOOK_AMOUNT_SOLD,REVIEW.BODY AS REVIEW_BODY,REVIEW.TITLE AS REVIEW_TITLE,REVIEW.CUSTOMER AS REVIEW_CUSTOMER,REVIEW.RATING AS REVIEW_RATING,REVIEW.BOOK AS REVIEW_BOOK,REVIEW.CREATED_AT_EPOCH AS REVIEW_CREATED_AT_EPOCH,CUSTOMER.ID AS CUSTOMER_ID,CUSTOMER.GIVENNAME AS CUSTOMER_GIVENNAME,CUSTOMER.SURNAME AS CUSTOMER_SURNAME,CUSTOMER.EMAIL AS CUSTOMER_EMAIL,CUSTOMER.USERNAME AS CUSTOMER_USERNAME,CUSTOMER.PASSWORD AS CUSTOMER_PASSWORD,CUSTOMER.STREET_NUMBER AS CUSTOMER_STREET_NUMBER,CUSTOMER.CREATED_AT_EPOCH AS CUSTOMER_CREATED_AT_EPOCH,CUSTOMER.STREET AS CUSTOMER_STREET,CUSTOMER.POSTAL_CODE AS CUSTOMER_POSTAL_CODE,CUSTOMER.CITY AS CUSTOMER_CITY,CUSTOMER.PROVINCE AS CUSTOMER_PROVINCE,CUSTOMER.COUNTRY AS CUSTOMER_COUNTRY,CUSTOMER.CREDIT_CARD AS CUSTOMER_CREDIT_CARD,CUSTOMER.CREDIT_CARD_NUMBER AS CUSTOMER_CREDIT_CARD_NUMBER,CUSTOMER.CREDIT_CARD_EXPIRY AS CUSTOMER_CREDIT_CARD_EXPIRY,CUSTOMER.CREDIT_CARD_CVV2 AS CUSTOMER_CREDIT_CARD_CVV2 FROM BOOK,REVIEW,CUSTOMER WHERE BOOK.ID=REVIEW.BOOK AND CUSTOMER.ID=REVIEW.CUSTOMER FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}
	
	@Test
	void referenceResultQueryIsCorrectQuery() {
		BookDAO bookDAO = new BookDAO();
		String query =bookDAO.newQueryRequest()
				.includeAllAttributesInResultFromSchema()
				.excludeBookAmountSoldInResult()
				.queryAttribute()
				.whereBookAmountSold()
				.numberBetween("1", "100")
				.queryAttribute()
				.whereBookAuthor()
				.varCharContains("refauth")
				.queryReview()
				.includeAllAttributesInResultFromSchema()
				.excludeReviewBodyInResult()
				.queryAttribute()
				.whereReviewBody()
				.varCharStartsWith("reviewStart")
				.queryAttribute()
				.whereReviewCreatedAtEpoch()
				.numberBetween("22", "200")
				.withAscendingOrderOf()
				.withPageNumber(2)
				.withResultLimit(93)
				.queryCustomer()
				.includeAllAttributesInResultFromSchema()
				.excludeCustomerPasswordInResult()
				.queryAttribute()
				.whereCustomerGivenName()
				.varCharEndsWith("nameEnd")
				.queryAttribute()
				.whereCustomerStreetNumber()
				.varCharContains("22")
				.getQueryString();
		String expectedResult="SELECT BOOK.COVER AS BOOK_COVER,BOOK.PUBLISH_YEAR AS BOOK_PUBLISH_YEAR,BOOK.AMOUNT_SOLD AS BOOK_AMOUNT_SOLD,BOOK.PRICE AS BOOK_PRICE,BOOK.ISBN AS BOOK_ISBN,BOOK.DESCRIPTION AS BOOK_DESCRIPTION,BOOK.RATING AS BOOK_RATING,BOOK.CATEGORY AS BOOK_CATEGORY,BOOK.TITLE AS BOOK_TITLE,BOOK.ID AS BOOK_ID,BOOK.AUTHOR AS BOOK_AUTHOR,REVIEW.RATING AS REVIEW_RATING,REVIEW.BOOK AS REVIEW_BOOK,REVIEW.CREATED_AT_EPOCH AS REVIEW_CREATED_AT_EPOCH,REVIEW.CUSTOMER AS REVIEW_CUSTOMER,REVIEW.TITLE AS REVIEW_TITLE,CUSTOMER.SURNAME AS CUSTOMER_SURNAME,CUSTOMER.CREDIT_CARD_NUMBER AS CUSTOMER_CREDIT_CARD_NUMBER,CUSTOMER.STREET_NUMBER AS CUSTOMER_STREET_NUMBER,CUSTOMER.CREATED_AT_EPOCH AS CUSTOMER_CREATED_AT_EPOCH,CUSTOMER.STREET AS CUSTOMER_STREET,CUSTOMER.EMAIL AS CUSTOMER_EMAIL,CUSTOMER.CREDIT_CARD AS CUSTOMER_CREDIT_CARD,CUSTOMER.CREDIT_CARD_CVV2 AS CUSTOMER_CREDIT_CARD_CVV2,CUSTOMER.CITY AS CUSTOMER_CITY,CUSTOMER.COUNTRY AS CUSTOMER_COUNTRY,CUSTOMER.POSTAL_CODE AS CUSTOMER_POSTAL_CODE,CUSTOMER.USERNAME AS CUSTOMER_USERNAME,CUSTOMER.PROVINCE AS CUSTOMER_PROVINCE,CUSTOMER.ID AS CUSTOMER_ID,CUSTOMER.CREDIT_CARD_EXPIRY AS CUSTOMER_CREDIT_CARD_EXPIRY,CUSTOMER.GIVENNAME AS CUSTOMER_GIVENNAME FROM BOOK,REVIEW,CUSTOMER WHERE BOOK.ID=REVIEW.BOOK AND CUSTOMER.ID=REVIEW.CUSTOMER AND BOOK.AMOUNT_SOLD >= 1 AND BOOK.AMOUNT_SOLD <= 100 AND BOOK.AUTHOR like '%refauth%' AND REVIEW.BODY like 'reviewStart%' AND REVIEW.CREATED_AT_EPOCH >= 22 AND REVIEW.CREATED_AT_EPOCH <= 200 AND CUSTOMER.GIVENNAME like '%nameEnd' AND CUSTOMER.STREET_NUMBER like '%22%' ORDER BY REVIEW.CREATED_AT_EPOCH ASC OFFSET 186 ROWS  FETCH NEXT 93 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}
	
	@Test
	void bookDAOKeyQuery() {
		BookDAO bookDAO = new BookDAO();
		String query =bookDAO.newQueryRequest()
				.queryAttribute()
				.whereBook()
				.isBook(new Book.Builder().withId(new Id("FAKEID")).build())
				.queryAttribute()
				.whereBookAuthor()
				.varCharContains("asdf")
				.getQueryString();
		String expectedResult="SELECT  * FROM BOOK  WHERE ID='FAKEID' AND AUTHOR like '%asdf%' FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}
	
	@Test
	void visitorDAOKeyQuery() {
		VisitorDAO visitorDAO = new VisitorDAO();
		String query =visitorDAO.newQueryRequest()
				.queryAttribute()
				.whereVisitor()
				.isVisitor(new Visitor.Builder().withId(new Id("FAKEID")).build())
				.queryAttribute()
				.whereVisitorCreatedAtEpoch()
				.numberBetween("22", "33")
				.getQueryString();
		String expectedResult="SELECT  * FROM VISITOR  WHERE ID='FAKEID' AND CREATED_AT_EPOCH >= 22 AND CREATED_AT_EPOCH <= 33 FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}
	
	@Test
	void reviewDAOKeyQuery() {
		ReviewDAO reviewDAO = new ReviewDAO();
		String query =reviewDAO.newQueryRequest()
				.queryAttribute()
				.whereReviewCustomer()
				.isCustomer(new Customer.Builder().withId(new Id("fakecust")).build())
				.queryAttribute()
				.whereReviewBook()
				.isBook(new Book.Builder().withId(new Id("FAKEBOOKID")).build())
				.queryAttribute()
				.whereReviewCustomer()
				.isCustomer(new Customer.Builder().withId(new Id("FAKEID")).build())
				.queryAttribute()
				.whereReviewCreatedAtEpoch()
				.numberBetween("22", "33")
				.queryAttribute()
				.whereReview()
				.isReview(new Review.Builder().withCustomer(new Customer.Builder().withId(new Id("fakecustrebv")).build()).withBook(new Book.Builder().withId(new Id("FAKEBOOKIDREB")).build()).build())
				.getQueryString();
		String expectedResult="SELECT  * FROM REVIEW  WHERE CUSTOMER='fakecust' AND BOOK='FAKEBOOKID' AND CUSTOMER='FAKEID' AND CREATED_AT_EPOCH >= 22 AND CREATED_AT_EPOCH <= 33 AND CUSTOMER='fakecustrebv' AND BOOK='FAKEBOOKIDREB' FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}
	
	
	@Test
	void customerDAOFullQuery() {
		CustomerDAO customerDAO = new CustomerDAO();
		String query =customerDAO.newQueryRequest()
				.queryAttribute()
				.whereCustomerCreatedAtEpoch()
				.numberAtLeast("1")
				.getQueryString();
		String expectedResult="SELECT PURCHASE_ORDER.ID AS PURCHASE_ORDER_ID,PURCHASE_ORDER.BOOK AS PURCHASE_ORDER_BOOK,PURCHASE_ORDER.AMOUNT AS PURCHASE_ORDER_AMOUNT,PURCHASE_ORDER.CREATED_AT_EPOCH AS PURCHASE_ORDER_CREATED_AT_EPOCH,CUSTOMER.ID AS CUSTOMER_ID,CUSTOMER.GIVENNAME AS CUSTOMER_GIVENNAME,CUSTOMER.SURNAME AS CUSTOMER_SURNAME,CUSTOMER.EMAIL AS CUSTOMER_EMAIL,CUSTOMER.USERNAME AS CUSTOMER_USERNAME,CUSTOMER.PASSWORD AS CUSTOMER_PASSWORD,CUSTOMER.STREET_NUMBER AS CUSTOMER_STREET_NUMBER,CUSTOMER.CREATED_AT_EPOCH AS CUSTOMER_CREATED_AT_EPOCH,CUSTOMER.STREET AS CUSTOMER_STREET,CUSTOMER.POSTAL_CODE AS CUSTOMER_POSTAL_CODE,CUSTOMER.CITY AS CUSTOMER_CITY,CUSTOMER.PROVINCE AS CUSTOMER_PROVINCE,CUSTOMER.COUNTRY AS CUSTOMER_COUNTRY,CUSTOMER.CREDIT_CARD AS CUSTOMER_CREDIT_CARD,CUSTOMER.CREDIT_CARD_NUMBER AS CUSTOMER_CREDIT_CARD_NUMBER,CUSTOMER.CREDIT_CARD_EXPIRY AS CUSTOMER_CREDIT_CARD_EXPIRY,CUSTOMER.CREDIT_CARD_CVV2 AS CUSTOMER_CREDIT_CARD_CVV2,REVIEW.BODY AS REVIEW_BODY,REVIEW.TITLE AS REVIEW_TITLE,REVIEW.CUSTOMER AS REVIEW_CUSTOMER,REVIEW.RATING AS REVIEW_RATING,REVIEW.BOOK AS REVIEW_BOOK,REVIEW.CREATED_AT_EPOCH AS REVIEW_CREATED_AT_EPOCH,CART.ID AS CART_ID,CART.BOOK AS CART_BOOK,CART.AMOUNT AS CART_AMOUNT,CART.USER_TYPE AS CART_USER_TYPE FROM PURCHASE_ORDER,CUSTOMER,REVIEW,CART WHERE CUSTOMER.ID=PURCHASE_ORDER.ID AND CUSTOMER.ID=REVIEW.CUSTOMER AND CUSTOMER.ID=CART.ID AND CUSTOMER.CREATED_AT_EPOCH >= 1 FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}
	
	@Test
	void customerDAOKeyQuery() {
		CustomerDAO customerDAO = new CustomerDAO();
		String query =customerDAO.newQueryRequest()
				.queryAttribute()
				.whereCustomer()
				.isCustomer(new Customer.Builder().withId(new Id("fakecustrebv")).build())
				.queryAttribute()
				.whereCustomerCreatedAtEpoch()
				.numberBetween("444", "5555")
				.getQueryString();
		String expectedResult="SELECT  * FROM CUSTOMER  WHERE ID='fakecustrebv' AND CREATED_AT_EPOCH >= 444 AND CREATED_AT_EPOCH <= 5555 FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}

	
	@Test
	void purchaseOrderDAOKeyQuery() {
		PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
		String query =purchaseOrderDAO.newQueryRequest()
				.queryAttribute()
				.wherePurchaseOrder()
				.isPurchaseOrder(new PurchaseOrder.Builder().withId(new Id("fakePO")).build())
				.queryAttribute()
				.wherePurchaseOrderCustomer()
				.isCustomer(new Customer.Builder().withId(new Id("fakecustrebv")).build())
				.queryAttribute()
				.wherePurchaseOrderBook()
				.isBook(new Book.Builder().withId(new Id("FAKEBOOKIDREB")).build())
				.queryAttribute()
				.wherePurchaseOrderStatus()
				.isDelivered()
				.queryAttribute()
				.wherePurchaseOrderStatus()
				.queryAsDisjunction()
				.isOrdered()
				.queryAttribute()
				.wherePurchaseOrderStatus()
				.queryAsDisjunction()
				.isProcessed()
				.queryAttribute()
				.wherePurchaseOrderStatus()
				.isDenied()
				.queryAttribute()
				.wherePurchaseOrderStatus()
				.isShipped()
				.getQueryString();
		String expectedResult="SELECT  * FROM PURCHASE_ORDER  WHERE (ID='' AND CREATED_AT_EPOCH=0 AND ID='fakecustrebv' AND ID='FAKEBOOKIDREB' AND STATUS='DELIVERED' AND STATUS='DENIED' AND STATUS='SHIPPED') AND (STATUS='ORDERED' OR STATUS='PROCESSED') FETCH FIRST 20 ROWS ONLY";
		assertEquals(query, expectedResult,"BookDAO did not generate the correct query string");
	}	
}
