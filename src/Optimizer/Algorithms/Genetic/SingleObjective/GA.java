package Optimizer.Algorithms.Genetic.SingleObjective;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.w3c.dom.Element;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Tester.BuccParametersProblem;
import Optimizer.Util.XML;

public class GA extends Genetic {

	public GA(IntegerProblem Problem) {
		super(Problem);
		
		// TODO Auto-generated constructor stub
		 
		init(Problem);
		
	}
	public GA() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void init (IntegerProblem Problem) {
		 algorithm = new GeneticAlgorithmBuilder<>(this.Problem, AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
		            .setPopulationSize(AlgorithmParameters.PopulationSize)
		            .setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
		            .setSelectionOperator(AlgorithmParameters.Selection)
		            .build();	
	}

	
}
