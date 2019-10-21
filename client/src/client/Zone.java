package client;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
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
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import java.awt.Font;


@SuppressWarnings("serial")
public class Zone extends JInternalFrame {
	
	//Attributes
	private ArrayList<JButton> customColors = new ArrayList<JButton>();
	private int numOfButtons = 0;
	private String zoneName;
	
	
	//GUI Components
	private JSlider sliderBrightness = new JSlider();
	private JSlider sliderRed = new JSlider();
	private JSlider sliderGreen = new JSlider();
	private JSlider sliderBlue = new JSlider();
	private JLabel portLabel = new JLabel();
	
	
	private ArrayList<Arduino> arduinos  = new ArrayList<Arduino>();
	
	public Zone(String name, ArrayList<Arduino> arduinos) {
		super(name);
		zoneName = name;
		this.arduinos = arduinos;
		GuiInit();
	}
	
	public String toString() {
		return zoneName;
	}
	
	public void setZoneName(String name) {
		zoneName = name;
		System.out.println("Set Zone Name:" + zoneName);
	}
	
	public String getName() {
		return zoneName;
	}
	
	public void sendDataToArduino() {
		for(int i = 0; i<arduinos.size(); i++) {
			arduinos.get(i).sendData(sliderBrightness.getValue(), sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
		}
	}
	
	private void setSliders(int bright, int red, int green, int blue) {
		sliderBrightness.setValue(bright);
		sliderRed.setValue(red);
		sliderGreen.setValue(green);
		sliderBlue.setValue(blue);
	}
	
	private void setSliders(int red, int green, int blue) {
		sliderRed.setValue(red);
		sliderGreen.setValue(green);
		sliderBlue.setValue(blue);
	}
	
	/**
	 * Create the frame.
	 * @return 
	 */
	
	private void GuiInit() {		
		setBounds(100, 100, 790, 606);
		getContentPane().setLayout(null);
		
		//Initialize TextField Objects
		JTextField textBrightness;
		JTextField textRed;
		JTextField textGreen;
		JTextField textBlue;
		
		//Initialize JPanel objects for the button panel
		JPanel buttonPanel = new JPanel();
		JPanel colorButtonPanel = new JPanel();
		JPanel offButtonPanel = new JPanel();
	
		
		//Buttons in the button panel
		JButton buttonRed = new JButton("Red");
		JButton buttonLightBlue = new JButton("Light Blue");
		buttonLightBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(0,255,255);
			}
		});
		JButton buttonGreen = new JButton("Green");
		buttonGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(0,255,0);
			}
		});
		JButton buttonPurple = new JButton("Purple");
		buttonPurple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(150,0,255);
			}
		});
		JButton buttonBlue = new JButton("Blue");
		buttonBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(0,0,255);
			}
		});
		JButton buttonPink = new JButton("Pink");
		buttonPink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(255,0,100);
			}
		});
		JButton buttonYellow = new JButton("Yellow");
		buttonYellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(255,255,0);
			}
		});
		JButton buttonOrange = new JButton("Orange");
		buttonOrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(255,100,0);
			}
		});
		JButton buttonWhite = new JButton("White");
		buttonWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(255,255,255);
			}
		});
		JButton buttonSpacer = new JButton("");
		
		JButton buttonOff = new JButton("OFF");
		buttonOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSliders(0,0,0);
			}
		});
		
		colorButtonPanel.setBounds(0, 0, 250, 200);
		buttonPanel.add(colorButtonPanel);
		colorButtonPanel.setLayout(new GridLayout(0, 2, 10, 10));
		
		buttonRed.setBackground(Color.RED);
		colorButtonPanel.add(buttonRed);
		buttonLightBlue.setBackground(new Color(100, 149, 237));
		colorButtonPanel.add(buttonLightBlue);
		buttonGreen.setBackground(Color.GREEN);
		colorButtonPanel.add(buttonGreen);
		
		
		buttonPurple.setForeground(Color.BLACK);
		buttonPurple.setBackground(new Color(138, 43, 226));
		colorButtonPanel.add(buttonPurple);
		
		
		buttonBlue.setForeground(Color.WHITE);
		buttonBlue.setBackground(Color.BLUE);
		colorButtonPanel.add(buttonBlue);
		

		buttonPink.setForeground(Color.BLACK);
		buttonPink.setBackground(Color.MAGENTA);
		colorButtonPanel.add(buttonPink);
		buttonYellow.setBackground(Color.YELLOW);
		colorButtonPanel.add(buttonYellow);
		
		
		buttonOrange.setForeground(Color.BLACK);
		buttonOrange.setBackground(Color.ORANGE);
		colorButtonPanel.add(buttonOrange);
		buttonWhite.setBackground(Color.WHITE);
		colorButtonPanel.add(buttonWhite);
		
		
		buttonSpacer.setEnabled(false);
		colorButtonPanel.add(buttonSpacer);
		
		
		offButtonPanel.setBounds(0, 210, 250, 50);
		buttonPanel.add(offButtonPanel);
		offButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		offButtonPanel.add(buttonOff);
		
		//Slider panel and labels
		JPanel sliderPanel = new JPanel();
		sliderPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JPanel brightnessSliderPanel = new JPanel();
		JLabel labelBrightness = new JLabel("Brightness");
		
		JPanel redSliderPanel = new JPanel();
		JLabel labelRed = new JLabel("Red");
		
		JPanel greenSliderPanel = new JPanel();
		JLabel labelGreen = new JLabel("Green");
		
		JPanel blueSliderPanel = new JPanel();
		JLabel labelBlue = new JLabel("Blue");
		
		JPanel customColorBox = new JPanel();

		//Configure and add buttons to button panel
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(26, 59, 250, 260);
		getContentPane().add(buttonPanel);
		
		sliderPanel.setBounds(325, 60, 419, 276);
		getContentPane().add(sliderPanel);
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		
		
		sliderPanel.add(brightnessSliderPanel);
		brightnessSliderPanel.setLayout(null);
		brightnessSliderPanel.setForeground(Color.BLACK);
		sliderBrightness.setValue(80);
		sliderBrightness.setToolTipText("Brightness");
		sliderBrightness.setMinimum(1);
		sliderBrightness.setBounds(100, 18, 200, 26);
		brightnessSliderPanel.add(sliderBrightness);
		
		
		labelBrightness.setBounds(10, 25, 78, 15);
		brightnessSliderPanel.add(labelBrightness);
		
		textBrightness = new JTextField(80);
		textBrightness.setText("80");
		textBrightness.setText("" + sliderBrightness.getValue());
		textBrightness.setBounds(330, 22, 50, 20);
		brightnessSliderPanel.add(textBrightness);
		
		
		redSliderPanel.setLayout(null);
		sliderPanel.add(redSliderPanel);
		sliderRed.setToolTipText("Red");
		sliderRed.setMaximum(255);
		sliderRed.setValue(0);
		sliderRed.setBounds(100, 18, 200, 26);
		redSliderPanel.add(sliderRed);
		
		
		labelRed.setBounds(10, 25, 71, 14);
		redSliderPanel.add(labelRed);
		
		textRed = new JTextField();
		textRed.setText("0");
		textRed.setColumns(10);
		textRed.setBounds(330, 22, 50, 20);
		redSliderPanel.add(textRed);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 0, 415, 14);
		redSliderPanel.add(separator_2);
		
		
		greenSliderPanel.setLayout(null);
		sliderPanel.add(greenSliderPanel);
		sliderGreen.setToolTipText("Green");
		sliderGreen.setMaximum(255);
		sliderGreen.setValue(0);
		sliderGreen.setBounds(100, 18, 200, 26);
		greenSliderPanel.add(sliderGreen);
		
		
		labelGreen.setBounds(10, 25, 78, 14);
		greenSliderPanel.add(labelGreen);
		
		textGreen = new JTextField();
		textGreen.setText("0");
		textGreen.setColumns(10);
		textGreen.setBounds(330, 22, 50, 20);
		greenSliderPanel.add(textGreen);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(0, 0, 415, 55);
		greenSliderPanel.add(separator_3);
		
		
		blueSliderPanel.setLayout(null);
		sliderPanel.add(blueSliderPanel);
		sliderBlue.setToolTipText("Blue");
		sliderBlue.setMaximum(255);
		sliderBlue.setValue(0);
		sliderBlue.setBounds(100, 18, 200, 26);
		blueSliderPanel.add(sliderBlue);

		
		
		labelBlue.setBounds(10, 25, 72, 14);
		blueSliderPanel.add(labelBlue);
		
		textBlue = new JTextField();
		textBlue.setText("0");
		textBlue.setColumns(10);
		textBlue.setBounds(330, 22, 50, 20);
		blueSliderPanel.add(textBlue);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(0, 0, 415, 14);
		blueSliderPanel.add(separator_4);
		
		
		//Slider, Button, and TextField action listeners
		buttonRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sliderRed.setValue(255);
				sliderGreen.setValue(0);
				sliderBlue.setValue(0);
				setSliders(255,0,0);
			}
		});
		
		textBrightness.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	int text = Integer.parseInt(textBrightness.getText());
		    	if(text>=1 && text<= 100) {
		    		sliderBrightness.setValue(text);
		    	} else {
		    		textBrightness.setText("100");
		    	}
		    }
		});
		
		textRed.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	int text = Integer.parseInt(textRed.getText());
		    	if(text>=1 && text<= 255) {
		    		sliderRed.setValue(text);
		    	} else {
		    		textRed.setText("255");
		    	}
		    }
		});
		
		textGreen.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	int text = Integer.parseInt(textGreen.getText());
		    	if(text>=1 && text<= 255) {
		    		sliderGreen.setValue(text);
		    	} else {
		    		textGreen.setText("255");
		    	}
		    }
		});
		
		textBlue.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	int text = Integer.parseInt(textBlue.getText());
		    	if(text>=1 && text<= 255) {
		    		sliderBlue.setValue(text);
		    	} else {
		    		textBlue.setText("255");
		    	}
		    }
		});
		
		sliderBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textBrightness.setText("" + sliderBrightness.getValue());
				//port.sendData(sliderBrightness.getValue(), sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
				sendDataToArduino();
			}
		});
		
		sliderRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textRed.setText("" + sliderRed.getValue());
				//port.sendData(sliderBrightness.getValue(), sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
				sendDataToArduino();
			}
		});
		
		sliderGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textGreen.setText("" + sliderGreen.getValue());
//				port.sendData(sliderBrightness.getValue(), sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
				sendDataToArduino();
			}
		});
		
		sliderBlue.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			textBlue.setText("" + sliderBlue.getValue());
//			port.sendData(sliderBrightness.getValue(), sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
			sendDataToArduino();

		}
		});
		
		

		portLabel.setBounds(615, 559, 148, 19);
		getContentPane().add(portLabel);
		portLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Tool Bar Initialization and Construction
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(0, 0, 831, 28);
		getContentPane().add(toolBar);
		
		JButton btnSaveCurrentColor = new JButton("Save Current Color");
		btnSaveCurrentColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrintStream p = null;
				try {
					p = new PrintStream(temp);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				String colorName = JOptionPane.showInputDialog(frame,"Enter Color Name");
				if(colorName != null) {
					customColors.add(new JButton(colorName));
					System.out.println("Before add");
					customColorBox.add(customColors.get(customColors.size()));
					System.out.println("After Add");
					int bright = sliderBrightness.getValue();
					int red = sliderRed.getValue();
					int green = sliderGreen.getValue();
					int blue = sliderBlue.getValue();
					p.println("-");
					p.println(colorName);
					p.println(bright);
					p.println(red);
					p.println(green);
					p.println(blue);
					customColors.get(numOfButtons).addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							System.out.println(colorName);
							setSliders(bright, red, green, blue);
						}
					});
//					System.out.println("After ActionListener");
					customColors.get(numOfButtons).revalidate();
					numOfButtons++;
				}
			}
		});
		toolBar.add(btnSaveCurrentColor);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(298, 59, 11, 277);
		getContentPane().add(separator_1);
		
		customColorBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		customColorBox.setBounds(26, 392, 718, 162);
		getContentPane().add(customColorBox);
		customColorBox.setLayout(new GridLayout(5, 4, 0, 0));
		
		JLabel lblCustomColors = new JLabel("Custom Colors");
		lblCustomColors.setBounds(0, 364, 774, 17);
		getContentPane().add(lblCustomColors);
		lblCustomColors.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomColors.setFont(new Font("Tahoma", Font.PLAIN, 14));
	}
}