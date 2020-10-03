package me.hsgamer.hscore.sql.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hsgamer.hscore.sql.Setting;
import me.hsgamer.hscore.sql.Sql;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariSqlite implements Sql<HikariDataSource> {
  private final HikariDataSource hikariDataSource;

  public HikariSqlite(Setting setting) {
    this(".", setting);
  }

  public HikariSqlite(String folder, Setting setting) {
    final HikariConfig config = new HikariConfig();
    config.setPoolName("SQLITE-" + setting.getDatabaseName());
    config.setDriverClassName(setting.getDriver().isEmpty() ? "org.sqlite.JDBC" : setting.getDriver());
    config.setJdbcUrl("jdbc:sqlite:" + Paths.get(folder, setting.getDatabaseName() + ".db").toString());
    config.setMaxLifetime(60000L);
    config.setIdleTimeout(45000L);
    config.setMaximumPoolSize(50);
    this.hikariDataSource = new HikariDataSource(config);
  }

  @Override
  public HikariDataSource getOriginal() {
    return hikariDataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hikariDataSource.getConnection();
  }
}
