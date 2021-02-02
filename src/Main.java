import java.util.Random;

public class Main {
	static final int n = 5;
	static double[] realReward = new double[n];
	static Random rand = new Random();
	
	static final int runCount = 200, stepCount = 100;
	static final double epsilon = 0.4;
	
	static double[][] avgReward = new double[2][stepCount]; //0 -> greedy, 1 -> epsilon-greedy
	static double[][] optimalActionRate = new double[2][stepCount]; //0 -> greedy, 1 -> epsilon-greedy
	
	public static void main(String[] args) {
		
		//perform runs
		for(int i = 0; i < runCount; i++){
			//generate real(hidden) reward
			for(int j = 0; j < realReward.length; j++){
				realReward[j] = rand.nextGaussian()*20;
			}
			performRun(false); 	//greedy run
			performRun(true); 	//epsilon-greedy run
		}
		
		//divide for average
		for(int i = 0; i < stepCount; i++) {
			avgReward[0][i] /= runCount;
			avgReward[1][i] /= runCount;
			optimalActionRate[0][i] /= runCount;
			optimalActionRate[1][i] /= runCount;
		}
		
		System.out.println("Done");
		
	}
	
	public static void performRun(boolean isEpsilon){
		int a, maxA = 0, optimal=0;
		double reward;
		double[] Q = new double[n];
		double[] N = new double[n];
		
		//find optimal reward
		for (int j = 0; j < realReward.length; j++) {
			optimal = realReward[j] > realReward[optimal] ? j : optimal;
		}
		
		for(int i = 0; i < stepCount; i++) {
			
			//make choice
			if(isEpsilon && rand.nextDouble() <= epsilon)
				a = rand.nextInt(n);
			else {
				for (int j = 0; j < Q.length; j++) {
				    maxA = Q[j] > Q[maxA] ? j : maxA;
				}
				a = maxA;
			}
			
			//get & record reward
			reward = realReward[a] + rand.nextGaussian();
			avgReward[isEpsilon ? 1 : 0][i] += reward;
			if(a == optimal) {
				optimalActionRate[isEpsilon ? 1 : 0][i]++;
			}
			
			//update beliefs
			N[a]++;
			Q[a] = Q[a] + (1/(N[a])) * (reward - Q[a]);
			
		}
		
		
	}
}
