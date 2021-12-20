/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.javargexamples;

/**
 *
 * @author samuel
 */
public class A {
    
    public int a1;
    public int a2;
    public boolean a3;
    
    
    public A(int a1, int a2, boolean a3) {
        this.a1 = a1;        
        this.a2 = a2;
        this.a3 = a3;
    }
    
    public void setA1(int a1) {
        this.a1 = a1;
    }
    
    public int getA1() {
        return a1;
    }
    
    public boolean isA3() {
        return a3;
    }
    
    public void setA2(int a2) {
        this.a2 = a2;
    }
    
    public int getA2() {
        return a2;
    }
    
}
