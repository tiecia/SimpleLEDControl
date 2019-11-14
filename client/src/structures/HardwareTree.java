package structures;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.tree.*;
import client.Arduino;
import client.LEDStrip;

public class HardwareTree extends JPanel implements TreeSelectionListener {
	
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
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		tree.setRootVisible(false);
		super.setLayout(new BorderLayout());
		super.add(tree);
	}
	
	public void add(Arduino newArduino) {
		
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		ArrayList<TreePath> pathsToSelect = new ArrayList<>();
		if(!selectedNode.isLeaf()) {
			if(!(selectedNode == rootNode)) {
				TreePath[] prevSelectedNodes = tree.getSelectionPaths();
				for(TreePath path : prevSelectedNodes) {
					pathsToSelect.add(path);
				}
				
				for(int i = 0; i<selectedNode.getChildCount(); i++) {
					TreeNode[] path = {rootNode, selectedNode, selectedNode.getChildAt(i)};
					pathsToSelect.add(new TreePath(path));
					System.out.println(pathsToSelect.get(pathsToSelect.size()-1));
				}
				TreePath[] childPathsArray = new TreePath[pathsToSelect.size()];

				pathsToSelect.toArray(childPathsArray);
				System.out.println(Arrays.toString(childPathsArray));
				tree.setSelectionPaths(childPathsArray);
			}
		}
	}
}
