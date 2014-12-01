import java.util.HashMap;


public class StopWords {
	public StopWords() {
		// TODO Auto-generated constructor stub
	}
	HashMap<String,Integer> stopwords=new HashMap<String,Integer>();
	
	public HashMap<String,Integer> populateStopwords(String arr[],HashMap<String,Integer> stopwords)
	{
	 	int i;
    	for(i=0;i<arr.length;i++)
    	{
    		if(stopwords.containsKey(arr[i]))
    			stopwords.put(arr[i], stopwords.get(arr[i])+1);
    		else
    			stopwords.put(arr[i], 1);
    	}
    	return stopwords;
	}
}
