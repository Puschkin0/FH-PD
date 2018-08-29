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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dit599.customPD.R;
import com.dit599.customPD.editorUI.Mappings.RingMapping;
import com.dit599.customPD.editorUI.Mappings.WandMapping;
import com.dit599.customPD.items.Ankh;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.Potion;
import com.dit599.customPD.items.potions.PotionOfHealing;
import com.dit599.customPD.items.rings.*;
import com.dit599.customPD.items.scrolls.Scroll;
import com.dit599.customPD.items.scrolls.ScrollOfIdentify;
import com.dit599.customPD.items.wands.Wand;
import com.dit599.customPD.items.wands.WandOfAmok;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.template.LevelTemplate;
import com.dit599.customPD.levels.template.LevelTemplate.MagicItem;
import com.dit599.customPD.plants.Dreamweed;
import com.dit599.customPD.plants.Plant;

import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MagicItemAdapter extends BaseAdapter {
	public ArrayAdapter<String> adapter,leveladapter;
	private int mItemCount = 0;
	private LevelTemplate level;
	private String type;
	private MagicItemsActivity activity;
	public static final String[] MAGICITEM_LEVEL = {"-2", "-1",  "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "10"};
	Context context;

	public MagicItemAdapter(Context context, LevelTemplate l, MagicItemsActivity a, String t) {
		this.context = context;
		this.level = l;
		this.activity = a;
		this.type = t;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemCount;
	}

	public void addItem(boolean inLists) {
		if(inLists){
			if(type.equals("Wands")){
				MagicItem wand =new MagicItem(WandOfAmok.class,0,false);
				level.wands.add(wand);
			   
			}
			else if(type.equals("Rings")){
				MagicItem ring=new MagicItem(RingOfAccuracy.class,0,false);	
				level.rings.add(ring);
			}
			
			
		}
		mItemCount++;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub    	
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		Spinner typespin;
		Spinner levelspin;
		if(convertView != null){
			typespin=(Spinner)convertView.findViewById(R.id.wand_ring_typespinner);
			levelspin=(Spinner)convertView.findViewById(R.id.wand_ring_level_spinner);
			Button delete = (Button) convertView.findViewById(R.id.wand_ring_deletbt); 
			delete.setOnClickListener(deleteItem(position));
		}
		else{
			convertView = inflater.inflate(R.layout.customizable_item3, null);
			typespin=(Spinner)convertView.findViewById(R.id.wand_ring_typespinner);
			levelspin=(Spinner)convertView.findViewById(R.id.wand_ring_level_spinner);
			Button delete = (Button) convertView.findViewById(R.id.wand_ring_deletbt); 
			delete.setOnClickListener(deleteItem(position));
		}
		ItemTypesFilter(typespin, levelspin,position);
		return convertView;
	}
	public void ItemTypesFilter(Spinner spin,Spinner levelspin, final int pos)
	{
		if(type.equals("Wands")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, WandMapping.getAllNames());    	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			leveladapter= new ArrayAdapter<String> (context, R.layout.item_spinner, MAGICITEM_LEVEL);    	
			leveladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		}else if(type.equals("Rings")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, RingMapping.getAllNames());    	   	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			leveladapter= new ArrayAdapter<String> (context, R.layout.item_spinner, MAGICITEM_LEVEL);    	
			leveladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



		}else {

		}
		spin.setAdapter(adapter);
		spin.setSelection(getType(pos));
		spin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String type =parent.getItemAtPosition(position).toString();
				setType(pos, type);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}}); 
		
		levelspin.setAdapter(leveladapter);
		levelspin.setSelection(leveladapter.getPosition(getLevel(pos)));
		levelspin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String choice =parent.getItemAtPosition(position).toString();
				setLevel(pos, choice);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}}); 


	}
	private int getType(int position){
		if(type.equals("Wands")){
			return WandMapping.getAllNames().indexOf(
					WandMapping.getWandName(level.wands.get(position).itemClass));			
		}
		else if(type.equals("Rings")){
			return RingMapping.getAllNames().indexOf(
					RingMapping.getRingName(level.rings.get(position).itemClass));	
		}
		
		return 0;
	}
	private void setType(int position, String subClass){
		try{
			if(type.equals("Wands")){
				MagicItem wand=new MagicItem(WandMapping.getWandClass(subClass),0,false);
				level.wands.set(position, wand);			
			}
			else if(type.equals("Rings")){
				MagicItem ring=new MagicItem(RingMapping.getRingClass(subClass),0,false);
				level.rings.set(position, ring);		
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	private String getLevel(int position){
		if(type.equals("Wands")){
			return level.wands.get(position).level + "";			
		}
		else if(type.equals("Rings")){
			return level.rings.get(position).level + "";
		}		
		return "0";
	}
	private void setLevel(int position, String choice){
		try{
			if(type.equals("Wands")){
				level.wands.get(position).level = Integer.parseInt(choice);		
			}
			else if(type.equals("Rings")){
				level.rings.get(position).level = Integer.parseInt(choice);			
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	private OnClickListener deleteItem(final int pos){
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(type.equals("Wands")){
					level.wands.remove(pos);
				}
				else if(type.equals("Rings")){
					level.rings.remove(pos);	
				}
				
				mItemCount--;
				notifyDataSetChanged();
			}
		};
	}
}
