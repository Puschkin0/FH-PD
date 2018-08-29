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
import com.dit599.customPD.effects.particles.EarthParticle;
import com.dit599.customPD.items.potions.PotionOfParalyticGas;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.sprites.ItemSpriteSheet;
import com.dit599.customPD.ui.BuffIndicator;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Earthroot extends Plant {

	private static final String TXT_DESC = 
		"When a creature touches an Earthroot, its roots " +
		"create a kind of natural armor around it.";
	
	{
		image = 5;
		plantName = "Earthroot";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Armor.class ).level = ch.HT;
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.bottom( pos ).start( EarthParticle.FACTORY, 0.05f, 8 );
			Camera.main.shake( 1, 0.4f );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Earthroot";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_EARTHROOT;
			
			plantClass = Earthroot.class;
			alchemyClass = PotionOfParalyticGas.class;
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
					WndStory.showChapter("You have picked up an earthroot seed. Plant it " +
							"then stand on it to significantly reduce damage taken! we recommend " +
							"that you save one of these for floor 4 of the tutorial...");			
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
	
	public static class Armor extends Buff {
		
		private static final float STEP = 1f;
		
		private int pos;
		private int level;
		
		@Override
		public boolean attachTo( Char target ) {
			pos = target.pos;
			return super.attachTo( target );
		}
		
		@Override
		public boolean act() {
			if (target.pos != pos) {
				detach();
			}
			spend( STEP );
			return true;
		}
		
		public int absorb( int damage ) {
			if (damage >= level) {
				detach();
				return damage - level;
			} else {
				level -= damage;
				return 0;
			}
		}
		
		public void level( int value ) {
			if (level < value) {
				level = value;
			}
		}
		
		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}
		
		@Override
		public String toString() {
			return "Herbal armor";
		}
		
		private static final String POS		= "pos";
		private static final String LEVEL	= "level";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
			bundle.put( LEVEL, level );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
			level = bundle.getInt( LEVEL );
		}
	}
}
