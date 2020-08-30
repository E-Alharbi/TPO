package Optimizer.Tool.Output;

import java.io.File;
import java.util.Vector;

import Optimizer.Tool.Tool;

public class Output {

	
	private Tool tool;
	public Output (Tool tool) {
		this.tool=tool;
		
	}
	public Vector <File>SerachForFileByName(String Name) {
		System.out.println("Tool working dir output "+ tool.GetWorkingPath());
		File [] Dir = new File(tool.GetWorkingPath()).listFiles();
		Vector <File> files = new Vector <File>();
		for(File file : Dir ) {
			if (file.getName().equals(Name))
				
				files.addElement(file);
		}
		return files;
	}
	
}
