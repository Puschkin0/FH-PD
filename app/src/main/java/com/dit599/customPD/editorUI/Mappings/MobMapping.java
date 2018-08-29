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
package com.dit599.customPD.editorUI.Mappings;

import java.util.ArrayList;
import java.util.List;

import com.dit599.customPD.actors.mobs.*;

public abstract class MobMapping {
	private static List<Class<? extends Mob>> mobsclasslist = null;
	private static List<String> mobsnamelist = null;

	public static void mobMappingInit() {
		mobsclasslist = new ArrayList<Class<? extends Mob>>();
		mobsclasslist.add(Rat.class);
		mobsclasslist.add(Albino.class);
		mobsclasslist.add(Gnoll.class);
		mobsclasslist.add(Crab.class);
		mobsclasslist.add(Swarm.class);
		mobsclasslist.add(Skeleton.class);
		mobsclasslist.add(Thief.class);
		mobsclasslist.add(Bandit.class);
		mobsclasslist.add(Shaman.class);
		mobsclasslist.add(Bat.class);
		mobsclasslist.add(Brute.class);
		mobsclasslist.add(Shielded.class);
		mobsclasslist.add(Spinner.class);
		mobsclasslist.add(Elemental.class);
		mobsclasslist.add(Monk.class);
		mobsclasslist.add(Senior.class);
		mobsclasslist.add(Warlock.class);
		mobsclasslist.add(Golem.class);
		mobsclasslist.add(Succubus.class);
		mobsclasslist.add(Eye.class);
		mobsclasslist.add(Scorpio.class);
		mobsclasslist.add(Acidic.class);
		//mobsclasslist.add(Mimic.class);
		//mobsclasslist.add(DM300.class);
		//mobsclasslist.add(Goo.class);
		//mobsclasslist.add(King.class);
		//mobsclasslist.add(Piranha.class);
		//mobsclasslist.add(Statue.class);
		//mobsclasslist.add(Tengu.class);
		//mobsclasslist.add(Wraith.class);
		//mobsclasslist.add(Yog.class);

		mobsnamelist = new ArrayList<String>();
		mobsnamelist.add("Rat");
		mobsnamelist.add("Albino Rat");
		mobsnamelist.add("Gnoll");
		mobsnamelist.add("Crab");
		mobsnamelist.add("Swarm of Flies");
		mobsnamelist.add("Skeleton");
		mobsnamelist.add("Thief");
		mobsnamelist.add("Bandit");
		mobsnamelist.add("Shaman");
		mobsnamelist.add("Bat");
		mobsnamelist.add("Brute");
		mobsnamelist.add("Shielded Brute");
		mobsnamelist.add("Cave Spinner");
		mobsnamelist.add("Elemental");
		mobsnamelist.add("Monk");
		mobsnamelist.add("Senior Monk");
		mobsnamelist.add("Warlock");
		mobsnamelist.add("Golem");
		mobsnamelist.add("Succubus");
		mobsnamelist.add("Evil Eye");
		mobsnamelist.add("Scorpio");
		mobsnamelist.add("Acidic Scorpio");
		//mobsnamelist.add("Mimic");
		//mobsnamelist.add("DM300");
		//mobsnamelist.add("Goo");
		//mobsnamelist.add("King");
		//mobsnamelist.add("Piranha");
		//mobsnamelist.add("Statue");
		//mobsnamelist.add("Tengu");
		//mobsnamelist.add("Wraith");
		//mobsnamelist.add("Yog");
	}

	public static String getMobName(Class<? extends Mob> mob) {
		for(int i=0;i<mobsclasslist.size();i++)
		 {
			if(mobsclasslist.get(i).getName().equals(mob.getName()))
			{
				return mobsnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Mob> getMobClass(String mobname) {
		for(int i=0;i<mobsnamelist.size();i++)
		 {
			if(mobsnamelist.get(i).equals(mobname))
			{
				return mobsclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return mobsnamelist;
		
	}
}
