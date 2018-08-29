/*
 * CustomPD
 * Copyright (C) 2014 CustomPD team
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

import com.dit599.customPD.actors.Actor;
import com.dit599.customPD.actors.mobs.Statue;
import com.dit599.customPD.items.keys.IronKey;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Point;

public class TutorialStatuePainter extends Painter {

	/**
	 * Paints a room containing a living statue enemy and a sign.
	 */
	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		Point c = room.center();
		int cx = c.x;
		int cy = c.y;
		
		Room.Door door = room.entrance();
		
		door.set( Room.Door.Type.REGULAR );
		
		if (door.x == room.left) {
			
			fill( level, room.right - 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
			set(level, new Point(room.right - 1, room.top + 1), Terrain.SIGN);
			cx = room.right - 2;
			
		} else if (door.x == room.right) {
			
			fill( level, room.left + 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
			set(level, new Point(room.left + 1, room.top + 1), Terrain.SIGN);
			cx = room.left + 2;
			
		} else if (door.y == room.top) {
			
			fill( level, room.left + 1, room.bottom - 1, room.width() - 1, 1 , Terrain.STATUE );
			set(level, new Point(room.left + 1, room.bottom - 1), Terrain.SIGN);
			cy = room.bottom - 2;
			
		} else if (door.y == room.bottom) {
			
			fill( level, room.left + 1, room.top + 1, room.width() - 1, 1 , Terrain.STATUE );
			set(level, new Point(room.left + 1, room.top + 1), Terrain.SIGN);
			cy = room.top + 2;
			
		}
		
		Statue statue = new Statue();
		statue.pos = cx + cy * Level.WIDTH;
		int pos = cx + (cy+1) * Level.WIDTH;
		level.mobs.add( statue );
		Actor.occupyCell( statue );
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "Statues do not give experience, so only attack if you need its weapon.";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Statue Room\n\n " +
				"In this room you can optionally fight the powerful statue enemy to take its weapon. " +
				"Use '?' on it to see what weapon it carries.";
	}
}
