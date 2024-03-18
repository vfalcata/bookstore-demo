package data.beans;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import data.beans.IdObject.IdObjectBuilder;
import data.beans.PurchaseOrder.Builder;
import data.schema.CustomerSchema;
import data.schema.UserTypes;
import data.schema.VisitorSchema;

public class Cart extends IdObject {
	private Map<Book, Integer> books;
	String userType;
	private boolean _isWithinCustomer;
	private boolean _isWithinSiteUser;

	@Override
	public boolean equals(Object object) {
		Cart other = (Cart) object;
		return other.id.isEqual(this.id);
	}

	public boolean isEmpty() {
		return this.books.isEmpty();
	}

	public boolean isCartOfUser(SiteUser siteUser) {
		return siteUser.getId().equals(siteUser.getId());
	}

	public boolean isBookInCart(Book book) {
		boolean result = false;
		for (Entry<Book, Integer> entry : this.books.entrySet()) {
			if (book.getId().equals(entry.getKey().getId()))
				result = true;
		}
		return result;
	}

	public int numberOfBook(Book book) {
		return isBookInCart(book) ? this.books.get(book) : 0;
	}

	public Map<Book, Integer> getBooks() {
		return books;
	}

	public void setBooks(Map<Book, Integer> books) {
		this.books = books;
	}

	public void addBookAmount(Book book, int amount) {
		this.books.put(book, amount);
	}

	public boolean isVisitorCart() {
		return this.userType.equals(UserTypes.VISITOR);
	}

	public boolean isCustomerCart() {
		return this.userType.equals(UserTypes.CUSTOMER);
	}

	public String getUserType() {
		return this.userType;
	}

	public boolean isWithinSiteUser() {
		return this._isWithinSiteUser;
	}



	public static class Builder extends IdObjectBuilder<Builder> {
		private Map<Book, Integer> books;
		private String userType;
		private boolean _isWithinSiteUser;
		public Builder(Cart cart) {
			this.books = cart.books==null?new LinkedHashMap<Book, Integer>():cart.books;
			this._isWithinSiteUser = cart._isWithinSiteUser;
		}

		public Builder() {
			super();
			this.books = new LinkedHashMap<Book, Integer>();
			this._isWithinSiteUser = false;
			userType="";
		}

		public Builder withSiteUser(Visitor visitor) {
			this.id = visitor.id;
			this.userType=UserTypes.VISITOR;
			return this;
		}

		public Builder withSiteUser(Customer customer) {
			this.id = customer.getId();
			this.userType=UserTypes.CUSTOMER;
			return this;
		}

		public Builder withBooks(Map<Book, Integer> books) {
			this.books = books;
			return this;
		}

		public Builder withAdditionalBooks(Map<Book, Integer> books) {
			this.books.putAll(books);
			return this;
		}

		public Builder withBook(Book book) {
			if (this.books.get(book) == null || this.books.get(book) == 0) {
				this.books.put(book, 1);
			} else {
				this.books.put(book, this.books.get(book) + 1);
			}

			return this;
		}

		public Builder withInSiteUser() {
			this._isWithinSiteUser = true;
			return this;
		}


		public Builder withBookAmount(Book book, int amount) {
			this.books.put(book, amount);
			return this;
		}

		public Cart build() {
			Cart cart = new Cart();
			cart.books = this.books==null?new LinkedHashMap<Book, Integer>():this.books;
			cart.userType = this.userType==null?"":this.userType;
			cart._isWithinSiteUser = this._isWithinSiteUser;
			cart.id = this.id==null?new Id(""):this.id;
			return cart;
		}

	}
	
	public void clearCart() {
		this.books=new LinkedHashMap<Book, Integer>();
	}

	@Override
	public String toJson() {
		String customerJson = "";
		String visitorJson = "";

		String booksJson = "\"books\": [";
		if (this.books != null && !this.books.isEmpty()) {
			for (Entry<Book, Integer> entry : this.books.entrySet()) {
				booksJson += "{\"amount\":" + Integer.toString(entry.getValue()) + ",";
				booksJson += "\"book\":";
				booksJson += entry.getKey().toJson() + "},";
			}
			booksJson = booksJson.substring(0, booksJson.length() - 1);

		}
		booksJson += "]";
		String userJson = this.userType.equals(UserTypes.VISITOR) ? visitorJson : customerJson;
		double total=0;
		for(Entry<Book,Integer> entry:getBooks().entrySet()) {
			total=entry.getKey().getPrice()*entry.getValue()+total;
		}
		return "{" + Bean.jsonMapVarChar("user", this.id.toString()) + ","
				+ Bean.jsonMapVarChar("userType", this.userType) + "," 
				+ Bean.jsonMapNumber("total", String.format("%.2f", total))+","
				+ booksJson + "}";
	}
}
