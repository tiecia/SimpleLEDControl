package client;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.JColorChooser;


@SuppressWarnings("serial")
public class Zone extends JInternalFrame {
	
	//Attributes
	private ArrayList<JButton> customColors = new ArrayList<JButton>();
	private int numOfButtons = 0;
	private String name;
	
	private Set<LEDPort> ports;
	private ArrayList<LEDStrip> strips;
	private JColorChooser colorChooser;
	
	private ColorChangeListener colorChangeListener = new ColorChangeListener();
	
	public Zone(String name, ArrayList<LEDStrip> strips) {
		super(name);
		this.name = name;
		this.strips = strips;
		this.ports = new HashSet<LEDPort>();
		for(LEDStrip strip : this.strips) {
			ports.add(strip.getParentArduino().getPort());
		}
		GuiInit();
	}
	
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		this.setTitle(name);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<LEDStrip> getStrips(){
		return strips;
	}
	
	public void sendData() {
		for(LEDStrip strip : this.strips) {
			LEDPort currentPort = strip.getParentArduino().getPort();
			currentPort.sendData(strip.getPin(), sliderBrightness.get, sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
		}
	}
	
	/**
	 * Create the frame.
	 * @return 
	 */
	
	private void GuiInit() {		
		setBounds(100, 100, 992, 606);
		
		JPanel customColorBox = new JPanel();
		getContentPane().setLayout(new MigLayout("", "[30][276px,center][:30:30][506px,grow][30]", "[15][17px][162px,grow][19px]"));
		
		colorChooser = new JColorChooser();
		colorChooser.setPreviewPanel(new JPanel());
		colorChooser.getSelectionModel().addChangeListener(colorChangeListener);
		AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
		colorChooser.removeChooserPanel(panels[1]);
		try {
			removeTransparencySlider(colorChooser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getContentPane().add(colorChooser, "cell 3 2,grow");
		
		customColorBox.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(customColorBox, "cell 1 2,grow");
		customColorBox.setLayout(new GridLayout(5, 4, 0, 0));
		
		JLabel lblCustomColors = new JLabel("Custom Colors");
		getContentPane().add(lblCustomColors, "cell 1 1,growx,aligny top");
		lblCustomColors.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomColors.setFont(new Font("Tahoma", Font.PLAIN, 14));
	}
	
	private class ColorChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			for(LEDStrip strip : strips) {
				LEDPort currentPort = strip.getParentArduino().getPort();
				currentPort.sendData(strip.getPin(), 80, colorChooser.getColor().getRed(), colorChooser.getColor().getGreen(), colorChooser.getColor().getBlue());
			}
		}
	}
	
	private static void removeTransparencySlider(JColorChooser jc) throws Exception {

	    AbstractColorChooserPanel[] colorPanels = jc.getChooserPanels();
	    for (int i = 1; i < colorPanels.length; i++) {
	        AbstractColorChooserPanel cp = colorPanels[i];

	        Field f = cp.getClass().getDeclaredField("panel");
	        f.setAccessible(true);

	        Object colorPanel = f.get(cp);
	        Field f2 = colorPanel.getClass().getDeclaredField("spinners");
	        f2.setAccessible(true);
	        Object spinners = f2.get(colorPanel);

	        Object transpSlispinner = Array.get(spinners, 3);
	        if (i == colorPanels.length - 1) {
	            transpSlispinner = Array.get(spinners, 4);
	        }
	        Field f3 = transpSlispinner.getClass().getDeclaredField("slider");
	        f3.setAccessible(true);
	        JSlider slider = (JSlider) f3.get(transpSlispinner);
	        slider.setEnabled(false);
	        Field f4 = transpSlispinner.getClass().getDeclaredField("spinner");
	        f4.setAccessible(true);
	        JSpinner spinner = (JSpinner) f4.get(transpSlispinner);
	        spinner.setEnabled(false);
	    }
	}
}