package Optimizer.Algorithms.Genetic.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Parameter.AlgorithmParameters;

public class ParallelGA extends Genetic {

	public ParallelGA(IntegerProblem Problem) {
		super(Problem);
		// TODO Auto-generated constructor stub
		init(Problem);
	}

	public ParallelGA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		GeneticAlgorithmBuilder<IntegerSolution> builder = new GeneticAlgorithmBuilder<IntegerSolution>(this.Problem,
				AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
						.setPopulationSize(AlgorithmParameters.PopulationSize)
						.setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
						.setSelectionOperator(AlgorithmParameters.Selection)
						.setSolutionListEvaluator(new MultithreadedSolutionListEvaluator<IntegerSolution>(
								AlgorithmParameters.numberOfCores, this.Problem));

		algorithm = builder.build();
		builder.getEvaluator().shutdown();
	}

}
