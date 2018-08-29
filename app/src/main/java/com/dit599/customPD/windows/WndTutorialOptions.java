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

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.scenes.PixelScene;
import com.dit599.customPD.ui.RedButton;
import com.dit599.customPD.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;

public class WndTutorialOptions extends Window {

	private static final int WIDTH			= 120;

	public WndTutorialOptions(String message) {
		super();
		BitmapTextMultiline tfMesage = PixelScene.createMultiline( message + "\n\nClick to close.", 8 );
		tfMesage.maxWidth = WIDTH;
		tfMesage.measure();
		tfMesage.x = 0;
		tfMesage.y = 1;
		

		float pos = tfMesage.height();

		RedButton btn = new RedButton( "" ) {
			@Override
			protected void onClick() {
				hide();
				onSelect();
			}
		};
		btn.setRect( -1, -1, tfMesage.width() + ((float)1.5), tfMesage.height() + ((float)1.5));
		add( btn );
		add( tfMesage );


		resize( (int)tfMesage.width(), (int)pos );
	}

	protected void onSelect() {
	};
}
