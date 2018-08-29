package com.dit599.customPD.windows;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.wands.Wand;
import com.dit599.customPD.ui.QuickSlot;

public class WndSetQuickSlot extends WndOptions{
	
	private Item item;

	public WndSetQuickSlot(String title, String message, String[] options, Item i) {
		super(title, message, options);
		item = i;
	}
	@Override
	protected void onSelect( int index ) {
		if(index == 1){
			Dungeon.qsRight = item instanceof Wand ? item : item.getClass();
			QuickSlot.refresh(true);
		}
		else{
			Dungeon.qsLeft = item instanceof Wand ? item : item.getClass();
			QuickSlot.refresh(false);
		}
	};
}
