package util;

public class SmartString extends SmartObject{
	
	public String value = "";
	
	public SmartString(Object o2){
		super(o2);
		value = o.toString();
	}
	
	@Override public String toString(){
		return this.value;
	}
	
	public String add(Object o2){
		try{
			value += o2.toString();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to long in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public String[] split(String s){
		return value.split(s);
	}
	
	public String replace(String s1, String s2){
		return value.replace(s1,s2);
	}
	
	public String getValue(){
		return value;
	}
}
