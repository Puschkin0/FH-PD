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

import com.dit599.customPD.Assets;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.Char;
import com.dit599.customPD.actors.buffs.Buff;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.effects.CellEmitter;
import com.dit599.customPD.effects.Speck;
import com.dit599.customPD.effects.particles.ShaftParticle;
import com.dit599.customPD.items.potions.PotionOfHealing;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.sprites.ItemSpriteSheet;
import com.dit599.customPD.ui.BuffIndicator;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Sungrass extends Plant {

	private static final String TXT_DESC = "Sungrass is renowned for its sap's healing properties.";
	
	{
		image = 4;
		plantName = "Sungrass";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Health.class );
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Sungrass";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_SUNGRASS;
			
			plantClass = Sungrass.class;
			alchemyClass = PotionOfHealing.class;
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
		public boolean doPickUp( Hero hero ) {
			if (collect( hero.belongings.backpack )) {
				if(Dungeon.isTutorial){
					WndStory.showChapter("You have picked up a sungrass seed. Plant it " +
							"then stand on it to slowly recover health!");			
				}
				GameScene.pickUp( this );
				Sample.INSTANCE.play( Assets.SND_ITEM );
				hero.spendAndNext( TIME_TO_PICK_UP );
				return true;
				
			} else {
				return false;
			}
		}
	}
	
	public static class Health extends Buff {
		
		private static final float STEP = 5f;
		
		private int pos;
		
		@Override
		public boolean attachTo( Char target ) {
			pos = target.pos;
			return super.attachTo( target );
		}
		
		@Override
		public boolean act() {
			if (target.pos != pos || target.HP >= target.HT) {
				detach();
			} else {
				target.HP = Math.min( target.HT, target.HP + target.HT / 10 );
				target.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
			spend( STEP );
			return true;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}
		
		@Override
		public String toString() {
			return "Herbal healing";
		}
		
		private static final String POS	= "pos";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
		}
	}
}
