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
import com.dit599.customPD.items.wands.*;
import com.dit599.customPD.levels.template.LevelTemplate.MagicItem;

public abstract class WandMapping {
	private static List<Class<? extends Item>> wandclasslist = null;
	private static List<String> wandnamelist = null;

	public static void wandMappingInit() {
		wandclasslist = new ArrayList<Class<? extends Item>>();
		wandclasslist.add(WandOfAmok.class);
		wandclasslist.add(WandOfAvalanche.class);
		wandclasslist.add(WandOfBlink.class);
		wandclasslist.add(WandOfDisintegration.class);
		wandclasslist.add(WandOfFirebolt.class);
		wandclasslist.add(WandOfFlock.class);
		wandclasslist.add(WandOfLightning.class);
		wandclasslist.add(WandOfMagicMissile.class);
		wandclasslist.add(WandOfPoison.class);
		wandclasslist.add(WandOfRegrowth.class);
		wandclasslist.add(WandOfSlowness.class);
		wandclasslist.add(WandOfTeleportation.class);
		wandclasslist.add(WandOfTelekinesis.class);

		wandnamelist = new ArrayList<String>();
		wandnamelist.add("Wand of Amok");
		wandnamelist.add("Wand of Avalanche");
		wandnamelist.add("Wand of Blink");
		wandnamelist.add("Wand of Disintegration");
		wandnamelist.add("Wand of Firebolt");
		wandnamelist.add("Wand of Flock");
		wandnamelist.add("Wand of Lightning");
		wandnamelist.add("Wand of Magic Missile");
		wandnamelist.add("Wand of Poison");
		wandnamelist.add("Wand of Regrowth");
		wandnamelist.add("Wand of Slowness");
		wandnamelist.add("Wand of Teleportation");
		wandnamelist.add("Wand of Telekinesis");
	}

	public static String getWandName(Class<? extends Item> wand) {
		for(int i=0;i<wandclasslist.size();i++)
		 {
			if(wandclasslist.get(i).getName().equals(wand.getName()))
			{
				return wandnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getWandClass(String wandname) {
		for(int i=0;i<wandnamelist.size();i++)
		 {
			if(wandnamelist.get(i).equals(wandname))
			{
				return wandclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return wandnamelist;
		
	}
}
