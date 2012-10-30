package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphics.ColorTable;
import reader.JSPUtil;

import monster.MonsterUtil;
import shared.LoonylandConstant;
import monster.MonsterDefinition;
import monster.MonsterDefinitionList;

@SuppressWarnings("serial")
public class Monster extends JFrame implements ActionListener, FocusListener, ChangeListener {
	JComboBox combo;
	JComboBox exeColorCombo1;
	JComboBox exeColorCombo2;
	JTextField[] jspColorText = new JTextField[8];
	JComboBox[] jspColorCombo = new JComboBox[9];
	JLabel imageLabel = new JLabel();
	ImageIcon ii;
	MonsterDefinitionList monsterDefinitionList = new MonsterDefinitionList();
	ColorTable cTable = new ColorTable(LoonylandConstant.TOOLDIR); // gets colour table for colours available in the game
	JButton titleScreen;
	JButton exePreviewButton;
	JButton propResetButton;
	JButton jspPreviewButton;
	JButton exeResetButton;
	JButton jspResetButton;
	JButton applyExeButton;
	JButton applyJspButton;
	JButton applyPropButton;
	JSlider brightnessSlider = new JSlider(JSlider.HORIZONTAL, -31, 31, 0);
	JSlider offsetSlider = new JSlider(JSlider.HORIZONTAL, -4, 4, 0);
	static int FIELDHEIGHT = LoonylandConstant.FIELDHEIGHT;
	JTextField[] statBox = new JTextField[LoonylandConstant.STATSTODISP];
	JTextField currentMonsterName = new JTextField();
	JCheckBox pngBox; // box to check if you want to print PNGs
	JCheckBox randomiseBox; // box to check if you want to randomise JSP
	String jspLocation; //String that will contain the location of a real JSP when a preview is created

	public static void main(String[] args) {
		(new File(LoonylandConstant.TOOLBOX_DIRECTORY)).mkdir();
		(new File(LoonylandConstant.TOOLBOX_DIRECTORY + "/Gifs")).mkdir();
		Monster frame = new Monster(LoonylandConstant.MONSTERNAME); // creates the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(LoonylandConstant.WINDOWSIZE[0], LoonylandConstant.WINDOWSIZE[1]);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public Monster(String title) {
		super(title);
		if (!monsterDefinitionList.BuildMonsterDefinition()) {
			JFrame frame = null; // Initialise frame
			JOptionPane.showMessageDialog(frame, "You must have Loonyland 2 Version 1.0M or 1.2 CE to run this program.", "Version error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		MonsterEdit(); // Launches program
	}

	public void MonsterEdit() {
		Color[] colors = cTable.getAllColors(); // gets colours available in LL2
		// UI
		getContentPane().setLayout(null);
		titleScreen = new JButton("Return to Title Screen"); // Button to go back to Title Screen
		titleScreen.addActionListener(this);
		titleScreen.setLocation(10, LoonylandConstant.WINDOWSIZE[1]-50);
		titleScreen.setSize(930, FIELDHEIGHT);

		combo = new JComboBox(monsterDefinitionList.getSortedNameArray(true)); // Lists all monsters in LL2 in order
		combo.setBackground(Color.white);
		combo.setForeground(Color.black);
		combo.setLocation(650, 10);
		combo.setSize(200, FIELDHEIGHT);

		JLabel monsterLabel = new JLabel("Monsters:");
		monsterLabel.setLocation(570, 10);
		monsterLabel.setSize(100, FIELDHEIGHT);		
		
		int index = monsterDefinitionList.getIndexByName((String) combo.getSelectedItem(), true); // finds which monster is selected
		MonsterDefinition md = MonsterDefinitionList.allDefinitions[index]; // calls information on said monster

		String[] stdButtonTextArray = {"Preview", "Reset", "Apply Changes"};
		
		JPanel exePanel = new JPanel(new FlowLayout(), false);
		JLabel colorLabel1 = new JLabel("Color 1:", JLabel.LEFT);
		exePanel.add(colorLabel1);
		exeColorCombo1 = new JComboBox(colors); // First colour box for EXE modifications
		exeColorCombo1.setRenderer(new CellColorRenderer());
		exePanel.add(exeColorCombo1);
		JLabel colorLabel2 = new JLabel(" To color 2:", JLabel.LEFT);
		exePanel.add(colorLabel2);
		exeColorCombo2 = new JComboBox(colors); // Second colour box for EXE modifications
		exeColorCombo2.setRenderer(new CellColorRenderer());
		exePanel.add(exeColorCombo2);

		exePreviewButton = new JButton(stdButtonTextArray[0]);
		exeResetButton = new JButton(stdButtonTextArray[1]);
		applyExeButton = new JButton(stdButtonTextArray[2]);
		JButton[] exeButtonArray = {exePreviewButton, exeResetButton, applyExeButton};
		for(int i=0; i<exeButtonArray.length; i++){
			exeButtonArray[i].addActionListener(this);
			exeButtonArray[i].setLocation(580+(90*i), 120);
			if(i==2){
				exeButtonArray[i].setSize(120, FIELDHEIGHT);
			}else{
				exeButtonArray[i].setSize(80, FIELDHEIGHT);
			}
			getContentPane().add(exeButtonArray[i]);
		}
		
		JLabel monsterName = new JLabel("Monster name: ");
		monsterName.setLocation(585,95);
		monsterName.setSize(100, FIELDHEIGHT);
		getContentPane().add(monsterName);
		Font courier = new Font("Courier", 12, 12);
		currentMonsterName.setFont(courier);
		currentMonsterName.setSize(205, FIELDHEIGHT);
		currentMonsterName.setLocation(675,95);
		currentMonsterName.setDocument(new JTextFieldLimit(28));
		currentMonsterName.setText(md.getName(true));
		getContentPane().add(currentMonsterName);

		// Create a box to put all the EXE modifications inside for a better visual feel
		exePanel.setLocation(570, 35);
		exePanel.setSize(320, 115);
		Border exeBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Exe Modifications");
		exePanel.setBorder(exeBorder);

		JPanel PropPanel = new JPanel();
		PropPanel.setLayout(null);
		JPanel PropInternalPanel = new JPanel(new GridLayout(2, 6, 5, 5));

		String[] statTextArray = {"Level", "Health", "Armor", "Damage", "Drop value", "Speed"};
		int[] statValueArray = {md.getLevel(), md.getHP1() + (md.getHP2() * 256), 
				md.getArmor(), md.getDmg1() + (md.getDmg2() * 256),
				md.getRarity(), md.getSpeed()
		};
		JLabel[] statBoxText = new JLabel[LoonylandConstant.STATSTODISP]; // JLevels for Stats inputs
		for(int i=0; i<statBox.length; i++){
			statBoxText[i] = new JLabel(statTextArray[i] + ": ", JLabel.CENTER);
			PropInternalPanel.add(statBoxText[i]);
			statBox[i] = new JTextField(String.valueOf(statValueArray[i]), JTextField.CENTER);
			PropInternalPanel.add(statBox[i]);
			statBox[i].addFocusListener(this);
		}

		// Puts stat information in its own box for visual effect
		PropInternalPanel.setLocation(30, 20);
		PropInternalPanel.setSize(500, 60);
		PropPanel.add(PropInternalPanel);

		PropPanel.setLocation(10, 480);
		PropPanel.setSize(553, 120);
		Border PropBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Monster Properties");
		PropPanel.setBorder(PropBorder);

		propResetButton = new JButton(stdButtonTextArray[1]);
		applyPropButton = new JButton(stdButtonTextArray[2]);
		JButton[] propButtonArray = {propResetButton, applyPropButton};
		for(int i=0; i<propButtonArray.length; i++){
			propButtonArray[i].addActionListener(this);
			propButtonArray[i].setLocation(50+(260*i), 567);
			propButtonArray[i].setSize(250-(22*i), FIELDHEIGHT);
			getContentPane().add(propButtonArray[i]);
		}
		
		// Creates box in which to put JSP modifications for better visual effect
		JPanel JSPPanel = new JPanel();
		JSPPanel.setLayout(null);
		JPanel JSPInternalPanel = new JPanel(new GridLayout(9, 2, 5, 5));

		JSPInternalPanel.add(new JLabel("Color 2"), JLabel.CENTER);
		JSPInternalPanel.add(new JLabel("Color 1"), JLabel.CENTER);
		
		for (int i = 0; i < 8; i++) { // Creates the JSP colour boxes for all the 8 main colours
			jspColorText[i] = new JTextField("   ");
			jspColorText[i].setBackground(cTable.getMiddleColor(i)); // Gets the middle colour of each shade so you know which colour you're changing
			jspColorText[i].setEditable(false);
			JSPInternalPanel.add(jspColorText[i]);
			
			jspColorCombo[i] = new JComboBox(colors); // Create combo box with corresponding colour
			jspColorCombo[i].setSelectedIndex(i);
			jspColorCombo[i].setRenderer(new CellColorRenderer());
			JSPInternalPanel.add(jspColorCombo[i]);
		}
		JSPInternalPanel.setLocation(30, 20);
		JSPInternalPanel.setSize(250, 220);
		JSPPanel.add(JSPInternalPanel);
		
		brightnessSlider.setMinorTickSpacing(8);
		offsetSlider.setMinorTickSpacing(1);
		offsetSlider.setSnapToTicks(true);
		JSlider[] sliderArray = {brightnessSlider, offsetSlider};
		String[] sliderTextArray = {"Adjust brightness", "Offset colors"};
		JLabel[] sliderLabel = new JLabel[sliderTextArray.length];
		for(int i=0; i<sliderArray.length; i++){
			sliderArray[i].addChangeListener(this);
			sliderArray[i].setPaintTicks(true);
			sliderArray[i].setPaintLabels(true);
			sliderArray[i].setSize(256, 40);
			sliderArray[i].setLocation(25, 265+(50*i));
			JSPPanel.add(sliderArray[i]);
			sliderLabel[i] = new JLabel(sliderTextArray[i], JLabel.CENTER);
			sliderLabel[i].setLocation(25, 245+(i*55));
			sliderLabel[i].setSize(256, FIELDHEIGHT);
			JSPPanel.add(sliderLabel[i]);
		}

		randomiseBox = new JCheckBox("randomise JSP when selected.");
		randomiseBox.setLocation(25, 360);
		randomiseBox.setSize(250, FIELDHEIGHT);
		JSPPanel.add(randomiseBox);
		pngBox = new JCheckBox("Print PNG when selected.");
		pngBox.setLocation(25, 380);
		pngBox.setSize(200, FIELDHEIGHT);
		JSPPanel.add(pngBox);
		
		jspPreviewButton = new JButton(stdButtonTextArray[0]);
		jspResetButton = new JButton(stdButtonTextArray[1]);
		applyJspButton = new JButton(stdButtonTextArray[2]);
		JButton[] buttonArray = {jspPreviewButton, jspResetButton, applyJspButton};
		for(int i=0;i<buttonArray.length; i++){
			buttonArray[i].addActionListener(this);
			buttonArray[i].setLocation(580+(90*i), 565);
			buttonArray[i].setSize(80, FIELDHEIGHT);
			getContentPane().add(buttonArray[i]);
		}
		
		JSPPanel.setLocation(570, 150);
		JSPPanel.setSize(320, 450);
		Border JSPBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "JSP Modifications");
		JSPPanel.setBorder(JSPBorder);

		init(true, false); // draws
		imageLabel.setIcon(ii);
		imageLabel.setLocation(10, 10);
		imageLabel.setSize(550, 470);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		imageLabel.setBorder(border);
		imageLabel.setOpaque(true);
		imageLabel.setBackground(Color.WHITE);
		imageLabel.setVerticalAlignment(JLabel.CENTER);
		imageLabel.setHorizontalAlignment(JLabel.CENTER);

		combo.addItemListener(new ItemListener() { // If a different monster is selected
			public void itemStateChanged(ItemEvent ie) {
				init(true, false); // redraw monster display
			}
		});

		exeColorCombo2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					exeColorCombo2.getEditor().getEditorComponent().setBackground((Color) exeColorCombo2.getSelectedItem());
				}
			}
		});
		exeColorCombo1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					exeColorCombo1.getEditor().getEditorComponent().setBackground((Color) exeColorCombo1.getSelectedItem());
				}
			}
		});
		// Add all elements to ContentPane that way they're displayed on the screen
		
		//getContentPane().add(titleScreen); feature not yet added
		getContentPane().add(exePanel);
		getContentPane().add(JSPPanel);
		getContentPane().add(PropPanel);
		getContentPane().add(monsterLabel);
		getContentPane().add(combo);
		getContentPane().add(imageLabel);
	}

	public void focusGained(FocusEvent e) { //If focus is gained on stat modiciations
	}

	public void focusLost(FocusEvent e) { //If focus is lost on stat modification
		int id = 0;
		for (int i = 0; i < LoonylandConstant.STATSTODISP-1; i++) {
			if (e.getSource() == statBox[i]) {
				id = i; // get statBox id through for loop comparison
			}
		}
		int max = 255; // default max unless otherwise specified
		int min = 0; // default min unless otherwise specified
		if (id == 1 || id == 3) {
			max = 65536;
		}
		if (id == 0 || id == 1 || id ==5) {
			min = 1;
		}
		VerifyInput(id, min, max); // verify the input is valid
	}

	private void VerifyInput(int id, int min, int max) {
		String msg = "Must be a number from " + Integer.toString(min) + "-" + Integer.toString(max);
		int fieldValue = 0;
		JFrame frame = null;
		try {
			fieldValue = Integer.parseInt(statBox[id].getText()); // Reads what's inside the editable field of a given stat
		} catch (Exception e) {
			// If what the user inputed isn't a number
			statBox[id].requestFocus();
			resetStat(id);
			JOptionPane.showMessageDialog(frame, "Only numbers are allowed!", "Not a number!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (fieldValue >= max || fieldValue <= min) {
			// If the value is out of the bounds LL2 has for stats
			statBox[id].requestFocus();
			resetStat(id);
			JOptionPane.showMessageDialog(frame, msg, "Input error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void resetStat(int id) {
		int index = monsterDefinitionList.getIndexByName((String) combo.getSelectedItem(), true); // Gets monster currently selected
		MonsterDefinition md = MonsterDefinitionList.allDefinitions[index];
		// checks id of box and sets to value accordingly
		if (id == 0) {
			statBox[0].setText(String.valueOf(md.getLevel()));
		} else if (id == 1) {
			statBox[1].setText(String.valueOf(md.getHP1() + (md.getHP2() * 256)));
		} else if (id == 2) {
			statBox[2].setText(String.valueOf(md.getArmor()));
		} else if (id == 3) {
			statBox[3].setText(String.valueOf(md.getDmg1() + (md.getDmg2() * 256)));
		} else if (id == 4) {
			statBox[4].setText(String.valueOf(md.getRarity()));
		} else if(id ==5){
			statBox[5].setText(String.valueOf(md.getSpeed()));
		}
	}

	public void actionPerformed(ActionEvent evt) {
		int index = monsterDefinitionList.getIndexByName((String) combo.getSelectedItem(), true); // Gets monster selected
		MonsterDefinition md = MonsterDefinitionList.allDefinitions[index];
		int[] colors = md.getColors();
		if (evt.getSource() == exePreviewButton) {
			setPreview(exeColorCombo1, exeColorCombo2, md);
		} else if (evt.getSource() == exeResetButton) {
			exeColorCombo1.setSelectedIndex(colors[0]);
			exeColorCombo2.setSelectedIndex(colors[1]);
			colors[2] = -1; // resets temp variables
			colors[3] = -1;
			currentMonsterName.setText(md.getName(true));
			md.setColors(colors);
			init(false, false); // redraw
		} else if (evt.getSource() == applyExeButton) {
			setPreview(exeColorCombo1, exeColorCombo2, md); // sets up preview
			MonsterUtil fileUtile = new MonsterUtil();
			try {
				if (colors[2] != -1 && colors[3] != -1) { // if preview is setup
					fileUtile.exeColorNameChange(index, currentMonsterName.getText(), colors[2], colors[3]);
				} else{
					fileUtile.exeColorNameChange(index, currentMonsterName.getText(), colors[0], colors[1]);
				}
				String oldName = md.getName(true);
				md.setName(currentMonsterName.getText());
				combo.addItem(md.getName(true));
				combo.setSelectedItem(md.getName(true));
				combo.removeItem(oldName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (evt.getSource() == applyPropButton) {
			MonsterUtil fileUtile = new MonsterUtil();
			try {
				fileUtile.readWriteStats(index, statBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (evt.getSource() == propResetButton) {
			for (int i = 0; i < 4; i++) {
				resetStat(i); // resets all stats one by one
			}
		} else if (evt.getSource() == jspPreviewButton || evt.getSource() == applyJspButton) {
			System.out.println("GOTTEN");
			int[] colorIndexes = new int[8];
			JSPUtil util = new JSPUtil(md.getJspName(false), cTable.getPalette());
			MonsterUtil jspWrite = new MonsterUtil();
			for (int i = 0; i < 8; i++) {
				colorIndexes[i] = jspColorCombo[i].getSelectedIndex();
			}
			try {
				System.out.println("TRY");
				if(evt.getSource() == jspPreviewButton){
					jspLocation = md.getJspName(false);
					util.writeJSP(jspLocation + "_2.jsp");
					md.setJspName("graphics/temp.jsp");
				}
				for(int i=0; i<8; i++){
					if(i != colorIndexes[i]){
						util.recolorAll(new int[]{0,1,2,3,4,5,6,7}, colorIndexes);
						break;
					}
				}
				if(brightnessSlider.getValue() != 0){
					util.light(brightnessSlider.getValue());
				}
				if(offsetSlider.getValue() != 0){
					util.shift(offsetSlider.getValue());
				}
				if(randomiseBox.isSelected()){
					util.randomColors();
				}
				System.out.println("WRITE");
				util.writeJSP(md.getJspName(true));
			} catch (Exception e) {
				e.printStackTrace();
			}
			init(false, true);
		} else if (evt.getSource() == jspResetButton) {
			for (int i = 0; i < 8; i++) {
				jspColorCombo[i].setSelectedIndex(i); // resets all colours one by one
			}
			brightnessSlider.setValue(0);
			offsetSlider.setValue(0);
			randomiseBox.setSelected(false);
			pngBox.setSelected(false);
			init(false, false); // redraw
		} else if (evt.getSource() == titleScreen) { //never implemented
			/*Title frame = new Title(LoonylandConstant.TITLENAME);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(LoonylandConstant.WINDOWSIZE[0], LoonylandConstant.WINDOWSIZE[1]);
			frame.setResizable(false);
			frame.setVisible(true);
			dispose();
			*/
		}
	}

	private void setPreview(JComboBox exeColorCombo1, JComboBox exeColorCombo2, MonsterDefinition md) {
		int[] colors = md.getColors();
		colors[2] = exeColorCombo1.getSelectedIndex();
		colors[3] = exeColorCombo2.getSelectedIndex();
		md.setColors(colors);
		init(false, true); // redraw
	}

	private static class CellColorRenderer extends DefaultListCellRenderer {
		public CellColorRenderer() {
			setOpaque(true);
		}

		public void setBackground(Color colr) {
		}

		public void setMyBackground(Color col) {
			super.setBackground(col);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			setText("       ");
			setMyBackground((Color) value);
			setForeground((Color) value);
			return this;
		}
	}

	private void init(boolean changedMonster, boolean tempBased) {
		int index = monsterDefinitionList.getIndexByName((String) combo.getSelectedItem(), true); // gets selected monster
		MonsterDefinition md = MonsterDefinitionList.allDefinitions[index];

		// Print gif
		File f = new File(md.getJspName(true));
		JSPUtil util = new JSPUtil(f, LoonylandConstant.TOOLDIR);
		String gifName = LoonylandConstant.TOOLBOX_DIRECTORY + "/Gifs/" + md.getName(true) + ".gif";
		if (tempBased == true) {
			md.setJspName("graphics/temp.jsp");
		}
		util.writeGif(LoonylandConstant.TOOLBOX_DIRECTORY, "/Gifs/" + md.getName(true), "/Images/" + md.getName(true), pngBox.isSelected());
		if(tempBased == true){
			md.setJspName(jspLocation);
		}
		ii = new ImageIcon(gifName);
		imageLabel.setIcon(ii);

		if (md.getColors()[0] != 255 && changedMonster) { // If monster is changed
			exeColorCombo1.setSelectedIndex(md.getColors()[0]);
		}
		if (md.getColors()[2] >= 0 && md.getColors()[3] >= 0) { // if colour was changed by user
			exeColorCombo2.setSelectedIndex(md.getColors()[3]);
		} else if (md.getColors()[0] != 255) { // If colour is changed in game
			exeColorCombo2.setSelectedIndex(md.getColors()[1]);
		}
		if (changedMonster == true) {
			currentMonsterName.setText(md.getName(true));
			statBox[0].setText(String.valueOf(md.getLevel()));
			statBox[1].setText(String.valueOf(md.getHP1() + (md.getHP2() * 256)));
			statBox[2].setText(String.valueOf(md.getArmor()));
			statBox[3].setText(String.valueOf(md.getDmg1() + (md.getDmg2() * 256)));
			statBox[4].setText(String.valueOf(md.getRarity()));
			statBox[5].setText(String.valueOf(md.getSpeed()));
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}
