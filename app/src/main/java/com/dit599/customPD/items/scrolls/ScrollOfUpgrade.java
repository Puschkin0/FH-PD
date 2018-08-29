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
package com.dit599.customPD.items.scrolls;

import com.dit599.customPD.Assets;
import com.dit599.customPD.Badges;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.effects.Speck;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.utils.GLog;
import com.dit599.customPD.windows.WndBag;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class ScrollOfUpgrade extends InventoryScroll {

	private static final String TXT_LOOKS_BETTER	= "your %s certainly looks better now";
	
	{
		name = "Scroll of Upgrade";
		inventoryTitle = "Select an item to upgrade";
		mode = WndBag.Mode.UPGRADEABLE;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		ScrollOfRemoveCurse.uncurse( Dungeon.hero, item );
		item.upgrade();
		
		upgrade( curUser );
		GLog.p( TXT_LOOKS_BETTER, item.name() );
		
		Badges.validateItemLevelAquired( item );
	}
	
	public static void upgrade( Hero hero ) {
		hero.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
	}
	
	@Override
	public String desc() {
		return
			"This scroll will upgrade a single item, improving its quality. A wand will " +
			"increase in power and in number of charges; a weapon will inflict more damage " +
			"or find its mark more frequently; a suit of armor will deflect additional blows; " +
			"the effect of a ring on its wearer will intensify. Weapons and armor will also " +
			"require less strength to use, and any curses on the item will be lifted.";
	}
	/**
	 * Modified with a tutorial clause that causes a prompt to display when this
	 * item is picked up by the player in tutorialmode.
	 */
	@Override
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			if(Dungeon.isTutorial){
				WndStory.showChapter("You have picked up a scroll of upgrade. Use it to upgrade " +
						"a weapon or armor of your choice. This will improve the item's " +
						"statistics as well as reducing its strength requirement!");			
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
