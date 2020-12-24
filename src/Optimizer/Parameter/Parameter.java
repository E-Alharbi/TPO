package Optimizer.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Optimizer.Parameter.Type.OptionsSet;

public class Parameter {

	String Keyword;
	String Value;
	String SecondKeyword = null;

	ValueType type;
	ParameterType Parametertype;
	int LowerLimitforNumberType = 0;
	int UpperLimitforNumberType = Integer.MAX_VALUE;// Default
	// String [] SetOfOptionsType;
	boolean Used = true;
	boolean Optimize = false;
	boolean Combination = false;
	OptionsSet optionsset = null;

	// Copy Constructor
	public Parameter(Parameter P) {

		this.Keyword = new String(P.GetKeyword());
		this.Value = new String(P.Value);
		if (P.GetSecondKeyword() == null)
			this.SecondKeyword = null;
		else
			this.SecondKeyword = new String(P.GetSecondKeyword());

		this.type = P.GetValueType();
		this.Parametertype = P.GetParameterType();
		this.LowerLimitforNumberType = new Integer(P.GetLowerLimitforNumberType());
		this.UpperLimitforNumberType = new Integer(P.GetUpperLimitforNumberType());

		if (P.GetSetOfOptionsType() == null)
			this.optionsset = null;
		else
			this.optionsset = new OptionsSet(P.optionsset);

		this.Used = new Boolean(P.IsUsed());
		this.Optimize = new Boolean(P.IsThisParameterNeedToOptimize());
		this.Combination = new Boolean(P.Combination);

	}

	public boolean IsUsed() {
		return Used;
	}

	public boolean IsCombination() {
		return Combination;
	}

	public boolean IsThisParameterNeedToOptimize() {
		return Optimize;
	}

	public enum ValueType {
		Number, String, File, SetOfOptions;
	}

	public enum ParameterType {
		Optional, Compulsory;
	}

	public Parameter() {

	}

	public Parameter(String keyword, String value, ParameterType Parametertype, ValueType type, boolean Optimize) {
		this.Keyword = keyword;
		this.Value = value;
		this.Parametertype = Parametertype;
		this.type = type;
		this.Optimize = Optimize;

	}

	public Parameter(String keyword, String value, ParameterType Parametertype, ValueType type, OptionsSet optionsset,
			boolean Optimize, boolean Combination) {
		this(keyword, value, Parametertype, type, Optimize);

		this.optionsset = optionsset;
		this.Combination = Combination;
		if (Combination == false && this.optionsset.size() > 1) {
			System.out.println(
					"Warring: You use mutiple item groups and you set Combination to false. Only first item group will be used and the rest will be ignored");
		}
	}

	public Parameter(String keyword, String value, ParameterType Parametertype, ValueType type,
			int LowerLimitforNumberType, int UpperLimitforNumberType, boolean Optimize) {
		this(keyword, value, Parametertype, type, Optimize);
		this.LowerLimitforNumberType = LowerLimitforNumberType;
		this.UpperLimitforNumberType = UpperLimitforNumberType;
	}

	public Parameter(String keyword, String value, String SecondKeyword, ParameterType Parametertype, ValueType type,
			boolean Optimize) {
		this.Keyword = keyword;
		this.Value = value;
		this.Parametertype = Parametertype;
		this.type = type;
		this.SecondKeyword = SecondKeyword;
		this.Optimize = Optimize;
	}

	public Parameter(String keyword, String value, String SecondKeyword, ParameterType Parametertype, ValueType type,
			OptionsSet optionsset, boolean Optimize) {
		this(keyword, value, SecondKeyword, Parametertype, type, Optimize);

		this.optionsset = optionsset;
	}

	public Parameter(String keyword, String value, String SecondKeyword, ParameterType Parametertype, ValueType type,
			int LowerLimitforNumberType, int UpperLimitforNumberType, boolean Optimize) {
		this(keyword, value, SecondKeyword, Parametertype, type, Optimize);
		this.LowerLimitforNumberType = LowerLimitforNumberType;
		this.UpperLimitforNumberType = UpperLimitforNumberType;
	}

	public boolean GetOptimize() {
		return Optimize;
	}

	public String GetKeyword() {
		return Keyword;
	}

	public String GetSecondKeyword() {
		return SecondKeyword;
	}

	public String GetValue() {
		if (SecondKeyword != null)
			return SecondKeyword + " " + Value;
		return Value;
	}

	public String GetValueofSecondKeyword() {
		if (SecondKeyword != null)
			return Value;

		return null;
	}

	public void SetValue(String v) {
		Value = v;
	}

	public ValueType GetValueType() {
		return type;
	}

	public void SetValueType(ValueType type) {
		this.type = type;
	}

	public ParameterType GetParameterType() {
		return Parametertype;
	}

	public int GetLowerBoundParameterType() {
		if (GetParameterType() == ParameterType.Compulsory)
			return 1;
		return 0;

	}

	public int GetUpperBoundParameterType() {
		return 1; // always one
	}

	public int GetLowerLimitforNumberType() {
		return LowerLimitforNumberType;
	}

	public int GetUpperLimitforNumberType() {
		return UpperLimitforNumberType;
	}

	public OptionsSet GetSetOfOptionsType() {
		return optionsset;
	}

	public List<String> ToList() {
		List<String> ParameterInArray = new ArrayList<String>();

		if (GetKeyword().length() != 0) {

			ParameterInArray.add(GetKeyword());
			if (GetValue().length() != 0)
				ParameterInArray.add(GetValue());
		} else {
			// System.out.println(toString());
			if (GetValue().length() != 0)
				ParameterInArray.add(GetValue());
		}
		return ParameterInArray;
	}

	public Vector<Integer> LowerBound() {
		Vector<Integer> LowerBounds = new Vector<Integer>();
		if (GetValueType() == ValueType.Number) {
			for (int i = 0; i < LengthInIndividual(); ++i)
				LowerBounds.add(GetLowerLimitforNumberType());
			// return GetLowerLimitforNumberType();
		} else {
			for (int i = 0; i < LengthInIndividual(); ++i)
				LowerBounds.add(0);
			// return 0;
		}
		return LowerBounds;
	}

	public Vector<Integer> UpperBound() {
		Vector<Integer> UpperBounds = new Vector<Integer>();
		if (GetValueType() == ValueType.Number) {
			UpperBounds.add(GetUpperLimitforNumberType());
			// return GetUpperLimitforNumberType();
		}
		if (GetValueType() == ValueType.File) {
			UpperBounds.add(1);
			// return 1;
		}
		if (GetValueType() == ValueType.String) {
			UpperBounds.add(1);
			// return 1;
		}
		if (GetValueType() == ValueType.SetOfOptions && this.Combination == false) {
			UpperBounds.add(GetSetOfOptionsType().GetByIndex(LengthInIndividual() - 1).size() - 1);

			// return GetSetOfOptionsType().length-1; // last element in an array is len -1

		}
		if (GetValueType() == ValueType.SetOfOptions && this.Combination == true) {
			for (int i = 0; i < LengthInIndividual(); ++i) {
				UpperBounds.add(GetSetOfOptionsType().GetByIndex(i).size() - 1);
			}

			// return GetSetOfOptionsType().length-1; // last element in an array is len -1

		}
		return UpperBounds;

	}

	public int LengthInIndividual() {
		if (GetValueType() == ValueType.SetOfOptions && this.Combination == true)
			return GetSetOfOptionsType().size();

		return 1; // else

	}

	@Override
	public String toString() {
		return "Parameter [Keyword=" + Keyword + ", Value=" + Value + ", SecondKeyword=" + SecondKeyword + ", type="
				+ type + ", Parametertype=" + Parametertype + ", LowerLimitforNumberType=" + LowerLimitforNumberType
				+ ", UpperLimitforNumberType=" + UpperLimitforNumberType + ", SetOfOptionsType=" + GetSetOfOptionsType()
				+ ", Used=" + Used + ", LowerBound()=" + LowerBound() + ", UpperBound()=" + UpperBound() + "]";
	}

	public void SetValueBasedOnOptimizationAlgorithm(int EncodedUsed, Vector<Integer> Value) {

		if ((GetValueType() == ValueType.File || GetValueType() == ValueType.String) && EncodedUsed == 0) {
			Used = false;
		}
		if ((GetValueType() == ValueType.File || GetValueType() == ValueType.String) && EncodedUsed != 0) {
			Used = true;
		}
		if (GetValueType() == ValueType.Number) {
			if (EncodedUsed == 0) {
				Used = false;
				SetValue(String.valueOf(Value.get(0)));
			} else {
				Used = true;
				SetValue(String.valueOf(Value.get(0)));
			}
		}

		if (GetValueType() == ValueType.SetOfOptions) {
			if (EncodedUsed == 0) {
				Used = false;

			} else {
				Used = true;
				String v = "";
				for (int i = 0; i < Value.size(); ++i) {
					// v+=GetSetOfOptionsType()[Value.get(i)];
					v += GetSetOfOptionsType().GetByIndex(i).GetByIndex(Value.get(i));
				}
				SetValue(v);
			}
		}

	}

}
