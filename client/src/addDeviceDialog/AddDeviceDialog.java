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
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.border.TitledBorder;

import com.fazecast.jSerialComm.SerialPort;

import client.*;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JList;

public class AddDeviceDialog extends JDialog {
	
	//Actions
	private final RemoveAction removeAction = new RemoveAction();
	private final AddStripAction addStripAction = new AddStripAction();
	
	//Components
	private JTextField deviceNameField;
	private JPanel stripPanel;
	private JComboBox<String> portComboBox;
	private JComboBox<String> deviceComboBox;
	private ArrayList<StripNode> stripNodes;
	
	//Attributes
	private boolean canceled;
	private LEDPort port;
	private int maxWindowHeight;
	private JScrollPane scrollPane;
	

	
	

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
		stripNodes = new ArrayList<>();
		setModal(true);
		setBounds(100, 100, 500, 372);
		getContentPane().setLayout(new BorderLayout());
		
		//CONTENT PANEL
		JPanel contentPanel = new JPanel();
		scrollPane = new JScrollPane(contentPanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[434px,grow]", "[57.00px][74px][grow]"));
		
		//DEVICE PANEL
		JPanel devicePanel = new JPanel();
		devicePanel.setBorder(new TitledBorder(null, "Device Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(devicePanel, "cell 0 0,growx,aligny top");
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
		
		
		//STRIP PANEL
		stripPanel = new JPanel();
		stripPanel.setBorder(null);
		contentPanel.add(stripPanel, "cell 0 1,grow");
		stripPanel.setLayout(new MigLayout("", "[434px,grow]", "[74px]"));
		
		//Add Initial Strip Node (Cannot Be Removed)
		stripNodes.add(new StripNode(true));
		stripPanel.add(stripNodes.get(stripNodes.size()-1), "cell 0 0,grow");
			
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
		
		//Add Strip Button (Located In Content Panel)
		JButton btnAddStrip = new JButton("Add Strip");
		buttonPane.add(btnAddStrip);
		btnAddStrip.addActionListener(addStripAction);
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
	
	private void addLEDStrip() {
		StripNode newStripNode = new StripNode(false);
		stripNodes.add(newStripNode);
		stripPanel.add(newStripNode, "cell 0 " + (stripNodes.size()-1) + ",grow");
		Rectangle bounds = getBounds();
		setBounds((int)bounds.getX(),(int)bounds.getY(),(int)bounds.getWidth(),(int)bounds.getHeight()+150);
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
		for(StripNode node: stripNodes) {
			strips.add(node.getStrip());
		}
		return strips;
	}
	
	private class AddStripAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			StripNode newStripNode = new StripNode(false);
			stripNodes.add(newStripNode);
			stripPanel.add(newStripNode, "cell 0 " + (stripNodes.size()-1) + ",grow");
			Rectangle bounds = getBounds();
			setBounds((int)bounds.getX(),(int)bounds.getY(),(int)bounds.getWidth(),(int)bounds.getWidth());
		}
	}
	
	private class RemoveAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(((JButton)e.getSource()).getParent());
			StripNode toRemove = (StripNode) ((JButton)e.getSource()).getParent();
			stripPanel.remove(toRemove);
//			setBounds(scrollPane.getViewportBorderBounds());
			Rectangle bounds = getBounds();
			setBounds((int)bounds.getX(),(int)bounds.getY(),(int)bounds.getWidth(),(int)scrollPane.getViewportBorderBounds().getHeight());
			getContentPane().repaint(50L);
		}
	}
	
	private class StripNode extends JPanel {
		private JTextField nameTextField;
		private JTextField pinTextField;
		private JTextField numTextField;
		
		private String[] data;
		private JButton btnRemove;

		/**
		 * Create the panel.
		 */
		public StripNode(boolean first) {
			setBorder(new TitledBorder(null, "LED Strip", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			setLayout(new MigLayout("", "[100px,center][grow,center]", "[grow][grow][grow][]"));
			
			JLabel lblName = new JLabel("Name");
			add(lblName, "cell 0 0,alignx center,aligny center");
			
			nameTextField = new JTextField();
			add(nameTextField, "cell 1 0,growx,aligny center");
			nameTextField.setColumns(10);
			
			JLabel lblPin = new JLabel("PIN");
			add(lblPin, "cell 0 1,alignx center,aligny center");
			
			pinTextField = new JTextField();
			add(pinTextField, "cell 1 1,growx,aligny center");
			pinTextField.setColumns(10);
			
			JLabel lblNumberOfLeds = new JLabel("Number of LEDs");
			add(lblNumberOfLeds, "cell 0 2,alignx center,growy");
			
			numTextField = new JTextField();
			add(numTextField, "cell 1 2,growx,aligny center");
			numTextField.setColumns(10);
			if(!first) {
				btnRemove = new JButton("Remove");
				btnRemove.addActionListener(removeAction);
				add(btnRemove, "cell 1 3,alignx right");
			}
		}
		
		public LEDStrip getStrip() {
			return new LEDStrip(nameTextField.getText(), Integer.parseInt(numTextField.getText()), Integer.parseInt(pinTextField.getText()));
		}
	}
}
