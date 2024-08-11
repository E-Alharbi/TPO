package Optimizer.Algorithms.Genetic.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Parameter.AlgorithmParameters;

public class GA extends Genetic {

	public GA(IntegerProblem Problem) {
		super(Problem);

		init(Problem);

	}

	public GA() {
		super();

	}

	public void init(IntegerProblem Problem) {

		GeneticAlgorithmBuilder<IntegerSolution> builder = new GeneticAlgorithmBuilder<>(this.Problem,
				AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
						.setPopulationSize(AlgorithmParameters.PopulationSize)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setSelectionOperator(AlgorithmParameters.Selection);

		algorithm = builder.build();
		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);

	}

}
