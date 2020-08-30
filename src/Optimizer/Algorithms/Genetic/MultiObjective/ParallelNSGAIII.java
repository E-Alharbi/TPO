package Optimizer.Algorithms.Genetic.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Algorithms.Genetic.SingleObjective.Genetic;
import Optimizer.Parameter.AlgorithmParameters;

public class ParallelNSGAIII extends Genetic{

	public ParallelNSGAIII(IntegerProblem Problem) {
		super(Problem);
		init(Problem);
		

		
	}
	public ParallelNSGAIII() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void init(IntegerProblem Problem) {
		SolutionListEvaluator<IntegerSolution> evaluator = new MultithreadedSolutionListEvaluator<IntegerSolution>(AlgorithmParameters.numberOfCores, this.Problem) ;

	    algorithm = new NSGAIIIBuilder<>(this.Problem)
	            .setCrossoverOperator(AlgorithmParameters.Crossover)
	            .setMutationOperator(AlgorithmParameters.Mutation)
	            .setSelectionOperator(AlgorithmParameters.Selection)
	            .setMaxIterations(AlgorithmParameters.MaxEvaluations).setSolutionListEvaluator(evaluator)
	            .build() ;
	}
}
