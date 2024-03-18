package data.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import data.beans.Bean;
import data.beans.Book;
import data.beans.Cart;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.beans.Visitor;
import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.CustomerSchema;
import data.schema.DataSchema;
import data.schema.PurchaseOrderSchema;
import data.schema.ReviewSchema;
import data.schema.VisitorSchema;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Flexible mechanism to submit queries. Query can be set up with several properties to specify
 * what type of queries are allowed for each attribute. Only the allowed query parameters can be submitted for any given attribute
 * Queries can easily be built on demand to allow for modular and flexible requests.
*/
public abstract class Query<T extends Query,U extends AttributeAccess> implements Queryable<T>{
	
	protected Query query;
	protected String pageNumber;
	protected String ascOrderOfAttribute;/*order of query results ascending*/
	protected String ascOrderOfTable;
	protected String descOrderOfAttribute;/*order of query results descending*/
	protected String descOrderOfTable;/*order of query results descending*/
	protected String resultLimit;
	protected String tableName;
	protected DataSchema dataSchema;
	public final static String referenceOperator=".";
	public final static String referenceSeparator="_";
	protected U attributeAccess;
	protected String currentAttributeAccess;
	protected PageRequestMetaData pageRequestMetaData;
	protected Map<String,Set<String>> attributesToIncludInResults; 	/*Attributes that will be received after a query*/
	protected Map<String,List<DataAccessString>> dataAccessRequestsConjunction;
	protected Map<String,List<DataAccessString>> dataAccessRequestsDisjunction;
	public boolean isDisjunctionMode;
	protected List<DataAccessString> tableJoins;
	
	
	public T queryAsDisjunction() {
		isDisjunctionMode=true;
		return (T) this;
	}
	
	public T queryAsConjunction() {
		isDisjunctionMode=false;
		return (T) this;
	}
	
	public T setDisjunctionMode(boolean disjunctionMode) {
		this.isDisjunctionMode=disjunctionMode;
		return (T) this; 
	}
	
	public PageRequestMetaData getPageData() {
		return pageRequestMetaData;
	}
	
	protected T addDataAccessString(DataAccessString dataAccessString) {
		if(isDisjunctionMode) {
			if(!this.dataAccessRequestsDisjunction.containsKey(tableName) || this.dataAccessRequestsDisjunction.get(tableName)==null)
			 this.dataAccessRequestsDisjunction.put(tableName, new ArrayList<DataAccessString>());
			this.dataAccessRequestsDisjunction.get(tableName).add(dataAccessString);
		}else {
			if(!this.dataAccessRequestsConjunction.containsKey(tableName) || this.dataAccessRequestsConjunction.get(tableName)==null)
				 this.dataAccessRequestsConjunction.put(tableName, new ArrayList<DataAccessString>());
				this.dataAccessRequestsConjunction.get(tableName).add(dataAccessString);
		}
		return (T) this;
	}

	protected Query(DataSchema dataSchema) {
		this.isDisjunctionMode=false;
		this.tableJoins=new LinkedList<DataAccessString>();
		this.dataAccessRequestsConjunction=new HashMap<String, List<DataAccessString>>();
		this.dataAccessRequestsDisjunction= new HashMap<String, List<DataAccessString>>();
		this.pageRequestMetaData=new PageRequestMetaData();
		this.pageNumber="";
		this.attributesToIncludInResults=new HashMap<String, Set<String>>();	/*Attributes that will be received after a query*/
		this.ascOrderOfAttribute="";/*order of query results ascending*/
		this.ascOrderOfTable="";
		this.descOrderOfAttribute="";/*order of query results descending*/
		this.descOrderOfTable="";/*order of query results descending*/
		this.resultLimit="20";
		this.dataSchema=dataSchema;		
		this.attributeAccess=attributeAccess;
		this.currentAttributeAccess="";
		this.tableName=dataSchema.tableName();
		this.attributesToIncludInResults.put(tableName, new HashSet<String>());
		if(isDisjunctionMode) {
			if(!this.dataAccessRequestsDisjunction.containsKey(tableName) || this.dataAccessRequestsDisjunction.get(tableName)==null) this.dataAccessRequestsDisjunction.put(tableName, new ArrayList<DataAccessString>());

		}else {
			if(!this.dataAccessRequestsConjunction.containsKey(tableName) || this.dataAccessRequestsConjunction.get(tableName)==null) this.dataAccessRequestsConjunction.put(tableName, new ArrayList<DataAccessString>());

		}
	}
	
	protected Query(Query query,DataSchema dataSchema) {
		this.pageRequestMetaData=query.pageRequestMetaData;
		this.tableJoins=query.tableJoins;
		this.pageNumber=query.pageNumber;
		this.attributesToIncludInResults=query.attributesToIncludInResults;	/*Attributes that will be received after a query*/
		this.ascOrderOfAttribute=query.ascOrderOfAttribute;/*order of query results ascending*/
		this.ascOrderOfTable=query.ascOrderOfTable;
		this.descOrderOfAttribute=query.descOrderOfAttribute;/*order of query results descending*/
		this.descOrderOfTable=query.descOrderOfTable;/*order of query results descending*/
		this.resultLimit=query.resultLimit;
		this.dataSchema=dataSchema;	
		this.attributeAccess=attributeAccess;
		this.currentAttributeAccess=query.currentAttributeAccess;
		this.dataAccessRequestsConjunction=query.dataAccessRequestsConjunction;
		this.dataAccessRequestsDisjunction= query.dataAccessRequestsDisjunction;
		this.isDisjunctionMode=query.isDisjunctionMode;
		this.tableName=dataSchema.tableName();
		if(!this.attributesToIncludInResults.containsKey(tableName) || this.attributesToIncludInResults.get(tableName)==null)this.attributesToIncludInResults.put(tableName, new HashSet<String>());
		if(isDisjunctionMode) {
			if(!this.dataAccessRequestsDisjunction.containsKey(tableName) || this.dataAccessRequestsDisjunction.get(tableName)==null) this.dataAccessRequestsDisjunction.put(tableName, new ArrayList<DataAccessString>());

		}else {
			if(!this.dataAccessRequestsConjunction.containsKey(tableName) || this.dataAccessRequestsConjunction.get(tableName)==null) this.dataAccessRequestsConjunction.put(tableName, new ArrayList<DataAccessString>());

		}
	}
	
	public U queryAttribute() {
		return (U) this.attributeAccess;
	}
	
	public void setAttribute(U attributeAccess) {
		this.attributeAccess=attributeAccess;
	}
	




	@Override
	public String getQueryString() {
		String joins="";
		BookSchema bookSchema = new BookSchema();
		CartSchema cartSchema = new CartSchema();
		CustomerSchema customerSchema = new CustomerSchema();
		PurchaseOrderSchema purchaseOrderSchema = new  PurchaseOrderSchema();
		ReviewSchema reviewSchema= new ReviewSchema();
		VisitorSchema visitorSchema=new VisitorSchema();
		Map<String,List<String>> tableAttributes = new LinkedHashMap<String, List<String>>();
		tableAttributes.put(bookSchema.tableName(), bookSchema.getAttributeLabels());
		tableAttributes.put(cartSchema.tableName(), cartSchema.getAttributeLabels());
		tableAttributes.put(customerSchema.tableName(), customerSchema.getAttributeLabels());
		tableAttributes.put(purchaseOrderSchema.tableName(), purchaseOrderSchema.getAttributeLabels());
		tableAttributes.put(reviewSchema.tableName(), reviewSchema.getAttributeLabels());
		tableAttributes.put(visitorSchema.tableName(), visitorSchema.getAttributeLabels());
		boolean hasReferences=this.dataAccessRequestsConjunction.keySet().size()>1 ||this.dataAccessRequestsDisjunction.keySet().size()>1;
		String queryString="SELECT ";
		if(this.attributesToIncludInResults.isEmpty() || (this.attributesToIncludInResults.keySet().size()==1 && this.attributesToIncludInResults.get(tableName).isEmpty())) {
			queryString+=" *";
		}else {	

				
			int count =0;
			for(Entry<String,Set<String>> entry: this.attributesToIncludInResults.entrySet()) {
				count++;
				String tableName=entry.getKey();
				List<String> attributeNames=new ArrayList<String>(entry.getValue());
				if(attributeNames.isEmpty()) {
					attributeNames=tableAttributes.get(entry.getKey());
				}

				for(String attribute:attributeNames) {
					queryString+= hasReferences?tableName+this.referenceOperator+attribute+" AS "+tableName+this.referenceSeparator+attribute:attribute;
					queryString+=",";
				}


			}
			queryString=queryString.substring(0, queryString.length()-1);
		}
		String andQuery=" AND ";
		if(hasReferences) {
			
			
			queryString+=" FROM ";
			Set<String> references = new HashSet<String>();
			references.addAll(this.dataAccessRequestsConjunction.keySet());
			references.addAll(this.dataAccessRequestsDisjunction.keySet());
			for(String tablesReference:references) {
				queryString+=  tablesReference+",";
			}
			queryString=queryString.substring(0, queryString.length()-1);
			queryString+= " WHERE ";
				int count=0;				
				for(DataAccessString referenceQuery:this.tableJoins) {
					joins+=referenceQuery.getReferenceDataAcessString() + andQuery;
				}

			if(joins.contains(andQuery))
				queryString+=joins.substring(0,joins.length()-andQuery.length());
		}else {
			queryString+=" FROM "+this.tableName+" ";
		}
		int queryCount=0;
		for(Entry<String,List<DataAccessString>> entry : this.dataAccessRequestsDisjunction.entrySet()) {
			queryCount=queryCount+entry.getValue().size();
		}
		for(Entry<String,List<DataAccessString>> entry : this.dataAccessRequestsConjunction.entrySet()) {
			queryCount=queryCount+entry.getValue().size();
		}
		if(!queryString.contains("WHERE") && queryCount>=1) queryString+=" WHERE ";
		
		String queryParameters=getQueryParameterString();	
		if(queryString.contains("WHERE") && !queryParameters.isEmpty() && !joins.isEmpty())
			queryString+=andQuery;
		queryString+=queryParameters;			
		
		if(this.pageRequestMetaData.hasOrder() && this.pageRequestMetaData.isAscending()) {
			String ascOrderRequest = hasReferences?this.pageRequestMetaData.getTableName()+this.referenceOperator+this.pageRequestMetaData.getAttributeName():this.pageRequestMetaData.getAttributeName();
			queryString+=" ORDER BY "+ascOrderRequest+ " ASC";
		}else if(this.pageRequestMetaData.hasOrder() && this.pageRequestMetaData.isDescending()) {			
			String descOrderRequest = hasReferences?this.pageRequestMetaData.getTableName()+this.referenceOperator+this.pageRequestMetaData.getAttributeName():this.pageRequestMetaData.getAttributeName();
			queryString+=" ORDER BY "+descOrderRequest+ " DESC ";
		}
		
		if(this.pageRequestMetaData.isPaginated()) {
			queryString+=" OFFSET "+Integer.toString(Integer.parseInt(this.pageRequestMetaData.getLimit())*Integer.parseInt(this.pageRequestMetaData.pageNumber)) + " ROWS ";
			queryString+=" FETCH NEXT "+this.pageRequestMetaData.getLimit()+ " ROWS ONLY";	
		}else {
			queryString+=" FETCH FIRST "+this.pageRequestMetaData.getLimit()+" ROWS ONLY";
		}		
		return queryString;
	}
	
	

	
	protected String getQueryParameterString() {		
		boolean hasReferences=this.dataAccessRequestsConjunction.keySet().size()>1 ||this.dataAccessRequestsDisjunction.keySet().size()>1;
		String queryString="";
		int count=0;
		String andConnector=" AND ";
		String orConnector=" OR ";
		String conjunctionQueries="";
		String disjunctionQueries="";
		if (this.dataAccessRequestsConjunction.isEmpty()) {
			System.err.println("No conjunction request submitted so nothing to render for Query.Sql.getQueryParameterString()!");
		}else {			
			for(Entry<String,List<DataAccessString>> entry : this.dataAccessRequestsConjunction.entrySet()) {
				for(DataAccessString dataAccessString:entry.getValue()) {
					count++;
					conjunctionQueries+=dataAccessString.getReferenceDataAcessString();
					conjunctionQueries+=andConnector;
				}
			
			}
			if(conjunctionQueries.contains(andConnector)) {
				conjunctionQueries=conjunctionQueries.substring(0,conjunctionQueries.length()-andConnector.length());	
			}			
		}
		if (this.dataAccessRequestsDisjunction.isEmpty()) {
			System.err.println("No disjunction request submitted so nothing to render for Query.Sql.getQueryParameterString()!");
		}else {
			
			for(Entry<String,List<DataAccessString>> entry : this.dataAccessRequestsDisjunction.entrySet()) {
				for(DataAccessString dataAccessString:entry.getValue()) {
					count++;
					disjunctionQueries+=dataAccessString.getReferenceDataAcessString();
					disjunctionQueries+=orConnector;				
				}
			
			}
			if(disjunctionQueries.contains(orConnector)) {
				disjunctionQueries=disjunctionQueries.substring(0,disjunctionQueries.length()-orConnector.length());	
			}			
		}
		if(!this.dataAccessRequestsDisjunction.isEmpty() && !this.dataAccessRequestsConjunction.isEmpty()) {
			return "("+conjunctionQueries+") AND ("+disjunctionQueries+")";
		}else if(!this.dataAccessRequestsDisjunction.isEmpty()) {
			return  disjunctionQueries;
		}else if(!this.dataAccessRequestsConjunction.isEmpty()) {
			return conjunctionQueries;
		}else {
			return "";	
		}
		
	}

	@Override
	public boolean isLegalDataAccessReference(String tableName) {
		return true;
	}
	
	
	
	@Override
	public T withAscendingOrderOf() {
		this.pageRequestMetaData.setAscOrder();
		this.pageRequestMetaData.setAttributeName(currentAttributeAccess);
		this.pageRequestMetaData.setTableName(tableName);
		return (T) this;
	}

	@Override
	public T withDescendingOrderOf() {
		this.pageRequestMetaData.setDescOrder();
		this.pageRequestMetaData.setAttributeName(currentAttributeAccess);
		this.pageRequestMetaData.setTableName(tableName);
		return (T) this;
	}
	@Override
	public T withPageNumber(int pageNumber){
		this.pageRequestMetaData.setPageNumber(pageNumber);
		return (T)this;
	}
	
	@Override
	public T withResultLimit(int resultLimit) {
		this.pageRequestMetaData.setLimit(resultLimit);
		return (T) this;
	}
	
	@Override
	public T includeAllAttributesInResultFromSchema(DataSchema dataSchema) {
		if(!this.attributesToIncludInResults.containsKey(dataSchema.tableName()))this.attributesToIncludInResults.put(dataSchema.tableName(), new HashSet<String>());
		for(String attributeName:dataSchema.getAttributeLabels()) {
			this.attributesToIncludInResults.get(dataSchema.tableName()).add(attributeName);
		}
		return (T) this;
	}
	
	@Override
	public T includeAllAttributesInResultFromSchema() {
		includeAllAttributesInResultFromSchema(this.dataSchema);
		return (T) this;
	}
	
	@Override
	public T includeAttributeInResult(String attributeName) {
		if(this.attributesToIncludInResults.containsKey(attributeName))this.attributesToIncludInResults.put(this.tableName, new HashSet<String>());
		this.attributesToIncludInResults.get(this.tableName).add(attributeName);
		return (T) this;
	}
	
	@Override
	public T excludeAttributeInResult(String attributeName) {
		if(this.attributesToIncludInResults.containsKey(this.tableName))this.attributesToIncludInResults.get(this.tableName).remove(attributeName);
		this.attributesToIncludInResults.get(this.tableName).add(attributeName);
		return (T) this;
	}
	
	@Override
	public T excludeAttributeInResult(DataSchema dataSchema,String attributeName) {
		if(this.attributesToIncludInResults.containsKey(dataSchema.tableName()))this.attributesToIncludInResults.get(dataSchema.tableName()).remove(attributeName);
		this.attributesToIncludInResults.get(this.tableName).add(attributeName);
		return (T) this;
	}
	
	

	@Override
	public T includeAttributesInResults(String ...attributeNames) {
		return (T) this;
	}

	public T setPageRequestMetaData( PageRequestMetaData pageRequestMetaData){
		this.pageRequestMetaData=pageRequestMetaData;
		return (T) this;
	}
	public T setAttributesToIncludInResults(Map<String,Set<String>> attributesToIncludInResults){
		this.attributesToIncludInResults=attributesToIncludInResults;
		if(!this.attributesToIncludInResults.containsKey(tableName) || this.attributesToIncludInResults.get(tableName)==null)this.attributesToIncludInResults.put(tableName, new HashSet<String>());
		return (T) this;
	}
	
	public T setDataAccessRequestsConjunction(Map<String,List<DataAccessString>> conjunctionRequests){
		this.dataAccessRequestsConjunction=conjunctionRequests;
		if(isDisjunctionMode) {
			if(!this.dataAccessRequestsDisjunction.containsKey(tableName) || this.dataAccessRequestsDisjunction.get(tableName)==null) this.dataAccessRequestsDisjunction.put(tableName, new ArrayList<DataAccessString>());
		}else {
			if(!this.dataAccessRequestsConjunction.containsKey(tableName) || this.dataAccessRequestsConjunction.get(tableName)==null) this.dataAccessRequestsConjunction.put(tableName, new ArrayList<DataAccessString>());
		}
		return (T) this;
	}
	
	
	public T setDataAccessRequestsDisjunction(Map<String,List<DataAccessString>> disjunctionRequests){
		this.dataAccessRequestsDisjunction=disjunctionRequests;
		if(isDisjunctionMode) {
			if(!this.dataAccessRequestsDisjunction.containsKey(tableName) || this.dataAccessRequestsDisjunction.get(tableName)==null) this.dataAccessRequestsDisjunction.put(tableName, new ArrayList<DataAccessString>());
		}else {
			if(!this.dataAccessRequestsConjunction.containsKey(tableName) || this.dataAccessRequestsConjunction.get(tableName)==null) this.dataAccessRequestsConjunction.put(tableName, new ArrayList<DataAccessString>());
		}
		return (T) this;
	}
	
	public T addTableJoins(List<DataAccessString> tableJoins){
		this.tableJoins.addAll(tableJoins);
		return (T) this;
	}

	private void clearQueryCache() {
		this.pageNumber="";
		this.attributesToIncludInResults=new HashMap<String, Set<String>>();	/*Attributes that will be received after a query*/
		this.ascOrderOfAttribute="";/*order of query results ascending*/
		this.ascOrderOfTable="";
		this.descOrderOfAttribute="";/*order of query results descending*/
		this.descOrderOfTable="";/*order of query results descending*/
		this.resultLimit="20";
	}
	
	public DataObjectCompiler executeQuery() {
		return new DataObjectCompiler(getQueryString(), attributesToIncludInResults);
	}
	
	protected DataAccessString generateReferenceWiring(String sourceTableName,String sourceTableAttribute, String referenceTableName, String referenceTableAttribute) {
	return new DataAccessString.Builder()
			 .withTableName(sourceTableName)
			 .withAttributeName(sourceTableAttribute)
			 .withDataAccessParameter(referenceTableName+referenceOperator+referenceTableAttribute)
			 .withDataAccessParameterPrefix("=")
			 .withReferenceOperator(referenceOperator)
			 .build();
	}

	protected abstract List<DataAccessString> getReferenceDataAccessString(String tableName, String otherTableName);
	protected abstract Map<String, Set<String>> getReferenceRules();	
}



