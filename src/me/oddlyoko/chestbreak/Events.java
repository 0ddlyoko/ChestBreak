/**
 * 
 */
package me.oddlyoko.chestbreak;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This class allow you to <br />
 * <br />
 * Created: 17 févr. 2018 (16:22:23);<br />
 * Last edit: 17 févr. 2018 (16:22:23);<br />
 * 
 * @author 0ddlyoko<br />
 *         <br />
 */
public class Events implements Listener {
	private ChestBreak plugin;

	public Events(ChestBreak plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void playerInventoryCloseEvent(InventoryCloseEvent e) {
		if (e.getInventory() == null)
			return;
		if (!plugin.isEnable())
			return;
		if (e.getInventory().getType().equals(InventoryType.CHEST)) {
			if (e.getInventory().getLocation() != null) {
				Location loc = e.getInventory().getLocation();
				Block b = loc.getBlock();
				if (b.getType().equals(Material.CHEST)) {
					if (plugin.hasChest(loc)) {
						Chest c = (Chest) b.getState();
						ItemStack[] is = c.getInventory().getContents();
						b.setType(Material.AIR);
						for (ItemStack i : is)
							if (i != null && !i.getType().equals(Material.AIR))
								loc.getWorld().dropItem(loc, i);
						plugin.removeChest(loc);
						Bukkit.broadcastMessage(Variables.PLUGINNAMECOLOR + ChatColor.YELLOW
								+ "Un coffre vient d'être cassé, plus que " + ChatColor.GOLD + ChatColor.BOLD
								+ plugin.getChestsConfig().getStringList("chests").size() + ChatColor.RESET
								+ ChatColor.YELLOW + " !");
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void playerRightClickEvent(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (b != null && b.getType().equals(Material.CHEST)) {
			Location loc = b.getLocation();
			ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
			if (is != null && is.getType().equals(Material.STICK)) {
				if (e.getPlayer().hasPermission("chestbreak.use")) {
					e.setCancelled(true);
					if (plugin.hasChest(loc)) {
						plugin.removeChest(loc);
						e.getPlayer()
								.sendMessage(Variables.PLUGINNAMECOLOR + ChatColor.YELLOW + "Chest retiré ("
										+ ChatColor.GOLD + ChatColor.BOLD
										+ plugin.getChestsConfig().getStringList("chests").size() + ChatColor.RESET
										+ ChatColor.YELLOW + ") !");
					} else {
						plugin.addChest(b.getLocation());
						e.getPlayer()
								.sendMessage(Variables.PLUGINNAMECOLOR + ChatColor.YELLOW + "Chest ajouté ("
										+ ChatColor.GOLD + ChatColor.BOLD
										+ plugin.getChestsConfig().getStringList("chests").size() + ChatColor.RESET
										+ ChatColor.YELLOW + ") !");
					}
				}
			}
		}
	}
}
