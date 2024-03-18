package data.fetcher;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;
import data.beans.Bean;

public abstract class DataFetcher<T extends Bean> {

	protected Map<String,Set<String>> attributesToIncludInResults;
	
	DataFetcher(Map<String,Set<String>> attributesToIncludInResults) {
		this.attributesToIncludInResults=attributesToIncludInResults;
	}

	public abstract T resultSetToBean(ResultSet resultSet);
	
	public boolean isReferenceQuery() {
		return attributesToIncludInResults.keySet().size()>1;
	}

	
}
