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

import com.dit599.customPD.items.*;
import com.dit599.customPD.items.bags.*;
import com.dit599.customPD.items.food.*;
import com.dit599.customPD.items.keys.SkeletonKey;
import com.dit599.customPD.items.weapon.missiles.*;
import com.dit599.customPD.levels.painters.BossExitPainter;
import com.dit599.customPD.plants.*;


public abstract class ConsumableMapping {
	private static List<Class<? extends Item>> consumableclasslist = null;
	private static List<String> consumablenamelist = null;

	public static void consumableMappingInit() {
		consumableclasslist = new ArrayList<Class<? extends Item>>();
		consumableclasslist.add(Ankh.class);
		consumableclasslist.add(DewVial.class);
		consumableclasslist.add(Gold.class);
		consumableclasslist.add(Food.class);
		consumableclasslist.add(MysteryMeat.class);
		consumableclasslist.add(Pasty.class);
		consumableclasslist.add(ScrollHolder.class);
		consumableclasslist.add(SeedPouch.class);
		consumableclasslist.add(WandHolster.class);
		consumableclasslist.add(ArmorKit.class);
		consumableclasslist.add(Weightstone.class);
		consumableclasslist.add(LloydsBeacon.class);
		consumableclasslist.add(Stylus.class);
		consumableclasslist.add(TomeOfMastery.class);
		consumableclasslist.add(Torch.class);
		consumableclasslist.add(Dart.class);
		consumableclasslist.add(IncendiaryDart.class);
		consumableclasslist.add(CurareDart.class);
		consumableclasslist.add(Shuriken.class);
		consumableclasslist.add(Javelin.class);
		consumableclasslist.add(Tamahawk.class);
		consumableclasslist.add(SkeletonKey.class);
		consumableclasslist.add(Bomb.class);
		consumableclasslist.add(Honeypot.class);

		consumablenamelist = new ArrayList<String>();
		consumablenamelist.add("Ankh");
		consumablenamelist.add("Dew Vial");
		consumablenamelist.add("Gold");
		consumablenamelist.add("Food");
		consumablenamelist.add("Mystery Meat");
		consumablenamelist.add("Pasty");
		consumablenamelist.add("Scroll Holder");
		consumablenamelist.add("Seed Pouch");
		consumablenamelist.add("Wand Holster");
		consumablenamelist.add("Armor Kit");
		consumablenamelist.add("Weightstone");
		consumablenamelist.add("Lloyd's Beacon");
		consumablenamelist.add("Arcane Stylus");
		consumablenamelist.add("Tome of Mastery");
		consumablenamelist.add("Torch");
		consumablenamelist.add("Dart");
		consumablenamelist.add("Incendiary Dart");
		consumablenamelist.add("Curare Dart");
		consumablenamelist.add("Shuriken");
		consumablenamelist.add("Javelin");
		consumablenamelist.add("Tomahawk");
		consumablenamelist.add("Skeleton Key");
		consumablenamelist.add("Bomb");
		consumablenamelist.add("Honey Pot");
	}

	public static String getConsumableName(Class<? extends Item> consumable) {
		for(int i=0;i<consumableclasslist.size();i++)
		 {
			if(consumableclasslist.get(i).getName().equals(consumable.getName()))
			{
				return consumablenamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getConsumableClass(String consumablename) {
		for(int i=0;i<consumablenamelist.size();i++)
		 {
			if(consumablenamelist.get(i).equals(consumablename))
			{
				return consumableclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		if(consumablenamelist == null){
			consumableMappingInit();
		}
		return consumablenamelist;
		
	}
}
