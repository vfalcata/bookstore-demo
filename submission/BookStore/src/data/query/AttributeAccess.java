package data.query;

import data.dao.BookDAO.BookStoreBookQuery;

public class AttributeAccess<T extends Query> {
	protected T query;
	
	public void setQuery(T query){
		this.query=query;
	}
}
