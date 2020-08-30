package Optimizer.Problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;

import Optimizer.Tool.Tool;

@SuppressWarnings("serial")
public abstract class Problem extends AbstractIntegerProblem{
	public Tool tool;
	public Problem(Tool tool) {
		setNumberOfVariables(tool.GetNumberOfNeededOptimizeParameters()*2);
	    setNumberOfObjectives(1);
	    setName("Optimizing Parameters");
	   
	    List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
	    List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;
	   
	    for (int i = 0; i < getNumberOfVariables()/2; i++) {
	      lowerLimit.add(tool.GetNeededOptimizeParameters().get(i).GetLowerBoundParameterType());
	      upperLimit.add(tool.GetNeededOptimizeParameters().get(i).GetUpperBoundParameterType());
	      
	    }
	    int Parameter=0;
	    for (int i = getNumberOfVariables()/2; i < getNumberOfVariables(); i++) {
		      lowerLimit.add(tool.GetNeededOptimizeParameters().get(Parameter).LowerBound());
		      upperLimit.add(tool.GetNeededOptimizeParameters().get(Parameter).UpperBound());
		      ++Parameter;
		      
		}  
	    setLowerLimit(lowerLimit);
	    setUpperLimit(upperLimit);
	    this.tool=tool;
	}
	

}
