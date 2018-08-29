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
import com.dit599.customPD.items.weapon.melee.BattleAxe;
import com.dit599.customPD.items.weapon.melee.Knuckles;
import com.dit599.customPD.items.weapon.melee.Mace;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class WeaponRoomPainter extends Painter {

	/**
	 * Paints a room that contains 3 (static) different unidentified weapons
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
		Item [] weapons = {
			new Knuckles(),
			new Mace(),
			new BattleAxe()
		};
		Mob [] mobs = {new Rat(), new Rat(), new Rat()};
		weapons[0].upgrade();
		weapons[0].upgrade();
		weapons[2].degrade();
		weapons[2].degrade();
		weapons[2].cursed = true;
		placeItems(weapons, Terrain.EMPTY, level, room);
		placeMobs(mobs, Terrain.EMPTY, level, room);
		entrance.set( Room.Door.Type.REGULAR );
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "Be careful when equipping unidentified weapons as they may be cursed, making you " +
				"unable to take them off until they are uncursed. Too heavy weapons will make you " +
				"miss with your attacks.";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Weapon Room\n\n" +
				"This room contains several unidentified weapons. You can't see if the weapons are up/degraded at the " +
				"moment. The required strength to use them efficiently could be less or more than the default " +
				"number displayed on them. Weapons have 5 tiers (5th = best).";
	}
}
