/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * Copyright (C) 2012-2014 Oleg Dolya
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
package com.dit599.customPD.levels.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.mobs.Mob;
import com.dit599.customPD.actors.mobs.Rat;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.Potion;
import com.dit599.customPD.items.rings.Ring;
import com.dit599.customPD.items.scrolls.Scroll;
import com.dit599.customPD.items.wands.Wand;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.SewerLevel;
import com.dit599.customPD.levels.Level.Feeling;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Data class defining everything customizable in a custom level.
 */
public class LevelTemplate implements Bundlable{
	/** Usually limited to 5-7. */
	public int maxMobs = 4;
	/** Default 50 */
	public float timeToRespawn = 50;

	public Class<? extends Level> theme = SewerLevel.class;
	public Feeling feeling = Feeling.NONE;

	public List<Item> weapons = new ArrayList<Item>();
	public List<Item> armor = new ArrayList<Item>();
	public List<Item> consumables = new ArrayList<Item>();
	public List<Item> seeds = new ArrayList<Item>();
	public List<Class<? extends Potion>> potions = new ArrayList<Class<? extends Potion>>();
	public List<Class<? extends Scroll>> scrolls = new ArrayList<Class<? extends Scroll>>();
	public List<MagicItem> wands = new ArrayList<MagicItem>();
	public List<MagicItem> rings = new ArrayList<MagicItem>();

	public List<Room.Type> specialRooms = new ArrayList<Room.Type>();

	public List<MobProbability> mobs = new ArrayList<MobProbability>(4);

	public LevelTemplate() {
		mobs.add(new MobProbability(Rat.class, 0));
		mobs.add(new MobProbability(Rat.class, 0));
		mobs.add(new MobProbability(Rat.class, 0));
		mobs.add(new MobProbability(Rat.class, 0));
	}

	public LevelTemplate(LevelTemplate copy) {
		this.maxMobs = copy.maxMobs;
		this.timeToRespawn = copy.timeToRespawn;
		try {
			this.theme = (copy.theme.newInstance()).getClass();
			this.weapons= new ArrayList<Item>(copy.weapons);
			this.armor= new ArrayList<Item>(copy.armor);
			this.consumables= new ArrayList<Item>(copy.consumables);
			this.seeds= new ArrayList<Item>(copy.seeds);
			this.potions= new ArrayList<Class<? extends Potion>>(copy.potions);
			this.scrolls= new ArrayList<Class<? extends Scroll>>(copy.scrolls);
			this.wands= new ArrayList<LevelTemplate.MagicItem>(copy.wands);
			this.rings= new ArrayList<LevelTemplate.MagicItem>(copy.rings);
			this.specialRooms= new ArrayList<Room.Type>(copy.specialRooms);
			this.mobs = new ArrayList<LevelTemplate.MobProbability> (copy.mobs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gives you the current level based on Dungeon.depth.
	 *
	 * @return
	 */
	public static LevelTemplate currentLevelTemplate() {
		if (Dungeon.template == null) {
			return null;
		}
		// Depth-1 because levelTemplates are 0-indexed
		return Dungeon.template.levelTemplates.get(Dungeon.depth - 1);
	}

	/**
	 * Returns the class of one of the predefined mobs for this level.
	 */
	public Class<? extends Mob> getRandomMob() {
		int totalWeight = 0;
		for (MobProbability prob : mobs) {
			totalWeight += prob.weight;
		}
		if(totalWeight == 0){
			return null;
		}
		int rnd = Random.Int(totalWeight);
		int index = 0;
		while (rnd >= mobs.get(index).weight) {
			rnd -= mobs.get(index).weight;
			index++;
		}
		return mobs.get(index).mobClass;
	}

	/**
	 * Container class for a single mobtype present in a level.
	 */
	public class MobProbability {
		public Class<? extends Mob> mobClass;
		public int weight;

		public MobProbability(Class<? extends Mob> mobClass, int weight) {
			this.mobClass = mobClass;
			this.weight = weight;
		}
	}
	public static class MagicItem {
		public Class<? extends Item> itemClass;
		public int level;
		public boolean cursed;

		public MagicItem(Class<? extends Item> itemClass, int level, boolean cursed) {
			this.itemClass = itemClass;
			this.level = level;
			this.cursed = cursed;
		}
	}

	public ArrayList<Item> getAllItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(weapons);
		items.addAll(armor);
		items.addAll(consumables);
		items.addAll(seeds);
		
		try {
			for (Class<? extends Item> c : potions) {
				items.add(c.newInstance());
			}
			for (Class<? extends Item> c : scrolls) {
				items.add(c.newInstance());
			}
			for (MagicItem m : wands) {
				Wand w = (Wand) m.itemClass.newInstance();
				w.cursed = m.cursed;
				w.level = m.level;
				if(m.level > 2){
					if(m.level <= 9){//hadcoded Maxcharges in Wand
						w.curCharges = m.level;
						w.maxCharges = m.level;
					}
					else{
						w.curCharges = 9;
						w.maxCharges = 9;
					}
				}
				items.add(w);
			}
			for (MagicItem m : rings) {
				Ring r = (Ring) m.itemClass.newInstance();
				r.cursed = m.cursed;
				r.level = m.level;
				items.add(r);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return items;
	}

	private void saveMobList(Bundle bundle){
		for (int i = 0; i < mobs.size(); i++){
			bundle.put("mobClass" + i, mobs.get(i).mobClass.getName());
			bundle.put("weight" + i, mobs.get(i).weight);
		}
	}
	private void loadMobList(Bundle bundle, ClassLoader cl){
		mobs = new ArrayList<MobProbability>(4);
		int i = 0;
		while (true){
			String s = bundle.getString("mobClass" +i);
			int j = bundle.getInt("weight" + i);
			if(s == ""){
				break;
			}
			else{
				try {
					mobs.add(new MobProbability((Class<? extends Mob>) cl.loadClass(s), j));
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void saveRoomList(Bundle bundle) {
		for (int i = 0; i < specialRooms.size(); i++){
			bundle.put("specialRooms" + i, specialRooms.get(i));
		}
	}
	private void loadRoomList(Bundle bundle, ClassLoader cl){
		specialRooms = new ArrayList<Room.Type>();
		int i = 0;
		while (true){
			Room.Type t = bundle.getEnum("specialRooms" +i, Room.Type.class);
			if(t == Room.Type.NULL){
				break;
			}
			else{
				specialRooms.add(t);
				i++;
			}
		}
	}
	
	private void saveScrollList(Bundle bundle) {
		for (int i = 0; i < scrolls.size(); i++){
			bundle.put("scrolls" + i, scrolls.get(i).getName());
		}
	}
	private void loadScrollList(Bundle bundle, ClassLoader cl){
		scrolls = new ArrayList<Class<? extends Scroll>>();
		int i = 0;
		while (true){
			String s = bundle.getString("scrolls" +i);
			if(s == ""){
				break;
			}
			else{
				try {
					scrolls.add((Class<? extends Scroll>) cl.loadClass(s));
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void savePotionList(Bundle bundle) {
		for (int i = 0; i < potions.size(); i++){
			bundle.put("potions" + i, potions.get(i).getName());
		}
	}
	private void loadPotionList(Bundle bundle, ClassLoader cl){
		potions = new ArrayList<Class<? extends Potion>>();
		int i = 0;
		while (true){
			String s = bundle.getString("potions" +i);
			if(s == ""){
				break;
			}
			else{
				try {
					potions.add((Class<? extends Potion>) cl.loadClass(s));
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void saveRingList(Bundle bundle) {
		for (int i = 0; i < rings.size(); i++){
			bundle.put("ringItemClass" + i, rings.get(i).itemClass.getName());
			bundle.put("ringLevel" + i, rings.get(i).level);
			bundle.put("ringCursed" + i, rings.get(i).cursed);
			
			
		}
	}
	private void loadRingList(Bundle bundle, ClassLoader cl){
		rings = new ArrayList<MagicItem>();
		int i = 0;
		while (true){
			String s = bundle.getString("ringItemClass" +i);
			int l = bundle.getInt("ringLevel" +i);
			boolean b = bundle.getBoolean("ringCursed" +i);
			if(s == ""){
				break;
			}
			else{
				try {
					rings.add(new MagicItem((Class<? extends Ring>) cl.loadClass(s), l, b));
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void saveWandList(Bundle bundle) {
		for (int i = 0; i < wands.size(); i++){
			bundle.put("wandItemClass" + i, wands.get(i).itemClass.getName());
			bundle.put("wandLevel" + i, wands.get(i).level);
			bundle.put("wandCursed" + i, wands.get(i).cursed);
		}
	}
	private void loadWandList(Bundle bundle, ClassLoader cl){
		wands = new ArrayList<MagicItem>();
		int i = 0;
		while (true){
			String s = bundle.getString("wandItemClass" +i);
			int l = bundle.getInt("wandLevel" +i);
			boolean b = bundle.getBoolean("wandCursed" +i);
			if(s == ""){
				break;
			}
			else{
				try {
					wands.add(new MagicItem((Class<? extends Wand>) cl.loadClass(s), l, b));
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		weapons = new ArrayList<Item>();
		armor = new ArrayList<Item>();
		consumables = new ArrayList<Item>();
		seeds = new ArrayList<Item>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		loadMobList(bundle, cl);
		loadRingList(bundle, cl);
		loadWandList(bundle, cl);
		loadPotionList(bundle, cl);
		loadScrollList(bundle, cl);
		loadRoomList(bundle, cl);
		this.maxMobs = bundle.getInt("maxMobs");
		this.timeToRespawn = bundle.getFloat("timeToRespawn");
		try {
			this.armor.addAll((Collection<? extends Item>) bundle.getCollection("armor"));
			this.weapons.addAll((Collection<? extends Item>) bundle.getCollection("weapons"));
			this.consumables.addAll((Collection<? extends Item>) bundle.getCollection("consumables"));
			this.seeds.addAll((Collection<? extends Item>) bundle.getCollection("seeds"));
			this.theme = (Class<? extends Level>) cl.loadClass(bundle.getString("theme"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		saveMobList(bundle);
		saveRingList(bundle);
		saveWandList(bundle);
		savePotionList(bundle);
		saveScrollList(bundle);
		saveRoomList(bundle);
		bundle.put("armor", this.armor);
		bundle.put("weapons", this.weapons);
		bundle.put("consumables", this.consumables);
		bundle.put("seeds", this.seeds);
		bundle.put("maxMobs", this.maxMobs);
		bundle.put("timeToRespawn", this.timeToRespawn);
		bundle.put("theme", this.theme.getName());
	}
}
