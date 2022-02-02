package me.hsgamer.hscore.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The setting for connection
 */
public class Setting {
  private final Map<String, Object> clientProperties;
  private final Map<String, Object> driverProperties;
  private String host;
  private String databaseName;
  private String port;
  private String username;
  private String password;

  /**
   * The constructor
   */
  private Setting() {
    clientProperties = new HashMap<>();
    driverProperties = new HashMap<>();
    host = "localhost";
    databaseName = "";
    port = "3306";
    username = "root";
    password = "";
  }

  /**
   * Create a new setting
   *
   * @return the setting
   */
  public static Setting create() {
    return new Setting();
  }

  /**
   * Get the host
   *
   * @return the host
   */
  public String getHost() {
    return this.host;
  }

  /**
   * Set the host
   *
   * @param host the host
   *
   * @return {@code this} for builder chain
   */
  public Setting setHost(String host) {
    this.host = host;
    return this;
  }

  /**
   * Get the database name
   *
   * @return the database name
   */
  public String getDatabaseName() {
    return this.databaseName;
  }

  /**
   * Set the database name
   *
   * @param databaseName the database name
   *
   * @return {@code this} for builder chain
   */
  public Setting setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
    return this;
  }

  /**
   * Get the port
   *
   * @return the port
   */
  public String getPort() {
    return this.port;
  }

  /**
   * Set the port
   *
   * @param port the port
   *
   * @return {@code this} for builder chain
   */
  public Setting setPort(String port) {
    this.port = port;
    return this;
  }

  /**
   * Get the username
   *
   * @return the username
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Set the username
   *
   * @param username the username
   *
   * @return {@code this} for builder chain
   */
  public Setting setUsername(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get the password
   *
   * @return the password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Set the password
   *
   * @param password the password
   *
   * @return {@code this} for builder chain
   */
  public Setting setPassword(String password) {
    this.password = password;
    return this;
  }

  /**
   * Set the client property
   *
   * @param property the property
   * @param value    the value
   */
  public Setting setClientProperty(String property, Object value) {
    this.clientProperties.put(property, value);
    return this;
  }

  /**
   * Get all client properties
   *
   * @return the properties
   */
  public Map<String, Object> getClientProperties() {
    return Collections.unmodifiableMap(clientProperties);
  }

  /**
   * Set the driver property
   *
   * @param property the property
   * @param value    the value
   */
  public Setting setDriverProperty(String property, Object value) {
    this.driverProperties.put(property, value);
    return this;
  }

  /**
   * Get all driver properties
   *
   * @return the properties
   */
  public Map<String, Object> getDriverProperties() {
    return Collections.unmodifiableMap(driverProperties);
  }
}
