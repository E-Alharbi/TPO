package Optimizer.Algorithms.Genetic.SingleObjective;

import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.problem.IntegerProblem;
import org.w3c.dom.Element;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Util.XML;

public abstract class Genetic extends OptimizationAlgorithm {
	
	
	public Genetic(IntegerProblem Problem) {
		super(Problem);
	}
	public Genetic() {
		super();
	}
	public void ConfigurationReprot() throws ParserConfigurationException, TransformerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AbstractGeneticAlgorithm GAB=(AbstractGeneticAlgorithm) algorithm;
		
		 XML xml = new XML();
	     xml.CreateDocument();
	    
	     Element rootElement = xml.getDocument().createElement("Parameters");
	     xml.getDocument().appendChild(rootElement);
	     
	     Element element = xml.getDocument().createElement("Algorithm");
	     Element Child = xml.getDocument().createElement("Name");
	     Child.setTextContent(String.valueOf(GAB.getName()));
	     element.appendChild(Child);
	      Child = xml.getDocument().createElement("Class");
	     Child.setTextContent(String.valueOf(GAB.getClass().getSimpleName()));
	     element.appendChild(Child);
	     
	     rootElement.appendChild(element);
	     
	      element = xml.getDocument().createElement("PopulationSize");
	     element.setTextContent(String.valueOf(GAB.getMaxPopulationSize()));
	     rootElement.appendChild(element);
	     
	      element = xml.getDocument().createElement("MaxEvaluations");
	      element.setTextContent(String.valueOf(AlgorithmParameters.MaxEvaluations));
	      rootElement.appendChild(element);
	     
	      element = xml.getDocument().createElement("Selection");
	      //element.setTextContent(String.valueOf(GAB.getSelectionOperator().toString()));
	      element.setAttribute("name", String.valueOf(GAB.getSelectionOperator().toString()));
	    
	     AddingGetter(GAB.getSelectionOperator().getClass().getMethods(),element, GAB.getSelectionOperator(),xml.getDocument());
	     rootElement.appendChild(element);
	     
	     element = xml.getDocument().createElement("Crossover");
	     element.setAttribute("name", String.valueOf(GAB.getCrossoverOperator().toString()));
	     //Class<? extends CrossoverOperator> cl = GAB.getCrossoverOperator().getClass();
	     AddingGetter(GAB.getCrossoverOperator().getClass().getMethods(),element, GAB.getCrossoverOperator(),xml.getDocument());
	     rootElement.appendChild(element);
	     
	     element = xml.getDocument().createElement("Mutation");
	    // element.setTextContent(String.valueOf(GAB.getMutationOperator().toString()));
	     element.setAttribute("name", String.valueOf(GAB.getMutationOperator().toString()));
	     //Class<? extends CrossoverOperator> cl = GAB.getCrossoverOperator().getClass();
	     AddingGetter(GAB.getMutationOperator().getClass().getMethods(),element, GAB.getMutationOperator(),xml.getDocument());
	     rootElement.appendChild(element);
	     xml.WriteDocument(xml.getDocument(), ConfigurationReprotTitle);
	}
}
