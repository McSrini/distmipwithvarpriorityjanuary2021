/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.MAX_THREADS;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.callbacks.*;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.utils.CplexUtils.*;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.NodeId;
import static java.lang.System.exit;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author tamvadss
 */
public class SubTree {
    
    private IloCplex cplex;
       
    protected static Logger logger;
    
    public static String hostname;
    public  static Set<BranchingCondition> myRootVarFixings =null;
 
    public static Map<String, NodeId > mapOfFarmedNodeIDs = null;
     
    static {
        logger=Logger.getLogger(SubTree.class);
        logger.setLevel(LOGGING_LEVEL);
        PatternLayout layout = new PatternLayout("%5p  %d  %F  %L  %m%n");     
        try {
            RollingFileAppender rfa =new  
                RollingFileAppender(layout,LOG_FOLDER+SubTree.class.getSimpleName()+ LOG_FILE_EXTENSION);
            rfa.setMaxBackupIndex(SIXTY);
            logger.addAppender(rfa);
            logger.setAdditivity(false);     
            
            hostname =  InetAddress.getLocalHost(). getHostName() ;
             
        } catch (Exception ex) {
            ///
            System.err.println("Exit: unable to initialize logging"+ex);       
            exit(ONE);
        }
    }
    
    public SubTree (Set<BranchingCondition> varFixings) throws Exception {
        myRootVarFixings = varFixings;
        cplex = getCPlex (varFixings );        
    }
    
    public SolveResult solve ( ) throws IloException {
        return sequentialSolve(ONE);
    }
    
    public Set<FarmedLeaf> farm () throws IloException{
                  
        FarmNodecallback farmingCallback =new FarmNodecallback () ;
        
        cplex.use(farmingCallback);
        cplex.setParam( IloCplex.Param.Threads, ONE);
        //allow unlimited time, although only a few seconds will be needed
        cplex.setParam( IloCplex.Param.TimeLimit, SIXTY * SIXTY* SOLUTION_CYCLE_TIME_MINUTES );
        
        cplex.solve();
        
        //reset callbacks, we need to remove the node callback
        cplex.clearCallbacks();
        
        mapOfFarmedNodeIDs = farmingCallback.mapOfFarmedLeaves.getFarmedLeaf_NodeIDs();
        
        return farmingCallback.mapOfFarmedLeaves.getFarmedLeafs();
    }
    
    public void prune (Collection<NodeId> nodeIDs) throws IloException{
        //
        Set<String> nodeNames = new HashSet<String> ();
        for (NodeId nid: nodeIDs){
            nodeNames.add (nid.toString() );
        }
        
        cplex.use (new BranchHandler(nodeNames)) ;
        cplex.use (new PruneNodeCallback(nodeIDs));
        cplex.setParam( IloCplex.Param.Threads, ONE);
        //allow unlimited time, although only a few seconds will be needed
        cplex.setParam( IloCplex.Param.TimeLimit, SIXTY * SIXTY* SOLUTION_CYCLE_TIME_MINUTES );
        
        cplex.solve();       
        
        //reset callbacks, we need to remove the node callback
        cplex.clearCallbacks();
        
    }
    
    public SolveResult sequentialSolve (int iterationLimit) throws IloException {
        
        SolveResult result = new SolveResult();
        
        cplex.use (new BranchHandler(null)) ;
        
        cplex.setParam( IloCplex.Param.Threads, MAX_THREADS);
        cplex.setParam( IloCplex.Param.TimeLimit, SIXTY* SOLUTION_CYCLE_TIME_MINUTES );
        
        for (int hour = ONE; hour <=iterationLimit ; hour ++){    
            
            cplex.solve();        
            double bestSolutionFound = BILLION;
            double relativeMipGap = BILLION;
            if (cplex.getStatus().equals( IloCplex.Status.Feasible ) || cplex.getStatus().equals( IloCplex.Status.Optimal )){
                bestSolutionFound =cplex.getObjValue();
                relativeMipGap=  cplex.getMIPRelativeGap();
            } 
           
            logger.info("" + hour + ","+  bestSolutionFound + ","+  
                cplex.getBestObjValue() + "," + cplex.getNnodesLeft64() +
                "," + cplex.getNnodes64() + "," + relativeMipGap ) ;
            
            
            result.bound= cplex.getBestObjValue();
            result.bestKnownSolution= bestSolutionFound;
            result.numLeafs=cplex.getNnodesLeft64();
            result.numNodesExplored =cplex.getNnodes64();
            result.relativeMIPgap= relativeMipGap;
                           
            if (cplex.getStatus().equals( IloCplex.Status.Infeasible ) || cplex.getStatus().equals( IloCplex.Status.Optimal )){  
                cplex.end();
                break;
            }

        }     
        
        return result;
    }
    
}
