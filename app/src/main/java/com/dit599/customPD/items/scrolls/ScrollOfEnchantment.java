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
import com.dit599.customPD.items.armor.Armor;
import com.dit599.customPD.items.weapon.Weapon;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.utils.GLog;
import com.dit599.customPD.windows.WndBag;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class ScrollOfEnchantment extends InventoryScroll {

	private static final String TXT_GLOWS	= "your %s glows in the dark";
	
	{
		name = "Scroll of Enchantment";
		inventoryTitle = "Select an enchantable item";
		mode = WndBag.Mode.ENCHANTABLE;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		ScrollOfRemoveCurse.uncurse( Dungeon.hero, item );
		
		if (item instanceof Weapon) {
			
			((Weapon)item).enchant();
			
		} else {

			((Armor)item).inscribe();
		
		}
		
		item.fix();
		
		curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
		GLog.w( TXT_GLOWS, item.name() );
	}
	
	@Override
	public String desc() {
		return
			"This scroll is able to imbue a weapon or an armor " +
			"with a random enchantment, granting it a special power.";
	}
	/**
	 * Modified with a tutorial clause that causes a prompt to display when this
	 * item is picked up by the player in tutorialmode.
	 */
	@Override
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			if(Dungeon.isTutorial){
				WndStory.showChapter("You have picked up a scroll of weapon upgrade. Use it to upgrade " +
						"a weapon of your choice. The weapon will also gain a magical effect if it did not " +
						"already have one.");			
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
