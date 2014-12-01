import java.util.ArrayList;
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

public class contextWords {

	
	Map<String,Integer> words;
public contextWords(Map<String,Integer> w)
{
	words=w;

}
	public void printContext()
	{
		Map<String,Integer> w=this.words;
		
		for (Map.Entry<String, Integer> entry : w.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println("key "+key +" value "+value);
		    // ...
		}
		
	}
}
