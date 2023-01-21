package me.hsgamer.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class DummyButton implements Button {
  private final Function<UUID, Item> itemFunction;

  public DummyButton(Function<@NotNull UUID, @Nullable Item> itemFunction) {
    this.itemFunction = itemFunction;
  }

  public DummyButton(@Nullable Item itemStack) {
    this(uuid -> itemStack);
  }

  @Override
  public @Nullable Item getItem(@NotNull UUID uuid) {
    return itemFunction.apply(uuid);
  }
}
