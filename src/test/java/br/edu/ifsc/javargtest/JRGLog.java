/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.javargtest;

/**
 *
 * @author samuel
 */
public class JRGLog {
    
    public enum Severity {
        MSG_ERROR,
        MSG_WARN,
        MSG_INFO,
        MSG_DEBUG,
        MSG_XDEBUG
    }
    
    public static Severity logLevel = Severity.MSG_ERROR;
    
    public static void showMessage(Severity s, String msg) {
        if (logLevel.ordinal() >= s.ordinal()) {
            System.out.println(msg);
        }
        
    }
   
}
