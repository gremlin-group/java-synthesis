package br.edu.ifsc.javarg;

import br.edu.ifsc.javargexamples.A;
import br.edu.ifsc.javargexamples.B;
import br.edu.ifsc.javargexamples.C;
import br.edu.ifsc.javargexamples.Aextend;
import br.edu.ifsc.javargexamples.AextendExtend;

public class MainClass {
    
    public int fieldExample1;
    public A fieldExample2;
    //A aObj = new A(5, 2);
        //int a = aObj.getA1();
        //int b = new B().b;
        //B bOjb = new C().getB();
        //int a1 = new C().getA().a1;
    public static void main(String args[]) {        
        int num = 10;
        int a;
        A aObj = new A(num, 2);
        if(num == 10) {
             a = 5;
             System.out.println(a);
        } else {
            a = 8;
        } 
        System.out.println(a);
    }
    
}
