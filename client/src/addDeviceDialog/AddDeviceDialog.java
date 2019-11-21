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

public class AddDeviceDialog extends JDialog {
	private JTextField deviceNameField;
	
	private boolean canceled;
	
	private final Action addStripAction = new SwingAction();
	private JPanel stripPanel;
	private LEDPort port;
	
	private ArrayList<StripNode> stripNodes;
	private JComboBox<String> portComboBox;
	private JComboBox<String> deviceComboBox;

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
		stripNodes = new ArrayList<>();
		setModal(true);
		setBounds(100, 100, 500, 360);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel contentPanel = new JPanel();
			JScrollPane scrollPane = new JScrollPane(contentPanel);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			contentPanel.setLayout(new MigLayout("", "[434px,grow]", "[57.00px][74px][23px,grow]"));
			{
				JPanel devicePanel = new JPanel();
				devicePanel.setBorder(new TitledBorder(null, "Device Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				contentPanel.add(devicePanel, "cell 0 0,growx,aligny top");
				devicePanel.setLayout(new MigLayout("", "[100,left][96px,grow,left]", "[][][][20px][20px]"));
				{
					JLabel lblDeviceType = new JLabel("Device Type");
					devicePanel.add(lblDeviceType, "cell 0 1,alignx center,aligny center");
				}
				{
					deviceComboBox = new JComboBox<String>();
					devicePanel.add(deviceComboBox, "cell 1 1,growx");
					deviceComboBox.addItem("Arduino Uno/Nano");
				}
				{
					JLabel deviceCOMLabel = new JLabel("COM Port");
					deviceCOMLabel.setHorizontalAlignment(SwingConstants.CENTER);
					devicePanel.add(deviceCOMLabel, "cell 0 3,alignx center,aligny center");
				}
				{
					portComboBox = new JComboBox<String>();
					for(SerialPort port : SerialPort.getCommPorts()) {
						System.out.println(port.getDescriptivePortName());
						portComboBox.addItem(port.getSystemPortName());
					}
					devicePanel.add(portComboBox, "cell 1 3,growx");
					
				}
				{
					JLabel deviceNameLabel = new JLabel("Name");
					deviceNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
					devicePanel.add(deviceNameLabel, "cell 0 4,alignx center,aligny center");
				}
				{
					deviceNameField = new JTextField();
					deviceNameField.setColumns(10);
					devicePanel.add(deviceNameField, "cell 1 4,growx,aligny center");
				}
			}
			{
				stripPanel = new JPanel();
				stripPanel.setBorder(null);
				contentPanel.add(stripPanel, "cell 0 1,grow");
				stripPanel.setLayout(new MigLayout("", "[434px,grow]", "[74px]"));
				{
					StripNode initialStripNode = new StripNode();
					stripNodes.add(initialStripNode);
					stripPanel.add(initialStripNode, "cell 0 0,grow");
				}
			}
			{
				JButton btnAddStrip = new JButton("Add Strip");
				btnAddStrip.setAction(addStripAction);
				contentPanel.add(btnAddStrip, "cell 0 2,alignx center,aligny bottom");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ok();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	private void addLEDStrip() {
		StripNode newStripNode = new StripNode();
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
	

	private class SwingAction extends AbstractAction {

		public SwingAction() {
			putValue(NAME, "Add Strip");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			addLEDStrip();
		}
	}
}
