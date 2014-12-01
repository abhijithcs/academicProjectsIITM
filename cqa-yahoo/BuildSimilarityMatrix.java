
public class BuildSimilarityMatrix {

	
	public Double[][] buildSimMatrix(String question,String query)
	
	{
		//System.out.println("In build Sim");
//		System.out.println("in build sim");
//		 System.out.println("query is "+query);
//		    System.out.println("question is"+question);
		WordSimilarity WS=new WordSimilarity();
		  String[] ques = question.split("\\s+");
		    String[] quer= query.split("\\s+");
		    Double[][] SimMat=new Double[quer.length][ques.length];
		    int i,j;
		    for(i=0;i<quer.length;i++)
		    {
		    //	System.out.print(quer[i]);
		    	for(j=0;j<ques.length;j++)
		    	{
		    		SimMat[i][j]=WS.ComputeWordSimilarity(quer[i], ques[j]);
		    	//	System.out.print(" "+SimMat[i][j]);
		    	}
		    	//System.out.println();
		    }
		    
		 
		    
		    
		    return SimMat;
	}
}
