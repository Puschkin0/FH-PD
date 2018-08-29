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
package com.dit599.customPD.editorUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.dit599.customPD.Dungeon;
import com.dit599.customPD.R;
import com.dit599.customPD.actors.hero.HeroClass;
import com.dit599.customPD.editorUI.Mappings.ArmorMapping;
import com.dit599.customPD.editorUI.Mappings.ConsumableMapping;
import com.dit599.customPD.editorUI.Mappings.EnchantmentsMapping;
import com.dit599.customPD.editorUI.Mappings.GlyphsMapping;
import com.dit599.customPD.editorUI.Mappings.LevelMapping;
import com.dit599.customPD.editorUI.Mappings.MobMapping;
import com.dit599.customPD.editorUI.Mappings.PotionMapping;
import com.dit599.customPD.editorUI.Mappings.RingMapping;
import com.dit599.customPD.editorUI.Mappings.RoomMapping;
import com.dit599.customPD.editorUI.Mappings.ScrollMapping;
import com.dit599.customPD.editorUI.Mappings.SeedMapping;
import com.dit599.customPD.editorUI.Mappings.WandMapping;
import com.dit599.customPD.editorUI.Mappings.WeaponMapping;
import com.dit599.customPD.levels.template.LevelTemplate;
import com.watabou.noosa.Game;


public class MapEditActivity extends FragmentActivity{

	public static final String EXTRA_FILENAME = "filename";

	public static final String TAB_ADD_FLOOR = "add_floor";

	public TemplateHandler templateHandler;

	private static String mapName;
	private FragmentTabHost mTabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_ui);

		mapName = getIntent().getStringExtra(EXTRA_FILENAME);
		if (mapName == null) {
			throw new NullPointerException("Intent parameter \"" + EXTRA_FILENAME + "\" missing");
		}
		setTitle(mapName);

		templateHandler = TemplateHandler.getInstance(mapName, MapEditActivity.this);

		if (templateHandler == null || templateHandler.getDungeon() == null) {
			throw new NullPointerException();
		}

		initMappings();

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(MapEditActivity.this, getSupportFragmentManager(), android.R.id.tabcontent);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_ADD_FLOOR).setIndicator("Add floor", null),
				Fragment.class, null);
		int depth = 1;
		for (LevelTemplate level : templateHandler.getDungeon().levelTemplates) {
			mTabHost.addTab(
					mTabHost.newTabSpec(String.valueOf(depth)).setIndicator("Floor " + depth, null),
					FloorFragment.class, null);
			depth++;
		}


		mTabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabSpec) {
				if (tabSpec.equals(TAB_ADD_FLOOR)) {
					int tabCount = mTabHost.getTabWidget().getTabCount();
					String tab_name = "Floor " + tabCount;
					templateHandler = TemplateHandler.getInstance(mapName, MapEditActivity.this);
					templateHandler.addLevel();
					mTabHost.addTab(
							mTabHost.newTabSpec(String.valueOf(tabCount)).setIndicator(tab_name,
									null),
									FloorFragment.class, null);
					mTabHost.getTabWidget().getChildTabViewAt(tabCount).setLongClickable(true);
					mTabHost.getTabWidget().getChildTabViewAt(tabCount).setOnLongClickListener(deleteTab());
					mTabHost.setCurrentTab(tabCount);	    	
				}
			}});

		for(int i = 1; i < mTabHost.getTabWidget().getTabCount(); i++){
			mTabHost.getTabWidget().getChildTabViewAt(i).setLongClickable(true);
			mTabHost.getTabWidget().getChildTabViewAt(i).setOnLongClickListener(deleteTab());
		}
		if (mTabHost.getTabWidget().getTabCount() > 1) {
			mTabHost.setCurrentTab(1);
		}
	}

	public void initMappings() {
		ArmorMapping.armorMappingInit();
		WeaponMapping.weaponMappingInit();
		WandMapping.wandMappingInit();
		RingMapping.ringMappingInit();
		PotionMapping.potionMappingInit();
		ScrollMapping.scrollMappingInit();
		ConsumableMapping.consumableMappingInit();
		LevelMapping.levelMappingInit();
		RoomMapping.roomMappingInit();
		MobMapping.mobMappingInit();
		EnchantmentsMapping.EnchantmentsMappingInit();
		GlyphsMapping.glyphMappingInit();
		SeedMapping.seedMappingInit();
	}

	public void onBackPressed() {
		templateHandler = TemplateHandler.getInstance(mapName, MapEditActivity.this);
		templateHandler.save(MapEditActivity.this);
		super.onBackPressed();
	}

	@Override
	public void onPause(){
		super.onPause();
		templateHandler = TemplateHandler.getInstance(mapName,MapEditActivity.this);
		templateHandler.save(MapEditActivity.this);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(MapEditActivity.this == null){
			finish();
		}
		if (mapName == null) {
			mapName = getIntent().getStringExtra(EXTRA_FILENAME);
		}
		setTitle(mapName);
		if (templateHandler == null || templateHandler.getDungeon() == null) {
			templateHandler = TemplateHandler.getInstance(mapName, MapEditActivity.this);
		}
		if(MobMapping.getAllNames() == null){
			initMappings();
		}
	}

	//	@Override
	//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	//		Log.d("TEST", "In item click");
	//		Intent intent = new Intent();
	//		intent.setClass(MapEditActivity.this,
	//		EnchantableItemsActivity.class);
	//		startActivity(intent);
	//		
	//	}
	//	
	//	public LevelTemplate currentLevel(){
	//		return template.levelTemplates.get(depth);
	//	}
	private OnLongClickListener deleteTab(){
		return new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MapEditActivity.this)
				.setTitle("Delete/Copy Floor?")
				.setPositiveButton("Delete", deleteConfirm(v))
				.setNegativeButton("Cancel", null)
				.setNeutralButton("Copy", copyConfirm(v));
				AlertDialog alert = builder.create(); 
				alert.show(); 
				return true;
			}};
	}
	private DialogInterface.OnClickListener deleteConfirm(final View v){
		return new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface d, int j) {
				if(mTabHost.getTabWidget().getTabCount() > 2){
					templateHandler = TemplateHandler.getInstance(mapName, MapEditActivity.this);
					templateHandler.getDungeon().levelTemplates.remove(mTabHost.getTabWidget().indexOfChild(v)-1);
					int temp = mTabHost.getTabWidget().getTabCount() -1;
					mTabHost.getTabWidget().removeAllViews();
					mTabHost.addTab(mTabHost.newTabSpec(TAB_ADD_FLOOR).setIndicator("Add floor", null),
							Fragment.class, null);
					for(int i = 1; i < temp; i++){
						mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(i)).setIndicator("Floor " + i, null),
								FloorFragment.class, null);
						mTabHost.getTabWidget().getChildTabViewAt(i).setLongClickable(true);
						mTabHost.getTabWidget().getChildTabViewAt(i).setOnLongClickListener(deleteTab());
						if(mTabHost.getCurrentTab() != i){
							mTabHost.setCurrentTab(i);
						}
					}
					//
				}
			}};
	}
	private DialogInterface.OnClickListener copyConfirm(final View v){
		return new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface d, int j) {
				int tabCount = mTabHost.getTabWidget().getTabCount();
				String tab_name = "Floor " + tabCount;

				templateHandler = TemplateHandler.getInstance(mapName, MapEditActivity.this);
				LevelTemplate copy = new LevelTemplate(templateHandler.getDungeon().
						             levelTemplates.get(mTabHost.getTabWidget().indexOfChild(v)-1));
				templateHandler.getDungeon().levelTemplates.add(copy);
				mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(tabCount)).setIndicator(tab_name, null),
						        FloorFragment.class, null);
				mTabHost.getTabWidget().getChildTabViewAt(tabCount).setLongClickable(true);
				mTabHost.getTabWidget().getChildTabViewAt(tabCount).setOnLongClickListener(deleteTab());
				mTabHost.setCurrentTab(tabCount);	
			}};
	}
}
