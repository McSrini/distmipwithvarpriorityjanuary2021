/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.distmipwithvarpriorityjanuary2021;

import static ca.mcmaster.distmipwithvarpriorityjanuary2021.Parameters.MIP_NAME;
import org.apache.log4j.Level;

/**
 *
 * @author tamvadss
 */
public class Constants {
    
    public static final int SOLUTION_CYCLE_TIME_MINUTES = 30;
    
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final double  DOUBLE_ONE = 1;
    public static final int TWO = 2;
    public static final int TEN = 10;
    public static final int FIFTY = 50;
    public static final int SIXTY = 60;
    public static final int THOUSAND = 1000;
    public static final int BILLION = 1000 * 1000 * 1000;
    
    public static final double REALTIVE_MIP_GAP_THRESHOLD = 0.0001;
    
    public static   final String LOG_FOLDER="./logs/"; 
    public static   final String LOG_FILE_EXTENSION = ".log";
    public static   final Level LOGGING_LEVEL= Level.INFO ;    
    
    public static final String MIPROOT_NODE_ID = "Node0";
    
   
    
}
