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
package com.dit599.customPD.items;

import com.dit599.customPD.Assets;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.actors.hero.HeroClass;
import com.dit599.customPD.effects.Speck;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.sprites.CharSprite;
import com.dit599.customPD.sprites.ItemSpriteSheet;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class Dewdrop extends Item {

	private static final String TXT_VALUE	= "%+dHP";

	{
		name = "dewdrop";
		image = ItemSpriteSheet.DEWDROP;

		stackable = true;
	}
	/**
	 * Picks up the dewdrop. Modified with a tutorial clause triggered the first
	 * time a user succesfully stores drop in vial in tutorialmode (causing a prompt
	 * that informs the user of this).
	 */
	@Override
	public boolean doPickUp( Hero hero ) {

		DewVial vial = hero.belongings.getItem( DewVial.class );

		if (hero.HP < hero.HT || vial == null || vial.isFull()) {

			int value = 1 + (Dungeon.depth - 1) / 5;
			if (hero.heroClass == HeroClass.HUNTRESS) {
				value++;
			}

			int effect = Math.min( hero.HT - hero.HP, value * quantity );
			if (effect > 0) {
				hero.HP += effect;
				hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				hero.sprite.showStatus( CharSprite.POSITIVE, TXT_VALUE, effect );
			}

		} else if (vial != null) {

			if(Dungeon.isTutorial && !Dungeon.collectedDrop){
				Dungeon.collectedDrop = true;
				WndStory.showChapter("You have stored a dewdrop in your dew vial!");			
			}
			vial.collectDew( this );
		}
		Sample.INSTANCE.play( Assets.SND_DEWDROP );
		hero.spendAndNext( TIME_TO_PICK_UP );

		return true;
	}

	@Override
	public String info() {
		return "A crystal clear dewdrop.";
	}
}
