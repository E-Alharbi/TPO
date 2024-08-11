package Optimizer.Util;

import java.util.Set;

public class SafeTerminate {

	public static void Terminate() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				// iterating over the threads to get the names of
				// all the active threads
				for (Thread x : threadSet) {

					x.interrupt();
				}

			}
		});
	}
}
