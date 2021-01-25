/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.MAX_FARMED_LEAVES_COUNT;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.NodeId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class SizeConstrainedMap {
    
    private TreeMap < Double , List<FarmedLeaf> > bag = new TreeMap < Double , List<FarmedLeaf> > ();
    private static Map <String, IloCplex.NodeId > farmedNodeIDMap = new HashMap  <String, IloCplex.NodeId > ();
    
    public int size = ZERO;
    public double largestLPrelax =   BILLION;
    
    public  Map <String, IloCplex.NodeId >   getFarmedLeaf_NodeIDs  () {
        return Collections.unmodifiableMap(farmedNodeIDMap);
    }
    
    public Set <FarmedLeaf>  getFarmedLeafs  () {
        Set <FarmedLeaf>  farmedLeafs = new HashSet <> ();
        
        for (  List<FarmedLeaf>  leafSet : bag.values()){
            for (FarmedLeaf leaf :   leafSet ){
                farmedLeafs.add (leaf) ;
            }
        }
         
        return farmedLeafs;
    }
    
    public  void  add (FarmedLeaf leaf, NodeId nodeID) {
        if (size >= MAX_FARMED_LEAVES_COUNT ){
            //remove one of the largest LP relax lefas from bag and the nam-ID map
            removeLeaf () ;            
        }
        
        List<FarmedLeaf> thisList = bag.get(leaf.lpRelax );
        if (null==thisList){
            thisList = new ArrayList <FarmedLeaf> ( );
        }
        thisList.add (leaf );
        bag.put (leaf.lpRelax, thisList) ;
        farmedNodeIDMap.put( leaf.nodeID, nodeID);
        
        size ++;
        this.largestLPrelax = this.bag.lastKey();
         
    }
    
    private FarmedLeaf removeLeaf (){
        FarmedLeaf removedLeaf = null;
        
        Map.Entry < Double , List<FarmedLeaf> > entry =  bag.lastEntry();
        List<FarmedLeaf> thisList = entry.getValue();
        removedLeaf =thisList.remove(ZERO);
        
        if (thisList.size()> ZERO) {
            bag.put (entry.getKey(), thisList) ;
        }else {
            bag.remove(entry.getKey() );
        }        
        
        farmedNodeIDMap.remove( removedLeaf.nodeID);
        
        size --;
        this.largestLPrelax = this.bag.lastKey();
        
        //System.out.println("removedLeaf from bag" + removedLeaf.nodeID);
        
        return removedLeaf;
    }
    
}
