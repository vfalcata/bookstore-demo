package data.schema;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import data.beans.Review;

public class BookSchema extends DataSchema{
	
	public static final String ID="ID";
	public static final String TITLE="TITLE";
	public static final String SERIES="SERIES";
	public static final String DESCRIPTION="DESCRIPTION";
	public static final String CATEGORY="CATEGORY";
	public static final String AUTHOR="AUTHOR";
	public static final String COVER="COVER";
	public static final String ISBN="ISBN";
	public static final String PUBLISH_YEAR ="PUBLISH_YEAR";
	public static final String PRICE="PRICE";
	
	public static final String AMOUNT_SOLD="AMOUNT_SOLD";
	public static final String RATING="RATING";	
	public static final String REVIEW="REVIEW";
	
	public static final String BOOK_CATEGORY_ADVENTURE="BOOK_CATEGORY_ADVENTURE";
	public static final String BOOK_CATEGORY_ARTISTS="BOOK_CATEGORY_ARTISTS";
	public static final String BOOK_CATEGORY_BUSINESS="BOOK_CATEGORY_BUSINESS";
	public static final String BOOK_CATEGORY_COMICS="BOOK_CATEGORY_COMICS";
	public static final String BOOK_CATEGORY_DYSTOPIA="BOOK_CATEGORY_DYSTOPIA";
	public static final String BOOK_CATEGORY_ENTERTAINMENT="BOOK_CATEGORY_ENTERTAINMENT";
	public static final String BOOK_CATEGORY_HISTORICAL="BOOK_CATEGORY_HISTORICAL";
	public static final String BOOK_CATEGORY_HORROR="BOOK_CATEGORY_HORROR";
	public static final String BOOK_CATEGORY_ROMANCE="BOOK_CATEGORY_ROMANCE";
	public static final String BOOK_CATEGORY_SCIENCE_FICTION="BOOK_CATEGORY_SCIENCE_FICTION";
	
	public BookSchema() {
		super();
		this.TABLE_NAME="BOOK";
		ATTRIBUTE_LABELS = new String[]{ID,TITLE,SERIES,DESCRIPTION,CATEGORY,AUTHOR,PRICE,COVER,PUBLISH_YEAR,ISBN,RATING,AMOUNT_SOLD};
		WORD_ATTRIBUTE_LABELS = new String[]{};
		NUMBER_ATTRIBUTE_LABELS = new String[]{PRICE,AMOUNT_SOLD,PUBLISH_YEAR};
		VARCHAR_ATTRIBUTE_LABELS = new String[]{ID,AUTHOR,TITLE,DESCRIPTION,CATEGORY,COVER,ISBN,SERIES};
		OBJECT_ATTRIBUTE_LABELS =  new String[]{REVIEW};		
	}
}
