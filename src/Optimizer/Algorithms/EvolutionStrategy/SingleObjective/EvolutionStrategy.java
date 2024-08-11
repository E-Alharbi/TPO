package Optimizer.Algorithms.EvolutionStrategy.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.EvolutionStrategyBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Parameter.AlgorithmParameters;

public class EvolutionStrategy extends Evolution {

	public EvolutionStrategy(IntegerProblem Problem) {
		super(Problem);
		// TODO Auto-generated constructor stub
		init(Problem);
	}

	public EvolutionStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		EvolutionStrategyBuilder<IntegerSolution> builder = new EvolutionStrategyBuilder<IntegerSolution>(this.Problem,
				AlgorithmParameters.Mutation, EvolutionStrategyBuilder.EvolutionStrategyVariant.ELITIST)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setMu(AlgorithmParameters.EvolutionStrategyMu)
						.setLambda(AlgorithmParameters.EvolutionStrategyLambda);
		algorithm = builder.build();

		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);

	}

}
