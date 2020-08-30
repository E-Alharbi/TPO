package Optimizer.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.solution.IntegerSolution;

public class Parameter {

	String Keyword;
	String Value;
	String SecondKeyword=null;
	
	ValueType type;
	ParameterType Parametertype;
	int LowerLimitforNumberType=0;
	int UpperLimitforNumberType = Integer.MAX_VALUE;// Default
	String [] SetOfOptionsType;
	boolean Used=true;
	boolean Optimize=false;
	
	//Copy Constructor
	public Parameter (Parameter P) {
		
		this.Keyword= new String (P.GetKeyword());
		this.Value= new String (P.Value);
		if(P.GetSecondKeyword()==null)
			this.SecondKeyword=null;
		else
		this.SecondKeyword= new String (P.GetSecondKeyword());
		
		this.type= P.GetValueType();
		this.Parametertype= P.GetParameterType();
		this.LowerLimitforNumberType=new Integer (P.GetLowerLimitforNumberType());
		this.UpperLimitforNumberType=new Integer (P.GetUpperLimitforNumberType());
		
		if(P.GetSetOfOptionsType()==null)
			this.SetOfOptionsType=null;
		else
		this.SetOfOptionsType=P.GetSetOfOptionsType().clone();
		
		this.Used=new Boolean (P.IsThisParameterUsed());
		this.Optimize=new Boolean (P.IsThisParameterNeedToOptimize());
		
		
	}
	public boolean IsThisParameterUsed() {
		return Used ;
	}
	public boolean IsThisParameterNeedToOptimize() {
		return Optimize ;
	}
	public enum ValueType 
	{ 
	    Number, String,File, SetOfOptions; 
	} 
	public enum ParameterType 
	{ 
		Optional, Compulsory; 
	} 
	public Parameter () {
		
	}
	public Parameter (String keyword, String value,ParameterType Parametertype , ValueType type,boolean Optimize) {
		this.Keyword=keyword;
		this.Value=value;
		this.Parametertype=Parametertype;
		this.type=type;
		this.Optimize=Optimize;
		
	}
	public Parameter (String keyword, String value,ParameterType Parametertype , ValueType type , String [] SetOfOptionsType , boolean Optimize) {
		this( keyword,  value , Parametertype ,  type,Optimize );
		
		this.SetOfOptionsType=SetOfOptionsType;
	}
	public Parameter (String keyword, String value,ParameterType Parametertype , ValueType type,int LowerLimitforNumberType ,int UpperLimitforNumberType,boolean Optimize) {
		this( keyword,  value , Parametertype ,  type,Optimize );
		this.LowerLimitforNumberType=LowerLimitforNumberType;
		this.UpperLimitforNumberType=UpperLimitforNumberType;
	}
	
	
	
	public Parameter (String keyword, String value,String SecondKeyword,ParameterType Parametertype , ValueType type,boolean Optimize) {
		this.Keyword=keyword;
		this.Value=value;
		this.Parametertype=Parametertype;
		this.type=type;
		this.SecondKeyword=SecondKeyword;
		this.Optimize=Optimize;
	}
	public Parameter (String keyword, String value,String SecondKeyword,ParameterType Parametertype , ValueType type , String [] SetOfOptionsType,boolean Optimize) {
		this( keyword,  value ,SecondKeyword, Parametertype ,  type,Optimize );
		
		this.SetOfOptionsType=SetOfOptionsType;
	}
	public Parameter (String keyword, String value,String SecondKeyword,ParameterType Parametertype , ValueType type,int LowerLimitforNumberType ,int UpperLimitforNumberType,boolean Optimize) {
		this( keyword,  value ,SecondKeyword, Parametertype ,  type,Optimize );
		this.LowerLimitforNumberType=LowerLimitforNumberType;
		this.UpperLimitforNumberType=UpperLimitforNumberType;
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
		if(SecondKeyword!=null)
		return SecondKeyword+" "+Value;
		return Value;
	}
	public String GetValueofSecondKeyword() {
		if(SecondKeyword!=null)
		return Value;
		
		return null;
	}
	public void SetValue(String v) {
		 Value=v;
	}
	public ValueType GetValueType() {
		return type;
	}
	public void SetValueType(ValueType type) {
		this.type=type;
	}
	public ParameterType GetParameterType() {
		return Parametertype;
	}
	public int GetLowerBoundParameterType() {
		if(GetParameterType()==ParameterType.Compulsory)
			return 1;
		return 0;
		
	}
	public int GetUpperBoundParameterType() {
		return 1; // always one
	}
	public int  GetLowerLimitforNumberType() {
		return LowerLimitforNumberType;
	}
	public int  GetUpperLimitforNumberType() {
		return UpperLimitforNumberType;
	}
	public String [] GetSetOfOptionsType() {
		return SetOfOptionsType ;
	}
	public List<String> ToList() {
		List<String> ParameterInArray = new ArrayList<String>();
		
		if(GetKeyword().length()!=0) {
			
			ParameterInArray.add(GetKeyword());
			if(GetValue().length()!=0)
			ParameterInArray.add(GetValue());
		}
		else {
			//System.out.println(toString());
			if(GetValue().length()!=0)
			ParameterInArray.add(GetValue());
		}
		return ParameterInArray;
	}
	public int LowerBound() {
		if(GetValueType()==ValueType.Number) {
			return GetLowerLimitforNumberType();
		}
		else {
			return 0;
		}
		/*
		if(GetParameterType()==ParameterType.Compulsory) {
			if(GetValueType()==ValueType.Number && GetLowerLimitforNumberType() > 0)
				return GetLowerLimitforNumberType();
			
			//if(GetValueType()==ValueType.SetOfOptions)
			//return 0; // first element in an array has index 0
			
			return 1; // must exist in the solution 
		}
		else {// then its optional 
			if(GetValueType()==ValueType.Number)
				return GetLowerLimitforNumberType();
			
			return 0; // not necessary to be in the  solution
		}
		*/
	}
	
	public int  UpperBound() {
		if(GetValueType()==ValueType.Number) {
			return GetUpperLimitforNumberType();
		}
		if(GetValueType()==ValueType.File) {
			return 1;
		}
		if(GetValueType()==ValueType.String) {
			return 1;
		}
		if(GetValueType()==ValueType.SetOfOptions) {
			return GetSetOfOptionsType().length-1; // last element in an array is len -1
			
		}
		return 0;
		
	}
	
	@Override
	public String toString() {
		return "Parameter [Keyword=" + Keyword + ", Value=" + Value + ", SecondKeyword=" + SecondKeyword + ", type="
				+ type + ", Parametertype=" + Parametertype + ", LowerLimitforNumberType=" + LowerLimitforNumberType
				+ ", UpperLimitforNumberType=" + UpperLimitforNumberType + ", SetOfOptionsType="
				+ Arrays.toString(SetOfOptionsType) + ", Used=" + Used + ", LowerBound()=" + LowerBound()
				+ ", UpperBound()=" + UpperBound() + "]";
	}
	
	public void SetValueBasedOnOptimizationAlgorithm(int EncodingUsed, int Value) {
		
		if((GetValueType()==ValueType.File ||GetValueType()==ValueType.String)  && EncodingUsed==0) {
			Used=false;
		}
		if((GetValueType()==ValueType.File ||GetValueType()==ValueType.String) && EncodingUsed!=0) {
			Used=true;
		}
		if(GetValueType()==ValueType.Number)
		{
			if(EncodingUsed==0) {
			Used=false;
			SetValue(String.valueOf(Value));
			}
			else {
				Used=true;
				SetValue(String.valueOf(Value));
			}
		}
			
		//if(GetValueType()==ValueType.SetOfOptions && GetParameterType()==ParameterType.Compulsory)
		//{
			//Used=true;
			//SetValue(GetSetOfOptionsType()[EncodingValue-1]);
		//}
		if(GetValueType()==ValueType.SetOfOptions)
		{
			if(EncodingUsed==0) {
			Used=false;
			
			}
			else {
				Used=true;
				
				SetValue(GetSetOfOptionsType()[Value]);
			}
		}
		
	}
	
}
