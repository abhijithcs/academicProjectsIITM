import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class CollocationCoca {

static Map<String,Collocation> colloc=new HashMap<String,Collocation>();

static Map<String,Integer> wordCount=new HashMap<String,Integer>();
static Map<String,Map<String,Integer>> tagger=new HashMap<String,Map<String,Integer>>();

CollocationCoca()
{
	
}
	public Map<String, Collocation> getAllfiles() throws Exception
	{
	File folder = new File("cocaCollocation");
	File[] listOfFiles = folder.listFiles();

	
	for (File file : listOfFiles) {
	    if (file.isFile() ) {
	    	System.out.println(file.getName());
	    	colloc= getCollocation(file.getName());
	    	
	    }
	}
//	System.out.println("files over for colloc");
	//System.out.println("abt to exit cocacolloc");
	return colloc;
	}

	
	
	

	
	
	public Map<String, Collocation> getCollocation(String name) throws Exception{
		// TODO Auto-generated method stub
		
		BufferedReader br = new BufferedReader(new FileReader("cocaCollocation/"+name));
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
		       
		//       System.out.println("finished scanning "+name);
		  //     System.out.println(lines.size());
		       Map<String,Integer> m=new HashMap<String,Integer>();
		     	Map<String,Integer>colloc_neighbours=new HashMap<String,Integer>();
		       for(k=0;k<89353;k++)
		       {
		    	   if(k%100000==0) System.out.println("k is "+k);
		    	   String[] words2 = lines.get(k).split("\\s+");
			        ArrayList<String> words = new ArrayList<String>(Arrays.asList(words2));
			        
			        
			        
			        for(i=1;i<6;i++)
			        {

				        	m.clear();
			       
			        if(colloc.containsKey(words.get(i)))
			        {
			        	colloc_neighbours=colloc.get(words.get(i)).words;
			        	
			        	int p;
			        	int val=0;
			        	String mid,left,right;
			        	
			        	/************left collocation ************/	
			        		// w1 w2 w3
			        	    if(i<=3)
			        		if(colloc_neighbours.containsKey(words.get(i)+" "+words.get(i+1)+" "+words.get(i+2)))
			        		colloc_neighbours.put(words.get(i)+" "+words.get(i+1)+" "+words.get(i+2),colloc_neighbours.get(words.get(i)+" "+words.get(i+1)+" "+words.get(i+2))+1);
			        		else
			        			colloc_neighbours.put(words.get(i)+" "+words.get(i+1)+" "+words.get(i+2),1);	
			        		
			        		//w1 w2
			        	    if(i<=4)
			        		if(colloc_neighbours.containsKey(words.get(i)+" "+words.get(i+1)))
				        		colloc_neighbours.put(words.get(i)+" "+words.get(i+1),colloc_neighbours.get(words.get(i)+" "+words.get(i+1))+1);
				        		else
				        			colloc_neighbours.put(words.get(i)+" "+words.get(i+1),1);	
			        		//w1 w2 N
			        	    if(i<=3)
			        		if(colloc_neighbours.containsKey(words.get(i+5)+" "+words.get(i+6)+" "+words.get(i+7)))
				        		colloc_neighbours.put(words.get(i+5)+" "+words.get(i+6)+" "+words.get(i+7),colloc_neighbours.get(words.get(i+5)+" "+words.get(i+6)+" "+words.get(i+7))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+5)+" "+words.get(i+6)+" "+words.get(i+7),1);	
				        	
			        	    
			        	    // w1 N
			        	    if(i<=4)
				        		if(colloc_neighbours.containsKey(words.get(i+5)+" "+words.get(i+6)))
					        		colloc_neighbours.put(words.get(i+5)+" "+words.get(i+6),colloc_neighbours.get(words.get(i+5)+" "+words.get(i+6))+1);
					        		else
					        			colloc_neighbours.put(words.get(i+5)+" "+words.get(i+6),1);	
				        	//w1 N N
			        	    if(i<=3)
				        		if(colloc_neighbours.containsKey(words.get(i)+" "+words.get(i+6)+" "+words.get(i+7)))
					        		colloc_neighbours.put(words.get(i)+" "+words.get(i+6)+" "+words.get(i+7),colloc_neighbours.get(words.get(i)+" "+words.get(i+6)+" "+words.get(i+7))+1);
					        		else
					        			colloc_neighbours.put(words.get(i)+" "+words.get(i+6)+" "+words.get(i+7),1);	
			        	// w1 w2 N
			        	    if(i<=3)
				        		if(colloc_neighbours.containsKey(words.get(i)+" "+words.get(i+1)+" "+words.get(i+7)))
					        		colloc_neighbours.put(words.get(i)+" "+words.get(i+1)+" "+words.get(i+7),colloc_neighbours.get(words.get(i)+" "+words.get(i+1)+" "+words.get(i+7))+1);
					        		else
					        			colloc_neighbours.put(words.get(i)+" "+words.get(i+1)+" "+words.get(i+7),1);	
			        	//w1 N w3
			        	    if(i<=3)
				        		if(colloc_neighbours.containsKey(words.get(i)+" "+words.get(i+6)+" "+words.get(i+2)))
					        		colloc_neighbours.put(words.get(i)+" "+words.get(i+6)+" "+words.get(i+2),colloc_neighbours.get(words.get(i)+" "+words.get(i+6)+" "+words.get(i+2))+1);
					        		else
					        			colloc_neighbours.put(words.get(i)+" "+words.get(i+6)+" "+words.get(i+2),1);	
			        	
			        	/********mid collocation************/
			        	   //wl wm wh
			        	    if(i>=2&&i<=4)
				        		if(colloc_neighbours.containsKey(words.get(i-1)+" "+words.get(i)+" "+words.get(i+1)))
				        		colloc_neighbours.put(words.get(i-1)+" "+words.get(i)+" "+words.get(i+1),colloc_neighbours.get(words.get(i-1)+" "+words.get(i)+" "+words.get(i+1))+1);
				        		else
				        			colloc_neighbours.put(words.get(i-1)+" "+words.get(i)+" "+words.get(i+1),1);	
			        	    //N N N
			        	    if(i>=2&&i<=4)
				        		if(colloc_neighbours.containsKey(words.get(i+4)+" "+words.get(i+5)+" "+words.get(i+6)))
				        		colloc_neighbours.put(words.get(i+4)+" "+words.get(i+5)+" "+words.get(i+6),colloc_neighbours.get(words.get(i+4)+" "+words.get(i+5)+" "+words.get(i+6))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+4)+" "+words.get(i+5)+" "+words.get(i+6),1);	
			        	    //N wm wh
			        	    if(i>=2&&i<=4)
				        		if(colloc_neighbours.containsKey(words.get(i+4)+" "+words.get(i)+" "+words.get(i+1)))
				        		colloc_neighbours.put(words.get(i+4)+" "+words.get(i)+" "+words.get(i+1),colloc_neighbours.get(words.get(i+4)+" "+words.get(i)+" "+words.get(i+1))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+4)+" "+words.get(i)+" "+words.get(i+1),1);	
			        	    //wl wm N
			        	    if(i>=2&&i<=4)
				        		if(colloc_neighbours.containsKey(words.get(i-1)+" "+words.get(i)+" "+words.get(i+6)))
				        		colloc_neighbours.put(words.get(i-1)+" "+words.get(i)+" "+words.get(i+6),colloc_neighbours.get(words.get(i-1)+" "+words.get(i)+" "+words.get(i+6))+1);
				        		else
				        			colloc_neighbours.put(words.get(i-1)+" "+words.get(i)+" "+words.get(i+6),1);	
			        	    //N wm N
			        	    if(i>=2&&i<=4)
				        		if(colloc_neighbours.containsKey(words.get(i+4)+" "+words.get(i)+" "+words.get(i+6)))
				        		colloc_neighbours.put(words.get(i+4)+" "+words.get(i)+" "+words.get(i+6),colloc_neighbours.get(words.get(i+4)+" "+words.get(i)+" "+words.get(i+6))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+4)+" "+words.get(i)+" "+words.get(i+6),1);	
			        	    
			        	    
			        	/*******right collocation***********/
			        	    //w3 w4 w5
			        	    if(i>=3)
				        		if(colloc_neighbours.containsKey(words.get(i-2)+" "+words.get(i-1)+" "+words.get(i)))
				        		colloc_neighbours.put(words.get(i-2)+" "+words.get(i-1)+" "+words.get(i),colloc_neighbours.get(words.get(i-2)+" "+words.get(i-1)+" "+words.get(i))+1);
				        		else
				        			colloc_neighbours.put(words.get(i-2)+" "+words.get(i-1)+" "+words.get(i),1);	
			        	    
			        	    //N N N
			        	    if(i>=3)
				        		if(colloc_neighbours.containsKey(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i+5)))
				        		colloc_neighbours.put(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i+5),colloc_neighbours.get(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i+5))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i+5),1);	
			        	    
			        	    //N w4 w5
			        	    if(i>=3)
				        		if(colloc_neighbours.containsKey(words.get(i+3)+" "+words.get(i-1)+" "+words.get(i)))
				        		colloc_neighbours.put(words.get(i+3)+" "+words.get(i-1)+" "+words.get(i),colloc_neighbours.get(words.get(i+3)+" "+words.get(i-1)+" "+words.get(i))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+3)+" "+words.get(i-1)+" "+words.get(i),1);	
			        	    //w3 N w5
			        	    if(i>=3)
				        		if(colloc_neighbours.containsKey(words.get(i-2)+" "+words.get(i+4)+" "+words.get(i)))
				        		colloc_neighbours.put(words.get(i-2)+" "+words.get(i+4)+" "+words.get(i),colloc_neighbours.get(words.get(i-2)+" "+words.get(i+4)+" "+words.get(i))+1);
				        		else
				        			colloc_neighbours.put(words.get(i-2)+" "+words.get(i+4)+" "+words.get(i),1);	
			        	    //N N w5
			        	    if(i>=3)
				        		if(colloc_neighbours.containsKey(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i)))
				        		colloc_neighbours.put(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i),colloc_neighbours.get(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i))+1);
				        		else
				        			colloc_neighbours.put(words.get(i+3)+" "+words.get(i+4)+" "+words.get(i),1);	
			        	    
			        	    
			        	    
			        	Collocation cont=new Collocation(colloc_neighbours);
		        		colloc.put(words.get(i), cont);
			        }
			        else
			        {
			        	colloc_neighbours=new HashMap<String,Integer>();
			        	Collocation cont=new Collocation(colloc_neighbours);
		        		colloc.put(words.get(i), cont);
			        }
			        
			        }
		       }
		       
		return colloc;
	}
	
	
	
public String getSense (Map<String,Map<String,Integer>> tag,String word)
{
	Map<String,Integer> m=tag.get(word);
	int max=0;
	String sense=null;
	for (Map.Entry<String, Integer> entry : m.entrySet()) {
	    String key = entry.getKey();
	    int value = entry.getValue();
	    if(value > max)
	    {
	    	max=value;
	    	sense =key;
	    }
	    // ...
	}
	return sense;
	
}
	
	
public Map<String,Map<String,Integer>> tagWords() throws Exception{
		
	BufferedReader br = new BufferedReader(new FileReader("cocaCollocation/w5c.txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }	
	        String everything = sb.toString().trim().toLowerCase();
	        
	        
	        String[] words1 = everything.split("\n");
		       
	        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(words1));
	       
int k;
	       for(k=0;k<lines.size();k++)
	       {
	    	//   System.out.println("line is "+lines.get(k));
	    	   String[] words2 = lines.get(k).split("\\s+");
		        ArrayList<String> words = new ArrayList<String>(Arrays.asList(words2));
		        int i;
		        for(i=1;i<=5;i++)
		        {
		        
		        	if(tagger.containsKey(words.get(i)))
		        	{
		        		Map<String,Integer> senses=tagger.get(words.get(i));
		        		if(senses.containsKey(words.get(i+5)))
		        		senses.put(words.get(i+5), senses.get(words.get(i+5))+1);
		        		else
		        			senses.put(words.get(i+5), 1);
		        		tagger.put(words.get(i), senses);
		        	}
		        	else
		    		{
		        		Map<String,Integer> senses =new HashMap<String,Integer>();
		        		senses.put(words.get(i+5), 1);
		    		tagger.put(words.get(i),senses);
		    		
		    		}
		        }
	       }
	        
	        
	    }
	        finally{}
	        return tagger;
	}
	
	
	
	
	
	
	
	
	public Map<String,Integer> OneCounts(String filename) throws Exception{
		
		BufferedReader br = new BufferedReader(new FileReader("cocaCollocation/w5c.txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }	
	        String everything = sb.toString().trim().toLowerCase();
	        
	        
	        String[] words1 = everything.split("\n");
		       
	        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(words1));
	       
int k;
	       for(k=0;k<lines.size();k++)
	       {
	    	   String[] words2 = everything.split("\\s+");
		        ArrayList<String> words = new ArrayList<String>(Arrays.asList(words2));
		        int i;
		        for(i=1;i<=5;i++)
		        {
		        
		        	if(wordCount.containsKey(words.get(i)))
		        	{

		        		wordCount.put(words.get(i), wordCount.get(words.get(i))+1);
		        	}
		        	else
		    		{
		    		wordCount.put(words.get(i), 1);
		    		
		    		}
		        }
	       }
	        
	        
	    }
	        finally{}
	        return wordCount;
	}
}
