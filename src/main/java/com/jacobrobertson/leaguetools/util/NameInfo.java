package com.jacobrobertson.leaguetools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.jacobrobertson.leaguetools.util.NamesKeyboardFinder.Hands;

public class NameInfo {
  private String name;
  private String cleanName;
  private Map<Hands, Map<Integer, List<String>>> keyedNames =
      new HashMap<Hands, Map<Integer, List<String>>>();

  public NameInfo(String name) {
    this.name = name;
    this.cleanName = name.toLowerCase(); // .replaceAll("[^a-z]", "");
    keyedNames.put(Hands.Both, new HashMap<Integer, List<String>>());
    keyedNames.put(Hands.Left, new HashMap<Integer, List<String>>());
    keyedNames.put(Hands.Right, new HashMap<Integer, List<String>>());
    for (int i = 1; i < NamesKeyboardFinder.MAX_KEY_COUNT; i++) {
      put(Hands.Left, i);
      put(Hands.Right, i);
      put(Hands.Both, i);
    }
  }

  private void put(Hands hands, int count) {
    keyedNames.get(hands).put(count, getPossibleNames(hands, count));
  }

  public String getName() {
    return name;
  }

  public String getCleanName() {
    return cleanName;
  }
  // we call this to see if we can find the other champ's shortcut name in this champ's name
  public boolean canFind(String keys) {
    int pos = -1;
    boolean canFind = true;
    for (int i = 0; i < keys.length(); i++) {
      int found = cleanName.indexOf(keys.charAt(i), pos + 1);
      if (found >= 0) {
        pos = found;
      } else {
        canFind = false;
        break;
      }
    }
    return canFind;
  }

  public List<String> getKeyedNames(int count, Hands hand) {
    return keyedNames.get(hand).get(count);
  }

  private List<String> getPossibleNames(Hands hands, int keyCount) {
    Set<String> namesSet = new HashSet<String>();
    getPossibleNames(cleanName, hands, 0, keyCount, "", namesSet);
    List<String> names = new ArrayList<String>(namesSet);
    Collections.sort(names, new NameComparator(this));
    return names;
  }

  private void getPossibleNames(String name, Hands hand, int pos, int keyCount, String current,
      Set<String> names) {
    if (current.length() == keyCount) {
      names.add(current);
    } else {
      for (int i = pos; i < name.length(); i++) {
        char c = name.charAt(i);
        if (hand.getKeys().indexOf(c) >= 0) {
          String next = current + c;
          getPossibleNames(name, hand, i + 1, keyCount, next, names);
        }
      }
    }
  }

}
