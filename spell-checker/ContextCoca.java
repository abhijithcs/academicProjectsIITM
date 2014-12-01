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
import java.util.regex.*;
public class ContextCoca {

	static Map<String,contextWords> trigrams=new HashMap<String,contextWords>();
	
	public Map<String, contextWords> getAllfiles() throws Exception
	{
	File folder = new File("coca");
	File[] listOfFiles = folder.listFiles();

	
	for (File file : listOfFiles) {
	    if (file.isFile() ) {
	  //  	System.out.println(file.getName());
	    	trigrams= readTrigrams(file.getName());
	    	
	    }
	}
	//System.out.println("files over");
	//System.out.println("abt to exit coca");
	return trigrams;
	}

	private Map<String, contextWords> readTrigrams(String name) throws Exception{
		// TODO Auto-generated method stub
		
		BufferedReader br = new BufferedReader(new FileReader("coca/"+name));
	   int i,j,k;
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	      //  sb=removeLots(sb);
	        String everything = sb.toString().trim().toLowerCase();
	        String[] words1 = everything.split("\n");
	       
	        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(words1));
	       
	       //System.out.println("finished scanning "+name);
	       //System.out.println(lines.size());
	       Map<String,Integer> m=new HashMap<String,Integer>();
	     	Map<String,Integer>neighbours=new HashMap<String,Integer>();
	       for(k=0;k<lines.size();k++)
	       {
	    	   
	    	   if(k%1000000==0) System.out.println("k is "+k +" total is "+lines.size());
	    	  // System.out.println("line is "+lines.get(k));
	    	   String[] words2 = lines.get(k).split("\\s+");
		        ArrayList<String> words = new ArrayList<String>(Arrays.asList(words2));
		     	words=removeLots(words);
		        for(i=1;i<words.size();i++)
	        {

		        	m.clear();
	       
	        if(trigrams.containsKey(words.get(i)))
	        {
	 
	        	neighbours=trigrams.get(words.get(i)).words;
	    //    	System.out.println("words before "+words.get(i)+"context"+neighbours.toString()
        	//			);
	        	int p;
	        	int val=0;
	        	for(j=1;j<words.size();j++)
	        	{
	        		int match=0;
	         
	        		if(words.get(i).equalsIgnoreCase(words.get(j))) continue;
	        			
	        			if(neighbours.containsKey(words.get(j)))
	        			{
	        	//			System.out.println("word present in negihbour "+words.get(j));
	        				match=1;
	        				val=neighbours.get(words.get(j));
	        				
	        			}
		        	
	        		if(match==1)
	        		neighbours.put(words.get(j).toString(),val+Integer.parseInt(words.get(0)));
	        		else
	        			neighbours.put(words.get(j).toString(),Integer.parseInt(words.get(0)));
	        	
	        		//System.out.println("words "+words.get(j)+"context"+);
	        	}
	        	contextWords cont=new contextWords(neighbours);
        		trigrams.put(words.get(i), cont);
//        		if(words.get(i).equals("earth"))
//        		{System.out.println("line is "+lines.get(k));
//        			System.out.println("words "+words.get(i)+"context"+neighbours.toString());
//        		}
        			//	);
        		
	        }
	        else
	        {
	        	neighbours=new HashMap<String,Integer>();
	        	for(j=1;j<words.size();j++)
	        	{
	     
	        		if(words.get(i).equals(words.get(j))) continue;
	        		neighbours.put(words.get(j).toString(),Integer.parseInt(words.get(0)));
	        		
	        		
	        	}
	        	contextWords cont=new contextWords(neighbours);
        		trigrams.put(words.get(i), cont);
        		//if(words.get(i).equals("moon"))
        		//System.out.println("words == "+words.get(i)+"context"+neighbours.toString()
        			//	);
        		
        		
	        }
	        }
	        
	       }
	      //  System.out.println("fetched context from coca");
	        return trigrams;
	
}

	private  ArrayList<String> removeLots( ArrayList<String> words) {
		// TODO Auto-generated method stub
	      words.removeAll(Collections.singleton("the"));
	        words.removeAll(Collections.singleton("the/at-tl"));
	        words.removeAll(Collections.singleton("of"));
	        words.removeAll(Collections.singleton("and"));
	        words.removeAll(Collections.singleton("a"));
	        words.removeAll(Collections.singleton(","));
	        words.removeAll(Collections.singleton("--"));
	        //words.removeAll(Collections.singleton("./."));
	        words.removeAll(Collections.singleton("and"));
	        words.removeAll(Collections.singleton("to/to"));
	        words.removeAll(Collections.singleton("in/in"));
	        words.removeAll(Collections.singleton("to"));
	        words.removeAll(Collections.singleton("in/rp"));
	        words.removeAll(Collections.singleton("in"));
	        words.removeAll(Collections.singleton("is"));
	        words.removeAll(Collections.singleton("it"));
	        words.removeAll(Collections.singleton("for"));
	        words.removeAll(Collections.singleton("was"));
	        words.removeAll(Collections.singleton("it"));
	      //  words.removeAll(Collections.singleton("that"));
	        words.removeAll(Collections.singleton("he"));
	  
	        words.removeAll(Collections.singleton("as"));
	        words.removeAll(Collections.singleton("as"));
	        words.removeAll(Collections.singleton("his"));
	        words.removeAll(Collections.singleton("with"));
	        words.removeAll(Collections.singleton("by"));
	        words.removeAll(Collections.singleton("had"));
	        words.removeAll(Collections.singleton("on"));
	        //words.removeAll(Collections.singleton("this"));
	        words.removeAll(Collections.singleton("be"));
	        words.removeAll(Collections.singleton(";"));
	        words.removeAll(Collections.singleton("i"));
	        words.removeAll(Collections.singleton("on"));
	        words.removeAll(Collections.singleton("at"));
	        words.removeAll(Collections.singleton("into"));
	        words.removeAll(Collections.singleton(":"));
	        words.removeAll(Collections.singleton("all"));
	        words.removeAll(Collections.singleton("you"));
	        //words.removeAll(Collections.singleton("which"));
	        //words.removeAll(Collections.singleton("then"));
	        words.removeAll(Collections.singleton("an"));
	        //words.removeAll(Collections.singleton("they"));
	        words.removeAll(Collections.singleton("but"));
	        words.removeAll(Collections.singleton("more"));
	        words.removeAll(Collections.singleton("may"));
	        words.removeAll(Collections.singleton("its"));
	        words.removeAll(Collections.singleton("not"));
	        words.removeAll(Collections.singleton("only"));
	        words.removeAll(Collections.singleton("will"));
	        words.removeAll(Collections.singleton("him"));
	        words.removeAll(Collections.singleton("any"));
	        words.removeAll(Collections.singleton("are"));
	        words.removeAll(Collections.singleton("more/ql"));
	        words.removeAll(Collections.singleton("if/cs"));
	        words.removeAll(Collections.singleton("have/hv"));
	        words.removeAll(Collections.singleton("so/ql"));
	        words.removeAll(Collections.singleton("there/ex"));
	        words.removeAll(Collections.singleton("which/wdt"));
	        words.removeAll(Collections.singleton("or/cc"));
	        words.removeAll(Collections.singleton("(/("));
	        words.removeAll(Collections.singleton(")/)"));
	        words.removeAll(Collections.singleton("out"));
	        //words.removeAll(Collections.singleton("these"));
	        words.removeAll(Collections.singleton("about"));
	        words.removeAll(Collections.singleton("her"));
	        words.removeAll(Collections.singleton("were"));
	        words.removeAll(Collections.singleton("up"));
	        words.removeAll(Collections.singleton("?"));
	        //words.removeAll(Collections.singleton("would"));
	        words.removeAll(Collections.singleton("has"));
	        words.removeAll(Collections.singleton("from"));
	        words.removeAll(Collections.singleton("that"));
	        words.removeAll(Collections.singleton("from/in-tl"));
	        words.removeAll(Collections.singleton("their/pp$"));
	        words.removeAll(Collections.singleton("we"));
	        words.removeAll(Collections.singleton("she"));
	        words.removeAll(Collections.singleton("it"));
	        words.removeAll(Collections.singleton("when"));
	        words.removeAll(Collections.singleton("who"));
	        words.removeAll(Collections.singleton("been"));
	        words.removeAll(Collections.singleton("no"));
	        words.removeAll(Collections.singleton("one"));
	        words.removeAll(Collections.singleton("no"));
	        words.removeAll(Collections.singleton("no/rb-nc"));
	        words.removeAll(Collections.singleton("it/pps-hl"));
	        words.removeAll(Collections.singleton("she"));
	        words.removeAll(Collections.singleton("do"));
	        words.removeAll(Collections.singleton("out"));
	        //words.removeAll(Collections.singleton("that"));
	        words.removeAll(Collections.singleton("of/in-tl"));
	        words.removeAll(Collections.singleton("also/rb"));
	        words.removeAll(Collections.singleton("our/pp$"));
	        //words.removeAll(Collections.singleton("than"));
	        words.removeAll(Collections.singleton("such"));
	        words.removeAll(Collections.singleton("like"));
	        words.removeAll(Collections.singleton("what"));
	        //words.removeAll(Collections.singleton("could"));
	        words.removeAll(Collections.singleton("some"));
	        //words.removeAll(Collections.singleton("them"));
	        words.removeAll(Collections.singleton("!"));
	       // words.removeAll(Collections.singleton("could"));
	        words.removeAll(Collections.singleton("said"));
	        words.removeAll(Collections.singleton("new"));
	        words.removeAll(Collections.singleton("new"));
	        words.removeAll(Collections.singleton("can"));
	        words.removeAll(Collections.singleton("other"));
	        words.removeAll(Collections.singleton("time"));
	        words.removeAll(Collections.singleton("time"));
	        words.removeAll(Collections.singleton("about"));
	        words.removeAll(Collections.singleton("my"));
	        words.removeAll(Collections.singleton("must"));
	        words.removeAll(Collections.singleton("after"));
	        words.removeAll(Collections.singleton("over"));
	        words.removeAll(Collections.singleton("me"));
	        words.removeAll(Collections.singleton("man"));
	        words.removeAll(Collections.singleton("over"));
	        words.removeAll(Collections.singleton("did"));
	        words.removeAll(Collections.singleton("before"));
	        words.removeAll(Collections.singleton("after"));
	        words.removeAll(Collections.singleton("first"));
	        words.removeAll(Collections.singleton("made"));
	        words.removeAll(Collections.singleton("so"));
	        words.removeAll(Collections.singleton("many"));
	        words.removeAll(Collections.singleton("most"));
	        words.removeAll(Collections.singleton("even"));
	        words.removeAll(Collections.singleton("her"));
	        words.removeAll(Collections.singleton("first"));
	        words.removeAll(Collections.singleton("now"));
	        words.removeAll(Collections.singleton("two"));
//	        words.removeAll(Collections.singleton("two"));
	        //words.removeAll(Collections.singleton("those"));
	        //words.removeAll(Collections.singleton("where"));
	        words.removeAll(Collections.singleton("people"));
	        words.removeAll(Collections.singleton("just"));
	        words.removeAll(Collections.singleton("your"));
	     //   words.removeAll(Collections.singleton("good"));
	        words.removeAll(Collections.singleton("how"));
	        words.removeAll(Collections.singleton("just"));
	        words.removeAll(Collections.singleton("you"));
	        words.removeAll(Collections.singleton("mr"));
	        words.removeAll(Collections.singleton("back"));
	        //words.removeAll(Collections.singleton("through"));
	        words.removeAll(Collections.singleton("down"));
	        words.removeAll(Collections.singleton("should"));
	        words.removeAll(Collections.singleton("way"));
	        //words.removeAll(Collections.singleton("because"));
	        words.removeAll(Collections.singleton("much"));
	        words.removeAll(Collections.singleton("well"));
	        words.removeAll(Collections.singleton("little"));
	        words.removeAll(Collections.singleton("little"));
	        words.removeAll(Collections.singleton("each"));
	        words.removeAll(Collections.singleton("years"));
	        words.removeAll(Collections.singleton("af"));
	        words.removeAll(Collections.singleton("too"));
	        words.removeAll(Collections.singleton("own"));
	        words.removeAll(Collections.singleton("very"));
	        words.removeAll(Collections.singleton("very"));
	        words.removeAll(Collections.singleton("still"));
	        words.removeAll(Collections.singleton("here"));
	        words.removeAll(Collections.singleton("get"));
	        words.removeAll(Collections.singleton("see"));
	        words.removeAll(Collections.singleton("being"));
	        words.removeAll(Collections.singleton("state"));
	        words.removeAll(Collections.singleton("make"));
	        words.removeAll(Collections.singleton("between"));
	        words.removeAll(Collections.singleton("both"));
	        words.removeAll(Collections.singleton("long"));
	        words.removeAll(Collections.singleton("under"));
	        words.removeAll(Collections.singleton("world"));
	        words.removeAll(Collections.singleton("life"));
	        words.removeAll(Collections.singleton("work"));
	     //   words.removeAll(Collections.singleton("men"));
		return words;
	}
}
