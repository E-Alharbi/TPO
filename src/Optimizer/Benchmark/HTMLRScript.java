package Optimizer.Benchmark;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Optimizer.Util.Folder;
import Optimizer.Util.Txt;

public class HTMLRScript {

	// Remove HTML tags
	public static void main(String[] args) throws IOException {

		// Path to R script test functions in HTML format
		File[] HtmlPages = new File("RscriptTestfunctionsHTML").listFiles();

		for (File html : HtmlPages) {
			Document doc = Jsoup.parse(html);
			new Folder().CreateFolder("RScripts");
			new Txt().WriteTxtFile("RScripts/" + html.getName().replaceAll(".html", ".r"), doc.text());
		}
	}

}
