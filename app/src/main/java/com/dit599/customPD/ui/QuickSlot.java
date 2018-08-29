/*
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.dit599.customPD.ui;

import android.util.Log;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.DungeonTilemap;
import com.dit599.customPD.actors.Actor;
import com.dit599.customPD.actors.Char;
import com.dit599.customPD.actors.hero.Belongings;
import com.dit599.customPD.items.Dewdrop;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.scenes.PixelScene;
import com.dit599.customPD.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Bundle;

public class QuickSlot extends Button implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";

	private static QuickSlot rightInstance;
	private static QuickSlot leftInstance;
	private Item itemInSlot;
	private ItemSlot slot;
	private int id;
	private static int RIGHT_SLOT = 1;

	private Image crossB;
	private Image crossM;

	private boolean targeting = false;
	private Item lastItem = null;
	private Char lastTarget= null;

	public QuickSlot(int i) {
		super();
		this.id = i;
		Log.d("ID", "" + id);
		if(this.id == RIGHT_SLOT){
			item(select(true));
			rightInstance = this;
		}
		else{
			item(select(false));
			leftInstance = this;
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		rightInstance = null;
		leftInstance = null;
		lastItem = null;
		lastTarget = null;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting) {
					GameScene.handleCell( lastTarget.pos );
				} else {
					Item item;
					if(QuickSlot.this.id == RIGHT_SLOT){
						item = select(true);
						Log.d("click1", "RIGHT");
					}
					else{
						item = select(false);
						Log.d("click1" + id, "LEFT");
					}
					if (item == lastItem) {
						useTargeting();
					} else {
						lastItem = item;
					}
					item.execute( Dungeon.hero );
				}
			}
			@Override
			protected boolean onLongClick() {
				return QuickSlot.this.onLongClick();
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}
		};
		add( slot );

		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add( crossB );

		crossM = new Image();
		crossM.copy( crossB );
	}

	@Override
	protected void layout() {
		super.layout();

		slot.fill( this );

		crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
		crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );
	}

	@Override
	protected void onClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
	}

	@Override
	protected boolean onLongClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
		return true;
	}

	@SuppressWarnings("unchecked")
	private static Item select(boolean rightSlot) {
		if(rightSlot){
			if (Dungeon.qsRight instanceof Item) {

				return (Item)Dungeon.qsRight;

			} else if (Dungeon.qsRight != null) {

				Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)Dungeon.qsRight );			
				return item != null ? item : Item.virtual( (Class<? extends Item>)Dungeon.qsRight );

			} else {

				return null;

			}
		}
		else{
			if (Dungeon.qsLeft instanceof Item) {

				return (Item)Dungeon.qsLeft;

			} else if (Dungeon.qsLeft != null) {

				Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)Dungeon.qsLeft );			
				return item != null ? item : Item.virtual( (Class<? extends Item>)Dungeon.qsLeft );

			} else {

				return null;

			}
		}
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {
			if(this.id == RIGHT_SLOT){
				Dungeon.qsRight = item.stackable ? item.getClass() : item;
				refresh(true);
			}
			else{
				Dungeon.qsLeft = item.stackable ? item.getClass() : item;
				refresh(false);
			}
		}
	}

	public void item( Item item ) {
		slot.item( item );
		itemInSlot = item;
		enableSlot();
	}

	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}

	private void enableSlot() {
		slot.enable( 
				itemInSlot != null && 
				itemInSlot.quantity() > 0 && 
				(Dungeon.hero.belongings.backpack.contains( itemInSlot ) || itemInSlot.isEquipped( Dungeon.hero )));
	}

	private void useTargeting() {

		targeting = lastTarget != null && lastTarget.isAlive() && Dungeon.visible[lastTarget.pos];

		if (targeting) {
			if (Actor.all().contains( lastTarget )) {
				lastTarget.sprite.parent.add( crossM );
				crossM.point( DungeonTilemap.tileToWorld( lastTarget.pos ) );
				crossB.visible = true;
			} else {
				lastTarget = null;
			}
		}
	}
	public static void refresh(boolean rightSlot) {
		if (rightSlot && rightInstance != null) {
			rightInstance.item( select(rightSlot) );
		}
		else if (!rightSlot && leftInstance != null) {
			leftInstance.item( select(rightSlot) );
		}
	}

	public static void target( Item item, Char target ) {
		if (rightInstance != null && item == rightInstance.lastItem && target != Dungeon.hero) {
			rightInstance.lastTarget = target;

			HealthIndicator.instance.target( target );
		}
		if (leftInstance != null &&item == leftInstance.lastItem && target != Dungeon.hero) {
			leftInstance.lastTarget = target;

			HealthIndicator.instance.target( target );
		}
	}

	public static void cancel() {
		if (rightInstance != null && rightInstance.targeting) {
			rightInstance.crossB.visible = false;
			rightInstance.crossM.remove();
			rightInstance.targeting = false;
		}
		if (leftInstance != null && leftInstance.targeting) {
			leftInstance.crossB.visible = false;
			leftInstance.crossM.remove();
			leftInstance.targeting = false;
		}
	}
}
