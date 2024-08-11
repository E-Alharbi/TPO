package Optimizer.Algorithms.Genetic.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Parameter.AlgorithmParameters;

public class ParallelGA extends Genetic {

	public ParallelGA(IntegerProblem Problem) {
		super(Problem);

		init(Problem);
	}

	public ParallelGA() {
		super();

	}

	public void init(IntegerProblem Problem) {

		GeneticAlgorithmBuilder<IntegerSolution> builder = new GeneticAlgorithmBuilder<IntegerSolution>(this.Problem,
				AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
						.setPopulationSize(AlgorithmParameters.PopulationSize)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setSelectionOperator(AlgorithmParameters.Selection)
						.setSolutionListEvaluator(new MultithreadedSolutionListEvaluator<IntegerSolution>(
								AlgorithmParameters.numberOfCores, this.Problem));

		super.algorithm = builder.build();

		builder.getEvaluator().shutdown();

		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);

	}

}
