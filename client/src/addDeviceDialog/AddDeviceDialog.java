package addDeviceDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.border.TitledBorder;

import com.fazecast.jSerialComm.SerialPort;

import client.*;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import java.awt.Dimension;

public class AddDeviceDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	//Components
	private JTextField deviceNameField;
	private JComboBox<String> portComboBox;
	private JComboBox<DeviceEntry> deviceComboBox;
	private DefaultListModel<StripNode> stripNodes = new DefaultListModel<>();
	
	//Attributes
	private boolean canceled = true;
	private LEDPort port;
	private JScrollPane scrollPane;
	private JPanel contentPanel;
	private JPanel currentStripNodePanel;
	private JList<StripNode> stripJList;
	private StripNode currentStripNode;
	
	//Actions
	private RemoveStripAction removeStripAction;
	private AddStripAction addStripAction;
	private StripListChanged stripListChangedAction;
	

	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddDeviceDialog dialog = new AddDeviceDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	
	//Constructor to populate with existing device
	public AddDeviceDialog(Arduino device) {
		initGUI();
		portComboBox.setSelectedItem(device.getPort());
		ComboBoxModel<String> model = portComboBox.getModel();
		for(int i = 0; i<model.getSize(); i++) {
			if(model.getElementAt(i) == device.getPort().getPortName()) {
				portComboBox.setSelectedIndex(i);
			}
		}
		deviceNameField.setText(device.getName());
		for(LEDStrip strip : device.getStrips()) {
			stripNodes.addElement(new StripNode(strip));
		}
		stripNodes.removeElementAt(0);
		addStripAction.checkState();
		removeStripAction.checkState();
	}
	
	//Constructor for normal blank creation
	public AddDeviceDialog() {
		initGUI();
	}
	
	public void initGUI() {
		setResizable(true);
		setModal(true);
		setBounds(100, 100, 600, 400);
		setMinimumSize(new Dimension(600,400));
		getContentPane().setLayout(new BorderLayout());
		

		
		//CONTENT PANEL
		contentPanel = new JPanel();
		scrollPane = new JScrollPane(contentPanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][200:200.00:200,grow][300:300,grow]", "[57.00px][grow][]"));
		
		//DEVICE PANEL
		JPanel devicePanel = new JPanel();
		devicePanel.setBorder(new TitledBorder(null, "Device Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(devicePanel, "cell 0 0 3 1,growx,aligny top");
		devicePanel.setLayout(new MigLayout("", "[100,left][96px,grow,left]", "[][][][20px][20px]"));
		
		//Device Select Dropdown Box
		deviceComboBox = new JComboBox<DeviceEntry>();
		devicePanel.add(deviceComboBox, "cell 1 1,growx");
		deviceComboBox.addItem(new DeviceEntry("Arduino Uno/Nano", Arduino.MAX_STRIPS));
		
		//INITIALIZE ACTIONS
		removeStripAction = new RemoveStripAction();
		addStripAction = new AddStripAction();
		stripListChangedAction = new StripListChanged();
		
		//Device Type Label
		JLabel lblDeviceType = new JLabel("Device Type");
		devicePanel.add(lblDeviceType, "cell 0 1,alignx center,aligny center");
		
		//COM Label
		JLabel deviceCOMLabel = new JLabel("COM Port");
		deviceCOMLabel.setHorizontalAlignment(SwingConstants.CENTER);
		devicePanel.add(deviceCOMLabel, "cell 0 3,alignx center,aligny center");
		
		//COM Select Dropdown Box
		portComboBox = new JComboBox<String>();
		for(SerialPort port : SerialPort.getCommPorts()) {
			System.out.println(port.getDescriptivePortName());
			portComboBox.addItem(port.getSystemPortName());
		}
		devicePanel.add(portComboBox, "cell 1 3,growx");
		
		//Device Name Label
		JLabel deviceNameLabel = new JLabel("Name");
		deviceNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		devicePanel.add(deviceNameLabel, "cell 0 4,alignx center,aligny center");
		
		//Device Name Text Field
		deviceNameField = new JTextField();
		deviceNameField.setColumns(10);
		devicePanel.add(deviceNameField, "cell 1 4,growx,aligny center");
		
		//List ScrollPane
		JScrollPane listScrollPane = new JScrollPane();
		contentPanel.add(listScrollPane, "cell 1 1,grow");
		
		//Strip JList
		StripNode toAdd = new StripNode();
		stripNodes.addElement(toAdd);
		stripJList = new JList<StripNode>(stripNodes);
		stripJList.addListSelectionListener(stripListChangedAction);
		stripJList.setSelectedIndex(0);
		listScrollPane.setViewportView(stripJList);
		
		//Add Strip Button
		JButton addStripButton = new JButton("Add Strip");
		addStripButton.setAction(addStripAction);
		contentPanel.add(addStripButton, "flowx,cell 1 2,growx,aligny center");
		
		//Remove Strip Button
		JButton removeStripButton = new JButton("Remove Strip");
		removeStripButton.setAction(removeStripAction);
		contentPanel.add(removeStripButton, "cell 1 2,growx");
		
		//Current Strip Node Panel
		currentStripNodePanel = new JPanel();
		currentStripNodePanel.setBackground(Color.RED);
			
		//BUTTON PANE (OK and Cancel Buttons)
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		//Ok Button
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean hasAllInfo = true;
				for(int i = 0; i<stripNodes.size(); i++) {
					if(stripNodes.get(i).isMissingInformation()) {
						JOptionPane.showMessageDialog(getContentPane(), "Missing Required Information", "Error", JOptionPane.ERROR_MESSAGE);
						hasAllInfo = false;
						break;
					}
				}
				if(hasAllInfo) {
					port = new LEDPort((String) portComboBox.getSelectedItem());
					canceled = false;
					setVisible(false);
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		//Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public String getDeviceName() {
		return deviceNameField.getText();
	}
	
	public LEDPort getDevicePort() {
		return port;
	}
	
	public ArrayList<LEDStrip> getStrips() {
		ArrayList<LEDStrip> strips = new ArrayList<LEDStrip>();
		StripNode[] nodeArray = new StripNode[stripNodes.getSize()];
		for(int i = 0; i<stripNodes.size(); i++) {
			strips.add(stripNodes.get(i).getStrip());
		}
		return strips;
	}
	
	
	//ACTION CLASSES
	
	//Displays the current selected strip node
	private class StripListChanged implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			@SuppressWarnings("unchecked")
			StripNode selectedNode = (StripNode) ((JList<StripNode>)e.getSource()).getSelectedValue();
			if(currentStripNode != null) { 
				contentPanel.remove(currentStripNode);
			}
			if(selectedNode != null) { 
				contentPanel.add(selectedNode, "cell 2 1,growx, aligny top");
				currentStripNode = selectedNode;
			}
			repaint();
			revalidate();
		}
	}
	
	private class AddStripAction extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public AddStripAction() {
			super("Add Strip");
			if(((DeviceEntry)deviceComboBox.getSelectedItem()).maxStrips() == 1) { //Fix to make dynamic
				setEnabled(false);
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			StripNode newStripNode = new StripNode();
			stripNodes.addElement(newStripNode);
			stripJList.setSelectedIndex(stripNodes.getSize()-1);
			if(stripNodes.size() >= ((DeviceEntry)deviceComboBox.getSelectedItem()).maxStrips()) { //Fix to make dynamic
				setEnabled(false);
			}
			removeStripAction.setEnabled(true);
		}
		
		public void checkState() {
			if(stripNodes.size() >= ((DeviceEntry)deviceComboBox.getSelectedItem()).maxStrips()) {
				setEnabled(false);
			} else {
				setEnabled(true);
			}
		}
	}
	
	private class RemoveStripAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public RemoveStripAction() {
			super("Remove Strip");
			if(stripNodes.size() <= 1) {
				setEnabled(false);
			}
		}
		public void actionPerformed(ActionEvent e) {
			StripNode toRemove = stripJList.getSelectedValue();
			if(stripNodes.getElementAt(stripNodes.size()-1) != toRemove) {
				stripJList.setSelectedIndex(stripJList.getModel().getSize()-1);
			} else {
				stripJList.setSelectedIndex(stripJList.getModel().getSize()-2);
			}
			stripNodes.removeElement(toRemove);
			if(stripNodes.size() <= 1) {
				setEnabled(false);
			}
			if(stripNodes.size() < ((DeviceEntry)deviceComboBox.getSelectedItem()).maxStrips()) {
				addStripAction.setEnabled(true);
			}
		}
		
		public void checkState() {
			if(stripNodes.size() <= 1) {
				setEnabled(false);
			} else {
				setEnabled(true);
			}
		}
	}
	
	//ENTRIES FOR STRIP JLIST
	private class DeviceEntry {
		private String name;
		private int maxStrips;
		
		public DeviceEntry(String name, int maxStrips) {
			this.name = name;
			this.maxStrips = maxStrips;
		}
		
		public int maxStrips() {
			return this.maxStrips;
		}
		
		public String toString() {
			return name;
		}
	}
}