[![Build Status](https://travis-ci.com/E-Alharbi/TPO.svg?token=z92wc12inrqPgG6Faxv2&branch=master)](https://travis-ci.com/E-Alharbi/TPO)
# Welcome to Tool Parameters Optimizer(TPO) documentation

Tool Parameters Optimizer(TPO) is a tool for optimizing any tool’s parameters that can run from command line.

## Installation

TPO can be downloaded from GitHub

```
https://github.com/E-Alharbi/ToolParametersOptimizer
```

Also, it is available in Maven repository

```
  <!-- https://mvnrepository.com/artifact/com.github.e-alharbi/Tool-Parameters-Optimizer -->
<dependency>
    <groupId>com.github.e-alharbi</groupId>
    <artifactId>Tool-Parameters-Optimizer</artifactId>
    <version>1.1</version>
</dependency>


```


* Simple example


    * Single objective optimization


    * Multi-objective optimization


* Advanced example


    * Single objective optimisation


* Optimization algorithms


* Configuration of optimization algorithms


* Parameters


    * Parameters types


    * Parameters value types


* Output of optimised tool


    * Search for files


    * Save log file

    
    # Simple example

## Single objective optimization

Let assume we have a tool that fills in an array with ones. The tool runs from the command line and takes two parameters: the array start index and end index. Our objective is to fill in all elements in the array with ones. For example, if the array size is 100, then the two parameters should be 0 and 100.

The tool code:

```
 public static void main(String[] args) {


             int[] num = new int [100];
             int StartIndex=Integer.valueOf(args[0]);
             int EndIndex=Integer.valueOf(args[1]);
             for(int i=StartIndex ; i < EndIndex ; ++i) {
                     num[i]=1;
             }
             //Check if all elements are ones

             int CountOnes=0;
             for(int i=0 ; i < num.length ; ++i) {
                     if(num[i]==1)
                             CountOnes++;
             }
             System.out.println(CountOnes);

     }
```

Copy the above code in a java project and export a runnable jar file.

The tool can be run from the command line using this command.

```
java -jar SimpleExample.jar 0 50
```

Now, let use TPO to optimize these parameters.

In a new java project, copy the following code.

```
 public static void main(String[] args) {


             Tool SE = new Tool("SimpleExample");
             Vector<Parameter> keywords = new Vector<Parameter>();
             keywords.addElement(new Parameter("java","-jar", Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false ));
             keywords.addElement(new Parameter("SimpleExample.jar","", Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false ));
             keywords.addElement(new Parameter("","1", Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,0,100,true ));
             keywords.addElement(new Parameter("","2", Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,0,100,true ));

             SE.SetKeywords(keywords);

             ParallelGA EA = new ParallelGA(new SimpleExampleProblem(SE));

             SE.Report(EA.Run().get(0));

     }
```


1. Line 4: we were given the tool a name


2. Line 6 and 7: we added the required commands for java to run from the command line. For both these two-line, we used `Parameter.ParameterType.Compulsory` which mean these parameters are essential to run the tool.


3. Line 8 and 9 these are the parameters that we want to optimize. We used  `Parameter.ValueType.Number` and we set the maximum and minimum boundaries `0` and `100`. Also, we set optimize to `true`, means we want to optimize this parameter.


4. Line 11: we set the parameters to the tool.


5. Line 13: `ParallelGA` is the algorithm that we want to use to optimize the parameters. See Optimization algorithms section.


6. Line 14: we started to optimize the parameters.

We need now to write our solution evaluator that evaluate the solutions found by the optimization algorithm.

```
 public class SimpleExampleProblem extends Problem {

     public SimpleExampleProblem(Tool tool) {
             super(tool);
             setNumberOfObjectives(1);
             // TODO Auto-generated constructor stub
     }

     /**
      *
      */
     private static final long serialVersionUID = 1L;

     @Override
     public void evaluate(IntegerSolution solution) {
             Tool SimpleExample = new Tool(this.tool);
             SimpleExample.SetParametersValueBasedOnOptimizationAlgorithm(solution);
             try {
                     SimpleExample.Run();
             } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
             }

             int CountOnes=Integer.valueOf(SimpleExample.GetLog());
                     solution.setObjective(0, -1 * CountOnes);

     }

     }
```

The above code will evaluate the solution by running the tool and evaluate the output.

**NOTE**: We multiply the `CountOnes` by -1 because the algorithm tries to find the minimum solution and our optimization problem is a maximised problem.

The output of TPO is an XML file that contains the parameters and its optimal values.

`ParametersReport.xml`

```
 <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     <Parameters>
 <Parameter>
     <Keyword/>
     <Value>1</Value>
     <Used>True</Used>
 </Parameter>
 <Parameter>
     <Keyword/>
     <Value>100</Value>
     <Used>True</Used>
 </Parameter>
     </Parameters>
```

## Multi-objective optimization

We changed the tool that we want to optimize to fill in two arrays. Our objective is to fill in the first array by ones and the second by zeros.

```
     public static void main(String[] args) {
             // TODO Auto-generated method stub

             int[] num = new int [100];
             int[] num2 = new int [100];
             int StartIndex=Integer.valueOf(args[0]);
             int EndIndex=Integer.valueOf(args[1]);

             int StartIndex2=Integer.valueOf(args[2]);
             int EndIndex2=Integer.valueOf(args[3]);
             for(int i=StartIndex ; i < EndIndex ; ++i) {
                     num[i]=1;
             }

             for(int i=StartIndex2 ; i < EndIndex2 ; ++i) {
                     num2[i]=1;
             }
             //Check if all elements are ones

             int CountOnes=0;
             int CountOnes2=0;
             for(int i=0 ; i < num.length ; ++i) {
                     if(num[i]==1)
                             CountOnes++;
                     if(num2[i]==1)
                             CountOnes2++;
             }
             System.out.println(CountOnes+"-"+CountOnes2);

     }
```

The tool can be run from the command line using this command.

```
java -jar SimpleExample.jar 0 99 94 85
```

The output

```
99-0
```

We need to change the parameters to add these new parameters.

```
     Tool SE = new Tool("SimpleExample");
             Vector<Parameter> keywords = new Vector<Parameter>();
             keywords.addElement(new Parameter("java","-jar", Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false ));
             keywords.addElement(new Parameter("SimpleExample.jar","", Parameter.ParameterType.Compulsory,Parameter.ValueType.File,false ));

             keywords.addElement(new Parameter("","1", Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,0,100,true ));
             keywords.addElement(new Parameter("","2", Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,0,100,true ));

             keywords.addElement(new Parameter("","1", Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,0,100,true ));
             keywords.addElement(new Parameter("","2", Parameter.ParameterType.Compulsory,Parameter.ValueType.Number,0,100,true ));

             SE.SetKeywords(keywords);


             ParallelNSGAII  EA = new ParallelNSGAII(new SimpleExampleProblem(SE));


             SE.Report(EA.Run().get(0));
```

Also, we need to change the number of objectives.

```
     public class SimpleExampleProblem extends Problem {

     public SimpleExampleProblem(Tool tool) {
             super(tool);
             setNumberOfObjectives(2);
             // TODO Auto-generated constructor stub
     }

     /**
      *
      */
     private static final long serialVersionUID = 1L;

     @Override
     public void evaluate(IntegerSolution solution) {
             // TODO Auto-generated method stub
             Tool SimpleExample = new Tool(this.tool);


             SimpleExample.SetParametersValueBasedOnOptimizationAlgorithm(solution);
             SimpleExample.SetNoWorkingPath(true);

             try {
                     SimpleExample.Run();
             } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
             }

             int CountOnes=Integer.valueOf(SimpleExample.GetLog().split("-")[0]);
             int CountOnes2=Integer.valueOf(SimpleExample.GetLog().split("-")[1]);

                     solution.setObjective(0, -1 * CountOnes);
                     solution.setObjective(1,  CountOnes2 );




     }

     }
```


1. Line 5: we set the number of objectives to 2


2. Line 34: we add the second objective that decreases the ones in the second array.
 
 # Advanced example

## Single objective optimisation

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

```
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
             ParallelGA EA= new ParallelGA(new BuccaneerOptimizationProblem(tool));
             List<IntegerSolution> sol=EA.Run();
             tool.Report(sol.get(0));
```

Now we have to write the evaluator class. As you can see below in the evaluate method, we set the new Buccaneer’s parameters values, and we run it using these new parameters values. Once Buccaneer done, it will create a file called Buccaneer.pdb which we will use to evaluate the performance of Buccaneer. In line 23, we use Output to search for this file in the Buccaneer directory and calculate the completeness we use here to evaluate the Buccaneer performance.

```
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

# Optimization algorithms

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


# Configuration of optimization algorithms

Below are the optimization algorithms parameters. These parameters set to the default. However, it can be changed using static class AlgorithmParameters

```
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

# Parameters

## Parameters types

You can set either a parameter is optional or compulsory  (`Parameter.ParameterType.Compulsory`  or `Parameter.ParameterType.Optional`). Optional here means an optimisation algorithm to determine whether the tool is better when run with or without this optional parameter.

## Parameters value types


1. `Parameter.ValueType.Number`


2. `Parameter.ValueType.String`


3. `Parameter.ValueType.File`


4. `Parameter.ValueType.SetOfOptions`

Use a set of options when a keyword takes single or multiple values from a set of values. For example, if a tool uses a keyword x and the possible group of values for this keyword is {a,b,c}. The parameter should be declared as in the below code.

```
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

```
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

In the above code, the tool will be run in different x’s values combinations. For example, `java -jar tool x ag`

# Output of optimised tool

## Search for files

If your tool that you to optimise write out some files and you to access to these files, TPO has a class called `Output` to easily manages these files. Assumes we have a Tool. This tool creates a working directory called “ToolDirectory” and writes an output file to this directory called “File1”. The following code shows how you can access to File1.

```
     Output out = new Output(Tool);
     Vector<File> files = out.SerachForFileByName("File1")
```

You can also search recursively using `out.FilesByNameRecursively`

## Save log file

If you want to save a log file produced by the tool that you want to optimise, `Output` class has a method to save this log file.

```
     Output out = new Output(Tool);
     out.SaveLog();
```
    