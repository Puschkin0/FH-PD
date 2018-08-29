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
package com.dit599.customPD.sprites;

import com.dit599.customPD.Assets;
import com.watabou.noosa.TextureFilm;

public class RatKingSprite extends MobSprite {
	
	public RatKingSprite() {
		super();
		
		texture( Assets.RATKING );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 2, 3, 4, 5, 6 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0 );
		
		play( idle );
	}
}
