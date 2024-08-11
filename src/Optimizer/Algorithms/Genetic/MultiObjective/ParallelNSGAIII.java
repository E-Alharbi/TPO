package Optimizer.Algorithms.Genetic.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Algorithms.Genetic.SingleObjective.Genetic;
import Optimizer.Parameter.AlgorithmParameters;

public class ParallelNSGAIII extends Genetic {

	public ParallelNSGAIII(IntegerProblem Problem) {
		super(Problem);
		init(Problem);

	}

	public ParallelNSGAIII() {
		super();

	}

	public void init(IntegerProblem Problem) {
		SolutionListEvaluator<IntegerSolution> evaluator = new MultithreadedSolutionListEvaluator<IntegerSolution>(
				AlgorithmParameters.numberOfCores, this.Problem);

		NSGAIIIBuilder<IntegerSolution> builder = new NSGAIIIBuilder<>(this.Problem)
				.setCrossoverOperator(AlgorithmParameters.Crossover).setMutationOperator(AlgorithmParameters.Mutation)
				.setSelectionOperator(AlgorithmParameters.Selection)
				.setMaxIterations(AlgorithmParameters.getMaxEvaluations(algorithm)).setSolutionListEvaluator(evaluator);

		algorithm = builder.build();
		if (builder.getMaxIterations() == Integer.MAX_VALUE)
			init(Problem);
	}
}
