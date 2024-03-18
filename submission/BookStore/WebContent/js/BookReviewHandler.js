/** ---------------------------------------------
 * Handle loading search request and pagination
 --------------------------------------------- */
 
window.onload = setAllVars;

const NUM_REV_PER_PAGE = 10;

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
	var numReviews = document.getElementsByClassName("review_row");	
	var numPages = Math.ceil(numReviews.length/ NUM_REV_PER_PAGE);
	
	if (numPages == 0)
		document.getElementById("totalNumPages").textContent = 1;
	else
		document.getElementById("totalNumPages").textContent = numPages;
}

function showNextPageReviews(n) {
  var numReviews = document.getElementsByClassName("review_row");	
  var numPages = Math.ceil(numReviews.length/ NUM_REV_PER_PAGE);
  
  console.log("num pages = "+numPages + ", currently on page: "+ page_num + ". Number fo books = "+numReviews)
  
  if (page_num + n <= numPages && page_num + n >= 1)
  	showNextPage(page_num += n);
}

// mouse hover listeners
function mouseOver_next() {
  var numReviews = document.getElementsByClassName("review_row");	
  var numPages = Math.ceil(numReviews.length/ NUM_REV_PER_PAGE);
  
  if (page_num >= numPages)
  	nextP.setAttribute("style", "color:grey;")
  else 
  	nextP.setAttribute("style", "color:red;text-decoration:underline;")
  
}

function mouseOut_next() {
  nextP.setAttribute("style", "color:#748df4; text-decoration:none;")
}

function mouseOver_prev() {
  var numReviews = document.getElementsByClassName("review_row");	
  var numPages = Math.ceil(numReviews.length/ NUM_REV_PER_PAGE);// 4 in each review_row, 4 rows total
  
  if (page_num <= 1)
    prevP.setAttribute("style", "color:grey;")	
  else 
    prevP.setAttribute("style", "color:red; text-decoration:underline;")
  
}

function mouseOut_prev() {
  prevP.setAttribute("style", "color:#748df4; text-decoration:none;")
}

function showNextPage(n) {
	var numReviews = document.getElementsByClassName("review_row");	
	var numPages = Math.ceil(numReviews.length/ NUM_REV_PER_PAGE);
	
	var i;
	var counter;
	var page = n;
	
	// hide all reviews
	for (i = 0; i < numReviews.length; i++) {
    	numReviews[i].style.display = "none";  
 	 }
	
	// Adjust page number if there exists overflow or underflow
	if (page  > numPages) {page_num = 1; page = 1;}
  	if (page  < 1) {page_num = numPages; page = numPages;}	
	
	var review_index = (page-1)*NUM_REV_PER_PAGE;
	//if (review_index > 0) review_index = review_index-1;
	
	for (counter = 0; counter < NUM_REV_PER_PAGE; counter ++) {
		
		if (review_index > numReviews.length-1) {break;}
	    if (review_index < 0) {break;}
		
		try {
			numReviews[review_index].style.display = "inline";
		} catch(err) {}
		
		review_index++;
	}
	console.log("num pages = "+page_num);
	document.getElementById("pageNum").textContent = page_num;
}