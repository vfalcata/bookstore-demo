package data.beans;

public class CreditCard implements Bean{
	private String creditCardType;
	private String creditCardNumber;
	private String creditCardExpiry;
	private String creditCardCVV2;
	private CreditCard() {
		
	}
	
	public String getCreditCardType() {
		return creditCardType;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	
	public String getConcealedCreditCardNumber() {
		return creditCardNumber.substring(0,creditCardNumber.length()-3).replaceAll(".*", "*")+creditCardNumber.substring(creditCardNumber.length()-4,creditCardNumber.length());
	}

	public String getCreditCardExpiry() {
		return creditCardExpiry;
	}

	public String getCreditCardCVV2() {
		return creditCardCVV2;
	}

	public boolean isEmpty() {
		return  creditCardType==null || creditCardType.isEmpty() ||creditCardExpiry==null || creditCardExpiry.isEmpty()||creditCardNumber==null || creditCardNumber.isEmpty()||creditCardCVV2==null || creditCardCVV2.isEmpty();
	}
	
	
	public static class Builder {
		private String creditCardType;
		private String creditCardNumber;
		private String creditCardExpiry;
		private String creditCardCVV2;
		public Builder() {
			this.creditCardCVV2="";
			this.creditCardNumber="";
			this.creditCardExpiry="";
			this.creditCardType="";
		}
		
		public Builder withCreditCardType(String creditCardType) {
			this.creditCardType=creditCardType;
			return this;
		}
		public Builder withCreditCardNumber(String creditCardNumber ) {
			this.creditCardNumber=creditCardNumber;
			return this;
		}
		public Builder withCreditCardExpiry(String creditCardExpiry) {
			this.creditCardExpiry=creditCardExpiry;
			return this;
		}
		public Builder withCreditCardCVV2(String creditCardCVV2) {
			this.creditCardCVV2=creditCardCVV2;
			return this;
		}
		
		public Builder(CreditCard creditCard) {
			this.creditCardCVV2=creditCard.creditCardCVV2;
			this.creditCardNumber=creditCard.creditCardNumber;
			this.creditCardExpiry=creditCard.creditCardExpiry;
			this.creditCardType=creditCard.creditCardType;
		}
		public CreditCard build() {
			CreditCard creditCard = new CreditCard();
			creditCard.creditCardCVV2=this.creditCardCVV2==null?"":this.creditCardCVV2;
			creditCard.creditCardNumber=this.creditCardNumber==null?"":this.creditCardNumber;
			creditCard.creditCardExpiry=this.creditCardExpiry==null?"":this.creditCardExpiry;
			creditCard.creditCardType=this.creditCardType==null?"":this.creditCardType;
			return creditCard;
		}
	}
	@Override
	public String toJson() {
		return  "{"+Bean.jsonMapVarChar("creditCardType",this.creditCardType)+","+
				Bean.jsonMapVarChar("creditCardNumber",this.creditCardNumber)+","+
				Bean.jsonMapVarChar("creditCardExpiry",this.creditCardExpiry)+","+
				Bean.jsonMapVarChar("creditCardCVV2",this.creditCardCVV2)+
				"}"
				;
	}
}
