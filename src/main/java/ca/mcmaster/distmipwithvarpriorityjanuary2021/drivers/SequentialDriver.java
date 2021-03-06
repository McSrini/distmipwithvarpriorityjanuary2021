/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.drivers;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.USE_WELL_KNOWN_OPTIMAL_AT_START;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.SubTree;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class SequentialDriver {
    
    public static void main(String[] args) throws Exception {
        //
        SubTree tree = new SubTree (new HashSet<BranchingCondition> ()) ;
        tree.sequentialSolve(BILLION, USE_WELL_KNOWN_OPTIMAL_AT_START); // 100 30 minute iterations = 50 hours
        
    }
    
}
