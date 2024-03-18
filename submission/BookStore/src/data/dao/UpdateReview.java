package data.dao;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import data.beans.Book;
import data.beans.Customer;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.dao.UpdateBook.BookInsert;
import data.dao.UpdateBook.InsertBookSeries;
import data.dao.UpdateBook.InsertBookTitle;
import data.schema.BookSchema;
import data.schema.ReviewSchema;

public class UpdateReview extends DataUpdate{
	UpdateReview(){
		
	}
	
	public void executeDeleteReview(SiteUser siteUser, Review review) {
		if(siteUser.getId().isEmpty() || review.getBook().getId().isEmpty()|| !review.getSiteUser().getId().equals(siteUser.getId())) return;
		String update="DELETE FROM REVIEW WHERE SITE_USER='"+siteUser.getId().toString()+"' AND  BOOK='"+review.getBook().getId().toString()+"' AND USER_TYPE='"+review.getUserType()+"'";
		sendUpdateToDatabase(update);
	}
	
	public ReviewUpdater requestUpdateReview(SiteUser siteUser, Review review) {
		if(siteUser.getId().isEmpty() || review.getBook().getId().isEmpty()|| !review.getSiteUser().getId().equals(siteUser.getId())) return null;
		return new ReviewUpdater(review);
	}
	
	public ReviewUpdater requestUpdateReview(Customer customer, Review review) {
		
		return requestUpdateReview((SiteUser) customer,review);
	}
	
	public ReviewUpdater requestUpdateReview(Visitor visitor, Review review) {		
		return requestUpdateReview((SiteUser) visitor,review);
	}
	
	public InsertReviewTitle requestNewReviewInsertion(SiteUser siteUser, Book book) {
		return new InsertReviewTitle(new Review.Builder().withSiteUser(siteUser).withBook(book).build());
	}
	
	public InsertReviewTitle requestNewReviewInsertion(Customer customer, Book book) {
		return new InsertReviewTitle(new Review.Builder().withSiteUser((SiteUser)customer).withBook(book).build());
	}
	public InsertReviewTitle requestNewReviewInsertion(Visitor visitor, Book book) {
		return new InsertReviewTitle(new Review.Builder().withSiteUser((SiteUser)visitor).withBook(book).build());
	}
	
	public class InsertReviewTitle extends ReviewInsert{
		private InsertReviewTitle(Review review){
			super(review);
		}
		
		public InsertReviewBody insertReviewWithTitle(String title){
			return new InsertReviewBody(new Review.Builder(review).withTitle(surroundWithQuotes(title)).build());
		}
	}
	
	public class InsertReviewBody extends ReviewInsert{
		private InsertReviewBody(Review review){
			super(review);
		}
		
		public InsertReviewRating insertReviewWithBody(String body){
			return new InsertReviewRating(new Review.Builder(review).withBody(surroundWithQuotes(body)).build());
		}
	}
	
	public class InsertReviewRating extends ReviewInsert{
		private InsertReviewRating(Review review){
			super(review);
		}
		
		public ExecuteReviewInsertion insertReviewWithRating(int rating){
			return new ExecuteReviewInsertion(new Review.Builder(review).withRating(rating).build());
		}
	}
	
	abstract class ReviewInsert extends DataUpdate{
		Review review;
		ReviewInsert(Review review){
			this.review=review;
		}
	}
	public class ExecuteReviewInsertion extends ReviewInsert{
		private ExecuteReviewInsertion(Review review) {
			super(review);
		}

		public Review executeReviewInsertion(){
			long createdAtEpoch=Instant.now().getEpochSecond();
			String epoch =Long.toString(createdAtEpoch);
			String update ="INSERT INTO REVIEW (USER_TYPE,NAME,SITE_USER,BOOK,RATING,TITLE,BODY,CREATED_AT_EPOCH ) VALUES "+
					"('"+review.getUserType()+"','"+review.getName()+"','"+review.getSiteUser().getId().toString()+"','"+review.getBook().getId().toString()+"',"+Integer.toString(review.getRating())+","+review.getTitle()+","+review.getBody()+","+epoch+")";
			sendUpdateToDatabase(update);
			return new Review.Builder(review).withCreatedAtEpoch(createdAtEpoch).build();
		}
	}
	
	
	public class ReviewUpdater extends DataUpdate{
		Map<String,String> updateRequest;
		private Review review;
		private ReviewSchema reviewSchema = new ReviewSchema();
		private ReviewUpdater(Review review){
			this.updateRequest=new LinkedHashMap<String, String>();
			this.review=review;
		}
		public ReviewUpdater updateReviewRating(int rating ) {
			this.updateRequest.put(reviewSchema.RATING, Integer.toString(rating));
			return this;
		}
		public ReviewUpdater updateReviewTitle(String title) {
			this.updateRequest.put(reviewSchema.TITLE, surroundWithQuotes(title));
			return this;
		}
		public ReviewUpdater updateReviewBody(String body) {
			this.updateRequest.put(reviewSchema.BODY, surroundWithQuotes(body));
			return this;
		}
		
		public Review executeUpdate() {
			String update = "UPDATE REVIEW SET ";
			for(Entry<String,String> entry:this.updateRequest.entrySet()) {
				update+=entry.getKey()+"="+entry.getValue()+",";
			}
			update=update.substring(0,update.length()-1);
			update+=" WHERE BOOK='"+review.getBook().getId().toString()+"' AND SITE_USER='"+review.getSiteUser().getId().toString()+"' AND USER_TYPE='"+review.getUserType()+"'";
			sendUpdateToDatabase(update);
			return new Review.Builder(review).build();
		}		
	}
}