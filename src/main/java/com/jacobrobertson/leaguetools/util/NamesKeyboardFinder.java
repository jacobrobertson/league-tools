package com.jacobrobertson.leaguetools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NamesKeyboardFinder {

  public static void main(String[] args) {
    NamesKeyboardFinder f = new NamesKeyboardFinder();
    
    f.findNames("Annie");
    
    f.findNames();
  }
  
  private static final int MAX_KEY_COUNT = 10;
  private static final int MAX_OTHER_CHAMPS_COUNT = 5;
  private NameInfo[] nameInfos;
  
  enum Hands {

	  Left("qwertasdfgzxcvb"), Right("yuiophjkklnm"), Both(Left.keys + Right.keys);
	  
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
  
  private class NameInfo {
    private String name;
    private String cleanName;
    private Map<Hands, Map<Integer, List<String>>> keyedNames = 
    		new HashMap<Hands, Map<Integer, List<String>>>();
    
    public NameInfo(String name) {
      this.name = name;
      this.cleanName = name.toLowerCase().replaceAll("[^a-z]", "");
      keyedNames.put(Hands.Both, new HashMap<Integer, List<String>>());
      keyedNames.put(Hands.Left, new HashMap<Integer, List<String>>());
      keyedNames.put(Hands.Right, new HashMap<Integer, List<String>>());
      for (int i = 1; i < MAX_KEY_COUNT; i++) {
          put(Hands.Left, i);
          put(Hands.Right, i);
          put(Hands.Both, i);
      }
    }
    private void put(Hands hands, int count) {
    	keyedNames.get(hands).put(count,  getPossibleNames(hands, count));
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

    public List<String> getKeyedNames(int count, Hands hand) {
    	return keyedNames.get(hand).get(count);
    }
    private List<String> getPossibleNames(Hands hands, int keyCount) {
      Set<String> namesSet = new HashSet<String>();
      getPossibleNames(cleanName, hands, 0, keyCount, "", namesSet);
      List<String> names = new ArrayList<String>(namesSet);
      Collections.sort(names, new NameComparator());
      return names;
    }
    
		class NameComparator implements Comparator<String> {
			public int compare(String o1, String o2) {
				// if one is the name in order (they will be same length)
				if (cleanName.startsWith(o1)) {
					return -1;
				} else if (cleanName.startsWith(o2)) {
					return 1;
				}
				// check number of hand switches
				Integer s1 = countSwitches(o1);
				Integer s2 = countSwitches(o1);
				int comp = s1.compareTo(s2);
				if (comp != 0) {
					return comp;
				} else {
					return o1.compareTo(o2);
				}
			}
		}
		private int countSwitches(String name) {
			Hands current = null;
			int switches = 0;
			for (int i = 0; i < name.length(); i++) {
				Hands found = Hands.forKey(name.charAt(i));
				if (current != found) {
					switches++;
				}
			}
			return switches;
		}
    
    private void getPossibleNames(String name, Hands hand, int pos, int keyCount, String current, Set<String> names) {
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
      if (testName != null && !nameInfo.name.equals(testName)) {
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
    if (keys.isUnique()) {
//      buf.append(hand);
//      buf.append(" ");
//      buf.append(keys.typedName.length());
//      buf.append(" - ");
      buf.append(keys.typedName);
//      buf.append(")");
    } else if (keys.typedName == null || keys.otherChampionNames.size() > MAX_OTHER_CHAMPS_COUNT) {
      buf.append(" - "); // means there are no letters on that hand at all
    } else {
      buf.append(keys.typedName);
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
