/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * Copyright (C) 2012-2014 Oleg Dolya
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
package com.dit599.customPD.windows;

import com.dit599.customPD.Chrome;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.scenes.PixelScene;
import com.dit599.customPD.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.watabou.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 6;
	
	private static final float bgR	= 0.77f;
	private static final float bgG	= 0.73f;
	private static final float bgB	= 0.62f;
	
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_METROPOLIS	= 3;
	public static final int ID_HALLS		= 4;
	
	private static final SparseArray<String> CHAPTERS = new SparseArray<String>();
	
	private static final SparseArray<String> T_CHAPTERS = new SparseArray<String>();
	
	static {
		CHAPTERS.put( ID_SEWERS, 
		"The Dungeon lies right beneath the City, its upper levels actually constitute the City's sewer system. " +
		"Being nominally a part of the City, these levels are not that dangerous. No one will call it a safe place, " +
		"but at least you won't need to deal with evil magic here." );
		
		CHAPTERS.put( ID_PRISON, 
		"Many years ago an underground prison was built here for the most dangerous criminals. At the time it seemed " +
		"like a very clever idea, because this place indeed was very hard to escape. But soon dark miasma started to permeate " +
		"from below, driving prisoners and guards insane. In the end the prison was abandoned, though some convicts " +
		"were left locked up here." );
		
		CHAPTERS.put( ID_CAVES, 
		"The caves, which stretch down under the abandoned prison, are sparcely populated. They lie too deep to be exploited " +
		"by the City and they are too poor in minerals to interest the dwarves. In the past there was a trade outpost " +
		"somewhere here on the route between these two states, but it has perished since the decline of Dwarven Metropolis. " +
		"Only omnipresent gnolls and subterranean animals dwell here now." );
		
		CHAPTERS.put( ID_METROPOLIS, 
		"Dwarven Metropolis was once the greatest of dwarven city-states. In its heyday the mechanized army of dwarves " +
		"has successfully repelled the invasion of the old god and his demon army. But it is said, that the returning warriors " +
		"have brought seeds of corruption with them, and that victory was the beginning of the end for the underground kingdom." );
		
		CHAPTERS.put( ID_HALLS,
		"In the past these levels were the outskirts of Metropolis. After the costly victory in the war with the old god " +
		"dwarves were too weakened to clear them of remaining demons. Gradually demons have tightened their grip on this place " +
		"and now it's called Demon Halls.\n\n" +
		"Very few adventurers have ever descended this far..." );
	};
	static {
	T_CHAPTERS.put(1, "Welcome to the tutorial dungeon! On this floor, you can learn about useful potions, scrolls and seeds " +
			"available in the game. Now start exploring by tapping where you want to go!");
	T_CHAPTERS.put(2, "On this floor, you can learn about equipment identification and how to use magic wells. To get out of the " +
			"first room, you will need to use the magnifying glass button to find the hidden doors!");
	T_CHAPTERS.put(3, "On this floor, you must learn how to fight effectively against both enemies that are hard to hit and " +
			"enemies that hit hard!");
	T_CHAPTERS.put(4, "Welcome to the bonus level, now use what you have learned and the items you have found to defeat the Goo!");
	};
	
	private BitmapTextMultiline tf;
	
	private float delay;
	
	/**
	 * Modified onClick() so that wndstory must always be displayed for 500 milliseconds before it
	 * can be closed. This is used to avoid accidentally closing the window when you tap the screen at the same
	 * time as it appears.
	 */
	public WndStory( String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );
		
		tf = PixelScene.createMultiline( text, 7 );
		tf.maxWidth = WIDTH - MARGIN * 2;
		tf.measure();
		tf.ra = bgR;
		tf.ga = bgG;
		tf.ba = bgB;
		tf.rm = -bgR;
		tf.gm = -bgG;
		tf.bm = -bgB;
		tf.x = MARGIN;
		add( tf );
		add( new TouchArea( chrome ) {
			@Override
			protected void onClick( Touch touch ) {
				if (System.currentTimeMillis() - Dungeon.timeStamp >= 500) {
					hide();
				}
			}
		} );
		
		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.height(), 180 ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
		}
	}
	/**
	 * Modified with a tutorial clause so the correct collection of strings
	 * is used for the "start of level" prompts when in tutorialmode.
	 */
	public static void showChapter( int id ) {
		
		if (Dungeon.chapters.contains( id )) {
			return;
		}
		String text;
		if(Dungeon.isTutorial){
			text = T_CHAPTERS.get( id );
		}
		else{
			text = CHAPTERS.get( id );
		}
		if (text != null) {
			WndStory wnd = new WndStory( text );
			if ((wnd.delay = 0.6f) > 0) {
				wnd.shadow.visible = wnd.chrome.visible = wnd.tf.visible = false;
			}
			
			Game.scene().add( wnd );
			
			Dungeon.chapters.add( id );
		}
	}
	/**
	 * This was added in order to more easily generate prompts (just pass in the
	 * String the prompt should contain).
	 */
	public static void showChapter( String custom ) {
		
		if (custom != null) {
			WndStory wnd = new WndStory( custom );
			Game.scene().add( wnd );

			Dungeon.timeStamp = System.currentTimeMillis();
		}
	}
}