/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class FarmedLeaf implements Serializable {
    
    public String machineName ;
    public String nodeID;
    public double lpRelax;
    public Set<BranchingCondition> varFixings;
    
    public void printMe (){
        System.out.print(" " + machineName + ", "+ nodeID + " ,"+ lpRelax + "  \n") ;
        for (BranchingCondition bc: varFixings){
            System.out.print(bc);
        }
        System.out.println() ;
    }
    
}
