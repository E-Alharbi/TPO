package Optimizer.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.uma.jmetal.solution.IntegerSolution;
import org.xml.sax.SAXException;

import Optimizer.Parameter.AlgorithmParameters;
import Optimizer.Problem.Problem;

public class Util {
	public HashMap<String, Vector<IntegerSolution>> ResumeAlgorithmRun(Problem problem)
			throws ParserConfigurationException, SAXException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		new Folder().RemoveEmptyFiles(AlgorithmParameters.ResumeDir);// Empty files cause an error 
		new Folder().RemoveEmptySubFolder(AlgorithmParameters.ResumeDir);// Remove empty folders, which means the iteration is not completed. 
		HashMap<String, Vector<IntegerSolution>> ResumePopulation = new HashMap<String, Vector<IntegerSolution>>();
		if (new File(AlgorithmParameters.ResumeDir).exists() == false)
			return ResumePopulation;
		//Check latest incomplete iteration
		for(File iteration : new File(AlgorithmParameters.ResumeDir).listFiles()) {
			if(iteration.getName().startsWith(AlgorithmParameters.BaseIterationFolder)) {
			AlgorithmParameters.ResumeDir=iteration.getAbsolutePath();
			if(iteration.isDirectory())
			for(File Algorithm: iteration.listFiles()) {
				
				new XML().XMLToPopulation(Algorithm, problem, true);
				

			}
		
			
			if(AlgorithmParameters.CheckAllAlgorithmCompleted()==false) {
			System.out.println("We will resume from this iteration: "+iteration.getAbsolutePath());
			break;
			
		}
			AlgorithmParameters.ResumeMaxEvaluations.clear();
		}
		}
		for (File file : new File(AlgorithmParameters.ResumeDir).listFiles()) {
			ResumePopulation.put(file.getName(), new XML().XMLToPopulation(file, problem, true));
		}
		return ResumePopulation;
		

	}
}
