package data.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import data.beans.Book;
public class CartSchema extends DataSchema{
	public static final String ID="ID";
	public static final String BOOK="BOOK";
	public static final String AMOUNT="AMOUNT";


	public CartSchema() {
		super();
		this.TABLE_NAME="CART";
		VARCHAR_ATTRIBUTE_LABELS = new String[]{ID,BOOK,AMOUNT};
		ATTRIBUTE_LABELS = new String[]{ID,BOOK,AMOUNT};
		WORD_ATTRIBUTE_LABELS = new String[]{};
		NUMBER_ATTRIBUTE_LABELS = new String[]{};
		OBJECT_ATTRIBUTE_LABELS =  new String[]{};		
	}

}
