package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minestom.gui.button.Button;
import me.hsgamer.hscore.ui.BaseDisplay;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * The base {@link me.hsgamer.hscore.ui.Display} for UI in Minestom
 */
public class GUIDisplay extends BaseDisplay<GUIHolder> {
  private final Map<Integer, Button> viewedButtons = new ConcurrentHashMap<>();
  private Inventory inventory;
  private EventNode<InventoryEvent> eventNode;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  public GUIDisplay(UUID uuid, GUIHolder holder) {
    super(uuid, holder);
  }

  /**
   * Handle the click event.
   * This can be used to cancel the event.
   *
   * @param uuid  the unique id
   * @param event the click event
   */
  public void handleClick(UUID uuid, InventoryPreClickEvent event) {
    Optional.ofNullable(viewedButtons.get(event.getSlot())).ifPresent(button -> {
      if (!button.handleAction(uuid, event)) {
        event.setCancelled(true);
      }
    });
  }

  /**
   * Handle the click event
   *
   * @param uuid  the unique id
   * @param event the click event
   */
  public void handleClick(UUID uuid, InventoryClickEvent event) {
    Optional.ofNullable(viewedButtons.get(event.getSlot())).ifPresent(button -> button.handleAction(uuid, event));
  }

  /**
   * Get the inventory of the display
   *
   * @return the inventory
   */
  public Inventory getInventory() {
    return inventory;
  }

  /**
   * Get the event node of the display
   *
   * @return the event node
   */
  public EventNode<InventoryEvent> getEventNode() {
    return eventNode;
  }

  @Override
  public void init() {
    this.inventory = new Inventory(holder.getInventoryType(), holder.getTitleFunction().apply(uuid));
    this.eventNode = EventNode.event("inventory-" + UUID.randomUUID(), EventFilter.INVENTORY, event -> Objects.equals(event.getInventory(), inventory));
    eventNode.addListener(InventoryOpenEvent.class, this::handleEvent);
    eventNode.addListener(InventoryPreClickEvent.class, this::handleEvent);
    eventNode.addListener(InventoryClickEvent.class, this::handleEvent);
    eventNode.addListener(InventoryCloseEvent.class, this::handleEvent);
    MinecraftServer.getGlobalEventHandler().addChild(eventNode);

    update();

    Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
    if (player != null) {
      player.openInventory(inventory);
    }
  }

  @Override
  public void stop() {
    if (inventory != null) {
      inventory.clear();
    }
    if (eventNode != null) {
      MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
    }
    viewedButtons.clear();
  }

  @Override
  public void update() {
    if (inventory == null) {
      return;
    }

    int size = inventory.getSize();
    List<Integer> emptyItemSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Integer> emptyActionSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    Map<Button, List<Integer>> buttonSlots = holder.getButtonMap().getButtons(uuid);
    buttonSlots.forEach((button, slots) -> {
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack == null && !button.forceSetAction(uuid)) {
        return;
      }
      slots.forEach(slot -> {
        if (slot >= size) {
          return;
        }
        if (itemStack != null) {
          inventory.setItemStack(slot, itemStack);
          emptyItemSlots.remove(slot);
        }
        viewedButtons.put(slot, button);
        emptyActionSlots.remove(slot);
      });
    });

    Button defaultButton = holder.getButtonMap().getDefaultButton(uuid);
    ItemStack itemStack = defaultButton.getItemStack(uuid);
    emptyItemSlots.forEach(slot -> inventory.setItemStack(slot, itemStack));
    emptyActionSlots.forEach(slot -> viewedButtons.put(slot, defaultButton));
  }
}
