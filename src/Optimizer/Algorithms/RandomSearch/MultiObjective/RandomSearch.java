package Optimizer.Algorithms.RandomSearch.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;

public class RandomSearch extends OptimizationAlgorithm {
	public RandomSearch(IntegerProblem Problem) {
		super(Problem);
		init(Problem);

	}

	public RandomSearch() {
		super();

	}

	public void init(IntegerProblem Problem) {
		RandomSearchBuilder<IntegerSolution> builder = new RandomSearchBuilder<IntegerSolution>(this.Problem)
				.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm));

		algorithm = builder.build();
		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);
	}

}
