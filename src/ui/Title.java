package ui;

/* Add delay on selecting different monsters (?)
 * Fix Teddies (?)
 * Create independency for dependent .jsp (?)
 * Add ability to make an allied unit
 */

/*Patch notes
 * 
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import shared.LoonylandConstant;

import javax.swing.*;

@SuppressWarnings("serial")
public class Title extends JFrame implements ActionListener, FocusListener {

	static int FIELDHEIGHT = LoonylandConstant.FIELDHEIGHT;
	JButton MonsterEdit;
	JButton LoonyEdit;
	LoonylandConstant Lconst;
	
	public static void main(String[] args) {
		(new File(LoonylandConstant.TOOLBOX_DIRECTORY)).mkdir();
		Title frame = new Title(LoonylandConstant.TITLENAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(LoonylandConstant.WINDOWSIZE[0], LoonylandConstant.WINDOWSIZE[1]);
		frame.setResizable(false);
		frame.setVisible(true);	
	}
	public Title(String title) {
		super(title);

		TitleMain();
	}
	
	private void TitleMain(){
		// UI
		JLabel IntroMsg1 = new JLabel("Welcome to",JLabel.CENTER);
		IntroMsg1.setSize(900, FIELDHEIGHT*3);
		IntroMsg1.setLocation(JLabel.CENTER,10);
		IntroMsg1.setFont(new Font("Times New Roman",1,36));
		JLabel IntroMsg2 = new JLabel("Hank's Toolbox!",JLabel.CENTER);
		IntroMsg2.setSize(900, FIELDHEIGHT*3);
		IntroMsg2.setLocation(0,50);
		IntroMsg2.setFont(new Font("Times New Roman",1,48));
		
		ImageIcon monster = new ImageIcon(LoonylandConstant.TOOLDIR + "HT tab_monsterno.png"); //Cursor not over monster tab picture
		MonsterEdit = new JButton(monster);
		MonsterEdit.addActionListener(this);
		MonsterEdit.setLocation(200, 524);
		MonsterEdit.setSize(48,76);
		Icon rolloverIconMonster = new ImageIcon(LoonylandConstant.TOOLBOX_DIRECTORY + "/Toolbox/HT tab_monster.png");  //Cursor over monster tab picture
		MonsterEdit.setRolloverIcon(rolloverIconMonster);
			
		ImageIcon loony = new ImageIcon(LoonylandConstant.TOOLDIR + "HT tab_player_no.png");  //Cursor not over player tab picture
		LoonyEdit = new JButton(loony);
		LoonyEdit.addActionListener(this);
		LoonyEdit.setLocation(248, 524);
		LoonyEdit.setSize(48,76);
		Icon rolloverIconLoony = new ImageIcon(LoonylandConstant.TOOLDIR + "HT tab_player.png"); //Cursor over player tab picture
		LoonyEdit.setRolloverIcon(rolloverIconLoony);
		
		ImageIcon bigBox = new ImageIcon(LoonylandConstant.TOOLDIR + "hankstoolboxbig.png"); //Game title picture
		JLabel toolbox = new JLabel(bigBox);
		toolbox.setSize(256, 256);
		toolbox.setLocation(347,182);

		getContentPane().add(IntroMsg1);
		getContentPane().add(IntroMsg2);
		getContentPane().add(MonsterEdit);
		getContentPane().add(LoonyEdit);
		getContentPane().add(toolbox);
	}
	
	public void actionPerformed(ActionEvent evt) {
		int Case=0;
		if(evt.getSource() == MonsterEdit){
			Case = 1;
		}else if(evt.getSource() == LoonyEdit){
			Case = 2;
		}
		new Loading("Loading", Case);
		dispose();
	}
		  
	@Override
	public void focusGained(FocusEvent e) {
		
	}
	@Override
	public void focusLost(FocusEvent e) {
		
	}
}