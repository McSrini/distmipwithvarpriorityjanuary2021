/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021;

/**
 *
 * @author tamvadss
 */
public class Parameters {
    
    public static final String MIP_NAME = "dws008";
    //public static final String MIP_NAME = "opm2-z10-s4";
    public static final String MIP_FILENAME =MIP_NAME  + ".pre.sav";    
    public static String PRIORITY_LIST_FILENAME = MIP_NAME + "_priorityList.ser";
    public static final String MIP_FOLDER = 
             System.getProperty("os.name").toLowerCase().contains("win") ?  "F:\\temporary files here recovered\\": "";
    
    public static final int NUM_WORKERS = 5;
    public static final int MAX_FARMED_LEAVES_COUNT = NUM_WORKERS - 1;
    
    public static int CPX_PARAM_VARSEL = 2; //pseudo cost branching
    public static int CPX_PARAM_NODEFILEIND = 3; //disk and compressed
    public static int CPX_PARAM_HEURFREQ = -1; //disabled
    public static int CPX_PARAM_MIPSEARCH = 1 ; //traditional 
    //public static int CPXPARAM_DistMIP_Rampup_Duration = 1;// unused here, forces dist mip
    public static int CPX_PARAM_MIPEMPHASIS = 2 ;//optimality    
    public static  int MAX_THREADS =   System.getProperty("os.name").toLowerCase().contains("win") ? 1 : 32;
    public static boolean USE_BARRIER_FOR_SOLVING_LP = MIP_NAME.equals("neostutaki" ) ||  MIP_NAME.equals("neoshuahum" ) ;
    
    public static final boolean USE_VAR_PRIORITY_LIST = true;
    public static int MAX_PRIORITY_VARS  = 50;
    
}
