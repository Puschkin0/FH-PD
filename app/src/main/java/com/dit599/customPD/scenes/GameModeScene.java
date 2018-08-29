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
import android.util.Log;

import com.dit599.customPD.Assets;
import com.dit599.customPD.CustomPD;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.editorUI.MapSelectorActivity;
import com.dit599.customPD.effects.BannerSprites;
import com.dit599.customPD.effects.Fireball;
import com.dit599.customPD.levels.template.DungeonTemplate;
import com.dit599.customPD.levels.template.TemplateFactory;
import com.dit599.customPD.ui.Archs;
import com.dit599.customPD.ui.ExitButton;
import com.dit599.customPD.ui.PrefsButton;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

/**
 * Based on TitleScene. Used to allow user to choose game mode to play.
 */
public class GameModeScene extends PixelScene{

	private static final String TXT_STANDARD		= "Standard";
	private static final String TXT_CUSTOM			= "Custom";
	private static final String TXT_MAP_EDITOR		= "Map Editor";
	private static final String TXT_TUTORIAL		= "Tutorial";

	/**
	 * Modified to use buttons that redirect to the correct game mode and sets some flags
	 * & other values when onClick().
	 */
	@Override
	public void create() {

		super.create();

		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		float height = 180;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		title.x = (w - title.width()) / 2;
		title.y = (h - height) / 2;

		placeTorch( title.x + 18, title.y + 20 );
		placeTorch( title.x + title.width - 18, title.y + 20 );

		DashboardItem btnCustom = new DashboardItem(TXT_CUSTOM, 2) {
			@Override
			protected void onClick() {
				Dungeon.isTutorial = false;
				Dungeon.template = new DungeonTemplate();
				Dungeon.firePrompt = true;
				Dungeon.encounteredMob = true;
				Dungeon.foundHeap = true;
				Dungeon.foundItem = true;
				Dungeon.invOpened = true;
				Dungeon.hungerNotified = true;
				Dungeon.starvingNotified = true;
				Dungeon.collectedDrop = true;
				CustomPD.switchNoFade( ChooseDungeonScene.class );
			}
		};
		btnCustom.setPos( w / 2 - btnCustom.width(), (h + height) / 2 - DashboardItem.SIZE );
		add( btnCustom );

		DashboardItem btnEditor = new DashboardItem(TXT_MAP_EDITOR, 3) {

			@Override
			protected void onClick() {
				Dungeon.isTutorial = false;
				Dungeon.template = null;
				Dungeon.firePrompt = true;
				Dungeon.encounteredMob = true;
				Dungeon.foundHeap = true;
				Dungeon.foundItem = true;
				Dungeon.invOpened = true;
				Dungeon.hungerNotified = true;
				Dungeon.starvingNotified = true;
				Dungeon.collectedDrop = true;
				//CustomPD.self.editMaps();
				Intent intent = new Intent(Game.instance, MapSelectorActivity.class);
				Game.instance.startActivity(intent);
			}

		};
		btnEditor.setPos( w / 2, (h + height) / 2 - DashboardItem.SIZE );
		add( btnEditor );

		DashboardItem btnStandard = new DashboardItem( TXT_STANDARD, 0 ) {
			@Override
			protected void onClick() {
				Dungeon.isTutorial = false;
				Dungeon.template = null;
				Dungeon.firePrompt = true;
				Dungeon.encounteredMob = true;
				Dungeon.foundHeap = true;
				Dungeon.foundItem = true;
				Dungeon.invOpened = true;
				Dungeon.hungerNotified = true;
				Dungeon.starvingNotified = true;
				Dungeon.collectedDrop = true;
				CustomPD.switchNoFade( StartScene.class );
			}
		};
		btnStandard.setPos( w / 2 - btnStandard.width(), btnEditor.top() - DashboardItem.SIZE );
		add( btnStandard );

		DashboardItem btnTutorial = new DashboardItem( TXT_TUTORIAL, 1 ) {
			@Override
			protected void onClick() {
				Dungeon.isTutorial = true;
				Dungeon.template = null;
				Dungeon.firePrompt = false;
				Dungeon.encounteredMob = false;
				Dungeon.foundHeap = false;
				Dungeon.foundItem = false;
				Dungeon.invOpened = false;
				Dungeon.hungerNotified = false;
				Dungeon.starvingNotified = false;
				Dungeon.collectedDrop = false;
				for(String f : Game.instance.fileList()){
					Log.d("FILES", f);
				}
				CustomPD.switchNoFade(  StartScene.class );
			}
		};
		btnTutorial.setPos( w / 2, btnStandard.top() );
		add( btnTutorial );

		BitmapText version = new BitmapText( "v " + Game.version, font1x );
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width();
		version.y = h - version.height();
		add( version );

		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setPos( 0, 0 );
		add( btnPrefs );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class DashboardItem extends Button {

		public static final float SIZE	= 48;

		private static final int IMAGE_SIZE	= 32;

		private Image image;
		private BitmapText label;
		private boolean enabled = true;

		public DashboardItem( String text, int index ) {
			super();

			image.frame( image.texture.uvRect( index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE ) );
			this.label.text( text );
			this.label.measure();

			setSize( SIZE, SIZE );
		}

		public DashboardItem(String text, int index, boolean enabled) {
			this(text, index);
			this.enabled = enabled;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image( Assets.TUTORIAL );
			add( image );

			label = createText( 9 );
			add( label );
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = align( x + (width - image.width()) / 2 );
			image.y = align( y );

			label.x = align( x + (width - label.width()) / 2 );
			label.y = align( image.y + image.height() +2 );
		}

		@Override
		protected void onTouchDown() {
			if (enabled) {
				image.brightness(1.5f);
				Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 0.8f);
			}
		}

		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
	protected void onBackPressed() {
		CustomPD.switchNoFade( TitleScene.class );
	}
}
