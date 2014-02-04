package eyeglassesmain;

public class FormattingException extends Exception{
	private String msg;
	
	FormattingException(String msg){
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}
}
