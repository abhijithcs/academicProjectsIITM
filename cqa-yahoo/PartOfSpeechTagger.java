import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class PartOfSpeechTagger {
public PartOfSpeechTagger() {
	// TODO Auto-generated constructor stub
}
	public String tagSentence(String a)
	{
	
	MaxentTagger tagger =  new MaxentTagger("home/amal/models/english-left3words-distsim.tagger");
	String tagged = tagger.tagString(a);
	return a;
	}
}
