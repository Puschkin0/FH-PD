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
package com.dit599.customPD.editorUI.Mappings;

import java.util.ArrayList;
import java.util.List;

import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.*;

public abstract class PotionMapping {
	private static List<Class<? extends Item>> potionclasslist = null;
	private static List<String> potionnamelist = null;

	public static void potionMappingInit() {
		potionclasslist = new ArrayList<Class<? extends Item>>();
		potionclasslist.add(PotionOfHealing.class);
		potionclasslist.add(PotionOfExperience.class);
		potionclasslist.add(PotionOfStrength.class);
		potionclasslist.add(PotionOfMight.class);
		potionclasslist.add(PotionOfLiquidFlame.class);
		potionclasslist.add(PotionOfFrost.class);
		potionclasslist.add(PotionOfInvisibility.class);
		potionclasslist.add(PotionOfLevitation.class);
		potionclasslist.add(PotionOfMindVision.class);
		potionclasslist.add(PotionOfParalyticGas.class);
		potionclasslist.add(PotionOfToxicGas.class);
		potionclasslist.add(PotionOfPurity.class);

		potionnamelist = new ArrayList<String>();
		potionnamelist.add("Healing Potion");
		potionnamelist.add("Experience Potion");
		potionnamelist.add("Strength Potion");
		potionnamelist.add("Might Potion");
		potionnamelist.add("Liquid Flame Potion");
		potionnamelist.add("Frost Potion");
		potionnamelist.add("Invisibility Potion");
		potionnamelist.add("Levitation Potion");
		potionnamelist.add("Mind Vision Potion");
		potionnamelist.add("Paralytic Gas Potion");
		potionnamelist.add("Toxic Gas Potion");
		potionnamelist.add("Purity Potion");
	}

	public static String getPotionName(Class<? extends Item> potion) {
		for(int i=0;i<potionclasslist.size();i++)
		 {
			if(potionclasslist.get(i).getName().equals(potion.getName()))
			{
				return potionnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getPotionClass(String potionname) {
		for(int i=0;i<potionnamelist.size();i++)
		 {
			if(potionnamelist.get(i).equals(potionname))
			{
				return potionclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return potionnamelist;
		
	}
}
