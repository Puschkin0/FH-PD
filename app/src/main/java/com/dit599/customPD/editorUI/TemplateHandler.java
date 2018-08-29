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

import java.io.FileNotFoundException;
import java.util.HashMap;

import android.content.Context;

import com.dit599.customPD.levels.template.DungeonTemplate;
import com.dit599.customPD.levels.template.LevelTemplate;
import com.dit599.customPD.levels.template.TemplateFactory;

/**
 * 
 * @author Anton
 *
 *         Class is not designed to be thread-safe
 */
public class TemplateHandler {

    private static HashMap<String, TemplateHandler> instances = new HashMap<String, TemplateHandler>();

    private String mapName;
    private DungeonTemplate template;
    private LevelTemplate current;

    /**
     * Provedes an instance associated with the map name. Handles reading from
     * and to files.
     * 
     * @param mapName
     * @return
     * @throws FileNotFoundException
     */
    public static TemplateHandler getInstance(String mapName, Context c) {
        TemplateHandler instance = instances.get(mapName);
        if (instance == null) {
            instance = new TemplateHandler(mapName, c);
            instances.put(mapName, instance);
        }
        return instance;
    }

    private TemplateHandler(String mapName, Context c) {
        this.mapName = mapName;
        loadFromFile(c);
    }

    /**
     * Synchronously loads the specified template.
     */
    private void loadFromFile(Context c) {
        setTemplate(TemplateFactory.createSimpleDungeon(mapName, c));
    }

    public void save(Context c) {
    	getTemplate().save(mapName, c);
    }

    public DungeonTemplate getDungeon() {
        return getTemplate();
    }

    /**
     * 
     * @param depth
     *            the 1-indexed depth
     * @return
     */
    public LevelTemplate getLevel(int depth) {
        return getTemplate().levelTemplates.get(depth - 1); // handle 1-index
    }

	public DungeonTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DungeonTemplate template) {
		this.template = template;
	}
	public void addLevel(){
		this.template.levelTemplates.add(TemplateFactory.createSimpleLevel());
	}
	public static void reset(String mapName){
		instances.put(mapName, null);
	}
	public static void resetAll(){
		instances = new HashMap<String, TemplateHandler>();
	}
}
