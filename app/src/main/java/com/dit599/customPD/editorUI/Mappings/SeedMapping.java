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
import com.dit599.customPD.plants.*;


public abstract class SeedMapping {
	private static List<Class<? extends Item>> seedclasslist = null;
	private static List<String> seednamelist = null;

	public static void seedMappingInit() {
		seedclasslist = new ArrayList<Class<? extends Item>>();
		seedclasslist.add(Dreamweed.Seed.class);
		seedclasslist.add(Earthroot.Seed.class);
		seedclasslist.add(Fadeleaf.Seed.class);
		seedclasslist.add(Firebloom.Seed.class);
		seedclasslist.add(Icecap.Seed.class);
		seedclasslist.add(Sorrowmoss.Seed.class);
		seedclasslist.add(Sungrass.Seed.class);

		seednamelist = new ArrayList<String>();
		seednamelist.add("Dreamweed Seed");
		seednamelist.add("Earthroot Seed");
		seednamelist.add("Fadeleaf Seed");
		seednamelist.add("Firebloom Seed");
		seednamelist.add("Icecap Seed");
		seednamelist.add("Sorrowmoss Seed");
		seednamelist.add("Sungrass Seed");
	}

	public static String getSeedName(Class<? extends Item> seed) {
		for(int i=0;i<seedclasslist.size();i++)
		 {
			if(seedclasslist.get(i).getName().equals(seed.getName()))
			{
				return seednamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Item> getSeedClass(String consumablename) {
		for(int i=0;i<seednamelist.size();i++)
		 {
			if(seednamelist.get(i).equals(consumablename))
			{
				return seedclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return seednamelist;
		
	}
}
