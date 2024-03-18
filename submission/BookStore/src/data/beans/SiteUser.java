package data.beans;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SiteUser extends User{
	protected Cart cart;
	protected PurchaseOrder[] purchaseOrders;
	public abstract String getUserType();
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	public void addPurchseOrder(PurchaseOrder purchaseOrder) {
		PurchaseOrder[] purchaseOrders=new PurchaseOrder[this.purchaseOrders.length+1];
		for(int i=0;i<this.purchaseOrders.length;i++) {
			purchaseOrders[i]=this.purchaseOrders[i];
		}
		purchaseOrders[this.purchaseOrders.length]=purchaseOrder;
		this.purchaseOrders=purchaseOrders;
	}
	
	public void addPurchseOrders(PurchaseOrder[] purchaseOrders) {
		PurchaseOrder[] appendPurchaseOrders=new PurchaseOrder[this.purchaseOrders.length+purchaseOrders.length];
		for(int i=0;i<this.purchaseOrders.length;i++) {
			appendPurchaseOrders[i]=this.purchaseOrders[i];
		}
		for(int i=0;i<purchaseOrders.length;i++) {
			appendPurchaseOrders[i+this.purchaseOrders.length]=purchaseOrders[i];
		}

		this.purchaseOrders=appendPurchaseOrders;
	}
	
	
	public PurchaseOrder[] getPurchaseOrders() {
		return purchaseOrders;
	}
	
	public Map<Long,PurchaseOrder> getCreatedAtEpochToPurchaseOrders() {
		Map<Long,PurchaseOrder>  result = new LinkedHashMap<Long, PurchaseOrder>();
		for(PurchaseOrder purchaseOrder:this.purchaseOrders) {
			result.put(purchaseOrder.getCreatedAtEpoch(), purchaseOrder);
		}
		return result;
	}
	
	public boolean isPurchaseOrderBySiteUser(PurchaseOrder purchaseOrder) {
		return purchaseOrder.getId().equals(id);
	}

	public PurchaseOrder getPurchaseOrderByCreatedAtEpoch(Long createdAtEpoch) {
		return getCreatedAtEpochToPurchaseOrders().get(createdAtEpoch);
	}
}
