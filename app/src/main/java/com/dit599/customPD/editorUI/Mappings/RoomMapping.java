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

public abstract class RoomMapping {
	private static List<Room.Type> roomtypelist = null;
	private static List<String> roomnamelist = null;

	public static void roomMappingInit() {
		roomtypelist = new ArrayList<Room.Type>();
		roomtypelist.add(Room.Type.ARMORY);
		roomtypelist.add(Room.Type.CRYPT);
		roomtypelist.add(Room.Type.GARDEN);
		roomtypelist.add(Room.Type.LABORATORY);
		roomtypelist.add(Room.Type.LIBRARY);
		roomtypelist.add(Room.Type.T_WELL_IDENTIFY);
		roomtypelist.add(Room.Type.T_WELL_HEALTH);
		roomtypelist.add(Room.Type.T_WELL_TRANSMUTE);
		roomtypelist.add(Room.Type.POOL);
		roomtypelist.add(Room.Type.SHOP); 
		roomtypelist.add(Room.Type.STATUE);
		roomtypelist.add(Room.Type.STORAGE);
		roomtypelist.add(Room.Type.TRAPS);
		roomtypelist.add(Room.Type.TREASURY);
		roomtypelist.add(Room.Type.VAULT);

		roomnamelist = new ArrayList<String>();
		roomnamelist.add("Armory");
		roomnamelist.add("Crypt");
		roomnamelist.add("Garden");
		roomnamelist.add("Laboratory");
		roomnamelist.add("Library");
		roomnamelist.add("Well of Awareness");
		roomnamelist.add("Well of Health");
		roomnamelist.add("Well of Transmutation");
		roomnamelist.add("Piranha Room");
		roomnamelist.add("Shop");
		roomnamelist.add("Animated Statue Room");
		roomnamelist.add("Storage");
		roomnamelist.add("Trapped Room");
		roomnamelist.add("Treasury");
		roomnamelist.add("Vault");

	}

	public static String getRoomName(Room.Type roomType) {
		for(int i=0;i<roomtypelist.size();i++)
		 {
			if(roomtypelist.get(i).equals(roomType))
			{
				return roomnamelist.get(i);
			}
		 }
		return null;
	}
	public static Room.Type getRoomType(String roomname) {
		for(int i=0;i<roomnamelist.size();i++)
		 {
			if(roomnamelist.get(i).equals(roomname))
			{
				return roomtypelist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return roomnamelist;
		
	}
}
