package ui;

import graphics.ColorTable;

import java.awt.Color;
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
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import monster.MonsterDefinition;
import monster.MonsterDefinitionList;
import monster.MonsterUtil;

import reader.JSPUtil;
import shared.LoonylandConstant;

public class Monster extends JFrame implements ActionListener, FocusListener, ChangeListener {

	private static final long serialVersionUID = 1L;
	MonsterDefinitionList monsterDefinitionList = new MonsterDefinitionList();
	ColorTable cTable = new ColorTable(LoonylandConstant.TOOLDIR); // gets colour table for colours available in the game
	
	private final int FIELDHEIGHT = LoonylandConstant.FIELDHEIGHT;
	private final String[] STD_BTN_TXT = {"Preview", "Reset", "Apply Changes"};
	private final Border STD_BORDER = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	private JComboBox<String> combo;
	private MonsterDefinition md;
	
	private JComboBox<Color>[] exeColorCombo = new JComboBox[2];
	private JButton exePreviewButton = new JButton(STD_BTN_TXT[0]);
	private JButton exeResetButton = new JButton(STD_BTN_TXT[1]);
	private JButton exeApplyButton = new JButton(STD_BTN_TXT[2]);
	private JButton[] exeButtonArray = {exePreviewButton, exeResetButton, exeApplyButton};
	
	JTextField currentMonsterName = new JTextField();
	
	JTextField[] statBox = new JTextField[LoonylandConstant.STATSTODISP];
	
	private JButton propResetButton = new JButton(STD_BTN_TXT[1]);
	private JButton propApplyButton = new JButton(STD_BTN_TXT[2]);
	private JButton[] propButtonArray = {propResetButton, propApplyButton};
	
	JComboBox<Color>[] jspColorCombo = new JComboBox[8];
	
	private JSlider brightnessSlider = new JSlider(JSlider.HORIZONTAL, -31, 31, 0);
	private JSlider offsetSlider = new JSlider(JSlider.HORIZONTAL, -4, 4, 0);
	
	private JCheckBox pngBox; // box to check if you want to print PNGs
	
	private JButton jspPreviewButton = new JButton(STD_BTN_TXT[0]);
	private JButton jspResetButton = new JButton(STD_BTN_TXT[1]);
	private JButton jspApplyButton = new JButton(STD_BTN_TXT[2]);
	private JButton[] jspButtonArray = {jspPreviewButton, jspResetButton, jspApplyButton};
	
	private JLabel imageLabel = new JLabel(); //imageLabel that will hold the gif
	private ImageIcon image;
	private int gifCount = 0;
	
	private JSPUtil util = new JSPUtil(cTable.getPalette(), false);
	
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
			JOptionPane.showMessageDialog(this, "You must have Loonyland 2 Version 1.0M or 1.2 CE to run this program.", "Version error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		LaunchMonster(); // Launches program
	}
	
	public void LaunchMonster(){
		Color[] colors = cTable.getAllColors();
		// UI
		getContentPane().setLayout(null);
	
		/*			Monster Combo			*/
		combo = new JComboBox<String>(monsterDefinitionList.getSortedNameArray(true)); // Lists all monsters in LL2 in order
		combo.setBackground(Color.WHITE);
		combo.setForeground(Color.BLACK);
		combo.setLocation(650, 10);
		combo.setSize(200, FIELDHEIGHT);
		
		JLabel monsterLabel = new JLabel("Monsters:");
		monsterLabel.setLocation(570, 10);
		monsterLabel.setSize(100, FIELDHEIGHT);		
		
		//Get monster info
		getNewMonster();
		util.loadJSP(md.getJspName(true));
		
		/*			EXE PANEL			*/
		JPanel exePanel = new JPanel(new FlowLayout(), false);
		
		JLabel[] colorLabelArray = {new JLabel("Color 1:", JLabel.LEFT), new JLabel(" To color 2:", JLabel.LEFT)};
		
		//Creates the combo boxes to edit monster colours
		for(int i=0; i<exeColorCombo.length; i++){
			exeColorCombo[i] = new JComboBox<Color>(colors); // First colour box for EXE modifications
			exeColorCombo[i].setRenderer(new CellColorRenderer());
			exePanel.add(colorLabelArray[i]);
			exePanel.add(exeColorCombo[i]);
		}
		
		//Creates the button to preview/reset/apply changes
		for(int i=0; i<exeButtonArray.length; i++){
			exeButtonArray[i].addActionListener(this);
			exeButtonArray[i].setLocation(580+(90*i), 120);
			exeButtonArray[i].setSize((i==2) ? 120 : 80, FIELDHEIGHT);
			getContentPane().add(exeButtonArray[i]);
		}
		
		JLabel monsterName = new JLabel("Monster name: ");
		monsterName.setLocation(585,95);
		monsterName.setSize(100, FIELDHEIGHT);
		getContentPane().add(monsterName);
		
		//Create field to edit/display monster name
		currentMonsterName.setFont(new Font("Courier", 12, 12));
		currentMonsterName.setSize(205, FIELDHEIGHT);
		currentMonsterName.setLocation(675,95);
		currentMonsterName.setDocument(new JTextFieldLimit(28));
		currentMonsterName.setText(md.getName(true));
		getContentPane().add(currentMonsterName);
		
		// Create a box to put all the EXE modifications inside for a better visual feel
		exePanel.setLocation(570, 35);
		exePanel.setSize(320, 115);
		exePanel.setBorder(BorderFactory.createTitledBorder(STD_BORDER, "Exe Modifications"));

		/*			PROP PANEL			*/
		JPanel PropPanel = new JPanel(null);
		JPanel PropInternalPanel = new JPanel(new GridLayout(2, 6, 5, 5));
		
		//Loads monster stats and displays them
		String[] statTextArray = {"Level", "Health", "Armor", "Damage", "Drop value", "Speed"};
		JLabel[] statBoxText = new JLabel[LoonylandConstant.STATSTODISP]; // JLevels for Stats inputs
		for(int i=0; i<statBox.length; i++){
			statBoxText[i] = new JLabel(statTextArray[i] + ": ", JLabel.CENTER);
			PropInternalPanel.add(statBoxText[i]);
			statBox[i] = new JTextField(JTextField.CENTER);
			resetStatTextBox(i);
			PropInternalPanel.add(statBox[i]);
			statBox[i].addFocusListener(this);
		}
		
		// Puts stat information in its own box for visual effect
		PropInternalPanel.setLocation(30, 20);
		PropInternalPanel.setSize(500, 60);
		PropPanel.add(PropInternalPanel);

		PropPanel.setLocation(10, 480);
		PropPanel.setSize(553, 120);
		
		PropPanel.setBorder(BorderFactory.createTitledBorder(STD_BORDER, "Monster Properties"));
		
		//Creates buttons to reset/apply changes
		for(int i=0; i<propButtonArray.length; i++){
			propButtonArray[i].addActionListener(this);
			propButtonArray[i].setLocation(50+(260*i), 567);
			propButtonArray[i].setSize(250-(22*i), FIELDHEIGHT);
			getContentPane().add(propButtonArray[i]);
		}
		
		/*			JSP PANEL			*/
		JPanel JSPPanel = new JPanel(null);
		JPanel JSPInternalPanel = new JPanel(new GridLayout(9, 2, 5, 5));

		JSPInternalPanel.add(new JLabel("Color 2"), JLabel.CENTER);
		JSPInternalPanel.add(new JLabel("Color 1"), JLabel.CENTER);
		
		JTextField[] jspColorText = new JTextField[8];
		
		for (int i = 0; i < 8; i++) { // Creates the JSP colour boxes for all the 8 main colours
			jspColorText[i] = new JTextField("   ");
			jspColorText[i].setBackground(cTable.getMiddleColor(i)); // Gets the middle colour of each shade so you know which colour you're changing
			jspColorText[i].setEditable(false);
			JSPInternalPanel.add(jspColorText[i]);
			
			jspColorCombo[i] = new JComboBox<Color>(colors); // Create combo box with corresponding colour
			jspColorCombo[i].setSelectedIndex(i);
			jspColorCombo[i].setRenderer(new CellColorRenderer());
			JSPInternalPanel.add(jspColorCombo[i]);
		}
		JSPInternalPanel.setLocation(30, 20);
		JSPInternalPanel.setSize(250, 220);
		JSPPanel.add(JSPInternalPanel);
		
		//Creates sliders for either colour offset or brightness offset
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
		
		//Add checkbox to decide whether or not PNGs should be printed along with the gif
		pngBox = new JCheckBox("Print PNG when selected.");
		pngBox.setLocation(25, 365);
		pngBox.setSize(200, FIELDHEIGHT);
		JSPPanel.add(pngBox);
		
		//Adds button to preview/reset/apply JSP modifications
		for(int i=0;i<jspButtonArray.length; i++){
			jspButtonArray[i].addActionListener(this);
			jspButtonArray[i].setLocation(580+(90*i), 565);
			jspButtonArray[i].setSize((i==2) ? 120 : 80, FIELDHEIGHT);
			getContentPane().add(jspButtonArray[i]);
		}
		
		JSPPanel.setLocation(570, 150);
		JSPPanel.setSize(320, 450);
		JSPPanel.setBorder(BorderFactory.createTitledBorder(STD_BORDER, "JSP Modifications"));
		
		repaint(true);
		
		//Creates the imageLabel to hold the gif of the monster
		imageLabel.setIcon(image);
		imageLabel.setLocation(10, 10);
		imageLabel.setSize(550, 470);
		imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		imageLabel.setOpaque(true);
		imageLabel.setBackground(Color.WHITE);
		imageLabel.setVerticalAlignment(JLabel.CENTER);
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		
		combo.addItemListener(new ItemListener() { // If a different monster is selected
			public void itemStateChanged(ItemEvent ie) {
				repaint(true); // redraw monster display
			}
		});
		
		//Add all elements to ContentPane that way they're displayed on the screen
		getContentPane().add(exePanel);
		getContentPane().add(JSPPanel);
		getContentPane().add(PropPanel);
		getContentPane().add(monsterLabel);
		getContentPane().add(combo);
		getContentPane().add(imageLabel);
	}
	
	private void resetStatTextBox(int statID){
		int value = getStat(statID);
		statBox[statID].setText(String.valueOf(value));
	}
	private void resetStatTextBoxes(){
		for(int i=0; i<LoonylandConstant.STATSTODISP; i++){
			resetStatTextBox(i);
		}
	}
	
	private void resetEXECombos(){
		if(md.getColors()[0] != 255){
			for(int i=0; i<exeColorCombo.length; i++){
				exeColorCombo[i].setSelectedIndex(md.getColors()[i]);
			}
		}else{
			for(int i=0; i<exeColorCombo.length; i++){
				exeColorCombo[i].setSelectedIndex(0);
			}
		}
	}
	private void resetJSPCombos(){
		for(int i=0; i<jspColorCombo.length; i++){
			jspColorCombo[i].setSelectedIndex(i);
		}
	}
	private boolean isJSPModified(){
		for(int i=0; i<jspColorCombo.length; i++){
			if(jspColorCombo[i].getSelectedIndex() != i){
				return true;
			}
		}
		return false;
	}
	private boolean isEXEModified(){
		int colorToChange = exeColorCombo[0].getSelectedIndex();
		int colorResult = exeColorCombo[1].getSelectedIndex();
		if(colorToChange == colorResult){
			return false;
		}
		int[] colors = md.getColors();
		if(colorToChange == colors[0] && colorResult == colors[1]){
			return false;
		}
		return true;
	}
	private int[] getJSPModifications(){
		int[] range = new int[jspColorCombo.length];
		for(int i=0; i<jspColorCombo.length; i++){
			range[i] = jspColorCombo[i].getSelectedIndex();
		}
		return range;
	}
	
	private void resetSliders(){
		brightnessSlider.setValue(0);
		offsetSlider.setValue(0);
	}
	
	private int getStat(int statID){
		int value = -1;
		switch(statID){
			case 0:
				value = md.getLevel();
				break;
			case 1:
				value = md.getHP();
				break;
			case 2:
				value = md.getArmor();
				break;
			case 3:
				value = md.getDmg();
				break;
			case 4:
				value = md.getRarity();
				break;
			case 5:
				value = md.getSpeed();
				break;
		}
		return value;
	}
	private void setStats(){
		int[] fieldValues = new int[statBox.length];
		for(int i=0; i<statBox.length; i++){ // Reads what's inside the stat boxes
			fieldValues[i] = Integer.parseInt(statBox[i].getText()); 
		}
		md.setLevel(fieldValues[0]);
		md.setHP(fieldValues[1]);
		md.setArmor(fieldValues[2]);
		md.setDmg(fieldValues[3]);
		md.setRarity(fieldValues[4]);
		md.setSpeed(fieldValues[5]);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {}

	@Override
	public void focusGained(FocusEvent evt) {}

	@Override
	public void focusLost(FocusEvent evt) {
		int id = 0;
		for (int i = 0; i < LoonylandConstant.STATSTODISP; i++) {
			if (evt.getSource() == statBox[i]) {
				id = i; // get statBox id through for loop comparison
			}
		}
		VerifyInput(id); // verify the input is valid
	}
	
	private void VerifyInput(int id) {
		int max = 255; // default max unless otherwise specified
		int min = 0; // default min unless otherwise specified
		if (id == 1 || id == 3) {
			max = 65536;
		}
		if (id == 0 || id == 1 || id ==5) {
			min = 1;
		}
		
		String msg = "Must be a number from " + Integer.toString(min) + "-" + Integer.toString(max);
		int fieldValue = 0;
		try {
			fieldValue = Integer.parseInt(statBox[id].getText()); // Reads what's inside the editable field of a given stat
		} catch (NumberFormatException e) {
			// If what the user inputed isn't a number
			statBox[id].requestFocus();
			resetStatTextBox(id);
			JOptionPane.showMessageDialog(this, "Only numbers are allowed!", "Not a number!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (fieldValue >= max || fieldValue <= min) {
			// If the value is out of the bounds LL2 has for stats
			statBox[id].requestFocus();
			resetStatTextBox(id);
			JOptionPane.showMessageDialog(this, msg, "Input error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void getNewMonster(){
		int index = monsterDefinitionList.getIndexByName((String) combo.getSelectedItem(), true); // finds which monster is selected
		md = MonsterDefinitionList.allDefinitions[index]; // calls information on said monster
	}
	
	private void repaint(boolean changedMonster) {
		if(changedMonster){
			getNewMonster();
			util.loadJSP(md.getJspName(true));
		}

		// Print gif
		if(!isEXEModified()){
			util.recolor(md.getColors()[0], md.getColors()[1]);
		}
		gifCount = 1-gifCount;
		String gifName = LoonylandConstant.TOOLBOX_DIRECTORY + "/Gifs/" + md.getName(true) + gifCount + ".gif";
		util.writeGif(LoonylandConstant.TOOLBOX_DIRECTORY, "/Gifs/" + md.getName(true) + gifCount, "/Images/" + md.getName(true), pngBox.isSelected());
		
		image = new ImageIcon(gifName);
		imageLabel.setIcon(image);

		if (changedMonster) { // If monster is changed
			resetEXECombos();
			currentMonsterName.setText(md.getName(true));
			resetStatTextBoxes();
			resetJSPCombos();
			resetSliders();
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object evt = e.getSource();
		boolean rewrite = false;
		boolean suppressEXE = true;
		boolean suppressJSP = true;
		
		if (evt == exePreviewButton || evt == exeApplyButton) {
			util.recolor(exeColorCombo[0].getSelectedIndex(), exeColorCombo[1].getSelectedIndex());
			if(evt == exePreviewButton){
				suppressEXE = false;
			}else if(evt == exeApplyButton){
				int colorFrom = exeColorCombo[0].getSelectedIndex();
				int colorTo = exeColorCombo[1].getSelectedIndex();
				md.setColors(new int[]{colorFrom, colorTo});
				MonsterUtil mutil = new MonsterUtil();
				try {
					mutil.exeColorNameChange(md.getID(), currentMonsterName.getText(), colorFrom, colorTo);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
				
				String oldName = md.getName(true);
				String newName = currentMonsterName.getText();
				if(!oldName.equals(newName)){
					combo.addItem(md.getName(true));
					combo.setSelectedItem(md.getName(true));
					combo.removeItem(oldName);
				}
			}
		}else if (evt == exeResetButton) {
			resetEXECombos();
		}else if (evt == propApplyButton) {
			MonsterUtil mutil = new MonsterUtil();
			try {
				mutil.readWriteStats(md.getID(), statBox);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			setStats();
		} else if (evt == propResetButton) {
			resetStatTextBoxes();
		}else if (evt == jspPreviewButton || evt == jspApplyButton) {
			suppressJSP = false;
			if(evt == jspApplyButton){
				rewrite = true;
			}
		} else if (evt == jspResetButton) {
			resetJSPCombos();
			resetSliders();
		}
		redrawJSP(suppressEXE, suppressJSP, rewrite);
		repaint(false);
	}
	
	private void redrawJSP(boolean suppressEXE, boolean suppressJSP, boolean rewrite){
		util.loadJSP(md.getJspName(true));
		if(isJSPModified() && !suppressJSP){
			util.recolorAll(getJSPModifications());
		}
		if(isEXEModified() && !suppressEXE){
			util.recolor(exeColorCombo[0].getSelectedIndex(), exeColorCombo[1].getSelectedIndex());
		}
		if(!suppressJSP){
			if(brightnessSlider.getValue() != 0){
				util.light(brightnessSlider.getValue());
			}
			if(offsetSlider.getValue() != 0){
				util.shift(offsetSlider.getValue());
			}
		}
		
		if(rewrite){
			util.writeJSP(md.getJspName(true));
		}
	}

}
