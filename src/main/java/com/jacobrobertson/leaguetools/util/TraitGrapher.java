package com.jacobrobertson.leaguetools.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.nio.gml.GmlExporter;
import org.jgrapht.nio.graphml.GraphMLExporter;

public class TraitGrapher {

  public static void main(String[] args) throws Exception {
    Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

    Set<String> addedVertices = new HashSet<>();
    Map<String, Set<String>> traitsToUnits = new HashMap<>();
    
    for (int i = 0; i < DATA.length; i++) {
      String trait = DATA[i][1];
      String unit = DATA[i][0];
      if (addedVertices.add(unit)) {
        g.addVertex(unit);
//        System.out.println(unit);
      }
      Set<String> units = traitsToUnits.get(trait);
      if (units == null) {
        units = new HashSet<>();
        traitsToUnits.put(trait, units);
      }
      units.add(unit);
    }
    
    traitsToUnits.forEach((trait, units) -> {
      Set<String> unitsCopy = new HashSet<>(units);
      units.forEach(unit -> {
        unitsCopy.forEach(unit2 -> {
          if (!unit2.equals(unit)) {
            g.addEdge(unit, unit2);
          }
        });
      });
    });
    
//    System.out.println(g);
    
    
//    GraphMLExporter<String, DefaultEdge> exporter = new GraphMLExporter<>();
//    exporter.setExportVertexLabels(true);
    
    GmlExporter<String, DefaultEdge> exporter = new GmlExporter<>();
    exporter.setParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS, true);
    exporter.exportGraph(g, System.out);
  }
  
  private static final String[][] DATA = {

{"Xayah","Celestial"},
{"Rakan","Celestial"},
{"Xin Zhao","Celestial"},
{"Ashe","Celestial"},
{"Kassadin","Celestial"},
{"Lulu","Celestial"},
{"Caitlyn","Chrono"},
{"TwistedFate","Chrono"},
{"Blitzcrank","Chrono"},
{"Shen","Chrono"},
{"Ezreal","Chrono"},
{"Wukong","Chrono"},
{"Thresh","Chrono"},
{"Fiora","Cybernetic"},
{"Leona","Cybernetic"},
{"Lucian","Cybernetic"},
{"Vi","Cybernetic"},
{"Irelia","Cybernetic"},
{"Ekko","Cybernetic"},
{"Jarvan IV","Dark Star"},
{"Mordekaiser","Dark Star"},
{"Karma","Dark Star"},
{"Lux","Dark Star"},
{"Shaco","Dark Star"},
{"Jhin","Dark Star"},
{"Annie","Mech-Pilot"},
{"Rumble","Mech-Pilot"},
{"Fizz","Mech-Pilot"},
{"Ziggs","Rebel"},
{"Malphite","Rebel"},
{"Sona","Rebel"},
{"Yasuo","Rebel"},
{"Master Yi","Rebel"},
{"Jinx","Rebel"},
{"Aurelion Sol","Rebel"},
{"Graves","Space Pirate"},
{"Darius","Space Pirate"},
{"Jayce","Space Pirate"},
{"Gangplank","Space Pirate"},
{"Poppy","Star Guardian"},
{"Zoe","Star Guardian"},
{"Ahri","Star Guardian"},
{"Neeko","Star Guardian"},
{"Syndra","Star Guardian"},
{"Soraka","Star Guardian"},
{"Kai'Sa","Valkyrie"},
{"Kayle","Valkyrie"},
{"Miss Fortune","Valkyrie"},
{"Kha'Zix","Void"},
{"Cho'Gath","Void"},
{"Vel'Koz","Void"},
{"Fiora","Blademaster"},
{"Xayah","Blademaster"},
{"Shen","Blademaster"},
{"Yasuo","Blademaster"},
{"Master Yi","Blademaster"},
{"Irelia","Blademaster"},
{"Kayle","Blademaster"},
{"Graves","Blaster"},
{"Lucian","Blaster"},
{"Ezreal","Blaster"},
{"Jinx","Blaster"},
{"Miss Fortune","Blaster"},
{"Malphite","Brawler"},
{"Blitzcrank","Brawler"},
{"Vi","Brawler"},
{"Cho'Gath","Brawler"},
{"Ziggs","Demolitionist"},
{"Rumble","Demolitionist"},
{"Gangplank","Demolitionist"},
{"Kha'Zix","Infiltrator"},
{"Kai'Sa","Infiltrator"},
{"Shaco","Infiltrator"},
{"Fizz","Infiltrator"},
{"Ekko","Infiltrator"},
{"Darius","Mana-Reaver"},
{"Kassadin","Mana-Reaver"},
{"Irelia","Mana-Reaver"},
{"Thresh","Mana-Reaver"},
{"Gangplank","Mercenary"},
{"Miss Fortune","Mercenary"},
{"Sona","Mystic"},
{"Karma","Mystic"},
{"Soraka","Mystic"},
{"Lulu","Mystic"},
{"Jarvan IV","Protector"},
{"Rakan","Protector"},
{"Xin Zhao","Protector"},
{"Neeko","Protector"},
{"Caitlyn","Sniper"},
{"Ashe","Sniper"},
{"Jhin","Sniper"},
{"Zoe","Sorcerer"},
{"TwistedFate","Sorcerer"},
{"Ahri","Sorcerer"},
{"Annie","Sorcerer"},
{"Lux","Sorcerer"},
{"Syndra","Sorcerer"},
{"Vel'Koz","Sorcerer"},
{"Aurelion Sol","Starship"},
{"Leona","Vanguard"},
{"Poppy","Vanguard"},
{"Mordekaiser","Vanguard"},
{"Jayce","Vanguard"},
{"Wukong","Vanguard"},

  };
  
}
