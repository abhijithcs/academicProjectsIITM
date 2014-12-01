import java.util.Iterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CosineSimilarity {

	public CosineSimilarity() {
		// TODO Auto-generated constructor stub
	}
	public Double Dabsolute(HashMap<String,Double> EachDocWordWeight,HashMap<String,Integer> stopwords,HashMap<String,Double> IDF)
	{
		  Iterator it = EachDocWordWeight.entrySet().iterator();
		 it = EachDocWordWeight.entrySet().iterator();
   	  	Double Dabs=0.0;
   	    while (it.hasNext()) {
   	    	
   	        Map.Entry pairs = (Map.Entry)it.next();
   	        if(stopwords.containsKey(pairs.getValue().toString()))
   	        	if(stopwords.get(pairs.getValue().toString())<=2 || stopwords.get(pairs.getValue().toString())>=25) continue;
   	       double D_weight=(Double) pairs.getValue();
   	       if(IDF.containsKey(pairs.getKey()))
   	       {
   	       EachDocWordWeight.put((String) pairs.getKey(),(Double) pairs.getValue()*IDF.get(pairs.getKey()));
   	       Dabs+=EachDocWordWeight.get( pairs.getKey());
   	       }
   	    }
   	 // System.out.println("Dabs is"+Dabs);
		return Dabs;
	}
	
	public Double DinnerprodQ(String query_words[],HashMap<String,Double> EachDocWordWeight,HashMap<String,Integer> stopwords,HashMap<String,Double> IDF)
	{
	
		 Double QdotD=0.0;
		 int word_strt;
 	    for(word_strt=0;word_strt<query_words.length;word_strt++)
	    	{
 	    	if(stopwords.containsKey(query_words[word_strt]))
 	    	if(stopwords.get(query_words[word_strt])<=2 || stopwords.get(query_words[word_strt])>=40) continue;	
 	    	if(EachDocWordWeight.containsKey(query_words[word_strt]))
 	    QdotD+=	(EachDocWordWeight.get(query_words[word_strt])*IDF.get(query_words[word_strt]));
 	    
	    
	    	
	    	}
		return QdotD;
	}
	
	public Double Qabsolute(String query_words[],HashMap<String,Double> EachDocWordWeight,HashMap<String,Integer> stopwords,HashMap<String,Double> IDF)
	{
		int word_strt;
		Double Qabs=0.0;
		 for(word_strt=0;word_strt<query_words.length;word_strt++)
	    	{
 	    	if(stopwords.containsKey(query_words[word_strt]))
 	    	if(stopwords.get(query_words[word_strt])<=2 || stopwords.get(query_words[word_strt])>=40) continue;	
 	    	
 	    	if(IDF.containsKey(query_words[word_strt]))
 	    Qabs+=	(IDF.get(query_words[word_strt]))*(IDF.get(query_words[word_strt]));
 	    
	    
	    	
	    	}
 	    Qabs=Math.sqrt(Qabs);
 	    return Qabs;
	}
}

