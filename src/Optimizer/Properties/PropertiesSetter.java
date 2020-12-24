package Optimizer.Properties;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.Operator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.IntegerSolution;

import Optimizer.Algorithms.OptimizationAlgorithm;
import Optimizer.Parameter.AlgorithmParameters;

public class PropertiesSetter {

	public void Set(HashMap<String, String> PropertiesValues) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException {

		for (String name : PropertiesValues.keySet()) {

			String key = name.toString();
			String value = PropertiesValues.get(name).toString();

			SetField(key, value);

			if (key.equals("Algorithm")) {

				if (SetAlgorithm(value) == false)
					System.out.println(value + " not recognised");
			}
			if (key.equals("Crossover")) {

				if (SetCrossover(value, PropertiesValues) == false)
					System.out.println("Unable to set crossover");
			}
			if (key.equals("Mutation")) {

				if (SetMutation(value, PropertiesValues) == false)
					System.out.println("Unable to set mutation" + SetMutation(value, PropertiesValues));
			}
			System.out.println(key + " " + value);
			if (key.equals("Selection")) {
				if (SetSelection(value) == false)
					System.out.println("Unable to set selection");
			}
		}
	}

	boolean SetAlgorithm(String value) throws InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("Optimizer.Algorithms");

		Set<Class<? extends OptimizationAlgorithm>> modules = reflections
				.getSubTypesOf(Optimizer.Algorithms.OptimizationAlgorithm.class);
		for (Iterator<Class<? extends OptimizationAlgorithm>> it = modules.iterator(); it.hasNext();) {
			Class<? extends OptimizationAlgorithm> f = it.next();
			if (value.equals(f.getSimpleName())) {
				AlgorithmParameters.algorithm = f.newInstance();

				return true;
			}

		}
		return false;
	}

	boolean SetField(String key, String value) throws NoSuchFieldException, SecurityException, NumberFormatException,
			IllegalArgumentException, IllegalAccessException {
		Field[] Fields = AlgorithmParameters.class.getFields();
		for (Field F : Fields) {
			if (key.equals(F.getName())) {
				Field field = AlgorithmParameters.class.getDeclaredField(F.getName());

				if (double.class.isAssignableFrom(F.getType())) {
					field.setDouble(field, Double.parseDouble(value));
					return true;
				}
				if (int.class.isAssignableFrom(F.getType())) {
					field.setInt(field, Integer.parseInt(value));
					return true;
				}

			}

		}
		return false;
	}

	Field GetField(String Field) {
		Field[] Fields = AlgorithmParameters.class.getFields();
		for (Field F : Fields) {
			if (Field.equals(F.getName()) && double.class.isAssignableFrom(F.getType()))
				return F;
		}
		return null;
	}

	boolean SetCrossover(String value, HashMap<String, String> PropertiesValues)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Reflections reflections = new Reflections("org.uma.jmetal.operator.impl.crossover");
		Set<Class<? extends CrossoverOperator>> allClasses = reflections.getSubTypesOf(CrossoverOperator.class);
		for (Iterator<Class<? extends CrossoverOperator>> it = allClasses.iterator(); it.hasNext();) {
			CrossoverOperator<IntegerSolution> cc;
			Class<? extends CrossoverOperator> operator = it.next();
			if (value.contains(operator.getSimpleName())) {
				if (operator.getGenericInterfaces()[0].getTypeName().contains("IntegerSolution")
						|| operator.getGenericInterfaces()[0].getTypeName().contains("PermutationSolution")) {

					Method[] m = operator.getMethods();
					// System.out.println( m[0].get);
					int NumberOfSetter = 0;
					for (Method c : m) {
						if (c.getName().startsWith("set"))
							NumberOfSetter++;
					}

					Object[] ConstructorParameters = new Object[NumberOfSetter];
					Arrays.fill(ConstructorParameters, 0);
					Constructor<?>[] Constructors = operator.getConstructors();
					for (Constructor<?> con : Constructors) {
						if (con.getParameterCount() == NumberOfSetter)
							AlgorithmParameters.Crossover = (CrossoverOperator<IntegerSolution>) con
									.newInstance(ConstructorParameters);
					}

					return SetConstructorParameters(m, PropertiesValues, AlgorithmParameters.Crossover);

				}
			}
		}
		return false;
	}

	boolean SetMutation(String value, HashMap<String, String> PropertiesValues)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Reflections reflections = new Reflections("org.uma.jmetal.operator.impl.mutation");
		Set<Class<? extends MutationOperator>> allClasses = reflections.getSubTypesOf(MutationOperator.class);
		for (Iterator<Class<? extends MutationOperator>> it = allClasses.iterator(); it.hasNext();) {
			MutationOperator<IntegerSolution> cc;
			Class<? extends MutationOperator> operator = it.next();
			if (value.contains(operator.getSimpleName())) {
				if (operator.getGenericInterfaces()[0].getTypeName().contains("IntegerSolution")
						|| operator.getGenericInterfaces()[0].getTypeName().contains("PermutationSolution")) {

					Method[] m = operator.getMethods();
					// System.out.println( m[0].get);
					int NumberOfSetter = 0;
					for (Method c : m) {
						if (c.getName().startsWith("set"))
							NumberOfSetter++;
					}

					Object[] ConstructorParameters = new Object[NumberOfSetter];
					Arrays.fill(ConstructorParameters, 0);
					Constructor<?>[] Constructors = operator.getConstructors();
					for (Constructor<?> con : Constructors) {

						Parameter[] ConParameters = con.getParameters();
						boolean IsConstructorParametersDouble = true;
						for (Parameter P : ConParameters) {

							if (!P.getParameterizedType().toString().equals("double"))
								IsConstructorParametersDouble = false;
						}
						if (con.getParameterCount() == NumberOfSetter && IsConstructorParametersDouble == true)
							AlgorithmParameters.Mutation = (MutationOperator<IntegerSolution>) con
									.newInstance(ConstructorParameters);
					}

					return SetConstructorParameters(m, PropertiesValues, AlgorithmParameters.Mutation);

				}
			}
		}
		return false;
	}

	boolean SetConstructorParameters(Method[] methods, HashMap<String, String> PropertiesValues, Operator operator)
			throws NumberFormatException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Method c : methods) {

			if (c.getName().startsWith("set")) {

				if (PropertiesValues.containsKey(c.getName().substring(3))) {

					c.invoke(operator, Double.parseDouble(PropertiesValues.get(c.getName().substring(3))));

				}

				else if (GetField(c.getName().substring(3)) != null) {
					c.invoke(operator, GetField(c.getName().substring(3)).getDouble(this));

				} else {
					System.out.println(c.getName().substring(3) + " Not found in properties.");
					return false;
				}
			}
		}
		return true;
	}

	boolean SetSelection(String value) throws InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("org.uma.jmetal.operator.impl.selection");
		Set<Class<? extends SelectionOperator>> allClasses = reflections.getSubTypesOf(SelectionOperator.class);
		for (Iterator<Class<? extends SelectionOperator>> it = allClasses.iterator(); it.hasNext();) {
			MutationOperator<IntegerSolution> cc;
			Class<? extends SelectionOperator> operator = it.next();
			if (value.contains(operator.getSimpleName())) {
				Constructor<?>[] Constructors = operator.getConstructors();
				for (Constructor<?> con : Constructors) {
					if (con.getParameterCount() == 0) {
						AlgorithmParameters.Selection = operator.newInstance();
						return true;
					}
				}
			}

		}
		return false;
	}
}
