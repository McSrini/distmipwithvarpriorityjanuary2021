/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.server;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class ServerResponseObject  implements Serializable {
    
    public double globalIncumbent = BILLION;
    
    //if assignment is not null , use these conditions to create a subtree and start solving it
    public   Set<BranchingCondition> assignment=null ;
            
    //
    public Set < String> pruneList=null;
    
}
