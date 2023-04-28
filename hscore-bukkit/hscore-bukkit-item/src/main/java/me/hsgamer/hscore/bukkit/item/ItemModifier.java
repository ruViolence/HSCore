package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * The item modifier
 */
public interface ItemModifier {
  /**
   * Modify the item
   *
   * @param original        the original item
   * @param uuid            the unique id
   * @param stringReplacers the string replacers
   *
   * @return the modified item
   */
  @NotNull
  ItemStack modify(@NotNull ItemStack original, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers);

  /**
   * Serialize the modifier to an object
   *
   * @return the object
   */
  Object toObject();

  /**
   * Load the modifier from an object
   *
   * @param object the object
   */
  void loadFromObject(Object object);

  /**
   * Load the modifier from an item
   *
   * @param itemStack the item
   *
   * @return true if it can
   */
  default boolean loadFromItemStack(ItemStack itemStack) {
    return false;
  }

  /**
   * Modify the item
   *
   * @param original the original item
   * @param uuid     the unique id
   *
   * @return the modified item
   */
  @NotNull
  default ItemStack modify(@NotNull ItemStack original, @Nullable UUID uuid) {
    return modify(original, uuid, Collections.emptyList());
  }

  /**
   * Modify the item
   *
   * @param original the original item
   *
   * @return the modified item
   */
  @NotNull
  default ItemStack modify(@NotNull ItemStack original) {
    return modify(original, null);
  }
}
