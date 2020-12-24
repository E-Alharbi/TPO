package Optimizer.Parameter.Type;

import java.util.Vector;

public class OptionsSet {

	Vector<ItemGroup> options = new Vector<ItemGroup>();

	public OptionsSet(Vector<ItemGroup> options) {
		this.options = options;
	}

	public OptionsSet(OptionsSet os) {
		this.options = new Vector<ItemGroup>(os.options);
	}

	public OptionsSet() {

	}

	public void add(ItemGroup i) {
		options.add(i);
	}

	public int size() {
		return options.size();
	}

	public ItemGroup GetByIndex(int i) {
		return options.get(i);
	}

}
