package Optimizer.Benchmark;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Example.BenchmarkProblem;
import Optimizer.Monitoring.MonitoringProgress;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Parameter.Parameter;
import Optimizer.Parameter.Type.ItemGroup;
import Optimizer.Parameter.Type.OptionsSet;
import Optimizer.Problem.Problem;
import Optimizer.Properties.PropertiesReader;
import Optimizer.Properties.PropertiesSetter;
import Optimizer.Runner.MultiAlgorithmsRunnerMultiThreded;
import Optimizer.Tool.Tool;
import Optimizer.Util.Txt;

public class Benchmark {

	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException,
			SAXException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {

		// Path to R script test functions
		File[] RScripts = new File(args[0]).listFiles();

		Vector<Tool> Benchmark = new Vector<Tool>();
		for (File script : RScripts) {
			if (script.getName().endsWith(".r")) {

				String RCode = new Txt().readFileAsString(script.getAbsolutePath());

				Tool SE = new Tool(script.getName());
				Vector<Parameter> keywords = new Vector<Parameter>();

				// Set to actual path for Rscript in bin folder
				keywords.addElement(new Parameter("/bin/Rscript", "", Parameter.ParameterType.Compulsory,
						Parameter.ValueType.File, false));

				keywords.addElement(new Parameter(args[0] + "/" + script.getName(), "",
						Parameter.ParameterType.Compulsory, Parameter.ValueType.File, false));

				keywords.addElement(new Benchmark().DetermineParameter(RCode));

				SE.SetKeywords(keywords);
				Benchmark.add(SE);

			}
		}

		for (Tool tool : Benchmark) {

			MonitoringProgress.Rest();

			AlgorithmParameters.MaxEvaluations = 5000;
			AlgorithmParameters.PopulationSize = 100;

			Problem pro = new BenchmarkProblem(tool);

			HashMap<String, String> H = new PropertiesReader().Read("config.properties");
			new PropertiesSetter().Set(H);

			Vector<OptimizationAlgorithm> Algorithms = new Vector<OptimizationAlgorithm>();

			AlgorithmParameters.algorithm.SetProblem(pro);
			Algorithms.add(AlgorithmParameters.algorithm);

			/*
			 * Algorithms.add(EA1);
			 * 
			 * Algorithms.add(EA2); Algorithms.add(EA3); Algorithms.add(EA4);
			 * Algorithms.add(EA5); Algorithms.add(EA6); Algorithms.add(EA7);
			 * Algorithms.add(EA8); Algorithms.add(EA9); Algorithms.add(EA10);
			 * 
			 * Algorithms.add(EA11);
			 * 
			 * Algorithms.add(EA12); Algorithms.add(EA13); Algorithms.add(EA14);
			 */

			// Algorithms.add(EA2);
			// HashMap<String, String> H = new PropertiesReader().Read("config.properties");
			// new PropertiesSetter().Set(H);
			MultiAlgorithmsRunnerMultiThreded m = new MultiAlgorithmsRunnerMultiThreded(Algorithms, pro);

			m.Run();

		}

	}

	Parameter DetermineParameter(String txt) {
		String[] lines = txt.split("\n");
		boolean inputs = false;
		for (String line : lines) {

			if (inputs) {

				if (line.contains("xx")) {
					long count = 0;
					if (line.contains("...")) {

						count = 2;
					} else {
						count = line.chars().filter(ch -> ch == ',').count() + 1;

					}

					OptionsSet os = new OptionsSet();
					for (int c = 0; c < count; ++c) {
						ItemGroup x1 = new ItemGroup();

						for (double x = -1000; x <= 1000; x = x + 0.01) {
							String val = new BigDecimal(x).setScale(2, RoundingMode.CEILING).toString();
							if (c + 1 < count)
								val += ",";
							x1.add(val);
						}
						os.add(x1);
					}
					return new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
							os, true, true);

					// break;
				}
			}
			if (line.contains("INPUT:") || line.contains("INPUTS:"))
				inputs = true;
		}
		OptionsSet os = new OptionsSet();

		ItemGroup x1 = new ItemGroup();

		for (double x = -1000; x <= 1000; x = x + 0.01) {
			String val = new BigDecimal(x).setScale(2, RoundingMode.CEILING).toString();

			x1.add(val);
		}
		os.add(x1);
		return new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions, os, true,
				true);

	}
}
