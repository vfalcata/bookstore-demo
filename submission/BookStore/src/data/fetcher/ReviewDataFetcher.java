package data.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.beans.Review;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.dao.BookDAO;
import data.dao.CustomerDAO;
import data.query.Query;
import data.schema.BookSchema;
import data.schema.CustomerSchema;
import data.schema.ReviewSchema;
import data.schema.UserTypes;

public class ReviewDataFetcher extends DataFetcher<Review> {

	public ReviewDataFetcher(Map<String, Set<String>> attributesToIncludInResults) {
		super(attributesToIncludInResults);
	}

	private ReviewSchema schema = new ReviewSchema();

	@Override
	public Review resultSetToBean(ResultSet resultSet) {
		try {
			boolean isRequestAllAttributes = this.attributesToIncludInResults.get(schema.tableName()).isEmpty();
			String prefix = isReferenceQuery()?schema.tableName()+Query.referenceSeparator:"";
			SiteUser siteUser = resultSet.getString(prefix + ReviewSchema.USER_TYPE).equals(UserTypes.CUSTOMER)?
					(SiteUser)new Customer.Builder().withId(new Id(resultSet.getString(prefix + ReviewSchema.SITE_USER))).build():
					(SiteUser)new Visitor.Builder().withId(new Id(resultSet.getString(prefix + ReviewSchema.SITE_USER))).build()
						;
			Book book = new Book.Builder().withId(new Id(resultSet.getString(prefix + ReviewSchema.BOOK))).build();	
			Review review = new Review.Builder()
					.withSiteUser(siteUser)
					.withBook(book)
					.withName(resultSet.getString(prefix + schema.NAME))
					.build();

			if (isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.TITLE)) {
				review = new Review.Builder(review).withTitle(resultSet.getString(prefix + schema.TITLE)).build();
			}

			if (isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.BODY)) {
				review = new Review.Builder(review).withBody(resultSet.getString(prefix + schema.BODY)).build();
			}

			if (isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.RATING)) {
				review = new Review.Builder(review).withRating(resultSet.getInt(prefix + schema.RATING)).build();
			}

			if (isRequestAllAttributes || attributesToIncludInResults.get(schema.tableName()).contains(schema.CREATED_AT_EPOCH)) {
				review = new Review.Builder(review).withCreatedAtEpoch(resultSet.getLong(prefix + schema.CREATED_AT_EPOCH)).build();
			}
			return review;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.err.println("Warning empty book, since resultSet could not produce review object");
		return null;
	}
}
