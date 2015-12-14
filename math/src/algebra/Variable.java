package algebra;

public class Variable {
	public float f = 0.0F;
	public String name = "";
	
	public Variable(String s){
		name = s;
	}
	
	public Variable(String s, float _f){
		name = s;
		f = _f;
	}
	
	public Variable add(Variable v){
		this.f+=v.f;
		return this;
	}
	
}
