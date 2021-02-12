/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.server.rampup;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.*;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.FarmedLeaf;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.NodeAttachment;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class RampupNodecallback extends IloCplex.NodeCallback {
    
    public List<FarmedLeaf> result = new ArrayList<FarmedLeaf>();
 
    protected void main() throws IloException {
        //
       
        if (getNremainingNodes64()==NUM_WORKERS){
            //prepare leafs
            for (int leafNum = ZERO; leafNum < NUM_WORKERS ; leafNum ++){
                //
                NodeAttachment attach = (NodeAttachment) getNodeData (leafNum) ;
                FarmedLeaf leaf = new FarmedLeaf ();
                leaf .varFixings =  getLeafBranchingCOnditions (attach) ;
                result.add (leaf );
            }

            abort();
        }
    }
    
  
    private  Set<BranchingCondition> getLeafBranchingCOnditions(NodeAttachment attachment){
        Set<BranchingCondition> branchingConditions = new HashSet<BranchingCondition> ();
        
        NodeAttachment current = attachment;
        NodeAttachment parent = current.parentNode;
        while (null != parent){
            BranchingCondition cond = new BranchingCondition ();
            if (current.amITheDownBranchChild) {
                cond.bound = parent.upperBound;
                cond.isBranchDirectionDown= true;
                cond.varName = parent.branchingVarName;
            }else {
                cond.bound = ONE + parent.upperBound;
                cond.isBranchDirectionDown= false;
                cond.varName = parent.branchingVarName;
            }
            
            branchingConditions.add (cond);
            
            current = parent;
            parent = parent.parentNode;
            
        }
        
        return branchingConditions;
    }
}
