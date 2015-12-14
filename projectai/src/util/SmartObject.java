package util;

public class SmartObject {
	public Object o = null;
	public int used = 0;
	public boolean equals(Object o2){
		if(o.equals(o2)){
			return true;
		}
		return false;
	}
	public String toString(){
		if(o==null)
			return "Null";
		return o.toString();
	}
	public Object getValue(){
		return o;
	}
	public SmartObject(Object o2){
		o = o2;
	}
}
