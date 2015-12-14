package main;
import util.*;
public class AI {
	static DynamicMemoryCluster dMC = new DynamicMemoryCluster(10);
	public AI(){
		dMC.addToCluster("Trollolol");
		dMC.addToCluster("lol");
		dMC.addToCluster("YOLO");
		dMC.addToCluster("SWAG");
		dMC.addToCluster("UltimateSwag");
		dMC.addToCluster("Trolling");
		dMC.addToCluster("Trolling");
		System.out.println(dMC.getObjectForString("Troll"));
		System.out.println(dMC.getObjectForString("Troll"));
		System.out.println(dMC.getObjectForString("Troll"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("UltimateSwag"));
		dMC.deleteFromCluster("lol");
	}
	public static void main(String[] args){
		dMC.addToCluster("Trollolol");
		dMC.addToCluster("lol");
		dMC.addToCluster("YOLO");
		dMC.addToCluster("SWAG");
		dMC.addToCluster("UltimateSwag");
		dMC.addToCluster("Trolling");
		dMC.addToCluster("Trolling");
		System.out.println(dMC.getObjectForString("Troll"));
		System.out.println(dMC.getObjectForString("Troll"));
		System.out.println(dMC.getObjectForString("Troll"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("Trolling"));
		System.out.println(dMC.getObjectForString("UltimateSwag"));
		dMC.deleteFromCluster("lol");
		AI ai = new AI();
	}
}
