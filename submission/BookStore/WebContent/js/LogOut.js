const logOutAddr = "/SignIn"

function LogOut()
{

	let request = new XMLHttpRequest();
	
	let page = document.getElementById("cameFromPage").value;
	let url = new URL(logOutAddr, window.location.protocol + window.location.host);
	
	url.searchParams.set("cameFromPage",page);
	
	console.log(url.toString());
	
	request.open("Post", url.toString());
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
			//let target = document.getElementById("label-error");
			//target.style.visibility = "visible";
			//target.innerHTML = request.responseText;
		}
		else if(request.status == 200)
		{
			window.location.href = "/MainPage";
		}
		
	}
}