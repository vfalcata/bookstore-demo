/**
 * 
 */

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
	var numOrders = document.getElementsByClassName("entry");	
	var numPages = Math.ceil(numOrders.length/ NUM_REV_PER_PAGE);
	
	if (numPages == 0)
		document.getElementById("totalNumPages").textContent = 1;
	else
		document.getElementById("totalNumPages").textContent = numPages;
}


function showNextPageOrders(n) {
  var numOrders = document.getElementsByClassName("entry");	
  var numPages = Math.ceil(numOrders.length/ NUM_REV_PER_PAGE);
  
  console.log("num pages = "+numPages + ", currently on page: "+ page_num + ". Number of books = "+numOrders)
  
  if (page_num + n <= numPages && page_num + n >= 1)
  	showNextPage(page_num += n);
}

// mouse hover listeners
function mouseOver_next() {
  var numOrders = document.getElementsByClassName("entry");	
  var numPages = Math.ceil(numOrders.length/ NUM_REV_PER_PAGE);
  
  if (page_num >= numPages)
  	nextP.setAttribute("style", "color:grey;")
  else 
  	nextP.setAttribute("style", "color:red;text-decoration:underline;")
  
}

function mouseOut_next() {
  nextP.setAttribute("style", "color:#748df4; text-decoration:none;")
}

function mouseOver_prev() {
  var numOrders = document.getElementsByClassName("entry");	
  var numPages = Math.ceil(numOrders.length/ NUM_REV_PER_PAGE);
  
  if (page_num <= 1)
    prevP.setAttribute("style", "color:grey;")	
  else 
    prevP.setAttribute("style", "color:red; text-decoration:underline;")
  
}

function mouseOut_prev() {
  prevP.setAttribute("style", "color:#748df4; text-decoration:none;")
}

function showNextPage(n) {
	var numOrders = document.getElementsByClassName("entry");	
	var numPages = Math.ceil(numOrders.length/ NUM_REV_PER_PAGE);
	
	var i;
	var counter;
	var page = n;
	
	// hide all reviews
	for (i = 0; i < numOrders.length; i++) {
    	numOrders[i].style.display = "none";  
 	 }
	
	// Adjust page number if there exists overflow or underflow
	if (page  > numPages) {page_num = 1; page = 1;}
  	if (page  < 1) {page_num = numPages; page = numPages;}	
	
	var index = (page-1)*NUM_REV_PER_PAGE;
	//if (index > 0) index = index-1;
	
	for (counter = 0; counter < NUM_REV_PER_PAGE; counter ++) {
		
		if (index > numOrders.length-1) {break;}
	    if (index < 0) {break;}
		
		try {
			numOrders[index].style.display = "";
		} catch(err) {}
		
		index++;
	}
	console.log("num pages = "+page_num);
	document.getElementById("pageNum").textContent = page_num;
}