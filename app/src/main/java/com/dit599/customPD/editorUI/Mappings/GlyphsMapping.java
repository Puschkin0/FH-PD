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

import com.dit599.customPD.items.armor.Armor.Glyph;
import com.dit599.customPD.items.armor.glyphs.*;

public abstract class GlyphsMapping {
	private static List<Class<? extends Glyph>> glyphclasslist = null;
	private static List<String> glyphnamelist = null;

	
	public static void glyphMappingInit() {
		glyphclasslist = new ArrayList<Class<? extends Glyph>>();
		glyphclasslist.add(null);
		glyphclasslist.add(Affection.class);
		glyphclasslist.add(AntiEntropy.class);
		glyphclasslist.add(Bounce.class);
		glyphclasslist.add(Displacement.class);
		glyphclasslist.add(Entanglement.class);
		glyphclasslist.add(Metabolism.class);
		glyphclasslist.add(Multiplicity.class);
		glyphclasslist.add(Potential.class);
		glyphclasslist.add(Stench.class);
		glyphclasslist.add(Viscosity.class);

		glyphnamelist = new ArrayList<String>();
		glyphnamelist.add("No Glyph");
		glyphnamelist.add("Affection");
		glyphnamelist.add("Anti-Entropy");
		glyphnamelist.add("Bounce");
		glyphnamelist.add("Displacement");
		glyphnamelist.add("Entanglement");
		glyphnamelist.add("Metabolism");
		glyphnamelist.add("Multiplicity");
		glyphnamelist.add("Potential");
		glyphnamelist.add("Stench");
		glyphnamelist.add("Viscosity");
	}

	public static String getGlyphName(Class<? extends Glyph> glyph) {
		for(int i=0;i<glyphclasslist.size();i++)
		 {
			if(glyphclasslist.get(i) != null && glyphclasslist.get(i).getName().equals(glyph.getName()))
			{
				return glyphnamelist.get(i);
			}
		 }
		return null;
	}
	public static Class<? extends Glyph> getGlyphClass(String glyphname) {
		for(int i=0;i<glyphnamelist.size();i++)
		 {
			if(glyphnamelist.get(i).equals(glyphname))
			{
				return glyphclasslist.get(i);
			}
		 }
		return null;
	}
	public static List<String> getAllNames()
	{
		return glyphnamelist;
		
	}
}
