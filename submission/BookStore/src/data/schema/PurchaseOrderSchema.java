package data.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import data.beans.Book;
import data.beans.Customer;
public class PurchaseOrderSchema extends DataSchema{
	public static final String ID="ID";
	public static final String BOOK="BOOK";
	public static final String AMOUNT="AMOUNT";
	public static final String CREATED_AT_EPOCH="CREATED_AT_EPOCH";
	public static final String STATUS="STATUS";
	public static final String PROCESSED_STATUS="PROCESSED";
	public static final String SHIPPED_STATUS="SHIPPED";
	public static final String DENIED_STATUS="DENIED";
	public static final String DELIVERED_STATUS="DELIVERED";
	public static final String ORDERED_STATUS="ORDERED";
	public static final String CREDIT_CARD="CREDIT_CARD";
	public static final String  CREDIT_CARD_NUMBER="CREDIT_CARD_NUMBER";
	public static final String  CREDIT_CARD_EXPIRY="CREDIT_CARD_EXPIRY";
	public static final String  CREDIT_CARD_CVV2="CREDIT_CARD_CVV2";
	public static final String EMAIL="EMAIL";
	public static final String STREET_NUMBER="STREET_NUMBER";
	public static final String STREET="STREET";
	public static final String POSTAL_CODE="POSTAL_CODE";
	public static final String CITY="CITY";
	public static final String PROVINCE="PROVINCE";
	public static final String COUNTRY="COUNTRY";
	public static final String ISBN="ISBN";

	public PurchaseOrderSchema() {
		super();
		this.TABLE_NAME="PURCHASE_ORDER";
		VARCHAR_ATTRIBUTE_LABELS = new String[]{ID,BOOK,AMOUNT,CREATED_AT_EPOCH};
		ATTRIBUTE_LABELS = new String[]{ID,ISBN,BOOK,AMOUNT,CREATED_AT_EPOCH,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2,STATUS,STREET_NUMBER,STREET,POSTAL_CODE,CITY,PROVINCE,COUNTRY,EMAIL};
		WORD_ATTRIBUTE_LABELS = new String[]{STATUS};
		NUMBER_ATTRIBUTE_LABELS = new String[]{};
		OBJECT_ATTRIBUTE_LABELS =  new String[]{};		
	}
	
}
