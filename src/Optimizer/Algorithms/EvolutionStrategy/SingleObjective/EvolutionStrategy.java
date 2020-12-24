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
		algorithm = new EvolutionStrategyBuilder<IntegerSolution>(this.Problem, AlgorithmParameters.Mutation,
				EvolutionStrategyBuilder.EvolutionStrategyVariant.ELITIST)
						.setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
						.setMu(AlgorithmParameters.EvolutionStrategyMu)
						.setLambda(AlgorithmParameters.EvolutionStrategyLambda).build();
	}

}
