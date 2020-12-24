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
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		algorithm = new GeneticAlgorithmBuilder<IntegerSolution>(this.Problem, AlgorithmParameters.Crossover,
				AlgorithmParameters.Mutation).setPopulationSize(AlgorithmParameters.PopulationSize)
						.setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
						.setSelectionOperator(AlgorithmParameters.Selection)
						.setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE).build();
	}

}
