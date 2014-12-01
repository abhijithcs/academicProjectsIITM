

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;


public class TestBKTree {
        private static final int EDITS = 2;
        
        public void distance() {
                
        }
        
        public void query() {
                BKTree tree = new BKTree();
               
        }
        
        public Map<String,Long> performance(String q) throws Throwable {
                InputStream inputStream = getClass().getResourceAsStream("count_1words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream),1024 * 10 * 70);
                String lineWithFreq;
                 String [] line;
                 int freq;
                BKTree tree = new BKTree();
                long start = System.nanoTime();
                while((lineWithFreq = reader.readLine()) != null) {
                	line=lineWithFreq.split("\\s+");
                        tree.add(line[0],Long.parseLong(line[1]));
                        
                }
                long diff = System.nanoTime() - start;
                //System.out.printf("Dictionary indexed in %f msec.%n",diff / (1000f * 1000f));
                start = System.nanoTime();
                String search = q;
                Map<String,Long> query = tree.search(search, EDITS);
              //System.out.println(Arrays.toString(query.toArray()));
                diff = System.nanoTime() - start;
                //System.out.printf(" matches for '%s' took %f msec.%n%s",search,diff / (1000f * 1000f),query);
                return query;
        }
}
