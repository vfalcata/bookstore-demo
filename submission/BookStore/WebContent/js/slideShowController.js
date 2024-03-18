/********************************************************************
 *   This file handles the product sliders in the main page
 *   of this website.  
 ********************************************************************/


var slideIndexDeal = 1;			// index for today's deals
var slideIndexTop = 1;			// index for best sellers
var slideIndexRating = 1;		// index for top rating books
var slideIndexProfile = 1;		// index for profile recommendation
var slideIndexComedy = 1;		// index for comedy genre
var slideIndexDrama = 1;		// index for drama genre
var slideIndexFiction = 1;		// index for fiction genre
var slideIndexNonFiction = 1;	// index for non fiction genre
var slideIndexHorror = 1;		// index for horror genre

// ----------------------------------------------------------------
// initialize the view of each slider
// ----------------------------------------------------------------
showSlidesDeal(slideIndexDeal);
showSlidesTop(slideIndexTop);
showSlidesRating(slideIndexRating);
showSlidesProfile(slideIndexProfile);
showSlidesComedy(slideIndexComedy);
showSlidesDrama(slideIndexDrama);
showSlidesFiction(slideIndexFiction);
showSlidesNonFiction(slideIndexNonFiction);
showSlidesHorror(slideIndexHorror);

// ----------------------------------------------------------------
// functions for each slider to handle next and prev events
// ----------------------------------------------------------------
function plusSlidesDeal(n) {
  showSlidesDeal(slideIndexDeal += n);
}

function plusSlidesTop(n) {
  showSlidesTop(slideIndexTop += n);
}

function plusSlidesRating(n) {
  showSlidesRating(slideIndexRating += n);
}

function plusSlidesProfile(n) {
  showSlidesProfile(slideIndexProfile += n);
}

function plusSlidesComedy(n) {
  showSlidesComedy(slideIndexComedy += n);
}

function plusSlidesDrama(n) {
  showSlidesDrama(slideIndexDrama += n);
}

function plusSlidesFiction(n) {
  showSlidesFiction(slideIndexFiction += n);
}

function plusSlidesNonFiction(n) {
  showSlidesNonFiction(slideIndexNonFiction += n);
}

function plusSlidesHorror(n) {
  showSlidesHorror(slideIndexHorror += n);
}

// ----------------------------------------------------------------
//  functions that handle the which slide is shown, as well as 
//  numeric underflow and overflow 
// ----------------------------------------------------------------
function showSlidesDeal(n) {
	var slides = document.getElementsByClassName("slides_deals");
	var i;
	var counter;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }
	  
	var slideNum = n;

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexDeal = 1; }
  	if (slideNum  < 1) {slideIndexDeal = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesTop(n) {
  	var slides = document.getElementsByClassName("slides_top");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexTop = 1; }
  	if (slideNum  < 1) {slideIndexTop = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesRating(n) {
	var slides = document.getElementsByClassName("slides_rating");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexRating = 1; }
  	if (slideNum  < 1) {slideIndexRating = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}


function showSlidesProfile(n) {
	var slides = document.getElementsByClassName("slides_profile");
	var i;
  	
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexProfile = 1; }
  	if (slideNum  < 1) {slideIndexProfile = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesComedy(n) {
	var slides = document.getElementsByClassName("slides_comedy");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexComedy = 1; }
  	if (slideNum  < 1) {slideIndexComedy = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesDrama(n) {
	var slides = document.getElementsByClassName("slides_drama");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexDrama = 1; }
  	if (slideNum  < 1) {slideIndexDrama = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesHorror(n) {
	var slides = document.getElementsByClassName("slides_horror");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }
	
	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexHorror = 1; }
  	if (slideNum  < 1) {slideIndexHorror = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesFiction(n) {
	var slides = document.getElementsByClassName("slides_fiction");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }

	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexFiction = 1; }
  	if (slideNum  < 1) {slideIndexFiction = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }

		slides[slideNum-1].style.display = "inline";
		slideNum++;
	}
}

function showSlidesNonFiction(n) {
	var slides = document.getElementsByClassName("slides_non_fiction");
	var i;
	var counter;
	var slideNum = n;
	
	for (i = 0; i < slides.length; i++) {
    	slides[i].style.display = "none";  
 	 }
	
	// Adjust slideIndex if there exists overflow or underflow
	if (slideNum  > slides.length) {slideIndexNonFiction = 1; }
  	if (slideNum  < 1) {slideIndexNonFiction = slides.length; }	
	
	for (counter = 0; counter < 7; counter ++) {
		
		if (slideNum > slides.length) {slideNum = 1;}
	    if (slideNum < 1) {slideNum = slides.length; }
		
		try {
			slides[slideNum-1].style.display = "inline";
		} catch(err) {}
		
		slideNum++;
	}
}