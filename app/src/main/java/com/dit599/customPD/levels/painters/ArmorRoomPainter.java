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

import com.dit599.customPD.actors.mobs.Mob;
import com.dit599.customPD.actors.mobs.Rat;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.armor.LeatherArmor;
import com.dit599.customPD.items.armor.MailArmor;
import com.dit599.customPD.items.armor.PlateArmor;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ArmorRoomPainter extends Painter {

	/**
	 * Paints a room that contains 3 (static) different unidentified armors
	 * and 3 rat enemies.
	 */
	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
		
		Room.Door entrance = room.entrance();
		Point sign = null;
		if (entrance.x == room.left) {
			sign = new Point( room.right-1, Random.Int( 2 ) == 0 ? room.top+1 : room.bottom-1 );
		} else if (entrance.x == room.right) {
			sign = new Point( room.left+1, Random.Int( 2 ) == 0 ? room.top+1 : room.bottom-1 );
		} else if (entrance.y == room.top) {
			sign = new Point( Random.Int( 2 ) == 0 ? room.left+1 : room.right-1, room.bottom-1 );
		} else if (entrance.y == room.bottom) {
			sign = new Point( Random.Int( 2 ) == 0 ? room.left+1 : room.right-1, room.top+1 );
		}
		if (sign != null) {
			set( level, sign, Terrain.SIGN );
		}
		else{
			set(level, room.center(), Terrain.SIGN );
		}
		Item [] armors = {
			new LeatherArmor(),
			new PlateArmor(),
			new MailArmor()
		};
		Mob [] mobs = {new Rat(), new Rat(), new Rat()};
		armors[0].upgrade();
		armors[0].upgrade();
		armors[2].degrade();
		armors[2].degrade();
		armors[2].cursed = true;
		placeItems(armors, Terrain.EMPTY, level, room);
		placeMobs(mobs, Terrain.EMPTY, level, room);
		entrance.set( Room.Door.Type.REGULAR );
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "Be careful when equipping unidentified armor as it may be cursed, making you " +
				"unable to take it off until it is uncursed. Too heavy armor makes you move slower.";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Armor Room\n\n" +
				"This room contains several unidentified pieces of armor. You can't see if the armor is upgraded/degraded at the " +
				"moment. The required strength to use them efficiently could be less or more than the default " +
				"number displayed on them.";
	}
}
