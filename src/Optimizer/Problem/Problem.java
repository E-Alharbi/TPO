package Optimizer.Problem;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.xml.sax.SAXException;

import Optimizer.Tool.Tool;
import Optimizer.Util.SafeTerminate;
import Optimizer.Util.Util;

@SuppressWarnings("serial")
public abstract class Problem extends AbstractIntegerProblem {
	public Tool tool;
	private Vector<IntegerSolution> Population = new Vector<IntegerSolution>();
	private HashMap<Long, Integer> Threads = new HashMap<Long, Integer>();
	private HashMap<String, Vector<IntegerSolution>> ResumePopulation = new HashMap<String, Vector<IntegerSolution>>();

	public Problem(Tool tool) {

		// setNumberOfVariables(tool.GetNumberOfNeededOptimizeParameters()*2);
		setNumberOfVariables(tool.GetLengthOfInd());
		setNumberOfObjectives(1);
		setName("Optimizing Parameters");

		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < tool.GetNumberOfNeededOptimizeParameters(); i++) {
			lowerLimit.add(tool.GetNeededOptimizeParameters().get(i).GetLowerBoundParameterType());
			upperLimit.add(tool.GetNeededOptimizeParameters().get(i).GetUpperBoundParameterType());

		}
		
		int Parameter = 0;
		for (int i = 0; i < tool.GetNumberOfNeededOptimizeParameters(); i++) {

			for (int l = 0; l < tool.GetNeededOptimizeParameters().get(Parameter).LowerBound().size(); ++l)
				lowerLimit.add(tool.GetNeededOptimizeParameters().get(Parameter).LowerBound().get(l));

			for (int u = 0; u < tool.GetNeededOptimizeParameters().get(Parameter).UpperBound().size(); ++u)
				upperLimit.add(tool.GetNeededOptimizeParameters().get(Parameter).UpperBound().get(u));

			++Parameter;

		}
	
		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
		this.tool = tool;

		SafeTerminate.Terminate();

		try {
			ResumePopulation = new Util().ResumeAlgorithmRun(this);
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Vector<IntegerSolution> getPopulation() {
		return Population;
	}

	public synchronized void setPopulation(Vector<IntegerSolution> population) {

		// Population.clear();
		if (Population.size() == 0)
			Population.addAll(population);
	}

	public synchronized void setPopulation(List<IntegerSolution> population) {
		// Population.clear();
		if (Population.size() == 0)
			Population.addAll(population);

	}

	public void clearPopulation() {
		Population.clear();
	}

	@Override
	public synchronized IntegerSolution createSolution() {

		String ThName = Thread.currentThread().getName();
		
		if (ResumePopulation.containsKey(ThName) && ResumePopulation.get(ThName).size() != 0) {
			return ResumePopulation.get(ThName).remove(0);
		}

		long ThID = Thread.currentThread().getId();
		int index = -1;
		if (Threads.containsKey(ThID)) { // Check if this thread was called the method before. If yes, return the next
											// individual.
			index = Threads.get(ThID) + 1;// +1 to get the next
		} else {
			index = 0;
		}
		Threads.put(ThID, index);

		if (index < Population.size()) {// Check if the individual is within the Population; otherwise, create a new
										// individual.
			return Population.get(index);
		}

		/*
		 * if(Population.isEmpty()==false) {
		 * System.out.println(Thread.currentThread().getId()+" "+Population.size());
		 * return Population.remove(0); }
		 */

		return super.createSolution();
	}

}
