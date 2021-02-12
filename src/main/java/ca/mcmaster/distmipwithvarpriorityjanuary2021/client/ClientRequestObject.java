/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.client;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.BILLION;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.ZERO;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.FarmedLeaf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class ClientRequestObject implements Serializable {
    
    public String clientname = null;
    
    //key is lp relax, value is a list of available leaf nodes for migration
    //Farmed leaf contains machine name
    public Set<FarmedLeaf> availableNodes=null;
     
    public Boolean isIdle=null ;
    
    public double local_bestBound =BILLION;
    public double local_incumbent =BILLION;
    public long numNodesProcessed = ZERO;
    
  
    
}
