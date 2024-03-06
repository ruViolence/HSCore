package me.hsgamer.hscore.config.annotated;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.DecorativeConfig;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.annotation.Comment;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.logger.common.LogLevel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * The annotated {@link Config}, where any fields can be assigned to the config with the annotation {@link ConfigPath}.
 * If the field is a final non-static, don't set default value directly, use constructor instead.
 * <pre>
 * public class ExampleConfig extends AnnotatedConfig {
 *   &#64;ConfigPath("test1.path")
 *   private final String test1 = "test1"; // If the field is final, Don't do this
 *   &#64;ConfigPath("test2.path")
 *   private final String test2; // Do this instead
 *
 *   public ExampleConfig(Config config) {
 *     super(config);
 *     test2 = "test2"; // Set default value via the constructor
 *   }
 * }
 * </pre>
 */
public class AnnotatedConfig extends DecorativeConfig {
  private final Map<PathString, Field> pathFieldMap = new HashMap<>();

  /**
   * Create an annotated config
   *
   * @param config the original config
   */
  public AnnotatedConfig(Config config) {
    super(config);
  }

  @Override
  public void setup() {
    super.setup();
    List<Field> validFields = new ArrayList<>();
    Arrays.stream(this.getClass().getDeclaredFields())
      .filter(this::checkPathField)
      .sorted(this::compareField)
      .forEach(field -> {
        ConfigPath configPath = field.getAnnotation(ConfigPath.class);
        pathFieldMap.put(new PathString(configPath.value()), field);
        validFields.add(field);
      });
    validFields.forEach(this::setupField);
    setupClassComment();
    this.save();
  }

  @Override
  public void set(PathString path, Object value) {
    Field field = pathFieldMap.get(path);
    if (field == null) {
      super.set(path, value);
      return;
    }
    checkAndSetField(field, value);
  }

  @Override
  public void reload() {
    super.reload();
    for (Field field : pathFieldMap.values()) {
      setupField(field);
    }
    setupClassComment();
    this.save();
  }

  private void setupClassComment() {
    if (this.getClass().isAnnotationPresent(Comment.class) && this.getComment(PathString.ROOT).isEmpty()) {
      this.setComment(PathString.ROOT, Arrays.asList(this.getClass().getAnnotation(Comment.class).value()));
    }
  }

  private boolean checkPathField(Field field) {
    ConfigPath configPath = field.getAnnotation(ConfigPath.class);
    if (configPath == null) {
      return false;
    }
    if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
      LOGGER.log(LogLevel.WARN, field.getName() + " is a static final field. Ignored");
      return false;
    }
    return true;
  }

  /**
   * Compare the fields
   *
   * @param field1 the first field
   * @param field2 the second field
   *
   * @return the comparison result
   */
  private int compareField(Field field1, Field field2) {
    ConfigPath configPath1 = field1.getAnnotation(ConfigPath.class);
    ConfigPath configPath2 = field2.getAnnotation(ConfigPath.class);
    return Integer.compare(configPath1.priority(), configPath2.priority());
  }

  private void setupField(Field field) {
    ConfigPath configPath = field.getAnnotation(ConfigPath.class);
    PathString path = new PathString(configPath.value());
    Converter converter = DefaultConverterManager.getConverterIfDefault(field.getGenericType(), configPath.converter());
    Object defaultValue = this.getValue(field);

    if (!contains(path)) {
      super.set(path, converter.convertToRaw(defaultValue));
      if (field.isAnnotationPresent(Comment.class)) {
        super.setComment(path, Arrays.asList(field.getAnnotation(Comment.class).value()));
      }
    } else {
      this.setValue(field, converter.convert(this.getNormalized(path)));
    }
  }

  private void checkAndSetField(Field field, Object value) {
    ConfigPath configPath = field.getAnnotation(ConfigPath.class);
    PathString path = new PathString(configPath.value());
    Converter converter = DefaultConverterManager.getConverterIfDefault(field.getGenericType(), configPath.converter());
    super.set(path, converter.convertToRaw(value));
    this.setValue(field, value);
  }

  @SuppressWarnings("deprecation")
  private void setValue(Field field, Object value) {
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    try {
      field.set(this, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Cannot set the value for " + field.getName(), e);
    } finally {
      field.setAccessible(accessible);
    }
  }

  @SuppressWarnings("deprecation")
  private Object getValue(Field field) {
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    Object value;
    try {
      value = field.get(this);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Cannot get the value for " + field.getName(), e);
    } finally {
      field.setAccessible(accessible);
    }
    return value;
  }
}
