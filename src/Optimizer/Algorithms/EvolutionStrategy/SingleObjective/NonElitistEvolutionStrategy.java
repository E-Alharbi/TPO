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
		algorithm = new EvolutionStrategyBuilder<IntegerSolution>(this.Problem, AlgorithmParameters.Mutation,
				EvolutionStrategyBuilder.EvolutionStrategyVariant.NON_ELITIST)
						.setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
						.setMu(AlgorithmParameters.EvolutionStrategyMu)
						.setLambda(AlgorithmParameters.EvolutionStrategyLambda).build();
	}

}
