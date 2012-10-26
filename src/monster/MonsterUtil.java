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

import com.JSPreader;
import com.LoonylandConstant;

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
	
	public void readWriteJSP(MonsterDefinition md, int[] colorResult, int[] offsetColor, boolean randomise) throws IOException {
		String monsterName = md.getJspName().trim();
		File doesOldExist = new File(monsterName.replace(".", "_old."));
		if(doesOldExist.exists() == true){
			doesOldExist.delete();
		}
		String previousFile = monsterName;
		File oldPicture = new File(previousFile);
		String oldFile = null;
		String newFile = null;
		oldPicture.renameTo(new File(monsterName.replace(".", "_old.")));
		oldFile = monsterName.replace(".", "_old.");
		newFile = monsterName;

		try {
			in = new FileInputStream(oldFile);
			out = new FileOutputStream(newFile);

			int a = in.read();
			out.write(a);
			int b = in.read();
			out.write(b);
			int count = 2;
			int imageCount = JSPreader.convert(a, b);
			int offset = 2 + (imageCount * 16);

			while ((c = in.read()) != -1) {
				if (count < offset) {
					out.write(c);
				} else {
					if (c > 128) {
						out.write(c);
					} else {
						out.write(c);
						for (int i = 0; i < c; i++) {
							int abc = in.read();
							if(randomise == true && abc != 0){
								abc = (int) (Math.random()*255);
							}
							int shiftedValue = Shift(abc, offsetColor[1]);
							int newRange = shiftedValue / 32;
							out.write(Brightness(Transpose(Shift(abc, offsetColor[1]), colorResult[newRange]),offsetColor[0]));
						}
					}
				}
				count++;
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "JSP not found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	
	public void jspCopy(String filename) throws IOException{
		outputFilename = "graphics/temp.jsp";
		try{
			in = new FileInputStream(filename);
			out = new FileOutputStream(outputFilename);
		} catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(frame, "JSP Could Not be Copied", "JSP Error", JOptionPane.ERROR_MESSAGE);
		}
		
		File doesOldExist = new File(outputFilename);
		if(doesOldExist.exists() == true){
			doesOldExist.delete(); //deletes temp.jsp to make room for new one
		}
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

	private int Transpose(int value, int newRange) {
		return ((value % 32) + (newRange * 32));
	}
	private int Brightness(int value, int offsetColor){
		if(offsetColor == 0){
			return value;
		}
		int range = value/32;
		int base = value%32;
		int newColor = base+offsetColor;
		if(newColor < 1){
			newColor = 1;
		} else if (newColor >30){
			newColor = 30;
		}
		return newColor+(range*32);
	}
	private int Shift(int value, int offsetColor){
		if(offsetColor == 0){
			return value;
		}
		int newColor = value+offsetColor;
		if(newColor>255){
			newColor-=256;
		}else if(newColor<0){
			newColor+=256;
		}
		return newColor;
	}
}