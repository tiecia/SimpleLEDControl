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
	
	private ArrayList<TreePath> selectedNodes;
	
	public HardwareTree(ArrayList<Arduino> arduinos, String rootName) {
		super();
		
		selectedNodes = new ArrayList<TreePath>();
		
		rootNode = new DefaultMutableTreeNode(rootName); //Root Node
		for(Arduino current : arduinos) {
			DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(current); //Create a new node for each arduino					
			for(LEDStrip strip : current.getStrips()) {
				deviceNode.add(new DefaultMutableTreeNode(strip)); //Create a new node for each strip in each arduino
			}
			rootNode.add(deviceNode);
		}
		tree = new JTree(rootNode);
//		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.setSelectionModel(new HardwareTreeSelectionModel());
		tree.addTreeSelectionListener(this);
		tree.setRootVisible(false);
		super.setLayout(new BorderLayout());
		super.add(tree);
	}
	
	public void add(Arduino newArduino) {
		
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		TreeNode[] selectedNodePathArray = {rootNode, selectedNode};
		TreePath selectedNodePath = new TreePath(selectedNodePathArray);
		if(!selectedNode.isLeaf() && !(selectedNode == rootNode)) {
			for(int i = 0; i<selectedNode.getChildCount(); i++) {
				TreeNode[] path = {rootNode, selectedNode, selectedNode.getChildAt(i)};
				TreePath newSelectionPath = new TreePath(path);
				if(!selectedNodes.contains(newSelectionPath)) {
					selectedNodes.add(newSelectionPath);
				}
			}
			selectNodes(selectedNodes);
		} else {
			selectedNodes.remove(selectedNodePath);
		}
	}
	
	private void selectNodes(ArrayList<TreePath> nodes) {
		System.out.println(selectedNodes);
		TreePath[] childPathsArray = new TreePath[selectedNodes.size()];
		selectedNodes.toArray(childPathsArray);
		System.out.println(Arrays.toString(childPathsArray));
		tree.setSelectionPaths(childPathsArray);
	}
}
