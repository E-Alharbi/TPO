package Optimizer.Algorithms.Genetic.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Algorithms.Genetic.SingleObjective.Genetic;
import Optimizer.Parameter.AlgorithmParameters;

public class ParallelMOCell extends Genetic {
	public ParallelMOCell(IntegerProblem Problem) {
		super(Problem);
		// TODO Auto-generated constructor stub
		init(Problem);

	}

	public ParallelMOCell() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		SolutionListEvaluator<IntegerSolution> evaluator = new MultithreadedSolutionListEvaluator<IntegerSolution>(
				AlgorithmParameters.numberOfCores, this.Problem);

		MOCellBuilder<IntegerSolution> builder = new MOCellBuilder<IntegerSolution>(Problem,
				AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
						.setSelectionOperator(AlgorithmParameters.Selection)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setPopulationSize(AlgorithmParameters.PopulationSize)
						.setArchive(new CrowdingDistanceArchive<IntegerSolution>(AlgorithmParameters.archiveSize))
						.setSolutionListEvaluator(evaluator);

		algorithm = builder.build();
		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);
	}
}
