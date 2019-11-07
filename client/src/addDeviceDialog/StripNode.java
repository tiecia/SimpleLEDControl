package addDeviceDialog;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import client.LEDStrip;

import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StripNode extends JPanel {
	private JTextField nameTextField;
	private JTextField pinTextField;
	private JTextField numTextField;
	
	private String[] data;

	/**
	 * Create the panel.
	 */
	public StripNode() {
		setBorder(new TitledBorder(null, "LED Strip", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new MigLayout("", "[100px,center][grow,center]", "[grow][grow][grow]"));
		
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
	}
	
	public LEDStrip getStrip() {
		return new LEDStrip(nameTextField.getText(), Integer.parseInt(numTextField.getText()), Integer.parseInt(pinTextField.getText()));
	}
}
