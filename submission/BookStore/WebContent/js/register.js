/**
 * 
 */
const registerAddr = "/Register";
const signInUIAddr = "/html/SignIn.jspx";

function register()
{

	let request = new XMLHttpRequest();
	
	let email = document.getElementById("input-email").value;
	let username = document.getElementById("input-username").value;
	let password = document.getElementById("input-password").value;
	let givenName = document.getElementById("input-given-name").value;
	let surname = document.getElementById("input-surname").value;
	
	let number = document.getElementById("input-number").value;
	let street = document.getElementById("input-street").value;
	let city = document.getElementById("input-city").value;
	let province = document.getElementById("input-province").value;
	let country = document.getElementById("input-country").value;
	let postalCode = document.getElementById("input-postal").value;
	
	let cardType = document.getElementById("select-cards").value;
	let ccNumber = document.getElementById("input-cc-number").value;
	let cvv = document.getElementById("input-cvv").value;
	let expiry = document.getElementById("input-expiry").value;
	
	
	let url = new URL(registerAddr, window.location.protocol + window.location.host);
	
	
	url.searchParams.set("username", username);
	url.searchParams.set("password", password);
	url.searchParams.set("email", email);
	url.searchParams.set("givenName", givenName);
	url.searchParams.set("surname", surname);
	
	url.searchParams.set("number", number);
	url.searchParams.set("street", street);
	url.searchParams.set("city", city);
	url.searchParams.set("province", province);
	url.searchParams.set("country", country);
	url.searchParams.set("postalCode", postalCode);
	
	url.searchParams.set("cardType", cardType);
	url.searchParams.set("ccNumber", ccNumber);
	url.searchParams.set("cvv", cvv);
	url.searchParams.set("expiry", expiry);
	
	console.log(url.toString());
	
	request.open("POST", url.toString());
	request.onreadystatechange = function (){
		handler(request);
	};
	
	request.send(null);
}

function handler(request)
{
	if(request.readyState == 4 && request.status == 403)
	{
		let target = document.getElementById("label-error");
		
		target.style.visibility = "visible";
		target.innerHTML = request.responseText;
	}
	else if( request.status == 200)
	{
		window.location.href = signInUIAddr;
	}
}