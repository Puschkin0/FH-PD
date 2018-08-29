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
import com.dit599.customPD.editorUI.Mappings.ConsumableMapping;
import com.dit599.customPD.editorUI.Mappings.PotionMapping;
import com.dit599.customPD.editorUI.Mappings.RoomMapping;
import com.dit599.customPD.editorUI.Mappings.ScrollMapping;
import com.dit599.customPD.editorUI.Mappings.SeedMapping;
import com.dit599.customPD.items.Ankh;
import com.dit599.customPD.items.Gold;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.potions.Potion;
import com.dit599.customPD.items.potions.PotionOfHealing;
import com.dit599.customPD.items.scrolls.Scroll;
import com.dit599.customPD.items.scrolls.ScrollOfIdentify;
import com.dit599.customPD.levels.Room;
import com.dit599.customPD.levels.template.LevelTemplate;
import com.dit599.customPD.plants.Dreamweed;
import com.dit599.customPD.plants.Plant;

import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ItemAdapter extends BaseAdapter {
	public ArrayAdapter<String> adapter;
	private int mItemCount = 0;
	private LevelTemplate level;
	private ItemsActivity activity;
	private String type;
	private static final int MAX_SPECIALS = 4;

	Context context;

	public ItemAdapter(Context context, LevelTemplate l, ItemsActivity a, String t) {
		this.context = context;
		this.level = l;
		this.activity = a;
		this.type = t;
	}

	@Override
	public int getCount() {
		return mItemCount;
	}

	public void addItem(boolean inLists) {
		if(inLists){
			if(type.equals("Potions")){
				level.potions.add(PotionOfHealing.class);
			}
			else if(type.equals("Scrolls")){
				level.scrolls.add(ScrollOfIdentify.class);		
			}
			else if(type.equals("Rooms")){
				level.specialRooms.add(Room.Type.ARMORY);		
			}
			else if(type.equals("Seeds")){
				level.seeds.add(new Dreamweed.Seed());		
			}
			else if(type.equals("Other")){
				level.consumables.add(new Ankh());		
			}
		}
		mItemCount++;
		//Max specialrooms reached
		if(type.equals("Rooms") && level.specialRooms.size() == MAX_SPECIALS){
			activity.addButton.setEnabled(false);
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
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
		Spinner spin;
		if(convertView != null){
			spin=(Spinner)convertView.findViewById(R.id.itemtypespinner);
			Button delete = (Button) convertView.findViewById(R.id.itemdeletbt); 
			delete.setOnClickListener(deleteItem(position));
		}
		else{
			convertView = inflater.inflate(R.layout.customizable_item2, null);
			spin=(Spinner)convertView.findViewById(R.id.itemtypespinner);
			Button delete = (Button) convertView.findViewById(R.id.itemdeletbt); 
			delete.setOnClickListener(deleteItem(position));
		}
		
		ItemTypesFilter(spin, position);
		return convertView;
	}
	public void ItemTypesFilter(Spinner spin, final int pos)
	{
		if(type.equals("Potions")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, PotionMapping.getAllNames());    	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		}else if(type.equals("Scrolls")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, ScrollMapping.getAllNames());    	   	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		}else if(type.equals("Rooms")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, RoomMapping.getAllNames());    	 	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		}else if(type.equals("Seeds")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, SeedMapping.getAllNames());    	   	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		}else if(type.equals("Other")){
			adapter= new ArrayAdapter<String> (context, R.layout.item_spinner, ConsumableMapping.getAllNames());    	    	
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


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


	}
	private int getType(int position){
		if(type.equals("Potions")){
			return PotionMapping.getAllNames().indexOf(
					PotionMapping.getPotionName(level.potions.get(position)));			
		}
		else if(type.equals("Scrolls")){
			return ScrollMapping.getAllNames().indexOf(
					ScrollMapping.getScrollName(level.scrolls.get(position)));	
		}
		else if(type.equals("Rooms")){
			return RoomMapping.getAllNames().indexOf(
					RoomMapping.getRoomName(level.specialRooms.get(position)));	
		}
		else if(type.equals("Seeds")){
			return SeedMapping.getAllNames().indexOf(
					SeedMapping.getSeedName((level.seeds.get(position)).getClass()));	
		}
		else if(type.equals("Other")){
			return ConsumableMapping.getAllNames().indexOf(
					ConsumableMapping.getConsumableName((level.consumables.get(position)).getClass()));		
		}
		return 0;
	}
	private void setType(int position, String subClass){
		try{
			if(type.equals("Potions")){
				level.potions.remove(position);
				level.potions.add(position, (Class<? extends Potion>) PotionMapping.getPotionClass(subClass));			
			}
			else if(type.equals("Scrolls")){
				level.scrolls.remove(position);
				level.scrolls.add(position, (Class<? extends Scroll>) ScrollMapping.getScrollClass(subClass));		
			}
			else if(type.equals("Rooms")){
				level.specialRooms.remove(position);
				level.specialRooms.add(position, RoomMapping.getRoomType(subClass));		
			}
			else if(type.equals("Seeds")){
				level.seeds.remove(position); 
				level.seeds.add(position, (Plant.Seed) SeedMapping.getSeedClass(subClass).newInstance());		
			}
			else if(type.equals("Other")){
				Item temp = (Item) ConsumableMapping.getConsumableClass(subClass).newInstance();
				if(temp instanceof Gold){
					temp = temp.random();
				}
				level.consumables.remove(position);	
				level.consumables.add(position, temp);		
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
				if(type.equals("Potions")){
					level.potions.remove(pos);
				}
				else if(type.equals("Scrolls")){
					level.scrolls.remove(pos);	
				}
				else if(type.equals("Rooms")){
					level.specialRooms.remove(pos);		
					activity.addButton.setEnabled(true);
				}
				else if(type.equals("Seeds")){
					level.seeds.remove(pos);	
				}
				else if(type.equals("Other")){
					level.consumables.remove(pos);	
				}
				mItemCount--;
				ItemAdapter.this.notifyDataSetChanged();
			}
		};
	}
}
