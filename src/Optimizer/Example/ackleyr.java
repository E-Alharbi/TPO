package Optimizer.Example;

import java.io.IOException;

import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Problem.Problem;
import Optimizer.Tool.Tool;

public class ackleyr extends Problem {

	public ackleyr(Tool tool) {
		super(tool);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void evaluate(IntegerSolution solution) {
		Tool ackleyr = new Tool(this.tool);

		ackleyr.SetParametersValueBasedOnOptimizationAlgorithm(solution);

		ackleyr.SetNoWorkingPath(true);

		try {
			ackleyr.Run();
		} catch (IOException e) {

			e.printStackTrace();
		}

		solution.setObjective(0, Double.valueOf(ackleyr.GetLog().trim()));

	}

}
