/**
 * signIn.js
 * Handles posting for user sign-in
 */
const signInAddr = "/SignIn";
const mainPageAddr = "/MainPage";


function signIn()
{

	let request = new XMLHttpRequest();
	
	let username = document.getElementById("input-uname").value;
	let password = document.getElementById("input-pword").value;
	
	let url = new URL(signInAddr, window.location.protocol + window.location.host);
	
	
	url.searchParams.set("username", username);
	url.searchParams.set("password", password);
	
	console.log(url.toString());
	
	request.open("POST", url.toString());
	request.onreadystatechange = function (){
		handler(request);
	};
	
	request.send(null);
}

function handler(request)
{
	console.log(request.status);
	if(request.readyState == 4)
	{
		if(request.status == 403)
		{
			let target = document.getElementById("label-error");
			target.style.visibility = "visible";
			target.innerHTML = request.responseText;
		}
		else if(request.status == 200)
		{
			window.location.href = mainPageAddr;
		}
		
	}
}