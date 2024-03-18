package data.beans;

public interface Bean {
	public abstract String toJson();
	
	public static String jsonMapVarChar(String key,String value) {
		return quote(key)+":"+quote(value);
	}
	
	public static String jsonMapNumber(String key,String value) {
		return quote(key)+":"+value; 
	}
	
	public static String quote(String word) {
		return "\""+word+"\"";
	}
	


}
