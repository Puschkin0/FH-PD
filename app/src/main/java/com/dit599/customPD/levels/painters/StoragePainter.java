
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

import com.dit599.customPD.items.Generator;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.PotionOfLiquidFlame;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.watabou.utils.Random;

public class StoragePainter extends Painter {

	public static void paint( Level level, Room room ) {

		final int floor = Terrain.EMPTY_SP;

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, floor );

//		if(Dungeon.template == null){
			int n = Random.IntRange( 3, 4 );
			Item [] items = new Item[n];
			for (int i=0; i < n; i++) { 
				items[i] = prize( level );
			}
			placeItems(items, floor, level, room);
//		}
		room.entrance().set( Room.Door.Type.BARRICADE );
		level.addItemToSpawn( new PotionOfLiquidFlame() );
	}

	private static Item prize( Level level ) {

		Item prize = level.itemToSpanAsPrize();
		if (prize != null) {
			return prize;
		}

		return Generator.random( Random.oneOf( 
				Generator.Category.POTION, 
				Generator.Category.SCROLL,
				Generator.Category.FOOD, 
				Generator.Category.GOLD,
				Generator.Category.MISC
				) );
	}
}