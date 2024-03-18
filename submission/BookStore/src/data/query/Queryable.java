package data.query;

import data.schema.DataSchema;

public interface Queryable<T extends Query>{
	public String getQueryString();
	public boolean isLegalDataAccessReference(String tableName);
	public T withAscendingOrderOf();
	public T withDescendingOrderOf();
	public T withPageNumber(int pageNumber);
	public T withResultLimit(int resultLimit);
	public T includeAllAttributesInResultFromSchema(DataSchema dataSchema);
	public T includeAllAttributesInResultFromSchema();
	public T includeAttributeInResult(String attributeName);
	public T excludeAttributeInResult(String attributeName);
	public T excludeAttributeInResult(DataSchema dataSchema,String attributeName);
	public T includeAttributesInResults(String ...attributeNames);	
}
