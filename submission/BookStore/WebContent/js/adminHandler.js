/**
 * 
 */

function doSimpleAjax(address) {
		var request = new XMLHttpRequest();
	 	var data = "";
	
		data += "startYear=" + document.getElementById("startYear").value + "&";
		data += "startMonth=" + document.getElementById("startMonth").value + "&";
		data += "endYear=" + document.getElementById("endYear").value + "&";
		data += "endMonth=" + document.getElementById("endMonth").value;
	
		request.open("GET", (address + "&" + data), true);
			request.onreadystatechange = function() {
			handler(request);
		};
		
		console.log(address + "&" + data);
		
		request.send(null); 
}

function handler(request) {
 	if ((request.readyState == 4) && (request.status == 200)){
 		var target = document.getElementById("result");
 		target.innerHTML = request.responseText;
 	}
} 