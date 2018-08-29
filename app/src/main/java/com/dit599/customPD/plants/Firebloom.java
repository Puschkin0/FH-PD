/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package com.dit599.customPD.plants;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.Char;
import com.dit599.customPD.actors.blobs.Blob;
import com.dit599.customPD.actors.blobs.Fire;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.effects.CellEmitter;
import com.dit599.customPD.effects.particles.FlameParticle;
import com.dit599.customPD.items.potions.PotionOfLiquidFlame;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.sprites.ItemSpriteSheet;
import com.dit599.customPD.windows.WndStory;

public class Firebloom extends Plant {

	private static final String TXT_DESC = "When something touches a Firebloom, it bursts into flames.";
	
	{
		image = 0;
		plantName = "Firebloom";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		GameScene.add( Blob.seed( pos, 2, Fire.class ) );
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).burst( FlameParticle.FACTORY, 5 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Firebloom";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_FIREBLOOM;
			
			plantClass = Firebloom.class;
			alchemyClass = PotionOfLiquidFlame.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
		/**
		 * Modified with a tutorial clause that causes a prompt to display when this
		 * item is picked up by the player in tutorialmode.
		 */
		@Override
		public boolean doPickUp(Hero hero){
			boolean b = super.doPickUp(hero);
			if(b && Dungeon.isTutorial && !Dungeon.firePrompt){
				Dungeon.firePrompt = true;
				WndStory.showChapter( 
						"You have found an item that creates fire! Now look for a pile of wood or a " +
								"lone bookcase to burn down. If you manage to set yourself on fire, " +
								"go stand in water!");
			}
			return b;
		}
	}
}
