/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import java.io.Serializable;

/**
 *
 * @author tamvadss
 */
public class BranchingCondition implements Serializable {
    public String varName = null;
    public double bound = -ONE;     
    public boolean isBranchDirectionDown = true;
    
    public String toString (){
        return "("+ varName + ", "+ bound + ", "+ isBranchDirectionDown+")";
    }
}
