package Optimizer.Algorithms.Genetic.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.smsemoa.SMSEMOABuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.Genetic.SingleObjective.Genetic;
import Optimizer.Parameter.AlgorithmParameters;

public class SMSEMOA extends Genetic {

	public SMSEMOA(IntegerProblem Problem) {
		super(Problem);
		init(Problem);

	}

	public SMSEMOA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {

		Hypervolume<IntegerSolution> hypervolume;
		hypervolume = new PISAHypervolume<>();
		hypervolume.setOffset(AlgorithmParameters.hypervolume);
		algorithm = new SMSEMOABuilder<IntegerSolution>(this.Problem, AlgorithmParameters.Crossover,
				AlgorithmParameters.Mutation).setSelectionOperator(AlgorithmParameters.Selection)
						.setMaxEvaluations(AlgorithmParameters.MaxEvaluations)
						.setPopulationSize(AlgorithmParameters.PopulationSize).setHypervolumeImplementation(hypervolume)
						.build();
	}
}
