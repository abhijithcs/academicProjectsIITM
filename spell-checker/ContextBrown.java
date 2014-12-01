import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class ContextBrown {

	static Map<String,contextWords> trigrams=new HashMap<String,contextWords>();
static Map<String,Integer> wordCount=new HashMap<String,Integer>();
public Map<String,contextWords> getAllfiles() throws Exception
{
File folder = new File("brown");
File[] listOfFiles = folder.listFiles();

for (File file : listOfFiles) {
    if (file.isFile() && file.getName().length()==4) {
    	OneCounts(file.getName());
    	
    }
}

for (File file : listOfFiles) {
    if (file.isFile() && file.getName().length()==4) {
    //	System.out.println(file.getName());
    	trigrams= readTrigrams(file.getName());
    	
    }
}
//System.out.println("files over");
//System.out.println("abt to exit post");
return trigrams;
}




public Map<String,contextWords> readTrigrams(String filename) throws Exception{
	BufferedReader br = new BufferedReader(new FileReader("brown/"+filename));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(" ");
            line = br.readLine();
        }
        String everything = sb.toString().trim().toLowerCase();
        String[] words1 = everything.split("\\s+");
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(words1));
     words=removeLots(words);
        //words.removeAll(Collections.singleton("men/nns"));
        int i;
       
        boolean dot=false;
     //   if(words.contains("moon/nn") && words.contains("earth/nn")) {System.out.println("file is "+filename);}    
        for(i=0;i<words.size();i++)
        {
        	//System.out.println(words[i]+" "+words[i+1]+" "+words[i+2]);
        	if(words.get(i).equals("./.")) continue;
        	ArrayList<String>neighbours=new ArrayList<String>();
        	
        	//Triwords t=new Triwords(words[i].split("/")[0],words[i+1].split("/")[0],words[i+2].split("/")[0],words[i].split("/")[1],words[i+1].split("/")[1],words[i+2].split("/")[1]);
int k;        	
        	for( k=-3;k<=3;k++)
            	if((i+k)>=0 &&  (i+k) < words.size() && words.get(i+k).equals("./.") && k!=0  )
        		{
            		break;
        		
        		}
        	int a=3;
        	if(k==4) k=-3;
        	else if(k<-1) k=k+1;
        	else if(k==-1) k=1;
        	else if(k==2 || k==3 ) {a=k-1;k=-3;}
        	else if(k==1) {a=-1;k=-3;}
        	for( ;k<=a;k++)
        	if((i+k)>=0 && (i+k) < words.size() && wordCount.get(words.get(i+k).split("/")[0])>10 &&  wordCount.get(words.get(i+k).split("/")[0])<700 )
    		{
        		if(k==0) continue;
    		neighbours.add(words.get(i+k).split("/")[0]);
 
    		}
    	
    	
    	
    
        	if(trigrams.containsKey(words.get(i).split("/")[0]))
        	{
        		//System.out.println(words[i]);
        		if(wordCount.get(words.get(i).split("/")[0])>700) 
        		continue;
        		//trigrams.put(words[i], trigrams.get(words[i]) + 1);
        	contextWords t_old=	(contextWords)trigrams.get(words.get(i).split("/")[0]);
        	Map<String,Integer>n=new HashMap<String,Integer>();
     
        	n=t_old.words;
    	 
    	        	int p,j;
    	        	int val=0;
    	        	for(j=0;j<neighbours.size();j++)
    	        	{
    	        		int match=0;
    	         
    	        		if(words.get(i).split("/")[0].equalsIgnoreCase(neighbours.get(j).split("/")[0])) continue;
    	        			
    	        			if(n.containsKey(neighbours.get(j).split("/")[0]))
    	        			{
    	                         val=n.get(neighbours.get(j).split("/")[0]);
    	        				match=1;
    	        		
    	        				
    	        			}
    		        	
    	        		if(match==1)
    	        		n.put(neighbours.get(j).split("/")[0].toString(),val+1);
    	        		else
    	        			n.put(neighbours.get(j).split("/")[0].toString(),1);
    	        	
    	        	}
    	        	contextWords cont=new contextWords(n);
            		trigrams.put(words.get(i).split("/")[0].toString(), cont);
            		
        		
        
        
        }
        	else
        	{
        		if(wordCount.get(words.get(i).split("/")[0])>700) 
            		continue;
        		
        		Map<String,Integer>n=new HashMap<String,Integer>();
        		int j;
	        	for(j=0;j<neighbours.size();j++)
	        	{
	     
	        		if(words.get(i).split("/")[0].toString().equals(neighbours.get(j).split("/")[0].toString())) continue;
	        		n.put(neighbours.get(j).toString(),1);
	        		
	        		
	        	}
	        	contextWords cont=new contextWords(n);
        		trigrams.put(words.get(i).split("/")[0].toString(), cont);
        		
        	}
    } 
    }
        finally {
        br.close();
    }
	return trigrams;
	
	
	
	
}






public void OneCounts(String filename) throws Exception{
	
	BufferedReader br = new BufferedReader(new FileReader("brown/"+filename));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(" ");
            line = br.readLine();
        }	
        String everything = sb.toString().trim().toLowerCase();
        String[] words1 = everything.split("\\s+");
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(words1));
        int i;
        for(i=0;i<words.size();i++)
        {
        
        	if(wordCount.containsKey(words.get(i).split("/")[0]))
        	{

        		wordCount.put(words.get(i).split("/")[0], wordCount.get(words.get(i).split("/")[0])+1);
        	}
        	else
    		{
    		wordCount.put(words.get(i).split("/")[0], 1);
    		
    		}
        }
    }
        finally{}
        return;
}



private  ArrayList<String> removeLots( ArrayList<String> words) {
	
	
	
	   words.removeAll(Collections.singleton("the/at"));
       words.removeAll(Collections.singleton("the/at-tl"));
       words.removeAll(Collections.singleton("of/in"));
       words.removeAll(Collections.singleton("and/cc"));
       words.removeAll(Collections.singleton("a/at"));
       words.removeAll(Collections.singleton(",/,"));
       words.removeAll(Collections.singleton("--/--"));
       //words.removeAll(Collections.singleton("./."));
       words.removeAll(Collections.singleton("and/cc"));
       words.removeAll(Collections.singleton("to/to"));
       words.removeAll(Collections.singleton("in/in"));
       words.removeAll(Collections.singleton("to/in"));
       words.removeAll(Collections.singleton("in/rp"));
       words.removeAll(Collections.singleton("in/in"));
       words.removeAll(Collections.singleton("is/bez"));
       words.removeAll(Collections.singleton("it/pps"));
       words.removeAll(Collections.singleton("for/in"));
       words.removeAll(Collections.singleton("was/bedz"));
       words.removeAll(Collections.singleton("it/pps"));
       words.removeAll(Collections.singleton("that/cs"));
       words.removeAll(Collections.singleton("he/pps"));
       words.removeAll(Collections.singleton("''/''"));
       words.removeAll(Collections.singleton("``/``"));
       words.removeAll(Collections.singleton("as/cs"));
       words.removeAll(Collections.singleton("as/ql"));
       words.removeAll(Collections.singleton("his/pp$"));
       words.removeAll(Collections.singleton("with/in"));
       words.removeAll(Collections.singleton("by/in"));
       words.removeAll(Collections.singleton("had/hvd"));
       words.removeAll(Collections.singleton("on/in"));
       words.removeAll(Collections.singleton("this/dt"));
       words.removeAll(Collections.singleton("be/be"));
       words.removeAll(Collections.singleton(";/."));
       words.removeAll(Collections.singleton("i/ppss"));
       words.removeAll(Collections.singleton("on/rp"));
       words.removeAll(Collections.singleton("at/in"));
       words.removeAll(Collections.singleton("into/in"));
       words.removeAll(Collections.singleton(":/:"));
       words.removeAll(Collections.singleton("all/abn"));
       words.removeAll(Collections.singleton("you/ppss"));
       words.removeAll(Collections.singleton("which/wdt"));
       words.removeAll(Collections.singleton("then/rb"));
       words.removeAll(Collections.singleton("an/at"));
       words.removeAll(Collections.singleton("they/ppss"));
       words.removeAll(Collections.singleton("but/cc"));
       words.removeAll(Collections.singleton("more/ap"));
       words.removeAll(Collections.singleton("may/md"));
       words.removeAll(Collections.singleton("its/pp$"));
       words.removeAll(Collections.singleton("not/*"));
       words.removeAll(Collections.singleton("only/rb"));
       words.removeAll(Collections.singleton("will/md"));
       words.removeAll(Collections.singleton("him/ppo"));
       words.removeAll(Collections.singleton("any/dti"));
       words.removeAll(Collections.singleton("are/ber"));
       words.removeAll(Collections.singleton("more/ql"));
       words.removeAll(Collections.singleton("if/cs"));
       words.removeAll(Collections.singleton("have/hv"));
       words.removeAll(Collections.singleton("so/ql"));
       words.removeAll(Collections.singleton("there/ex"));
       words.removeAll(Collections.singleton("which/wdt"));
       words.removeAll(Collections.singleton("or/cc"));
       words.removeAll(Collections.singleton("(/("));
       words.removeAll(Collections.singleton(")/)"));
       words.removeAll(Collections.singleton("out/in"));
       words.removeAll(Collections.singleton("these/dts"));
       words.removeAll(Collections.singleton("about/rb"));
       words.removeAll(Collections.singleton("her/pp$"));
       words.removeAll(Collections.singleton("were/bed"));
       words.removeAll(Collections.singleton("up/rp"));
       words.removeAll(Collections.singleton("?/."));
       words.removeAll(Collections.singleton("would/md"));
       words.removeAll(Collections.singleton("has/hvz"));
       words.removeAll(Collections.singleton("from/in"));
       words.removeAll(Collections.singleton("that/dt"));
       words.removeAll(Collections.singleton("from/in-tl"));
       words.removeAll(Collections.singleton("their/pp$"));
       words.removeAll(Collections.singleton("we/ppss"));
       words.removeAll(Collections.singleton("she/pps"));
       words.removeAll(Collections.singleton("it/ppo"));
       words.removeAll(Collections.singleton("when/wrb"));
       words.removeAll(Collections.singleton("who/wps"));
       words.removeAll(Collections.singleton("been/ben"));
       words.removeAll(Collections.singleton("no/ql"));
       words.removeAll(Collections.singleton("one/cd"));
       words.removeAll(Collections.singleton("no/at"));
       words.removeAll(Collections.singleton("no/rb-nc"));
       words.removeAll(Collections.singleton("it/pps-hl"));
       words.removeAll(Collections.singleton("she/pps-nc"));
       words.removeAll(Collections.singleton("do/do"));
       words.removeAll(Collections.singleton("out/rp"));
       words.removeAll(Collections.singleton("that/wps"));
       words.removeAll(Collections.singleton("of/in-tl"));
       words.removeAll(Collections.singleton("also/rb"));
       words.removeAll(Collections.singleton("our/pp$"));
       words.removeAll(Collections.singleton("than/cs"));
       words.removeAll(Collections.singleton("such/jj"));
       words.removeAll(Collections.singleton("like/cs"));
       words.removeAll(Collections.singleton("what/wdt"));
       words.removeAll(Collections.singleton("could/md"));
       words.removeAll(Collections.singleton("some/dti"));
       words.removeAll(Collections.singleton("them/ppo"));
       words.removeAll(Collections.singleton("!/."));
       words.removeAll(Collections.singleton("could/md"));
       words.removeAll(Collections.singleton("said/vbd"));
       words.removeAll(Collections.singleton("new/jj"));
       words.removeAll(Collections.singleton("new/jj-tl"));
       words.removeAll(Collections.singleton("can/md"));
       words.removeAll(Collections.singleton("other/ap"));
       words.removeAll(Collections.singleton("time/nn"));
       words.removeAll(Collections.singleton("time/nn-tl"));
       words.removeAll(Collections.singleton("about/in"));
       words.removeAll(Collections.singleton("my/pp$"));
       words.removeAll(Collections.singleton("must/md"));
       words.removeAll(Collections.singleton("after/in"));
       words.removeAll(Collections.singleton("over/in"));
       words.removeAll(Collections.singleton("me/ppo"));
     //  words.removeAll(Collections.singleton("man/nn"));
       words.removeAll(Collections.singleton("over/rp"));
       words.removeAll(Collections.singleton("did/dod"));
       words.removeAll(Collections.singleton("before/cs"));
       words.removeAll(Collections.singleton("after/cs"));
       words.removeAll(Collections.singleton("first/rb"));
       words.removeAll(Collections.singleton("made/vbd"));
       words.removeAll(Collections.singleton("so/rb"));
       words.removeAll(Collections.singleton("many/ap"));
       words.removeAll(Collections.singleton("most/ql"));
       words.removeAll(Collections.singleton("even/rb"));
       words.removeAll(Collections.singleton("her/ppo"));
       words.removeAll(Collections.singleton("first/od"));
       words.removeAll(Collections.singleton("now/rb"));
       words.removeAll(Collections.singleton("two/cd"));
       words.removeAll(Collections.singleton("two/cd-hl"));
       words.removeAll(Collections.singleton("those/dts"));
       words.removeAll(Collections.singleton("where/wrb"));
       words.removeAll(Collections.singleton("people/nns"));
       words.removeAll(Collections.singleton("just/rb"));
       words.removeAll(Collections.singleton("your/pp$"));
      // words.removeAll(Collections.singleton("good/jj"));
       words.removeAll(Collections.singleton("how/wrb"));
       words.removeAll(Collections.singleton("just/rb"));
       words.removeAll(Collections.singleton("you/ppo"));
       words.removeAll(Collections.singleton("mr./np"));
       words.removeAll(Collections.singleton("back/rb"));
       words.removeAll(Collections.singleton("through/in"));
       words.removeAll(Collections.singleton("down/rp"));
       words.removeAll(Collections.singleton("should/md"));
       words.removeAll(Collections.singleton("way/nn"));
       words.removeAll(Collections.singleton("because/cs"));
       words.removeAll(Collections.singleton("much/ap"));
       words.removeAll(Collections.singleton("well/rb"));
       words.removeAll(Collections.singleton("little/ap"));
       words.removeAll(Collections.singleton("little/ql"));
       words.removeAll(Collections.singleton("each/dt"));
       words.removeAll(Collections.singleton("years/nns"));
       words.removeAll(Collections.singleton("af/nn"));
       words.removeAll(Collections.singleton("too/ql"));
       words.removeAll(Collections.singleton("own/jj"));
       words.removeAll(Collections.singleton("very/ql"));
       words.removeAll(Collections.singleton("very/ap"));
       words.removeAll(Collections.singleton("still/rb"));
       words.removeAll(Collections.singleton("here/rb"));
       words.removeAll(Collections.singleton("get/vb"));
       words.removeAll(Collections.singleton("see/vb"));
       words.removeAll(Collections.singleton("being/beg"));
       words.removeAll(Collections.singleton("state/nn-tl"));
       words.removeAll(Collections.singleton("make/vb"));
       words.removeAll(Collections.singleton("between/in"));
       words.removeAll(Collections.singleton("both/abx"));
       words.removeAll(Collections.singleton("long/jj"));
       words.removeAll(Collections.singleton("under/in"));
       words.removeAll(Collections.singleton("world/nn"));
       words.removeAll(Collections.singleton("life/nn"));
       words.removeAll(Collections.singleton("work/nn"));
       return words;
}

}