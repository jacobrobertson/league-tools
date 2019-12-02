package com.jacobrobertson.leaguetools.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.jacobrobertson.leaguetools.util.NamesKeyboardFinder.Hands;

public class NameComparator implements Comparator<String> {

  private final NameInfo nameInfo;
  private List<NameScorer> scorers = new ArrayList<>();

  public NameComparator(NameInfo nameInfo) {
    this.nameInfo = nameInfo;
    scorers.add(new IsSimpleRepeatedLetterScorer());
    scorers.add(new CleanNameScorer());
    scorers.add(new HandSwitchesScorer());
    scorers.add(new StartsWithFirstLetterScorer());
    scorers.add(new NonAlphaScorer());
    scorers.add(new DistanceFromLeftScorer());
  }

  public int compare(String o1, String o2) {

    // TODO
    // X * rank any special symbols lower
    // X *** not sure about space - I think so, because it's weird
    // * how far from the left (total)
    // X * rank anything that starts with the first letter higher (is this unnecessary with the distance scorer?)
    // * easy to type things?
    // *** there are two examples, but one is too crazy.
    // *** zz, nn, etc. -- anything that is same letter repeated
    // *** I could create an entire "keyboard" class and then we rank easier to type things higher,
    // *** like pokm is "easier" than pnoh
    // *** another idea which is not that crazy to implement is I could just check the distance
    // letters are from each other left to right
    // *** to make that work, I'd have to do something like "qwert~~~~~~~~~~~~~~~~~~~~~~~~asdfg..."
    // so that "t" and "a" weren't close

    // TODO - beyond that, add the "." and " " and "'" back
    // ----- however, I really don't like those, so I'm not sure. For sure, rank those lower


    for (NameScorer scorer : scorers) {
      Integer score1 = scorer.score(o1, nameInfo);
      Integer score2 = scorer.score(o2, nameInfo);
      int comp = score1.compareTo(score2);
      if (comp != 0) {
        return comp;
      }
    }
    
    // default to alphabetical
    return o1.compareTo(o2);
  }
  
  interface NameScorer {
    // better means lower score - sort to the top of the list
    int score(String name, NameInfo info);
  }

  class CleanNameScorer implements NameScorer {
    public int score(String name, NameInfo info) {
      if (info.getCleanName().startsWith(name)) {
        return 0;
      } else {
        return 1;
      }
    }
  }
  class HandSwitchesScorer implements NameScorer {
    public int score(String name, NameInfo info) {
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
  }
  class StartsWithFirstLetterScorer implements NameScorer {
    public int score(String name, NameInfo info) {
      if (info.getCleanName().charAt(0) == name.charAt(0)) {
        return 0;
      } else {
        return 1;
      }
    }
  }
  class NonAlphaScorer implements NameScorer {
    public int score(String name, NameInfo info) {
      int count = 0;
      for (int i = 0; i < name.length(); i++) {
        if (!Character.isAlphabetic(name.charAt(i))) {
          count++;
        }
      }
      return count;
    }
  }
  class IsSimpleRepeatedLetterScorer implements NameScorer {
    public int score(String name, NameInfo info) {
      char c = 0;
      for (int i = 0; i < name.length(); i++) {
        if (c == 0) {
          c = name.charAt(i);
        } else if (name.charAt(i) != c) {
          return 1;
        }
      }
      return 0;
    }
  }
  class DistanceFromLeftScorer implements NameScorer {
    public int score(String testName, NameInfo info) {
      StringBuilder buf = new StringBuilder(info.getCleanName());
      String infoName = buf.toString();
      int distance = 0;
      for (int i = 0; i < testName.length(); i++) {
        distance += score(testName, i, infoName);
        buf.setCharAt(i, '~');
        infoName = buf.toString();
      }
      return distance;
    }
    public int score(String testName, int pos, String infoName) {
      char c = testName.charAt(pos);
      int cpos = infoName.indexOf(c);
      // impossible for that to be negative - validate this
      if (cpos < 0) {
        throw new IllegalArgumentException();
      }
      return cpos;
    }
  }
}
