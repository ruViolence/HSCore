package me.hsgamer.hscore.expansion.common.object;

public interface Expansion {
  /**
   * Called when loading the addon
   *
   * @return whether the addon loaded properly
   */
  default boolean onLoad() {
    return true;
  }

  /**
   * Called when enabling the addon
   */
  default void onEnable() {
    // EMPTY
  }

  /**
   * Called when disabling the addon
   */
  default void onDisable() {
    // EMPTY
  }
}
