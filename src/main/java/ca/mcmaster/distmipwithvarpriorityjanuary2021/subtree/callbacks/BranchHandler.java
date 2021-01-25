/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.callbacks;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.NodeAttachment;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.BranchCallback;
import ilog.cplex.IloCplex.NodeId;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class BranchHandler extends BranchCallback {
    
    private Set<String> nodeIDs_for_pruning = null;
    
    public BranchHandler (Set<String> nodes_for_pruning){
        this.nodeIDs_for_pruning = nodes_for_pruning;
    }
    
    private boolean isPruneTarget () throws IloException {
        return nodeIDs_for_pruning!=null && nodeIDs_for_pruning.contains(getNodeId().toString() );
    }
 
    protected void main() throws IloException {
        // 
        if ( getNbranches()> ZERO && ! isPruneTarget()){  
            
            String thisNodeID=getNodeId().toString();
            if (thisNodeID.equals( MIPROOT_NODE_ID)){
                //root node
                NodeAttachment attachment = new   NodeAttachment ( );
                setNodeData (attachment );
            } 
            
            //get the branches about to be created
            IloNumVar[][] vars = new IloNumVar[TWO][] ;
            double[ ][] bounds = new double[TWO ][];
            IloCplex.BranchDirection[ ][]  dirs = new  IloCplex.BranchDirection[ TWO][];
            getBranches(  vars, bounds, dirs);
            
            NodeAttachment thisNodesAttachment = null;
            try {
                thisNodesAttachment  = (NodeAttachment) getNodeData () ;
            }        catch (Exception ex){
                //stays null
            }
            
            //now allow  both kids to spawn
            for (int childNum = ZERO ;childNum<getNbranches();  childNum++) {   

                IloNumVar var = vars[childNum][ZERO];
                double bound = bounds[childNum][ZERO];
                IloCplex.BranchDirection dir =  dirs[childNum][ZERO];     

                boolean isDownBranch = dir.equals(   IloCplex.BranchDirection.Down);

                IloCplex.NodeId  kid = null;
                if (null==thisNodesAttachment){
                    //default
                    kid = makeBranch(var,bound, dir ,getObjValue());
                }else {
                    NodeAttachment attach = new NodeAttachment ( );
                    attach.parentNode = thisNodesAttachment;
                    
                    if (isDownBranch) {
                        attach.amITheDownBranchChild = true;
                        thisNodesAttachment.branchingVarName= var.getName();
                        thisNodesAttachment.upperBound= bound;                         
                    } else {
                        if (thisNodesAttachment.branchingVarName==null){
                            thisNodesAttachment.branchingVarName= var.getName();
                            thisNodesAttachment.upperBound= bound- ONE; 
                        }
                    }
                   
                    //create the kid
                    kid = makeBranch(var,bound, dir ,getObjValue(), attach); 
                }

                //System.out.println("Node " + getNodeId() + " created " + kid + " isdown " + isDownBranch + " var " + var.getName()) ;

            } 

        } else if (getNbranches()> ZERO){
            //prune this node
            prune();
            nodeIDs_for_pruning.remove (getNodeId().toString());
            
            System.out.println("pruned leaf "+getNodeId() ) ;
            
        }
    }
    
}
