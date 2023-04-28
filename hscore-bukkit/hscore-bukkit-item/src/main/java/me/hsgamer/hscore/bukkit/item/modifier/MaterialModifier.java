package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemComparator;
import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The material modifier
 */
@SuppressWarnings("deprecation")
public class MaterialModifier implements ItemModifier, ItemComparator {
  private static final Map<Integer, Material> ID_MATERIAL_MAP = new HashMap<>();

  static {
    for (Material material : Material.values()) {
      try {
        ID_MATERIAL_MAP.put(material.getId(), material);
      } catch (Exception ignored) {
        // IGNORED
      }
    }
  }

  private List<String> materialList = Collections.emptyList();

  private static Material getMaterial(String materialString) {
    materialString = materialString.replace(" ", "_");
    Material material;
    try {
      if (VersionUtils.isAtLeast(13)) {
        material = Material.matchMaterial(materialString, false);
        if (material == null) {
          material = Material.matchMaterial(materialString, true);
        }
      } else {
        material = Material.matchMaterial(materialString);
      }
    } catch (Exception ignored) {
      material = null;
    }

    if (material == null) {
      try {
        material = ID_MATERIAL_MAP.get(Integer.parseInt(materialString));
      } catch (NumberFormatException ignored) {
        // IGNORED
      }
    }

    return material;
  }

  private static boolean setMaterial(ItemStack itemStack, String materialString) {
    String[] split = materialString.split(":", 2);
    Material material = getMaterial(split[0].trim());
    if (material != null) {
      itemStack.setType(material);
      if (split.length > 1) {
        itemStack.setDurability(Short.parseShort(split[1].trim()));
      }
      return true;
    }
    return false;
  }

  private static boolean compareMaterial(ItemStack itemStack, String materialString) {
    String[] split = materialString.split(":", 2);
    Material material = getMaterial(split[0].trim());
    if (itemStack.getType() == material) {
      if (split.length > 1) {
        return itemStack.getDurability() == Short.parseShort(split[1].trim());
      }
      return true;
    }
    return false;
  }

  @Override
  public @NotNull ItemStack modify(@NotNull ItemStack original, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    for (String materialString : materialList) {
      if (setMaterial(original, StringReplacer.replace(materialString, uuid, stringReplacers))) {
        break;
      }
    }
    return original;
  }

  @Override
  public Object toObject() {
    return this.materialList;
  }

  @Override
  public void loadFromObject(Object object) {
    this.materialList = CollectionUtils.createStringListFromObject(object, true);
  }

  @Override
  public boolean loadFromItemStack(ItemStack itemStack) {
    this.materialList = Collections.singletonList(itemStack.getType().name());
    return true;
  }

  @Override
  public boolean compare(@NotNull ItemStack itemStack, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    if (materialList.isEmpty()) {
      return true;
    }
    return materialList.parallelStream()
      .map(s -> StringReplacer.replace(s, uuid, stringReplacers))
      .anyMatch(s -> compareMaterial(itemStack, s));
  }

  /**
   * Set the material
   *
   * @param material the material
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public MaterialModifier setMaterial(Material material) {
    this.materialList = Collections.singletonList(material.name());
    return this;
  }

  /**
   * Set the material
   *
   * @param material the material
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public MaterialModifier setMaterial(String... material) {
    this.materialList = Arrays.asList(material);
    return this;
  }
}
