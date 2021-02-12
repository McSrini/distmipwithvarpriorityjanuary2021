/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.callbacks;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.NodeCallback;
import ilog.cplex.IloCplex.NodeId;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class PruneNodeCallback extends NodeCallback {
    
    private Set<NodeId> nodeIDs_for_pruning = null;
    
    public PruneNodeCallback (Set<NodeId> nodes_for_pruning){
        this.nodeIDs_for_pruning = nodes_for_pruning;
    }

  
    protected void main() throws IloException {
        //
        final long LEAFCOUNT =getNremainingNodes64();
        
        if (LEAFCOUNT>ZERO   ) {
            
            IloCplex.NodeId pruneTarget =  getPruneTarget();
            if (pruneTarget!=null){
                selectNode ( getNodeNumber64( pruneTarget )  ) ;
                //System.out.println(" node selected " + pruneTarget ) ;
            }else {
                abort ();
            }
            
        }
    }
    
    //sometimes, cplex silenty prunes infeasible nodes we were planning to prune explicitly
    private  IloCplex.NodeId   getPruneTarget () {
        IloCplex.NodeId  result = null;
           
        for (IloCplex.NodeId nd: nodeIDs_for_pruning){
            try  {
                getNodeNumber64(nd);
                result =nd ;                
                break;
            } catch (IloException iloEx) {
                //System.out.println(" no longer exists -------------- " + nd );
            }
        }         
        return result;
    }
    
}
