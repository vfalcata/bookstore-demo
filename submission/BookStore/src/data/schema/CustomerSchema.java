package data.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerSchema extends DataSchema {
	
	public static final String ID="ID";
	public static final String GIVENNAME="GIVENNAME";
	public static final String SURNAME="SURNAME";
	public static final String USERNAME="USERNAME";
	public static final String PASSWORD="PASSWORD";
	public static final String EMAIL="EMAIL";

	public static final String STREET_NUMBER="STREET_NUMBER";
	public static final String STREET="STREET";
	public static final String POSTAL_CODE="POSTAL_CODE";
	public static final String CITY="CITY";
	public static final String PROVINCE="PROVINCE";
	public static final String COUNTRY="COUNTRY";
	public static final String CREATED_AT_EPOCH ="CREATED_AT_EPOCH";
	public static final String USER_TYPE="USER_TYPE";
	public static final String CREDIT_CARD="CREDIT_CARD";
	public static final String  CREDIT_CARD_NUMBER="CREDIT_CARD_NUMBER";
	public static final String  CREDIT_CARD_EXPIRY="CREDIT_CARD_EXPIRY";
	public static final String  CREDIT_CARD_CVV2="CREDIT_CARD_CVV2";


	public CustomerSchema() {
		super();
		this.TABLE_NAME="CUSTOMER";
		VARCHAR_ATTRIBUTE_LABELS = new String[]{ID,GIVENNAME,EMAIL,SURNAME,USERNAME,PASSWORD,CITY,STREET_NUMBER,STREET,POSTAL_CODE,PROVINCE,COUNTRY,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2};
		ATTRIBUTE_LABELS = new String[]{ID,GIVENNAME,SURNAME,EMAIL,USERNAME,PASSWORD,STREET_NUMBER,CREATED_AT_EPOCH,STREET,POSTAL_CODE,CITY,PROVINCE,COUNTRY,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2};
		WORD_ATTRIBUTE_LABELS = new String[]{};
		NUMBER_ATTRIBUTE_LABELS = new String[]{CREATED_AT_EPOCH};
		OBJECT_ATTRIBUTE_LABELS =  new String[]{};		
	}
	

}
