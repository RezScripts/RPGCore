package me.rezscripts.rpgexperience.utils;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RUtils {

    public static boolean hasInventorySpace(Player p, ItemStack item) {
        int free = 0;
        for (int k = 0; k < 36; k++) {
            ItemStack i = p.getInventory().getItem( k );
            if (i == null) {
                free += item.getMaxStackSize();
            } else if (i.isSimilar( item )) {
                free += item.getMaxStackSize() - i.getAmount();
            }
        }
        return free >= item.getAmount();
    }

    public static String getPercecentBar(ChatColor r, int amount, int max) {
        int bars = 25;
        char line = '❚';
        //2 steps, 1 done
        StringBuilder stringBuilder = new StringBuilder();

        float percent = RMath.calculatePercentOfNumber( amount, max );
        int lines = RMath.calculatePercent( bars, percent );

        String prefix = ChatColor.BOLD + "" + r;
        String completedLines = new String( new char[lines] ).replace( '\0', line );
        String suffix = "";

        if (bars - lines > 1) {
            suffix = ChatColor.BOLD + "" + ChatColor.GRAY + new String( new char[(bars - lines)] ).replace( '\0', line );
        }

        return prefix + completedLines + suffix + "  " + ChatColor.GRAY + "(" + RMath.round2( percent, 1 ) + "%)";
    }

    static public Location getLocationString(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split( "=" );
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld( parts[0] );
            final int x = Integer.parseInt( parts[1] );
            final int y = Integer.parseInt( parts[2] );
            final int z = Integer.parseInt( parts[3] );
            return new Location( w, x, y, z );
        }
        return null;
    }

    public static boolean hasEmptySpaces(Player p, int count) {
        int empty = 0;
        for (int k = 0; k < 36; k++) {
            if (p.getInventory().getItem( k ) == null)
                empty++;
        }
        // code below is WRONG
        //        for (ItemStack i : p.getInventory().getContents()) {
        //            if (i == null) {
        //                empty++;
        //            }
        //        }
        return empty >= count;
    }

    private static final Color[] colors = {
            Color.AQUA,
            Color.BLACK,
            Color.BLUE,
            Color.FUCHSIA,
            Color.GRAY,
            Color.GREEN,
            Color.LIME,
            Color.MAROON,
            Color.NAVY,
            Color.OLIVE,
            Color.ORANGE,
            Color.PURPLE,
            Color.RED,
            Color.SILVER,
            Color.TEAL,
            Color.WHITE,
            Color.YELLOW
    };

    public static Color randomColor() {
        return colors[(int) (Math.random() * colors.length)];
    }

    public static void giveItem(Player p, ItemStack i) {
        String s = i.getItemMeta().hasDisplayName() ? i.getItemMeta().getDisplayName() : i.getType().name();
        p.sendMessage( ChatColor.translateAlternateColorCodes( '&', "&a + " + i.getAmount() + "x " + s ) );
        p.getInventory().addItem( i );
    }

    public static Integer[] convertIntegers(List<Integer> integers) {
        Integer[] ret = new Integer[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    public static ItemStack getItemFromYAML(ConfigurationSection f) {
        Material m = Material.getMaterial( f.getString( "Material" ) );

        ItemStack item = new ItemStack( m );

        if (m == Material.SKULL_ITEM) {
            ItemStack skull = new ItemStack( Material.SKULL_ITEM, 1, (byte) 3 );
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if (f.getString( "Owner" ) != null) {
                meta.setOwner( f.getString( "Owner" ) );
            }
            return skull;
        } else {

            if (f.getInt( "Amount" ) != 0) {
                item.setAmount( f.getInt( "Amount" ) );
            }

            if (f.getInt( "Data" ) != 0) {
                item.setData( new MaterialData( f.getInt( "Data" ) ) );
            }


            if (f.getInt( "Durability" ) != 0) {
                item.setDurability( (short) f.getInt( "Durability" ) );
            }

            ItemMeta im = item.getItemMeta();

            if (f.getString( "Display Name" ) != null) {
                im.setDisplayName(
                        ChatColor.translateAlternateColorCodes( '&', f.getString( "Display Name" ) ) );
            }
            ArrayList<String> lore = new ArrayList<String>();
            if (f.getConfigurationSection( "Lore" ) != null) {
                int o = 1;
                while (f.getConfigurationSection( "Lore" ).getString( "Line " + o ) != null) {
                    lore.add( ChatColor.translateAlternateColorCodes( '&',
                            f.getConfigurationSection( "Lore" ).getString( "Line " + o ) ) );
                    o += 1;
                }
            }
            im.setLore( lore );

            if (f.getConfigurationSection( "Enchants" ) != null) {
                int o = 1;
                while (f.getConfigurationSection( "Enchants" ).getString( "Enchant " + o ) != null) {
                    im.addEnchant( Enchantment.getByName( f.getConfigurationSection( "Enchants" ).getString( "Enchant " + o ).toLowerCase() ), 1, true );
                    o += 1;
                }
            }

            if (f.getString( "RGB" ) != null && im instanceof LeatherArmorMeta){
                String[] split = f.getString("RGB").split(",");
                int r = Integer.parseInt(split[0].trim());
                int g = Integer.parseInt(split[1].trim());
                int b = Integer.parseInt(split[2].trim());
                ((LeatherArmorMeta) im).setColor(org.bukkit.Color.fromRGB(r, g, b));
            }

            item.setItemMeta( im );
            return item;
        }
    }

    public static ItemStack getPlaceHolderItem(String ph, int amount) {
        switch (ph.replaceAll( "%", "" )) {
            case "gold":
                return new ItemBuilder( Material.GOLD_NUGGET ).setName( ChatColor.GOLD + "Gold: " + ChatColor.WHITE + amount ).addLoreLine( "" ).addLoreLine( ChatColor.GRAY + "Right-Click to redeem" ).toItemStack();
        }
        return null;
    }


}
