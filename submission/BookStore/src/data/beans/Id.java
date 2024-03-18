package data.beans;

public class Id {
	private final String id;
	public  Id(String id) {
		this.id=id;
	}
	
	
	public boolean isEmpty() {
		return this.id==null || this.id.isEmpty();
	}
	
	public boolean isEqual(Id other) {
		return !this.isEmpty() && other !=null && !other.id.isEmpty() && this.id.equals(other.id);
	}
	
	@Override
	public boolean equals(Object other) {
		Id otherId = (Id) other;
		return !this.isEmpty() && otherId !=null && !otherId.isEmpty() && this.id.toString().equals(otherId.toString());
		
	}
	
	@Override
	public int hashCode() {

		return 0;
		
	}
	
	public String toString() {
		return this.id;
	}

}
