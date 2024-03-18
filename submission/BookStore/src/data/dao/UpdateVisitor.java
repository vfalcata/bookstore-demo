package data.dao;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import data.beans.Cart;
import data.beans.Id;
import data.beans.Visitor;
import data.schema.UserTypes;

public class UpdateVisitor extends DataUpdate{
	UpdateVisitor(){}
	public Visitor executeInsertNewVisitor(HttpServletRequest request) {
		if(request==null) return new Visitor.Builder().build();
		String id=request.getSession().getId();
		String epoch =Long.toString(Instant.now().getEpochSecond());
		String userTablesUpdate = "INSERT INTO SITE_USER (ID,USER_TYPE) VALUES ('"+id+"','"+UserTypes.VISITOR+"')";
		sendUpdateToDatabase(userTablesUpdate);
		String update ="INSERT INTO VISITOR (ID,CREATED_AT_EPOCH) VALUES ('"+
				"('"+id+"',"+epoch+")";
		sendUpdateToDatabase(update);
		return new Visitor.Builder().withId(new Id(id)).withCart(new Cart.Builder().withId(new Id(id)).build()).withCreatedAtEpoch(epoch).build();
	}
}
