/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ISM {

    public static ItemStack getItem(Material mat) {
        return new ItemStack(mat);
    }

    public static ItemStack getItem(Material mat, Integer amount) {
        return new ItemStack(mat, amount.intValue());
    }

    public static ItemStack getItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(Material mat, String name, int data) {
        ItemStack item = new ItemStack(mat, 1, (byte) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(Material mat, String name, short data) {
        ItemStack item = new ItemStack(mat, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(Material mat, String name, Integer amount) {
        ItemStack item = new ItemStack(mat, amount.intValue());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(Material mat, String name, String... lore) {
        List<String> lorelist = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String lorec = arrayOfString[i];
            lorelist.add(lorec);
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lorelist);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(Material mat, String name, Integer amount, String... lore) {
        List<String> lorelist = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String lorec = arrayOfString[i];
            lorelist.add(lorec);
        }
        ItemStack item = new ItemStack(mat, amount.intValue());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lorelist);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkull() {
        return new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    }

    public static ItemStack getSkull(Integer amount) {
        return new ItemStack(Material.SKULL_ITEM, amount.intValue(), (short) 3);
    }

    public static ItemStack getSkull(String name) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkull(String name, Integer amount) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount.intValue(), (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkull(String name, String... lore) {
        List<String> lorelist = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String lorec = arrayOfString[i];
            lorelist.add(lorec);
        }
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lorelist);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkull(String name, Integer amount, String... lore) {
        List<String> lorelist = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String lorec = arrayOfString[i];
            lorelist.add(lorec);
        }
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount.intValue(), (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lorelist);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkinSkull(String owner) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkinSkull(Integer amount, String owner) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount.intValue(), (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkinSkull(String name, String owner) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkinSkull(String name, Integer amount, String owner) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount.intValue(), (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkinSkull(String name, String owner, String... lore) {
        List<String> lorelist = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String lorec = arrayOfString[i];
            lorelist.add(lorec);
        }
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(name);
        meta.setLore(lorelist);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkinSkull(String name, Integer amount, String owner, String... lore) {
        List<String> lorelist = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String lorec = arrayOfString[i];
            lorelist.add(lorec);
        }
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount.intValue(), (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(name);
        meta.setLore(lorelist);
        item.setItemMeta(meta);

        return item;
    }

    // UTILS
    public static ItemStack setDisplayName(ItemStack stack, String displayName) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        stack.setItemMeta(meta);
        return stack;
    }

    public static String getDisplayName(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName()) {
            return "";
        }

        return stack.getItemMeta().getDisplayName();
    }

    @SuppressWarnings("deprecation")
    public static boolean isSimilar(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {

        } else if (stack1.hasItemMeta() != stack2.hasItemMeta()) {
            return false;
        } else if (stack1.getType() != stack2.getType()) {
            return false;
        } else if (stack1.getItemMeta().hasDisplayName() != stack2.getItemMeta().hasDisplayName()) {
            return false;
        } else if (!stack1.getItemMeta().getDisplayName().equals(stack1.getItemMeta().getDisplayName())) {
            return false;
        } else if (stack1.getAmount() != stack2.getAmount()) {
            return false;
        } else if (stack1.getData().getData() != stack2.getData().getData()) {
            return false;
        }

        return true;
    }
}
