package hardwareConfigurator;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import hardware.Arduino;
import hardware.LEDStrip;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import structures.DeviceComponent;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;

public class HardwareConfigurator extends JDialog {
	private static final long serialVersionUID = 1L;

	private ArrayList<Arduino> arduinos = new ArrayList<Arduino>();
	
	private JTree tree;
	private DefaultMutableTreeNode rootTreeNode;
	private JTextArea htmlArea;
	
	private RightClickMenu rightClickMenu = new RightClickMenu();
	

	
	public HardwareConfigurator() {
		super();
		setBackground(Color.LIGHT_GRAY);
		GuiInit();
	}
	
	public void GuiInit() {
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		setModal(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		JPanel bodyPanel = new JPanel();
		bodyPanel.setBackground(SystemColor.menu);
		
		rootTreeNode = new DefaultMutableTreeNode("Devices"); //Create Root Tree Node (Always There)

		tree = new JTree(rootTreeNode); //Create tree 
		tree.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				DefaultMutableTreeNode selectedNode = null;
				if(e.getButton() == MouseEvent.BUTTON3){
				    int selRow = tree.getRowForLocation(e.getX(), e.getY());
				    if(selRow != -1) { //Check for right click not on a node
					    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					    tree.setSelectionPath(selPath); 
					    if (selRow>-1){
					       tree.setSelectionRow(selRow); 
					    }
					    selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						if(selectedNode != rootTreeNode && !selectedNode.isLeaf()) {
							rightClickMenu.show(tree, e.getX(), e.getY());
						}
				    }
				}

			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath path = (TreePath) tree.getSelectionPath();
				DefaultMutableTreeNode selectedNode;
				if(path != null) {
					selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
					if(selectedNode == rootTreeNode) {
						htmlArea.setText("To add a device press the \"Add Device\" button. \r\n"
								+ "If a device is already added, click on it to see its attributes.");
					} else {
						DeviceComponent component = (DeviceComponent) selectedNode.getUserObject();
						htmlArea.setText(component.printInfo());
					}
				}
		}
		});
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		bodyPanel.setLayout(new MigLayout("", "[212px][212px,grow]", "[180px,grow]"));
        
        
		JScrollPane treeScrollPane = new JScrollPane(tree); //Create tree scroll pane and add tree
		bodyPanel.add(treeScrollPane, "cell 0 0,grow");
		
		getContentPane().add(bodyPanel);
		
		JScrollPane htmlScrollPane = new JScrollPane(); //Create HTML scroll pane
		bodyPanel.add(htmlScrollPane, "cell 1 0,grow");
		
		htmlArea = new JTextArea(); //create HTML text area
		htmlArea.setEditable(false);
		htmlArea.setToolTipText("Click on a device to see its attributes.");
		htmlScrollPane.setViewportView(htmlArea); //Add HTML text area to scroll pane
		
		//Confirm Panel (Bottom Button Panel)
		JPanel confirmPanel = new JPanel();
		getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.setLayout(new MigLayout("", "[141.00px][grow,center]", "[23px]"));
		
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		confirmPanel.add(btnOK, "cell 0 0,alignx left,aligny center");
		//AddArduino Button
		JButton btnAddDevice = new JButton("Add Device");
		confirmPanel.add(btnAddDevice, "flowx,cell 1 0,alignx right,aligny center");
		btnAddDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Arduino newArduino = new Arduino();
				if(newArduino.getPort() != null) {
					if(newArduino.isOpen()) {
						arduinos.add(newArduino);
						updateTree();
					} else {
						newArduino = null;
					}
				}
			}
		});
		updateTree();
		//End Confirm Panel
	}
	
	private void updateTree() {
		System.out.println(arduinos);
		rootTreeNode.removeAllChildren();
		for(Arduino ard : arduinos) {
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ard);
			for(LEDStrip currentStrip : ard.getStrips()) {
				newNode.add(new DefaultMutableTreeNode(currentStrip));
			}
			rootTreeNode.add(newNode);
			tree.updateUI();
			tree.expandRow(0);
			tree.expandRow(1);
		}
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload();
		tree.repaint();
		tree.setSelectionRow(0);
	}
	
	public ArrayList<Arduino> getArduinos() {
		return arduinos;
	}
	
	public void setDevices(ArrayList<Arduino> devices) {
		arduinos = devices;
	}
	
	public void setVisible(boolean b) {
		updateTree();
		super.setVisible(b);
	}
	
	public LEDStrip getCorrespondingStrip(String name, String portName, int numOfLEDs, int pin) {
		for(Arduino ard : arduinos) {
			if(ard.getPort().getPortName().equals(portName)) {
				for(LEDStrip strip : ard.getStrips()) {
					if(name.equals(strip.getName()) && pin == strip.getPin() && numOfLEDs == strip.getNumOfLEDs()) {
						return strip;
					}
				}
			}
		}
		return null;
	}
	
	public void closeAllDevices() {
		for(Arduino ard : arduinos) {
			ard.getPort().closePort();
		}
	}
	
	private class RightClickMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;

		public RightClickMenu() {
	    	JMenuItem editButton = new JMenuItem("Edit Device");
	    	editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
					((Arduino)selectedNode.getUserObject()).edit();
					updateTree();
				}	    		
	    	});
	    	
	    	JMenuItem removeButton = new JMenuItem("Remove Device");
	    	removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
					Arduino selectedDevice = ((Arduino)selectedNode.getUserObject());
					selectedDevice.getPort().closePort();
					arduinos.remove(selectedDevice);
					updateTree();
				}	    		
	    	});
	    	
	        add(editButton);
	        add(removeButton);
	    }
	}
}
