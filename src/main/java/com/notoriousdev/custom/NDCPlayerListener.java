package com.notoriousdev.custom;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class NDCPlayerListener implements Listener {

    public NDCustom plugin;

    public NDCPlayerListener(NDCustom plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event){
        event.setJoinMessage(ChatColor.GREEN + "Join: " + ChatColor.GOLD + event.getPlayer().getName());

    }
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event){
        event.setQuitMessage(ChatColor.RED + "Quit: " + ChatColor.GOLD + event.getPlayer().getName());

    }
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event){
        event.setLeaveMessage(ChatColor.RED + "Kick: " + ChatColor.GOLD + event.getPlayer().getName());

    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionThrow(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if((item.getType() == Material.POTION) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE)
        {
            player.sendMessage(ChatColor.RED + "You cannot use potions in creative!");
            event.setCancelled(true);
            player.getInventory().setItemInHand(null);
            player.updateInventory();
        }
        if((item.getType() == Material.MONSTER_EGG || item.getType() == Material.MONSTER_EGGS) && (event.getAction() == Action.RIGHT_CLICK_BLOCK))
        {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot use monster eggs in creative!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBowFire(EntityShootBowEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            if (player.getGameMode() == GameMode.CREATIVE)
            {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You may not use bows in creative!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDispenserInteract(InventoryClickEvent event)
    {
        Player player = (Player)event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            if (player.getGameMode() == GameMode.CREATIVE && event.getInventory().getType().equals(InventoryType.DISPENSER) && item.getType() == Material.POTION || item.getType() == Material.MONSTER_EGG || item.getType() == Material.MONSTER_EGGS && (event.isLeftClick() || event.isRightClick() || event.isShiftClick()))
            {
                event.setCancelled(true);
                event.setCursor(null);
                event.setCurrentItem(null);
                player.sendMessage(ChatColor.RED + "You may not put that into a dispenser!");
            }
        } else
        {
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreativeDamage(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player)
        {
            event.setCancelled(true);
            ((Player) event.getDamager()).sendMessage(ChatColor.RED + "You may not PVP others while in creative!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSkyblockDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        if(player.getLastDamageCause().equals(EntityDamageEvent.DamageCause.VOID))
        {
            if(player.getLocation().getWorld().getName().equalsIgnoreCase("skyblock"))
            {
                event.setDeathMessage(ChatColor.GOLD + "[Skyblock]" + ChatColor.RED + player.getDisplayName() + " couldn't handle the skyblock.");
            }
            else
            {
                event.setDeathMessage(ChatColor.GOLD + "[Skyblock]" + ChatColor.RED + player.getDisplayName() + " died of unknown causes...");
            }
        }
    }
}
