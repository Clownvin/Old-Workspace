package util;

public class SmartBoolean extends SmartObject{
	public boolean value = false;
	public SmartBoolean(Object o2) {
		super(o2);
		try{
			value = Boolean.parseBoolean(o2.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override public String toString(){
		if(value)
			return "true";
		return "false";
	}
}
