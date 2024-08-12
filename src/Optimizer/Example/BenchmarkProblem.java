package Optimizer.Example;

import java.io.IOException;

import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Problem.Problem;
import Optimizer.Tool.Tool;

public class BenchmarkProblem extends Problem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BenchmarkProblem(Tool tool) {
		super(tool);

	}

	@Override
	public void evaluate(IntegerSolution solution) {

		Tool tool = new Tool(this.tool);

		tool.SetParametersValueBasedOnOptimizationAlgorithm(solution);

		tool.SetNoWorkingPath(true);

		try {
			tool.Run();
		} catch (IOException e) {

			e.printStackTrace();
		}

		solution.setObjective(0, Double.valueOf(tool.GetLog()));

	}

}
