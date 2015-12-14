package util;

public class SmartInteger extends SmartObject{
	
	public int value = 0;
	
	public SmartInteger(Object o2){
		super(o2);
		value = (int) Integer.parseInt(o.toString());
	}
	
	@Override public String toString(){
		String s = ""+this.value;
		return s;
	}
	
	public int add(Object o2){
		try{
			value += (int) Integer.parseInt(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartInteger");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to integer in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public int subtract(Object o2){
		try{
			value -= (int) Integer.parseInt(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartInteger");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to integer in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public int multiply(Object o2){
		try{
			value *= (int) Integer.parseInt(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartInteger");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to integer in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public int devide(Object o2){
		try{
			value /= (int) Integer.parseInt(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartInteger");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to integer in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
}
