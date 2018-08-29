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
import com.dit599.customPD.items.armor.*;

public abstract class ArmorMapping {
	private static List<Class<? extends Item>> armorclasslist = null;
	private static List<String> armornamelist = null;

	public static void armorMappingInit() {
		armorclasslist = new ArrayList<Class<? extends Item>>();
		armorclasslist.add(ClothArmor.class);
		armorclasslist.add(LeatherArmor.class);
		armorclasslist.add(MailArmor.class);
		armorclasslist.add(ScaleArmor.class);
		armorclasslist.add(PlateArmor.class);

		armornamelist = new ArrayList<String>();
		armornamelist.add("Cloth Armor");
		armornamelist.add("Leather Armor");
		armornamelist.add("Mail Armor");
		armornamelist.add("Scale Armor");
		armornamelist.add("Plate Armor");
	}

	public static String getArmorName(Class<? extends Item> armor) {
		for(int i=0;i<armorclasslist.size();i++)
		 {
			if(armorclasslist.get(i).getName().equals(armor.getName()))
			{
				return armornamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getArmorClass(String armorname) {
		for(int i=0;i<armornamelist.size();i++)
		 {
			if(armornamelist.get(i).equals(armorname))
			{
				return armorclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return armornamelist;
		
	}
}
