/*
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.dit599.customPD.levels.painters;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.Actor;
import com.dit599.customPD.actors.mobs.Mob;
import com.dit599.customPD.actors.mobs.Piranha;
import com.dit599.customPD.items.Generator;
import com.dit599.customPD.items.Heap;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.PotionOfInvisibility;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Random;

public class PoolPainter extends Painter {

	private static final int NPIRANHAS	= 3;

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
//		if(Dungeon.template == null){
			int pos = x + y * Level.WIDTH;
			Item [] items = {
					prize( level )	
			};
			placeHeap(items, pos, level, Random.Int( 3 ) == 0 ? Heap.Type.CHEST : Heap.Type.HEAP);
			set( level, pos, Terrain.PEDESTAL );

			level.addItemToSpawn( new PotionOfInvisibility() );

			Mob [] mobs = new Mob[NPIRANHAS];
			for (int i=0; i < NPIRANHAS; i++) {
				mobs[i] = new Piranha();
			}
			placeMobs(mobs, Terrain.WATER, level, room);
//		}
	}

	private static Item prize( Level level ) {

		Item prize = level.itemToSpanAsPrize();
		if (prize != null) {
			return prize;
		}

		prize = Generator.random( Random.oneOf(  
				Generator.Category.WEAPON, 
				Generator.Category.ARMOR 
				) );

		for (int i=0; i < 4; i++) {
			Item another = Generator.random( Random.oneOf(  
					Generator.Category.WEAPON, 
					Generator.Category.ARMOR 
					) );
			if (another.level > prize.level) {
				prize = another;
			}
		}

		return prize;
	}
}
