package data.query;

public interface NumberQueryable <T extends Query> extends Queryable<T>{	
	
	public abstract T numberAtMost(String max);
	
	public abstract T numberAtLeast(String min);
	
	public abstract T numberEquals(String number);
	
	public abstract T numberBetween(String min,String max);


}
