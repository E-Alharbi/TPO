package Optimizer.Tool.Output;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

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
	
	public File[] FilesByNameRecursively( String Name) throws IOException {
		Collection<File> files= FileUtils.listFiles(
				new File(tool.GetWorkingPath()), 
				  new RegexFileFilter("^(.*?)"+Name), 
				  DirectoryFileFilter.DIRECTORY
				);
		
		return files.toArray(new File[files.size()]);
	}
	
	public void SaveLog() throws FileNotFoundException {
		WriteStringToTxtFile(tool.GetWorkingPath()+"/"+tool.GetName()+".log",tool.GetLog());
	}
	public void WriteStringToTxtFile(String File, String Txt) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(File)) {
			out.println(Txt);
		}
	}
}
