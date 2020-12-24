package Optimizer.Algorithms.Coralreefs.SingleObjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.impl.AbstractCoralReefsOptimization;
import org.uma.jmetal.algorithm.impl.AbstractEvolutionStrategy;
import org.uma.jmetal.algorithm.singleobjective.coralreefsoptimization.CoralReefsOptimization;
import org.uma.jmetal.algorithm.singleobjective.coralreefsoptimization.CoralReefsOptimizationBuilder;
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
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.w3c.dom.Element;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Util.XML;

import java.lang.reflect.InvocationTargetException;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
public class CoralReefs extends OptimizationAlgorithm {

	public CoralReefs(IntegerProblem Problem) {
		super(Problem);
		init(Problem);
		
		// TODO Auto-generated constructor stub
	}
public void init(IntegerProblem Problem) {
	algorithm = new CoralReefsOptimizationBuilder<IntegerSolution>(Problem,
			AlgorithmParameters.Selection, AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
			.setM(AlgorithmParameters.CoralReefsM).setN(AlgorithmParameters.CoralReefsN).setRho(AlgorithmParameters.CoralReefsRho).setFbs(AlgorithmParameters.CoralReefsFbs).setFbr(AlgorithmParameters.CoralReefsFbr)
			.setFa(AlgorithmParameters.CoralReefsFa).setPd(AlgorithmParameters.CoralReefsPd).setAttemptsToSettle(AlgorithmParameters.CoralReefsttemptsToSettle)
			.setComparator(new ObjectiveComparator<IntegerSolution>(0))
			.build();
}
	public CoralReefs() {
		super();
	}
	public List<IntegerSolution> RunCoralReefs() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParserConfigurationException, TransformerException{
		ConfigurationReprot();
		Algorithm<List<IntegerSolution>> algorithm;
		IntegerProblem problem = this.Problem;

		

		algorithm = new CoralReefsOptimizationBuilder<IntegerSolution>(problem,
				AlgorithmParameters.Selection, AlgorithmParameters.Crossover, AlgorithmParameters.Mutation)
				.setM(AlgorithmParameters.CoralReefsM).setN(AlgorithmParameters.CoralReefsN).setRho(AlgorithmParameters.CoralReefsRho).setFbs(AlgorithmParameters.CoralReefsFbs).setFbr(AlgorithmParameters.CoralReefsFbr)
				.setFa(AlgorithmParameters.CoralReefsFa).setPd(AlgorithmParameters.CoralReefsPd).setAttemptsToSettle(AlgorithmParameters.CoralReefsttemptsToSettle)
				.setComparator(new ObjectiveComparator<IntegerSolution>(0))
				.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(
				algorithm).execute();
		List<IntegerSolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();
		new SolutionListOutput(population)
				.setSeparator("\t")
				.setVarFileOutputContext(
						new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(
						new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime
				+ "ms");
		JMetalLogger.logger
				.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger
				.info("Variables values have been written to file VAR.tsv");
		
		return  population;
	}
	public void ConfigurationReprot() throws ParserConfigurationException, TransformerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CoralReefsOptimization GAB=(CoralReefsOptimization) this.algorithm;
		
		 XML xml = new XML();
	     xml.CreateDocument();
	    
	     Element rootElement = xml.getDocument().createElement("Parameters");
	     xml.getDocument().appendChild(rootElement);
	     
	     Element element = xml.getDocument().createElement("Algorithm");
	     
	     
	   
	     Element Child = xml.getDocument().createElement("Class");
	     Child.setTextContent(String.valueOf(GAB.getClass().getSimpleName()));
	     element.appendChild(Child);
	     
	     rootElement.appendChild(element);
	     
	     AddingGetter(GAB.getClass().getMethods(),element, GAB,xml.getDocument());

	     
	    
	     element = xml.getDocument().createElement("Selection");
	      //element.setTextContent(String.valueOf(GAB.getSelectionOperator().toString()));
	      element.setAttribute("name", String.valueOf(AlgorithmParameters.Selection));
	    
	     AddingGetter(AlgorithmParameters.Selection.getClass().getMethods(),element, AlgorithmParameters.Selection,xml.getDocument());
	     rootElement.appendChild(element);
	     
	     element = xml.getDocument().createElement("Crossover");
	     element.setAttribute("name", String.valueOf(AlgorithmParameters.Crossover.toString()));
	     //Class<? extends CrossoverOperator> cl = GAB.getCrossoverOperator().getClass();
	     AddingGetter(AlgorithmParameters.Crossover.getClass().getMethods(),element, AlgorithmParameters.Crossover,xml.getDocument());
	     rootElement.appendChild(element);
	     
	     element = xml.getDocument().createElement("Mutation");
	    // element.setTextContent(String.valueOf(GAB.getMutationOperator().toString()));
	     element.setAttribute("name", String.valueOf(AlgorithmParameters.Mutation.toString()));
	     //Class<? extends CrossoverOperator> cl = GAB.getCrossoverOperator().getClass();
	     AddingGetter(AlgorithmParameters.Mutation.getClass().getMethods(),element, AlgorithmParameters.Mutation,xml.getDocument());
	     rootElement.appendChild(element);
	     
	     
	     
	     
	     xml.WriteDocument(xml.getDocument(), ConfigurationReprotTitle);
	}
}
