import java.util.Map;
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

public class Scoring {

	public float calculateLikelihood(Map<String, contextWords> context_coca,
			String element, String[] tokens) {
		// TODO Auto-generated method stub
         int i;
		float f=0;
		float total=0;
		Map<String,Integer> w=context_coca.get(element).words;
		for (Integer value : w.values()) {
			total+=value;
			}
		for(i=0;i<tokens.length;i++)
		{
			if(w.containsKey(tokens[i]))
			{
				f=(float) (f+ (float) (w.get(tokens[i])));
				
			}
			
			else continue;
		}
		
		return f;
	}

}
