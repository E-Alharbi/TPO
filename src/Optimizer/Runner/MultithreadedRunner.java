package Optimizer.Runner;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Stack;
import java.util.Vector;

import Optimizer.Tool.Tool;
import Optimizer.Util.Folder;

public class MultithreadedRunner implements Runnable {

	Vector<Tool> tools;
	Stack<Tool> Tools = new Stack<>();
	String RunnerWorkingDir = "./";
	String WorkingDir = "./";
	boolean CreateWorkingDir = true;
	private int DelayInMilliseconds = 0;

	public MultithreadedRunner(Vector<Tool> tools) {
		super();
		this.tools = tools;
		Tools.addAll(this.tools);
		this.CreateWorkingDir = false;
	}

	public MultithreadedRunner() {

	}

	public void SetDelayInMilliseconds(int Delay) {
		DelayInMilliseconds = Delay;
	}

	public MultithreadedRunner(Vector<Tool> tools, String WorkingDir) {
		this(tools);
		this.WorkingDir = WorkingDir;
		Folder folder = new Folder();
		folder.CreateFolder(WorkingDir);
		this.CreateWorkingDir = true;
	}

	public synchronized Tool GetTool() {

		// System.out.println(" #### Tools which not run yet: "+ Tools.size());
		if (!Tools.isEmpty())
			return Tools.pop();
		return null;
	}

	@Override
	public void run() {
		if (!Tools.isEmpty()) {
			Tool t = GetTool();

			if (t == null)
				return; // no more tools for running

			t.SetWorkingPath(RunnerWorkingDir + "/" + Thread.currentThread().getId());
			try {

				t.Run();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Run() throws FileAlreadyExistsException, InterruptedException {
		// new Folder().CreateFolderInOrderNumber("MultithreadedRunner","./");
		Folder folder = new Folder();

		if (this.CreateWorkingDir == true) {
			if (folder.CreateFolderInOrderNumber("MultithreadedRunner", WorkingDir) == false) {
				throw new FileAlreadyExistsException("Folder Exists");

			}

			RunnerWorkingDir = folder.GetRecentCreatedFolder().getAbsolutePath();
		}
		// System.out.println("Thread "+ Thread.currentThread().getId() +" assign to
		// "+RunnerWorkingDir);

		int ThreadNumber = 0;
		Vector<Thread> Threads = new Vector<Thread>();
		while (Tools.size() != 0) {
			if (Thread.activeCount() < Runtime.getRuntime().availableProcessors() * 2) {

				Thread t1 = new Thread(this, "Thread" + String.valueOf(ThreadNumber));
				Thread.sleep(DelayInMilliseconds);
				t1.start();

				++ThreadNumber;
				Threads.add(t1);

			}
		}
		boolean StillRunning = true;
		while (StillRunning) {
			StillRunning = false;
			for (int i = 0; i < Threads.size(); ++i) {
				if (Threads.get(i).isAlive()) {
					StillRunning = true;
				}
			}

		}
		// Thread t1 = new Thread(new MultithreadedRunner(), "Thread
		// "+String.valueOf(ThreadNumber));

		// t1.start();
	}

}
