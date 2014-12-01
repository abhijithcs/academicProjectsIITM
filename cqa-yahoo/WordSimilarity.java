import java.util.List;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.Lin;


public class WordSimilarity {

	private static ILexicalDatabase db = new NictWordNet();
	  static RelatednessCalculator rc = new HirstStOnge(db);
	 
	   static List<POS[]> posPairs = rc.getPOSPairs();
	   
	    
	    public Double ComputeWordSimilarity(String s1,String s2)
	    {
	    	double maxScore = -1D;
	        for(POS[] posPair: posPairs) {
                List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(s1, posPair[0].toString());
                List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(s2, posPair[1].toString());

                for(Concept synset1: synsets1) {
                    for (Concept synset2: synsets2) {
                        Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
                        double score = relatedness.getScore();
                        if (score > maxScore) { 
                            maxScore = score;
                        }
                    }
                }
            }

            if (maxScore == -1D) {
                maxScore = 0.0;
            }
	    	return maxScore;
	    }
	    
}
