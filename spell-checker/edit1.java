import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class edit1 {
	String actual;
	Map <String,Long> typed;
	edit1(){
		
	}

	public Map<String,Map<String,Long>> readLines(String filename) throws IOException {
		
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
       Map<String,Map<String,Long>> everything=new HashMap <String,Map<String,Long>>();
       Map<String,Long> m;
  
       while ((line = bufferedReader.readLine()) != null) {
        	line=line.trim();
        //	System.out.println("line is "+line);
            String[] each=line.split("\\|");
          //  System.out.println("each 0 is "+each[0]);
            String Actual=each[0];
            if(everything.containsKey(Actual))
            	m=everything.get(Actual);
            else
            	m=new HashMap <String,Long>();
            each[1]=each[1].trim();
            Long Count;
            //System.out.println("each1 is "+each[1]);
            String Typed=each[1].split("\\s+")[0];
            if(each[1].split("\\s+").length==2)
            {}//System.out.println("typed is "+Typed);
            else
            	Typed="";
            if(each[1].split("\\s+").length==2)
            Count=Long.parseLong(each[1].split("\\s+")[1]);
            else
           Count=Long.parseLong(each[1].split("\\s+")[0]);
 
            m.put(Typed, Count);
            everything.put(Actual, m);
        }
       //System.out.println("everything is "+everything);
        bufferedReader.close();
     //   System.out.println(everything.get("a").get("at"));
        return everything;
    }

	public long numerator(Map<String, Map<String, Long>> everything,
			String actual2, String typed2) {
		// TODO Auto-generated method stub
	//	System.out.println("actual "+actual2+" typed "+typed2);
		if(everything.containsKey(actual2))
		{
		Map<String, Long> right=everything.get(actual2);
		if(right.containsKey(typed2)){
			//System.out.println("numerator is "+right.get(typed2));
		//System.out.println("count is edit is "+right.get(typed2));
			return right.get(typed2);
		}
		else return 40L;
		}
			 return Long.parseLong("300");
		
	}

	public long denominator(Map<String, Map<String, Long>> everything,
			String actual2) {
		long sum=1;
		//System.out.println("actual "+actual2);
		if(everything.containsKey(actual2))
		{
		Map<String, Long> right=everything.get(actual2);
		
		for (Long value : right.values()) {
		//	System.out.println("values is "+value);
		 sum+=value;
			// ...
		}
		}
	//	System.out.println("denominator is "+sum);
		if(sum!=1)
		return sum;
		else
			return 400L;
		// TODO Auto-generated method stub
		
	}
}
