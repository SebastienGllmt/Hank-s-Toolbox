package monster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import shared.LoonylandConstant;

public class MonsterUtil {

	String filename;
	String outputFilename;
	FileInputStream in = null;
	FileOutputStream out = null;
	int c;
	long count = 0;
	JFrame frame = null;
	
	public List<String> GetFileList(String dir) {
		List<String> listOfJSP = new ArrayList<String>();

		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().toLowerCase().contains(".jsp"))
					listOfJSP.add(listOfFiles[i].getName()); //gets names of JSPs in folder
			}
		}
		return listOfJSP;
	}
	public void modifySetup(){ //gets ready to modify EXE
		try{
			filename = LoonylandConstant.LOONYLANDEXE.replace(".","_old.");
			outputFilename = LoonylandConstant.LOONYLANDEXE;
			try{
				in = new FileInputStream(filename);
				out = new FileOutputStream(outputFilename);
			} catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(frame, "Loonyland.exe not found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
			}
			
			File doesOldExist = new File(LoonylandConstant.LOONYLANDEXE.replace(".","_old."));
			if(doesOldExist.exists() == true){
				doesOldExist.delete(); //deletes loonyland_old.exe to make room for new one
			}
			File oldFile = new File(LoonylandConstant.LOONYLANDEXE);
			oldFile.renameTo(new File(LoonylandConstant.LOONYLANDEXE.replace(".","_old."))); //renames current loonyland.exe to loonyland_old.exe
			File backup = new File(LoonylandConstant.LOONYLANDEXE.replace(".","_bak."));
			if(backup.exists() == false){
				readCreate("_bak."); //if no backup exists, create one
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Loonyland.exe not found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void readCreate(String titleAddition) throws IOException{ //creats an exact copy of the exe
		modifySetup();
		outputFilename = LoonylandConstant.LOONYLANDEXE.replace(".",titleAddition);

		while ((c = in.read()) != -1) {
			out.write(c);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
	}
	
	public void exeColorNameChange(int monsterID, String monsterName, int byteValueColor1, int byteValueColor2) throws Exception {
		modifySetup();
		int versionID = MonsterDefinitionList.getVersionNumber(LoonylandConstant.LOONYLANDEXE.replace(".","_old."));
		long monsterStart = LoonylandConstant.MONSTER_START_BYTE[versionID]+ (monsterID * LoonylandConstant.MONSTER_LENGTH);
		long AddressColor1 = monsterStart + LoonylandConstant.COLOR_SELECTED_BYTE_OFFSET;
		long AddressColor2 = monsterStart + LoonylandConstant.COLOR_RESULT_BYTE_OFFSET;
		int position = 0;
		
		while ((c = in.read()) != -1) {
			if (count == AddressColor1) {
				c = byteValueColor1;
			} else if (count == AddressColor2) {
				c = byteValueColor2;
			}else if(count >= monsterStart && count < monsterStart + LoonylandConstant.MONSTER_NAME_LENGTH){
				if(count < monsterStart+ monsterName.length()){
					c = monsterName.substring(position, position+1).getBytes()[0];
					position++;
				}else{
					c = 0;
				}
			}
			out.write(c);
			count++;
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
	}

	public void readWriteStats(int monsterID, JTextField[] statBox) throws Exception {
		modifySetup();
		int versionID = MonsterDefinitionList.getVersionNumber(LoonylandConstant.LOONYLANDEXE.replace(".","_old."));
		int start = LoonylandConstant.MONSTER_START_BYTE[versionID] + (monsterID * LoonylandConstant.MONSTER_LENGTH);
		long levelAddress = start + LoonylandConstant.MONSTER_LEVEL;
		long hpAddress = start + LoonylandConstant.MONSTER_HP[0];
		long armorAddress = start + LoonylandConstant.MONSTER_ARMOR;
		long dmgAddress = start + LoonylandConstant.MONSTER_DMG[0];
		long rarityAddress = start + LoonylandConstant.ITEM_RARITY;
		long speedAddress = start + LoonylandConstant.WALKSPEED;
		
		while ((c = in.read()) != -1) {
			if (count == levelAddress) {
				c = Integer.parseInt(statBox[0].getText()) / 256;
			} else if (count == hpAddress) {
				c = Integer.parseInt(statBox[1].getText())%256; //gets the first byte for hp
			} else if(count == hpAddress + 1){
				c = Integer.parseInt(statBox[1].getText())/256; //gets the second byte for hp
			} else if (count == armorAddress) {
				c = Integer.parseInt(statBox[2].getText());
			} else if (count == dmgAddress) {
				c = Integer.parseInt(statBox[3].getText())%256; //gets the first byte for dmg
			} else if (count == dmgAddress + 1){
				c = Integer.parseInt(statBox[3].getText())/256; //gets the second byte for dmg
			} else if (count == rarityAddress) {
				c = Integer.parseInt(statBox[4].getText());
			} else if (count == speedAddress){
				c = Integer.parseInt(statBox[5].getText());
			}
			out.write(c);
			count++;
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
	}
}