package me.hsgamer.hscore.utils;

import java.util.HashMap;
import java.util.Map;

public class CaseInsensitiveStringMap<V> extends HashMap<String, V> {

  public CaseInsensitiveStringMap() {
    super();
  }

  public CaseInsensitiveStringMap(Map<? extends String, ? extends V> map) {
    super();
    putAll(map);
  }

  @Override
  public V put(String key, V value) {
    return super.put(key.toLowerCase(), value);
  }

  @Override
  public V get(Object key) {
    return super.get(key.getClass() == String.class ? key.toString().toLowerCase() : key);
  }

  @Override
  public boolean containsKey(Object key) {
    return super.containsKey(key.getClass() == String.class ? key.toString().toLowerCase() : key);
  }

  @Override
  public V remove(Object key) {
    return super.remove(key.getClass() == String.class ? key.toString().toLowerCase() : key);
  }

  @Override
  public void putAll(Map<? extends String, ? extends V> m) {
    Map<String, V> mp = new HashMap<>();
    m.forEach((s, o) -> mp.put(s.toLowerCase(), o));
    super.putAll(mp);
  }
}
