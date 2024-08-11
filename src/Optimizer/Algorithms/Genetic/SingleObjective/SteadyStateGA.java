package Optimizer.Algorithms.Genetic.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Parameter.AlgorithmParameters;

public class SteadyStateGA extends Genetic {

	public SteadyStateGA(IntegerProblem Problem) {
		super(Problem);
		// TODO Auto-generated constructor stub
		init(Problem);

	}

	public SteadyStateGA() {
		super();

	}

	public void init(IntegerProblem Problem) {
		GeneticAlgorithmBuilder<IntegerSolution> builder = new GeneticAlgorithmBuilder<IntegerSolution>(this.Problem,
				AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
						.setPopulationSize(AlgorithmParameters.PopulationSize)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setSelectionOperator(AlgorithmParameters.Selection)
						.setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE);

		algorithm = builder.build();
		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);
	}

}
