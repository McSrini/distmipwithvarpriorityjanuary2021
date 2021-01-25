/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021.utils;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Constants.ONE;
import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.*;
import ca.mcmaster.distmipwithvarpriorityjanuary2021.subtree.BranchingCondition;
import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class CplexUtils {
  
    public static IloCplex getCPlex (Set<BranchingCondition> varFixings) throws Exception {
        
        IloCplex cplex = new IloCplex();
        cplex.importModel(  MIP_FOLDER + MIP_FILENAME);
        
        applyVarFixings (cplex, varFixings) ;
        
        cplex.setParam( IloCplex.Param.MIP.Strategy.File,  CPX_PARAM_NODEFILEIND );
        cplex.setParam( IloCplex.Param.MIP.Strategy.HeuristicFreq ,CPX_PARAM_HEURFREQ); 
        cplex.setParam(IloCplex.Param.Emphasis.MIP, CPX_PARAM_MIPEMPHASIS) ; 
        if (USE_BARRIER_FOR_SOLVING_LP) {
            cplex.setParam( IloCplex.Param.NodeAlgorithm  ,  IloCplex.Algorithm.Barrier);
            cplex.setParam( IloCplex.Param.RootAlgorithm  ,  IloCplex.Algorithm.Barrier);
        }
        
        cplex.setParam(IloCplex.Param.MIP.Strategy.Search, CPX_PARAM_MIPSEARCH ) ; 
        cplex.setParam(IloCplex.Param.MIP.Strategy.VariableSelect,  CPX_PARAM_VARSEL ) ; 
        
                
        if (USE_VAR_PRIORITY_LIST){
            File file = new File(PRIORITY_LIST_FILENAME ); 
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(PRIORITY_LIST_FILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                List<String>  recreatedVarPriorityList = (List<String>) ois.readObject();
                ois.close();
                fis.close();
                
                int priorityValue = ONE + MAX_PRIORITY_VARS;
                Map<String, IloNumVar> varMap = CplexUtils. getVariables (  cplex);
                for (String varname :  recreatedVarPriorityList){
                    cplex.setPriority(varMap.get (varname) , priorityValue-- );
                }
            }else {
                System.err.println("cannot find var priority list ! ") ;
                exit (1);
            }
        }
        
        return cplex;        
    }
    
    public static void applyVarFixings ( IloCplex cplex, Set<BranchingCondition> varFixings) throws IloException {
        Map<String, IloNumVar> varMap = CplexUtils. getVariables (  cplex);
        
        for (BranchingCondition condition : varFixings ){
            
            IloNumVar var= varMap.get (condition.varName);
            double newBound= condition.bound;
            if (  condition.isBranchDirectionDown ){
                CplexUtils.updateVariableBounds( var,   newBound, true  )   ;
            }else {
                CplexUtils.updateVariableBounds( var,   newBound, false  )   ;
            }
            
        }
    }
    
           
    /**
     * 
     *  Update variable bounds as specified    
    */
    public static   void updateVariableBounds(IloNumVar var, double newBound, boolean isUpperBound   )      throws IloException{
 
        if (isUpperBound){
            if ( var.getUB() > newBound ){
                //update the more restrictive upper bound
                var.setUB( newBound );
                //System.out.println(" var " + var.getName() + " set upper bound " + newBound ) ;
            }
        }else{
            if ( var.getLB() < newBound){
                //update the more restrictive lower bound
                var.setLB(newBound);
                //System.out.println(" var " + var.getName() + " set lower bound " + newBound ) ;
            }
        }  

    } 
    
    public static Map<String, IloNumVar> getVariables (IloCplex cplex) throws IloException{
        Map<String, IloNumVar> result = new HashMap<String, IloNumVar>();
        IloLPMatrix lpMatrix = (IloLPMatrix)cplex.LPMatrixIterator().next();
        IloNumVar[] variables  =lpMatrix.getNumVars();
        for (IloNumVar var :variables){
            result.put(var.getName(),var ) ;
        }
        return result;
    }
    
}
 
   
