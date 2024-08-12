package Optimizer.Runner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.math3.util.Pair;
import org.uma.jmetal.solution.IntegerSolution;
import org.xml.sax.SAXException;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Monitoring.MonitoringProgress;
import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Problem.Problem;
import Optimizer.Util.XML;

public class MultiAlgorithmsRunnerMultiThreded implements Runnable {

	Vector<OptimizationAlgorithm> Algorithms;
	Problem problem;
	List<IntegerSolution> NextPopulation = null;
	Vector<IntegerSolution> RestPopulation = new Vector<IntegerSolution>();
	String NextPopulationFile = "NextPopulation.xml";
	String BestPopulationFile = "BestPopulation.xml";
	String IterationFile = "iteration";

	public MultiAlgorithmsRunnerMultiThreded(Vector<OptimizationAlgorithm> algorithms, Problem problem) {
		super();

		// The problem might be null when setting the algorithm using config.properties
		for (OptimizationAlgorithm algorithm : algorithms) {
			if (algorithm.GetProblem() == null)
				algorithm.SetProblem(problem);
		}
		Algorithms = algorithms;
		this.problem = problem;

	}

	public synchronized OptimizationAlgorithm GetAlgorithm() {
		if (!Algorithms.isEmpty())
			return Algorithms.remove(0);
		return null;

	}

	public synchronized void PutInRestPopulation(OptimizationAlgorithm algorithm)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, ParserConfigurationException, SAXException, IOException {
		Vector<IntegerSolution> AlgorithmPopulation = new XML().XMLToPopulation(
				new File(AlgorithmParameters.ResumeDir + "/" + algorithm.getMonitor().getAlgorithmStatusName()),
				algorithm.getMonitor().getProblem(), false);
		for (IntegerSolution s : AlgorithmPopulation) {
			s.setAttribute("Algorithm", algorithm.getClass().getSimpleName());
		}
		// new XML().PopulationToXML(AlgorithmPopulation,
		// algorithm.getClass().getSimpleName()+"-"+String.valueOf(i)+".xml", 0);
		RestPopulation.addAll(AlgorithmPopulation);
	}

	public synchronized void PutInNextPopulation(List<IntegerSolution> sol) {
		if (NextPopulation == null) {
			sol.subList(1, sol.size()).clear();// Keep only the first sol

			NextPopulation = sol;
		} else {
			NextPopulation.add(sol.get(0));
		}

	}

	@Override
	public void run() {

		// System.out.println("\n");

		OptimizationAlgorithm algorithm = GetAlgorithm();
		if (algorithm != null) {
			// System.out.println(algorithm.getAlgorithm().getName());

			// AlgorithmParameters.MaxEvaluations=AlgorithmParameters.PopulationSize*2;
			// AlgorithmParameters.setMaxEvaluations(algorithm.getAlgorithm().getDescription(),AlgorithmParameters.getMaxEvaluations(algorithm.getAlgorithm().getDescription()));
			// AlgorithmParameters.setMaxEvaluations(algorithm.getAlgorithm().getDescription(),
			// AlgorithmParameters.PopulationSize * 2);

			// System.out.println(AlgorithmParameters.ResumeMaxEvaluations);
			// AlgorithmParameters.setMaxEvaluations(algorithm.getAlgorithm().getDescription(),
			// AlgorithmParameters.PopulationSize * 2);

			if (new File(NextPopulationFile).exists()) {

				try {
					this.problem.setPopulation(
							new XML().XMLToPopulation(new File(NextPopulationFile), algorithm.GetProblem(), false));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException | ParserConfigurationException | IOException e1) {

					e1.printStackTrace();
				}

			}

			algorithm.init(algorithm.GetProblem());// To update max evaluations

			// System.out.println("AlgorithmParameters.ResumeMaxEvaluations
			// "+AlgorithmParameters.ResumeMaxEvaluations);

			List<IntegerSolution> sol = null;
			try {
				sol = algorithm.Run();

				if (algorithm.getMonitor().evaluations == 0) {// in the case of the Algorithm cannot be monitored we
																// save the population
					algorithm.getMonitor().SaveAlgorithmStatus(sol);

				}

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| ParserConfigurationException | TransformerException | SecurityException e) {

				e.printStackTrace();
			}

			if (sol != null) {

				sol.get(0).setAttribute("Algorithm", algorithm.getClass().getSimpleName());
				PutInNextPopulation(sol);
				try {
					PutInRestPopulation(algorithm);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException | ParserConfigurationException | SAXException
						| IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				algorithm.SetMonitor(null);// Reset monitor. If not reset that will cause in not saving the new
											// population
			}
		}

	}

	public void Run() throws ParserConfigurationException, TransformerException, SAXException, IOException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		int iteration = AlgorithmParameters.MaxEvaluations / AlgorithmParameters.PopulationSize;

		AlgorithmParameters.MaxEvaluations = AlgorithmParameters.PopulationSize * 2;// multiply two because solution
																					// creation is counted one
																					// evaluation
		// AlgorithmParameters.ResumeMaxEvaluations.clear();
		int ThreadNumber = 0;
		Vector<Thread> Threads = new Vector<Thread>();
		MonitoringProgress.Progress();
		int i = 0;
		Pair<Integer, Integer> resumeIterations = new XML().XMLToiteration(IterationFile);
		if (resumeIterations != null) {
			iteration = resumeIterations.getFirst();
			i = resumeIterations.getSecond();

		}
		if (i + 1 >= iteration) {// that means all iterations are complete
			i = iteration;// to stop the iterations
			System.out.println("All iterations are complete!");
			System.exit(-1);
		}

		for (; i < iteration; ++i) {

			// new Folder().CreatePath(AlgorithmParameters.ResumeDir+"/iteration_"+i);
			// AlgorithmParameters.ResumeDir=
			AlgorithmParameters.setResumeDir(i);
			RestPopulation.clear();
			if (NextPopulation != null)
				NextPopulation.clear();
			Vector<OptimizationAlgorithm> AlgorithmsCopy = new Vector<OptimizationAlgorithm>(Algorithms);

			AlgorithmParameters.setMaxEvaluations(Algorithms, AlgorithmParameters.PopulationSize * 2, i);

			while (Algorithms.size() != 0) {
				if (Thread.activeCount() < Runtime.getRuntime().availableProcessors() * 2) {

					Thread t1 = new Thread(this, "Thread" + String.valueOf(ThreadNumber));

					t1.start();

					++ThreadNumber;
					Threads.add(t1);
				}
			}

			boolean StillRunning = true;
			while (StillRunning) {
				StillRunning = false;
				for (int t = 0; t < Threads.size(); ++t) {
					if (Threads.get(t).isAlive()) {

						StillRunning = true;
					}
				}

			}

			if (NextPopulation != null) {// null when all iterations have completed

				Algorithms.addAll(AlgorithmsCopy);

				Collections.shuffle(RestPopulation);

				new XML().PopulationToXML(NextPopulation, BestPopulationFile, 0);

				int RemainNextPopulationToFill = AlgorithmParameters.PopulationSize - NextPopulation.size();
				if (RestPopulation.size() >= RemainNextPopulationToFill)// In the case of a single population algorithm,
																		// which only produces one solution when it runs
																		// on its own.
					NextPopulation.addAll(RestPopulation.subList(0, RemainNextPopulationToFill));

				new XML().PopulationToXML(NextPopulation, NextPopulationFile, 0);

				this.problem.clearPopulation();
				AlgorithmParameters.ResumeMaxEvaluations.clear();

			}

			// new XML().iterationToXML(iteration-1, i, IterationFile);// iteration-1 to
			// stop when reach the last iteration. For example, iterations is 5 and the last
			// iteration (i) is 4 that will make to do an extra iteration when run it again
			new XML().iterationToXML(iteration, i, IterationFile);// iteration-1 to stop when reach the last iteration.
																	// For example, iterations is 5 and the last
																	// iteration (i) is 4 that will make to do an extra
																	// iteration when run it again

		}

		MonitoringProgress.Stop();

		// Write the best the Parameters
		Vector<IntegerSolution> BestPopulation = new XML().XMLToPopulation(new File(BestPopulationFile), this.problem,
				false);
		int count_file = 1;
		for (IntegerSolution sol : BestPopulation) {
			AlgorithmParameters.BestParametersReport = "BestParameters_" + this.problem.tool.GetName() + "_"
					+ count_file;

			this.problem.tool.Report(sol);
		}

		// Clean files
		/*
		 * new File(BestPopulationFile).delete(); new File(NextPopulationFile).delete();
		 * new File(IterationFile).delete(); new
		 * File(AlgorithmParameters.BestParametersReportEncoded).delete();
		 * 
		 * new Folder().RemoveFolder(AlgorithmParameters.ResumeDir);
		 */
	}

}
