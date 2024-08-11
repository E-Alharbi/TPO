package Optimizer.Algorithms.EvolutionStrategy.SingleObjective;

import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.EvolutionStrategyBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Parameter.AlgorithmParameters;

public class NonElitistEvolutionStrategy extends Evolution {

	public NonElitistEvolutionStrategy(IntegerProblem Problem) {
		super(Problem);
		// TODO Auto-generated constructor stub
		init(Problem);
	}

	public NonElitistEvolutionStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		EvolutionStrategyBuilder<IntegerSolution> builder = new EvolutionStrategyBuilder<IntegerSolution>(this.Problem,
				AlgorithmParameters.Mutation, EvolutionStrategyBuilder.EvolutionStrategyVariant.NON_ELITIST)
						.setMaxEvaluations(AlgorithmParameters.getMaxEvaluations(algorithm))
						.setMu(AlgorithmParameters.EvolutionStrategyMu)
						.setLambda(AlgorithmParameters.EvolutionStrategyLambda);

		algorithm = builder.build();
		if (builder.getMaxEvaluations() == Integer.MAX_VALUE)
			init(Problem);
	}

}
