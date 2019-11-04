package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Choice;
import net.miginfocom.swing.MigLayout;
import sun.reflect.generics.tree.Tree;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JEditorPane;

public class HardwareConfigurator extends JDialog {


	public final JPanel panel = new JPanel();
	
	private ArrayList<Arduino> arduinos = new ArrayList<Arduino>();
	
	private JTree tree;
	private DefaultMutableTreeNode rootTreeNode;
	private JEditorPane htmlArea;
	

	
	public HardwareConfigurator() {
		super();
		GuiInit();
	}
	
	public void GuiInit() {
		/*
		 * 
		 * ContentPane
		 * 
		 */
		setModal(true);
		panel.setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
			JPanel bodyPanel = new JPanel();
			bodyPanel.setBackground(Color.WHITE);
			bodyPanel.setLayout(new GridLayout(0, 2, 0, 0));
			
			rootTreeNode = new DefaultMutableTreeNode("Devices"); //Create Root Tree Node (Always There)

			tree = new JTree(rootTreeNode); //Create tree 
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent arg0) {
					updateHTML();
				}
			});
	        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	        
	        
			JScrollPane treeScrollPane = new JScrollPane(tree); //Create tree scroll pane and add tree
			bodyPanel.add(treeScrollPane);
			
			getContentPane().add(bodyPanel);
			
			JScrollPane htmlScrollPane = new JScrollPane(); //Create HTML scroll pane
			bodyPanel.add(htmlScrollPane);
			
			htmlArea = new JEditorPane(); //create HTML text area
			htmlScrollPane.setViewportView(htmlArea); //Add HTML text area to scroll pane
			
			//Confirm Panel (Bottom Button Panel)
			JPanel confirmPanel = new JPanel();
			getContentPane().add(confirmPanel, BorderLayout.SOUTH);
			confirmPanel.setLayout(new MigLayout("", "[141.00px][grow,center]", "[23px]"));
			
			
			JButton btnOK = new JButton("OK");
			confirmPanel.add(btnOK, "cell 0 0,alignx left,aligny center");
			//AddArduino Button
			JButton btnAddDevice = new JButton("Add Device");
			confirmPanel.add(btnAddDevice, "flowx,cell 1 0,alignx right,aligny center");
			btnAddDevice.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createArduino();
				}
			});
			
			JButton btnAddLedStrip = new JButton("Add LED Strip");
			btnAddLedStrip.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					createLEDStrip();
				}
			});
			confirmPanel.add(btnAddLedStrip, "cell 1 0,alignx right,aligny center");
			//End Confirm Panel
	}
	
	private void createArduino() {
		Arduino newArduino = new Arduino();
		if(newArduino.isOpen()) {
			arduinos.add(newArduino);
			addDeviceToTree(newArduino);
		}
//		AddArduinoDialog add = new AddArduinoDialog();
//		add.addWindowListener(new WindowAdapter(){
//			public void windowClosed(WindowEvent e) {
//				System.out.println("Closed");
//			}
//			
//		});
//		add.setVisible(true);
//		
//		if(!add.isCanceled()) { //If dialog box is not canceled or closed (Normal operation)
//			Arduino newArduino = new Arduino(add.getName(), add.getPort());
//			if(newArduino.isOpen()) { //Checks if Arduino object has properly connected to the physical arduino
//				arduinos.add(newArduino);
//				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newArduino);
//				rootTreeNode.add(newNode);
//				tree.updateUI();
//				tree.expandRow(0);
//			} else { //If failed to connect
//				JOptionPane.showMessageDialog(panel, "Failed to connect to Arduino \"" + newArduino.getName() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
//			}
//		}
	}
	
	private void addDeviceToTree(Arduino device) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(device);
		for(LEDStrip currentStrip : device.getStrips()) {
			newNode.add(new DefaultMutableTreeNode(currentStrip));
		}
		rootTreeNode.add(newNode);
		tree.updateUI();
		tree.expandRow(0);
		tree.expandRow(1);
	}
	
	private void createLEDStrip() {
		AddStripDialog add = new AddStripDialog(arduinos);
		add.setVisible(true);
		
		if(!add.isCanceled()) {
			Arduino parentDevice = add.getArduino();
			String newName = add.getName();
			int newPIN = add.getPIN();
			int newNumOfLEDs = add.getNUM();
			LEDStrip newStrip = new LEDStrip(newName, newNumOfLEDs, newPIN);
			parentDevice.addStrip(newStrip);
			
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newStrip);
			rootTreeNode.add(newNode);
			tree.updateUI();
//			tree.expandRow(0);
		}
	}
	
	private void updateHTML() {
		Object selected = tree.getLastSelectedPathComponent();
		
	}
	
	public ArrayList<Arduino> getArduinos() {
		return arduinos;
	}
}
