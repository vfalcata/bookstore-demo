package data.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.schema.BookSchema;
import data.schema.CartSchema;
import data.schema.CustomerSchema;
import data.schema.DataSchema;
import data.schema.PurchaseOrderSchema;
import data.schema.ReviewSchema;
import data.schema.VisitorSchema;

public class BookStoreQuery <T extends Query,U extends AttributeAccess> extends Query<T, U>{

	protected BookStoreQuery(DataSchema dataSchema) {
		super(dataSchema);		
	}
	
	protected BookStoreQuery(Query query, DataSchema dataSchema) {
		super(query, dataSchema);
	}


	@Override
	protected Map<String, Set<String>> getReferenceRules() {
		Map<String, Set<String>> results = new HashMap<String, Set<String>>();
		BookSchema bookSchema = new BookSchema();
		CartSchema cartSchema = new CartSchema();
		CustomerSchema customerSchema = new CustomerSchema();
		PurchaseOrderSchema purchaseOrderSchema = new  PurchaseOrderSchema();
		ReviewSchema reviewSchema= new ReviewSchema();
		VisitorSchema visitorSchema=new VisitorSchema();
		
		results.put(bookSchema.tableName(), new HashSet<String>());
		results.put(reviewSchema.tableName(), new HashSet<String>());
		results.put(customerSchema.tableName(), new HashSet<String>());
		results.put(visitorSchema.tableName(), new HashSet<String>());
		results.put(reviewSchema.tableName(), new HashSet<String>());
		results.put(cartSchema.tableName(), new HashSet<String>());
		results.put(purchaseOrderSchema.tableName(), new HashSet<String>());
		
		results.get(bookSchema.tableName()).add(reviewSchema.tableName());
		results.get(bookSchema.tableName()).add(cartSchema.tableName());
		results.get(bookSchema.tableName()).add(purchaseOrderSchema.tableName());
		
		results.get(reviewSchema.tableName()).add(bookSchema.tableName());
		results.get(reviewSchema.tableName()).add(customerSchema.tableName());
		
		results.get(customerSchema.tableName()).add(bookSchema.tableName());
		results.get(customerSchema.tableName()).add(reviewSchema.tableName());
		results.get(customerSchema.tableName()).add(cartSchema.tableName());
		results.get(customerSchema.tableName()).add(purchaseOrderSchema.tableName());
		
		results.get(visitorSchema.tableName()).add(cartSchema.tableName());
		
		results.get(cartSchema.tableName()).add(bookSchema.tableName());
		results.get(cartSchema.tableName()).add(customerSchema.tableName());
		results.get(cartSchema.tableName()).add(visitorSchema.tableName());
		
		results.get(purchaseOrderSchema.tableName()).add(bookSchema.tableName());
		results.get(purchaseOrderSchema.tableName()).add(customerSchema.tableName());	
		return null;
	}
	
	@Override
	protected List<DataAccessString> getReferenceDataAccessString(String tableName, String otherTableName) {
		List<DataAccessString> referenceWirings = new ArrayList<DataAccessString>();
		BookSchema bookSchema = new BookSchema();
		CartSchema cartSchema = new CartSchema();
		CustomerSchema customerSchema = new CustomerSchema();
		PurchaseOrderSchema purchaseOrderSchema = new  PurchaseOrderSchema();
		ReviewSchema reviewSchema= new ReviewSchema();
		VisitorSchema visitorSchema=new VisitorSchema();
		List<String> tableNames = new ArrayList<String>();
		tableNames.add(tableName);
		tableNames.add(otherTableName);
		
		if(tableNames.contains(bookSchema.tableName()) && tableNames.contains(reviewSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(bookSchema.tableName(),bookSchema.ID,reviewSchema.tableName(),reviewSchema.BOOK));
			
		}
		
		if(tableNames.contains(bookSchema.tableName()) && tableNames.contains(cartSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(bookSchema.tableName(),bookSchema.ID,cartSchema.tableName(),cartSchema.BOOK));
		}
		
		if(tableNames.contains(bookSchema.tableName()) && tableNames.contains(purchaseOrderSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(bookSchema.tableName(),bookSchema.ID,purchaseOrderSchema.tableName(),purchaseOrderSchema.BOOK));
		}
		
		if(tableNames.contains(customerSchema.tableName()) && tableNames.contains(reviewSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(customerSchema.tableName(),customerSchema.ID,reviewSchema.tableName(),reviewSchema.SITE_USER));
			
		}
		
		if(tableNames.contains(customerSchema.tableName()) && tableNames.contains(cartSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(customerSchema.tableName(),customerSchema.ID,cartSchema.tableName(),cartSchema.ID));
		}
		
		if(tableNames.contains(customerSchema.tableName()) && tableNames.contains(purchaseOrderSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(customerSchema.tableName(),customerSchema.ID,purchaseOrderSchema.tableName(),purchaseOrderSchema.ID));			
		}
		
		if(tableNames.contains(visitorSchema.tableName()) && tableNames.contains(cartSchema.tableName())){
			referenceWirings.add(generateReferenceWiring(visitorSchema.tableName(),visitorSchema.ID,cartSchema.tableName(),cartSchema.ID));			
		}
		return referenceWirings;
	}
}
