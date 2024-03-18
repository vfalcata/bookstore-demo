import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import data.beans.Customer;
import data.schema.PurchaseOrderSchema;
import data.schema.UserTypes;


public class DataGenerator {
	static List<String> customers;
	static List<String> poStatus;
	static List<String> completedLinks;
	static  Map<String, List<String>> koboParentLinks;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		customers = new ArrayList<String>();
		poStatus= new ArrayList<String>();
		completedLinks=new ArrayList<String>();
		try {
			customers = Files.readAllLines(new File("ids-lf.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		poStatus.add("PAID");
		poStatus.add("PROCESSED");
		poStatus.add("SHIPPED");
		poStatus.add("COMPLETED");
		koboParentLinks= new HashMap<String, List<String>>();
		koboParentLinks.put("adventure", new ArrayList<String>());
		koboParentLinks.put("comics",  new ArrayList<String>());
		koboParentLinks.put("dystopia",  new ArrayList<String>());
		koboParentLinks.put("historical",  new ArrayList<String>());
		koboParentLinks.put("horror",  new ArrayList<String>());
		koboParentLinks.put("science-fiction",  new ArrayList<String>());
		koboParentLinks.put("artists",  new ArrayList<String>());
		koboParentLinks.put("business",  new ArrayList<String>());
		koboParentLinks.put("entertainment", new ArrayList<String>());
		koboParentLinks.put("romance",  new ArrayList<String>());
		for(String category:koboParentLinks.keySet()) {
			try {
				koboParentLinks.get(category).addAll(Files.readAllLines(new File(category+".txt").toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			completedLinks=Files.readAllLines(new File("completedLinks.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	@Test
	public void regexTest() {
		String json="{ creditCardType: \"MasterCard\", creditCardNumber: \"5114170954034388\", creditCardExpiry: \"11/2024\", creditCardCVV2: \"412\" }";
		System.out.println(json.replaceAll("creditCardNumber:\\s*(\"\\d{4})(\\d+)(\\d{4}\")", "$1*************$3"));
	
	}
	
	@Test
	public void addNameToReviews() {
		List<String> ids=new ArrayList<String>();
		List<String> reviews=new ArrayList<String>();
		try {
			ids=Files.readAllLines(new File("idToToNameToUname.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reviews=Files.readAllLines(new File("insertReviews.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter writer=null;
		try {
			Map<String,String> idToUname= new LinkedHashMap<String, String>();
			writer = new FileWriter("reviewNew.txt"); 
			for(String line:ids) {
				String[] arr = line.split(":");
				idToUname.put(arr[0], arr[1]);
			}
			for(String line:reviews) {
				String id = line.substring(1,39);				
				String newLine = "("+"'CUSTOMER',"+idToUname.get(id)+","+line.substring(1, line.length())+"\n";
				writer.write(newLine);

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
//	@Test
	public void IdToNames() {
		List<String> customers=new ArrayList<String>();

		try {
			customers=Files.readAllLines(new File("insertCustomers.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileWriter writer=null;
		try {
			writer = new FileWriter("idToToNameToUname.txt"); 
			for(String line:customers) {
				String[] arr = line.split(",");
				
				System.out.println(arr[1]);
				System.out.println(arr[4]);
				writer.write(arr[1]+":"+arr[4]+"\n");
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
//	@Test
	public void delimPoTest() {
		List<String> pos2=new ArrayList<String>();
		try {
			pos2=Files.readAllLines(new File("insertPOsPt1.sql").toPath());
			for(String line:pos2) {
				
				System.out.println(line.replace("'CUSTOMER'", "NOOO"));
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void formatReviews() {
		List<String> reviews=new ArrayList<String>();

		try {
			reviews=Files.readAllLines(new File("insertReviews1by1.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter writer=null;
		try {
			writer = new FileWriter("formatedReviews.txt"); 
			String command ="INSERT INTO REVIEW (CUSTOMER,BOOK,RATING,TITLE,BODY,CREATED_AT_EPOCH ) VALUES  ";
			writer.write(command);
			for(String line:reviews) {
				String newLine=line.replace(command, "").replace(";", ",\n");

				writer.write(newLine);
				
				

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
//	@Test
	public void addCreditCardToPOs() {
		List<String> inserts= new ArrayList<String>();
		List<String> bookIds= new ArrayList<String>();
		List<String> pos1= new ArrayList<String>();
		List<String> pos2=new ArrayList<String>();
		List<String> customers= new ArrayList<String>();
		List<String> visitorIds=new ArrayList<String>();
		Map<String,String[]> custIdToCard=new LinkedHashMap<String, String[]>();

		try {
			pos2=Files.readAllLines(new File("insertPOsPt2.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			pos1=Files.readAllLines(new File("insertPOsPt2.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			customers=Files.readAllLines(new File("insertCustomers.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String line:customers) {
			String[] atts=line.split(",");
			String customerId=atts[1];
			String cardType=atts[14];
			String cardNumber=atts[15];
			String cardExpiry=atts[16];
			String cardCvv2=atts[17].replace(")", "");		
			String[] card= {cardType,cardNumber,cardExpiry,cardCvv2};
			custIdToCard.put(customerId, card);		
		}

		FileWriter writer=null;
		try {
			writer = new FileWriter("CLEANinsertPOsPt2WithCC.txt"); 
			writer.write("INSERT INTO PURCHASE_ORDER (ID,BOOK,STATUS,AMOUNT,CREATED_AT_EPOCH,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2)	VALUES 	");
			String[] status= {PurchaseOrderSchema.DELIVERED_STATUS,
					PurchaseOrderSchema.ORDERED_STATUS,
					PurchaseOrderSchema.PROCESSED_STATUS,
					PurchaseOrderSchema.SHIPPED_STATUS,
					PurchaseOrderSchema.DENIED_STATUS};
			List<String> ids = new ArrayList<String>();
			for(String line:pos1) {
				String id=line.split(",")[0].replace("(", "")+line.split(",")[1]+line.split(",")[4].replace(")", "");
				System.out.println(id);
				if(ids.contains(id)) {
					System.out.println("NOT EQUAL");
					continue;
				}else {
					ids.add(id);
				}
				String[] cardArr=custIdToCard.get(line.split(",")[0].replace("(", ""));
				String card=
				","+cardArr[0]+","+
				cardArr[1]+","+
				cardArr[2]+","+
				cardArr[3]+
				"),\n";
				String newLine = line.replace("),", card);
				writer.write(newLine);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void generateCarts() {
		List<String> inserts= new ArrayList<String>();
		List<String> bookIds= new ArrayList<String>();
		List<String> customerIds= new ArrayList<String>();
		List<String> visitorIds=new ArrayList<String>();
		List<String> key=new ArrayList<String>();
		try {
			bookIds=Files.readAllLines(new File("bookIdsFINAL.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			customerIds=Files.readAllLines(new File("customerIds.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			visitorIds=Files.readAllLines(new File("visitorIds.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter writer=null;
		try {
			writer = new FileWriter("insertCarts.txt"); 
			writer.write("INSERT INTO CART (ID,BOOK,AMOUNT,USER_TYPE) VALUES "+"\n");
			for(String customerId:customerIds) {
				int numberBooks=((int)(Math.random()*8)+1);

				for(int i=0;i< numberBooks;i++) {
					int bookIndex=(int)(Math.random()*bookIds.size());
					String bookId=bookIds.get(bookIndex);
					if(key.contains(customerId+bookId)) {
						continue;
					}
					key.add(customerId+bookId);
					String amount=Integer.toString((int)(Math.random()*5)+1);
					writer.write("('"+customerId+"','"+bookIds.get(bookIndex)+"',"+amount+",'"+UserTypes.CUSTOMER+"'),\n");	
				}
			}
			
			for(String visitorId:visitorIds) {
				int numberBooks=((int)(Math.random()*8)+1);
				for(int i=0;i< numberBooks;i++) {
					int bookIndex=(int)(Math.random()*bookIds.size());
					String bookId=bookIds.get(bookIndex);
					if(key.contains(visitorId+bookId)) {
						continue;
					}
					key.add(visitorId+bookId);
					String amount=Integer.toString((int)(Math.random()*5)+1);
					writer.write("('"+visitorId+"','"+bookIds.get(bookIndex)+"',"+amount+",'"+UserTypes.VISITOR+"'),\n");	
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	@Test
	public void testVisitorDelim() {
		List<String> visitors= new ArrayList<String>();
		try {
			visitors=Files.readAllLines(new File("insertVisitors.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String line:visitors) {
			String[] atts = line.split(",");
			System.out.println("card");
			System.out.println(atts[1]); //ID
			System.out.println(atts[6]);//email
			System.out.println(atts[7]);//s#
			System.out.println(atts[8]);//st
			System.out.println(atts[9]);//postalcode
			System.out.println(atts[10]);//city
			System.out.println(atts[11]);//province
			System.out.println(atts[12]);//country
			System.out.println(atts[14]);//cardtype
			System.out.println(atts[15]);//cardnum
			System.out.println(atts[16]);//exp
			System.out.println(atts[17].replace(")", ""));//cvv2
			return;
		}
	}
	
	@Test
	public void testCustomerDelim() {
		List<String> customers= new ArrayList<String>();
		try {
			customers=Files.readAllLines(new File("insertCustomers.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String line:customers) {
			String[] atts = line.split(",");
			System.out.println("card");
			System.out.println(atts[1]); //ID
			System.out.println(atts[6]);//email
			System.out.println(atts[7]);//s#
			System.out.println(atts[8]);//st
			System.out.println(atts[9]);//postalcode
			System.out.println(atts[10]);//city
			System.out.println(atts[11]);//province
			System.out.println(atts[12]);//country
			System.out.println(atts[14]);//cardtype
			System.out.println(atts[15]);//cardnum
			System.out.println(atts[16]);//exp
			System.out.println(atts[17].replace(")", ""));//cvv2
			return;
		}
	}
	
	@Test
	public void delimitBookISBNTest() {
		List<String> books=new ArrayList<String>();
		try {
			books=Files.readAllLines(new File("insertBooks.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}		
		FileWriter writer=null;
		try {
			writer = new FileWriter("bookIdToISBN.txt"); 
			for(String line:books) {
				Pattern isbnPattern = Pattern.compile("'([0-9]+)',[0-9]+,[0-9.]+,[0-9]+,[0-9]+\\)");//. represents single character  
				Matcher isbnMatcher = isbnPattern.matcher(line);  
				isbnMatcher.find();
				writer.write(line.substring(2,38)+":"+isbnMatcher.group(1)+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
//	@Test
	public void addISBNTOPo() {
		List<String> bookIdtoISBN=new ArrayList<String>();
		List<String> pos=new ArrayList<String>();
		try {
			bookIdtoISBN=Files.readAllLines(new File("bookIdToISBN.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			pos=Files.readAllLines(new File("PoRaw.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, String> ids = new HashMap<String, String>();

		for(String line:bookIdtoISBN) {
			String[] arr=line.split(":");			
			ids.put(arr[0], arr[1]);
		}
		
		FileWriter writer=null;
		try {
			writer = new FileWriter("PosWithISBNRaw.txt"); 
			for(String line:pos) {
				String ISBN = ids.get(line.substring(41,77));
				String editLine = "('"+ISBN+"'," + line.substring(1);
				writer.write(editLine+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
//	@Test
	public void generatePO() {
		List<String> inserts= new ArrayList<String>();
		List<String> bookIds= new ArrayList<String>();
		List<String> customers= new ArrayList<String>();
		List<String> visitors=new ArrayList<String>();

		try {
			bookIds=Files.readAllLines(new File("bookIdsFINAL.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			customers=Files.readAllLines(new File("insertCustomers.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			visitors=Files.readAllLines(new File("insertVisitors.sql").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter writer=null;
		List<String> duplicates = new ArrayList();
		try {
			writer = new FileWriter("insertPOsPt1.txt"); 
			writer.write("INSERT INTO PURCHASE_ORDER (ID,BOOK,USER_TYPE,EMAIL,STREET_NUMBER,STREET,POSTAL_CODE,CITY,PROVINCE,COUNTRY,STATUS,AMOUNT,CREATED_AT_EPOCH,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2)	VALUES "); 
			String[] status= {PurchaseOrderSchema.DELIVERED_STATUS,
					PurchaseOrderSchema.ORDERED_STATUS,
					PurchaseOrderSchema.PROCESSED_STATUS,
					PurchaseOrderSchema.SHIPPED_STATUS,
					PurchaseOrderSchema.DENIED_STATUS};
			int countIndex = 0;
			Map<Integer, Customer> customersData = new LinkedHashMap();
			for(String line:customers) {
				int numberPos=((int)(Math.random()*8)+1);
				String[] atts=line.split(",");
				String customerId=atts[1];
				String cardType=atts[14];
				String cardNumber=atts[15];
				String cardExpiry=atts[16];
				String cardCvv2=atts[17].replace(")", "");
				String email = atts[6];
				String streetNum = atts[7];
				String street = atts[8];
				String postalCode=atts[9];
				String city=atts[10];
				String province=atts[11];
				String country=atts[12];
				customersData.put(countIndex, new Customer.Builder()
						.withEmail(email)
						.withStreetNumber(streetNum)
						.withStreet(street)
						.withPostalCode(postalCode)
						.withCity(city)
						.withProvince(province)
						.withCountry(country)
						.withCreditCardType(cardType)
						.withCreditCardNumber(cardNumber)
						.withCreditCardCVV2(cardCvv2)
						.withCreditCardExpiry(cardExpiry)
						.build());
				countIndex++;


				
				
				for(int j=0;j< numberPos;j++) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					int poDay=(int)(Math.random()*(27))+1;
					String poDayString = Integer.toString(poDay);
					int poMonth=(int)(Math.random()*11)+1;
					String poMonthString = Integer.toString(poMonth);
					String poDateString = ("2020-"+poMonthString+"-"+poDayString).stripLeading().stripTrailing();
					dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));			
				    Date date=null;
				    long poEpoch =0;
					try {
						date = dateFormat.parse(poDateString);
						poEpoch = date.getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String postatus=status[((int)(Math.random()*5))];
					int numberBooks=((int)(Math.random()*8)+1);
					for(int i=0;i< numberBooks;i++) {
						int bookIndex=(int)(Math.random()*bookIds.size());
						String amount=Integer.toString((int)(Math.random()*5)+1);
						String bookId=bookIds.get(bookIndex);
						String epoch=Long.toString(poEpoch);
						if(duplicates.contains(customerId+bookId+epoch)) continue;
						duplicates.add(customerId+bookId+epoch);
						writer.write("("+customerId+",'"+bookId+
								"','"+UserTypes.CUSTOMER+
								"',"+email+
								","+streetNum+
								","+street+
								","+postalCode+
								","+city+
								","+province+
								","+country+
								",'"+postatus+"',"+
								amount+","+epoch+
								","+cardType+","+
								cardNumber+","+
								cardExpiry+","+
								cardCvv2+
								"),\n");	
					}
				}

			}
			for(String line:visitors) {
				int numberPos=((int)(Math.random()*8)+1);
				int randcust=((int)(Math.random()*customersData.keySet().size()));
				Customer customer =customersData.get(randcust);
				String id = line.split(",")[1];
				String cardType=customer.getCreditCard().getCreditCardType();
				String cardNumber=customer.getCreditCard().getCreditCardNumber();
				String cardExpiry=customer.getCreditCard().getCreditCardExpiry();
				String cardCvv2=customer.getCreditCard().getCreditCardCVV2();
				String email = customer.getEmail();
				String streetNum = customer.getAddress().getNumber();
				String street = customer.getAddress().getStreet();
				String postalCode=customer.getAddress().getPostalCode();
				String city=customer.getAddress().getCity();
				String province=customer.getAddress().getProvince();
				String country=customer.getAddress().getCountry();
				for(int j=0;j< numberPos;j++) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					int poDay=(int)(Math.random()*(27))+1;
					String poDayString = Integer.toString(poDay);
					int poMonth=(int)(Math.random()*11)+1;
					String poMonthString = Integer.toString(poMonth);
					String poDateString = ("2020-"+poMonthString+"-"+poDayString).stripLeading().stripTrailing();
					dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));			
				    Date date=null;
				    long poEpoch =0;
					try {
						date = dateFormat.parse(poDateString);
						poEpoch = date.getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String postatus=status[((int)(Math.random()*5))];
					int numberBooks=((int)(Math.random()*10)+1);
					for(int i=0;i< numberBooks;i++) {
						int bookIndex=(int)(Math.random()*bookIds.size());
						String amount=Integer.toString((int)(Math.random()*5)+1);
						String bookId=bookIds.get(bookIndex);
						String epoch=Long.toString(poEpoch);
						if(duplicates.contains(id+bookId+epoch)) continue;
						duplicates.add(id+bookId+epoch);
						writer.write("("+id+",'"+bookId+
								"','"+UserTypes.VISITOR+
								"',"+email+
								","+streetNum+
								","+street+
								","+postalCode+
								","+city+
								","+province+
								","+country+
								",'"+postatus+"',"+
								amount+","+epoch+
								","+cardType+","+
								cardNumber+","+
								cardExpiry+","+
								cardCvv2+
								"),\n");	
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	@Test
	public void generateVisitors() {
		List<String> customerIds= new ArrayList<String>();
		List<String> visitorIds=new ArrayList<String>();
		try {
			customerIds=Files.readAllLines(new File("customerIds.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}		
		FileWriter writer=null;
		try {
			writer = new FileWriter("visitorIds.txt"); 
			for(String customerId:customerIds) {
				String id =UUID.nameUUIDFromBytes(new StringBuilder(customerId).reverse().toString().getBytes()).toString().stripLeading().stripTrailing();
				writer.write(id+"\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			visitorIds=Files.readAllLines(new File("visitorIds.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer = new FileWriter("insertVisitors.txt"); 
			writer.write("INSERT INTO VISITOR (USER_TYPE,ID,CREATED_AT_EPOCH) VALUES "+"\n");
			for(String visitorId:visitorIds) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				int poDay=(int)(Math.random()*(27))+1;
				String poDayString = Integer.toString(poDay);
				int poMonth=(int)(Math.random()*11)+1;
				String poMonthString = Integer.toString(poMonth);
				String poDateString = ("2020-"+poMonthString+"-"+poDayString).stripLeading().stripTrailing();
				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));			
			    Date date=null;
			    long poEpoch =0;
				try {
					date = dateFormat.parse(poDateString);
					poEpoch = date.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				writer.write("('"+UserTypes.VISITOR+"','"+visitorId+"',"+Long.toString(poEpoch)+"),\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	
	@Test
	public void formatBookInserts() {
		List<String> inserts= new ArrayList<String>();
		List<String> bookIds= new ArrayList<String>();
		List<String> customerIds= new ArrayList<String>();	
		String insertion="";
		try {
			inserts=Files.readAllLines(new File("reviewInserts.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			customerIds=Files.readAllLines(new File("customerIds.txt").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String insertion2="INSERT INTO REVIEW (CUSTOMER,BOOK,RATING,TITLE,BODY,CREATED_AT_EPOCH ) VALUES ";
			String[] statuss = {"PROCESSED","SHIPPED","DENIED","DELIVERED","ORDERED"};
			FileWriter writer=null;
			try {
				writer = new FileWriter("insertReviews.txt"); 
				writer.write("INSERT INTO REVIEW (CUSTOMER,BOOK,RATING,TITLE,BODY,CREATED_AT_EPOCH ) VALUES "+"\n");
				for(String insert:inserts) {
					writer.write(insert+","+"\n");			
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(writer!=null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}		
	}

	public void generateBooksFromLinkFiles() {
		for(Entry<String, List<String>> entry:koboParentLinks.entrySet()) {
			for(String link: entry.getValue()) {
				generateBook(link,entry.getKey());
			}			
		}
	}	

	public void generateBooks() {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		WebDriver driver = new FirefoxDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 3);	
		Map<String, Set<String>> koboParentLinks= new HashMap<String, Set<String>>();
		koboParentLinks.put("adventure", new HashSet<String>());
		koboParentLinks.put("comics", new HashSet<String>());
		koboParentLinks.put("dystopia", new HashSet<String>());
		koboParentLinks.put("historical", new HashSet<String>());
		koboParentLinks.put("horror", new HashSet<String>());
		koboParentLinks.put("science-fiction", new HashSet<String>());
		koboParentLinks.put("artists", new HashSet<String>());
		koboParentLinks.put("business", new HashSet<String>());
		koboParentLinks.put("entertainment", new HashSet<String>());
		koboParentLinks.put("romance", new HashSet<String>());		
		koboParentLinks.get("adventure").add("https://www.kobo.com/ca/en/ebooks/action-adventure-3");
		koboParentLinks.get("adventure").add("https://www.kobo.com/ca/en/ebooks/action-adventure-3?pageNumber=2");
		koboParentLinks.get("adventure").add("https://www.kobo.com/ca/en/ebooks/action-adventure-3?pageNumber=3");
		koboParentLinks.get("adventure").add("https://www.kobo.com/ca/en/ebooks/action-adventure-3?pageNumber=4");
		koboParentLinks.get("comics").add("https://www.kobo.com/ca/en/ebooks/comics-graphic-novels-1");
		koboParentLinks.get("comics").add("https://www.kobo.com/ca/en/ebooks/comics-graphic-novels-1?pageNumber=2");
		koboParentLinks.get("comics").add("https://www.kobo.com/ca/en/ebooks/comics-graphic-novels-1?pageNumber=3");
		koboParentLinks.get("comics").add("https://www.kobo.com/ca/en/ebooks/comics-graphic-novels-1?pageNumber=4");
		koboParentLinks.get("dystopia").add("https://www.kobo.com/ca/en/ebooks/dystopia");
		koboParentLinks.get("dystopia").add("https://www.kobo.com/ca/en/ebooks/dystopia?pageNumber=2");
		koboParentLinks.get("dystopia").add("https://www.kobo.com/ca/en/ebooks/dystopia?pageNumber=3");
		koboParentLinks.get("dystopia").add("https://www.kobo.com/ca/en/ebooks/dystopia?pageNumber=4");
		koboParentLinks.get("historical").add("https://www.kobo.com/ca/en/ebooks/historical-9");
		koboParentLinks.get("historical").add("https://www.kobo.com/ca/en/ebooks/historical-9?pageNumber=2");
		koboParentLinks.get("historical").add("https://www.kobo.com/ca/en/ebooks/historical-9?pageNumber=3");
		koboParentLinks.get("historical").add("https://www.kobo.com/ca/en/ebooks/historical-9?pageNumber=4");
		koboParentLinks.get("horror").add("https://www.kobo.com/ca/en/ebooks/horror-5");
		koboParentLinks.get("horror").add("https://www.kobo.com/ca/en/ebooks/horror-5?pageNumber=2");
		koboParentLinks.get("horror").add("https://www.kobo.com/ca/en/ebooks/horror-5?pageNumber=3");
		koboParentLinks.get("horror").add("https://www.kobo.com/ca/en/ebooks/horror-5?pageNumber=4");
		koboParentLinks.get("science-fiction").add("https://www.kobo.com/ca/en/ebooks/science-fiction-4");
		koboParentLinks.get("science-fiction").add("https://www.kobo.com/ca/en/ebooks/science-fiction-4?pageNumber=2");
		koboParentLinks.get("science-fiction").add("https://www.kobo.com/ca/en/ebooks/science-fiction-4?pageNumber=3");
		koboParentLinks.get("science-fiction").add("https://www.kobo.com/ca/en/ebooks/science-fiction-4?pageNumber=4");
		koboParentLinks.get("artists").add("https://www.kobo.com/ca/en/ebooks/artists-architects-photographers");
		koboParentLinks.get("artists").add("https://www.kobo.com/ca/en/ebooks/artists-architects-photographers?pageNumber=2");
		koboParentLinks.get("artists").add("https://www.kobo.com/ca/en/ebooks/artists-architects-photographers?pageNumber=3");
		koboParentLinks.get("artists").add("https://www.kobo.com/ca/en/ebooks/artists-architects-photographers?pageNumber=4");
		koboParentLinks.get("business").add("https://www.kobo.com/ca/en/ebooks/business");
		koboParentLinks.get("business").add("https://www.kobo.com/ca/en/ebooks/business?pageNumber=2");
		koboParentLinks.get("business").add("https://www.kobo.com/ca/en/ebooks/business?pageNumber=3");
		koboParentLinks.get("business").add("https://www.kobo.com/ca/en/ebooks/business?pageNumber=4");
		koboParentLinks.get("entertainment").add("https://www.kobo.com/ca/en/ebooks/entertainment-performing-arts");
		koboParentLinks.get("entertainment").add("https://www.kobo.com/ca/en/ebooks/entertainment-performing-arts?pageNumber=2");
		koboParentLinks.get("entertainment").add("https://www.kobo.com/ca/en/ebooks/entertainment-performing-arts?pageNumber=3");
		koboParentLinks.get("entertainment").add("https://www.kobo.com/ca/en/ebooks/entertainment-performing-arts?pageNumber=4");
		koboParentLinks.get("romance").add("https://www.kobo.com/ca/en/ebooks/contemporary-1");
		koboParentLinks.get("romance").add("https://www.kobo.com/ca/en/ebooks/contemporary-1?pageNumber=2");
		koboParentLinks.get("romance").add("https://www.kobo.com/ca/en/ebooks/contemporary-1?pageNumber=3");
		koboParentLinks.get("romance").add("https://www.kobo.com/ca/en/ebooks/contemporary-1?pageNumber=4");

		Map<String, Set<String>> koboCategoryLinks= new HashMap<String, Set<String>>();
		koboCategoryLinks.put("adventure", new HashSet<String>());
		koboCategoryLinks.put("comics", new HashSet<String>());
		koboCategoryLinks.put("dystopia", new HashSet<String>());
		koboCategoryLinks.put("historical", new HashSet<String>());
		koboCategoryLinks.put("horror", new HashSet<String>());
		koboCategoryLinks.put("science-fiction", new HashSet<String>());
		koboCategoryLinks.put("artists", new HashSet<String>());
		koboCategoryLinks.put("business", new HashSet<String>());
		koboCategoryLinks.put("entertainment", new HashSet<String>());
		koboCategoryLinks.put("romance", new HashSet<String>());
		
		for(Entry<String, Set<String>> entry:koboParentLinks.entrySet()) {
			for(String parentLink:entry.getValue()) {
				driver.get(parentLink);
				wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
				Set<String> links=	driver.findElements(By.tagName("a"))
						.stream()
						.map(element->element.getAttribute("href"))
						.filter(link->link!=null)
						.filter(link->link.contains("ebook"))
						.filter(link->!link.contains("ebooks"))
						.filter(link->!link.contains("facebook"))
						.filter(link->!link.contains("?"))
						.collect(Collectors.toSet());
				for(String link:links) {
					boolean linkExists=false;
					for(Entry<String, Set<String>> entryLink :koboCategoryLinks.entrySet()) {
						if(entryLink.getValue().contains(link)) {
							linkExists=true;
							break;
						}
					}
					if(!linkExists) {
						koboCategoryLinks.get(entry.getKey()).add(link);
					}
				}
			}
		}
		driver.close();
		driver.quit();
		
		koboCategoryLinks.entrySet()
			.stream()
			.map(entry -> entry.getValue())
			.forEach(System.out::println);
		for(Entry<String, Set<String>> entry:koboCategoryLinks.entrySet()) {
			FileWriter writer = null;
			  try {
				  writer = new FileWriter(entry.getKey()+".txt",true);				
				for(String link: entry.getValue()) {
					writer.write(link + "\n");
				}

				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					if(writer!=null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}		
		}
		
		for(Entry<String, Set<String>> entry:koboCategoryLinks.entrySet()) {
			for(String link: entry.getValue()) {
				generateBook(link,entry.getKey());
			}			
		}
	}
	
	@Test	
	public void generateBook(String bookLink,String category) {
		if(completedLinks.contains(bookLink)) return;
		System.out.println("link: "+bookLink);
		FileWriter writer = null;
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		WebDriver driver = new FirefoxDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 3);		
		driver.get(bookLink);
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
		String source = driver.getPageSource();
		
		String title = "";
		try {
			title = driver.findElement(By.cssSelector("h2.product-field:nth-child(2)")).getText().stripLeading().stripTrailing();

		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}	catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if( title==null||title.isEmpty()) {
				title = driver.findElement(By.cssSelector("h2.title:nth-child(3)")).getText().stripLeading().stripTrailing();
			}

		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}	catch (Exception e) {
			e.printStackTrace();
		}	
		try {
			if( title==null||title.isEmpty()) {
				title = driver.findElement(By.cssSelector("title product-field")).getText().stripLeading().stripTrailing();
			}

		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}	catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if( title==null||title.isEmpty()) {
				title = driver.findElement(By.cssSelector("title")).getText().stripLeading().stripTrailing();
			}
		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}	catch (Exception e) {
			e.printStackTrace();
		}
		if( title==null||title.isEmpty()) {
		title = driver.findElement(By.className("title")).getText().stripLeading().stripTrailing();
		Pattern titlePattern = Pattern.compile("\\s*\"@type\":\\s*\"Book\",\\s*\"name\":\\s*\"(.+)\",");
		Matcher titleMatcher = titlePattern.matcher(source);
		title = "";
			while(titleMatcher.find()) {
				title=titleMatcher.group(1).stripLeading().stripTrailing();
				if(title !=null || !title.isEmpty()) break;
			}
		}
		String author = driver.findElement(By.className("contributor-name")).getText().stripLeading().stripTrailing();
		String series="";
			try {
				series = driver.findElement(By.className("book-series")).getText().stripLeading().stripTrailing();
				if( series==null||series.isEmpty()) {
				series = driver.findElement(By.cssSelector(".product-sequence-field > a:nth-child(1)")).getText().stripLeading().stripTrailing();
				}

			}catch(NoSuchElementException e) {
				e.printStackTrace();
			}	catch (Exception e) {
				e.printStackTrace();
			}		
			String isbn = "";
			Pattern isbnPattern = Pattern.compile("ISBN:\\s*<span\\s*translate=\"no\">(\\d+)</span>");
			Matcher isbnMatcher = isbnPattern.matcher(source);
			if(isbnMatcher.find()) {
				isbn=isbnMatcher.group(1);
			}
			String price = "";
	        
			Pattern pricePattern = Pattern.compile("<span\\s*class=\"price\">\\$([0-9]+.99)</span>");
			Matcher priceMatcher = pricePattern.matcher(source);
			while(priceMatcher.find()) {
				price=priceMatcher.group(1).replace("$", "").replaceAll("\\s+", "").stripLeading().stripTrailing();
				if(price !=null || !price.isEmpty()) break;
			}
			if(price ==null || price.isEmpty()) {
				try {
					price=driver.findElement(By.className("price")).getText().replace("$", "").replaceAll("\\s+", "").stripLeading().stripTrailing();
				}catch(NoSuchElementException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if(price ==null || price.isEmpty()) 
						price = driver.findElement(By.cssSelector("div.pricing-details:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1)")).getText().replace("$", "").replaceAll("\\s+", "").stripLeading().stripTrailing();
				}catch(NoSuchElementException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if(price ==null || price.isEmpty()) 
					price = driver.findElement(By.cssSelector("div.pricing-details:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > span:nth-child(1)")).getText().replace("$", "").replaceAll("\\s+", "").stripLeading().stripTrailing();
				}catch(NoSuchElementException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			if(price ==null || price.isEmpty())
				price=Integer.toString((int)(Math.random()*10)+1)+"."+"99";
		String releaseDate ="";
		if(driver.findElement(By.cssSelector(".bookitem-secondary-metadata > ul:nth-child(2) > li:nth-child(2) > span:nth-child(1)")).isDisplayed()) {
			releaseDate = driver.findElement(By.cssSelector(".bookitem-secondary-metadata > ul:nth-child(2) > li:nth-child(2) > span:nth-child(1)")).getText().stripLeading().stripTrailing();
		}		
		String releaseYear = releaseDate==null||releaseDate.isEmpty()?Integer.toString((int)(Math.random()*50)+1970) :releaseDate.replaceAll("[A-Za-z]+ [0-9]+, ", "").stripLeading().stripTrailing();
		String coverUrl = driver.findElement(By.className("cover-image")).getAttribute("src").stripLeading().stripTrailing();
		String descriptionRaw = driver.findElement(By.cssSelector(".synopsis-description")).getText().stripLeading().stripTrailing();
		if(descriptionRaw==null|| descriptionRaw.isEmpty())
			descriptionRaw=driver.findElement(By.cssSelector(".synopsis-description > p:nth-child(1)")).getText().stripLeading().stripTrailing();
		if(descriptionRaw==null|| descriptionRaw.isEmpty())
			descriptionRaw=driver.findElement(By.cssSelector(".synopsis-description > p:nth-child(1) > strong:nth-child(1)")).getText().stripLeading().stripTrailing();
		String[] descriptionRawLines=descriptionRaw.split("\\n");
		String description = "";
		for(int i=1;i<descriptionRawLines.length;i++) {
			description+=descriptionRawLines[i];
			description+=" ";
		}
		List<WebElement> reviewElements = new ArrayList<WebElement>();
		try {
			if(driver.findElement(By.className("review-item-wrapper")).isDisplayed()) {
				reviewElements = driver.findElements(By.className("review-item-wrapper"));
			}
		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		int loads=0;
		if(reviewElements.isEmpty()) {
			try {
				while(driver.findElement(By.cssSelector("#load-more-reviews")).isDisplayed()||loads==4) {
					if(driver.findElement(By.cssSelector("#load-more-reviews")).isDisplayed()) {
						driver.findElement(By.cssSelector("#load-more-reviews")).click();
					}
					loads++;
				}
			}catch(NoSuchElementException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}	
			try {
				reviewElements = driver.findElements(By.className("review-item-wrapper"));
			}catch(NoSuchElementException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}	

		}
		String idInput=isbn+releaseYear;
		String id =UUID.nameUUIDFromBytes(idInput.getBytes()).toString().stripLeading().stripTrailing();

		for(WebElement review: reviewElements) {
			int reviewDay=(int)(Math.random()*27)+1;
			String reviewDayString=Integer.toString(reviewDay);
			int reviewMonth=(int)(Math.random()*11)+1;
			String reviewMonthString=Integer.toString(reviewDay);
			String dateString = ("2020-"+reviewMonthString+"-"+reviewDayString).stripLeading().stripTrailing();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			int customerCount= customers.size();
			System.out.println(customerCount);
			int customerIndex=(int)(Math.random()*customerCount);
			System.out.println(customerIndex);
			String userId=customers.get(customerIndex);
			int amountPO = (int)(Math.random()*3)+1;
			int poDay=(int)(Math.random()*(reviewDay-1))+1;
			String poDayString = Integer.toString(poDay);
			int poMonth=(int)(Math.random()*reviewMonth)+1;
			String poMonthString = Integer.toString(poMonth);
			String poDateString = ("2020-"+poMonthString+"-"+poDayString).stripLeading().stripTrailing();
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));			
		    Date date=null;
		    long utc =0;
		    long poUtc =0;
			try {
				date = dateFormat.parse(dateString);
				poUtc = date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				date = dateFormat.parse(dateString);
				 utc = date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String reviewRating =review.findElement(By.className("rating-stars")).getAttribute("aria-label").replace(" out of 5","").replaceAll("[A-Za-z\\s]+", "").stripLeading().stripTrailing();
			String reviewTitle=review.findElement(By.className("review-title")).getText().stripLeading().stripTrailing();
			String reviewText=review.findElement(By.className("review-text")).getText().stripLeading().stripTrailing();
			String insertReview = "('"+userId+"','"+id+"',"+reviewRating+",'"+reviewTitle+"','"+reviewText+"',"+Long.toString(utc)+")";			
			String purchaseOrder= "('"+userId+"','"+id+"','COMPLETED',"+amountPO+","+Long.toString(poUtc)+")";			
			  try {
					writer = new FileWriter("poInserts.txt",true); 
					writer.write(purchaseOrder + "\n");
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					writer = new FileWriter("poId.txt",true); 
					writer.write(userId+":"+poUtc+ "\n");
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					writer = new FileWriter("reviewInserts.txt",true); 
					writer.write(insertReview + "\n");
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					writer = new FileWriter("reviewIds.txt",true); 
					writer.write(userId+":"+id+ "\n");

				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					if(writer!=null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		}
		String rating="0";
		String amountSold="0";
		 URL imageURL=null;
		 BufferedImage saveImage=null;
		 String imageFileName="NULL";
		try {
			imageURL = new URL(coverUrl);
			saveImage = ImageIO.read(imageURL);
			imageFileName=(title+"_"+isbn).replaceAll("\\s+", "_").replaceAll("\\W+","")+".jpg";
			 ImageIO.write(saveImage, "jpg", new File(".."+File.separator+".."+File.separator+"database"+File.separator+"book_images"+File.separator+"covers"+File.separator+imageFileName));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String insertValue="('"+id+"'"+",'"+title+"','"+series+"','"+description+"','"+category+"','"+author+"','"+imageFileName+"','"+isbn+"',"+releaseYear+","+price+","+rating+","+amountSold+")";
		System.out.println(insertValue);
		  try {
			writer = new FileWriter("bookInserts.txt",true); 
			writer.write(insertValue + "\n");
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer = new FileWriter("bookIds.txt",true); 
			writer.write(id + "\n");


		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	  

			  try {
				  writer = new FileWriter("completedLinks.txt",true);		
				  writer.write(bookLink + "\n");			

				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					if(writer!=null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}		  
				driver.close();		
	}
	
	public void generateCustomers() {
		String update ="INSERT INTO CUSTOMER (ID,GIVENNAME,SURNAME,USERNAME,PASSWORD ,EMAIL,STREET_NUMBER,STREET,POSTAL_CODE,CITY,PROVINCE,COUNTRY,UTC,CREDIT_CARD,CREDIT_CARD_NUMBER,CREDIT_CARD_EXPIRY,CREDIT_CARD_CVV2) VALUES";		
		Map<Integer,String> emailServices = new HashMap<Integer, String>();
		emailServices.put(0, "gmail");
		emailServices.put(1, "yahoo");
		emailServices.put(2, "live");
		emailServices.put(3, "icloud");
		emailServices.put(4, "outlook");
		emailServices.put(5, "protonmail");
		List<String> namesAdded = new ArrayList<String>();
		List<String> customerInserstions = new ArrayList<String>();
		List<String> customerIds = new ArrayList<String>();
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

		for(int i=0;i<100;i++) {
			WebDriver driver=null;
			try {
			FirefoxOptions options = new FirefoxOptions();
			options.setHeadless(true);
			driver = new FirefoxDriver(options);
			driver.get("https://www.fakeaddressgenerator.com/World/ca_address_generator");
			WebDriverWait wait = new WebDriverWait(driver, 10);	
			wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
			wait.wait(2);
			String fullName = driver.findElement(By.cssSelector(".table > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > strong:nth-child(1)")).getText();
			if(!namesAdded.contains(fullName)) {
				namesAdded.add(fullName);
				int nameWordCount=fullName.split(" ").length;
				String firstName=fullName.split(" ")[0].stripLeading().stripTrailing();
				String lastName=fullName.split(" ")[nameWordCount-1].stripLeading().stripTrailing();
				String address = driver.findElement(By.cssSelector(".detail > div:nth-child(3) > div:nth-child(4) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String streetNumber = address.replaceAll("\\D", "").stripLeading().stripTrailing();
				String userName= ((firstName.length()>2?firstName.substring(0, 1):firstName)+lastName+streetNumber).stripLeading().stripTrailing();
				String password=(firstName+"password").stripLeading().stripTrailing();
				String email=(userName+"@"+emailServices.get((int)(Math.random()*5))+".com").stripLeading().stripTrailing();
				String street = address.replaceAll("\\d", "").stripLeading().stripTrailing();
				String city = driver.findElement(By.cssSelector(".detail > div:nth-child(3) > div:nth-child(5) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String province = driver.findElement(By.cssSelector("div.row:nth-child(6) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String postalCode = driver.findElement(By.cssSelector("div.row:nth-child(7) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String creditCardType = driver.findElement(By.cssSelector("div.row:nth-child(26) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String creditCardNumber = driver.findElement(By.cssSelector("div.row:nth-child(27) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String cvv2 = driver.findElement(By.cssSelector("div.row:nth-child(28) > div:nth-child(2) > strong:nth-child(1) > input:nth-child(1)")).getAttribute("value").stripLeading().stripTrailing();
				String creditCardExpiry= driver.findElement(By.cssSelector("#creditCard")).getAttribute("value").stripLeading().stripTrailing();
				String id =UUID.nameUUIDFromBytes(userName.getBytes()).toString().stripLeading().stripTrailing();
				String country ="Canada";
				String dateStringRaw=driver.findElement(By.cssSelector(".table > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2) > strong:nth-child(1)")).getText().stripLeading().stripTrailing();
				String[] dateComponents=dateStringRaw.split("/");
				String dateString = ("2020-"+dateComponents[0]+"-"+dateComponents[1]).stripLeading().stripTrailing();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			    Date date;
			    long utc =0;
				try {
					date = dateFormat.parse(dateString);
					 utc = date.getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean emptyData=
						id==null || id.isEmpty() ||
						firstName==null  || firstName.isEmpty() ||
						lastName==null  || lastName.isEmpty() ||
						userName==null  || userName.isEmpty() ||
						password==null  || password.isEmpty() ||
						email==null  || email.isEmpty() ||
						streetNumber==null  || streetNumber.isEmpty() ||
						street==null  || street.isEmpty() ||
						postalCode==null  || postalCode.isEmpty() ||
						city==null  || city.isEmpty() ||
						province==null  || province.isEmpty() ||
						country==null  || country.isEmpty() ||
						utc==0 ||
						creditCardType==null  || creditCardType.isEmpty() ||
						creditCardNumber==null  || creditCardNumber.isEmpty() ||
						creditCardExpiry==null  || creditCardExpiry.isEmpty() ||
						cvv2==null  || cvv2.isEmpty();
		   
			
				if(!emptyData) {
					customerInserstions.add("("+
							id+","+
							firstName+","+
							lastName+","+
							userName+","+
							password+","+
							email+","+
							streetNumber+","+
							street+","+
							postalCode+","+
							city+","+
							province+","+
							country+","+
							utc+","+
							creditCardType+","+
							creditCardNumber+","+
							creditCardExpiry+","+
							cvv2+")");
					customerIds.add(id);
				}
			}
		}catch(NoSuchElementException e) {
				e.printStackTrace();
		}catch(NoSuchWindowException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(driver!=null) {
				driver.close();
			}
		}	
	}
		customerInserstions.forEach(System.out::println);
		customerIds.forEach(System.out::println);
		FileWriter writer = null;
		  try {
			writer = new FileWriter("sqlinserts.txt",true); 
			for(String str: customerInserstions) {
			  writer.write(str + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		  try {
			writer = new FileWriter("ids.txt",true); 
			for(String str: customerIds) {
			  writer.write(str + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
		}

	public void fixInputs() {
		List<String> formatLines = new ArrayList<String>();
		try {
			List<String> lines = Files.readAllLines(new File("formatInserts.txt").toPath());


			for(String line:lines) {
				String formatLine = "";
				String[] items=line.split(",");
				for(int i=0;i<items.length;i++) {
					if(i==items.length-1) {
						String itemRemoveTail = items[i].substring(0, items[i].length()-1);
						formatLine += "'"+itemRemoveTail+"'),";
					}else if(i!=0 && i!=1 && i!=13){
						String quote = "'"+items[i]+"',";
						formatLine+=quote;
					}else {
				
						formatLine+=items[i]+",";
					}
				}
				formatLines.add(formatLine);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter("finalInserts.txt",true); 
			for(String str: formatLines) {
			  writer.write(str + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
//	@Test 
	public void fixEmails(){
		Map<Integer,String> emailServices = new HashMap<Integer, String>();
		emailServices.put(0, "gmail");
		emailServices.put(1, "yahoo");
		emailServices.put(2, "live");
		emailServices.put(3, "icloud");
		emailServices.put(4, "outlook");
		emailServices.put(5, "protonmail");
		List<String> fixedLines = new ArrayList<String>();
		
		try {
			List<String> lines = Files.readAllLines(new File("finalInserts.txt").toPath());
			for(String line:lines) {
				fixedLines.add(line.replace("@gmail","@"+ emailServices.get((int)(Math.random()*5))));	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		FileWriter writer = null;
		try {
			writer = new FileWriter("finalInsertsFixed.txt",true); 
			for(String str: fixedLines) {
			  writer.write(str + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
