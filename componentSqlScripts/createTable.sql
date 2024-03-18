CREATE TABLE SITE_USER(
 ID varchar(100) not null,
 USER_TYPE varchar(20) not null,
 PRIMARY KEY(ID,USER_TYPE),
 UNIQUE(ID),
 CONSTRAINT type_check CHECK (USER_TYPE IN('VISITOR','CUSTOMER'))
);


CREATE  TABLE CUSTOMER (
  ID varchar(100) not null,
  USER_TYPE varchar(20) not null,
  GIVENNAME varchar (100) not null,
  SURNAME varchar (100),
  USERNAME varchar (20) not null constraint unique_username unique,
  PASSWORD varchar (20) not null,
  EMAIL varchar (50) not null constraint unique_email unique,
  STREET_NUMBER varchar (20),
  STREET varchar (100),
  POSTAL_CODE varchar (10),
  CITY varchar (50),
  PROVINCE varchar (50),
  COUNTRY varchar (50),
  CREATED_AT_EPOCH bigint,
  CREDIT_CARD varchar (20),
  CREDIT_CARD_NUMBER varchar (50),
  CREDIT_CARD_EXPIRY varchar (10),
  CREDIT_CARD_CVV2 varchar (10),
  PRIMARY KEY(ID),
  FOREIGN KEY (ID,USER_TYPE) REFERENCES SITE_USER(ID,USER_TYPE)
  );	

CREATE TABLE VISITOR (
  ID varchar(100) not null,
  USER_TYPE varchar(20) not null,
  CREATED_AT_EPOCH bigint not null,
  PRIMARY KEY(ID),
  FOREIGN KEY (ID,USER_TYPE) REFERENCES SITE_USER(ID,USER_TYPE)
  );


CREATE  TABLE BOOK (
  ID varchar(100) not null,
  TITLE varchar (100) not null,
  SERIES varchar (100),
  DESCRIPTION varchar (5000),
  CATEGORY varchar (100),
  AUTHOR varchar (100),
  COVER varchar (100),
  ISBN varchar (100) not null,
  PUBLISH_YEAR int,
  PRICE DOUBLE,
  RATING DOUBLE not null,
  AMOUNT_SOLD int not null,
  PRIMARY KEY(ID),
  CONSTRAINT amount_sold_positive CHECK (AMOUNT_SOLD>=0),
  CONSTRAINT rating_upper_bound_book CHECK (RATING<=5),
  CONSTRAINT rating_lower_bound_book CHECK (RATING>=0)
  );

  
  
  CREATE  TABLE REVIEW (
  SITE_USER varchar(100) not null,
  USER_TYPE varchar(20) not null,
  NAME varchar (20) not null,
  BOOK varchar (100) not null,
  RATING DOUBLE not null,
  TITLE varchar (100) not null,
  BODY varchar (5000) not null,
  CREATED_AT_EPOCH bigint not null,
  PRIMARY KEY(SITE_USER,BOOK),
  FOREIGN KEY (BOOK) REFERENCES BOOK(ID),
  FOREIGN KEY (SITE_USER) REFERENCES SITE_USER(ID),
  CONSTRAINT rating_upper_bound_review CHECK (RATING<=5),
  CONSTRAINT rating_lower_bound_review CHECK (RATING>=0)
  );

CREATE  TABLE CART (
  ID varchar(100) not null,
  BOOK varchar (100) not null,
  AMOUNT int not null,
  PRIMARY KEY(ID,BOOK),
  FOREIGN KEY (ID) REFERENCES CUSTOMER(ID),
  FOREIGN KEY (BOOK) REFERENCES BOOK(ID)
  );

CREATE  TABLE PURCHASE_ORDER(
  ID varchar(100) not null,
  BOOK varchar (100) not null,	
  STATUS varchar (50) not null,
  AMOUNT int not null,
  EMAIL varchar (50) not null,
  ISBN varchar (100) not null,
  CREDIT_CARD varchar (20) not null,
  CREDIT_CARD_NUMBER varchar (50) not null,
  CREDIT_CARD_EXPIRY varchar (10) not null,
  CREDIT_CARD_CVV2 varchar (10) not null,
  STREET_NUMBER varchar (20) not null,
  STREET varchar (100) not null,
  POSTAL_CODE varchar (10) not null,
  CITY varchar (50) not null,
  PROVINCE varchar (50) not null,
  COUNTRY varchar (50) not null,
  CREATED_AT_EPOCH bigint not null,
  PRIMARY KEY(ID,CREATED_AT_EPOCH,BOOK),
  FOREIGN KEY (ID) REFERENCES CUSTOMER(ID),
  FOREIGN KEY (BOOK) REFERENCES BOOK(ID),
  CONSTRAINT type_check_status_purchase_order CHECK (STATUS IN('PROCESSED','SHIPPED','DENIED','DELIVERED','ORDERED'))
  );