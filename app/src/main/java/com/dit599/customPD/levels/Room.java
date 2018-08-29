/*
 * YourPD
 * Copyright (C) 2014 YourPD team
 * This is a modification of source code from: 
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.dit599.customPD.levels;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import android.util.Log;

import com.dit599.customPD.CustomPD;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.levels.painters.*;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.windows.WndMessage;
import com.dit599.customPD.windows.WndStory;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class Room extends Rect implements Graph.Node, Bundlable {
	
	public HashSet<Room> neigbours = new HashSet<Room>();
	public HashMap<Room, Door> connected = new HashMap<Room, Door>();
	
	public int distance;
	public int price = 1;
	public boolean enteredRoom = false;
	
	/**
	 * Has been extended with the 12 new tutorial Painter subclasses. 
	 */
	public static enum Type {
		NULL( null ),
		T_WELL_IDENTIFY (IdentifyWellPainter.class ),
		T_WELL_HEALTH (HealthWellPainter.class ),
		T_WELL_TRANSMUTE (TransmutationWellPainter.class ),
		T_POTION_ROOM (PotionRoomPainter.class ),
		T_SCROLL_ROOM (ScrollRoomPainter.class ),
		T_SEED_ROOM (SeedRoomPainter.class ),
		T_BAG_ROOM (BagRoomPainter.class ),
		T_ARMOR_ROOM (ArmorRoomPainter.class ),
		T_WEAPON_ROOM (WeaponRoomPainter.class ),
		T_GARDEN_ROOM (TutorialGardenPainter.class ),
		T_STATUE_ROOM (TutorialStatuePainter.class ),
		T_HARDHITTING_ROOM (HardHittingRoomPainter.class ),
		T_HARD_TO_HIT_ROOM (HardToHitRoomPainter.class ),
		//End of tutorial rooms
		STANDARD	( StandardPainter.class ),
		ENTRANCE	( EntrancePainter.class ),
		EXIT		( ExitPainter.class ),
		BOSS_EXIT	( BossExitPainter.class ),
		TUNNEL		( TunnelPainter.class ),
		PASSAGE		( PassagePainter.class ),
		SHOP		( ShopPainter.class ),
		BLACKSMITH	( BlacksmithPainter.class ),
		TREASURY	( TreasuryPainter.class ),
		ARMORY		( ArmoryPainter.class ),
		LIBRARY		( LibraryPainter.class ),
		LABORATORY	( LaboratoryPainter.class ),
		VAULT		( VaultPainter.class ),
		TRAPS		( TrapsPainter.class ),
		STORAGE		( StoragePainter.class ),
		MAGIC_WELL	( MagicWellPainter.class ),
		GARDEN		( GardenPainter.class ),
		CRYPT		( CryptPainter.class ),
		STATUE		( StatuePainter.class ),
		POOL		( PoolPainter.class ),
		RAT_KING	( RatKingPainter.class ),
		WEAK_FLOOR	( WeakFloorPainter.class ),
		PIT			( PitPainter.class );
		
		private Method paint;
		private Method tip;
		private Method prompt;
		
		/**
		 * Modified so that for each Painter type the code also tries to locate
		 * a tip and prompt method (originally it only searched for paint).
		 */
		private Type( Class<? extends Painter> painter ) {
			try {
				paint = painter.getMethod( "paint", Level.class, Room.class );
			} catch (Exception e) {
				paint = null;
			}
			try {//Separate trycatches so paint does not reset to null.
				tip = painter.getMethod("tip", null);
				prompt = painter.getMethod("prompt", null);
			} catch (Exception e) {
				//tip = null;
				//prompt = null;
			}
		}
		
		public void paint( Level level, Room room ) {
			try {
				Log.d("ROOM paint", room.type.name());
				paint.invoke( null, level, room );
			} catch (Exception e) {
				CustomPD.reportException( e );
			}
		}
		/**
		 * Displays the result of the Painter subclass' tip() method,
		 * or else the result of Dungeon's tip() if no method is found.  
		 */
		public void tip() {
			try {
				String s = (String) tip.invoke(null, null);
				GameScene.show(new WndMessage(s));
			} catch (Exception e) {
				Log.d("isnull", "tip method got set to null on first fail");
				GameScene.show( new WndMessage( Dungeon.tip() ) );
			}
		}
		/**
		 * Displays the result of the Painter subclass' prompt() method,
		 * if the method is found.  
		 */
		public void prompt() {
			try {
				String s = (String) prompt.invoke(null, null);
				WndStory.showChapter(s);
			} catch (Exception e) {
				Log.d("isnull", "prompt method got set to null on first fail");
			}
		}
	};
	
	public static final ArrayList<Type> SPECIALS = new ArrayList<Type>( Arrays.asList(
		Type.ARMORY, Type.WEAK_FLOOR, Type.MAGIC_WELL, Type.CRYPT, Type.POOL, Type.GARDEN, Type.LIBRARY,
		Type.TREASURY, Type.TRAPS, Type.STORAGE, Type.STATUE, Type.LABORATORY, Type.VAULT
	) );
	
	/**
	 * Rooms on tutorial floor 1.
	 */
	public static final ArrayList<Type> T_FLOOR1 = new ArrayList<Type>( Arrays.asList(
			Type.T_SCROLL_ROOM, Type.T_SEED_ROOM, Type.T_BAG_ROOM, Type.T_POTION_ROOM
		) );
	
	/**
	 * Rooms on tutorial floor 2.
	 */
	public static final ArrayList<Type> T_FLOOR2 = new ArrayList<Type>( Arrays.asList(
			Type.T_WELL_HEALTH, Type.T_WEAPON_ROOM, Type.T_ARMOR_ROOM, Type.T_WELL_IDENTIFY
		) );
	
	/**
	 * Rooms on tutorial floor 3.
	 */
	public static final ArrayList<Type> T_FLOOR3 = new ArrayList<Type>( Arrays.asList(
			Type.T_GARDEN_ROOM, Type.T_HARDHITTING_ROOM, Type.T_STATUE_ROOM, Type.T_HARD_TO_HIT_ROOM
		) );
	
	public Type type = Type.NULL;
	
	public int random() {
		return random( 0 );
	}
	
	public int random( int m ) {
		int x = Random.Int( left + 1 + m, right - m );
		int y = Random.Int( top + 1 + m, bottom - m );
		return x + y * Level.WIDTH;
	}
	
	public void addNeigbour( Room other ) {
		
		Rect i = intersect( other );
		if ((i.width() == 0 && i.height() >= 3) || 
			(i.height() == 0 && i.width() >= 3)) {
			neigbours.add( other );
			other.neigbours.add( this );
		}
		
	}
	
	public void connect( Room room ) {
		if (!connected.containsKey( room )) {	
			connected.put( room, null );
			room.connected.put( this, null );			
		}
	}
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
	
	public boolean inside( int p ) {
		int x = p % Level.WIDTH;
		int y = p / Level.WIDTH;
		return x > left && y > top && x < right && y < bottom;
	}
	
	public Point center() {
		return new Point( 
			(left + right) / 2 + (((right - left) & 1) == 1 ? Random.Int( 2 ) : 0),
			(top + bottom) / 2 + (((bottom - top) & 1) == 1 ? Random.Int( 2 ) : 0) );
	}
	
	// **** Graph.Node interface ****

	@Override
	public int distance() {
		return distance;
	}

	@Override
	public void distance( int value ) {
		distance = value;
	}
	
	@Override
	public int price() {
		return price;
	}

	@Override
	public void price( int value ) {
		price = value;
	}

	@Override
	public Collection<Room> edges() {
		return neigbours;
	} 
	
	// FIXME: use proper string constants
	
	@Override
	public void storeInBundle( Bundle bundle ) {	
		bundle.put( "left", left );
		bundle.put( "top", top );
		bundle.put( "right", right );
		bundle.put( "bottom", bottom );
		bundle.put( "enteredRoom", enteredRoom );
		bundle.put( "type", type.toString() );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		left = bundle.getInt( "left" );
		top = bundle.getInt( "top" );
		right = bundle.getInt( "right" );
		bottom = bundle.getInt( "bottom" );		
		enteredRoom = bundle.getBoolean("enteredRoom");
		type = Type.valueOf( bundle.getString( "type" ) );
	}
	
	public static void shuffleTypes() {
		int size = SPECIALS.size();
		for (int i=0; i < size - 1; i++) {
			int j = Random.Int( i, size );
			if (j != i) {
				Type t = SPECIALS.get( i );
				SPECIALS.set( i, SPECIALS.get( j ) );
				SPECIALS.set( j, t );
			}
		}
	}
	
	public static void useType( Type type ) {
		if (SPECIALS.remove( type )) {
			SPECIALS.add( type );
		}
	}
	
	private static final String ROOMS	= "rooms";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( ROOMS )) {
			SPECIALS.clear();
			for (String type : bundle.getStringArray( ROOMS )) {
				SPECIALS.add( Type.valueOf( type ));
			}
		} else {
			shuffleTypes();
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		String[] array = new String[SPECIALS.size()];
		for (int i=0; i < array.length; i++) {
			array[i] = SPECIALS.get( i ).toString();
		}
		bundle.put( ROOMS, array );
	}
	
	public static class Door extends Point {
		
		public static enum Type {
			EMPTY, TUNNEL, REGULAR, UNLOCKED, HIDDEN, BARRICADE, LOCKED
		}
		public Type type = Type.EMPTY;
		
		public Door( int x, int y ) {
			super( x, y );
		}
		
		public void set( Type type ) {
			if (type.compareTo( this.type ) > 0) {
				this.type = type;
			}
		}
	}
}
