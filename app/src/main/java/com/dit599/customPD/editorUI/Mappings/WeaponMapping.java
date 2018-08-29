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
import com.dit599.customPD.items.weapon.melee.*;
import com.dit599.customPD.items.weapon.missiles.Boomerang;

public abstract class WeaponMapping {
	private static List<Class<? extends Item>> weaponclasslist = null;
	private static List<String> weaponnamelist = null;

	public static void weaponMappingInit() {
		weaponclasslist = new ArrayList<Class<? extends Item>>();
		weaponclasslist.add(Dagger.class);
		weaponclasslist.add(ShortSword.class);
		weaponclasslist.add(Knuckles.class);
		weaponclasslist.add(Boomerang.class);
		weaponclasslist.add(Quarterstaff.class);
		weaponclasslist.add(Spear.class);
		weaponclasslist.add(Sword.class);
		weaponclasslist.add(Mace.class);
		weaponclasslist.add(Longsword.class);
		weaponclasslist.add(BattleAxe.class);
		weaponclasslist.add(Glaive.class);
		weaponclasslist.add(WarHammer.class);

		weaponnamelist = new ArrayList<String>();
		weaponnamelist.add("Dagger");
		weaponnamelist.add("Shortsword");
		weaponnamelist.add("Knuckleduster");
		weaponnamelist.add("Boomerang");
		weaponnamelist.add("Quarterstaff");
		weaponnamelist.add("Spear");
		weaponnamelist.add("Sword");
		weaponnamelist.add("Mace");
		weaponnamelist.add("Longsword");
		weaponnamelist.add("Battle Axe");
		weaponnamelist.add("Glaive");
		weaponnamelist.add("War Hammer");
	}

	public static String getWeaponName(Class<? extends Item> weapon) {
		for(int i=0;i<weaponclasslist.size();i++)
		 {
			if(weaponclasslist.get(i).getName().equals(weapon.getName()))
			{
				return weaponnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getWeaponClass(String weaponname) {
		for(int i=0;i<weaponnamelist.size();i++)
		 {
			if(weaponnamelist.get(i).equals(weaponname))
			{
				return weaponclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return weaponnamelist;
		
	}
}
