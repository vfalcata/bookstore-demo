package data.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class VisitorSchema extends DataSchema {

	public static final String ID="ID";
	public static final String CREATED_AT_EPOCH ="CREATED_AT_EPOCH";
	public static final String LAST_ACCESSED_AT_EPOCH ="LAST_ACCESSED_AT_EPOCH";
	public static final String USER_TYPE="USER_TYPE";


	public VisitorSchema() {
		super();
		this.TABLE_NAME="VISITOR";
		VARCHAR_ATTRIBUTE_LABELS = new String[]{ID};
		ATTRIBUTE_LABELS = new String[]{ID};
		WORD_ATTRIBUTE_LABELS = new String[]{};
		NUMBER_ATTRIBUTE_LABELS = new String[]{};
		OBJECT_ATTRIBUTE_LABELS =  new String[]{};		
	}

}
