package Optimizer.Example;

import java.nio.file.FileAlreadyExistsException;
import java.util.Vector;

import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Problem.Problem;
//import Optimizer.Runner.MultithreadedRunner;
import Optimizer.Tool.Tool;

public class SimpleProblem extends Problem {

	public SimpleProblem(Tool tool) {
		super(tool);

	}

	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub

		/*
		 * for (int i = 0; i < solution.getNumberOfVariables(); i++) { int value =
		 * solution.getVariableValue(i) ;
		 * //System.out.print(solution.getVariableValue(i)+" ");
		 * tool.GetNeededOptimizeParameters().get(i).
		 * SetValueBasedOnOptimizationAlgorithm(value);
		 * 
		 * }
		 */
/*
		Tool tool = new Tool(this.tool);
		tool.SetParametersValueBasedOnOptimizationAlgorithm(solution);
		Vector<Tool> tools = new Vector<Tool>();
		tools.add(tool);
		MultithreadedRunner MP = new MultithreadedRunner(tools, "SimpleProblem");
		try {
			MP.Run();
		} catch (FileAlreadyExistsException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		solution.setObjective(0, Integer.parseInt(tools.get(0).GetLog().trim()));
		tool.ClearLog();
		*/

	}

}
