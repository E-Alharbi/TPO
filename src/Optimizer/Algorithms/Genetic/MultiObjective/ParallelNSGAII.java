package Optimizer.Algorithms.Genetic.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Algorithms.Genetic.SingleObjective.Genetic;
import Optimizer.Parameter.AlgorithmParameters;

public class ParallelNSGAII extends Genetic {

	public ParallelNSGAII(IntegerProblem Problem) {
		super(Problem);

		init(Problem);

	}

	public void init(IntegerProblem Problem) {

		SolutionListEvaluator<IntegerSolution> evaluator = new MultithreadedSolutionListEvaluator<IntegerSolution>(
				AlgorithmParameters.numberOfCores, this.Problem);

		NSGAIIBuilder<IntegerSolution> builder = new NSGAIIBuilder<IntegerSolution>(this.Problem,
				AlgorithmParameters.Crossover, AlgorithmParameters.Mutation, AlgorithmParameters.PopulationSize)
						.setSelectionOperator(AlgorithmParameters.Selection)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setSolutionListEvaluator(evaluator);

		algorithm = builder.build();

		if (builder.getMaxIterations() == Integer.MAX_VALUE)
			init(Problem);

	}

	public ParallelNSGAII() {
		super();
		// TODO Auto-generated constructor stub
	}
}
