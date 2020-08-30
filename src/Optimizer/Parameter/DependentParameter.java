package Optimizer.Parameter;

import Optimizer.Parameter.Parameter.ParameterType;
import Optimizer.Parameter.Parameter.ValueType;

public class DependentParameter extends Parameter  {

	Parameter PrimaryParameter;
	
	public DependentParameter (String keyword, String value,ParameterType Parametertype , ValueType type,Parameter PrimaryParameter,boolean Optimize) {
		super( keyword,  value, Parametertype ,  type,Optimize);
		this.PrimaryParameter=PrimaryParameter;
	}
	public DependentParameter (String keyword, String value,ParameterType Parametertype , ValueType type , String [] SetOfOptionsType,Parameter PrimaryParameter,boolean Optimize) {
		super( keyword,  value, Parametertype ,  type ,  SetOfOptionsType,Optimize);
		this.PrimaryParameter=PrimaryParameter;
	}
	public DependentParameter (String keyword, String value,ParameterType Parametertype , ValueType type,int LowerLimitforNumberType ,int UpperLimitforNumberType,Parameter PrimaryParameter,boolean Optimize) {
		super( keyword,  value, Parametertype ,  type, LowerLimitforNumberType , UpperLimitforNumberType,Optimize);
		this.PrimaryParameter=PrimaryParameter;
	}
	
	public DependentParameter (String keyword, String value , String SecondKeyword,ParameterType Parametertype , ValueType type,Parameter PrimaryParameter,boolean Optimize) {
		super( keyword,  value, SecondKeyword, Parametertype ,  type,Optimize);
		this.PrimaryParameter=PrimaryParameter;
	}
	public DependentParameter (String keyword, String value, String SecondKeyword,ParameterType Parametertype , ValueType type , String [] SetOfOptionsType,Parameter PrimaryParameter,boolean Optimize) {
		super( keyword,  value,  SecondKeyword, Parametertype ,  type ,  SetOfOptionsType,Optimize);
		this.PrimaryParameter=PrimaryParameter;
	}
	public DependentParameter (String keyword, String value , String SecondKeyword,ParameterType Parametertype , ValueType type,int LowerLimitforNumberType ,int UpperLimitforNumberType,Parameter PrimaryParameter,boolean Optimize) {
		super( keyword,  value, SecondKeyword, Parametertype ,  type, LowerLimitforNumberType , UpperLimitforNumberType,Optimize);
		this.PrimaryParameter=PrimaryParameter;
	}
	
}
