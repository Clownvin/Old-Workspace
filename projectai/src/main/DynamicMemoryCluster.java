package main;

import util.SmartObject;

public class DynamicMemoryCluster{
	
	private SmartObject[] anObjectCluster = null;
	private int clusterLength = 0;
	
	public DynamicMemoryCluster(int i){
		anObjectCluster = new SmartObject[i];
		clusterLength = i;
		System.out.println("MADE CLUSTER");
	}
	
	public Object getObjectAtPoint(int i){
		if(i>-1 && i<anObjectCluster.length){
			if(anObjectCluster[i]!=null){
				anObjectCluster[i].used++;
			}
			return anObjectCluster[i];
		}
		return null;
	}
	
	public Object getPrimitaveObjectAtPoint(int i){
		if(i>-1 && i<anObjectCluster.length){
			if(anObjectCluster[i]!=null){
				anObjectCluster[i].used++;
			}
			return anObjectCluster[i].getValue();
		}
		return null;
	}
	
	public void addToCluster(Object o){
		System.out.println("ADDING TO CLUSTER");
		for(SmartObject s : anObjectCluster){
			if(s==null||s.getValue()==null){
				s = null;
				s = new SmartObject(o);
			}
		}
	}
	
	public Object getObjectForString(String s){
		System.out.println("GETTING OBJECT FOR STRING");
		for(SmartObject so : anObjectCluster){
			if(so!=null){
				System.out.println("ISN'T NULL");
				if(so.toString().equals(s.toString())){
					System.out.println("EQUALS");
					so.used++;
					return so;
				}
				System.out.println("DOES NOT EQUAL");
			}
		}
		return new SmartObject("");
	}
	
	public Object[] getAllObjectsForString(String s){
		System.out.println("GETTING ALL OBJECTS FOR STRING");
		int amount = 0;
		for(SmartObject so : anObjectCluster){
			if(so!=null){
				if(so.toString().equals(s.toString())){
					amount++;
				}
			}
		}
		SmartObject[] toReturn = new SmartObject[amount];
		amount=0;
		for(SmartObject so : anObjectCluster){
			if(so!=null){
				if(so.toString().equals(s.toString())){
					toReturn[amount]=so;
					so.used++;
					amount++;
				}
			}
		}
		return toReturn;
	}
	
	public void deleteFromCluster(Object o){
		System.out.println("DELETING FROM CLUSTER");
		for(SmartObject s : anObjectCluster){
			if(s!=null){
				if(s.getValue()!=null){
					if((s.getClass()==o.getClass())&&(s.toString().equals(o.toString()))){
						s = null;
						reload();
					}else{
						System.out.println(s.getClass()+" does not equals "+o.getClass()+" or "+s.toString()+" does not equal "+o.toString());
					}
				}
			}
		}
	}
	
	public void reload(){
		System.out.println("RELOADING");
		int startsAt = 0;
		int endsAt = Integer.MAX_VALUE;
		SmartObject[] tempCluster = new SmartObject[anObjectCluster.length];
		int i = 0;
		for(int j = 0; j < anObjectCluster.length; j++){
			if(anObjectCluster[j]!=null){
				try{
					while(tempCluster[anObjectCluster[j].used+i]==null){
						i++;
					}
					if(anObjectCluster[j].used > startsAt){
						startsAt = anObjectCluster[j].used;
					}else if(anObjectCluster[j].used+i < endsAt){
						endsAt = anObjectCluster[j].used+i;
					}
					tempCluster[anObjectCluster[j].used+i]=anObjectCluster[j];
					i = 0;
				}catch(Exception e){
					System.err.println("Error trying to find non null index");
					e.printStackTrace();
				}
			}
		}
		anObjectCluster = new SmartObject[clusterLength];
		i = 0;
		for(int k = startsAt; k > endsAt; k++){
			if(tempCluster[k]!=null){
				anObjectCluster[i]=tempCluster[k];
				System.out.println(anObjectCluster[i].toString()+" was used "+anObjectCluster[i].used+" times.");
				anObjectCluster[i].used = 0;
				i++;
			}
		}
	}
}
