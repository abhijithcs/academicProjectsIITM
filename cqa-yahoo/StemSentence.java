import java.util.HashMap;


public class StemSentence {
public StemSentence() {
	// TODO Auto-generated constructor stub
}
	public String StemSent(String s,HashMap<String,Integer> stopwords)
	{
		String[] query_words_stem=s.split("\\s+");
    	String querystem="";
    	int word_strt;
		for(word_strt=0;word_strt<query_words_stem.length;word_strt++)	
		{
			//if(stopwords.containsKey(query_words_stem[word_strt]))
			//if(stopwords.get(query_words_stem[word_strt])<=2 || stopwords.get(query_words_stem[word_strt])>=60) continue;
			querystem=querystem+new PorterStemmer().stripAffixes(query_words_stem[word_strt])+" ";
		}
	
	return querystem;
	}
}
