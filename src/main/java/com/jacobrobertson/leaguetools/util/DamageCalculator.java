package com.jacobrobertson.leaguetools.util;

public class DamageCalculator {

  
  public static class Unit {
    
    private float attackSpeed;
    private int initialMana;
    private int totalMana;
    
    private Spell[] spell;
    
  }
  public static class Spell {
    
    private int baseDamage;
    private int numberOfTargetsExample;
    
    // for example, Ahri will cast it x2, although even that is a simplification
    // could also take MR into account (i.e. true damage)
    private int multiplier; // needs to allow for Syndra to do more damage each cast
    
  }
  
}
