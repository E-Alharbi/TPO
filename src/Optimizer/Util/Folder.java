package Optimizer.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

public class Folder {

	// synchronized for thread safety when run ParallelGA

	File Folder;

	public static void main(String[] args) throws IOException {
		// System.out.println(new Folder().CreateFolder("Test"));
		// System.out.println(new Folder().CreatePath("T/T1/T2"));
		//System.out.println(new Folder().CreateFolderInOrderNumber("Runner", "./"));
	
		/*Collection<File> files=	FileUtils.listFiles(new File("/Users/emadalharbi/Desktop/ResearchProjects/TPO/Test/ResumeRun"), null, true);
	for(File file : files) {
		System.out.println(file.getAbsolutePath());
		System.out.println(file.length());
	}
	*/
	new Folder().RemoveEmptyFiles("/Users/emadalharbi/Desktop/ResearchProjects/TPO/Test/ResumeRun");
	}

	public void RemoveEmptyFolder(String dir) {
		if(new File(dir).listFiles().length==0)
			new File(dir).delete();
	}
	public void RemoveEmptySubFolder(String dir) {
		if(!new File(dir).exists()) return;
		for (File folder : new File(dir).listFiles()) {
			if(folder.isDirectory()&& folder.listFiles().length==0)
				RemoveEmptyFolder(folder.getAbsolutePath());
		}
	}
	public void RemoveEmptyFiles(String dir) {
		
		if(!new File(dir).exists()) return;
		
		Collection<File> files=	FileUtils.listFiles(new File(dir), null, true);
		for(File file : files) {
			
			if(file.length()==0) {
				file.delete();
			}
		}
	}
	public File GetRecentCreatedFolder() {
		return Folder;
	}

	public void RemoveFolder(String folder) throws IOException {
		FileUtils.deleteDirectory(new File(folder));
	}

	public boolean CreateFolder(String Name) {
		synchronized (Folder.class) {
			

			File F = new File(Name);
			

			if (F.mkdir() == true) {
				Folder = F;
				return true;
			} else {
				Folder = null;
				return false;
			}
		}

	}

	public boolean CreatePath(String Path) {
		return new File(Path).mkdirs();
	}

	public synchronized boolean CreateFolderInOrderNumber(String Name, String Path) {
		synchronized (Folder.class) {
			File[] Folders = new File(Path).listFiles();
			int MaxFolderID = 0;
			for (File F : Folders) {
				if (F.getName().startsWith(Name)) {
					int ThisFolderID = Integer
							.parseInt(F.getName().substring(F.getName().indexOf(Name) + Name.length()));
					if (MaxFolderID < ThisFolderID)
						MaxFolderID = ThisFolderID;
				}

			}
			MaxFolderID++;
			return CreateFolder(new File(Path).getAbsolutePath() + "/" + Name + MaxFolderID);
		}
	}
}
