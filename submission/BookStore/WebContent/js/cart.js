/**
 * 
 */
const shoppingCartAddr = "/ShoppingCart";

function updateQuantity(isbn, quantity)
{
	let request = new XMLHttpRequest();
	
	let url = new URL(shoppingCartAddr, window.location.protocol + window.location.host);
	
	
	url.searchParams.set("isbn", isbn);
	url.searchParams.set("quantity", quantity);
	
	console.log(url.toString());
	
	request.open("POST", url.toString());
	request.onreadystatechange = function (){
		updateTotalPrice(request);
	};
	
	request.send(null);
}



function updateTotalPrice(request)
{
	if(request.readyState == 4 && request.status == 200)
	{
		let target = document.getElementById("span-price");
		if(request.responseText.length > 0)
		{
			if(parseFloat(request.responseText) <= 0)
			{
				location.reload();
			}
			else{
				target.innerHTML = parseFloat(request.responseText).toFixed(2);
			}
			
		}
		
	}
}

function removeBook(isbn)
{
	let request = new XMLHttpRequest();
	
	let url = new URL(shoppingCartAddr + "/remove", window.location.protocol + window.location.host);
	
	url.searchParams.set("isbn", isbn);
	console.log(url.toString());
	
	request.open("POST", url.toString());
	request.onreadystatechange = function (){
		updateBooks(request, isbn);
	};
	
	request.send(null);
}
function updateBooks(request, isbn)
{
	if(request.readyState == 4 && request.status == 200)
	{
		document.getElementById("div-" + isbn).remove();
		updateTotalPrice(request);
	}
}