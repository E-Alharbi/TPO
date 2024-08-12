package Optimizer.Monitoring;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Util.Folder;
import Optimizer.Util.XML;

public class Monitoring {

	Algorithm algorithm;
	protected IntegerProblem Problem;

	private Timer t = new Timer();

	// private ProgressBar pb ;

	private String AlgorithmStatusName = "AlgorithmStatus.xml";

	public int evaluations = 0;

	// public String ResumeDir="./ResumeRun";
	public IntegerProblem getProblem() {
		return Problem;
	}

	public void setProblem(IntegerProblem problem) {
		Problem = problem;
	}

	public String getAlgorithmStatusName() {
		return AlgorithmStatusName;
	}

	public void setAlgorithmStatusName(String algorithmStatusName) {
		AlgorithmStatusName = algorithmStatusName;
	}

	public Monitoring() {

	}

	public Monitoring(String algorithmStatusName) {
		super();
		this.AlgorithmStatusName = algorithmStatusName;
	}

	public Monitoring(Algorithm algorithm, IntegerProblem Problem, String algorithmStatusName) {
		super();
		this.algorithm = algorithm;
		this.Problem = Problem;
		this.AlgorithmStatusName = algorithmStatusName;
	}

	public Monitoring(Algorithm algorithm, IntegerProblem Problem) {
		super();
		this.algorithm = algorithm;
		this.Problem = Problem;
	}

	public Monitoring(IntegerProblem Problem) {
		super();

		this.Problem = Problem;

	}

	public void MonitorStop() {

		t.cancel();
		// pb.close();
	}

	public void monitor() {

		t.scheduleAtFixedRate(new TimerTask() {

			public void run() {

				try {
					if (evaluations != 0)// The population should be evaluated at least once
						SaveAlgorithmStatus();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException | IOException | ParserConfigurationException
						| TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				boolean found_evaluations_var = false;
				for (Field m : algorithm.getClass().getDeclaredFields()) {

					if (m.getName().equals("evaluations")) {
						found_evaluations_var = true;
						try {

							m.setAccessible(true);
							evaluations = m.getInt(algorithm);
							MonitoringProgress.ProgressStatus.put(algorithm, evaluations);

						} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (found_evaluations_var == false) {
					System.out.println(algorithm.getName()
							+ " can not be monitored and the algorithm run status cannot be saved. However, the algorithm is running!");
					MonitorStop();
				}

			}
		}, 0, 1);
	}

	boolean CanBeMonitored() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		// Some algorithms do not update the var evaluations. So we check if the
		// population got evaluated and evaluations remain zero, the method returns
		// false;
		List<IntegerSolution> Population = (List<IntegerSolution>) algorithm.getClass().getMethod("getPopulation", null)
				.invoke(algorithm);
		if (Population == null)
			return false;
		boolean PopulationEvaluated = true;
		double[] zeroarr = new double[Population.get(0).getNumberOfObjectives()];
		for (int i = 0; i < Population.size(); ++i) {

			if (Arrays.equals(Population.get(i).getObjectives(), zeroarr)) {
				PopulationEvaluated = false;
			}
		}

		if (PopulationEvaluated == true && evaluations == 0) {
			// pb.setExtraMessage("The algorithm is running, but it can not be monitored.");

			return false;
		}
		if (PopulationEvaluated == true && evaluations != 0)
			return true;

		return false;
	}

	public void SaveAlgorithmStatus()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ParserConfigurationException, TransformerException {

		new Folder().CreateFolder(AlgorithmParameters.ResumeDir);
		List<IntegerSolution> Population = (List<IntegerSolution>) algorithm.getClass().getMethod("getPopulation", null)
				.invoke(algorithm);
		if (Population == null)
			return;
		SaveAlgorithmStatus(Population);

		// new XML().PopulationToXML(Population, AlgorithmParameters.ResumeDir + "/" +
		// AlgorithmStatusName, evaluations);

	}

	public void SaveAlgorithmStatus(List<IntegerSolution> Population)
			throws ParserConfigurationException, TransformerException {
		new XML().PopulationToXML(Population, AlgorithmParameters.ResumeDir + "/" + AlgorithmStatusName, evaluations);

	}

}
