package data.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import data.schema.DataSchema;

public class BookStoreVarCharQuery <T extends BookStoreVarCharQuery,U extends AttributeAccess,V extends BookStoreQuery> extends VarCharQuery<T, U>{
	
	BookStoreQuery<T, U> emptyBookStoreQuery;
	protected BookStoreVarCharQuery(Query query, DataSchema dataSchema) {
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
