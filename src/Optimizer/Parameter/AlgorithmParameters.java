package Optimizer.Parameter;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Algorithms.Genetic.SingleObjective.GA;

public class AlgorithmParameters {
	// should be same as names in setter method in Jmetal
	public static OptimizationAlgorithm algorithm = new GA(null);
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
	public static int EvolutionStrategyMu = 1;
	public static int EvolutionStrategyLambda = 10;
	public static int PopulationSize = 1000;
	public static int numberOfCores = Runtime.getRuntime().availableProcessors();
	public static int archiveSize = 200;
	public static double epsilon = 0.01;
	public static int BiSections = 5;
	public static int hypervolume = 100;
	public static int K = 1;

}
