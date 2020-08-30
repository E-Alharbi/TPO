package Optimizer.Algorithms.EvolutionStrategy.SingleObjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.EvolutionStrategyBuilder;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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
		        .setLambda(AlgorithmParameters.EvolutionStrategyLambda)
		        .build() ;
	}
	
}
