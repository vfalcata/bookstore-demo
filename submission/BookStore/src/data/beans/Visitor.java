package data.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.beans.Cart.Builder;
import data.beans.IdObject.IdObjectBuilder;
import data.schema.UserTypes;
public class Visitor extends SiteUser {
	private  long createdAtEpoch;
	private  long lastAccessedAtEpoch;
	public static final String userType=UserTypes.VISITOR;	
	
	public String getUserType() {
		return this.userType;
	}
	
	public long getCreatedAtEpoch() {
		return createdAtEpoch;
	}	
	
	public long getLastAccessedAtEpoch() {
		return lastAccessedAtEpoch;
	}


	public static class Builder extends IdObjectBuilder<Builder>{
		private Cart cart;
		private  long createdAtEpoch;
		private  long lastAccessedAtEpoch;
		private PurchaseOrder[] purchaseOrders;

		public Builder(){
			super();
			this.cart=new Cart.Builder().withId(this.id).build();
			this.id=new Id("");
			createdAtEpoch=0;
			lastAccessedAtEpoch=0;
			this.purchaseOrders=new PurchaseOrder[0];
		}
		
		public Builder(Visitor visitor){
			super();
			this.id=visitor.getId();
			this.cart=visitor.cart;
			this.createdAtEpoch=visitor.createdAtEpoch;
			this.lastAccessedAtEpoch=visitor.lastAccessedAtEpoch;
			this.purchaseOrders=visitor.purchaseOrders;
		}

		public Builder withSessionId(String sessionId){
			this.id=new Id(sessionId);
			if(this.id.isEmpty())System.err.println("warning: generated visitor object without sessionId!");
			return this;
		}

		public Builder withCart(Cart cart){
			this.cart=cart;
			return this;
		}
		
		public Builder withCreatedAtEpoch(long createdAtEpoch) {
			this.createdAtEpoch=createdAtEpoch;
			return this;
		}
		
		public Builder withLastAccessedAtEpoch(long lastAccessedAtEpoch) {
			this.lastAccessedAtEpoch=lastAccessedAtEpoch;
			return this;
		}
		
		public Builder withCreatedAtEpoch(String createdAtEpoch) {
			this.createdAtEpoch=Long.parseLong(createdAtEpoch);
			return this;
		}
		
		public Builder withLastAccessedAtEpoch(String lastAccessedAtEpoch) {
			this.lastAccessedAtEpoch=Long.parseLong(lastAccessedAtEpoch);
			return this;
		}
		
		public Builder withPurchaseOrders(PurchaseOrder[] purchaseOrders){
			this.purchaseOrders=purchaseOrders;
			return this;
		}

		public Builder withPurchaseOrders(List<PurchaseOrder> purchaseOrders){
			PurchaseOrder[] purchaseOrderArr = new PurchaseOrder[purchaseOrders.size()];
			return withPurchaseOrders(purchaseOrders.toArray(purchaseOrderArr));
		}
		public Builder withPurchaseOrders(PurchaseOrder purchaseOrders){
			this.purchaseOrders=new PurchaseOrder[] {purchaseOrders};
			return this;
		}

		public Visitor build(){
			Visitor visitor=new Visitor();
			visitor.id=this.id==null?new Id(""):this.id;
			visitor.cart=this.cart==null? new Cart.Builder().withId(this.id).build():this.cart;
			visitor.createdAtEpoch=this.createdAtEpoch;
			visitor.lastAccessedAtEpoch=this.lastAccessedAtEpoch;
			visitor.purchaseOrders=this.purchaseOrders==null?new PurchaseOrder[0]:this.purchaseOrders;
			return visitor;
		}

	}



	@Override
	public String toJson() {
		String purchaseOrdersJson="\"purchaseOrders\": [";
		if (this.purchaseOrders!=null && purchaseOrders.length>0) {
			for(PurchaseOrder purchaseOrder:this.purchaseOrders) {
				purchaseOrdersJson+=purchaseOrder.toJson()+",";
			}
			purchaseOrdersJson=purchaseOrdersJson.substring(0, purchaseOrdersJson.length() - 1);				
				
		}
		purchaseOrdersJson+="]";
		return "{"+Bean.jsonMapVarChar("id",this.id.toString())+","+
		Bean.jsonMapNumber("createdAtEpoch",Long.toString(createdAtEpoch))+","+
		Bean.jsonMapNumber("lastAccessedAtEpoch",Long.toString(lastAccessedAtEpoch))+","+
		Bean.jsonMapNumber("cart",this.cart.toJson())+","+
		purchaseOrdersJson+
		"}";
	}
}
