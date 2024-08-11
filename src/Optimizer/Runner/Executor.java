package Optimizer.Runner;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.util.JMetalException;

public class Executor extends org.uma.jmetal.util.AlgorithmRunner.Executor {

	Algorithm<?> algorithm;

	public Executor(Algorithm<?> algorithm) {
		super(algorithm);
		this.algorithm = algorithm;
	}

	@Override
	public org.uma.jmetal.util.AlgorithmRunner execute() {
		
		Thread thread = new Thread(algorithm);
		thread.setName(algorithm.getName());
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new JMetalException("Error in thread.join()", e);
		}

		return null;
	}
}
