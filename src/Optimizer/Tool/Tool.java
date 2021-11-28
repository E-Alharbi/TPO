package Optimizer.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.solution.IntegerSolution;
import org.w3c.dom.Element;

import Optimizer.Parameter.Parameter;
import Optimizer.Util.Folder;
import Optimizer.Util.XML;

public class Tool {

	private String Name;
	private String Log = "";
	private String ErrorLog = "";
	private String WorkingPath;
	Vector<Parameter> Keywords;
	private boolean NoWorkingPath = false;

	// Copy Constructor
	public Tool(Tool tool) {

		this.Name = new String(tool.GetName());
		this.Log = new String(tool.GetLog());
		this.ErrorLog = new String(tool.GetErrorLog());
		this.WorkingPath = new String(tool.GetWorkingPath());
		this.Keywords = new Vector<Parameter>();
		this.NoWorkingPath = tool.NoWorkingPath();
		for (int i = 0; i < tool.GetKeywords().size(); ++i) {

			this.Keywords.add(new Parameter(tool.GetKeywords().get(i)));
		}

	}

	public Tool(String Name) {

		this.Name = Name;
		WorkingPath = Name;
	}

	public String GetName() {
		return Name;
	}

	public void SetName(String Name) {
		this.Name = Name;
		this.WorkingPath = Name;
	}

	public void SetNoWorkingPath(boolean status) {
		NoWorkingPath = status;
	}

	public boolean NoWorkingPath() {
		return NoWorkingPath;
	}

	public String GetLog() {
		return Log;
	}

	public void ClearLog() {
		Log = "";
	}

	public void ErrorLog() {
		ErrorLog = "";
	}

	public String GetErrorLog() {
		return ErrorLog;
	}

	public int GetLengthOfInd() {
		int Len = GetNeededOptimizeParameters().size();
		for (int p = 0; p < GetNeededOptimizeParameters().size(); ++p) {
			Len += GetNeededOptimizeParameters().get(p).LengthInIndividual();
		}

		return Len;
	}

	public void SetWorkingPath(String Path) {
		WorkingPath = Path;
	}

	public String GetWorkingPath() {
		return WorkingPath;
	}

	public boolean CreateWorkingPath() {

		//String Folder = String.valueOf(Thread.currentThread().getName() + System.currentTimeMillis());
		String Folder = String.valueOf(Thread.currentThread().getName()+"-"+Thread.currentThread().getId()+"-"+ System.currentTimeMillis());

		if (new File(Folder).exists())
			return CreateWorkingPath();

		SetWorkingPath(Folder);
		return true;
	}

	public void SetKeywords(Vector<Parameter> keywords) {
		this.Keywords = keywords;
	}

	public Vector<Parameter> GetKeywords() {

		return Keywords;
	}

	public Vector<Parameter> GetNeededOptimizeParameters() {
		Vector<Parameter> Parameters = new Vector<Parameter>();
		for (int i = 0; i < Keywords.size(); ++i) {
			if (Keywords.get(i).GetOptimize())
				Parameters.add(Keywords.get(i));
		}
		return Parameters;
	}

	public void SetParametersValueBasedOnOptimizationAlgorithm(IntegerSolution solution) {

		int ValueIndex = GetNumberOfNeededOptimizeParameters();
		for (int i = 0; i < GetNumberOfNeededOptimizeParameters(); i++) {

			int Used = solution.getVariableValue(i);
			// int
			// Value=solution.getVariableValue((GetNumberOfNeededOptimizeParameters())+i);
			Vector<Integer> Value = new Vector<Integer>();
			while (Value.size() < this.GetNeededOptimizeParameters().get(i).LengthInIndividual()) {
				Value.add(solution.getVariableValue(ValueIndex));
				ValueIndex++;
			}

			this.GetNeededOptimizeParameters().get(i).SetValueBasedOnOptimizationAlgorithm(Used, Value);

		}

	}

	public Vector<Parameter> GetUsingParameters() {
		Vector<Parameter> Parameters = new Vector<Parameter>();
		for (int i = 0; i < Keywords.size(); ++i) {
			if (Keywords.get(i).IsUsed())
				Parameters.add(Keywords.get(i));
		}
		return Parameters;
	}

	public Vector<Parameter> GetKeywordByName(String Name) {
		Vector<Parameter> parameters = new Vector<Parameter>();
		for (int i = 0; i < Keywords.size(); ++i) {
			if (Keywords.get(i).GetKeyword().equals(Name))
				parameters.add(Keywords.get(i));
		}
		return parameters;
	}

	public Vector<Parameter> GetKeywordByValue(String Val) {
		Vector<Parameter> parameters = new Vector<Parameter>();
		for (int i = 0; i < Keywords.size(); ++i) {
			if (Keywords.get(i).GetValue().equals(Val))
				parameters.add(Keywords.get(i));
		}
		return parameters;
	}

	public Vector<Parameter> GetSecondKeywordByName(String Name) {
		Vector<Parameter> parameters = new Vector<Parameter>();
		for (int i = 0; i < Keywords.size(); ++i) {
			if (Keywords.get(i).GetSecondKeyword() != null && Keywords.get(i).GetSecondKeyword().equals(Name))
				parameters.add(Keywords.get(i));
		}
		return parameters;
	}

	public int GetNumberOfNeededOptimizeParameters() {
		int count = 0;
		for (int i = 0; i < Keywords.size(); ++i) {
			if (Keywords.get(i).GetOptimize())
				count++;
		}
		return count;
	}

	@Override
	public String toString() {
		return "Tool [Keywords=" + Keywords + "]";
	}

	public void RemoveWorkingPath() throws IOException {
		if (new File(GetWorkingPath()).exists() && NoWorkingPath == false)
			FileUtils.deleteDirectory(new File(GetWorkingPath()));
	}

	public void Run() throws IOException {
		if (NoWorkingPath == false) {
			if (new Folder().CreateFolder(GetWorkingPath()) == false) {
				throw new FileAlreadyExistsException(GetWorkingPath()
						+ " folder cannot be created because the folder is exists. Remove the folder!");

			}
		}
		if (NoWorkingPath == true) {
			SetWorkingPath("./");
		}
		List<String> Args = new ArrayList<String>();
		Vector<Parameter> Parameters = GetUsingParameters();
		for (int i = 0; i < Parameters.size(); i++) {
			Args.addAll(Parameters.get(i).ToList());
		}

		String[] callAndArgs = new String[Args.size()];
		callAndArgs = Args.toArray(callAndArgs);

		//System.out.println(Arrays.toString(callAndArgs));

		Process p = Runtime.getRuntime().exec(callAndArgs, null, new File(GetWorkingPath()));

		String st = null;
		try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			

			while ((st = stdInput.readLine()) != null) {

				if (Log.trim().length() != 0)
					Log += "\n";

				Log += st;

			}
		}
		
			try (BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
				while ((st = stdError.readLine()) != null) {

					// System.out.println("error "+st);
					ErrorLog += st + "\n";
				}
			}
		

	}

	public void Report(IntegerSolution solution) throws ParserConfigurationException, TransformerException {

		this.SetParametersValueBasedOnOptimizationAlgorithm(solution);

		XML xml = new XML();
		xml.CreateDocument();
		Element rootElement = xml.getDocument().createElement("Parameters");

		xml.getDocument().appendChild(rootElement);
		for (int i = 0; i < GetNumberOfNeededOptimizeParameters(); i++) {
			Element ParameterRoot = xml.getDocument().createElement("Parameter");

			// int Used = solution.getVariableValue(i) ;
			// int
			// Value=solution.getVariableValue((GetNumberOfNeededOptimizeParameters())+i);

			Element Keyword = xml.getDocument().createElement("Keyword");
			Element KeywordVlaue = xml.getDocument().createElement("Value");
			Element IsUsed = xml.getDocument().createElement("Used");

			Keyword.setTextContent(this.GetNeededOptimizeParameters().get(i).GetKeyword());
			KeywordVlaue.setTextContent(this.GetNeededOptimizeParameters().get(i).GetValue());
			IsUsed.setTextContent(String.valueOf(this.GetNeededOptimizeParameters().get(i).IsUsed()));

			/*
			 * if(Used==0) IsUsed.setTextContent(String.valueOf("False"));
			 * 
			 * 
			 * else IsUsed.setTextContent(String.valueOf("True"));
			 * 
			 * 
			 * Keyword.setTextContent(this.GetNeededOptimizeParameters().get(i).GetKeyword()
			 * ); if(this.GetNeededOptimizeParameters().get(i).GetValueType()==
			 * Parameter.ValueType.Number) {
			 * 
			 * KeywordVlaue.setTextContent(String.valueOf(Value)); }
			 * if(this.GetNeededOptimizeParameters().get(i).GetValueType()==
			 * Parameter.ValueType.String ||
			 * this.GetNeededOptimizeParameters().get(i).GetValueType()==
			 * Parameter.ValueType.File ) {
			 * 
			 * KeywordVlaue.setTextContent(String.valueOf(this.GetNeededOptimizeParameters()
			 * .get(i).GetValue())); }
			 * if(this.GetNeededOptimizeParameters().get(i).GetValueType()==
			 * Parameter.ValueType.SetOfOptions) {
			 * 
			 * if(this.GetNeededOptimizeParameters().get(i).IsCombination()==false)
			 * KeywordVlaue.setTextContent(String.valueOf(this.GetNeededOptimizeParameters()
			 * .get(i).GetSetOfOptionsType().GetByIndex(this.GetNeededOptimizeParameters().
			 * get(i).LengthInIndividual()-1).GetByIndex(Value)));
			 * 
			 * 
			 * }
			 */

			ParameterRoot.appendChild(Keyword);
			ParameterRoot.appendChild(KeywordVlaue);
			ParameterRoot.appendChild(IsUsed);
			if (this.GetNeededOptimizeParameters().get(i).GetSecondKeyword() != null) {
				Element SecondKeyword = xml.getDocument().createElement("SecondKeyword");
				SecondKeyword.setTextContent(String.valueOf(this.GetNeededOptimizeParameters().get(i)));
				ParameterRoot.appendChild(SecondKeyword);
			}

			rootElement.appendChild(ParameterRoot);

		}
		xml.WriteDocument(xml.getDocument(), "ParametersReport.xml");

	}
}
