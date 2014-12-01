import static java.lang.Math.min;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.rmi.CORBA.Util;


public class WordSpell {
static int[][] d;
	public WordSpell() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line,phrase;
		int others;
		TestBKTree BK=new TestBKTree();
		Map<String,Long> results=null;
		int i;
		edit1 edit=new edit1();
		Map<String,Map<String,Long>> everything=edit.readLines("count_1edit.txt");
	     
//		for (String ke : everything.keySet()) {
//			if(ke.equals("t"))
//			{
//				Map<String,Long> mim=everything.get("t");
//				for (String ki : mim.keySet()) {
//					System.out.println(ke+" "+ki+" count "+mim.get(ki));
//				}
//			}
//		 
//			// ...
//		}
		ContextBrown brown=new ContextBrown();
		Map<String,contextWords> context_brown=brown.getAllfiles();
		System.out.println("brown over");
		CollocationCoca coll=new CollocationCoca();
	
		Map<String,Map<String,Integer>> tagger=coll.tagWords();
		//Map<String,Integer> MoreOften = coll.OneCounts("w5c.txt");
		Map<String, Collocation> CollocationMap=coll.getCollocation("w5c.txt");
		
		ContextCoca coca=new ContextCoca();
		Map<String,contextWords> context_coca=coca.getAllfiles();
		System.out.println("coca over");
		System.out.println("Enter line");
		while ((phrase = br.readLine()) != null) {
			HashMap<String,Float> topCandidates=new HashMap <String,Float>();
		     ValueComparator bvc =  new ValueComparator(topCandidates);
		        TreeMap<String,Float> sorted_map = new TreeMap<String,Float>(bvc);
			// process the line.
			try {
				phrase=phrase.toLowerCase();
				String[] tokens=phrase.split("\\s+");
				for(i=0;i<tokens.length;i++)
				{
					results=BK.performance(tokens[i]);
					if(results.containsKey(tokens[i]))
					{
					//	System.out.println("sense of "+tokens[i]+" is "+coll.getSense(tagger, tokens[i]));
						continue;
					
					}

				
				/*** results -confusion set ***/
				//	System.out.println(Arrays.toString(results.toArray()));
				//String soundexWrong=Soundex.soundex(tokens[i]);
				//System.out.println(tokens[i]+" "+soundexWrong);

				Iterator it = results.entrySet().iterator();
				long sum = 0;
				while (it.hasNext()) {

					Map.Entry pairs = (Map.Entry)it.next();
					sum += (Long) pairs.getValue();
				}
				System.out.println("======");
			//	System.out.println("sum is"+sum);
			//	System.out.println("brown start context");
				/*****iterating over confusion words ***/
				it = results.entrySet().iterator();
				HashMap<String,Float> brownresults=new HashMap <String,Float>();
				HashMap<String,Float> cocaresults=new HashMap <String,Float>();
				float brownsum=0;
				float cocasum=0;
				while (it.hasNext()) {
					
					Map.Entry pairs = (Map.Entry)it.next();
					String element = (String)pairs.getKey();
				//	System.out.println("prior of "+element+" is "+(float) (results.get(element)*1.0/sum));
					
					d=distance(tokens[i], element);
					int row=tokens[i].length();
					int col=element.length();
					float likelihood=1;
//					for(int rowi=1;rowi<=row;rowi++)
//					{
//						for(int colj=1;colj<=col;colj++)
//							System.out.print(d[rowi][colj]+" ");
//						System.out.println();
//						
//					}
					String wrong=tokens[i];
					String actual,typed;
					while(row>0&&col>0)
					{
						if(d[row][col]==(d[row-1][col-1]) && wrong.charAt(row-1)==element.charAt(col-1))
						{
							row--;
							col--;
						}
						else if(d[row][col]==(d[row-1][col-1]+1))
						{
						
							actual=Character.toString(element.charAt(col-1));
							typed=Character.toString(wrong.charAt(row-1));
			//				System.out.println(actual+" typed sub as "+typed);
							long num=	edit.numerator(everything,actual,typed);
							long den=	edit.denominator(everything,actual);
						//	System.out.println(actual+" typed as del "+typed+" "+num+" "+den);
							likelihood*=(float) num*1.0/den;
							row--;
							col--;
						}
						else if(d[row][col]==(d[row-1][col]+1))
						{
		//System.out.println(wrong.charAt(row-2)+" typed as ins "+wrong.charAt(row-2)+wrong.charAt(row-1));
		actual=Character.toString(wrong.charAt(row-2));
		typed=Character.toString(wrong.charAt(row-2))+Character.toString(wrong.charAt(row-1));				
		long num=	edit.numerator(everything,actual,typed);
		long den=	edit.denominator(everything,actual);
	//	System.out.println(actual+" typed as del "+typed+" "+num+" "+den);
		likelihood*=(float) num*1.0/den;
		row--;
							
						}
						else if(d[row][col]==(d[row][col-1]+1))
						{
							
							actual=Character.toString(element.charAt(col-2))+Character.toString(element.charAt(col-1));
							typed=Character.toString(element.charAt(col-2));
						
						long num=	edit.numerator(everything,actual,typed);
						long den=	edit.denominator(everything,actual);
					//	System.out.println(actual+" typed as del "+typed+" "+num+" "+den);
						likelihood*=(float) num*1.0/den;	
						col--;
						}
						else if(d[row][col]==(d[row-2][col-2]+1))
								{
							row=row-2;
							col=col-2;
								}
					//	System.out.println("likelihood intermediate is "+likelihood);
					}
					while(row>0)
					{
						actual=Character.toString(wrong.charAt(row-1));
						typed=Character.toString(wrong.charAt(row-1))+Character.toString(wrong.charAt(row));
					long num=	edit.numerator(everything,actual,typed);
					long den=	edit.denominator(everything,actual);
					//System.out.println(actual+" typed as del "+typed+" "+num+" "+den);
					likelihood*=(float) num*1.0/den;		
				//	System.out.println(row+"==="+wrong.charAt(row-1)+" typed as "+wrong.charAt(row-1)+wrong.charAt(row));
						row--;
					}
					while(col>0)
					{
						actual=Character.toString(element.charAt(col-1))+Character.toString(element.charAt(col));
						typed=Character.toString(element.charAt(col));
					long num=	edit.numerator(everything,actual,typed);
					long den=	edit.denominator(everything,actual);
				//	System.out.println(actual+" typed as del "+typed+" "+num+" "+den);
					likelihood*=(float) num*1.0/den;	
				//	System.out.println(col+"===="+element.charAt(col-1)+element.charAt(col)+" typed as "+element.charAt(col));
					col--;
				}
				//	System.out.println("element likelihood is "+element+" "+likelihood);
					//System.out.println("total is for "+element+" score "+ (float) (results.get(element)*likelihood/sum));
				if(tokens.length==1)
					topCandidates.put(element, (float) (results.get(element)*likelihood/sum))	;
				else
					topCandidates.put(element, (float) 1.0 /**(float) (results.get(element)*1.0)*/ )	;
				}
				
				sorted_map.putAll(topCandidates);
				//float fifthKey = sorted_map.lowerValue(sorted_map.lowerValue(sorted_map.firstValue()));
				
			//	System.out.println("sorted map is "+sorted_map);
				
			//  String soundexConfusion=Soundex.soundex(element);
				//  if(soundexConfusion.equals(soundexWrong))
				  //  System.out.println(element+" "+soundexConfusion);

		//		Iterator iterator = context_words.keySet().iterator();  
				/*** iterating over confusion words context words ***/
				//  while (iterator.hasNext()) {  
				//  String key = (String) iterator.next();
				//if(key.equals(element))
				//{
				//   System.out.println("element is"+element);
				
				
				
				it = results.entrySet().iterator();
				while (it.hasNext()) {
					
					Map.Entry pairs = (Map.Entry)it.next();
					String element = (String)pairs.getKey();
					contextWords value = context_brown.get(element);  
					if(value==null)continue;
					
					 Scoring sbrown=new Scoring();
		               float score=       sbrown.calculateLikelihood(context_brown,element,tokens);
					for(others=0;others<tokens.length;others++)
					{
						if(others==i) continue;
						if(value.words.containsKey(tokens[others]))
						{
							
						//	System.out.print("element "+element +" context "+tokens[others]);
							
							
							for (Map.Entry<String, Float> entry : sorted_map.entrySet()) {
							    String key = entry.getKey();
							    Float bayesian = entry.getValue();
							    if(key.equals(element))
							    //System.out.print(" brown score "+(score*bayesian));
							    {
							    	brownresults.put(element,score*bayesian);
							    brownsum+=score*bayesian;
							    }
							    // ...
							}
							
						//	System.out.println(" with count "+value.words.get(tokens[others]));
						//tokens[i]=element;
						}
					}
					//}  
					//    }
				}
				
				
				
				System.out.println("begin coca context");
				it = results.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
					String element = (String)pairs.getKey();
			//	System.out.println("checking for "+element);
					contextWords value = context_coca.get(element);  
					if(value==null)
						{
				//		System.out.println("val is null");
						continue;
						}
                      int context_iterator;
                  //    System.out.println("context of candidate "+element);
                     
                   //   va.printContext();
                      Scoring s=new Scoring();
               float score=       s.calculateLikelihood(context_coca,element,tokens);
			//	System.out.println("sorted map is "+sorted_map);
               for(others=0;others<tokens.length;others++)
					{
						if(others==i) continue;
					//	System.out.println("token are"+tokens[others]);
						if(value.words.containsKey(tokens[others]))
							{
							
							//	System.out.print("element "+element +" context "+tokens[others]);
								
								
								for (Map.Entry<String, Float> entry : sorted_map.entrySet()) {
								    String key = entry.getKey();
								    Float bayesian = entry.getValue();
								    if(key.equals(element))
								    {
								    	if(CollocationMap.containsKey(element))
								    	{
								    		Collocation collocation=CollocationMap.get(element);
								    	
								    	Map<String,Integer> collocationWords=collocation.words;
								    	
					//			    	System.out.println("collocationWords"+collocationWords);
								    	String left=null,left2=null,mid=null,right=null,right2=null;
								    	//System.out.println("i is "+i);
								    	if(i-2>=0 && i< tokens.length)
								    	left=coll.getSense(tagger, tokens[i-2])+" "+coll.getSense(tagger,tokens[i-1])+" "+coll.getSense(tagger,element);
								    	if(i-1>=0 && (i+1)< tokens.length)
								    	mid=coll.getSense(tagger, tokens[i-1])+" "+coll.getSense(tagger,element)+" "+coll.getSense(tagger,tokens[i+1]);
								    	if(i>=0 && (i+2)< tokens.length)
								    	right=coll.getSense(tagger, element)+" "+coll.getSense(tagger,tokens[i+1])+" "+coll.getSense(tagger,tokens[i+2]);
								    	if(i-1>=0 && i< tokens.length)
								    	left2=coll.getSense(tagger, tokens[i-1])+" "+coll.getSense(tagger,element);
								    	if(i>=0 && (i+1)< tokens.length)
								    	right2=coll.getSense(tagger, element)+" "+coll.getSense(tagger,tokens[i+1]);
//								    System.out.println("left is"+left);
//								    System.out.println("left 2 is "+left2);
//								    System.out.println("right is"+right);
//								    System.out.println("right 2 is "+right2);
//								    System.out.println("mid is "+mid);
								    if(collocationWords.containsKey(left)||collocationWords.containsKey(right2)||collocationWords.containsKey(left2)||collocationWords.containsKey(mid)||collocationWords.containsKey(right))	
								    	System.out.println("coca tells "+element+" with score "+score*bayesian);
								    	cocaresults.put(element,score*bayesian);
								    	 cocasum+=score*bayesian;
								    	}
								
								    }
								    	// ...
								  
								}
							
							//	System.out.println(" with count "+value.words.get(tokens[others]));
							
							}
						
						 
					}
					//}  
					//    }
				}
	
				System.out.println("coca results");
				boolean match =false;
				
				HashMap<String,Float> topCocaCandidates=new HashMap <String,Float>();
			     ValueComparator bvcCoca =  new ValueComparator(cocaresults);
			        TreeMap<String,Float> sorted_map_coca = new TreeMap<String,Float>(bvcCoca);
			        
				sorted_map_coca.putAll(cocaresults);
				SortedMap<String, Float> firstFiveCoca = putFirstEntries(5, sorted_map_coca);
				System.out.println("coca results are"+firstFiveCoca);
				for (Map.Entry<String, Float> entryCoca : sorted_map_coca.entrySet()) {
					match=true;
					
					//if((float) entryCoca.getValue()/cocasum >0.05)
				    //System.out.println("result is "+entryCoca.getKey()+" with score "+(float) entryCoca.getValue()/cocasum);
				
				
				}
				System.out.println("brown results");
				ValueComparator bvcbrown =  new ValueComparator(brownresults);
				 TreeMap<String,Float> sorted_map_brown = new TreeMap<String,Float>(bvcbrown);
				 sorted_map_brown.putAll(brownresults);
				 SortedMap<String, Float> firstFiveBrown = putFirstEntries(5, sorted_map_brown);
					System.out.println("brown results are"+firstFiveBrown);
				 
				 for (Map.Entry<String, Float> entrybrown : sorted_map_brown.entrySet()) {
					match=true;
				//	if((float) entrybrown.getValue()/brownsum >0.1 && !firstFiveCoca.containsKey(entrybrown.getKey()))
					
					//System.out.println("result is "+entrybrown.getKey()+" with score "+(float) entrybrown.getValue()/brownsum);
				}
				if(!match && tokens.length!=1) 
				{
					System.out.println("In case of no context from coca and browns");
					SortedMap<String, Float> firstFive = putFirstEntries(5, sorted_map);
				
				
					for (Map.Entry<String, Float> entry : sorted_map.entrySet()) {
					    String key = entry.getKey();
					    Float bayesian = entry.getValue();
					   // System.out.println("key for last case "+key);
					    	if(CollocationMap.containsKey(key))
					    	{
					    		Collocation collocation=CollocationMap.get(key);
					    	
					    	Map<String,Integer> collocationWords=collocation.words;
					    	
					    	//System.out.println("collocationWords"+collocationWords);
					    	String left=null,left2=null,mid=null,right=null,right2=null;
					    	//System.out.println("i is "+i);
					    	if(i-2>=0 && i< tokens.length)
					    	left=coll.getSense(tagger, tokens[i-2])+" "+coll.getSense(tagger,tokens[i-1])+" "+coll.getSense(tagger,key);
					    	if(i-1>=0 && (i+1)< tokens.length)
					    	mid=coll.getSense(tagger, tokens[i-1])+" "+coll.getSense(tagger,key)+" "+coll.getSense(tagger,tokens[i+1]);
					    	if(i>=0 && (i+2)< tokens.length)
					    	right=coll.getSense(tagger, key)+" "+coll.getSense(tagger,tokens[i+1])+" "+coll.getSense(tagger,tokens[i+2]);
					    	if(i-1>=0 && i< tokens.length)
					    	left2=coll.getSense(tagger, tokens[i-1])+" "+coll.getSense(tagger,key);
					    	if(i>=0 && (i+1)< tokens.length)
					    	right2=coll.getSense(tagger, key)+" "+coll.getSense(tagger,tokens[i+1]);
					  //  System.out.println("left is"+left);
					    //System.out.println("left 2 is "+left);
					    //System.out.println("right is"+right);
					    //System.out.println("right 2 is "+right2);
					    
					    if(collocationWords.containsKey(left)||collocationWords.containsKey(right2)||collocationWords.containsKey(left2)||collocationWords.containsKey(mid)||collocationWords.containsKey(right))	
					    	
					    	System.out.println("possible choice is "+key+"with score"+firstFive.get(key));
					    	}
					    
					    
					    	// ...
					}
				}
				else if(!match)
				{
					SortedMap<String, Float> firstFive = putFirstEntries(5, sorted_map);
					System.out.println("entries possible with scores are"+sorted_map);
				}
				else
				{
					
				}
			}
				System.out.println("Over :)");

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		br.close();
	}


	
    public static int[][] distance(String src, String tgt) {
       
        
        d = new int[src.length() + 1][tgt.length() + 1];
        for (int i = 0; i <= src.length(); i++) {
                d[i][0] = i;
        }
        for (int j = 0; j <= tgt.length(); j++) {
                d[0][j] = j;
        }
        for (int i = 1; i <= src.length(); i++) {
                char sch = src.charAt(i - 1);
                for (int j = 1; j <= tgt.length(); j++) {
                        char tch = tgt.charAt(j - 1);
                        int cost = sch == tch ? 0 : 1;
                        d[i][j] = minimum(d[i - 1][j] + 1,         //deletion
                                                      d[i][j - 1] + 1,         //insertion
                                                      d[i - 1][j - 1] + cost); //substitution
                
                
                if((i-2)>=0 && (j-2)>=0 )
                {
                	if(src.charAt(i-2)==tgt.charAt(j-1)&&src.charAt(i-1)==tgt.charAt(j-2))
                	{
                		if(d[i][j]> (d[i-2][j-2]+1))
                		d[i][j]=d[i-2][j-2]+1;
                	}
                }
                }
        }
        return d;

}
	
	
    private static int minimum(int a, int b, int c) {
        return min(min(a, b), c);
}

    public static SortedMap<String,Float> putFirstEntries(int max, SortedMap<String,Float> source) {
    	  int count = 0;
    	  TreeMap<String,Float> target = new TreeMap<String,Float>();
    	  for (Map.Entry<String,Float> entry:source.entrySet()) {
    	     if (count >= max) break;

    	     target.put(entry.getKey(), entry.getValue());
    	     count++;
    	  }
    	  return target;
    	}
    
}



class ValueComparator implements Comparator<String> {

    Map<String, Float> base;
    public ValueComparator(Map<String, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }
}

