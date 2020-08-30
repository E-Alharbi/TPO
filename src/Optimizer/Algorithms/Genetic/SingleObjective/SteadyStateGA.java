package Optimizer.Algorithms.Genetic.SingleObjective;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import Optimizer.Algorithms.OptimizationAlgorithm;
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
	public void init (IntegerProblem Problem) {
		algorithm = new GeneticAlgorithmBuilder<IntegerSolution>(this.Problem, AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
		        .setPopulationSize(AlgorithmParameters.PopulationSize)
		        .setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
		        .setSelectionOperator(AlgorithmParameters.Selection)
		        .setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE)
		        .build() ;
	}
	
}
