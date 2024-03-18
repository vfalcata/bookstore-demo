package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import data.beans.Book;
import model.RestModel;

@Path("orders")

public class Orders {

	@GET
	@Path("/getOrdersByPartNumber/")
	@Produces("application/json")
	public Response getOrdersByPartNumber(@QueryParam("productID") String productID) throws Exception {

		String bookJSON = RestModel.getInstance().getOrdersByISBN(productID);

		return Response.ok(bookJSON).build();

	}

}