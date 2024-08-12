# Welcome to Tool Parameters Optimizer(TPO) documentation
Tool Parameters Optimiser (TPO) is a tool for optimising any tool’s parameters that can run from the command line.

## Installation

TPO can be downloaded from GitHub

```
https://github.com/E-Alharbi/TPO
```

Also, it is available in the Maven repository (V2.0 is not available yet in the Maven repository)

```xml
  <!-- https://mvnrepository.com/artifact/com.github.e-alharbi/Tool-Parameters-Optimizer -->
<dependency>
    <groupId>com.github.e-alharbi</groupId>
    <artifactId>Tool-Parameters-Optimizer</artifactId>
    <version>1.1</version>
</dependency>


```


The document  contains the following  
1. Simple example
	* Single objective optimisation
    * Multi-objective optimisation


2. Advanced example
    * Single objective optimisation


3. Optimisation algorithms


4. Configuration of optimisation algorithms


5. Parameters
    * Parameters types
    * Parameters value types


6. Output of optimised tool
    * Search for files
    * Save the log file
      
7. Resume running
   
    
## 1. Simple example

### Single objective optimisation

The Drop-Wave function is used to test optimisation frameworks and has two optimisation parameters. Its optimal objective is -1 at 0 and 0 for the two parameters. The following is an implementation of this function in Java.

```java
 public static void main(String[] args) {
        double x1=Double.valueOf(args[0]);
		double x2=Double.valueOf(args[1]);
		double frac1=1+Math.cos(12*Math.sqrt(Math.pow(x1,2)+Math.pow(x2,2)));
		double frac2=0.5* (Math.pow(x1,2)+Math.pow(x2,2))+2;
		System.out.println(-frac1/frac2);
     }
```

A runnable jar of this function is in [/Optimizer/Tester/DROPWAVEFUNCTION.jar](https://github.com/E-Alharbi/TPO/blob/master/src/Optimizer/Tester/DROPWAVEFUNCTION.jar).

The tool can be run from the command line using this command.

```sh 
java -jar DROPWAVEFUNCTION.jar 2 3
```

Now, let's use TPO to optimise these parameters.

We need to implement your solution evaluator. In TPO, this is made easy by only extending the Abstract class Problem. See the example below. 
```java
public class DropWaveProblem extends Problem {
	private static final long serialVersionUID = 1L;
	public DropWaveProblem(Tool tool) {
		super(tool);
	}
	@Override
	public void evaluate(IntegerSolution solution) {
		Tool DropWave = new Tool(this.tool);
		DropWave.SetParametersValueBasedOnOptimizationAlgorithm(solution);
		DropWave.SetNoWorkingPath(true);
		try {
			DropWave.Run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		solution.setObjective(0, Double.valueOf(DropWave.GetLog()));
	}
}
```

In the evaluate function, you need to place the first few lines. 

1- Define your tool as the following:
```java
	Tool DropWave = new Tool(this.tool);
```

2- Decode the solution to actual parameter values.
```java
		DropWave.SetParametersValueBasedOnOptimizationAlgorithm(solution);
```

3- Run your tool to get the output using newly suggested parameter values. 
```java
		try {
			DropWave.Run();
		} catch (IOException e) {
			e.printStackTrace();
		}
```

3- Get your tool output and send it back to the search algorithm. In this case, we do not need to parse the log file, as our test function only prints out the function results. If your tool prints a complicated log, you need to do some parsing.   
```java
		solution.setObjective(0, Double.valueOf(DropWave.GetLog()));
```

Now, we can write the code to run the search algorithm. 
```java
 public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ParserConfigurationException, TransformerException, NoSuchMethodException,
			SecurityException, SAXException, IOException, InterruptedException {

		Tool SE = new Tool("DropWaveExample");
		Vector<Parameter> keywords = new Vector<Parameter>();
		keywords.addElement(
				new Parameter("java", "-jar", Parameter.ParameterType.Optional, Parameter.ValueType.File, false));
		keywords.addElement(new Parameter("DROPWAVEFUNCTION.jar", "", Parameter.ParameterType.Compulsory,
				Parameter.ValueType.File, false));

		OptionsSet os = new OptionsSet();

		ItemGroup x1 = new ItemGroup();

		for (double x = -5.12; x < 5.13; x = x + 0.01) {

			x1.add(new BigDecimal(x).setScale(2, RoundingMode.CEILING).toString());
		}
		os.add(x1);
		keywords.addElement(new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
				os, true, true));
		keywords.addElement(new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
				os, true, true));

		SE.SetKeywords(keywords);

		AlgorithmParameters.MaxEvaluations = 500;
		AlgorithmParameters.PopulationSize = 50;

		Problem pro = new DropWaveProblem(SE);

		ParallelNSGAII EA1 = new ParallelNSGAII(pro);

		Vector<OptimizationAlgorithm> Algorithms = new Vector<OptimizationAlgorithm>();
		Algorithms.add(EA1);

		MultiAlgorithmsRunnerMultiThreded m = new MultiAlgorithmsRunnerMultiThreded(Algorithms, pro);

		m.Run();

	}

```

Here, we define the tool we want to optimise and its parameters. Usually, the first parameter is the command we use to run the tool. In our case, the tool is written in Java, so we use 'java—jar', and in Python, we should use 'python' as the first parameter. The second parameter is the tool file named 'DROPWAVEFUNCTION.jar'. 

The TPO does not support decimal parameters directly, so to workaround this, we set the values in 'Group' for the two parameters each in one 'Group'. We set the two parameters to 'Compulsory'. 
```java
	Tool SE = new Tool("DropWaveExample");
		Vector<Parameter> keywords = new Vector<Parameter>();
		keywords.addElement(
				new Parameter("java", "-jar", Parameter.ParameterType.Optional, Parameter.ValueType.File, false));
		keywords.addElement(new Parameter("DROPWAVEFUNCTION.jar", "", Parameter.ParameterType.Compulsory,
				Parameter.ValueType.File, false));

		OptionsSet os = new OptionsSet();

		ItemGroup x1 = new ItemGroup();

		for (double x = -5.12; x < 5.13; x = x + 0.01) {

			x1.add(new BigDecimal(x).setScale(2, RoundingMode.CEILING).toString());
		}
		os.add(x1);
		keywords.addElement(new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
				os, true, true));
		keywords.addElement(new Parameter("", "1", Parameter.ParameterType.Compulsory, Parameter.ValueType.SetOfOptions,
				os, true, true));

		SE.SetKeywords(keywords);
```
Finally, we choose the search algorithm we want to use and add it to the 'vector'.

```java
	AlgorithmParameters.MaxEvaluations = 500;
		AlgorithmParameters.PopulationSize = 50;

		Problem pro = new DropWaveProblem(SE);

		ParallelNSGAII EA1 = new ParallelNSGAII(pro);

		Vector<OptimizationAlgorithm> Algorithms = new Vector<OptimizationAlgorithm>();
		Algorithms.add(EA1);

		MultiAlgorithmsRunnerMultiThreded m = new MultiAlgorithmsRunnerMultiThreded(Algorithms, pro);

		m.Run();

	}

```







The output of TPO is an XML file that contains the parameters and their optimal values.

`BestParameters_DropWaveExample_1`

```xml
 <?xml version="1.0" encoding="UTF-8" standalone="no"?>
<Tool Name="DropWaveExample">
    <Parameters>
        <Parameter>
            <Keyword/>
            <Value>0.02</Value>
            <Used>true</Used>
        </Parameter>
        <Parameter>
            <Keyword/>
            <Value>0.00</Value>
            <Used>true</Used>
        </Parameter>
    </Parameters>
    <Objectives>-0.98557043038297653669843612078693695366382598876953125</Objectives>
</Tool>

```

### Multi-objective optimisation

TPO can optimise multi-objective problems; however, you need to make small changes to the solution evaluator. You have to set the number of objectives and the feedback to the search algorithms for all the objective values. See the example below.  

```java
public class DropWaveProblem extends Problem {
	private static final long serialVersionUID = 1L;
	public DropWaveProblem(Tool tool) {
		super(tool);
		setNumberOfObjectives(2);
	}
	@Override
	public void evaluate(IntegerSolution solution) {
		Tool DropWave = new Tool(this.tool);
		DropWave.SetParametersValueBasedOnOptimizationAlgorithm(solution);
		DropWave.SetNoWorkingPath(true);
		try {
			DropWave.Run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		solution.setObjective(0, Double.valueOf(DropWave.GetLog()));
		solution.setObjective(1, Double.valueOf(DropWave.GetLog()));
	}
}
```




   
### Single objective optimisation

In this example, we optimise the parameters of a tool called Buccaneer. It is a tool to build a protein model, and it has too many parameters. We will optimise some of these parameters.

Here are the Buccaneer’s parameters



| Parameter | Value type | Need to optimise? |
| ------------- | ------------- |-------------|
| mtzin | File | No|
| seqin | File | No|
| colin-fo | String | No|
| colin-hl | String | No|
| buccaneer-fast | String | Yes|
| cycles | Number | Yes|


In the below code, we defined Buccaneer parameters and their values. In line 10, we have given the parameter a value. This value is just a dummy value. The optimisation algorithm will not be necessary to use this dummy value because it will be changed during the optimisation process. In lines 11 and 12, these two parameters have no values because they are boolean flags.

```java
             Tool tool = new Tool("Buccaneer");
             Vector<Parameter> keywords = new Vector<Parameter>();
             keywords.addElement(new Parameter("buccaneer_pipeline","", Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false ));

             keywords.addElement(new Parameter("-mtzin",mtz,Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false));
             keywords.addElement(new Parameter("-seqin",seq,Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false));
             keywords.addElement(new Parameter("-colin-fo","FP,SIGFP",Parameter.ParameterType.Compulsory,Parameter.ValueType.String,false));
             keywords.addElement(new Parameter("-colin-hl","model.HLA,model.HLB,model.HLC,model.HLD",Parameter.ParameterType.Compulsory,Parameter.ValueType.String,false));
             keywords.addElement(new Parameter("-colin-free","FREE",Parameter.ParameterType.Compulsory,Parameter.ValueType.String,false));
             keywords.addElement(new Parameter("-cycles","5",Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,5,25,true));
             keywords.addElement(new Parameter("-buccaneer-anisotropy-correction","",Parameter.ParameterType.Compulsory,Parameter.ValueType.String,false));
             keywords.addElement(new Parameter("-buccaneer-fast","",Parameter.ParameterType.Optional,Parameter.ValueType.String,true));
             tool.SetKeywords(keywords);
             tool.Run();
        ParallelNSGAII EA1 = new ParallelNSGAII(new BuccaneerOptimizationProblem(tool));
		Vector<OptimizationAlgorithm> Algorithms = new Vector<OptimizationAlgorithm>();
		Algorithms.add(EA1);
		MultiAlgorithmsRunnerMultiThreded m = new MultiAlgorithmsRunnerMultiThreded(Algorithms, pro);
		m.Run();
```

Now we have to write the evaluator class. As you can see below in the evaluate method, we set the new Buccaneer’s parameters values, and we run it using these new parameters values. Once Buccaneer done, it will create a file called Buccaneer.pdb which we will use to evaluate the performance of Buccaneer. In line 23, we use output to search for this file in the Buccaneer directory and calculate the completeness we use here to evaluate the Buccaneer performance.

```java
     @SuppressWarnings("serial")
     public class BuccaneerOptimizationProblem extends Problem {

             Tool tool;
             public BuccaneerOptimizationProblem (Tool tool) {
                     super(tool);
                     this.tool=tool;

             }

             @Override
             public void evaluate(IntegerSolution solution) {


                      Tool tool= new Tool(this.tool);
                      tool.SetParametersValueBasedOnOptimizationAlgorithm(solution);
                     try {
                             tool.Run();
                     } catch (IOException e2) {

                             e2.printStackTrace();
                     }
                     Output out = new Output(tool);
                     solution.setObjective(0, -1 * Completeness(out.SerachForFileByName("buccaneer.pdb")));

     }

             }
```

## 3. Optimisation algorithms

Here are the algorithms that are supported by TPO

| Algorithms | Single/Multi objective |
| ------------- | ------------- |
| CoralReefs | Single |
| ESPEA | Multi |
| PAES | Multi |
| EvolutionStrategy | Single |
| NonElitistEvolutionStrategy | Single |
| ParallelMOCell | Multi |
| ParallelNSGAII | Multi |
| ParallelNSGAIII | Multi |
| ParallelSPEA2 | Multi |
| SMSEMOA | Multi |
| GA | Single |
| ParallelGA | Single |
| SteadyStateGA | Single |  
| RandomSearch | Multi | 


## 4. Configuration of optimisation algorithms

Below are the optimisation algorithm parameters. These parameters set to the default. However, it can be changed using static class AlgorithmParameters

```java
     public static double CrossoverProbability=0.9;
     public static double DistributionIndex=20;
     public static double MutationProbability=0.2;
     public static int CoralReefsM=10;
     public static int CoralReefsN=10;
     public static double CoralReefsRho=0.6;
     public static double CoralReefsFbs=0.9;
     public static double CoralReefsFbr=0.1;
     public static double CoralReefsFa=0.1;
     public static double CoralReefsPd=0.1;
     public static int CoralReefsttemptsToSettle=3;
     public static int MaxEvaluations=40000;
     public static int EvolutionStrategyMu=1;
     public static int EvolutionStrategyLambda=10;
     public static int PopulationSize=1000;
     public static int numberOfCores=Runtime.getRuntime().availableProcessors();
     public static int archiveSize=200;
     public static double epsilon = 0.01 ;
     public static int BiSections=5;
     public static int hypervolume=100;
     public static int K=1;
```

## 5. Parameters

### Parameters types

You can set either a parameter is optional or compulsory  (`Parameter.ParameterType.Compulsory`  or `Parameter.ParameterType.Optional`). Optional here means an optimisation algorithm to determine whether the tool is better when run with or without this optional parameter.

### Parameters value types


1. `Parameter.ValueType.Number`


2. `Parameter.ValueType.String`


3. `Parameter.ValueType.File`


4. `Parameter.ValueType.SetOfOptions`

Use a set of options when a keyword takes single or multiple values from a set of values. For example, if a tool uses a keyword x and the possible group of values for this keyword is {a,b,c}. The parameter should be declared as in the below code.

```java
     OptionsSet os = new OptionsSet();

                     ItemGroup i1 = new ItemGroup();
                     i1.add("a");
                     i1.add("b");
                     i1.add("c");
                     os.add(i1);


     Parameter opts=new Parameter("x","",Parameter.ParameterType.Compulsory,Parameter.ValueType.SetOfOptions,os,true,true);
             tool.GetKeywords().add(opts);
```

In the above code, the tool will be run using x keyword like this `java -jar tool x a` or `java -jar tool x b` or `java -jar tool x c`

If you have multiple groups of values, and you want the keyword, x takes a value from each group in a combination, use the set of options as the following code.

```java
     OptionsSet os = new OptionsSet();

                     ItemGroup i1 = new ItemGroup();
                     i1.add("a");
                     i1.add("b");
                     i1.add("c");
                     os.add(i1);



                     ItemGroup i2 = new ItemGroup();
                     i2.add("e");
                     i2.add("f");
                     i2.add("g");
                     os.add(i2);


     Parameter opts=new Parameter("x","",Parameter.ParameterType.Compulsory,Parameter.ValueType.SetOfOptions,os,true,true);
             tool.GetKeywords().add(opts);
```

In the above code, the tool will be run using different x’s values combinations. For example, `java -jar tool x ag`

## 6. Output of optimised tool

### Search for files

If your tool that you to optimise write out some files and you to access to these files, TPO has a class called `Output` to easily manages these files. Assumes we have a Tool. This tool creates a working directory called “ToolDirectory” and writes an output file to this directory called “File1”. The following code shows how you can access to File1.

```java
     Output out = new Output(Tool);
     Vector<File> files = out.SerachForFileByName("File1")
```

You can also search recursively using `out.FilesByNameRecursively`

### Save log file

If you want to save a log file produced by the tool that you want to optimise, `Output` class has a method to save this log file.

```java
     Output out = new Output(Tool);
     out.SaveLog();
```

## 7. Resume running
    TPO saves intermediate results in 'Checkpoints'. You can rerun it if it stops, and TPO will resume at the last saved checkpoint.  

## 8. Monitoring search algorithm progress
TPO visualizes the progress of search algorithms by showing a progress bar for each iteration. For example, if you set 'AlgorithmParameters.MaxEvaluations = 500' and AlgorithmParameters.PopulationSize = 50, TPO will run 10 iterations and show a progress bar for each iteration.  

![Screen Recording 2024-08-08 at 11 21 26](https://github.com/user-attachments/assets/c83a4d13-6788-4179-b236-eb661f5b19e9)


## Citing
```
@software{TPO,
  author = {Alharbi,Emad},
  doi = {},
  month = {03},
  title = {{TPO: A tool for optimising a tool's parameters}},
  url = {https://github.com/E-Alharbi/TPO},
  year = {2022}
}
```
    
