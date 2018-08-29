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

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.blobs.Alchemy;
import com.dit599.customPD.items.Heap;
import com.dit599.customPD.items.potions.Potion;
import com.dit599.customPD.items.potions.PotionOfExperience;
import com.dit599.customPD.items.potions.PotionOfHealing;
import com.dit599.customPD.items.potions.PotionOfStrength;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.dit599.customPD.levels.Room.Type;
import com.dit599.customPD.scenes.GameScene;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

/**
 * Paints a room that contains potions of strength, health and experience, as well as
 * an alchemy pot.
 */
public class PotionRoomPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );

		Room.Door entrance = room.entrance();

		Point pot = null;
		if (entrance.x == room.left) {
			pot = new Point( room.right-1, Random.Int( 2 ) == 0 ? room.top + 1 : room.bottom - 1 );
		} else if (entrance.x == room.right) {
			pot = new Point( room.left+1, Random.Int( 2 ) == 0 ? room.top + 1 : room.bottom - 1 );
		} else if (entrance.y == room.top) {
			pot = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.bottom-1 );
		} else if (entrance.y == room.bottom) {
			pot = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.top+1 );
		}
		set( level, pot, Terrain.ALCHEMY );
		set(level, room.center(), Terrain.SIGN);
		Alchemy alchemy = new Alchemy();
		alchemy.seed( pot.x + Level.WIDTH * pot.y, 1 );
		level.blobs.put( Alchemy.class, alchemy );

//		if (Dungeon.template == null){
			int pos;
			Potion [] potions = {
					new PotionOfExperience(), new PotionOfExperience(), new PotionOfHealing(), new PotionOfHealing(),
					new PotionOfStrength(), new PotionOfStrength()
			};

			for (Potion p : potions){
				p.setKnown();
			}

			do {
				pos = room.random();
			} while (
					level.map[pos] != Terrain.EMPTY_SP || 
					level.heaps.get( pos ) != null);

			placeHeap(potions, pos, level, Heap.Type.HEAP);
//		}
		entrance.set( Room.Door.Type.REGULAR );
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "A combination of 3 seeds of the same or varying colour can be used in an alchemy pot to brew a potion.";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Potion Brewery\n\n " +
				"This room contains a few copies of the most useful potions in the game, press them in your inventory to learn " +
				"what they do. Potions can be either drunk or thrown, and which colour gives which effect is " +
				"randomized each new game (and in the real game all potion types are unidentified until used).";
	}
}

