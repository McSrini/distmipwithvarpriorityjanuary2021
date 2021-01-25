/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.callbacks;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.MAX_FARMED_LEAVES_COUNT;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.NUM_WORKERS;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.SizeConstrainedMap;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.FarmedLeaf;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.NodeAttachment;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.SubTree;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class FarmNodecallback  extends IloCplex.NodeCallback {
    
    public SizeConstrainedMap mapOfFarmedLeaves = new SizeConstrainedMap();
 
    //public  Set <FarmedNode>  farmedLeafs  = null;
    //public TreeMap< Double, Set <FarmResult>> farmedLeafsMap = new  TreeMap< Double, Set <FarmResult>> ();
     
    protected void main() throws IloException {
        //
        final long LEAFCOUNT =getNremainingNodes64();
        if (LEAFCOUNT>ZERO) {
            for (long leafNum = ZERO; leafNum < LEAFCOUNT; leafNum ++){
                
                try {
                    IloCplex.NodeId   nodeID =getNodeId(leafNum) ;
                    NodeAttachment attachment= (NodeAttachment)getNodeData(  leafNum );
                    double lpRelaxObjective = getObjValue (leafNum) ;
                    
                    if (null!= attachment) {
                        
                        //this leaf and ints ancestors have always been in memory
                        
                        boolean isAddable = lpRelaxObjective < mapOfFarmedLeaves.largestLPrelax;
                        isAddable = isAddable || mapOfFarmedLeaves.size < MAX_FARMED_LEAVES_COUNT;
                        
                        if (isAddable ){
                            FarmedLeaf leaf = new FarmedLeaf ();
                            leaf.lpRelax = lpRelaxObjective;
                            leaf.machineName= SubTree.hostname;
                            leaf.nodeID = nodeID.toString();
                            leaf.varFixings = getLeafBranchingCOnditions(attachment);
                            mapOfFarmedLeaves.add (leaf , nodeID);
                        }
                       
                    }
                    
                }catch (Exception ex){
                    //ignore this leaf                    
                }
                                     
            }
        }
        
        /*System.out.println("printing leafs");
        for (TreeStructureNode  leaf :leafNodeAttahments){
            System.out.println(leaf.nodeID);
        }*/
        
        
        System.out.println(" out of " + LEAFCOUNT + " farmed " + mapOfFarmedLeaves.size);
        
        abort();
    }
    
   
    
    private  Set<BranchingCondition> getLeafBranchingCOnditions(NodeAttachment attachment){
        Set<BranchingCondition> branchingConditions = new HashSet<BranchingCondition> ();
        branchingConditions.addAll(SubTree.myRootVarFixings);
        
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
