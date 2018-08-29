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
package com.dit599.customPD.items.potions;

import com.dit599.customPD.Assets;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class PotionOfExperience extends Potion {

	{
		name = "Potion of Experience";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		hero.earnExp( hero.maxExp() - hero.exp );
	}
	
	@Override
	public String desc() {
		return
			"The storied experiences of multitudes of battles reduced to liquid form, " +
			"this draught will instantly raise your experience level.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 80 * quantity : super.price();
	}
	/**
	 * Modified with a tutorial clause that causes a prompt to display when this
	 * item is picked up by the player in tutorialmode.
	 */
	@Override
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			if(Dungeon.isTutorial){
				WndStory.showChapter("You have picked up a potion of experience. Drink it now " +
						"to increase your level by 1! This will increase your max health, chance to hit " +
						"enemies and chance to avoid enemy attacks.");			
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
