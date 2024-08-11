package Optimizer.Example;

import java.io.IOException;

import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Problem.Problem;
import Optimizer.Tool.Tool;

public class DropWaveProblem extends Problem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DropWaveProblem(Tool tool) {
		super(tool);

	}

	@Override
	public void evaluate(IntegerSolution solution) {

		
		Tool DropWave = new Tool(this.tool);

		DropWave.SetParametersValueBasedOnOptimizationAlgorithm(solution);

		DropWave.SetNoWorkingPath(true);

		try {
			DropWave.Run();
		} catch (IOException e) {

			e.printStackTrace();
		}

		
		solution.setObjective(0, Double.valueOf(DropWave.GetLog()));

	}

}
