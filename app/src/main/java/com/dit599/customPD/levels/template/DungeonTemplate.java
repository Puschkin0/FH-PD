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
package com.dit599.customPD.levels.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class DungeonTemplate implements Bundlable{
    public static final int MAX_DEPTH = 30;
    public String name;

    /**
     * The levelTemplates are 0-indexed, so the level at depth 1 has index 0.
     */
    public List<LevelTemplate> levelTemplates = new ArrayList<LevelTemplate>(MAX_DEPTH);
    public DungeonTemplate() {
//    	for(int i = 0; i < MAX_DEPTH; i++){
//			levelTemplates.add(new LevelTemplate());
//    	}
    }

    public DungeonTemplate(String file, Context c) {
        load(file, c);
    }

    public void reset(){
    	levelTemplates = new ArrayList<LevelTemplate>(MAX_DEPTH);
    	for(int i = 0; i < MAX_DEPTH; i++){
			levelTemplates.add(new LevelTemplate());
    	}
    }
	@Override
	public void restoreFromBundle(Bundle bundle) {
		levelTemplates = new ArrayList<LevelTemplate>(MAX_DEPTH);
		levelTemplates.addAll((Collection<? extends LevelTemplate>) bundle.getCollection("levelTemplates"));
		name = bundle.getString("templateName");
		Log.d("DTRESTORE", levelTemplates.size() + "");
	}
	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put("levelTemplates", levelTemplates );
		bundle.put("templateName", name);
		Log.d("DTSTORE", levelTemplates.size() + "");
	}


	public void save(String file, Context c) {
		Bundle bundle = new Bundle();
		name = file;
		storeInBundle(bundle);
		OutputStream output;
		try {
			File temp = null;
			if(file.contains("/")){
				temp = new File(file + ".map");
				temp.canWrite();
				temp.setWritable(true);
				output = new FileOutputStream(temp);
			}
			else{
				output = c.openFileOutput(name + ".map", Game.MODE_PRIVATE );
			}
			Bundle.write( bundle, output );
			output.close();
			
			if(file.contains("/")){
				if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
					Intent mediaScanIntent = new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					String mCurrentFilePath = "file://" + temp.getPath();
					File f = new File(mCurrentFilePath);
					Uri contentUri = Uri.fromFile(f);
					mediaScanIntent.setData(contentUri);
					Game.instance.sendBroadcast(mediaScanIntent);
				} else {
					Game.instance.sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_MOUNTED,
							Uri.parse("file://"
									+ Environment.getExternalStorageDirectory())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void load(String file, Context c) {
		Bundle  bundle = null;
		name = file;
		InputStream input; 
		try {
			if(file.contains("/")){
				File temp = new File(file + ".map");
				input = new FileInputStream(temp);
			}
			else{
				input = c.openFileInput(name + ".map");
			}
			bundle = Bundle.read( input );
			input.close();
			if(bundle == null){
				bundle = new Bundle();
			}
			restoreFromBundle(bundle);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
