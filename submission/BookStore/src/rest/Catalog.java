package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import model.RestModel;

@Path("catalog")

public class Catalog {

	@GET
	@Path("/getProductInfo/")
	@Produces("application/json")
	public Response getProductInfo(@QueryParam("productID") String productID) throws Exception {
		
		String bookJSON = RestModel.getInstance().getBookByISBN_JSON(productID);

		return Response.ok(bookJSON).build();
	}
	
}