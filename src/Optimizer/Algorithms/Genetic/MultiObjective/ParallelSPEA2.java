package Optimizer.Algorithms.Genetic.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import Optimizer.Algorithms.Genetic.SingleObjective.Genetic;
import Optimizer.Parameter.AlgorithmParameters;

public class ParallelSPEA2 extends Genetic{

	public ParallelSPEA2(IntegerProblem Problem) {
		super(Problem);
		
		init(Problem);
		
	}
	public ParallelSPEA2() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void init(IntegerProblem Problem) {
		SolutionListEvaluator<IntegerSolution> evaluator = new MultithreadedSolutionListEvaluator<IntegerSolution>(AlgorithmParameters.numberOfCores, this.Problem) ;

		algorithm = new SPEA2Builder<>(this.Problem, AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
	            .setSelectionOperator(AlgorithmParameters.Selection)
	            .setMaxIterations(AlgorithmParameters.MaxEvaluations)
	            .setPopulationSize(AlgorithmParameters.PopulationSize)
	            .setK(AlgorithmParameters.K).setSolutionListEvaluator(evaluator)
	            .build();
	}
}
