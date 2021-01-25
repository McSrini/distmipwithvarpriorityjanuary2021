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
public class SolveResult implements Serializable {
    
    public double bestKnownSolution = BILLION;
    public double bound = - BILLION;
    public long numLeafs = -ONE;
    public long numNodesExplored= -ONE;
    public double relativeMIPgap = BILLION;
    
    
    
}
