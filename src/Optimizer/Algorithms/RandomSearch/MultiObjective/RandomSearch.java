package Optimizer.Algorithms.RandomSearch.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;

public class RandomSearch extends OptimizationAlgorithm {
	public RandomSearch(IntegerProblem Problem) {
		super(Problem);
		init(Problem);   
			   
		
	}
	public RandomSearch() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void init (IntegerProblem Problem) {
		 algorithm = new RandomSearchBuilder<IntegerSolution>(this.Problem)
		            .setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
		            .build() ;  
	}
}
