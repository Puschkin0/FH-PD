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
import com.dit599.customPD.actors.buffs.Bleeding;
import com.dit599.customPD.actors.buffs.Buff;
import com.dit599.customPD.actors.buffs.Cripple;
import com.dit599.customPD.actors.buffs.Poison;
import com.dit599.customPD.actors.buffs.Weakness;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.effects.Speck;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.utils.GLog;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class PotionOfHealing extends Potion {

	{
		name = "Potion of Healing";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		heal( Dungeon.hero );
		GLog.p( "Your wounds heal completely." );
	}
	
	public static void heal( Hero hero ) {
		
		hero.HP = hero.HT;
		Buff.detach( hero, Poison.class );
		Buff.detach( hero, Cripple.class );
		Buff.detach( hero, Weakness.class );
		Buff.detach( hero, Bleeding.class );
		
		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
	}
	
	@Override
	public String desc() {
		return
			"An elixir that will instantly return you to full health and cure poison.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
	/**
	 * Modified with a tutorial clause that causes a prompt to display when this
	 * item is picked up by the player in tutorialmode.
	 */
	@Override
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			if(Dungeon.isTutorial){
				WndStory.showChapter("You have picked up a potion of healing. Use it when " +
						"you are low on life!");			
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
