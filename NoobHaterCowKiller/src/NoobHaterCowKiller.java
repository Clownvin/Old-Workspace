import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.bot.event.listener.PaintListener;


@SuppressWarnings("deprecation")
@Manifest(authors = { "NoobHater" }, name = "NoobHaterCowKiller", description = "Kills cattle, so you don't have to!", version = 1.0)
public class NoobHaterCowKiller extends ActiveScript implements PaintListener{
	
	//Variables
	public final Player player = Players.getLocal();
	public NPC cow = null;
	public int cowsAttacked = 0;
	public Tile gateLoc = null;
	public SceneObject fire = null;
	public boolean doingSomething = false;
	public String status = "Starting script";
	public final int[] FIRE = {70755,70757};
	public final int RAW_MEAT = 2132;
	public final int COOKED = 2142;
	public final int BURNT_MEAT = 2146;
	public final int BONES = 526;
	public final int[] LOG_1 = {1511};
	public final int[] LOG_15 = {1511,1521};
	public final int[] GATE = {45210,45211,45212,45213};
	public final int[] TREE_1 = {38782,38783,38784,38785,38786,38787,1286,1282};
	public final int[] TREE_15 = {38782,38783,38784,38785,38786,38787,1286,1282,/*15*/38732};
	public final int[] COW = {12362,12363,12365,12366};
	
	public final CreateFire fireMaker = new CreateFire();
	public final Strategy createFire = new Strategy(fireMaker,fireMaker);
	
	final CookMeat meatCooker = new CookMeat();
	final Strategy cookMeat = new Strategy(meatCooker, meatCooker);
	
	final TakeItems itemTaker = new TakeItems();
	final Strategy takeItems = new Strategy(itemTaker,itemTaker);
	
	final BoneBurrier buryBones = new BoneBurrier();
	final Strategy boneBurrier = new Strategy(buryBones,buryBones);
	
	final EatFood foodEater = new EatFood();
	final Strategy eatFood = new Strategy(foodEater,foodEater);
	
	final AttackCow cowAttacker = new AttackCow();
	final Strategy attackCow = new Strategy(cowAttacker,cowAttacker);
	
	final GetUnknownStatus statusCheck = new GetUnknownStatus();
	final Strategy getIdling = new Strategy(statusCheck,statusCheck);
	public int bonesBurried = 0;
	public boolean needFire = false;
	
	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
        g.setFont(new Font("Arial", 0, 9));
        g.setColor(new Color(0,0,0));
        g.drawRect(0, 0, 200, 200);
        g.fillRect(0, 0, 200, 200);
        g.setColor(new Color(255, 255, 255));
        g.drawString("Bones Burried : "+bonesBurried,10,10);
        g.drawString("Cow Assults : "+cowsAttacked, 10, 20);
        g.drawString("Status : "+status, 10, 30);
	}

	@Override
	protected void setup() {
		//provide(boneBurrier);
		provide(eatFood);
		provide(attackCow);
		provide(takeItems);
		provide(cookMeat);
		provide(createFire);
		provide(getIdling);
	}
	

	
	public NPC getNearestCow(){
		return NPCs.getNearest(COW);
	}
	
	public boolean doingNothing(){
		return player.isIdle() && !player.isInCombat() && !player.isMoving() && !doingSomething;
	}
	
	public SceneObject getNearestFire(){
		return SceneEntities.getNearest(FIRE);
	}
	
	public SceneObject getNearestGate(){
		return SceneEntities.getNearest(GATE);
	}
	
	public SceneObject getNearestTree(){
		if(Skills.getLevel(Skills.WOODCUTTING)>=15){
			return SceneEntities.getNearest(TREE_15);
		}
		return SceneEntities.getNearest(TREE_1);
	}
	
	public void cook(){
		if(Widgets.get(905, 14).isOnScreen()){
			status = "[Cooking]: Cooking some Raw meat...";
			doingSomething=true;
			Widgets.get(905, 14).click(true);
			Time.sleep(1000,2000);
			while(fire!=null&&Inventory.contains(RAW_MEAT)&&!player.isMoving()){
				continue;
			}
			if(Inventory.contains(BURNT_MEAT)){
				while(Inventory.contains(BURNT_MEAT)){
					Inventory.getItem(BURNT_MEAT).getWidgetChild().interact("Drop");
					Time.sleep(10,20);
				}
			}
			doingSomething=false;
		}
	}
	
	public int[] logForLevel(){
		if(Skills.getLevel(Skills.FIREMAKING)>=15){
			return LOG_15;
		}
		return LOG_1;
	}
	
	public void walkTo(Locatable o){
		Walking.walk(o);
	}
	
	public void makeFire(){
		if(getNearestFire()==null)
			if(Inventory.containsOneOf(logForLevel())){
				status = "[Firemaker]: Attempting to make fire...";
				Inventory.getItem(logForLevel()).getWidgetChild().interact("Light");
				Time.sleep(200);
				while(!doingNothing())
					continue;
				if(meatCooker.validate()){
					meatCooker.run();
				}
			}
	}
	
	public void getWood(){
		status = "[Woodcutter]: Attempting to get some wood...";
		SceneObject tree;
		gateLoc = new Tile(getNearestGate().getLocation().getX(),getNearestGate().getLocation().getY()-1,getNearestGate().getLocation().getPlane());
		if(!gateLoc.isOnScreen()){
			walkTo(gateLoc);
		}
		Camera.turnTo(gateLoc);
		getNearestGate().interact("Open");
		tree = getNearestTree();
		if(!tree.isOnScreen()){
			Camera.turnTo(tree);
			if(!tree.getLocation().canReach()){
				walkTo(tree);
				Time.sleep(100,200);
			}
		}
		tree.interact("Chop down");
		Time.sleep(500,1000);
		while(!player.isIdle())
			continue;
		if(!gateLoc.isOnScreen()){
			walkTo(gateLoc);
			Time.sleep(500,1000);
		}
		Camera.turnTo(gateLoc);
		getNearestGate().interact("Open");
		while(!player.getLocation().equals(gateLoc)){
			if(!gateLoc.canReach()){
				walkTo(gateLoc);
				Time.sleep(600,800);
			}else{
				Camera.turnTo(gateLoc);
				gateLoc.click(true);
				Time.sleep(400,500);
			}
		}
	}
	
	private class AttackCow implements Task, Condition{
    	public final int[] COW = {12362,12363,12365,12366};
    	public NPC cow = null;
		@Override
        public void run() {
        	if(!cow.isInCombat()&&!playerIsAttacking()&&!NPCs.getNearest("Cow").isInCombat()){
        		status = "[CowKiller]: Attacking cow...";
        		Camera.turnTo(cow);
        		cow.interact("Attack");
        		Time.sleep(1000,2000);
        		while(cow.getHpPercent()>0&&cow!=null&&(cow.getInteracting().getName().equals(player.getName()))){
        			//System.out.println(cow.getInteracting().getName());
        			if(cow==null)
        				status = "Cow is not there!";
        			continue;
        		}
        		System.out.println(cow.getInteracting().getName());
        		while(player.isInCombat())
        			continue;
        		if((cow.isInCombat()&&Players.getLocal().isInCombat())||cow==null)
        			cowsAttacked++;
        	}
        	Time.sleep(100,2000);
        	if(itemTaker.validate())
        		itemTaker.run();
        }
        
        public boolean playerIsAttacking(){
        	return player.isInCombat();
        }
        
        @Override
        public boolean validate() {
        	cow = NPCs.getNearest(COW);
        	try{
        	if(cow == null){
        		System.err.println("[CowKiller]: Cow is null!");
        		return false;
        	}else if(player.getHpPercent()<25){
        		System.err.println("[CowKiller]: Player HP is only "+Players.getLocal().getHpPercent()+"!");
        		return false;
        	}
        	else if(/*!player.isMoving()&&*/!playerIsAttacking()&&!cow.isInCombat()&&cow.isOnScreen()){
        		Camera.turnTo(cow);
        		return true;
        	}else if(/*!player.isMoving()&&*/!playerIsAttacking()&&!cow.isInCombat()&&!cow.isOnScreen()){
        		status = "[CowKiller]: Walking to cow...";
        		walkTo(cow);
        		return true;
        	}
        	}catch(Exception e){
        		
        	}
        	return false;
        }
	}
	
	/*private class WalkToCow implements Task, Condition{

		@Override
		public void run() {
			
		}

		@Override
		public boolean validate() {
			if(!doingSomething){
				cow = getNearestCow();
				if(cow!=null)
					return true;
			}
			return false;
		}
		
	}*/
	
	private class CreateFire implements Task, Condition{

		@Override
		public void run() {
			doingSomething = true;
			needFire = false;
			if(Inventory.containsOneOf(logForLevel())){
				makeFire();
				Time.sleep(50,200);
			}else{
				getWood();
				Time.sleep(10,100);
			}
			if(cookMeat.validate()){
				meatCooker.run();
			}
			doingSomething = false;
		}

		@Override
		public boolean validate() {
			if(!doingSomething&&!player.isMoving()&&!player.isInCombat()&&needFire)
				return true;
			return false;
		}
	}
	
	private class CookMeat implements Task, Condition{

		@Override
		public void run() {
			fire = getNearestFire();
			if(!fire.isOnScreen())
				walkTo(fire);
			Camera.turnTo(fire);
			status = "[Cooking]: Attempting to cook some Raw meat...";
			Inventory.getItem(RAW_MEAT).getWidgetChild().interact("Use");
			fire.interact("Use");
			Time.sleep(400,500);
			cook();
		}

		@Override
		public boolean validate() {
			if(attackCow.validate()){
				cowAttacker.run();
				return false;
			}
			if(!player.isInCombat()&&player.isIdle()&&
					(Inventory.getCount(RAW_MEAT)>=5||
					player.getHpPercent()<=25)&&
					!doingSomething&&
					!player.isMoving()){
				if(getNearestFire()==null){
					if(Inventory.containsOneOf(logForLevel())){
						makeFire();
						return true;
					}else if(GroundItems.getNearest(logForLevel())!=null){
						GroundItems.getNearest(logForLevel()).interact("Take", GroundItems.getNearest(logForLevel()).getGroundItem().getName());
						makeFire();
					}else{
						getWood();
						makeFire();
						return true;
					}
				}else if(getNearestFire().isOnScreen()){
					return true;
				}
			}
			return false;
		}
    }
	
	private class TakeItems implements Task, Condition{

		@Override
		public void run() {
			if(attackCow.validate()){
				cowAttacker.run();
				return;
			}
			if(player.getHpPercent()<=25){
				if(GroundItems.getNearest(RAW_MEAT)!=null){
					if(Inventory.getCount()>=26){
						return;
					}
					if(!GroundItems.getNearest(RAW_MEAT).isOnScreen()){
						Camera.turnTo(GroundItems.getNearest(RAW_MEAT));
					}
					status = "[Drops]: Getting some Raw meat...";
					if(!GroundItems.getNearest(RAW_MEAT).getLocation().canReach()){
						walkTo(GroundItems.getNearest(RAW_MEAT));
						Time.sleep(1000,2000);
					}
					GroundItems.getNearest(RAW_MEAT).interact("Take","Raw beef");
					Time.sleep(30,100);
				}
			}else{
				/*if(GroundItems.getNearest(BONES)!=null){
					if(Inventory.getCount()>=27){
						//return;
					}else{
						if(!GroundItems.getNearest(BONES).isOnScreen()){
							Camera.turnTo(GroundItems.getNearest(BONES));
						}
						status = "[Drops]: Getting some Bones...";
						if(!GroundItems.getNearest(BONES).getLocation().canReach()){
							walkTo(GroundItems.getNearest(BONES));
							Time.sleep(10,20);
						}
						Camera.turnTo(GroundItems.getNearest(BONES));
						GroundItems.getNearest(BONES).interact("Take", "Bones");//.interact("Take");
						Time.sleep(20,100);
					}
					
				}
				if(GroundItems.getNearest(RAW_MEAT)!=null){
					if(Inventory.getCount()>=26){
						return;
					}
					if(!GroundItems.getNearest(RAW_MEAT).isOnScreen()){
						Camera.turnTo(GroundItems.getNearest(RAW_MEAT));
					}
					status = "[Drops]: Getting some Raw meat...";
					if(!GroundItems.getNearest(RAW_MEAT).getLocation().canReach()){
						walkTo(GroundItems.getNearest(RAW_MEAT));
						Time.sleep(100,200);
					}
					GroundItems.getNearest(RAW_MEAT).interact("Take");
					Time.sleep(20,100);
				}*/
			}
		}

		@Override
		public boolean validate() {
			if(attackCow.validate()){
				cowAttacker.run();
				return false;
			}
			if(player.isIdle()&&!player.isInCombat()&&!player.isMoving()
				&& !doingSomething){
				if(player.getHpPercent()<=25){
					if(GroundItems.getNearest(RAW_MEAT)!=null){
						if(Inventory.getCount()<25){
							return true;
						}
					}
				}else if(GroundItems.getNearest(RAW_MEAT)!=null || GroundItems.getNearest(BONES)!=null)
					if(GroundItems.getNearest(BONES)!=null){
						if(Inventory.getCount()<27){
							return true;
						}
					}
					if(Inventory.getCount()<25){
						return true;
					}
			}
			return false;
		}
	
	}
	
	private class BoneBurrier implements Task, Condition{
		@Override
		public void run() {
			if(Inventory.contains(BONES)&&doingNothing()){
				while(Inventory.contains(BONES)&&doingNothing()){
					status = "[Bones]: Burying bones...";
					Inventory.getItem(BONES).getWidgetChild().interact("Bury");
					bonesBurried++;
					Time.sleep(300,500);
				}
			}
		}

		@Override
		public boolean validate() {
			if(attackCow.validate()){
				cowAttacker.run();
				return false;
			}
			if(player.isIdle()&&!player.isInCombat()&&!player.isMoving()
					&& !doingSomething && doingNothing()){
				if(Inventory.contains(BONES)){
					return true;
				}
			}
			return false;
		}
    }
	
	private class EatFood implements Task, Condition{
		@Override
		public void run() {
			while(Players.getLocal().getHpPercent()<=75&&Inventory.contains(2142))
				Inventory.getItem(2142).getWidgetChild().click(true);
		}
		
		@Override
		public boolean validate() {
			if(Players.getLocal().getHpPercent()<=25&&Inventory.contains(2142)){
				status = "[Food]: Eating food...";
				return true;
			}else if(Players.getLocal().getHpPercent()<=25&&!Inventory.contains(2142)){
				cookMeat.validate();
				return false;
			}
			return false;
		}
    	
    }
	private class GetUnknownStatus implements Task, Condition{

		@Override
		public void run() {
			if(player.isIdle()&&!player.isInCombat()&&!player.isMoving())
				status = "[Main]: Idling...";
			//Camera.setNorth();
		}

		@Override
		public boolean validate() {
			if(attackCow.validate()){
				cowAttacker.run();
				return false;
			}
			if(player.isIdle()){
				return true;
			}
			return false;
		}
		
	}
}