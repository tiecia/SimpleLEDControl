package structures;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
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
		tree.setToggleClickCount(0);
		super.setLayout(new BorderLayout());
		super.add(tree);
	}
	
	public void add(Arduino newArduino) {
		
	}
	
	public TreePath[] getSelectionPaths() {
		return tree.getSelectionPaths();
	}
	
	public ArrayList<LEDStrip> getSelectedStrips() {
		ArrayList<LEDStrip> selectedStrips = new ArrayList<>();
		for(TreePath path : selectedNodes) {
			DefaultMutableTreeNode lastPathObject = (DefaultMutableTreeNode) path.getLastPathComponent();
			selectedStrips.add((LEDStrip) lastPathObject.getUserObject());
		}
		System.out.println("Selected Strips in HardwareTree" + selectedStrips);
		return selectedStrips;
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if(!selectedNode.isLeaf() && !(selectedNode == rootNode)) { //If clicked node is an arduino node
			TreeNode[] selectedNodePathArray = {rootNode, selectedNode};
			TreePath selectedNodePath = new TreePath(selectedNodePathArray);
			
			if(isAllLeafNodesSelected(selectedNode)) { //If all leaf nodes are selected deselect all leaf nodes
				for(int i = 0; i<selectedNode.getChildCount(); i++) {
					TreeNode[] path = {rootNode, selectedNode, selectedNode.getChildAt(i)};
					TreePath newSelectionPath = new TreePath(path);
					selectedNodes.remove(newSelectionPath);
				}
			} else { //If not all leaf nodes are selected select all leaf nodes
				for(int i = 0; i<selectedNode.getChildCount(); i++) {
					TreeNode[] path = {rootNode, selectedNode, selectedNode.getChildAt(i)};
					TreePath newSelectionPath = new TreePath(path);
					if(!selectedNodes.contains(newSelectionPath)) {
						selectedNodes.add(newSelectionPath);
					}
				}
			}

			selectNodes();
		} else { //If the clicked node is a leaf node
			TreeNode[] selectedNodePathArray = {rootNode, selectedNode.getParent(), selectedNode};
			TreePath selectedNodePath = new TreePath(selectedNodePathArray);
			System.out.println(selectedNode);
			if(selectedNodes.contains(selectedNodePath)) {
				selectedNodes.remove(selectedNodePath);
			} else {
				selectedNodes.add(selectedNodePath);
			}
			selectNodes();
		}
	}
	
	private boolean isAllLeafNodesSelected(DefaultMutableTreeNode selectedNode) {
		for(int i = 0; i<selectedNode.getChildCount(); i++) { //Goes through the leaf nodes of selected parent node to see if they are all selected
			TreeNode[] path = {rootNode, selectedNode, selectedNode.getChildAt(i)};
			TreePath newSelectionPath = new TreePath(path);
			if(!selectedNodes.contains(newSelectionPath)) {
				return false;
			}
		}
		return true;
	}
	
	private void selectNodes() {
		tree.removeTreeSelectionListener(this);
		System.out.println(selectedNodes);
		TreePath[] childPathsArray = new TreePath[selectedNodes.size()];
		selectedNodes.toArray(childPathsArray);
		System.out.println(Arrays.toString(childPathsArray));
		tree.setSelectionPaths(childPathsArray);
		tree.addTreeSelectionListener(this);
	}
}
