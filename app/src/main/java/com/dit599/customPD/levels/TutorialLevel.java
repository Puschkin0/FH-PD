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
package com.dit599.customPD.levels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

import com.dit599.customPD.Assets;
import com.dit599.customPD.Dungeon;
import com.dit599.customPD.DungeonTilemap;
import com.dit599.customPD.actors.Char;
import com.dit599.customPD.items.DewVial;
import com.dit599.customPD.items.bags.SeedPouch;
import com.dit599.customPD.levels.Room.Type;
import com.dit599.customPD.plants.Firebloom;
import com.dit599.customPD.scenes.GameScene;
import com.dit599.customPD.windows.WndStory;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Graph;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class TutorialLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	private boolean allRoomsPlaced;
	
	/**
	 * Modified from regularlevels's build() in order to read from different Type collections
	 * depending on current depth in the tutorial dungeon.
	 */
	@Override
	protected boolean build() {
		allRoomsPlaced = false;
		if (!initRooms()) {
			return false;
		}
		int distance;
		int retry = 0;
		int minDistance = (int)Math.sqrt( rooms.size() );
		do {
			do {
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4);
			do {
				roomExit = Random.element( rooms );
			} while (roomExit == roomEntrance || roomExit.width() < 4 || roomExit.height() < 4);

			Graph.buildDistanceMap( rooms, roomExit );
			distance = roomEntrance.distance();
			if (retry++ > 10) {
				return false;
			}

		} while (distance < minDistance);
		roomEntrance.type = Type.ENTRANCE;
		roomExit.type = Type.EXIT;

		HashSet<Room> connected = new HashSet<Room>();
		connected.add( roomEntrance );

		Graph.buildDistanceMap( rooms, roomExit );
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );

		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}

		Graph.setPrice( path, roomEntrance.distance );
		Log.d("TUTORIAL BUILD", "BEFORE DISTANCEMAP2" );
		Graph.buildDistanceMap( rooms, roomExit );
		Log.d("TUTORIAL BUILD", "BEFORE BUILDPATH2" );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );

		room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}

		int nConnected = (int)(rooms.size() * Random.Float( 0.5f, 0.7f ));
		Log.d("TUTORIAL BUILD", "BEFORE connected size while" );
		while (connected.size() < nConnected) {

			Room cr = Random.element( connected );
			Room or = Random.element( cr.neigbours );
			if (!connected.contains( or )) {

				cr.connect( or );
				connected.add( or );
			}
		}
		switch(Dungeon.depth){
		case 1:
			specials = new ArrayList<Room.Type>( Room.T_FLOOR1);
			break;
		case 2:
			specials = new ArrayList<Room.Type>( Room.T_FLOOR2 );
			break;
		case 3:
			specials = new ArrayList<Room.Type>( Room.T_FLOOR3 );
			break;
		}
		Log.d("TUTORIAL BUILD", "BEFORE ROOMTYPE" );
		assignRoomType();
		if(!allRoomsPlaced){
			return false;
		}
		Log.d("TUTORIAL BUILD", "AFTER ROOMTYPE" );
		paint();
		Log.d("TUTORIAL BUILD", "AFTER Paint" );
		paintWater();
		paintGrass();
		return true;
	}
	/**
	 * Heavily changed from assignRoomType() in regularlevel. This version ensures
	 * that all special rooms from the Type collection are placed on the level (even
	 * if it has to start over by generating a new level to place them on). This has 
	 * only been tested for up to 4 specials on a single floor.
	 */
	@Override
	protected void assignRoomType() {

		for (Room r : rooms) {
			if (r.type == Type.NULL && 
					r.connected.size() == 1) {

				if (specials.size() > 0 &&
						r.width() > 3 && r.height() > 3) {
					int n = specials.size();
					r.type = specials.get( Math.min( Random.Int( n ), Random.Int( n ) ) );			
					Room.useType( r.type );
					specials.remove( r.type );

				} else if (Random.Int( 2 ) == 0){

					HashSet<Room> neigbours = new HashSet<Room>();
					for (Room n : r.neigbours) {
						if (!r.connected.containsKey( n ) && 
								!(Room.SPECIALS.contains( n.type ) || Room.T_FLOOR1.contains( n.type ) ||
										Room.T_FLOOR2.contains( n.type ) || Room.T_FLOOR3.contains( n.type ))  &&
										n.type != Type.PIT) {

							neigbours.add( n );
						}
					}
					if (neigbours.size() > 1) {
						r.connect( Random.element( neigbours ) );
					}
				}
			}
		}
		if(specials.size() == 0){
			allRoomsPlaced = true;
			int count = 0;
			for (Room r : rooms) {
				if (r.type == Type.NULL) {
					int connections = r.connected.size();
					if (connections == 0) {

					} else if (Random.Int( connections * connections ) == 0) {
						r.type = Type.STANDARD;
						count++;
					} else {
						r.type = Type.TUNNEL; 
					}
				}
			}

			while (count < 4) {
				Room r = randomRoom( Type.TUNNEL, 1 );
				if (r != null) {
					r.type = Type.STANDARD;
					count++;
				}
			}
		}
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

	protected boolean[] water() {
		return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
	}

	protected boolean[] grass() {
		return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 4 );
	}

	@Override
	protected void decorate() {

		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&  
					map[i + WIDTH] == Terrain.WATER &&
					Random.Int( 4 ) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL && 
					map[i - WIDTH] == Terrain.WALL && 
					map[i + WIDTH] == Terrain.WATER &&
					Random.Int( 2 ) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) { 

				int count = 
						(map[i + 1] == Terrain.WALL ? 1 : 0) + 
						(map[i - 1] == Terrain.WALL ? 1 : 0) + 
						(map[i + WIDTH] == Terrain.WALL ? 1 : 0) +
						(map[i - WIDTH] == Terrain.WALL ? 1 : 0);

				if (Random.Int( 16 ) < count * count) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}

		while (true) {
			int pos = roomEntrance.random();
			if (pos != entrance) {
				map[pos] = Terrain.SIGN;
				break;
			}
		}
		while (true) {
			int pos = roomExit.random();
			if (pos != exit) {
				map[pos] = Terrain.SIGN;
				break;
			}
		}
	}
	/**
	 * Modified to disable random enemy spawns on tutorial levels. Kept this way so that
	 * spawns can be easily reenabled.
	 */
	@Override
	protected void createMobs() {
		if(!Dungeon.isTutorial){
			super.createMobs();
		}
	}

	/**
	 * Modified to guarantee that 2 fireseeds and 1 dewvial will appear on floor 1
	 * of the tutorial.
	 */
	@Override
	protected void createItems() {//Possibly spawn more floorspecific items?
		if(Dungeon.depth == 1){
			for (int i = 0; i< 2; i++){
				addItemToSpawn( new Firebloom.Seed() );
			}
			addItemToSpawn( new DewVial() );
			Dungeon.dewVial = false;
		}

		super.createItems(); //Maybe do more custom stuff instead of calling this
	}

	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}

	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Sink( i ) );
			}
		}
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Murky water";
		default:
			return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "Wet yellowish moss covers the floor.";
		case Terrain.BOOKSHELF:
			return "The bookshelf is packed with cheap useless books. Might it burn?";
		default:
			return super.tileDesc( tile );
		}
	}

	private static class Sink extends Emitter {

		private int pos;
		private float rippleDelay = 0;

		private static final Emitter.Factory factory = new Factory() {

			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				WaterParticle p = (WaterParticle)emitter.recycle( WaterParticle.class );
				p.reset( x, y );
			}
		};

		public Sink( int pos ) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 2, p.y + 1, 4, 0 );

			pour( factory, 0.05f );
		}

		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {

				super.update();

				if ((rippleDelay -= Game.elapsed) <= 0) {
					GameScene.ripple( pos + WIDTH ).y -= DungeonTilemap.SIZE / 2;
					rippleDelay = Random.Float( 0.2f, 0.3f );
				}
			}
		}
	}

	public static final class WaterParticle extends PixelParticle {

		public WaterParticle() {
			super();

			acc.y = 50;
			am = 0.5f;

			color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
			size( 2 );
		}

		public void reset( float x, float y ) {
			revive();

			this.x = x;
			this.y = y;

			speed.set( Random.Float( -2, +2 ), 0 );

			left = lifespan = 0.5f;
		}
	}
	@Override
	public void press( int cell, Char ch ) {
		super.press(cell, ch);
		if(this.room(cell)!= null && !this.room(cell).enteredRoom){
			this.room(cell).enteredRoom = true;
			this.room(cell).type.prompt();
		}
	}
}
