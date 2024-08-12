package Optimizer.Tester;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Algorithms.Genetic.MultiObjective.ParallelNSGAII;
import Optimizer.Example.DropWaveProblem;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Parameter.Parameter;
import Optimizer.Parameter.Type.ItemGroup;
import Optimizer.Parameter.Type.OptionsSet;
import Optimizer.Problem.Problem;
import Optimizer.Runner.MultiAlgorithmsRunnerMultiThreded;
import Optimizer.Tool.Tool;

public class DropWaveExample {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ParserConfigurationException, TransformerException, NoSuchMethodException,
			SecurityException, SAXException, IOException, InterruptedException {

		Tool SE = new Tool("DropWaveExample");
		Vector<Parameter> keywords = new Vector<Parameter>();
		keywords.addElement(
				new Parameter("java", "-jar", Parameter.ParameterType.Optional, Parameter.ValueType.File, false));
		keywords.addElement(new Parameter("DROPWAVEFUNCTION.jar", "", Parameter.ParameterType.Compulsory,
				Parameter.ValueType.File, false));

		OptionsSet os = new OptionsSet();

		ItemGroup x1 = new ItemGroup();

		for (double x = -5.12; x < 5.13; x = x + 0.01) {

			x1.add(new BigDecimal(x).setScale(2, RoundingMode.CEILING).toString());
		}
		os.add(x1);
		keywords.addElement(new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
				os, true, true));
		keywords.addElement(new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
				os, true, true));

		SE.SetKeywords(keywords);

		AlgorithmParameters.MaxEvaluations = 500;
		AlgorithmParameters.PopulationSize = 50;

		Problem pro = new DropWaveProblem(SE);

		ParallelNSGAII EA1 = new ParallelNSGAII(pro);

		Vector<OptimizationAlgorithm> Algorithms = new Vector<OptimizationAlgorithm>();
		Algorithms.add(EA1);

		MultiAlgorithmsRunnerMultiThreded m = new MultiAlgorithmsRunnerMultiThreded(Algorithms, pro);

		m.Run();

	}

}
