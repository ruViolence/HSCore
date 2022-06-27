package me.hsgamer.hscore.addon.loader;

import me.hsgamer.hscore.addon.exception.InvalidAddonDescription;
import me.hsgamer.hscore.addon.object.AddonDescription;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarFile;

/**
 * The simplified {@link AddonDescriptionLoader} that loads the {@link AddonDescription} from the information map of the jar file
 */
public interface MapAddonDescriptionLoader extends AddonDescriptionLoader {
  /**
   * Load the information map from the jar file
   *
   * @param jarFile the jar file
   *
   * @return the information map
   *
   * @throws IOException             if an I/O error occurs
   * @throws InvalidAddonDescription if the information map is invalid
   */
  Map<String, Object> loadAsMap(JarFile jarFile) throws IOException, InvalidAddonDescription;

  @Override
  default AddonDescription load(JarFile jarFile) throws IOException, InvalidAddonDescription {
    Map<String, Object> data = loadAsMap(jarFile);
    // Load required descriptions
    final String name = Objects.toString(data.get("name"), null);
    final String version = Objects.toString(data.get("version"), null);
    final String mainClass = Objects.toString(data.get("main"), null);
    if (name == null) {
      throw new InvalidAddonDescription("Addon '" + jarFile.getName() + "' doesn't define a name");
    }
    if (version == null) {
      throw new InvalidAddonDescription("Addon '" + jarFile.getName() + "' doesn't define a version");
    }
    if (mainClass == null) {
      throw new IllegalArgumentException("Addon '" + jarFile.getName() + "' doesn't define a main class");
    }
    return new AddonDescription(name, version, mainClass, data);
  }
}
