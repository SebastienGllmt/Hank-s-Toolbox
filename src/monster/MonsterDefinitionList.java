package monster;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import shared.LoonylandConstant;

public class MonsterDefinitionList {
	public static MonsterDefinition[] allDefinitions;

	public boolean BuildMonsterDefinition() {
		allDefinitions = GetMonsterDefinition(LoonylandConstant.LOONYLANDEXE);
		if (allDefinitions == null) {
			return false;
		}

		for (int i = 0; i < allDefinitions.length; i++) {
			if (allDefinitions[i].getRefIndex() != 0) {
				allDefinitions[i].setJspName(allDefinitions[allDefinitions[i].getRefIndex() - 1].getJspName(false));
			}
		}
		return true;
	}

	public int getIndexByName(String searchedName, boolean trimmed) { //finds which id the monster has using its name
		for (int i = 0; i < allDefinitions.length; i++) {
			String name = allDefinitions[i].getName(trimmed);
			if (name.equalsIgnoreCase(searchedName)) {
				return i;
			}
		}
		//If it can't find it
		System.out.println("No such monster could be found");
		return 0;
	}
	
	public String getNameByIndex(int id, boolean trimmed){
		String name = allDefinitions[id].getName(trimmed);
		return name;
	}

	public String[] getSortedNameArray(boolean trimmed) {
		String[] monstersNames = new String[allDefinitions.length];

		for (int i = 0; i < allDefinitions.length; i++) {
			monstersNames[i] = allDefinitions[i].getName(trimmed);
		}
		Arrays.sort(monstersNames);
		return monstersNames;
	}

	private MonsterDefinition[] GetMonsterDefinition(String filename) {
		FileInputStream in = null;
		int count = 0;
		int monsterLength = LoonylandConstant.MONSTER_LENGTH;
		int[] def = new int[monsterLength];
		int versionID = getVersionNumber(filename);
		int startByte;
		int numberOfMonsters;
		MonsterDefinition[] allMonsters;
		if(versionID == 0 || versionID == 1){
			numberOfMonsters = LoonylandConstant.MONSTER_NUMBER_LIST[versionID]; //Gets number of monsters depending version
			startByte = LoonylandConstant.MONSTER_START_BYTE[versionID]; //gets byte where monsters are held
			allMonsters = new MonsterDefinition[numberOfMonsters];
		} else{
			return null;
		}

		try {
			in = new FileInputStream(filename);
			int c;

			while ((c = in.read()) != -1) {
				if (count >= startByte && count <= (startByte + (monsterLength * numberOfMonsters))) {
					int byteIndex = (count - startByte) % monsterLength;
					int monsterID = (count - startByte) / monsterLength;

					def[byteIndex] = c;
					if (byteIndex == monsterLength - 1) {

						MonsterDefinition m = new MonsterDefinition(def);
						allMonsters[monsterID] = m;
					}

				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return allMonsters;
	}

	static int getVersionNumber(String filename) { //Gets what version of the game you're playing
		FileInputStream in = null;
		int count = 0;
		String version = "";
		String version2 = "";
		int versionID = -1;
		try {
			in = new FileInputStream(filename);
			int c;

			while ((c = in.read()) != -1) {
				if (count >= 704060 && count < 704072) { //the range in which the text Version 1.0M should be held
					char txt = (char) c;
					version = version + txt;
				}
				if (count >= 710668 && count < 710681) { //the range in which the text Version 1.2CE should be held
					char txt2 = (char) c;
					version2 = version2 + txt2;
				} else if (count >= 710681) {
					if (version2.equalsIgnoreCase("Version 1.2CE")) {
						versionID = 0;
					}
					else if (version.equalsIgnoreCase("Version 1.0M")) {
							versionID = 1;
					}
					break;
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return versionID;
	}
}
