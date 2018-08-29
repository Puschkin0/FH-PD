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

import com.dit599.customPD.R;
import com.dit599.customPD.editorUI.Mappings.MobMapping;
import com.dit599.customPD.levels.template.DungeonTemplate;
import com.dit599.customPD.levels.template.LevelTemplate;
import com.watabou.noosa.Game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class MapMobItemActivity extends Activity {
	private Spinner mobtypespin,mobfrespin=null;
	private Button mobconfimbt,mobcancelbt=null;
	private ArrayAdapter<CharSequence> freqAdapter = null;
	private ArrayAdapter<String> typeAdapter=null;
	private static String mobtypeinfo=null;
	private static int moblevelinfo=0;
	LevelTemplate level;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		level = TemplateHandler.getInstance(getIntent().getStringExtra(MapEditActivity.EXTRA_FILENAME), MapMobItemActivity.this)
				.getLevel(getIntent().getIntExtra(EnchantableItemsActivity.EXTRA_DEPTH, 0));
		super.setContentView(R.layout.mobitem); 
		MapMobItemActivity.this.mobconfimbt=(Button) MapMobItemActivity.this.findViewById(R.id.mobconfirmbutton);
		MapMobItemActivity.this.mobcancelbt=(Button) MapMobItemActivity.this.findViewById(R.id.mobcancelbutton);

		MapMobItemActivity.this.mobconfimbt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				level.mobs.set(getIntent().getIntExtra("mobIndex", 0), 
				               level.new MobProbability(MobMapping.getMobClass(mobtypeinfo), moblevelinfo));
				finish();
			}});

		MapMobItemActivity.this.mobcancelbt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				onBackPressed();
			}});
		MapMobItemActivity.this.mobtypespin=(Spinner)MapMobItemActivity.this.findViewById(R.id.mobtypespinner);
		MapMobItemActivity.this.mobfrespin=(Spinner)MapMobItemActivity.this.findViewById(R.id.mobfrespinner);

		MobMapping.mobMappingInit();

		typeAdapter = new ArrayAdapter<String>(MapMobItemActivity.this, android.R.layout.simple_spinner_item, MobMapping.getAllNames());
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		mobtypespin.setAdapter(typeAdapter); 
		mobtypespin.setSelection(MobMapping.getAllNames().indexOf(MobMapping.getMobName(level
				                 .mobs.get(getIntent().getIntExtra("mobIndex", 0)).mobClass)));
		mobtypeinfo = mobtypespin.getSelectedItem().toString();
		mobtypespin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mobtypeinfo = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}}); 

		freqAdapter = ArrayAdapter.createFromResource(MapMobItemActivity.this, 
				R.array.mobfrequ, android.R.layout.simple_spinner_item); 
		freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		mobfrespin.setAdapter(freqAdapter); 
		mobfrespin.setSelection(level.mobs.get(getIntent().getIntExtra("mobIndex", 0)).weight);
		moblevelinfo = mobfrespin.getSelectedItemPosition();
		mobfrespin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				moblevelinfo = position;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}}); 

	}	
	@Override
	public void onStart(){
		super.onStart();
		if (level == null){
			level = TemplateHandler.getInstance(getIntent().getStringExtra(MapEditActivity.EXTRA_FILENAME), MapMobItemActivity.this)
					.getLevel(getIntent().getIntExtra(EnchantableItemsActivity.EXTRA_DEPTH, 0));
		}
		if(MobMapping.getAllNames() == null){
			MobMapping.mobMappingInit();
		}
	}

}
