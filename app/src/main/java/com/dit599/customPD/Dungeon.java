/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.dit599.customPD;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import android.content.Context;
import android.util.Log;

import com.dit599.customPD.actors.Actor;
import com.dit599.customPD.actors.Char;
import com.dit599.customPD.actors.buffs.Amok;
import com.dit599.customPD.actors.buffs.Light;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.actors.hero.HeroClass;
import com.dit599.customPD.actors.mobs.npcs.Blacksmith;
import com.dit599.customPD.actors.mobs.npcs.Ghost;
import com.dit599.customPD.actors.mobs.npcs.Imp;
import com.dit599.customPD.actors.mobs.npcs.Wandmaker;
import com.dit599.customPD.items.Ankh;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.Potion;
import com.dit599.customPD.items.rings.Ring;
import com.dit599.customPD.items.scrolls.Scroll;
import com.dit599.customPD.items.wands.Wand;
import com.dit599.customPD.levels.CavesBossLevel;
import com.dit599.customPD.levels.CavesLevel;
import com.dit599.customPD.levels.CityBossLevel;
import com.dit599.customPD.levels.CityLevel;
import com.dit599.customPD.levels.DeadEndLevel;
import com.dit599.customPD.levels.HallsBossLevel;
import com.dit599.customPD.levels.HallsLevel;
import com.dit599.customPD.levels.LastLevel;
import com.dit599.customPD.levels.LastShopLevel;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.PrisonBossLevel;
import com.dit599.customPD.levels.PrisonLevel;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.SewerBossLevel;
import com.dit599.customPD.levels.SewerLevel;
import com.dit599.customPD.levels.TutorialBossLevel;
import com.dit599.customPD.levels.TutorialLevel;
import com.dit599.customPD.levels.template.DungeonTemplate;
import com.dit599.customPD.levels.template.LevelTemplate;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.scenes.StartScene;
import com.dit599.customPD.ui.QuickSlot;
import com.dit599.customPD.utils.BArray;
import com.dit599.customPD.utils.Utils;
import com.dit599.customPD.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

public class Dungeon {

	private static final String NO_TIPS = "The text  is indecipherable...";
	private static final String[] TIPS = {
		"Don't overestimate your strength, use weapons and armor you can handle.",
		"Not all doors in the dungeon are visible at first sight. If you are stuck, search for hidden doors.",
		"Remember, that raising your strength is not the only way to access better equipment, you can go " +
				"the other way lowering its strength requirement with Scrolls of Upgrade.",
				"You can spend your gold in shops on deeper levels of the dungeon. The first one is on the 6th level.",

				"Beware of Goo!",

				"Pixel-Mart - all you need for successful adventure!",
				"Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
						"when you actually need them.",
						"Being hungry doesn't hurt, but starving does hurt.",
						"Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
								"a closed door when you know it is approaching.",

								"Don't let The Tengu out!",

								"Pixel-Mart. Spend money. Live longer.",
								"When you're attacked by several monsters at the same time, try to retreat behind a door.",
								"If you are burning, you can't put out the fire in the water while levitating.",
								"There is no sense in possessing more than one Ankh at the same time, because you will lose them upon resurrecting.",

								"DANGER! Heavy machinery can cause injury, loss of limbs or death!",

								"Pixel-Mart. A safer life in dungeon.",
								"When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
								"In a Well of Transmutation you can get an item, that cannot be obtained otherwise.",
								"The only way to enchant a weapon is by upgrading it with a Scroll of Weapon Upgrade.",

								"No weapons allowed in the presence of His Majesty!",

								"Pixel-Mart. Special prices for demon hunters!",
								"The text is written in demonic language.",
								"The text is written in demonic language.",
								"The text is written in demonic language."
	};
	/**
	 * This list of strings corresponds to the strings displayed on the sign found in the entrance room
	 * of each of the 4 tutorial floors.
	 */
	private static final String[] T_TIPS = { 
		"Use '?' to get more information about anything you see in the game. In order to enter one " +
				"of the rooms on this floor, you will need to find something that creates fire! Keep an eye " +
				"out for more signs.",
				"From now on, doors may be hidden, so don't forget to use the magnifying glass button from time to time. " +
						"There are two different magical wells on this floor. Make sure to investigate wells with '?' to " +
						"see what type they are. ",
						"Remember to change equipment depending on the situation! If you are wounded, try to find the safe " +
								"resting place on this floor.",
								"Make sure you have cleared the three previous floors, so that you are as prepared as possible for " +
										"this difficult fight! If you need a reminder on the types of potions and scrolls you have discovered, " +
										"press the player portrait in the upper left corner."
	};

	private static final String TXT_DEAD_END = 
			"What are you doing here?!";


	public static int potionOfStrength;
	public static int scrollsOfUpgrade;
	public static int scrollsOfEnchantment;
	public static boolean dewVial;		// true if the dew vial can be spawned
	public static int transmutation;	// depth number for a well of transmutation

	public static int challenges;

	public static Hero hero;
	public static Level level;
	public static DungeonTemplate template = null;

	// Either Item or Class<? extends Item>
	public static Object qsRight;
	public static Object qsLeft;
	public static int depth;
	public static int gold;
	// Reason of death
	public static String resultDescription;

	public static HashSet<Integer> chapters;

	// Hero's field of view
	public static boolean[] visible = new boolean[Level.LENGTH];

	public static boolean nightMode;
	/**
	 * Tutorialmode switch.
	 */
	public static boolean isTutorial = false;
	/**
	 * Used to check if the user has picked up a fire item in the tutorial yet.
	 */
	public static boolean firePrompt = false;
	/**
	 * Used to check if the user has been damaged by a monster in the tutorial yet.
	 */
	public static boolean encounteredMob = false;
	/**
	 * Used to check if the user encountered multiple items stacked in a heap in the
	 * tutorial yet.
	 */
	public static boolean foundHeap = false;
	/**
	 * Used to check if the user has picked up any item in the tutorial yet.
	 */
	public static boolean foundItem = false;
	/**
	 * Used to check if the user has opened the inventory in the tutorial yet.
	 */
	public static boolean invOpened = false;
	/**
	 * Used to check if the user has become hungry in the tutorial yet.
	 */
	public static boolean hungerNotified = false;
	/**
	 * Used to check if the user has started starving in the tutorial yet.
	 */
	public static boolean starvingNotified = false;
	/**
	 * Used to check if the user has picked up a dewdrop in the tutorial yet.
	 */
	public static boolean collectedDrop = false;
	/**
	 * Used to check if 500 milliseconds have passed since the prompt was displayed.
	 */
	public static long timeStamp = 0;

	
	public static SparseArray<ArrayList<Item>> droppedItems;
	
	public static void init() {

		challenges = CustomPD.challenges();

		Actor.clear();

		PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );

		Scroll.initLabels();
		Potion.initColors();
		Wand.initWoods();
		Ring.initGems();

		Statistics.reset();
		Journal.reset();

		depth = 0;
		gold = 0;
		droppedItems = new SparseArray<ArrayList<Item>>();
		potionOfStrength = 0;
		scrollsOfUpgrade = 0;
		scrollsOfEnchantment = 0;
		dewVial = true;
		transmutation = Random.IntRange( 6, 14 );

		chapters = new HashSet<Integer>();

		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		Room.shuffleTypes();

		hero = new Hero();
		hero.live();

		Badges.reset();

		StartScene.curClass.initHero( hero );
	}

	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	/**
	 * Generates the next level. Modified with a tutorial clause which changes what
	 * level type is selected.
	 */
	public static Level newLevel() {

		Dungeon.level = null;
		Actor.clear();

		depth++;
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;

			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		Arrays.fill( visible, false );

		Level level = null;
		if (template != null) {
			try {
				level = LevelTemplate.currentLevelTemplate().theme.newInstance();
			} catch (Exception e){
				level = new LastLevel();
			}
		} else if (isTutorial) {
			switch (depth) {

			case 1:
			case 2:
			case 3:
				level = new TutorialLevel();
				break;
			case 4:
				level = new TutorialBossLevel();
				break;
			default:
				level = new DeadEndLevel();
				Statistics.deepestFloor--;
			}
		} else {
			switch (depth) {
			case 1:
			case 2:
			case 3:
			case 4:
				level = new SewerLevel();
				break;
			case 5:
				level = new SewerBossLevel();
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				level = new PrisonLevel();
				break;
			case 10:
				level = new PrisonBossLevel();
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				level = new CavesLevel();
				break;
			case 15:
				level = new CavesBossLevel();
				break;
			case 16:
			case 17:
			case 18:
			case 19:
				level = new CityLevel();
				break;
			case 20:
				level = new CityBossLevel();
				break;
			case 21:
				level = new LastShopLevel();
				break;
			case 22:
			case 23:
			case 24:
				level = new HallsLevel();
				break;
			case 25:
				level = new HallsBossLevel();
				break;
			case 26:
				level = new LastLevel();
				break;
			default:
				level = new DeadEndLevel();
				Statistics.deepestFloor--;
			}
		}
		level.create();

		Statistics.qualifiedForNoKilling = !bossLevel();

		return level;
	}

	public static void resetLevel() {

		Actor.clear();

		Arrays.fill( visible, false );

		level.reset();
		switchLevel( level, level.entrance );
	}
	/**
	 * Returns the string to display on an EntranceRoom sign.
	 * Modified with a tutorial clause so that it reads from a
	 * different collection of strings in tutorialmode.
	 */
	public static String tip() {

		if (level instanceof DeadEndLevel) {

			return TXT_DEAD_END;

		} else {

			int index = depth - 1;
			if(isTutorial && index < T_TIPS.length){
				return T_TIPS[index];
			}
			else if (!isTutorial && Dungeon.template == null && index < TIPS.length) {
				return TIPS[index];
			} else {
				return NO_TIPS;
			}
		}
	}
	public static boolean shopOnLevel() {
		if(Dungeon.template != null){
			return false;
		}
		return depth == 6 || depth == 11 || depth == 16;
	}

	public static boolean bossLevel() {
		return bossLevel( depth );
	}

	public static boolean bossLevel( int depth ) {
		if(Dungeon.template != null && depth <= Dungeon.template.levelTemplates.size()){
			Class<? extends Level> temp = Dungeon.template.levelTemplates.get(depth-1).theme;
			return temp.equals(SewerBossLevel.class) || temp.equals(PrisonBossLevel.class) || 
					temp.equals(CavesBossLevel.class) || temp.equals(CityBossLevel.class) || temp.equals(HallsBossLevel.class);
		}
		else if(Dungeon.template == null){
			return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
		}
		return true;
	}
	/**
	 * Used when ascending /descending to an existing level. Modified so that nightmode
	 * (increased mob respawn during night hours) is disbled in the tutorial.
	 */
	@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {

		if(Dungeon.isTutorial || Dungeon.template != null){
			nightMode = false;
		}
		else{
			nightMode = new Date().getHours() < 7;
		}

		Dungeon.level = level;
		Actor.init();

		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.add(respawner);
		}

		hero.pos = pos != -1 ? pos : level.exit;

		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		Log.d("In Dungeon", "END OF SAVE/NEW/SWITCH");
		observe();
	}
	
	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = (ArrayList<Item>)Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<Item>() ); 
		}
		dropped.add( item );
	}
	
	public static boolean posNeeded() {
		int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
		return chance( quota, potionOfStrength );
	}
	
	public static boolean souNeeded() {
		int[] quota = {5, 3, 10, 6, 15, 9, 20, 12, 25, 13};
		return chance( quota, scrollsOfUpgrade );
	}
	
	public static boolean soeNeeded() {
		return Random.Int( 12 * (1 + scrollsOfEnchantment) ) < depth;
	}
	private static boolean chance( int[] quota, int number ) {

		for (int i=0; i < quota.length; i += 2) {
			int qDepth = quota[i];
			if (depth <= qDepth) {
				int qNumber = quota[i + 1];
				return Random.Float() < (float)(qNumber - number) / (qDepth - depth + 1);
			}
		}

		return false;
	}
	private static final String RG_GAME_FILE	= "game.dat";
	private static final String RG_DEPTH_FILE	= "depth%d.dat";

	private static final String WR_GAME_FILE	= "warrior.dat";
	private static final String WR_DEPTH_FILE	= "warrior%d.dat";

	private static final String MG_GAME_FILE	= "mage.dat";
	private static final String MG_DEPTH_FILE	= "mage%d.dat";

	private static final String RN_GAME_FILE	= "ranger.dat";
	private static final String RN_DEPTH_FILE	= "ranger%d.dat";

	private static final String T_RG_GAME_FILE	= "tutorial_game.dat";
	private static final String T_RG_DEPTH_FILE	= "tutorial_depth%d.dat";

	private static final String T_WR_GAME_FILE	= "tutorial_warrior.dat";
	private static final String T_WR_DEPTH_FILE	= "tutorial_warrior%d.dat";

	private static final String T_MG_GAME_FILE	= "tutorial_mage.dat";
	private static final String T_MG_DEPTH_FILE	= "tutorial_mage%d.dat";

	private static final String T_RN_GAME_FILE	= "tutorial_ranger.dat";
	private static final String T_RN_DEPTH_FILE	= "tutorial_ranger%d.dat";

	private static final String VERSION		= "version";
	private static final String CHALLENGES	= "challenges";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String QSRIGHT	= "qsRight";
	private static final String QSLEFT	= "qsLeft";
	private static final String LEVEL		= "level";
	private static final String DROPPED		= "dropped%d";
	private static final String POS			= "potionsOfStrength";
	private static final String SOU			= "scrollsOfEnhancement";
	private static final String SOE			= "scrollsOfEnchantment";
	private static final String DV			= "dewVial";
	private static final String WT			= "transmutation";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";

	/**
	 * Returns the filepath of the savegame of a certain heroclass. Has been
	 * modified with tutorialclauses to provide alternative filepaths when in
	 * tutorialmode, thus allowing for separate saves.
	 */
	public static String gameFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			if(isTutorial){
				return T_WR_GAME_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + WR_GAME_FILE;
			}
			else{
				return WR_GAME_FILE;
			}
		case MAGE:
			if(isTutorial){
				return T_MG_GAME_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + MG_GAME_FILE;
			}
			else{
				return MG_GAME_FILE;
			}
		case HUNTRESS:
			if(isTutorial){
				return T_RN_GAME_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + RN_GAME_FILE;
			}
			else{
				return RN_GAME_FILE;
			}
		default:
			if(isTutorial){
				return T_RG_GAME_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + RG_GAME_FILE;
			}
			else{
				return RG_GAME_FILE;
			}
		}
	}
	/**
	 * Returns the filepath of the separate floor details for the savegame of a certain heroclass. Has been
	 * modified with tutorialclauses to provide alternative filepaths when in tutorialmode, thus allowing for 
	 * separate saves.
	 */
	private static String depthFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			if(isTutorial){
				return T_WR_DEPTH_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + WR_DEPTH_FILE;
			}
			else{
				return WR_DEPTH_FILE;
			}
		case MAGE:
			if(isTutorial){
				return T_MG_DEPTH_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + MG_DEPTH_FILE;
			}
			else{
				return MG_DEPTH_FILE;
			}
		case HUNTRESS:
			if(isTutorial){
				return T_RN_DEPTH_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + RN_DEPTH_FILE;
			}
			else{
				return RN_DEPTH_FILE;
			}
		default:
			if(isTutorial){
				return T_RG_DEPTH_FILE;
			}
			else if(Dungeon.template != null){
				return template.name + RG_DEPTH_FILE;
			}
			else{
				return RG_DEPTH_FILE;
			}
		}
	}
	/**
	 * Modified to save the variables added to this class by our modifications.
	 */
	public static void saveGame( String fileName ) throws IOException {
		try {
			OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
			Bundle bundle = new Bundle();

			bundle.put( VERSION, Game.version );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );
			for (int d : droppedItems.keyArray()) {
				bundle.put( String.format( DROPPED, d ), droppedItems.get( d ) );
			}
			bundle.put( POS, potionOfStrength );
			bundle.put( SOU, scrollsOfUpgrade );
			bundle.put( SOE, scrollsOfEnchantment );
			bundle.put( DV, dewVial );
			bundle.put( WT, transmutation );

			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );

			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );

			Room.storeRoomsInBundle( bundle );

			Statistics.storeInBundle( bundle );
			Journal.storeInBundle( bundle );

			if (qsRight instanceof Class) {
				bundle.put( QSRIGHT, ((Class<?>)qsRight).getName() );
			}
			if (qsLeft instanceof Class) {
				bundle.put( QSLEFT, ((Class<?>)qsLeft).getName() );
			}
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Wand.save( bundle );
			Ring.save( bundle );

			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );

			bundle.put("tutorial", isTutorial);
			bundle.put("firePrompt", firePrompt);
			bundle.put("encountered", encounteredMob);
			bundle.put("foundHeap", foundHeap);
			bundle.put("foundItem", foundItem);
			bundle.put("invOpened", invOpened);
			bundle.put("hungerNotified", hungerNotified);
			bundle.put("starvingNotified", starvingNotified);
			bundle.put("collectedDrop", collectedDrop);
			if(template != null){
				bundle.put("dungeonTemplate", template);
			}
			Bundle.write( bundle, output );
			output.close();
			Log.d("SAVING", "Sucessfully saved character.");

		} catch (Exception e) {

			GamesInProgress.setUnknown( hero.heroClass );
		}
	}

	public static void saveLevel() throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );

		OutputStream output = Game.instance.openFileOutput( Utils.format( depthFile( hero.heroClass ), depth ), Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}

	public static void saveAll() throws IOException {
		if (hero.isAlive()) {

			Actor.fixTime();

			saveGame( gameFile( hero.heroClass ) );
			saveLevel();
			
			GamesInProgress.set( hero.heroClass, depth, hero.lvl, challenges != 0 );
			
		} else if (WndResurrect.instance != null) {

			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.causeOfDeath );

		}
	}

	public static void loadGame( HeroClass cl ) throws IOException {
		loadGame( gameFile( cl ), true );
	}

	public static void loadGame( String fileName ) throws IOException {
		loadGame( fileName, false );
	}
	/**
	 * Modified to load the variables added to this class by our modifications.
	 */
	public static void loadGame( String fileName, boolean fullLoad ) throws IOException {

		Bundle bundle = gameBundle( fileName );
		
		if(template != null){
			template = (DungeonTemplate) bundle.get("dungeonTemplate");
		}

		Dungeon.challenges = bundle.getInt( CHALLENGES );

		Dungeon.level = null;
		Dungeon.depth = -1;

		if (fullLoad) {
			PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		}

		Scroll.restore( bundle );
		Potion.restore( bundle );
		Wand.restore( bundle );
		Ring.restore( bundle );

		potionOfStrength = bundle.getInt( POS );
		scrollsOfUpgrade = bundle.getInt( SOU );
		scrollsOfEnchantment = bundle.getInt( SOE );
		dewVial = bundle.getBoolean( DV );
		transmutation = bundle.getInt( WT );
		isTutorial = bundle.getBoolean("tutorial");
		firePrompt = bundle.getBoolean("firePrompt");
		encounteredMob = bundle.getBoolean("encountered");
		foundHeap = bundle.getBoolean("foundHeap");
		foundItem = bundle.getBoolean("foundItem");
		invOpened = bundle.getBoolean("invOpened");
		hungerNotified = bundle.getBoolean("hungerNotified");
		starvingNotified = bundle.getBoolean("starvingNotified");
		collectedDrop = bundle.getBoolean("collectedDrop");
		timeStamp = 0;

		if (fullLoad) {
			chapters = new HashSet<Integer>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}

			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}

			Room.restoreRoomsFromBundle( bundle );
		}

		Bundle badges = bundle.getBundle( BADGES );
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		String qsClass = bundle.getString( QSRIGHT );
		if (qsClass != null) {
			try {
				qsRight = Class.forName( qsClass );
			} catch (ClassNotFoundException e) {
			}
		} else {
			qsRight = null;
		}
		
		
		qsClass = bundle.getString( QSLEFT );
		if (qsClass != null) {
			try {
				qsLeft = Class.forName( qsClass );
			} catch (ClassNotFoundException e) {
			}
		} else {
			qsLeft = null;
		}

		@SuppressWarnings("unused")
		String version = bundle.getString( VERSION );

		hero = null;
		hero = (Hero)bundle.get( HERO );
		
		
		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );

		Statistics.restoreFromBundle( bundle );
		Journal.restoreFromBundle( bundle );
		
		droppedItems = new SparseArray<ArrayList<Item>>();
		for (int i=2; i <= Statistics.deepestFloor + 1; i++) {
			ArrayList<Item> dropped = new ArrayList<Item>();
			for (Bundlable b : bundle.getCollection( String.format( DROPPED, i ) ) ) {
				dropped.add( (Item)b );
			}
			if (!dropped.isEmpty()) {
				droppedItems.put( i, dropped );
			}
		}
	}

	public static Level loadLevel( HeroClass cl ) throws IOException {

		Dungeon.level = null;
		Actor.clear();
		Log.d("FILENAME", Utils.format( depthFile( cl ), depth ));
		InputStream input = Game.instance.openFileInput( Utils.format( depthFile( cl ), depth ) ) ;
		Bundle bundle = Bundle.read( input );
		input.close();

		return (Level)bundle.get( "level" );
	}

	public static void deleteGame( HeroClass cl, boolean deleteLevels ) {

		Game.instance.deleteFile( gameFile( cl ) );

		if (deleteLevels) {
			int depth = 1;
			while (Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
				depth++;
			}
		}

		GamesInProgress.delete( cl );
	}
	public static void deleteGameWithContext( HeroClass cl, boolean deleteLevels,  Context c) {

		c.deleteFile( gameFile( cl ) );

		if (deleteLevels) {
			int depth = 1;
			while (c.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
				depth++;
			}
		}

		GamesInProgress.delete( cl );
	}

	public static Bundle gameBundle( String fileName ) throws IOException {

		InputStream input = Game.instance.openFileInput( fileName );
		Bundle bundle = Bundle.read( input );
		input.close();
		boolean b = bundle != null;
		return bundle;
	}

	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.challenges = (bundle.getInt( CHALLENGES ) != 0);
		if (info.depth == -1) {
			info.depth = bundle.getInt( "maxDepth" );	// FIXME
		}
		Hero.preview( info, bundle.getBundle( HERO ) );
	}

	public static void fail( String desc ) {
		resultDescription = desc;
		if (hero.belongings.getItem( Ankh.class ) == null) { 
			Rankings.INSTANCE.submit( false );
		}
	}

	public static void win( String desc ) {
		
		hero.belongings.identify();
		if (challenges != 0) {
			Badges.validateChampion();
		}

		resultDescription = desc;
		Rankings.INSTANCE.submit( true );
	}

	public static void observe() {

		if (level == null) {
			return;
		}

		level.updateFieldOfView( hero );
		System.arraycopy( Level.fieldOfView, 0, visible, 0, visible.length );

		BArray.or( level.visited, visible, level.visited );

		GameScene.afterObserve();
	}

	private static boolean[] passable = new boolean[Level.LENGTH];

	public static int findPath( Char ch, int from, int to, boolean pass[], boolean[] visible ) {

		if (Level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Level.avoid[to]) ? to : -1;
		}

		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}

		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}

		return PathFinder.getStep( from, to, passable );

	}

	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {

		if (ch.flying) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}

		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		passable[cur] = true;

		return PathFinder.getStepBack( cur, from, passable );

	}

}
