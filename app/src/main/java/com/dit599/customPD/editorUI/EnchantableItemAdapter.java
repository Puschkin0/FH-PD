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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dit599.customPD.R;
import com.dit599.customPD.editorUI.Mappings.ArmorMapping;
import com.dit599.customPD.editorUI.Mappings.EnchantmentsMapping;
import com.dit599.customPD.editorUI.Mappings.GlyphsMapping;
import com.dit599.customPD.editorUI.Mappings.WeaponMapping;
import com.dit599.customPD.items.Item;
import com.dit599.customPD.items.armor.Armor;
import com.dit599.customPD.items.armor.LeatherArmor;
import com.dit599.customPD.items.armor.Armor.Glyph;
import com.dit599.customPD.items.weapon.Weapon;
import com.dit599.customPD.items.weapon.Weapon.Enchantment;
import com.dit599.customPD.items.weapon.melee.Dagger;
import com.dit599.customPD.levels.template.LevelTemplate;

public class EnchantableItemAdapter extends BaseAdapter {

    private LevelTemplate level;
    private int type;

    Context context;

    private List<Item> activeList = new ArrayList<Item>();

    public EnchantableItemAdapter(Context context, LevelTemplate level, int type) {
        this.context = context;
        this.level = level;
        this.type = type;

        switch (type) {
        case EnchantableItemsActivity.WEAPON:
            activeList = level.weapons;
            break;
        case EnchantableItemsActivity.ARMOR:
            activeList = level.armor;
            break;
        }
    }

    @Override
    public int getCount() {
        return activeList.size();
    }

    public void addItem(boolean inLists) {
    	if(inLists){
        switch (type) {
        case EnchantableItemsActivity.WEAPON:
            activeList.add(new Dagger());
            break;
        case EnchantableItemsActivity.ARMOR:
            activeList.add(new LeatherArmor());
            break;
        }
    	}
        notifyDataSetChanged();
    }

    @Override
    public Item getItem(int position) {
        return activeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemViewHolder holder;
        if (convertView != null) {
            holder = (ItemViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.customizable_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.resetFields(position);
        return convertView;
    }

    private class ItemViewHolder {
        public int position;

        public Spinner typeSpn;
        public Spinner levelSpn;
        public Spinner enchantSpn;
        public Button deleteBtn;
        private ArrayAdapter<String> levelsAdapter;

        final List<String> weapons = WeaponMapping.getAllNames();
        List<String> enchants = EnchantmentsMapping.getAllNames();
        final List<String> armor = ArmorMapping.getAllNames();
        List<String> glyphs = GlyphsMapping.getAllNames();

        public ItemViewHolder(View root) {
            typeSpn = (Spinner) root.findViewById(R.id.item_type_spinner);
            levelSpn = (Spinner) root.findViewById(R.id.level_spinner);
            enchantSpn = (Spinner) root.findViewById(R.id.enchant_spinner);
            deleteBtn = (Button) root.findViewById(R.id.delete_btn);
            
            switch (type) {
            case EnchantableItemsActivity.WEAPON:

                ArrayAdapter<String> weaponAdapter = new ArrayAdapter<String>(context,
                        R.layout.item_spinner, weapons);
                weaponAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpn.setAdapter(weaponAdapter);
                typeSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int typeIndex,
                            long id) {
                        Weapon oldWeapon = (Weapon) getItem(ItemViewHolder.this.position);
                        Weapon newWeapon = null;
                        try {
                            newWeapon = (Weapon) WeaponMapping.getWeaponClass(
                                    weapons.get(typeIndex))
                                    .newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        newWeapon.level = oldWeapon.level;
                        newWeapon.enchantment = oldWeapon.enchantment;
                        activeList.remove(ItemViewHolder.this.position);
                        activeList.add(ItemViewHolder.this.position, newWeapon);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                
                
                ArrayAdapter<String> enchantmentsAdapter = new ArrayAdapter<String>(context,
                        R.layout.item_spinner, enchants);
                enchantmentsAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                enchantSpn.setAdapter(enchantmentsAdapter);
                enchantSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int enchantIndex,
                            long id) {
                        Weapon weapon = (Weapon) getItem(ItemViewHolder.this.position);
                        Class<? extends Enchantment> enchantment = EnchantmentsMapping
                                .getEnchantmentClass(EnchantmentsMapping.getAllNames().get(
                                        enchantIndex));
                        if (enchantment != null) {
                            try {
                                weapon.enchant(enchantment.newInstance());
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {
                            weapon.enchant(null);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                break;
            case EnchantableItemsActivity.ARMOR:
                ArrayAdapter<String> armorAdapter = new ArrayAdapter<String>(context,
                		R.layout.item_spinner, armor);
                armorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpn.setAdapter(armorAdapter);
                typeSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int typeIndex,
                            long id) {
                        Armor oldArmor = (Armor) getItem(ItemViewHolder.this.position);
                        Armor newArmor = null;
                        try {
                            newArmor = (Armor) ArmorMapping.getArmorClass(armor.get(typeIndex))
                                    .newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        newArmor.level = oldArmor.level;
                        newArmor.glyph = oldArmor.glyph;
                        activeList.remove(ItemViewHolder.this.position);
                        activeList.add(ItemViewHolder.this.position, newArmor);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                ArrayAdapter<String> glyphsAdapter = new ArrayAdapter<String>(context,
                		R.layout.item_spinner, glyphs);
                glyphsAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                enchantSpn.setAdapter(glyphsAdapter);
                enchantSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int glyphIndex,
                            long id) {
                        Armor armor = (Armor) getItem(ItemViewHolder.this.position);
                        Class<? extends Glyph> glyph = GlyphsMapping
                                .getGlyphClass(GlyphsMapping.getAllNames().get(
                                        glyphIndex));
                        if (glyph != null) {
                            try {
                                armor.inscribe(glyph.newInstance());
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {
                            armor.inscribe(null);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                break;
            }

            String[] levels = context.getResources().getStringArray(R.array.item_levels);
            levelsAdapter = new ArrayAdapter<String>(context, R.layout.item_spinner,
                    levels);
            levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            levelSpn.setAdapter(levelsAdapter);
            levelSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int levelIndex, long id) {
                    int level = Integer.valueOf((String) levelSpn.getSelectedItem());

                    if (type == EnchantableItemsActivity.WEAPON) {
                        Weapon weapon = (Weapon) getItem(ItemViewHolder.this.position);
                        weapon.level = level;
                    } else {
                        Armor armor = (Armor) getItem(ItemViewHolder.this.position);
                        armor.level = level;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            deleteBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activeList.remove(ItemViewHolder.this.position);
                    notifyDataSetChanged();
                }
            });

        }

        public void resetFields(int position) {

            this.position = position;

            switch (type) {
            case EnchantableItemsActivity.WEAPON:
                Weapon presentWeapon = (Weapon) getItem(position);

                List<String> names = WeaponMapping.getAllNames();
                typeSpn.setSelection(names.indexOf(WeaponMapping.getWeaponName(presentWeapon
                        .getClass())));

                if (presentWeapon.enchantment != null) {
                    int selection = enchants.indexOf(EnchantmentsMapping
                            .getEnchantmentName(presentWeapon.enchantment.getClass()));
                    enchantSpn.setSelection(selection);
                } else {
                    enchantSpn.setSelection(0);
                }

                levelSpn.setSelection(levelsAdapter.getPosition(String.valueOf(presentWeapon.level)));

                break;
            case EnchantableItemsActivity.ARMOR:
                Armor presentArmor = (Armor) getItem(position);

                List<String> armorNames = ArmorMapping.getAllNames();
                typeSpn.setSelection(armorNames.indexOf(ArmorMapping.getArmorName(presentArmor
                        .getClass())));

                if (presentArmor.glyph != null) {
                    int selection = glyphs.indexOf(GlyphsMapping.getGlyphName(presentArmor.glyph
                            .getClass()));
                    enchantSpn.setSelection(selection);
                } else {
                    enchantSpn.setSelection(0);
                }

                levelSpn.setSelection(levelsAdapter.getPosition(String.valueOf(presentArmor.level)));

                break;
            }
        }
    }
}
