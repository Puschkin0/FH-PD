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
package com.dit599.customPD.levels.painters;

import com.dit599.customPD.actors.mobs.Crab;
import com.dit599.customPD.actors.mobs.Mob;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;

/**
 * Paints a room that contains 3 crabs (which are quite strong compared 
 * too the expected level of the player on the tutorial floor this room
 * is placed on).
 */
public class HardHittingRoomPainter extends Painter {

	private static final int NMOBS	= 4;
	
	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.WATER );
		
		Room.Door door = room.entrance(); 
		door.set( Room.Door.Type.REGULAR );
		
		int x = -1;
		int y = -1;
		if (door.x == room.left) {
			
			x = room.right - 1;
			y = room.top + room.height() / 2;
			
		} else if (door.x == room.right) {
			
			x = room.left + 1;
			y = room.top + room.height() / 2;
			
		} else if (door.y == room.top) {
			
			x = room.left + room.width() / 2;
			y = room.bottom - 1;
			
		} else if (door.y == room.bottom) {
			
			x = room.left + room.width() / 2;
			y = room.top + 1;
			
		}
		
		int pos = x + y * Level.WIDTH;
		set( level, pos, Terrain.SIGN );
		Mob [] mobs = new Mob[NMOBS];
		for (int i=0; i < NMOBS; i++) {
			mobs[i] = new Crab();
		}
		placeMobs(mobs, Terrain.WATER, level, room);
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "The penalty for wearing too heavy armor (getting " +
				"hungry faster and taking more turns for every movement) are only activated when moving, so while standing still " +
				"there is no downside. However, armor does nothing against magical damage.";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Hard-Hitting Enemies\n\n" +
				"The best way to take reduced damage is to wear good armor and to stand in a position where as few " +
				"enemies as possible can attack you at the same time. Equip your heaviest armor to survive!";
	}
}
