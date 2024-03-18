package data.beans;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

public class Book extends IdObject{
	private String title;
	private String description;
	private String category;
	private String author;
	private  String series;
	private double price;
	private File cover;
	private String ISBN;
	private Review[] reviews;
	private double rating;
	private int amountSold;
	private int publishYear;
	private static final String relativeImageFolderPath=".."+File.separator+".."+File.separator+"database"+File.separator+"book_images"+File.separator+"covers"+File.separator;

	private Book(){
		
	}
	
	public String getISBN() {
		return ISBN;
	}

	


	public double getRating() {
		return rating;
	}
	
	public int getAmountSold() {
		return amountSold;
	}

	
	
	public boolean isEqual(Book book){
		return false;		
	}
	
	@Override
	public boolean equals(Object object){
		Book book = (Book)object;
		return this.id.equals(book.id);		
	}

	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public String getCategory() {
		return category;
	}


	public String getAuthor() {
		return author;
	}


	public double getPrice() {
		return price;
	}


	public File getCover() {
		return cover;
	}


	public Review[] getReviews() {
		return reviews;
	}
	
	public String getSeries() {
		return this.series;
	}
	


	public boolean isReviewOfBook(Review review) {
		return false;		
	}
	
	private boolean _isInReview;
	
	public boolean isInReview() {
		return _isInReview;		
	}
	
	public int getPublishYear(){
		return this.publishYear;
	}
	
	
	@Override
	public String toJson() {
		String reviewJson="";
		reviewJson="\"reviews\": [";
		if(!isInReview()) {			
			if (this.reviews!=null && this.reviews.length>0) {
				for(Review review:this.reviews) {
					if(review!=null) {
						reviewJson+=review.toJson()+",";
					}
				}
				reviewJson=reviewJson.substring(0, reviewJson.length() - 1);				
			}
		}
		reviewJson+="]";

		return "{"+Bean.jsonMapVarChar("id",this.id.toString())+","+
				Bean.jsonMapVarChar("title",this.title.replaceAll("[\"]", ""))+","+
				Bean.jsonMapVarChar("series",this.series.replaceAll("[\"]", ""))+","+
				Bean.jsonMapVarChar("description",this.description.replaceAll("[\"]", ""))+","+
				Bean.jsonMapVarChar("category",this.category.replaceAll("[\"]", ""))+","+
				Bean.jsonMapVarChar("author",this.author)+","+
				Bean.jsonMapNumber("publishYear",Integer.toString(this.getPublishYear()))+","+
				Bean.jsonMapVarChar("cover",this.cover.getPath())+","+
				Bean.jsonMapVarChar("ISBN",this.ISBN)+","+
				Bean.jsonMapNumber("price",Double.toString(this.price))+","+
				Bean.jsonMapNumber("rating",Double.toString(this.rating))+","+
				Bean.jsonMapNumber("amountSold",Integer.toString(this.amountSold))+","+
				reviewJson+"}"
				;
	}
	

	

	
	public static class Builder extends IdObjectBuilder<Builder>{
		private String title;
		private String description;
		private String category;
		private String author;
		private double price;
		private File cover;
		private Review[] reviews;
		private String ISBN;
		private double rating;
		private int amountSold;
		private int publishYear;
		private String series;
		private boolean _isInReview;

		public Builder(Book book){
			super(book);
			this.title=book.title;
			this.description=book.description;
			this.category=book.category;
			this.author=book.author;
			this.price=book.price;
			this.cover=book.cover;
			this.reviews=book.reviews;
			this.ISBN=book.ISBN;
			this.id=book.getId();
			this.amountSold=book.amountSold;
			this.rating=book.rating;
			this.series=book.series;
			this._isInReview=book._isInReview;
			this.publishYear=book.publishYear;			
		}
		
		public Builder(){
			super();
			this.title="";
			this.description="";
			this.category="";
			this.author="";
			this.price=0.0;
			this.cover=new File("");
			this.ISBN="";
			this.reviews=new Review[0];
			this.id=new Id("");
			this.series="";
			this._isInReview=false;
			this.publishYear=0;
		}
		public Builder withPublishYear(int publishYear){
			this.publishYear=publishYear;
			return this;
		}
		
		public Builder withRating(double rating){
			this.rating=rating;
			return this;
		}

		public Builder withTitle(String title){
			this.title=title;
			return this;
		}
		
		public Builder withInReview() {
			this._isInReview=true;
			return this;
		}
		
		public Builder withSeries(String series){
			this.series=series;
			return this;
		}
		
		
		public Builder withISBN(String ISBN){
			this.ISBN=ISBN;
			return this;
		}

		public Builder withDescription(String description){
			this.description=description;
			return this;
		}

		public Builder withCategory(String category){
			this.category=category;
			return this;
		}

		public Builder withAuthor(String author){
			this.author=author;
			return this;
		}

		public Builder withPrice(double price){
			this.price=price;
			return this;
		}



		public Builder withAmountSold(int amountSold){
			this.amountSold=amountSold;
			return this;
		}
		public Builder withCover(File cover){
			this.cover=cover;
			return this;
		}

		public Builder withReviews(Review[] reviews){
			this.reviews=reviews;
			return this;
		}
		
		public Builder withReviews(List<Review> reviews){
			if(reviews==null ||reviews.isEmpty()) return this;
			Review[] reviewsArray=new Review[reviews.size()];
			return withReviews(reviews.toArray(reviewsArray));
		}
		
		public Builder withReviews(Review review){
			this.reviews=new Review[] {review};
			return this;
		}
		
		public Builder withAdditionalReviews(Review review){
			Review[] reviewAppended = new Review[reviews.length+1];
			for(int i=0;i<this.reviews.length;i++) {
				reviewAppended[i]=this.reviews[i];
			}
			reviewAppended[reviews.length]=review;
			this.reviews=reviewAppended;
			return this;
		}
		
		public Builder withAdditionalReviews(Review[] reviews){
			Review[] reviewAppended = new Review[reviews.length+this.reviews.length];
			int i=0;
			for(Review aReview:this.reviews) {
				reviewAppended[i]=aReview;
				i++;
			}
			
			for(Review aReview:reviews) {
				reviewAppended[i]=aReview;
				i++;
			}
			this.reviews=reviewAppended;
			return this;
		}
		
		public Builder withAdditionalReviews(List<Review> reviews){
			Review[] reviewAppended = new Review[reviews.size()];
			return withAdditionalReviews(reviews.toArray(reviewAppended));
		}
			



		public Book build(){
			Book book=new Book();
			book.id=this.id==null?new Id(""):this.id;
			book.title=this.title==null?"":this.title;
			book.description=this.description==null?"":this.description;
			book.category=this.category==null?"":this.category;
			book.author=this.author==null?"":this.author;
			book.price=this.price;
			book.cover=this.cover==null?new File(""):this.cover;
			book.reviews=this.reviews==null?new Review[0]:this.reviews;
			book.rating=this.rating;
			book.amountSold=this.amountSold;
			book.series=this.series==null?"":this.series;
			book._isInReview=this._isInReview;
			book.publishYear=this.publishYear;
			book.ISBN=this.ISBN==null?"":this.ISBN;
			return book;
		}

	}







}
