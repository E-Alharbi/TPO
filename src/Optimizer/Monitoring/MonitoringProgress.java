package Optimizer.Monitoring;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.uma.jmetal.algorithm.Algorithm;

import Optimizer.Parameter.AlgorithmParameters;
import me.tongfei.progressbar.ProgressBar;

public class MonitoringProgress {

	public static HashMap<Algorithm, Integer> ProgressStatus = new HashMap<Algorithm, Integer>();
	static HashMap<Algorithm, ProgressBar> ProgressBars = new HashMap<Algorithm, ProgressBar>();
	static double ProgressBarComplate = 0;
	static boolean Start = false;
	static Timer t = new Timer();

	public synchronized static void Rest() {
		ProgressStatus = new HashMap<Algorithm, Integer>();
		ProgressBars = new HashMap<Algorithm, ProgressBar>();
		ProgressBarComplate = 0;
		Start = false;
		Stop();
		t = new Timer();
	}

	public synchronized static void Progress() {
		if (Start)
			return;
		System.out.println("Start progress mointer");

		ProgressBarComplate = 0;
		ProgressStatus.clear();
		ProgressBars.clear();
		Start = true;
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				try {

					for (Algorithm algorithm : ProgressStatus.keySet()) {
						if (!ProgressBars.containsKey(algorithm)) {

							ProgressBar pb = new ProgressBar("Evaluating population (" + algorithm.getName() + ")",
									AlgorithmParameters.getMaxEvaluations(algorithm));
							ProgressBars.put(algorithm, pb);
						} else {
							ProgressBars.get(algorithm).stepTo(ProgressStatus.get(algorithm));
							ProgressBarComplate = ProgressBars.get(algorithm).getNormalizedProgress();

						}

					}
				} catch (Exception e) {
					//System.out.println("Exception ");
					//e.printStackTrace();
				}

			}

		}, 0, 1);
	}

	public synchronized static void Stop() {
		t.cancel();
		Start = false;
		/*
		 * boolean Complate=true; for(Algorithm algorithm : ProgressStatus.keySet()) {
		 * //System.out.println("ProgressStatus.get(algorithm) "+ProgressStatus.get(
		 * algorithm));
		 * System.out.println(AlgorithmParameters.getMaxEvaluations(algorithm));
		 * if(ProgressStatus.get(algorithm)>=AlgorithmParameters.getMaxEvaluations(
		 * algorithm)) Complate=false;
		 * 
		 * } if(Complate) { t.cancel(); Start=false; }
		 */
	}
}
