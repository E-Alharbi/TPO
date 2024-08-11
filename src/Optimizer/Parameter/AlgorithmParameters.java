package Optimizer.Parameter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Util.Folder;

public class AlgorithmParameters {
	// must be same as names in setter method in Jmetal
	// public static OptimizationAlgorithm algorithm = new GA(null);
	public static OptimizationAlgorithm algorithm;

	public static double CrossoverProbability = 0.9;
	public static double DistributionIndex = 20;
	public static double MutationProbability = 0.2;
	public static CrossoverOperator<IntegerSolution> Crossover = new IntegerSBXCrossover(CrossoverProbability,
			DistributionIndex);;
	public static MutationOperator<IntegerSolution> Mutation = new IntegerPolynomialMutation(MutationProbability,
			DistributionIndex);
	public static SelectionOperator<List<IntegerSolution>, IntegerSolution> Selection = new BinaryTournamentSelection<IntegerSolution>();
	public static int CoralReefsM = 10;
	public static int CoralReefsN = 10;
	public static double CoralReefsRho = 0.6;
	public static double CoralReefsFbs = 0.9;
	public static double CoralReefsFbr = 0.1;
	public static double CoralReefsFa = 0.1;
	public static double CoralReefsPd = 0.1;
	public static int CoralReefsttemptsToSettle = 3;
	public static int MaxEvaluations = 40000;
	public static HashMap<String, Integer> ResumeMaxEvaluations = new HashMap<String, Integer>();

	public static int EvolutionStrategyMu = 1;
	public static int EvolutionStrategyLambda = 10;
	public static int PopulationSize = 1000;
	public static int numberOfCores = Runtime.getRuntime().availableProcessors() - 1;// keep one for main thread
	public static int archiveSize = 200;
	public static double epsilon = 0.01;
	public static int BiSections = 5;
	public static int hypervolume = 100;
	public static int K = 1;
	public static String BestParametersReport = "ParametersReport.xml";
	public static String BestParametersReportEncoded = "BestParametersReportEncoded.xml";
	public static String ResumeDir = "./ResumeRun";
	public static String BaseIterationFolder="iteration_";
	/*
	 * public static int getMaxEvaluations(OptimizationAlgorithm Algorithm) {
	 * System.out.println(Algorithm.getClass().getSimpleName()); return
	 * getMaxEvaluations(Algorithm.getClass().getSimpleName()); }
	 */
	public static int getMaxEvaluations(Algorithm Algorithm) {


		if (Algorithm == null)
			return Integer.MAX_VALUE;

		return getMaxEvaluations(Algorithm.getDescription());
	}

	public static int getMaxEvaluations(String Algorithm) {
		
		 if (ResumeMaxEvaluations.containsKey(Algorithm)) {

			return ResumeMaxEvaluations.get(Algorithm);
		} else {
		//	setMaxEvaluations(Algorithm, MaxEvaluations);
			setMaxEvaluations(Algorithm, PopulationSize*2);
			//PopulationSize
			return ResumeMaxEvaluations.get(Algorithm);
		}
	}
	
	
	
	public static void setMaxEvaluations(OptimizationAlgorithm Algorithm, int maxEvaluations) {
		setMaxEvaluations(Algorithm.getClass().getSimpleName(), maxEvaluations);
	}

	public static void setMaxEvaluations(String Algorithm, int maxEvaluations) {
		ResumeMaxEvaluations.put(Algorithm, maxEvaluations);
	}
	public static void setMaxEvaluations(Vector<OptimizationAlgorithm> Algorithms, int maxEvaluations){
		for(OptimizationAlgorithm algorithm: Algorithms) {
			ResumeMaxEvaluations.put(algorithm.getAlgorithm().getDescription(), maxEvaluations);
		}
	}
	public static void setMaxEvaluations(Vector<OptimizationAlgorithm> Algorithms, int maxEvaluations, int Iteration) {
		
		//if(Iteration==0) {// if first Iteration set all to max 
			
	//		setMaxEvaluations(Algorithms,maxEvaluations);
		//}
		if(Iteration!=0) {
			boolean AllAlgorithmCompleted=CheckAllAlgorithmCompleted();
			
			/*
			for(String algorithm: ResumeMaxEvaluations.keySet()) {
				if(ResumeMaxEvaluations.get(algorithm)!=0) {
					AllAlgorithmCompleted=false;
					break;
				}
			}
			*/
			if(AllAlgorithmCompleted) {// only set to max when all Algorithms have completed the iteration
				setMaxEvaluations(Algorithms,maxEvaluations);
			}
		}
		
	}
	public static boolean CheckAllAlgorithmCompleted() {
	
		for(String algorithm: ResumeMaxEvaluations.keySet()) {
			if(ResumeMaxEvaluations.get(algorithm)!=0) {
				return false;
				
			}
		}
		return true;
	}
	public static void setResumeDir(int Iteration) {
		String path="./ResumeRun"+"/"+BaseIterationFolder+Iteration;
		new Folder().CreatePath(path);
		AlgorithmParameters.ResumeDir=path;
	}
}
