package com;

public interface LoonylandConstant {
	int[] MONSTER_START_BYTE = {574933,570637};
	//LL2 CE = 574933;
	//LL2 1.0M = 570637
	int MONSTER_LENGTH = 389;
	//Length of bytes used for a monster
	int[] MONSTER_NUMBER_LIST = {96,95};
	//LL2 CE = 96
	//LL2 1.0M = 95
	int MONSTER_NAME_LENGTH = 28;
	int COLOR_SELECTED_BYTE_OFFSET = 32; //in dec
	int COLOR_RESULT_BYTE_OFFSET = 36; //in dec
	int MONSTER_LEVEL = 44;
	int[] MONSTER_HP = {48,49};
	int MONSTER_ARMOR = 50;
	int[] MONSTER_DMG = {51,52};
	int WALKSPEED = 54;
	int ITEM_RARITY = 95;
	int STATSTODISP = 6; //number of stats that can be modified
	
	String LOONYLANDEXE = "loonyland.exe";
	String TOOLBOX_DIRECTORY ="Tools";
	String TOOLDIR = LoonylandConstant.TOOLBOX_DIRECTORY + "/Toolbox/";
	
	static int FIELDHEIGHT = 20; //Constant high for aesthetic feel
	String VERSIONID = "v2.0";
	String MONSTERNAME = "Hank's Toolbox " +  LoonylandConstant.VERSIONID /*+ " — Monster Editor"*/;
	String PROFILENAME = "Hank's Toolbox " +  LoonylandConstant.VERSIONID + " — Profile Editor";
	String TITLENAME = "Hank's Toolbox " +  LoonylandConstant.VERSIONID;
	int[] WINDOWSIZE = {910, 635};
}
