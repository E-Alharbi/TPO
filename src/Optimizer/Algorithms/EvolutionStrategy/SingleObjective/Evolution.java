package Optimizer.Algorithms.EvolutionStrategy.SingleObjective;

import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.uma.jmetal.algorithm.impl.AbstractEvolutionStrategy;
import org.uma.jmetal.problem.IntegerProblem;
import org.w3c.dom.Element;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Util.XML;

public abstract class Evolution extends OptimizationAlgorithm {

	public Evolution(IntegerProblem Problem) {
		super(Problem);
	}

	public Evolution() {
		super();
	}

	public void ConfigurationReprot() throws ParserConfigurationException, TransformerException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		AbstractEvolutionStrategy GAB = (AbstractEvolutionStrategy) algorithm;

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

		element = xml.getDocument().createElement("MaxEvaluations");
		element.setTextContent(String.valueOf(AlgorithmParameters.getMaxEvaluations(this.algorithm)));
		rootElement.appendChild(element);

		element = xml.getDocument().createElement("Mu");
		element.setTextContent(String.valueOf(AlgorithmParameters.EvolutionStrategyMu));
		rootElement.appendChild(element);

		element = xml.getDocument().createElement("Lambda");
		element.setTextContent(String.valueOf(AlgorithmParameters.EvolutionStrategyLambda));
		rootElement.appendChild(element);

		element = xml.getDocument().createElement("Mutation");
		// element.setTextContent(String.valueOf(GAB.getMutationOperator().toString()));
		element.setAttribute("name", String.valueOf(AlgorithmParameters.Mutation.toString()));
		// Class<? extends CrossoverOperator> cl =
		// GAB.getCrossoverOperator().getClass();

		AddingGetter(AlgorithmParameters.Mutation.getClass().getMethods(), element, AlgorithmParameters.Mutation,
				xml.getDocument());
		rootElement.appendChild(element);
		xml.WriteDocument(xml.getDocument(), ConfigurationReprotTitle);
	}
}
