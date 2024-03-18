package data.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import data.schema.DataSchema;

public class BookStoreNumberQuery <T extends BookStoreNumberQuery,U extends AttributeAccess,V extends BookStoreQuery> extends NumberQuery<T, U>{
	BookStoreQuery<T, U> emptyBookStoreQuery;
	protected BookStoreNumberQuery(Query query, DataSchema dataSchema) {
		super(query, dataSchema);
		this.emptyBookStoreQuery=new BookStoreQuery<T, U>(dataSchema);
	}
	@Override
	protected List<DataAccessString> getReferenceDataAccessString(String tableName, String otherTableName){
		return emptyBookStoreQuery.getReferenceDataAccessString(tableName, otherTableName);
	}
	@Override
	protected Map<String, Set<String>> getReferenceRules() {
		return emptyBookStoreQuery.getReferenceRules();
	}

}
