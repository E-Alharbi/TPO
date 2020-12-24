package Optimizer.Algorithms.Genetic.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;

import Optimizer.Parameter.AlgorithmParameters;

public class GA extends Genetic {

	public GA(IntegerProblem Problem) {
		super(Problem);

		// TODO Auto-generated constructor stub

		init(Problem);

	}

	public GA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		algorithm = new GeneticAlgorithmBuilder<>(this.Problem, AlgorithmParameters.Crossover,
				AlgorithmParameters.Mutation).setPopulationSize(AlgorithmParameters.PopulationSize)
						.setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
						.setSelectionOperator(AlgorithmParameters.Selection).build();
	}

}
