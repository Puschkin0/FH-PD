/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * 
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.dit599.customPD.items.potions;

import com.dit599.customPD.Assets;
import com.dit599.customPD.Badges;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.sprites.CharSprite;
import com.dit599.customPD.utils.GLog;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class PotionOfStrength extends Potion {

	{
		name = "Potion of Strength";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		
		hero.STR++;
		hero.sprite.showStatus( CharSprite.POSITIVE, "+1 str" );
		GLog.p( "Newfound strength surges through your body." );
		
		Badges.validateStrengthAttained();
	}
	
	@Override
	public String desc() {
		return
			"This powerful liquid will course through your muscles, " +
			"permanently increasing your strength by one point.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
	/**
	 * Modified with a tutorial clause that causes a prompt to display when this
	 * item is picked up by the player in tutorialmode.
	 */
	@Override
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			if(Dungeon.isTutorial){
				WndStory.showChapter("You have picked up a potion of strength. Drink it now to permanently increase " +
						"your strength by 1! This will allow you to use heavier armor & weapons effectively.");			
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
