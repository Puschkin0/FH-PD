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
import com.dit599.customPD.items.rings.*;

public abstract class RingMapping {
	private static List<Class<? extends Item>> ringclasslist = null;
	private static List<String> ringnamelist = null;

	public static void ringMappingInit() {
		ringclasslist = new ArrayList<Class<? extends Item>>();
		ringclasslist.add(RingOfAccuracy.class);
		ringclasslist.add(RingOfDetection.class);
		ringclasslist.add(RingOfElements.class);
		ringclasslist.add(RingOfEvasion.class);
		ringclasslist.add(RingOfHaggler.class);
		ringclasslist.add(RingOfHaste.class);
		ringclasslist.add(RingOfHerbalism.class);
		ringclasslist.add(RingOfMending.class);
		ringclasslist.add(RingOfPower.class);
		ringclasslist.add(RingOfSatiety.class);
		ringclasslist.add(RingOfShadows.class);
		ringclasslist.add(RingOfThorns.class);

		ringnamelist = new ArrayList<String>();
		ringnamelist.add("Ring of Accuracy");
		ringnamelist.add("Ring of Detection");
		ringnamelist.add("Ring of Elements");
		ringnamelist.add("Ring of Evasion");
		ringnamelist.add("Ring of Haggler");
		ringnamelist.add("Ring of Haste");
		ringnamelist.add("Ring of Herbalism");
		ringnamelist.add("Ring of Mending");
		ringnamelist.add("Ring of Power");
		ringnamelist.add("Ring of Satiety");
		ringnamelist.add("Ring of Shadows");
		ringnamelist.add("Ring of Thorns");
	}

	public static String getRingName(Class<? extends Item> ring) {
		for(int i=0;i<ringclasslist.size();i++)
		 {
			if(ringclasslist.get(i).getName().equals(ring.getName()))
			{
				return ringnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getRingClass(String ringname) {
		for(int i=0;i<ringnamelist.size();i++)
		 {
			if(ringnamelist.get(i).equals(ringname))
			{
				return ringclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return ringnamelist;
		
	}
}
