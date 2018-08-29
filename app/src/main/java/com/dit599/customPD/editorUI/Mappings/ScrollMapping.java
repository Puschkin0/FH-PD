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
import com.dit599.customPD.items.scrolls.*;

public abstract class ScrollMapping {
	private static List<Class<? extends Item>> scrollclasslist = null;
	private static List<String> scrollnamelist = null;

	public static void scrollMappingInit() {
		scrollclasslist = new ArrayList<Class<? extends Item>>();
		scrollclasslist.add(ScrollOfIdentify.class);
		scrollclasslist.add(ScrollOfRemoveCurse.class);
		scrollclasslist.add(ScrollOfMagicMapping.class);
		scrollclasslist.add(ScrollOfUpgrade.class);
		scrollclasslist.add(ScrollOfEnchantment.class);
		scrollclasslist.add(ScrollOfWeaponUpgrade.class);
		scrollclasslist.add(ScrollOfRecharging.class);
		scrollclasslist.add(ScrollOfTeleportation.class);
		scrollclasslist.add(ScrollOfChallenge.class);
		scrollclasslist.add(ScrollOfLullaby.class);
		scrollclasslist.add(ScrollOfTerror.class);
		scrollclasslist.add(ScrollOfMirrorImage.class);
		scrollclasslist.add(ScrollOfPsionicBlast.class);

		scrollnamelist = new ArrayList<String>();
		scrollnamelist.add("Scroll of Identify");
		scrollnamelist.add("Scroll of Remove Curse");
		scrollnamelist.add("Scroll of Magic Mapping");
		scrollnamelist.add("Scroll of Upgrade");
		scrollnamelist.add("Scroll of Enchantment");
		scrollnamelist.add("Scroll of Weapon Upgrade");
		scrollnamelist.add("Scroll of Recharging");
		scrollnamelist.add("Scroll of Teleportation");
		scrollnamelist.add("Scroll of Challenge");
		scrollnamelist.add("Scroll of Lullaby");
		scrollnamelist.add("Scroll of Terror");
		scrollnamelist.add("Scroll of Mirror Image");
		scrollnamelist.add("Scroll of Psionic Blast");
	}

	public static String getScrollName(Class<? extends Item> scroll) {
		for(int i=0;i<scrollclasslist.size();i++)
		 {
			if(scrollclasslist.get(i).getName().equals(scroll.getName()))
			{
				return scrollnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getScrollClass(String scrollname) {
		for(int i=0;i<scrollnamelist.size();i++)
		 {
			if(scrollnamelist.get(i).equals(scrollname))
			{
				return scrollclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return scrollnamelist;
		
	}
}
