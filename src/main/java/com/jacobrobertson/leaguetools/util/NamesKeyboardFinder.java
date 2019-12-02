package com.jacobrobertson.leaguetools.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NamesKeyboardFinder {

  public static void main(String[] args) {
    
    String testName = "None";
    if (testName.equals("None")) {
      new NamesKeyboardFinder().findNames();
    } else { 
      new NameInfo(testName);
      new NamesKeyboardFinder().findNames(testName);
    }
  }
  
  static final int MAX_KEY_COUNT = 10;
  private static final int MAX_OTHER_CHAMPS_COUNT = 5;
  private NameInfo[] nameInfos;
  
  enum Hands {

	  Left("qwertasdfgzxcvb "), Right("yuiophjkkl;'nm,. "), Both(Left.keys + Right.keys);
	  
	  private String keys;
	  
	  private Hands(String keys) {
		  this.keys = keys;
	  }
	  public String getKeys() {
		  return keys;
	  }
	  
	  public static Hands forKey(char key) {
		  for (Hands hands : Hands.values()) {
			if (hands.keys.indexOf(key) >= 0) {
				return hands;
			}
		  }
		  return null;
	  }
	  
  }
  
  private class KeyUniqueness {
    String typedName;
    List<String> otherChampionNames;
    public boolean isUnique() {
      return typedName != null && otherChampionNames.isEmpty();
    }
    public boolean isBetterThan(KeyUniqueness that) {
    	if (this.isUnique() && !that.isUnique()) {
    		return true;
    	} else if (!this.isUnique() && that.isUnique()) {
    		return false;
    	} else {
    		return this.typedName.length() < that.typedName.length();
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
    System.out.println("Champion | Left Hand | Right Hand | Both Hands");
    System.out.println("---|---|---|---");
    for (NameInfo nameInfo : nameInfos) {
      if (testName != null && !nameInfo.getName().equals(testName)) {
        continue;
      }
      KeyUniqueness left = new KeyUniqueness();
      KeyUniqueness right = new KeyUniqueness();
      KeyUniqueness both = new KeyUniqueness();
      for (int i = 1; i <= MAX_KEY_COUNT; i++) {
        findUniqueNess(nameInfo, Hands.Left, i, left);
        findUniqueNess(nameInfo, Hands.Right, i, right);
        findUniqueNess(nameInfo, Hands.Both, i, both);
      }
      outputMessage(nameInfo, left, right, both);
    }
  }
  
  private void outputMessage(NameInfo nameInfo, KeyUniqueness left, KeyUniqueness right, KeyUniqueness both) {
    StringBuilder buf = new StringBuilder();
    buf.append(nameInfo.getName());
    buf.append(" | ");
    appendMessage(buf, left);
    buf.append(" | ");
    appendMessage(buf, right);
    buf.append(" | ");
    
    if (both.isBetterThan(left) && both.isBetterThan(right)) {
        appendMessage(buf, both);   	
    } else {
    	buf.append(" - ");
    }
    
    System.out.println(buf.toString());
  }
  
  private void appendMessage(StringBuilder buf, KeyUniqueness keys) {
    String typedName = Optional.ofNullable(keys.typedName).map(s -> s.replace(' ', '_')).orElse(null);
    if (keys.isUnique()) {
//      buf.append(hand);
//      buf.append(" ");
//      buf.append(keys.typedName.length());
//      buf.append(" - ");
      buf.append(typedName);
//      buf.append(")");
    } else if (typedName == null || keys.otherChampionNames.size() > MAX_OTHER_CHAMPS_COUNT) {
      buf.append(" - "); // means there are no letters on that hand at all
    } else {
      buf.append(typedName);
      if (!keys.otherChampionNames.isEmpty()) {
        buf.append(" +");
        buf.append(keys.otherChampionNames.size());
      }
    }
    
  }
  
  public void findUniqueNess(NameInfo nameInfo, Hands hand, int keyCount, KeyUniqueness current) {
    
    // look over all names and see if anyone else could be found with those same keys
    
    List<String> testNames = nameInfo.getKeyedNames(keyCount, hand);
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
