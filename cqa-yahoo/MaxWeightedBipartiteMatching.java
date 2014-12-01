
public class MaxWeightedBipartiteMatching {
	static int matches=0;
	public MaxWeightedBipartiteMatching() {
		// TODO Auto-generated constructor stub
		
	}
	public Double getScore(Double Mat[][],int m,int n)
	{
		
	Double	ScoreSum = 0.0;
	int bestCandidate;
	Double bestScore;
int i=0,j;
		for (i=0;i<m;i++){
			int[] Y=new int[n];
		  bestCandidate = -1;
		  bestScore = -10000.0;
		  for (j=0;j<n;j++){
		    if (Y[j] ==0 && Mat[i][j] > bestScore){
		        bestScore = Mat[i][j]; 
		        bestCandidate = j;                
		      }  
		  }

		  if (bestCandidate > -1){
		    
		      Y[bestCandidate]=1;
		      ScoreSum = ScoreSum + bestScore;
		      matches++;
		  }
		}
		return ScoreSum;
	}
}
