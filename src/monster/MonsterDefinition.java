package monster;

import shared.LoonylandConstant;

public class MonsterDefinition {
	private String name;
	private int[] colors;
	private int level;
	private int HP1;
	private int HP2;
	private int armor;
	private int dmg1;
	private int dmg2;
	private int speed;
	private int rarity;
	private String jspName;
	private int refIndex;
	
	MonsterDefinition(int[] def) {
		// extract name
		name = extractValue(def, 0, 28);

		// extract stats
		level = def[LoonylandConstant.MONSTER_LEVEL];
		HP1 = def[LoonylandConstant.MONSTER_HP[0]];
		HP2 = def[LoonylandConstant.MONSTER_HP[1]];
		armor = def[LoonylandConstant.MONSTER_ARMOR];
		dmg1 = def[LoonylandConstant.MONSTER_DMG[0]];
		dmg2 = def[LoonylandConstant.MONSTER_DMG[1]];
		speed = def[LoonylandConstant.WALKSPEED];
		rarity = def[LoonylandConstant.ITEM_RARITY];
		
		//extract colors
		colors = new int[4];
		colors[0] = def[LoonylandConstant.COLOR_SELECTED_BYTE_OFFSET];
		colors[1] = def[LoonylandConstant.COLOR_RESULT_BYTE_OFFSET];
		if(colors[0] == 255){
			colors[0] = 0;
			colors[1] = 0;
		}
		colors[2] = -1;
		colors[3] = -1;
		
		String val = extractValue(def, 57, 32); //Get location of monster jsps for all monsters
		if (val.charAt(0) == '!') { //if Monsters takes jsp from another monster
			refIndex = Integer.parseInt((val.substring(1)).trim());
		} else {
			jspName = val;
		}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHP1() {
		return HP1;
	}

	public void setHP1(int hP1) {
		HP1 = hP1;
	}

	public int getHP2() {
		return HP2;
	}

	public void setHP2(int hP2) {
		HP2 = hP2;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getDmg1() {
		return dmg1;
	}

	public void setDmg1(int dmg1) {
		this.dmg1 = dmg1;
	}

	public int getDmg2() {
		return dmg2;
	}
	
	public int getSpeed(){
		return speed;
	}

	public void setDmg2(int dmg2) {
		this.dmg2 = dmg2;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public int getRefIndex() {
		return refIndex;
	}

	public void setRefIndex(int refIndex) {
		this.refIndex = refIndex;
	}

	public String getName(boolean trimmed) {
		String fullName = name;
		if(trimmed){
			fullName = fullName.trim();
		}
		return fullName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public String getJspName(boolean trimmed) {
		String fullName = jspName;
		if(trimmed){
			fullName = fullName.trim();
		}
		return fullName;
	}

	public void setJspName(String jspName) {
		this.jspName = jspName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name:" + name);
		sb.append("Color 1: " + colors[0] + " ");
		sb.append("Color 2: " + colors[1] + " ");
		sb.append("Image Name: " + jspName);
		return sb.toString();
	}

	private String extractValue(int[] definition, int start, int length) {
		StringBuffer nameBuffer = new StringBuffer();
		for (int i = start; i < (start + length); i++) {
			if (definition[i] > 0) { //if byte contains a letter
				nameBuffer.append((char) definition[i]);
			} else {
				nameBuffer.append(" "); //Else print blank space that will be trimmed later
			}
		}
		return nameBuffer.toString();
	}

}
