package Optimizer.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.math3.util.Pair;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
import org.xml.sax.SAXException;

import Optimizer.Monitoring.Monitoring;
import Optimizer.Parameter.AlgorithmParameters;

public class XML {
	Document document;
	private int count_call;
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document CreateDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		setDocument(doc);
		return doc;
	}

	public Element CreateElement(Document doc, String ElementName, String Value) {

		Element element = doc.createElement(ElementName);
		element.setTextContent(String.valueOf(Value));
		return element;
	}

	public void SetElementValue(Element element, String Value) {

		element.setTextContent(String.valueOf(Value));

	}

	public void WriteDocument(Document doc, String DocName) throws TransformerException {
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(DocName));

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// Beautify the format of the resulted XML
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.transform(source, result);

	}

	public Vector<IntegerSolution> XMLToPopulation(Monitoring m, boolean SetEvaluations)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, ParserConfigurationException, IOException {
		return XMLToPopulation(new File(m.getAlgorithmStatusName()), m.getProblem(), SetEvaluations);
	}

	public Vector<IntegerSolution> XMLToPopulation(File AlgorithmStatusFile, IntegerProblem Problem,
			boolean SetEvaluations) throws ParserConfigurationException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// File AlgorithmStatusFile = new File(FileName);
		Vector<IntegerSolution> Population = new Vector<IntegerSolution>();

		
		Document doc = ParseXML(AlgorithmStatusFile);

		NodeList list = doc.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < list.getLength(); ++i) {

			if (list.item(i).getNodeName().equals("Individual")) {
				NodeList Variables = list.item(i).getChildNodes().item(1).getChildNodes();

				IntegerSolution sol = Problem.createSolution();
				int Index = 0;
				for (int v = 0; v < Variables.getLength(); ++v) {
					Node var = Variables.item(v);
					if (var.getTextContent().trim().length() != 0) {
						sol.setVariableValue(Index, Integer.valueOf(var.getTextContent()));
						Index++;
					}
				}

				NodeList Objectives = list.item(i).getChildNodes().item(3).getChildNodes();

				Index = 0;
				for (int o = 0; o < Objectives.getLength(); ++o) {
					Node obj = Objectives.item(o);

					if (obj.getTextContent().trim().length() != 0) {

						sol.setObjective(Index, Double.valueOf(obj.getTextContent()));
					}
				}

				
				Population.add(sol);
			}

		}
		
		if (SetEvaluations)
			AlgorithmParameters.setMaxEvaluations(AlgorithmStatusFile.getName(),
					Integer.valueOf(doc.getElementsByTagName("RemainingEvaluations").item(0).getTextContent()));
		
		
		return Population;
	}

	public synchronized void PopulationToXML(IntegerSolution solution, String FileName, int Evaluations)
			throws ParserConfigurationException, TransformerException {
		List<IntegerSolution> Population = new Vector<IntegerSolution>();
		Population.add(solution);
		PopulationToXML(Population, FileName, Evaluations);
	}

	public synchronized void PopulationToXML(List<IntegerSolution> Population, String FileName, int Evaluations)
			throws ParserConfigurationException, TransformerException {
		
		XML xml = new XML();
		xml.CreateDocument();

		Element rootElement = xml.getDocument().createElement("Population");
		xml.getDocument().appendChild(rootElement);
		for (int i = 0; i < Population.size(); ++i) {

			Element individual = xml.getDocument().createElement("Individual");
			Element Variables = xml.getDocument().createElement("Variables");

			for (int v = 0; v < Population.get(i).getVariables().size(); ++v) {
				Element var = xml.getDocument().createElement("Variable");
				var.setTextContent(Population.get(i).getVariables().get(v).toString());
				Variables.appendChild(var);
			}
			individual.appendChild(Variables);

			Element Objectives = xml.getDocument().createElement("Objectives");
			for (int o = 0; o < Population.get(i).getObjectives().length; ++o) {
				Element Obj = xml.getDocument().createElement("Objective");
				
				Obj.setTextContent(new BigDecimal(Population.get(i).getObjectives()[o]).toPlainString()); //BigDecimal to avoid scientific notation
				Objectives.appendChild(Obj);
			}
			individual.appendChild(Objectives);
			rootElement.appendChild(individual);
		}

		// evaluations
		Element evaluationsEle = xml.getDocument().createElement("RemainingEvaluations");
		int remainingEvaluations = AlgorithmParameters.getMaxEvaluations(new File(FileName).getName()) - Evaluations;
		if (remainingEvaluations < 0)// Some algorithms go beyond the number of max evaluations by one evaluation.
			remainingEvaluations = 0;
		evaluationsEle.setTextContent(String.valueOf(remainingEvaluations));// remaining
																			// number
																			// of
																			// evaluations
		rootElement.appendChild(evaluationsEle);

		xml.WriteDocument(xml.getDocument(), FileName);

	}

	public synchronized void iterationToXML(int iteration, int currentIteration, String FileName)
			throws ParserConfigurationException, TransformerException {
		XML xml = new XML();
		xml.CreateDocument();

		Element rootElement = xml.getDocument().createElement("iteration");
		xml.getDocument().appendChild(rootElement);

		Element iterationElement = xml.getDocument().createElement("totalIteration");
		iterationElement.setTextContent(String.valueOf(iteration));
		rootElement.appendChild(iterationElement);

		Element currentIterationElement = xml.getDocument().createElement("currentIteration");
		currentIterationElement.setTextContent(String.valueOf(currentIteration));
		rootElement.appendChild(currentIterationElement);

		xml.WriteDocument(xml.getDocument(), FileName);
	}

	public synchronized Pair<Integer, Integer> XMLToiteration(String FileName)
			throws ParserConfigurationException, SAXException, IOException {

		if (!new File(FileName).exists())
			return null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.parse(FileName);

		NodeList list = doc.getChildNodes().item(0).getChildNodes();

		return new Pair<>(Integer.valueOf(list.item(1).getTextContent()),
				Integer.valueOf(list.item(3).getTextContent()));
	}

	
	Document ParseXML(File xml) throws ParserConfigurationException {
		
		if(count_call==5) {
			System.out.println("We tried 5 times to read this file "+xml.getAbsolutePath()+", but we fail!");
			System.out.println("You may clean "+AlgorithmParameters.ResumeDir+" and start a new run");
			System.exit(-1);
		}
		
		// Instantiate the Factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// optional, but recommended
		// process XML securely, avoid attacks like XML External Entities (XXE)
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		// parse XML file
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc = db.parse(xml);
		} catch (SAXException e) {

			e.printStackTrace();
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			count_call++;
			return ParseXML(xml);
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		return doc;
	}
}
