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

import com.dit599.customPD.levels.*;

public abstract class LevelMapping {
	private static List<Class<? extends Level>> themeclasslist = null;
	private static List<String> themenamelist = null;

	public static void levelMappingInit() {
		themeclasslist = new ArrayList<Class<? extends Level>>();
		//themeclasslist.add(DeadEndLevel.class);
		themeclasslist.add(SewerLevel.class);
		themeclasslist.add(SewerBossLevel.class);
		themeclasslist.add(PrisonLevel.class);
		themeclasslist.add(PrisonBossLevel.class);
		themeclasslist.add(CavesLevel.class);
		themeclasslist.add(CavesBossLevel.class);
		themeclasslist.add(CityLevel.class);
		themeclasslist.add(CityBossLevel.class);
		themeclasslist.add(HallsLevel.class);
		themeclasslist.add(HallsBossLevel.class);
		//themeclasslist.add(LastLevel.class);
		//themeclasslist.add(LastShopLevel.class);
		//themeclasslist.add(RegularLevel.class);
		//themeclasslist.add(TutorialBossLevel.class);
		//themeclasslist.add(TutorialLevel.class);

		themenamelist = new ArrayList<String>();
		//themenamelist.add("DeadEndLevel");
		themenamelist.add("Sewer Level");
		themenamelist.add("Sewer Boss Level");
		themenamelist.add("Prison Level");
		themenamelist.add("Prison Boss Level");
		themenamelist.add("Caves Level");
		themenamelist.add("Caves Boss Level");
		themenamelist.add("City Level");
		themenamelist.add("City Boss Level");
		themenamelist.add("Halls Level");
		themenamelist.add("Halls Boss Level");
		//themenamelist.add("LastLevel");
		//themenamelist.add("LastShopLevel");
		//themenamelist.add("RegularLevel");
		//themenamelist.add("TutorialBossLevel");
		//themenamelist.add("TutorialLevel");

	}

	public static String getThemeName(Class<? extends Level> theme) {
		for(int i=0;i<themeclasslist.size();i++)
		 {
			if(themeclasslist.get(i).getName().equals(theme.getName()))
			{
				return themenamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Level> getThemeClass(String themename) {
		for(int i=0;i<themenamelist.size();i++)
		 {
			if(themenamelist.get(i).equals(themename))
			{
				return themeclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return themenamelist;
		
	}
}
