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

import com.dit599.customPD.actors.blobs.Foliage;
import com.dit599.customPD.items.food.Pasty;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.Terrain;
import com.dit599.customPD.plants.Sungrass;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class TutorialGardenPainter extends Painter {

	/**
	 * Paints a room that is covered in tall grass and contains 1 piece of food.
	 */
	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.HIGH_GRASS );
		fill( level, room, 2, Terrain.GRASS );
		
		room.entrance().set( Room.Door.Type.REGULAR );
		
		int bushes = Random.Int( 3 ) == 0 ? (Random.Int( 5 ) == 0 ? 2 : 1) : 0;
		for (int i=0; i < bushes; i++) {
			level.plant( new Sungrass.Seed(), room.random() );
		}
		
		Foliage light = (Foliage)level.blobs.get( Foliage.class );
		if (light == null) {
			light = new Foliage();
		}
		for (int i=room.top + 1; i < room.bottom; i++) {
			for (int j=room.left + 1; j < room.right; j++) {
				light.seed( j + Level.WIDTH * i, 1 );
			}
		}
		level.blobs.put( Foliage.class, light );
		set( level, room.center(), Terrain.SIGN );
		level.drop(new Pasty(), room.center().x + (room.center().y-1) * Level.WIDTH);
	}
	/**
	 * Returns the string to display on a sign found in this room type.
	 */
	public static String tip() {
		return "Resting speeds up your natural health regeneration, " +
				"but also makes you go hungry faster.";
	}
	/**
	 * Returns the string to display on the prompt that appears when entering this room.
	 */
	public static String prompt() {
		return "Garden Of Resting\n\n" +
				"In gardens you can safely rest (hold down the icon in the lower-left corner) " +
				"as long as you are not starving. Enemies will not find you in here.";
	}
}
