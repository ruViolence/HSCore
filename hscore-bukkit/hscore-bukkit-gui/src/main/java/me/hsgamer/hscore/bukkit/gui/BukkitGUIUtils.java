package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.event.BukkitDragEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * The utility class for {@link BukkitGUIHolder}
 */
public final class BukkitGUIUtils {
  private BukkitGUIUtils() {
    // EMPTY
  }

  /**
   * Set that the holder should not cancel the click event on bottom inventory
   *
   * @param holder the gui holder
   */
  public static void allowMoveItemOnBottom(BukkitGUIHolder holder) {
    holder.addEventConsumer(BukkitClickEvent.class, event -> {
      InventoryClickEvent clickEvent = event.getEvent();
      if (clickEvent.getClickedInventory() == clickEvent.getInventory()) {
        return;
      }
      switch (clickEvent.getAction()) {
        case DROP_ALL_SLOT:
        case DROP_ONE_SLOT:
        case PICKUP_ALL:
        case PICKUP_HALF:
        case PICKUP_ONE:
        case PICKUP_SOME:
        case HOTBAR_MOVE_AND_READD:
        case PLACE_ALL:
        case PLACE_ONE:
        case PLACE_SOME:
        case HOTBAR_SWAP:
        case SWAP_WITH_CURSOR:
          clickEvent.setCancelled(false);
          break;
        default:
          break;
      }
    });
  }

  /**
   * Set that the holder cancels drag event on top inventory
   *
   * @param holder the gui holder
   */
  public static void cancelDragEvent(BukkitGUIHolder holder) {
    holder.addEventConsumer(BukkitDragEvent.class, event -> {
      InventoryDragEvent dragEvent = event.getEvent();
      for (int slot : dragEvent.getRawSlots()) {
        if (slot < dragEvent.getInventory().getSize()) {
          event.setCancelled(true);
          break;
        }
      }
    });
  }

  /**
   * Normalize the size to a valid chest size
   *
   * @param size the size
   *
   * @return the normalized size
   */
  public static int normalizeToChestSize(int size) {
    int remain = size % 9;
    size -= remain;
    size += remain > 0 ? 9 : 0;
    return size;
  }
}
