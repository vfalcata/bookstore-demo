package data.query;

public class PageRequestMetaData {
	private static final String ASC="ASC";
	private static final String DESC="DESC";
	String order;
	String attributeName;
	String pageNumber;
	String tableName;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	String limit;
	
	public PageRequestMetaData() {
		this.order="";
		this.attributeName="";
		this.pageNumber="";
		this.limit="20";
		this.tableName="";
	}
	public String getOrder() {
		return order;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public String getLimit() {
		return limit;
	}
	public void setDescOrder() {
		this.order = DESC;
	}
	
	public void setAscOrder() {
		this.order = ASC;
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
		if(this.order.isEmpty())setDescOrder();
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = Integer.toString(pageNumber);
	}
	public void setLimit(int limit) {
		this.limit = Integer.toString(limit);
	}
	
	public boolean hasOrder() {
		return order !=null && !order.isEmpty() && attributeName!=null && !attributeName.isEmpty() && tableName!=null && !tableName.isEmpty();
	}
	
	public boolean isAscending() {
		return order.equals(ASC);
	}
	
	public boolean isDescending() {
		return order.equals(DESC);
	}
	
	public boolean isPaginated() {
		return pageNumber !=null && !pageNumber.isEmpty() && limit!=null && !limit.isEmpty();
	}


}
