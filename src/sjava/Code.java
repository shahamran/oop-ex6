package sjava;

import java.util.LinkedList;

public class Code {
	LinkedList<Code> items = new LinkedList<Code>();
	
	public Code() {
		
	}
	
	public <T extends Code> void addItem(T item) {
		items.add(item);
	}
	
	public LinkedList<Code> getArguments() {
		return items;
	}
	
}
