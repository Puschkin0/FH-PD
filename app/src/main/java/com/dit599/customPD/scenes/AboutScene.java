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

import android.content.Intent;
import android.net.Uri;

import com.dit599.customPD.CustomPD;
import com.dit599.customPD.effects.Flare;
import com.dit599.customPD.ui.Archs;
import com.dit599.customPD.ui.ExitButton;
import com.dit599.customPD.ui.Icons;
import com.dit599.customPD.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;

public class AboutScene extends PixelScene {

	private static final String TXT_UPPER = "This is a modification of Pixel Dungeon, " +
			"which can be found here:\n";
	
	private static final String TXT_LOWER = 
		"\n\nCode & graphics: Watabou\n" +
		"Music: Cube_Code\n\n" + 
		"Additional modifications & coding: YourPD team\n\n"
            +
		"This game is inspired by Brian Walker's Brogue. " +
		"Try it on Windows, Mac OS or Linux - it's awesome! ;)\n\n" +
		"License and warranty can be found here:";
	
	private static final String PD_LNK = "play.google.com/store/apps/details?id=com.dit599.customPD";
	private static final String GPL_LNK = "gnu.org/copyleft/gpl.html";
	
	/**
	 * Modified (as well as the static strings in this class) to present relevant links to the license
	 * and original game, as well as present how this is a modification.
	 */
	@Override
	public void create() {
		super.create();
		
		BitmapTextMultiline upperText = createMultiline( TXT_UPPER, 8 );
		upperText.maxWidth = Math.min( Camera.main.width, 120 );
		upperText.measure();
		add( upperText );
		
		BitmapTextMultiline pdLink = createMultiline( PD_LNK, 8 );
		pdLink.maxWidth = Math.min( Camera.main.width, 120 );
		pdLink.measure();
		pdLink.hardlight( Window.TITLE_COLOR );
		add( pdLink );
		
		BitmapTextMultiline lowerText = createMultiline( TXT_LOWER, 8 );
		lowerText.maxWidth = Math.min( Camera.main.width, 120 );
		lowerText.measure();
		add( lowerText );
		
		BitmapTextMultiline gplLink = createMultiline( GPL_LNK, 8 );
		gplLink.maxWidth = Math.min( Camera.main.width, 120 );
		gplLink.measure();
		gplLink.hardlight( Window.TITLE_COLOR );
		add( gplLink );
		
		upperText.x = align( (Camera.main.width - upperText.width()) / 2 );
		upperText.y = align( (Camera.main.height - (upperText.height() + pdLink.height() + 
													lowerText.height() + gplLink.height())) / 2 );
		
		pdLink.x = upperText.x;
		pdLink.y = upperText.y + upperText.height();
		
		lowerText.x = upperText.x;
		lowerText.y = pdLink.y + pdLink.height();
		
		gplLink.x = upperText.x;
		gplLink.y = lowerText.y + lowerText.height();
		
		TouchArea pdHotArea = new TouchArea( pdLink ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + PD_LNK ) );
				Game.instance.startActivity( intent );
			}
		};
		add( pdHotArea );
		
		TouchArea gplHotArea = new TouchArea( gplLink ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + GPL_LNK ) );
				Game.instance.startActivity( intent );
			}
		};
		add( gplHotArea );
		
		Image title = Icons.TITLE.get();
		title.x = align( upperText.x + (upperText.width() - title.width) / 2 );
		title.y = upperText.y - title.height - 8;
		add( title );
		
		new Flare( 7, 64 ).color( 0x112233, true ).show( title, 0 ).angularSpeed = +20;
		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		CustomPD.switchNoFade( TitleScene.class );
	}
}