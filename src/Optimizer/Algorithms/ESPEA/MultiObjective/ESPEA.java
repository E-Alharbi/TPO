package Optimizer.Algorithms.ESPEA.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.espea.ESPEABuilder;
import org.uma.jmetal.algorithm.multiobjective.espea.util.EnergyArchive.ReplacementStrategy;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;

public class ESPEA extends OptimizationAlgorithm {
	public ESPEA(IntegerProblem Problem) {
		super(Problem);
		init(Problem);

	}

	public ESPEA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		ESPEABuilder<IntegerSolution> builder = new ESPEABuilder<>(Problem, AlgorithmParameters.Crossover,
				AlgorithmParameters.Mutation);
		builder.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm));
		builder.setPopulationSize(AlgorithmParameters.PopulationSize);
		builder.setReplacementStrategy(ReplacementStrategy.WORST_IN_ARCHIVE);

		algorithm = builder.build();

		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);
	}
}
