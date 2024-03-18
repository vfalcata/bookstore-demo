package data.beans;

import java.util.UUID;

public abstract class IdObject implements Bean{
	
	protected Id id;
	public Id getId() {
		return id;
	}
	public boolean isEqual(IdObject idObject) {
		return idObject.getId().equals(idObject.getId());
	}

	
	public boolean isEqual(Id other) {
		return id.equals(other);
	}
	
	
	public boolean hasId() {
		return this.id!=null && !this.id.isEmpty();
	}
	
	public IdObject(){
		this.id=new Id("");
	}
	
	public String idToString() {
		return this.id.toString();
	}
	
	@Override
	public int hashCode() {

		return 0;
		
	}
	@Override
	public boolean equals(Object other) {
		IdObject otherIdObject = (IdObject) other;
		return otherIdObject !=null && !otherIdObject.getId().isEmpty() && this.getId().toString().equals(otherIdObject.getId().toString());
		
	}
	
	
 protected abstract static class IdObjectBuilder<T extends IdObjectBuilder>{
	 protected Id id;
	 IdObjectBuilder(IdObject idObject){
		 this.id=idObject.getId();
	 }
	 
	 IdObjectBuilder(){
		 this.id=new Id("");
	 }
	 
	public T withId(IdObject idObject) {
			if(idObject.hasId()) {
				this.id=idObject.getId();	
			}else {
				System.err.println("Warning no idea was attached to the object when contructing it with the builder using withId()");
			}
			
			return (T) this;
	}
	
	 
		public T withId(Id id) {
			this.id=id;		
			return (T) this;
		}

 


}
}
