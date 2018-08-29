/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
import com.dit599.customPD.items.Gold;
import com.dit599.customPD.items.Heap;
import com.dit599.customPD.items.keys.IronKey;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Random;

public class TreasuryPainter extends Painter {

	private static final int MAX_TRIES = 10;

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		set( level, room.center(), Terrain.STATUE );

		Heap.Type heapType = Random.Int( 2 ) == 0 ? Heap.Type.CHEST : Heap.Type.HEAP;

		//		if(Dungeon.template == null){
		int n = Random.IntRange( 2, 3 );
		for (int i=0; i < n; i++) {
			int pos;
			int tries = MAX_TRIES;
			do {
				pos = room.random();
				tries--;
			} while ((level.map[pos] != Terrain.EMPTY || level.heaps.get( pos ) != null) && tries > 0);
			if(!(level.map[pos] != Terrain.EMPTY || level.heaps.get( pos ) != null)){
				level.drop( new Gold().random(), pos ).type = (i == 0 && heapType == Heap.Type.CHEST ? Heap.Type.MIMIC : heapType);
			}
		}

		if (heapType == Heap.Type.HEAP) {
			for (int i=0; i < 6; i++) {
				int pos;
				int tries = MAX_TRIES;
				do {
					pos = room.random();
					tries--;
				} while (level.map[pos] != Terrain.EMPTY && tries > 0);
				if(!(level.map[pos] != Terrain.EMPTY)){
					level.drop( new Gold( Random.IntRange( 1, 3 ) ), pos );
				}
			}
		}
		//		}
		room.entrance().set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
}