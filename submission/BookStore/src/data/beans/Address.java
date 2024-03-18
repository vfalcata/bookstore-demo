package data.beans;

public class Address implements Bean{
	

	private String number;
	private String street;
	private String postalCode;
	private String province;
	private String country;
	private String city;
	
	public String getNumber() {
		return number;
	}

	public String getStreet() {
		return street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getProvince() {
		return province;
	}

	public String getCountry() {
		return country;
	}
	
	public String getCity() {
		return this.city;
	}

	
	public boolean isEmpty() {
		return (number==null || number.isEmpty()) && (street==null || street.isEmpty()) && (postalCode==null || postalCode.isEmpty()) && (province==null || province.isEmpty()) && (country==null || country.isEmpty());
	}
	
	public boolean hasMissingComponents() {
		return number==null || number.isEmpty() || street==null || street.isEmpty() || postalCode==null || postalCode.isEmpty()|| province==null || province.isEmpty()||country==null || country.isEmpty();
	}
	
	public static class Builder{
		private String number;
		private String street;
		private String postalCode;
		private String province;
		private String country;
		private String city;

		public Builder(){
			number="";
			street="";
			postalCode="";
			province="";
			country="";
			city="";
		}
		
		public Builder(Address address){
			this.number=address.number;
			this.street=address.street;
			this.postalCode=address.postalCode;
			this.province=address.province;
			this.country=address.country;
			this.city=address.city;
		}
		
		public Builder withCity(String city) {
			this.city=city;
			return this;
		}

		public Builder withNumber(String number){
			this.number=number;
			return this;
		}

		public Builder withStreet(String street){
			this.street=street;
			return this;
		}

		public Builder withPostalCode(String postalCode){
			this.postalCode=postalCode;
			return this;
		}

		public Builder withProvince(String province){
			this.province=province;
			return this;
		}

		public Builder withCountry(String country){
			this.country=country;
			return this;
		}

		public Address build(){
			Address address=new Address();
			address.number=this.number==null?"":this.number;
			address.street=this.street==null?"":this.street;
			address.postalCode=this.postalCode==null?"":this.postalCode;
			address.province=this.province==null?"":this.province;
			address.country=this.country==null?"":this.country;
			address.city=this.city==null?"":this.city;
			return address;
		}
		


	}

	@Override
	public String toJson() {
		return  "{"+Bean.jsonMapVarChar("number",this.number)+","+
				Bean.jsonMapVarChar("street",this.street)+","+
				Bean.jsonMapVarChar("city",this.city)+","+
				Bean.jsonMapVarChar("province",this.province)+","+
				Bean.jsonMapVarChar("country",this.country)+","+
				Bean.jsonMapVarChar("postalCode",this.postalCode)+
				"}"
				;
	}
}
