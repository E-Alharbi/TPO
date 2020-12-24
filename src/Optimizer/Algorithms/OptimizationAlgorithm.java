package Optimizer.Algorithms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.ClassUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class OptimizationAlgorithm {
	protected IntegerProblem Problem;
	protected Algorithm algorithm;
	protected String  ConfigurationReprotTitle="AlgorithmConfiguration.xml";
	public OptimizationAlgorithm(IntegerProblem Problem) {
		this.Problem=Problem;
		
	}
	public void init(IntegerProblem Problem) {
		
	}
	public OptimizationAlgorithm() {
		
	}
	
	public  List<IntegerSolution> Run() throws ParserConfigurationException, TransformerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ConfigurationReprot();
		 AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;
		 
		    
		
		 List<IntegerSolution> population=null;
		 if(algorithm.getResult() instanceof java.util.List) // in case  of multi objectives algorithms  
			 population = (List<IntegerSolution>) algorithm.getResult() ;
		 else {
			 population = new ArrayList<IntegerSolution>() ;
			 population.add((IntegerSolution)algorithm.getResult());
		 }
			
			 
		 
		 

		 

		    long computingTime = algorithmRunner.getComputingTime() ;

		    new SolutionListOutput(population)
		            .setSeparator("\t")
		            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
		            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
		            .print();

		    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

		    
		return population;
	}
	public void SetProblem(IntegerProblem Problem) {
		
		this.Problem=Problem;
		
	}
	
	public void ConfigurationReprot() throws ParserConfigurationException, TransformerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		
	}
	public void AddingGetter( Method [] methods ,  Element element , Object object , Document doc) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
     for (Method c : methods) {
    	
    	 
    	
    	 
    		 
    	 
				if( c.getName().startsWith("get") && ClassUtils.isPrimitiveOrWrapper(c.getReturnType())) {
					
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
}
