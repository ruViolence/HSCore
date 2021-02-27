package me.hsgamer.hscore.bukkit.gui.mask.multi;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Per-user Set Buttons interface
 */
public interface PerUserSetButtons {
  /**
   * Set the buttons for the unique id
   *
   * @param uuid    the unique id
   * @param buttons the buttons
   */
  void setButtons(UUID uuid, List<Button> buttons);

  /**
   * Set the buttons for the unique id
   *
   * @param uuid    the unique id
   * @param buttons the buttons
   */
  default void setButtons(UUID uuid, Button... buttons) {
    setButtons(uuid, Arrays.asList(buttons));
  }
}
