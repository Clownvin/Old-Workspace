package Bot;
import org.jibble.pircbot.*;
import java.util.Random;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BotClass extends PircBot{
	private String[] inChatWith = new String[10];
	private BufferedWriter bR = null;
	private String[] inChatWithChan = new String[10];
	private boolean[] madeSmartRemarkTo = new boolean[10];
	private boolean[] madeSmartRemarkFrom = new boolean[10];
	private boolean[] justAskedWasup = new boolean[10];
	private boolean[] justSaidHello = new boolean[10];
	private boolean[] justAskedIfBot = new boolean[10];
	private String[] ignoreList = new String[40];
	private String[] jO = new String [40];
	private final String tellMeP = "Vavsucks";
	private boolean tellme = true;
	/**private boolean[] pubBool = new boolean[10];
	private String[] p**/
	private String justAskedWhoMeTo = "";
	private boolean justAskedWhoMe = false;
	private final String[] welcome={"Welcome","Hello","sup"};
	private final String[] happyEmoticons = {":)",":D","(:"};
	private final String[] no={"no","naw","nope"};
	private final String[] why={"why","y"};
	private final String[] wadup={"wadup","whadup","what's up","whats up","whatsup","what'sup","wudup","whudup","wad up"};
	private final String[] bro={"bro","brother","dude","man","brah","broski","brahski","brudah"};
	private final String[] yes={"yes","yeah","yup","ye","ya","mhm"};
	private final String[] areYouABotResponses={yes[random(yes.length)],yes[random(yes.length)]+", i am a bot","i am a bot"};
	private final String[] areYouABotResponses2={/**yes[random(yes.length)]+"!".toUpperCase().replace("MHM","YES").replace("YUP","YES!"),**/yes[random(yes.length)]+", i am a bot. i just told you","i am a bot","i just told you...","i just told you... yes, i am a bot"};
	private final String[] agreementOnNounBeingSomething={yes[random(yes.length)]+", it is",yes[random(yes.length)]};
	private final String[] wadupResponses={"not much really","not much","nothing","well, a bot normally doesnt do anything, so I guess nothing.","nothing that is important enough to talk about","well... nothing. Im just an IRC Bot. We tend not to do much."+happyEmoticons[random(happyEmoticons.length)]};
	private Random random = new Random();
	
	public BotClass(String name){
		setName(name);
		cleanChats();
		cleanJO();
		cleanIgnore();
		try{
			bR=new BufferedWriter(new FileWriter("thread.txt"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private String[] helloResponses = {"Hey","Hai","Ello","Hello","Hi"};
	private String lolRegex = "(.* )?(lol|(ba)?(ha)*|heh(eh?)*)( .*)?",
			   whatsupRegex = "(.* )?((wha|wa|wu)t?)('s*|s*|z*|d*)? ?(up)(\\p{Punct})*",
			   howsitgoingRegex = "(.* )?(how) ?('|i)(s|z) ?it ?(goin('|g)?) ?(eve?ry(1|one|body)|pe?o?ple?(s|z)?|guy(s|z))?\\p{Punct}*",
			   howareyouRegex = "(.* )?(how) ?('?re|a?re?|'?s|is) ?(y?o?u|eve?ry(1|one|body)) ?(doin'?g?)?\\p{Punct}*",
			   hiRegex = "(ha?i|h?ello|hey|howdy|(j|y)o) ?(eve?ry(1|one|body)|pe?o?ple?(s|z)?|guy(s|z))?\\p{Punct}*",
			   levelRegex = "(wha|wa|wu)t(('?s| ?i(s|z)) ?(yo?)?ur ?)? ?le?v(e?l)( ?a?re? ?(yo?)?u)?\\p{Punct}*",
			   nicelevelRegex = "((ni*ce|(c|k)((e?w)|(oo))?l|swee*t|th?at'?s (really )?high|not bad|epic|awe?s(o|u)me?|wow) ?)*\\p{Punct}*",
			   howlongRegex = "how ?long ?have? ?y?o?u ?bee?n ?(trainin('|g)? ?(this|at(t|t?(ac)?k)|str(e?ngth)?|def(e?n(s|c)e)?|pray(er)?|herb(y|lore)?|agil(ity)?|con(st(itution|ruction)?)?|summ?(oning)?)?|doin('|g)? ?(this|at(t|t?(ac)?k)|str(e?ngth)?|def(e?n(s|c)e)?|pray(er)?|herb(y|lore)?|agil(ity)?|con(st(itution|ruction)?)?|summ?(oning)?)|(rang|mag|cook|w(ood ?)?c(utt)?|fle?tch|fish|f(ire ?)?m(ak)?|cra?ft|smi?th|min|th(ie|ei)(f|v)|slay|farm|r(une ?)?c(ra?ft)?|hu?nt|dung(e?oneer)?)(in('|g)?)?)\\p{Punct}?",
			   botRegex = "(.* )?(((bot|botter)|(auto|autoer))s?)( .*| ?\\p{Punct}*)",
			   botsSay = "((.*)( is|((yo?)?u'?| a)re?)(n'?t| not).*(bott?|auto)(er|in('|g)?)?( |, ?)say.*)|(say.*(report|((aren'?t( a )?|don'?t) ?(bott?|auto)(er|in('|g)?)?))\\p{Punct}*)",
			   congratsRegex = "((con)?g(rat(ulation)?|(tar))(s|z)).*",
			   byeRegex = "(b(ai|ye)|l(a?te?|8)r|(see|c) ?((yo)?u|ya)( ?l(a?te?|8)r)?|(g(ot ?)?(to|2)|gotta) ?go?|peace ?out)\\p{Punct}*",
			   welcomebackRegex = "(i'?m )?back\\p{Punct}*",
			   boredRegex = ".*bored.*",
			   boringRegex = ".*borin.*",
			   helloRegex = "(h?(i?|e(y?|llo)|ai)",
			   exptillRegex = ".*e?xp ?((un)?till?|to).*le?ve?l\\p{Punct}*";
	
	
	private int pID(String profile){
		for(int i=0;i<inChatWith.length;i++){
			if(inChatWith[i].equalsIgnoreCase(profile)){
				return i;
			}
		}
		return -1;
	}
	
	private boolean icw(String p){/**inChatWith**/
		for(int i=0;i<inChatWith.length;i++){
			if(inChatWith[i].equalsIgnoreCase(p)){
				return true;
			}
		}
		return false;
	}
	
	private boolean ignored(String p){
		for(int i=0; i<ignoreList.length;i++){
			if(ignoreList[i].equalsIgnoreCase(p)){
				return true;
			}
		}
		return false;
	}
	
	private boolean justOnline(String p){
		for(int i=0; i<jO.length;i++){
			if(jO[i].equalsIgnoreCase(p)){
				return true;
			}
		}
		return false;
	}
	
	public String respond(String input) {
		// If a blank string was sent, return a blank.
		if (input.isEmpty())
			return "";
		
		// Sanitize the input.
		input = input.toLowerCase();
				
		// Mimic laughter
		/*boolean sayLol = Pattern.matches(this.lolRegex, input);
		if (this.mimicLol && sayLol && (this.lolTime == this.firstTime || System.nanoTime() - this.lolTime >= this.tenMins)) {
			this.lolTime = System.nanoTime();
			return this.getRandomArrayValue(this.lols);
		}*/
		boolean saidHello = Pattern.matches(this.helloRegex, input);
		if(saidHello){
			return getRandomArrayValue(helloResponses);
		}
		return "";
	}
	
	private String getRandomArrayValue(String[] arr) {
		return arr[(int)(arr.length*Math.random())];
	}
	
	private void cleanChats(){
		for(int i=0;i<inChatWith.length;i++){
			inChatWith[i]="";
			inChatWithChan[i]="";
		}
	}
	
	private void cleanIgnore(){
		for(int i=0;i<ignoreList.length;i++){
			ignoreList[i]="";
		}
	}
	
	private void cleanJO(){
		for(int i=0;i<jO.length;i++){
			jO[i]="";
		}
	}
	
	private boolean addToIgnore(String p){
		if(tellme){
			sendNotice(tellMeP,"Attempting to add "+p+" to the ignoreList.");
		}
		for(int i=0;i<ignoreList.length;i++){
			if(ignoreList[i].length()<=0){
				ignoreList[i]=p;
				removeFromChats(p);
				if(tellme){
					sendNotice(tellMeP,p+" added to ignoreList.");
				}
				return true;
			}
		}
		if(tellme){
			sendNotice(tellMeP,"ignoreList's is full, cannot add.");
		}
		return false;
	}
	
	private boolean addToJO(String p){
		if(tellme){
			sendNotice(tellMeP,"Attempting to add "+p+" to Just Online.");
		}
		for(int i=0;i<jO.length;i++){
			if(jO[i].length()<=0){
				jO[i]=p;
				if(tellme){
					sendNotice(tellMeP,p+" Just Online.");
				}
				return true;
			}
		}
		if(tellme){
			sendNotice(tellMeP,"Just Online's is full, cannot add.");
		}
		return false;
	}
	
	private boolean addToChats(String p, String channel){
		if(tellme){
			sendNotice(tellMeP,"Attempting to add "+p+" to chats on channel "+channel);
		}
		if(!icw(p)){
			for(int i=0;i<inChatWith.length;i++){
				if(inChatWith[i].length()<=0){
					inChatWith[i]=p;
					inChatWithChan[i]=channel;
					if(tellme){
						sendNotice(tellMeP,p+" added to chats in channel "+channel);
					}
					return true;
				}
			}
		}
		if(tellme){
			sendNotice(tellMeP,"Chat's is full, cannot add.");
		}
		return false;
	}
	
	private boolean removeFromChats(String p){
		int x = 0;
		if(tellme){
			sendNotice(tellMeP,"Attempting to remove "+p+" from chats");
		}
		if(icw(p)){
			x = pID(p);
			inChatWith[x]="";
			inChatWithChan[x]="";
			madeSmartRemarkTo[x]=false;
			madeSmartRemarkFrom[x]=false;
			justSaidHello[x]=false;
			justAskedIfBot[x]=false;
			justAskedWasup[x]=false;
			if(tellme){
				sendNotice(tellMeP,"Succesfully removed "+p+" from chats");
			}
			return true;
		}
		if(tellme){
			sendNotice(tellMeP,"Failed to remove "+p+" from chats");
		}
		return false;
	}
	
	public static void main(String[] args){
		BotClass bot=new BotClass("dudeimmike");
		try{
			bot.setVerbose(true);
			bot.connect("irc.hackthissite.org");
			bot.sendMessage("nickserv","IDENTIFY <your password>" );
			bot.setVersion(bot.getName());
			bot.joinChannel("#bottest");
			//bot.setMode("#bots","+B");
		}catch (Exception e){e.printStackTrace();}
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		if(tellme){
			sendNotice(tellMeP,"Recieved message: "+message+"; Sender: "+sender+"; Channel: "+channel);
			try{
				bR.write("Recieved message: "+message+"; Sender: "+sender+"; Channel: "+channel);
				bR.newLine();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private int random(int i){
		try{
			return random.nextInt(i);
		}catch(NullPointerException npe){
		}
		return 0;
	}
	
	public void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue){
		if(tellme){
			sendNotice(tellMeP,"Just got pinged. Sender: "+sourceNick+"; Ping value: "+pingValue);
		}
		if(!icw(sourceNick)&&!ignored(sourceNick)){
			sendNotice(sourceNick,"don't ping me dude... ffs -----......-----");
		}else if(icw(sourceNick)&&!ignored(sourceNick)){
			sendMessage(inChatWithChan[pID(sourceNick)], "lol, "+sourceNick.toLowerCase()+", did you just ping me? :D");
		}
	}
	
	/**public void onServerPing(String serverResponse){
		sendNotice("VavBot","LOL, Just got Pinged. Server Response:"+serverResponse);
	}**/
	public void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target){
		if(tellme){
			sendNotice(tellMeP,"Just got version request. Sender: "+sourceNick);
		}
		sendNotice(sourceNick,"\001VERSION myBot:1.0:YOURMOM\001");
	}
	
	public void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel){
		if(tellme){
			sendNotice(tellMeP,"Just got invite request. Sender: "+sourceNick+"; Channel: "+channel);
		}
	}
	
	public void onJoin(String channel, String sender, String login, String hostname){
		if(tellme){
			sendNotice(tellMeP,sender+" joined "+channel);
		}
		if(!sender.equalsIgnoreCase(getName())&&!ignored(sender)){
			sendMessage(channel, "hello "/**+sender.toLowerCase()**/);
		}
		if(sender.equalsIgnoreCase(getName())){
			setMode(channel, "+B");
		}
	}
	
	public void onPart(String channel, String sender, String login, String hostname){
		if(tellme){
			sendNotice(tellMeP,sender+" Leaving channel "+channel);
		}
		if(!sender.equalsIgnoreCase(getName())&&!ignored(sender)){
			if(justOnline(sender)){
				addToIgnore(sender);
			}
			removeFromChats(sender);
			sendMessage(channel,"bai "+sender.toLowerCase());
			addToJO(sender);
		}
	}
	
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice){
		if(tellme){
			sendNotice(tellMeP,sourceNick+" sent notice: "+notice);
		}
		if(sourceNick.equalsIgnoreCase("vavsucks")&& notice.contains("change")){
			if(notice.contains("change mode +")){
				setMode("#bottest",notice.replace("change mode ", ""));
				sendNotice(sourceNick,"Mode changed to "+notice.replace("change mode ", ""));
			}
			else if(notice.contains("change name ")){
				setName(notice.replace("change name ", ""));
				sendNotice(sourceNick,"Name changed to "+notice.replace("change name ", ""));
			}
		}
		else if(notice.contains("jChannel = ")){
			if(notice.contains("#")){
				notice = notice.replace("#","");
			}
			joinChannel("#"+notice.replace("jChannel = ", ""));
			sendNotice(sourceNick,"Joined channel #"+notice.replace("jChannel = ", ""));
			//setMode("#"+notice.replace("change jChannel = ", ""),"+B");
		}
		else if(notice.contains("lChannel = ")){
			if(notice.contains("#")){
				notice = notice.replace("#","");
			}
			sendMessage("#"+notice.replace("lChannel = ", ""),"cya");
			partChannel("#"+notice.replace("lChannel = ", ""));
			sendNotice(sourceNick,"Left channel #"+notice.replace("lChannel = ", ""));
		}
		else if(notice.equalsIgnoreCase("tellme")){
			tellme=true;
		}
		else if(notice.equalsIgnoreCase("donttellme")){
			tellme=false;
		}
		else if(notice.equalsIgnoreCase("startrecorder")){
			if(bR==null){
				try{
					bR=new BufferedWriter(new FileWriter(notice.replace("startrecorder","")+".txt"));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else if(notice.equalsIgnoreCase("endrecorder")){
			if(bR!=null){
				try{
					bR.close();
					bR=null;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else if(notice.contains("getCommands")){
			sendNotice(sourceNick,"~Commands~");
			sendNotice(sourceNick,"change mode <mode:String>");
			sendNotice(sourceNick,"change name <name:String>");
			sendNotice(sourceNick,"change jChannel = <channel:String> ~ Joins channel");
			sendNotice(sourceNick,"change lChannel = <channel:String> ~ Leaves channel");
			sendNotice(sourceNick,"getCommands ~ Shows this");
			sendNotice(sourceNick,"tellme ~ Makes me show you whats happn'n");
			sendNotice(sourceNick,"donttellme ~ I won't tell you whats happn'n");
			sendNotice(sourceNick,"startrecorder <FileName:String> ~ Starts chat recorder, if not already started.");
			sendNotice(sourceNick,"endrecorder ~ Finish recording the Chat.");
		}
	}
	
	public void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname){
		
	}
}
