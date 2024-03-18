package data.dao.exceptions;

public class UpdateDBFailureException extends Exception{
	public UpdateDBFailureException(String objectType,String errorMsg, String updateString){
		super("The update on object type: "+objectType+" failed "+errorMsg+" \n the request query string was: \n"+updateString);
	}

}
