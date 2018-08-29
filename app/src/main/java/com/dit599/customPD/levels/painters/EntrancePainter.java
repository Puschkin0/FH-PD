/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.dit599.customPD.levels.painters;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.PotionOfExperience;
import com.dit599.customPD.items.potions.PotionOfHealing;
import com.dit599.customPD.items.potions.PotionOfStrength;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;

public class EntrancePainter extends Painter {

	/**
	 * Modified with tutorial clauses so that all doors to this room are hidden when 
	 * on floor 2 in tutorial mode. Additionally when in tutorialmode certain items
	 * will appear in the entrance room depending on which floor. 
	 */
	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		for (Room.Door door : room.connected.values()) {
			if(Dungeon.isTutorial && Dungeon.depth == 2){
				door.set( Room.Door.Type.HIDDEN );
			}
			else{
				door.set( Room.Door.Type.REGULAR );
			}
		}
		do{
			level.entrance = room.random( 1 );
		}while (level.map[level.entrance] == Terrain.SIGN);
		set( level, level.entrance, Terrain.ENTRANCE );

		if(Dungeon.isTutorial){
			if(Dungeon.depth == 2){
				Item [] items = {
						new PotionOfStrength()
				};
				placeItems(items, Terrain.EMPTY, level, room);
			}
			else if(Dungeon.depth == 3){
				Item [] items = {
						new PotionOfHealing()
				};
				placeItems(items, Terrain.EMPTY, level, room);
			}
			else if (Dungeon.depth == 4){
				Item [] items = {
						new PotionOfExperience()
				};
				placeItems(items, Terrain.EMPTY, level, room);
			}
		}
	}

}
