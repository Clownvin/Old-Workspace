package util;

public class SmartLong extends SmartObject{
	
	public long value = 0;
	
	public SmartLong(Object o2){
		super(o2);
		value = (long) Long.parseLong(""+o);
	}
	
	@Override public String toString(){
		String s = ""+this.value;
		return s;
	}
	
	public long add(Object o2){
		try{
			value += (long) Long.parseLong(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartLong");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to long in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public long subtract(Object o2){
		try{
			value -= (long) Long.parseLong(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartLong");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to long in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public long multiply(Object o2){
		try{
			value *= (long) Long.parseLong(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartLong");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to long in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}
	
	public long devide(Object o2){
		try{
			value /= (long) Long.parseLong(o2.toString());
		}catch(ClassCastException cce){
			System.err.println("[ClassCastException]: While making cast in add field: SmartLong");
			cce.printStackTrace();
		}catch(NumberFormatException nfe){
			System.err.println("[NumberFormatException]: While parsing object to long in add field: SmartInteger");
			nfe.printStackTrace();
		}
		return value;
	}

}
