# we keep over writing it, so just gonna past context for cloud db for now, to lazy to fix the gitignore atm
```
<?xml version="1.0" encoding="UTF-8"?>
<Context privileged="true" reloadable="true">
	<WatchedResource>WEB-INF/web.xml</WatchedResource>
	<Manager pathname="" />
	<!--TODO resource URL should be dynamic for deployment -->
	<Resource name="jdbc/EECS" 
		factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
		type="javax.sql.DataSource"
		username="bzq45828"
		password="runtimeTerror_1" 
		driverClassName="com.ibm.db2.jcc.DB2Driver"
		url="jdbc:db2://dashdb-txn-sbox-yp-dal09-14.services.dal.bluemix.net:50000/BLUDB"/>
	<ResourceLink global="jdbc/EECS" name="jdbc/EECS" type="javax.sql.DataSource"/>
</Context>
```

# UPDATE

video in the google docs how to use my DAO's
let me know if there are any bugs, I didnt have time test everything


# Quick DAO GUIDE:

There is a video on the google drive, its about 10 mins and i give a quick demo.

DAOs were made to be flexible, you can chain up the methods however you want

Example: lets say you want all the books from an author, all the reviews of those books with 5 stars, and all the customer who wrote the reviews Then you want all those results to be ordered by best selling books of the authors, and give at most 50 results per page, and the results for the 3rd page

```
		BookDAO bookDAO= new BookDAO();
		bookDAO.newQueryRequest()
		.includeAllAttributesInResultFromSchema()
		.excludeBookDescriptionInResult()
		.excludeBookPriceInResult()
		.queryAttribute()
		.whereBookAuthor()
		.varCharContains("Tolkien")
		.queryAttribute()
		.whereBookAmountSold()
		.withAscendingOrderOf() 
		.withResultLimit(50)
		.withPageNumber(3) //ordering, limit and page number applies to the latest whereAttribute, in this case it is whereBookAmountSold()
		.queryReviews()
		.includeAllAttributesInResultFromSchema()
		.queryAttribute()
		.whereReviewRating()
		.numberAtLeast("5")
		.queryCustomers()
		.includeAllAttributesInResultFromSchema()
		.excludeCustomerPasswordInResult()
		.executeQuery()
		.executeCompilation()
		.compileBooks()
```
Queries are one way, do not make circular query. If you need to refer to a previous queried table, just do a new query instead.
Ex, if you want a customers reviews, and a customers purchase orders. you would have to make two queries. one for customer->reviews another for customer->purchase order. then 
link the results. Doing the circular requests will either give you the empty set or an absurd amount of redundant/irrelavant info.  The dao object is essential your root query.


You can only chain query properties to one attribute at a time any time you call the following method it will give you a list of all the attributes related to the object
```
queryAttribute()
```

from here you must pick one of the attributes they are always prefixed with keyword "where" ex for book Title 
```
whereBookTitle()
```

After you select the attribute the set of methods that you can query will come up, you can add as many as you want, it will all apply to the lastest call of 
queryAttribute() chained with the whereAttribute(). They will be all conjunctions unless you explicitly state it to be a disjunction. Any query parameters you set will
apply to that currently selected whereAttribute, this includes pagination and page limints and ordering

# Ecommerce-EECS4413

Our actual source code is in the submission folder

Utilities folder is for stuff that will help us develop

Docs, for docs

# OTHER NOTES:
Ive merged my branch, no pull request, since main wasnt big enough.
How to instantiate a bean or as i call it dataobject:

Root data objects: Book, Customer, Visitor etc. have the actual child objects as fields, eg. Customer has the Review object etc.

non root objects contained by some root eg. reviews and purchase order are contained by customer, will refer to their root parent via Id.

All the data objects use builder pattern to build of the fly heres an example of building a book.
```
Book book=new Book.builder()
           .withTitle("Book Title")
           .withDescription("Book about stuff")
           .withAuthor("Mr Writer")
           .withPrice(10.00)
           .withCover(cover)
           .withReviews(Review[] reviews)
           .build();
```

The builder fields will be stable, i wont change them. Using this method to instantiate means, that i can add new fields if needed, and it wont break anyones code like a regular constructor would.


BECAREFUL ABOUT USING ID!!, I have the type set as string for now, but it may change just use it for the mock objects for now, 

but in your actual implementation use the comparator methods and pass in the object instead. 

eg. instead of using id to check which cart belongs to a customer instead call:
```
isCartOfUser(Customer customer)
```

# Style/Format

**branches naming schema:**

feature related:
```
feature/name_of_feature
```
bug related:
```
bug/name_of_bug
```

test related:
```
test/name_of_test
```

for anything that is purely experimental meant to be thrownout:

```
sandbox/name_of_experiment
```

# TASKS:

Nina: User stuff, cart, payment, purchase history.. etc

Jess: Product Oriented stuff Analytics

Daniel: Security (no analytics)

Kevin: DAO data stuff
