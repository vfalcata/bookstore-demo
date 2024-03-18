package data.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import data.beans.Id;
import data.beans.Visitor;
import data.query.Query;
import data.schema.VisitorSchema;

public class VisitorDataFetcher extends DataFetcher<Visitor>{
	

	public VisitorDataFetcher(Map<String, Set<String>> attributesToIncludInResults) {
		super(attributesToIncludInResults);
	}

	private VisitorSchema schema=new VisitorSchema();	
	
	@Override
	public Visitor resultSetToBean(ResultSet resultSet) {
		boolean isRequestAllAttributes = this.attributesToIncludInResults.get(schema.tableName()).isEmpty();
		String prefix = isReferenceQuery()?schema.tableName()+Query.referenceSeparator:"";	

				
		try {	
			
			Visitor visitor = new Visitor.Builder()
					.withId(new Id(resultSet.getString(prefix+schema.ID)))
					.build();
			if (isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.ID)) {
				visitor = new Visitor.Builder(visitor).withId(new Id(resultSet.getString(prefix + schema.ID))).build();
			}

			if (isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREATED_AT_EPOCH)) {
				visitor = new Visitor.Builder(visitor).withCreatedAtEpoch(resultSet.getString(prefix + schema.CREATED_AT_EPOCH)).build();
			}


			return visitor;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.err.println("Warning empty Visitor, since resultSet could not produce visitor object");
		return new Visitor.Builder().build();		
	}
}
