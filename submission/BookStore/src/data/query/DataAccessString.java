package data.query;


public class DataAccessString {
	private String attributeName;
	private String tableName;
	private String dataAccessParameter;
	private String dataAccessParameterPrefix;
	private String dataAccessParameterSuffix;
	private String referenceOperator;

	
	
	private DataAccessString() {
		this.attributeName="";
		this.tableName="";
		this.dataAccessParameter="";
		this.dataAccessParameterPrefix="";
		this.dataAccessParameterSuffix="";
		this.referenceOperator="";
	}

	public String getAttributeName() {
		return attributeName;
	}
	
	public String getDataAccessParameter() {
		return dataAccessParameter;
	}
	
	
	public String getTableName() {
		return tableName;
	}
	
	
	public String getDataAccessString() {
		return this.attributeName+ this.dataAccessParameterPrefix+this.dataAccessParameter+this.dataAccessParameterSuffix;
	}
	
	public String getReferenceDataAcessString() {
		return this.tableName+this.referenceOperator+this.attributeName+ this.dataAccessParameterPrefix+this.dataAccessParameter+this.dataAccessParameterSuffix;
		}
	

	
	public static class Builder {
		private String attributeName;
		private String tableName;
		private String dataAccessParameter;
		private String dataAccessParameterPrefix;
		private String dataAccessParameterSuffix;
		private String referenceOperator;


		
		public Builder(){
			this.attributeName="";
			this.tableName="";
			this.dataAccessParameter="";
			this.dataAccessParameterPrefix="";
			this.dataAccessParameterSuffix="";
			this.referenceOperator="";
		}
		

		
		public Builder withDataAccessParameter(String dataAccessParameter){
			this.dataAccessParameter=dataAccessParameter;
			return this;
		}
	
		public Builder withDataAccessParameterPrefix(String dataAccessParameterPrefix){
			this.dataAccessParameterPrefix=dataAccessParameterPrefix;
			return this;
		}
		
		public Builder withDataAccessParameterSuffix(String dataAccessParameterSuffix){
			this.dataAccessParameterSuffix=dataAccessParameterSuffix;
			return this;
		}
		
		public Builder withAttributeName(String attributeName){
			this.attributeName=attributeName;
			return this;
		}

		
		public Builder withTableName(String tableName){
			this.tableName=tableName;
			return this;
		}
		
		public Builder withReferenceOperator(String referenceOperator){
			this.referenceOperator=referenceOperator;
			return this;
		}
		
		
		
		public DataAccessString build() {
			DataAccessString dataAccessString=new DataAccessString();
			dataAccessString.attributeName=this.attributeName;
			dataAccessString.tableName=this.tableName;
			dataAccessString.dataAccessParameter=this.dataAccessParameter;
			dataAccessString.dataAccessParameterPrefix=this.dataAccessParameterPrefix;
			dataAccessString.dataAccessParameterSuffix=this.dataAccessParameterSuffix;
			dataAccessString.referenceOperator=this.referenceOperator;
			return dataAccessString;
		}
		
	}

}
