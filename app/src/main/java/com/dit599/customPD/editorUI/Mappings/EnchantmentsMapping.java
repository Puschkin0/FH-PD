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

import com.dit599.customPD.items.weapon.Weapon.Enchantment;
import com.dit599.customPD.items.weapon.enchantments.*;

public abstract class EnchantmentsMapping {
	private static List<Class<? extends Enchantment>> enchantmentsclasslist = null;
	private static List<String> enchantmentsnamelist = null;

	
	public static void EnchantmentsMappingInit() {
		enchantmentsclasslist = new ArrayList<Class<? extends Enchantment>>();
		enchantmentsclasslist.add(null);
		enchantmentsclasslist.add(Death.class);
		enchantmentsclasslist.add(Fire.class);
		enchantmentsclasslist.add(Horror.class);
		enchantmentsclasslist.add(Instability.class);
		enchantmentsclasslist.add(Leech.class);
		enchantmentsclasslist.add(Luck.class);
		enchantmentsclasslist.add(Paralysis.class);
		enchantmentsclasslist.add(Piercing.class);
		enchantmentsclasslist.add(Poison.class);
		enchantmentsclasslist.add(Slow.class);
		enchantmentsclasslist.add(Swing.class);
		enchantmentsclasslist.add(Shock.class);
		enchantmentsclasslist.add(Tempering.class);

		enchantmentsnamelist = new ArrayList<String>();
		enchantmentsnamelist.add("No Enchant");
		enchantmentsnamelist.add("Grim");
		enchantmentsnamelist.add("Blazing");
		enchantmentsnamelist.add("Eldritch");
		enchantmentsnamelist.add("Unstable");
		enchantmentsnamelist.add("Vampiric");
		enchantmentsnamelist.add("Lucky");
		enchantmentsnamelist.add("Stunning");
		enchantmentsnamelist.add("Piercing");
		enchantmentsnamelist.add("Venomous");
		enchantmentsnamelist.add("Chilling");
		enchantmentsnamelist.add("Wild");
		enchantmentsnamelist.add("Shocking");
		enchantmentsnamelist.add("Tempered");

	}

	public static String getEnchantmentName(Class<? extends Enchantment> enchantment) {
		
		for(int i=0;i<enchantmentsclasslist.size();i++)
		 {
			if(enchantmentsclasslist.get(i) != null && enchantmentsclasslist.get(i).getName().equals(enchantment.getName()))
			{
				return enchantmentsnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Enchantment> getEnchantmentClass(String enchantmentname) {
		for(int i=0;i<enchantmentsnamelist.size();i++)
		 {
			if(enchantmentsnamelist.get(i).equals(enchantmentname))
			{
				return enchantmentsclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return enchantmentsnamelist;
		
	}
}
