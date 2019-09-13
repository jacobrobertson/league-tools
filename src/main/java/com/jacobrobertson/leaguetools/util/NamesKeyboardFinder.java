package com.jacobrobertson.leaguetools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NamesKeyboardFinder {

  public static void main(String[] args) {
    NamesKeyboardFinder f = new NamesKeyboardFinder();
    
//    f.findNames("Annie");
    
    f.findNames();
  }
  
  private static String leftHand = "qwertasdfgzxcvb";
  private static String rightHand = "yuiophjkklnm";
  private static final int MAX_KEY_COUNT = 10;
  private NameInfo[] nameInfos;
  
  
  private class KeyUniqueness {
    String typedName;
    List<String> otherChampionNames;
    public boolean isUnique() {
      return typedName != null && otherChampionNames.isEmpty();
    }
  }
  
  private class NameInfo {
    private String name;
    private String cleanName;
    private Map<Integer, List<String>> keyedNamesLeft = new HashMap<Integer, List<String>>(); 
    private Map<Integer, List<String>> keyedNamesRight = new HashMap<Integer, List<String>>(); 
    
    public NameInfo(String name) {
      this.name = name;
      this.cleanName = name.toLowerCase().replaceAll("[^a-z]", "");
      for (int i = 01; i < MAX_KEY_COUNT; i++) {
        keyedNamesLeft.put(i, getPossibleNames(leftHand, i));
        keyedNamesRight.put(i, getPossibleNames(rightHand, i));
      }
    }
    
    public String getName() {
      return name;
    }
    
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

    public List<String> getKeyedNames(int count, boolean left) {
      if (left) {
        return keyedNamesLeft.get(count);
      } else {
        return keyedNamesRight.get(count);
      }
    }
    private List<String> getPossibleNames(String keys, int keyCount) {
      Set<String> namesSet = new HashSet<String>();
      getPossibleNames(cleanName, keys, 0, keyCount, "", namesSet);
      List<String> names = new ArrayList<String>(namesSet);
      Collections.sort(names);
      return names;
    }
    private void getPossibleNames(String name, String keys, int pos, int keyCount, String current, Set<String> names) {
      if (current.length() == keyCount) {
        names.add(current);
      } else {
        for (int i = pos; i < name.length(); i++) {
          char c = name.charAt(i);
          if (keys.indexOf(c) >= 0) {
            String next = current + c;
            getPossibleNames(name, keys, i + 1, keyCount, next, names);
          }
        }
      }
    }
    
  }
  
  public NamesKeyboardFinder() {
    String[] names = ChampionNames.NAMES;
    nameInfos = new NameInfo[names.length];
    int index = 0;
    for (String name : names) {
      nameInfos[index++] = new NameInfo(name);
    }
  }
  
  /*
   * Goal
   * - find names that can be uniquely identified by the fewest keys on each hand
   */
  
  public void findNames() {
    findNames(null);
  }
  public void findNames(String testName) {
    System.out.println("Champion | Left Hand | Right Hand");
    System.out.println("---|---|---");
    for (NameInfo nameInfo : nameInfos) {
      if (testName != null && !nameInfo.name.equals(testName)) {
        continue;
      }
      KeyUniqueness left = new KeyUniqueness();
      KeyUniqueness right = new KeyUniqueness();
      for (int i = 1; i <= MAX_KEY_COUNT; i++) {
        findUniqueNess(nameInfo, true, i, left);
        findUniqueNess(nameInfo, false, i, right);
      }
      outputMessage(nameInfo, left, right);
    }
  }
  
  private void outputMessage(NameInfo nameInfo, KeyUniqueness left, KeyUniqueness right) {
    StringBuilder buf = new StringBuilder();
    buf.append(nameInfo.getName());
    buf.append(" | ");
    appendMessage(buf, left);
    buf.append(" | ");
    appendMessage(buf, right);
    System.out.println(buf.toString());
  }
  private void appendMessage(StringBuilder buf, KeyUniqueness keys) {
    if (keys.isUnique()) {
//      buf.append(hand);
//      buf.append(" ");
      buf.append(keys.typedName.length());
      buf.append(" - ");
      buf.append(keys.typedName);
//      buf.append(")");
    } else {
      buf.append(" - ");
    }
    
  }
  
  public void findUniqueNess(NameInfo nameInfo, boolean left, int keyCount, KeyUniqueness current) {
    
    // look over all names and see if anyone else could be found with those same keys
    
    List<String> testNames = nameInfo.getKeyedNames(keyCount, left);
    if (testNames == null || testNames.isEmpty()) {
      return;
    }
    
    for (String testName : testNames) {
    
      List<String> currentFound = new ArrayList<String>();
      
      for (NameInfo info : nameInfos) {
        if (info == nameInfo) {
          continue;
        }
        boolean canFind = info.canFind(testName);
        if (canFind) {
          currentFound.add(info.getName());
        }
      }
      
      if (current.otherChampionNames == null || currentFound.size() < current.otherChampionNames.size()) {
        current.otherChampionNames = currentFound;
        current.typedName = testName;
      }
      
    }
  }
  
}
