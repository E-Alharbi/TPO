package Optimizer.Algorithms;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.ClassUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Optimizer.Monitoring.Monitoring;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Runner.Executor;
import Optimizer.Util.XML;

public abstract class OptimizationAlgorithm {
	protected IntegerProblem Problem;

	protected Algorithm algorithm;

	protected String ConfigurationReprotTitle = "AlgorithmConfiguration.xml";

	Monitoring monitor = null;

	public Monitoring getMonitor() {
		return monitor;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public OptimizationAlgorithm(IntegerProblem Problem) {
		this.Problem = Problem;

	}

	public void init(IntegerProblem Problem) {

	}

	public OptimizationAlgorithm() {

	}

	public List<IntegerSolution> Run() throws ParserConfigurationException, TransformerException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// MonitoringProgress.Progress();

		List<IntegerSolution> population = null;
		try {
			if (new File(AlgorithmParameters.BestParametersReportEncoded).exists()) {
				population = new XML().XMLToPopulation(new File(AlgorithmParameters.BestParametersReportEncoded),
						Problem, false);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ParserConfigurationException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (AlgorithmParameters.getMaxEvaluations(algorithm) != 0) {

			if (monitor == null)
				monitor = new Monitoring(algorithm, Problem, algorithm.getDescription());
			monitor.monitor();

			new Executor(algorithm).execute();

			ConfigurationReprot();// Save it after the Algorithm runs because saving it before the run is
									// completed cause an error with CoralReefs.

			if (algorithm.getResult() instanceof java.util.List) // in case of multi objectives algorithms
				population = (List<IntegerSolution>) algorithm.getResult();
			else {
				population = new ArrayList<IntegerSolution>();
				population.add((IntegerSolution) algorithm.getResult());
			}

			try {
				TimeUnit.SECONDS.sleep(1); // Sometimes, the progress bar stops before completing 100% because the
											// algorithm finishes running before the timer updates the progress bar. So,
											// We
											// delay for 1 second to allow for the progress bar to get updated.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monitor.MonitorStop();
			// monitor=null;
		}

		// MonitoringProgress.Stop();
		return population;

	}

	public void SetProblem(IntegerProblem Problem) {

		this.Problem = Problem;

	}

	public void SetMonitor(Monitoring monitor) {

		this.monitor = monitor;

	}

	public IntegerProblem GetProblem() {

		return this.Problem;

	}

	public void ConfigurationReprot() throws ParserConfigurationException, TransformerException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

	}

	public void AddingGetter(Method[] methods, Element element, Object object, Document doc)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Method c : methods) {

			if (c.getName().startsWith("get") && ClassUtils.isPrimitiveOrWrapper(c.getReturnType())) {

				try {

					Element ele = doc.createElement(c.getName().substring(3));
					ele.setTextContent(String.valueOf(c.invoke(object)));
					element.appendChild(ele);

				} catch (InvocationTargetException e) {

					e.getCause().printStackTrace();

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		}
	}

	protected String FindBuildReturnType(Class b) {

		Method build = null;
		try {
			build = b.getDeclaredMethod("build");
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return build.getReturnType().getSimpleName();
	}

}
