/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.server.rampup;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.*;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.FarmedLeaf;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.callbacks.BranchHandler;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.utils.CplexUtils;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author tamvadss
 */
public class RampUp {
    private static Logger logger = Logger.getLogger(RampUp.class);
    private  static  IloCplex cplex  ;
    
    
    static {
        logger.setLevel( LOGGING_LEVEL);
        PatternLayout layout = new PatternLayout("%5p  %d  %F  %L  %m%n");     
        try {
            RollingFileAppender rfa =new  RollingFileAppender(layout,LOG_FOLDER+RampUp.class.getSimpleName()+ LOG_FILE_EXTENSION);
            rfa.setMaxBackupIndex(SIXTY);
            logger.addAppender(rfa);
            logger.setAdditivity(false);
        } catch (Exception ex) {
            ///
            System.err.println("Exit: unable to initialize logging"+ex);       
            exit(ONE);
        }
    } 
    
    public List< FarmedLeaf> doRampUp () throws   Exception{
        List<FarmedLeaf> result = new ArrayList<FarmedLeaf>();
        
        cplex = CplexUtils.getCPlex(new HashSet<BranchingCondition>());
         
        RampupNodecallback rn = new RampupNodecallback ();
        BranchHandler bh = new BranchHandler (new HashSet<String>  ()) ;
        cplex.use (rn);
        cplex.use (bh );
        cplex.solve ();
        //cplex.end();
        
        
        for (FarmedLeaf leaf: rn.result){
            
            result.add (leaf);
        }
        
        return result;
    }
    
    public double getSolutionValue() throws IloException {
        return cplex.getStatus().equals(IloCplex.Status.Feasible) ? cplex.getObjValue() : BILLION;
    }
    
}

