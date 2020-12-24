package Optimizer.Parameter.Type;

import java.util.Vector;

public class ItemGroup {

	
	Vector<String> values= new Vector<String>();
	public ItemGroup(Vector <String> val) {
		super();
		values=val;
	}
	public ItemGroup() {};
	public void add(String val) {
		values.add(val);
	}
	public int size() {
		return values.size();
	}
	public String GetByIndex(int i) {
		return values.get(i);
	}
	
}
