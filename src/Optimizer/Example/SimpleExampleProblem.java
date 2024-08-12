package Optimizer.Example;

import java.io.IOException;

import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Problem.Problem;
import Optimizer.Tool.Tool;

public class SimpleExampleProblem extends Problem {

	public SimpleExampleProblem(Tool tool) {
		super(tool);
		setNumberOfObjectives(2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub

		Tool SimpleExample = new Tool(this.tool);
		// SimpleExample.ClearLog();

		SimpleExample.SetParametersValueBasedOnOptimizationAlgorithm(solution);

		SimpleExample.SetNoWorkingPath(true);
		// SimpleExample.CreateWorkingPath();
		try {
			SimpleExample.Run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int CountOnes = Integer.valueOf(SimpleExample.GetLog().split("-")[0]);
		int CountOnes2 = Integer.valueOf(SimpleExample.GetLog().split("-")[1]);

		solution.setObjective(0, -1 * CountOnes);
		solution.setObjective(1, CountOnes2);

		// try {
		// SimpleExample.RemoveWorkingPath();
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
