package Optimizer.Util;

import java.io.File;
import java.io.IOException;

public class Folder {
	
	//synchronized for thread safety when run  ParallelGA 
	
File Folder;
	public static void main(String[] args) throws IOException {
		//System.out.println(new Folder().CreateFolder("Test"));
		//System.out.println(new Folder().CreatePath("T/T1/T2"));
		System.out.println(new Folder().CreateFolderInOrderNumber("Runner","./"));
	}
	public File GetRecentCreatedFolder() {
		return Folder;
	}
	public boolean CreateFolder(String Name) {
		synchronized(Folder.class) {
			//System.out.println("Thread want to Create a folder "+ Thread.currentThread().getName());
		
			File F= new File(Name);
			//System.out.println("Folder "+ F.getAbsolutePath());

		if(F.mkdir()==true) {
			Folder=F;
			return true;
		}
		else {
			Folder=null;
			return false;
			}
		}
		
	}
	public boolean CreatePath(String Path) {
		return new File(Path).mkdirs();
	}
	public synchronized boolean CreateFolderInOrderNumber(String Name, String Path) {
		 synchronized(Folder.class) {
		File [] Folders = new File(Path).listFiles();
		int MaxFolderID=0;
		for(File F :Folders ) {
			if(F.getName().startsWith(Name)) {
				int ThisFolderID=Integer.parseInt(F.getName().substring(F.getName().indexOf(Name) + Name.length()));
			if(MaxFolderID < ThisFolderID)
				MaxFolderID=ThisFolderID;
			}
		
	}
		MaxFolderID++;
		return CreateFolder( new File(Path).getAbsolutePath()+"/"+Name+MaxFolderID);}
	}
}
