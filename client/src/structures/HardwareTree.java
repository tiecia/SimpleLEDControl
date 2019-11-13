package structures;

import javax.swing.JPanel;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.tree.*;
import client.Arduino;
import client.LEDStrip;

public class HardwareTree extends JPanel {
	
	private DefaultMutableTreeNode rootNode;
	private JTree tree;
	
	public HardwareTree(ArrayList<Arduino> arduinos, String rootName) {
		super();
		rootNode = new DefaultMutableTreeNode(rootName); //Root Node
		for(Arduino current : arduinos) {
			DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(current); //Create a new node for each arduino					
			for(LEDStrip strip : current.getStrips()) {
				deviceNode.add(new DefaultMutableTreeNode(strip)); //Create a new node for each strip in each arduino
			}
			rootNode.add(deviceNode);
		}
		tree = new JTree(rootNode);
		super.setLayout(new BorderLayout());
		super.add(tree);
	}
	
	public void add(Arduino newArduino) {
		
	}
}
