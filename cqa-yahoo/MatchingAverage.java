
public class MatchingAverage {

	public MatchingAverage() {
		// TODO Auto-generated constructor stub
	}
	public double GetScore(Double BipartiteScore,int m,int n)
	{
		
		double score=2*(BipartiteScore)/(m+n);
		return score;
	}
}
