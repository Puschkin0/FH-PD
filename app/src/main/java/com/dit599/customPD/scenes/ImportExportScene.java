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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.util.Log;

import com.dit599.customPD.Assets;
import com.dit599.customPD.CustomPD;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.levels.template.DungeonTemplate;
import com.dit599.customPD.levels.template.TemplateFactory;
import com.dit599.customPD.ui.Archs;
import com.dit599.customPD.ui.ExitButton;
import com.dit599.customPD.ui.RedButton;
import com.dit599.customPD.ui.Window;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;

import android.os.Environment;

public class ImportExportScene extends PixelScene {

	//private static final String TXT_TITLE = "Import/Export Custom Maps";
	private static final String TXT_WARNING	= 
			"Importing/Exporting will overwrite maps with the same name. You will need to press one of " +
			"these buttons to create the public YourPD folder if you do not have it yet.\n\n" +
			"You can only have 10 maps ingame due to UI limitations. Import will still overwrite existing " +
			"files with same name when you have 10 maps.\n\n" +
			"Upload your exported maps wherever you choose by copying the file from the public folder " +
			"to your pc. You of course place other peoples' maps in the same folder in order to be able to import them.\n\n";
	

	private static final int BTN_HEIGHT	= 20;
	private static final int GAP 		= 2;

	private static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().toString() + "/YourPD/";

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
		float left = (w - Math.min( 160, w )) / 2 + GAP*10;
		float top = align( (h - BTN_HEIGHT  * 2) / 2 );

		BitmapTextMultiline title = createMultiline( TXT_WARNING, 8 );
		title.maxWidth = Math.min( Camera.main.width, 120 );
		title.measure();
		title.x = align( (w - title.width()) / 2 );
		title.y = align(top - title.height() - GAP );
		add( title );

		int pos = 0;

		RedButton imp = new RedButton( "Import Maps" ) {
			@Override
			protected void onClick() {
				int count = 0;
				ArrayList<String> files = new ArrayList<String>();

				for(String f : Game.instance.fileList()){
					if(f.endsWith(".map")){
						files.add(f);
					}
				}
				ArrayList<String> importFiles = new ArrayList<String>();

				try {
					File SDCardRoot = Environment.getExternalStorageDirectory();
					File dir = new File(SDCardRoot, "/YourPD");
					if (!dir.exists()) {
						dir.mkdir();
					}
					for(String f : dir.list()){
						if(f.endsWith(".map")){
							importFiles.add(f);
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
					Log.d("FAILED IMPORT", "Before File Reading/Writing");
				}
				for(String f : importFiles){
					if(files.size() == 10 && !files.contains(f)){
						continue;
					}
					else{
						try {
							DungeonTemplate template = new DungeonTemplate();
							template.load(DEFAULT_PATH + f.substring(0, f.length() - 4), Game.instance);
							template.save(f.substring(0, f.length() - 4), Game.instance);
							//If map from empty bundle created, default layout will overwrite instead.
							TemplateFactory.createSimpleDungeon(f.substring(0, f.length() - 4), Game.instance);
							count++;
						}
						catch(Exception e){
							e.printStackTrace();
							Log.d("FAILED IMPORT", f);
						}
					}
				}
				WndStory.showChapter(count + " Maps were imported!");
			}
		};
		add( imp.setRect(left, pos * BTN_HEIGHT + top + (GAP/2) * pos, w - left * 2, BTN_HEIGHT) );
		pos++;

		RedButton exp = new RedButton( "Export Maps" ) {
			@Override
			protected void onClick() {
				int count = 0;
				ArrayList<String> files = new ArrayList<String>();

				for(String f : Game.instance.fileList()){
					if(f.endsWith(".map")){
						files.add(f);
					}
				}

				for (String f : files){
					try {
						File SDCardRoot = Environment.getExternalStorageDirectory();
						File dir = new File(SDCardRoot, "/YourPD");
						if (!dir.exists()) {
							dir.mkdir();
						}
						DungeonTemplate template = new DungeonTemplate();
						template.load(f.substring(0, f.length() - 4), Game.instance);
						template.save(DEFAULT_PATH + f.substring(0, f.length() - 4), Game.instance);
						count++;
					}
					catch(Exception e){
						e.printStackTrace();
						Log.d("FAILED EXPORT", f);
					}
				}
				WndStory.showChapter(count + " Maps were exported!");
			}
		};
		add( exp.setRect(left, pos * BTN_HEIGHT + top + (GAP/2) * pos, w - left * 2, BTN_HEIGHT) );	
		pos++;

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
