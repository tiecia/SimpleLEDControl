package addDeviceDialog;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import client.LEDStrip;

import javax.swing.AbstractAction;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;

public class StripNode extends JPanel {
	private static final long serialVersionUID = 1L;
	
	//Static Fields
	private static int totalStrips = 1;
	
	//Components
	private JTextField nameTextField;
	private JFormattedTextField pinTextField;
	private JFormattedTextField numTextField;
	
	//Attributes
	private final int thisStripNum = totalStrips;
	private LEDStrip strip;
	
	public StripNode() {
		initGUI();
	}
	
	public StripNode(LEDStrip strip) {
		initGUI();
		nameTextField.setText(strip.getName());
		numTextField.setText(strip.getNumOfLEDs() + "");
		pinTextField.setText(strip.getPin() + "");
		this.strip = strip;
		setBorder(new TitledBorder(null, nameTextField.getText(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
	}
	
	public void initGUI() {
		setLayout(new MigLayout("", "[100px,center][grow,center]", "[25:n][25:n][25:n]"));
		
		//Name Field Label
		JLabel lblName = new JLabel("Name");
		add(lblName, "cell 0 0,alignx center,aligny center");
		
		//Name Field
		nameTextField = new JTextField();
		nameTextField.setText("LED Strip #" + thisStripNum);
		nameTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased (KeyEvent e) { //Updated the border label
				if(!nameTextField.getText().equals("")) {
					setBorder(new TitledBorder(null, nameTextField.getText(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
					
				} else {
					setBorder(new TitledBorder(null, " ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				}
				
			}
		});
		add(nameTextField, "cell 1 0,growx,aligny center");
		nameTextField.setColumns(10);
		
		//Pin Field Label
		JLabel lblPin = new JLabel("PIN");
		add(lblPin, "cell 0 1,alignx center,aligny center");
		
		//Pin Field
		pinTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		add(pinTextField, "cell 1 1,growx,aligny center");
		pinTextField.setColumns(10);
		
		//Number of LEDs Field Label
		JLabel lblNumberOfLeds = new JLabel("Number of LEDs");
		add(lblNumberOfLeds, "cell 0 2,alignx center,growy");
		
		//Number of LEDs Field
		numTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		add(numTextField, "cell 1 2,growx,aligny center");
		numTextField.setColumns(10);
		
		setBorder(new TitledBorder(null, toString(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		totalStrips++;
	}
	
	public LEDStrip getStrip() {
		if(strip == null) {
			return new LEDStrip(nameTextField.getText(), Integer.parseInt(numTextField.getText()), Integer.parseInt(pinTextField.getText()));
		}
		strip.setName(nameTextField.getText());
		strip.setPin(Integer.parseInt(pinTextField.getText()));
		strip.setNumOfLEDs(Integer.parseInt(numTextField.getText()));
		return strip;
	}
	
	public String toString() {
		return nameTextField.getText();
	}
	
	public boolean isMissingInformation() {
		if(nameTextField.getText().equals("") || pinTextField.getText().equals("") || numTextField.getText().equals("")) {
			return true;
		}
		return false;
	}
}
