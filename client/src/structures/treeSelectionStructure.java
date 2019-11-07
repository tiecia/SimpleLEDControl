package structures;

import javax.swing.JTree;
import java.util.ArrayList;
import javax.swing.tree.*;
import client.Arduino;
import client.LEDStrip;

public class treeSelectionStructure extends JTree {
	
	private DefaultMutableTreeNode rootNode;
	
	public treeSelectionStructure(ArrayList<Arduino> arduinos, String rootName) {
		rootNode = new DefaultMutableTreeNode(rootName); //Root Node
		for(Arduino current : arduinos) {
			DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(current); //Create a new node for each arduino					
			for(LEDStrip strip : current.getStrips()) {
				deviceNode.add(new DefaultMutableTreeNode(strip)); //Create a new node for each strip in each arduino
			}
			rootNode.add(deviceNode);
		}
	}
	
	public void add(Arduino newArduino) {
		
	}
}
