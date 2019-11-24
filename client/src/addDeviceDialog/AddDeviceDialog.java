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
import javax.swing.ListModel;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
	
	//Actions
	private final RemoveStripAction removeStripAction = new RemoveStripAction();
	private final AddStripAction addStripAction = new AddStripAction();
	private final StripListChanged stripListChangedAction = new StripListChanged();
	
	//Components
	private JTextField deviceNameField;
	private JComboBox<String> portComboBox;
	private JComboBox<String> deviceComboBox;
	private DefaultListModel<StripNode> stripNodes;
	
	//Attributes
	private boolean canceled;
	private LEDPort port;
	private int maxWindowHeight;
	private JScrollPane scrollPane;
	private JPanel contentPanel;
	private JPanel stripNodeInfoPanel;
	private JList<StripNode> stripJList;
	private StripNode currentStripNode;
	

	
	

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
	public AddDeviceDialog() {
		setResizable(true);
		maxWindowHeight = (int) (getBounds().getX()-300);
		stripNodes = new DefaultListModel<>();
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
		
		//Device Type Label
		JLabel lblDeviceType = new JLabel("Device Type");
		devicePanel.add(lblDeviceType, "cell 0 1,alignx center,aligny center");
		
		//Device Select Dropdown Box
		deviceComboBox = new JComboBox<String>();
		devicePanel.add(deviceComboBox, "cell 1 1,growx");
		deviceComboBox.addItem("Arduino Uno/Nano");
		
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
		stripNodes.addElement(new StripNode());
		stripJList = new JList<StripNode>(stripNodes);
		stripJList.addListSelectionListener(stripListChangedAction);
		stripJList.setSelectedIndex(0);
		listScrollPane.setViewportView(stripJList);
		
		JButton addStripButton = new JButton("Add Strip");
		addStripButton.addActionListener(addStripAction);
		contentPanel.add(addStripButton, "flowx,cell 1 2,growx,aligny center");
		
		JButton removeStripButton = new JButton("Remove Strip");
		removeStripButton.addActionListener(removeStripAction);
		contentPanel.add(removeStripButton, "cell 1 2,growx");
		
		stripNodeInfoPanel = new JPanel();
		stripNodeInfoPanel.setBackground(Color.RED);
//		contentPanel.add(stripNodeInfoPanel, "cell 2 1,grow");
			
		//BUTTON PANE (OK and Cancel Buttons)
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		//Ok Button
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		//Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
			
		
		
	}
	
	private void ok() {
		port = new LEDPort((String) portComboBox.getSelectedItem());
		if(port.isOpen()) {
			canceled = false;
			setVisible(false);
		} else {
			JOptionPane.showMessageDialog(getContentPane(), "Failed to connect to device on port \"" + portComboBox.getSelectedItem() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void cancel() {
		canceled = true;
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
	
	private class StripListChanged implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			System.out.println(e.getSource());
			StripNode selectedNode = (StripNode) ((JList<StripNode>)e.getSource()).getSelectedValue();
			System.out.println(selectedNode);
			if(currentStripNode != null) {
				contentPanel.remove(currentStripNode);
			}
			contentPanel.add(selectedNode, "cell 2 1,growx, aligny top");
			currentStripNode = selectedNode;
		}
	}
	
	private class AddStripAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			StripNode newStripNode = new StripNode();
			stripNodes.addElement(newStripNode);
			stripJList.setSelectedIndex(stripJList.getModel().getSize()-1);
			System.out.println(stripNodes);

		}
	}
	
	private class RemoveStripAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			StripNode toRemove = stripJList.getSelectedValue();
			DefaultListModel<StripNode> listModel = (DefaultListModel<StripNode>) stripJList.getModel();
			if(listModel.getElementAt(listModel.size()-1) != toRemove) {
				stripJList.setSelectedIndex(stripJList.getModel().getSize()-1);
			} else {
				stripJList.setSelectedIndex(stripJList.getModel().getSize()-2);
			}
			listModel.removeElement(toRemove);
			System.out.println(toRemove);
			System.out.println(stripNodes);
		}
	}
	
//	private class StripNode extends JPanel {
//		private JTextField nameTextField;
//		private JTextField pinTextField;
//		private JTextField numTextField;
//		
//		private String[] data;
//		private JButton btnRemove;
//
//		/**
//		 * Create the panel.
//		 */
//		public StripNode(boolean first) {
//			setBorder(new TitledBorder(null, "LED Strip", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//			setLayout(new MigLayout("", "[100px,center][grow,center]", "[grow][grow][grow][]"));
//			
//			JLabel lblName = new JLabel("Name");
//			add(lblName, "cell 0 0,alignx center,aligny center");
//			
//			nameTextField = new JTextField();
//			add(nameTextField, "cell 1 0,growx,aligny center");
//			nameTextField.setColumns(10);
//			
//			JLabel lblPin = new JLabel("PIN");
//			add(lblPin, "cell 0 1,alignx center,aligny center");
//			
//			pinTextField = new JTextField();
//			add(pinTextField, "cell 1 1,growx,aligny center");
//			pinTextField.setColumns(10);
//			
//			JLabel lblNumberOfLeds = new JLabel("Number of LEDs");
//			add(lblNumberOfLeds, "cell 0 2,alignx center,growy");
//			
//			numTextField = new JTextField();
//			add(numTextField, "cell 1 2,growx,aligny center");
//			numTextField.setColumns(10);
//			if(!first) {
//				btnRemove = new JButton("Remove");
//				btnRemove.addActionListener(removeAction);
//				add(btnRemove, "cell 1 3,alignx right");
//			}
//		}
//		
//		public LEDStrip getStrip() {
//			return new LEDStrip(nameTextField.getText(), Integer.parseInt(numTextField.getText()), Integer.parseInt(pinTextField.getText()));
//		}
//	}
}
