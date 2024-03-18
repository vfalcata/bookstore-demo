/** ---------------------------------------------
 * Handle loading search request and pagination
 --------------------------------------------- */
 
window.onload = setAllVars;

const NUM_REV_PER_PAGE = 4;

var page_num = 1;
var nextP;
var prevP;

function setAllVars() {
	page_num = 1;
	
	nextP = document.getElementById("nextP");
	prevP = document.getElementById("prevP");
	
	nextP.addEventListener("mouseover", mouseOver_next, false);
	nextP.addEventListener("mouseout", mouseOut_next, false);
	prevP.addEventListener("mouseover", mouseOver_prev, false);
	prevP.addEventListener("mouseout", mouseOut_prev, false);
	
	showNextPage(page_num);
	
	setTotalNumPages();
}

function setTotalNumPages() {
	var numBooks = document.getElementsByClassName("book_row");	
	var numPages = Math.ceil(numBooks.length/ NUM_REV_PER_PAGE);
	
	if (numPages == 0)
		document.getElementById("totalNumPages").textContent = 1;
	else
		document.getElementById("totalNumPages").textContent = numPages;
}

function showNextPageResults(n) {
  var numBooks = document.getElementsByClassName("book_row");	
  var numPages = Math.ceil(numBooks.length/ 4);// 4 in each book_row, 4 rows total
  
  console.log("num pages = "+numPages + ", currently on page: "+ page_num + ". Number fo books = "+numBooks)
  
  if (page_num + n <= numPages && page_num + n >= 1)
  	showNextPage(page_num += n);
}

// mouse hover listeners
function mouseOver_next() {
  var numBooks = document.getElementsByClassName("book_row");	
  var numPages = Math.ceil(numBooks.length/ 4);// 4 in each book_row, 4 rows total
  
  if (page_num >= numPages)
  	nextP.setAttribute("style", "color:grey;")
  else 
  	nextP.setAttribute("style", "color:red;text-decoration:underline;")
  
}

function mouseOut_next() {
  nextP.setAttribute("style", "color:#748df4; text-decoration:none;")
}

function mouseOver_prev() {
  var numBooks = document.getElementsByClassName("book_row");	
  var numPages = Math.ceil(numBooks.length/ 4);// 4 in each book_row, 4 rows total
  
  if (page_num <= 1)
    prevP.setAttribute("style", "color:grey;")	
  else 
    prevP.setAttribute("style", "color:red; text-decoration:underline;")
  
}

function mouseOut_prev() {
  prevP.setAttribute("style", "color:#748df4; text-decoration:none;")
}

function showNextPage(n) {
	var numBooks = document.getElementsByClassName("book_row");	
	var numPages = Math.ceil(numBooks.length/ 4);// 4 in each book_row, 4 rows total
	
	var i;
	var counter;
	var page = n;
	
	// hide all rows of books
	for (i = 0; i < numBooks.length; i++) {
    	numBooks[i].style.display = "none";  
 	 }
	
	// Adjust page number if there exists overflow or underflow
	if (page  > numPages) {page_num = 1; page = 1;}
  	if (page  < 1) {page_num = numPages; page = numPages;}	
	
	var rowOfBooksInPage = (page-1)*4;
	//if (rowOfBooksInPage > 0) rowOfBooksInPage = rowOfBooksInPage-1;
	
	// 4 rows per page
	for (counter = 0; counter < 4; counter ++) {
		
		if (rowOfBooksInPage > numBooks.length-1) {break;}
	    if (rowOfBooksInPage < 0) {break;}
		
		try {
			numBooks[rowOfBooksInPage].style.display = "inline";
		} catch(err) {}
		
		rowOfBooksInPage++;
	}
	console.log("num pages = "+page_num);
	document.getElementById("pageNum").textContent = page_num;
	/*
	if (page_num >= numPages)
		document.getElementById("nextP").style.display = "none";
	else
		document.getElementById("nextP").style.display = "inline";
		
	if (page_num <= 1)
		document.getElementById("prevP").style.display = "none";
	else
		document.getElementById("prevP").style.display = "inline";
		*/
}