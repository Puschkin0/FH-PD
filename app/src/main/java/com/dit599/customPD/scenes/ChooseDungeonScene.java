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
package com.dit599.customPD.scenes;

import java.util.ArrayList;

import android.util.Log;

import com.dit599.customPD.Assets;
import com.dit599.customPD.CustomPD;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.levels.template.TemplateFactory;
import com.dit599.customPD.ui.Archs;
import com.dit599.customPD.ui.ExitButton;
import com.dit599.customPD.ui.RedButton;
import com.dit599.customPD.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;

public class ChooseDungeonScene extends PixelScene {
	
    private static final String TXT_TITLE = "YourPD Custom Dungeons";
	private static final String TXT_NO_DUNGEONS	= "No custom Dungeons found, go use the Map Editor!";
	
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP 		= 2;
	
	private Archs archs;
	
	@Override
	public void create() {
		
		super.create();
		
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		ArrayList<String> files = new ArrayList<String>();
		
		for(String f : Game.instance.fileList()){
			if(f.endsWith(".map")){
				files.add(f);
			}
		}
		float left = (w - Math.min( 160, w )) / 2 + GAP*10;
		float top = align( (h - BTN_HEIGHT  * files.size()) / 2 );
		
		if (files.size() > 0) {
			
			BitmapText title = PixelScene.createText( TXT_TITLE, 9 );
			title.hardlight( Window.TITLE_COLOR );
			title.measure();
			title.x = align( (w - title.width()) / 2 );
			title.y = align(top - title.height() - GAP );
			add( title );
			
			int pos = 0;
			
			for (final String f : files) {
				RedButton temp = new RedButton( f ) {
					@Override
					protected void onClick() {
						Log.d("SELECTION", f.substring(0, f.length() - 4));
						Dungeon.template = TemplateFactory.createSimpleDungeon(f.substring(0, f.length() - 4), Game.instance);
						CustomPD.switchNoFade( StartScene.class );
					}
				};
				add( temp.setRect(left, pos * BTN_HEIGHT + top + (GAP/2) * pos, w - left * 2, BTN_HEIGHT) );
				
				pos++;
			}
			
		} else {
			
			BitmapText title = PixelScene.createText( TXT_NO_DUNGEONS, 8 );
			title.hardlight( Window.TITLE_COLOR );
			title.measure();
			title.x = align( (w - title.width()) / 2 );
			title.y = align( (h - title.height()) / 2 );
			add( title );
			
		}
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		CustomPD.switchNoFade( GameModeScene.class );
	}
}
