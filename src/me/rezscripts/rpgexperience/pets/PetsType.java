package me.rezscripts.rpgexperience.pets;

import me.rezscripts.rpgexperience.utils.REntities;
import me.rezscripts.rpgexperience.utils.RHead;
import me.rezscripts.rpgexperience.utils.entities.CustomMushroomCow;
import me.rezscripts.rpgexperience.utils.entities.CustomZombie;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;


public enum PetsType {
    BABY_MOOSHROOM("Baby Mooshroom", new ItemStack(Material.RED_MUSHROOM), (p, loc) -> {
        LivingEntity le = REntities.createLivingEntity(CustomMushroomCow.class, loc);
        ((Ageable) le).setAgeLock(true);
        ((Ageable) le).setBaby();
        return le;
    }),
    LIL_ME("Lil' Me", null, (p, loc) -> {
        LivingEntity le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
        le.setCustomName(ChatColor.YELLOW + "Lil' " + p.getName());
        le.setCustomNameVisible(true);
        ((Zombie) le).setBaby(true);
        ((Zombie) le).setVillagerProfession(Profession.HUSK);

        EntityEquipment ee = le.getEquipment();
        ee.setHelmet(RHead.getPlayerSkull(p.getName()));
        ee.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        ee.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        ee.setBoots(new ItemStack(Material.IRON_BOOTS));
        return le;
    }),
    BABY_XERTY("Baby Xerty", RHead.getPlayerSkull("Xerty"), (p, loc) -> {
        LivingEntity le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
        le.setCustomName(ChatColor.YELLOW + "Baby Xerty");
        le.setCustomNameVisible(true);
        ((Zombie) le).setBaby(true);
        ((Zombie) le).setVillagerProfession(Profession.HUSK);

        EntityEquipment ee = le.getEquipment();
        ee.setHelmet(RHead.getPlayerSkull("Xerty"));
        ee.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        ee.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        ee.setBoots(new ItemStack(Material.IRON_BOOTS));
        return le;
    })

    ;

    public String display;
    protected ItemStack item;
    private PetTypeSpawner spawner;

    PetsType(String display, ItemStack item, PetTypeSpawner spawner) {
        this.display = display;
        this.item = item;
        this.spawner = spawner;
    }

    protected LivingEntity spawn(Player owner, Location loc) {
        return spawner.spawn(owner, loc);
    }

    @FunctionalInterface
    private static interface PetTypeSpawner {
        public LivingEntity spawn(Player p, Location loc);
    }
}