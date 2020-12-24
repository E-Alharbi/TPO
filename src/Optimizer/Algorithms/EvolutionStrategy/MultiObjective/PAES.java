package Optimizer.Algorithms.EvolutionStrategy.MultiObjective;

import org.uma.jmetal.algorithm.multiobjective.paes.PAESBuilder;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.EvolutionStrategy.SingleObjective.Evolution;
import Optimizer.Parameter.AlgorithmParameters;;

public class PAES extends Evolution {
	public PAES(IntegerProblem Problem) {
		super(Problem);
		// TODO Auto-generated constructor stub
		init(Problem);

	}

	public PAES() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(IntegerProblem Problem) {
		algorithm = new PAESBuilder<IntegerSolution>(this.Problem).setMutationOperator(AlgorithmParameters.Mutation)
				.setMaxEvaluations(AlgorithmParameters.MaxEvaluations).setArchiveSize(AlgorithmParameters.archiveSize)
				.setBiSections(AlgorithmParameters.BiSections).build();
	}
}
