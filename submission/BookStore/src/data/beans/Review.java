package data.beans;

import javax.servlet.http.HttpServletRequest;

import data.schema.UserTypes;

public class Review implements Bean{
	private String body;
	private String title;
	private SiteUser siteUser;
	private int rating;
	private Book book;//SUBJECT TO CHANGE
	private long createdAtEpoch;
	private String name;
	private boolean _isWithinBook;
	private boolean _isWithinCustomer;
	public String getBody() {
		return body;
	}

	public String getTitle() {
		return title;
	}

	public SiteUser getSiteUser() {
		return siteUser;
	}

	public int getRating() {
		return rating;
	}

	public Book getBook() {
		return book;
	}

	public long getCreatedAtEpoch() {
		return createdAtEpoch;
	}
	
	public boolean isWithinCustomer() {
		return this._isWithinCustomer;
	}
	
	public boolean isWithinBook() {
		return this._isWithinBook;
	}

	public String getName() {
		return this.name;
	}
	public String getUserType() {
		return this.siteUser.getUserType();
	}
	
	public boolean isReviewByACustomer() {
		return this.siteUser.getUserType().equals(UserTypes.CUSTOMER);
	}

	
	public String toJson() {
		String siteUserJson=isWithinCustomer()?Bean.jsonMapNumber("siteUser","{}"):Bean.jsonMapNumber("siteUser",this.siteUser.toJson());
		String bookJson=isWithinBook()?Bean.jsonMapNumber("book","{}"):Bean.jsonMapNumber("book",this.book.toJson());


		return "{"+Bean.jsonMapVarChar("userId",this.siteUser.getId().toString())+","+
				Bean.jsonMapVarChar("userType",this.siteUser.getUserType())+","+
				Bean.jsonMapVarChar("name",this.name)+","+
				Bean.jsonMapVarChar("book",this.book.getId().toString())+","+
				Bean.jsonMapVarChar("body",this.body.replaceAll("\"", "\\\""))+","+
				Bean.jsonMapVarChar("title",this.title.replaceAll("\"", "\\\""))+","+
				Bean.jsonMapNumber("rating",Integer.toString(this.rating))+","+
				Bean.jsonMapNumber("createdAtEpoch",Long.toString(this.createdAtEpoch))+","+
				siteUserJson+","+
				bookJson+
				"}"
				;

	}

	public static class Builder{
		private String body;
		private String title;
		private int rating;
		private Book book;
		private long createdAtEpoch;
		private boolean _isWithinBook;
		private boolean _isWithinCustomer;
		private SiteUser siteUser;
		private String name;

		public Builder(){
			this.body="";
			this.title="";
			this.name="";
			this.rating=0;
			this.book=new Book.Builder().build();
			this._isWithinBook=false;
			this._isWithinCustomer=false;
			this.siteUser=new Visitor.Builder().build();
		}
		
		public Builder withinBook() {
			this._isWithinBook=true;
			return this;
		}
		
		public Builder withinCustomer() {
			this._isWithinCustomer=true;
			return this;
		}
		
		public Builder(Review review){
			this.body=review.body;
			this.title=review.title;
			this.siteUser=review.siteUser;
			this.name=review.name;
			this.rating=review.rating;
			this.book=new Book.Builder(review.book).withReviews(new Review[0]).build();
			this.createdAtEpoch=review.createdAtEpoch;
			this._isWithinBook=review._isWithinBook;
			this._isWithinCustomer=review._isWithinCustomer;
			
		}

		public Builder withBody(String body){
			this.body=body;
			return this;
		}

		public Builder withTitle(String title){
			this.title=title;
			return this;
		}

		public Builder withRating(int rating){
			this.rating=rating;
			return this;
		}

		public Builder withCustomer(Customer customer){
			this.siteUser=(SiteUser)customer;
			return this;
		}
		
		public Builder withVisitor(HttpServletRequest request){
			this.siteUser=new Visitor.Builder().withId(new Id(request.getSession().getId())).build();
			return this;
		}
		
		public Builder withVisitor(Visitor visitor){
			this.siteUser=(SiteUser)visitor;
			return this;
		}
		public Builder withSiteUser(SiteUser siteUser){
			this.siteUser=siteUser;
			return this;
		}
		
		public Builder withName(String name){
			this.name=name;
			return this;
		}
		

		
		public Builder withBook(Book book){
			this.book=new Book.Builder(book).withReviews(new Review[0]).build();
			return this;
		}
		
		
		public Builder withCreatedAtEpoch(long createdAtEpoch){
			this.createdAtEpoch=createdAtEpoch;
			return this;
		}

		public Review build(){
			Review review=new Review();
			review.body=this.body==null?"":this.body;
			review.title=this.title==null?"":this.title;
			review.siteUser=this.siteUser==null? new Visitor.Builder().build():this.siteUser;
			review.name=this.name==null?"":this.name;
			review.rating=this.rating;
			review.book=this.book==null?new Book.Builder().build():this.book;
			review.createdAtEpoch=this.createdAtEpoch;
			review._isWithinBook=this._isWithinBook;
			review._isWithinCustomer=this._isWithinCustomer;

			return review;
		}

	}
}
