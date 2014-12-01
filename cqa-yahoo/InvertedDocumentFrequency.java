import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class InvertedDocumentFrequency {

	public InvertedDocumentFrequency() {
		// TODO Auto-generated constructor stub
	}
	public HashMap<String,Double> PopulateIDF( HashMap<String,Integer> totalDocWordCount,int count,HashMap<String,Double> IDF)
	{
	
		  Iterator it = totalDocWordCount.entrySet().iterator();
  	    while (it.hasNext()) {
  	        Map.Entry pairs = (Map.Entry)it.next();
  
  	        int dfi=totalDocWordCount.get(pairs.getKey().toString());
  	        int D=(Integer) pairs.getValue();
  	        double IDFi=Math.log((double) count/dfi);
  	        IDF.put(pairs.getKey().toString(), IDFi);
  	//        System.out.println (" word "+pairs.getKey()+ " IDFi "+IDFi);
  	      //  it.remove(); // avoids a ConcurrentModificationException
  	    }
		
		return IDF;
	}
}
