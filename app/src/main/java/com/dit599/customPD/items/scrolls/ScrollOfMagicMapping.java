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
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.actors.buffs.Invisibility;
import com.dit599.customPD.actors.hero.Hero;
import com.dit599.customPD.effects.CellEmitter;
import com.dit599.customPD.effects.Speck;
import com.dit599.customPD.effects.SpellSprite;
import com.dit599.customPD.levels.Level;
import com.dit599.customPD.levels.Terrain;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.utils.GLog;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.audio.Sample;

public class ScrollOfMagicMapping extends Scroll {

	private static final String TXT_LAYOUT = "You are now aware of the level layout.";
	
	{
		name = "Scroll of Magic Mapping";
	}
	
	@Override
	protected void doRead() {
		
		int length = Level.LENGTH;
		int[] map = Dungeon.level.map;
		boolean[] mapped = Dungeon.level.mapped;
		boolean[] discoverable = Level.discoverable;
		Terrain.mappingScroll = true;
		boolean noticed = false;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				mapped[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					
					Level.set( i, Terrain.discover( terr ) );						
					GameScene.updateMap( i );
					
					if (Dungeon.visible[i]) {
						GameScene.discoverTile( i, terr );
						discover( i );
						
						noticed = true;
					}
				}
			}
		}
		Dungeon.observe();
		
		GLog.i( TXT_LAYOUT );
		if (noticed) {
			Sample.INSTANCE.play( Assets.SND_SECRET );
		}
		
		SpellSprite.show( curUser, SpellSprite.MAP );
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		setKnown();
		
		curUser.spendAndNext( TIME_TO_READ );
		Terrain.mappingScroll = false;
	}
	
	@Override
	public String desc() {
		return
			"When this scroll is read, an image of crystal clarity will be etched into your memory, " +
			"alerting you to the precise layout of the level and revealing all hidden secrets. " +
			"The locations of items and creatures will remain unknown.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 25 * quantity : super.price();
	}
	
	public static void discover( int cell ) {
		CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.1f, 4 );
	}
	/**
	 * Modified with a tutorial clause that causes a prompt to display when this
	 * item is picked up by the player in tutorialmode.
	 */
	@Override
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			if(Dungeon.isTutorial){
				WndStory.showChapter("You have picked up a scroll of mapping. Use it to " +
						"reveal the map of the floor you are on!");			
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
