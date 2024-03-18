package data.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ReviewSchema extends DataSchema {
	
	
	public static final String BODY="BODY";
	public static final String TITLE="TITLE";
	public static final String RATING="RATING";
	public static final String SITE_USER="SITE_USER";
	public static final String BOOK="BOOK";
	public static final String CREATED_AT_EPOCH="CREATED_AT_EPOCH";
	public static final String USER_TYPE="USER_TYPE";
	public static final String NAME="NAME";


	public ReviewSchema() {
		super();
		this.TABLE_NAME="REVIEW";
		ATTRIBUTE_LABELS = new String[]{NAME,USER_TYPE,SITE_USER,BODY,TITLE,RATING,BOOK,CREATED_AT_EPOCH};
		WORD_ATTRIBUTE_LABELS = new String[]{};
		NUMBER_ATTRIBUTE_LABELS = new String[]{RATING,CREATED_AT_EPOCH};
		VARCHAR_ATTRIBUTE_LABELS = new String[]{BODY,TITLE};
		OBJECT_ATTRIBUTE_LABELS =  new String[]{BOOK};	
	}	
}
