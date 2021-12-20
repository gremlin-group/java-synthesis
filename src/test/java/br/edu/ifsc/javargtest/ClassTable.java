/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.javargtest;

import java.util.List;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClassTable {
    
    private  List<String> mImports;
    
    public ClassTable(List<String> imports) {
        mImports = imports;
       
    }
    
    public  List<String> getTypes() throws ClassNotFoundException {
        List<String> list = new ArrayList<>();
        
        for (String s : mImports) {
            list.add(Class.forName(s).getName());
        }
        
        return list;
    }
    
    public  List<Field> getCandidateFields(String type) 
            throws ClassNotFoundException {
        List<Field> candidates = new ArrayList<>();
        
        for (String c : mImports) {
            List<Field> flds = getClassFields(c);
            
            List<Field> collect = flds.stream().filter(
                    f -> f.getType().toString().equals(type))
                    .collect(Collectors.toList());
            
            candidates.addAll(collect);
        }
        
        return candidates;
    }
    
    public  List<Method> getCandidateMethods(String type) 
            throws ClassNotFoundException {
        List<Method> candidatesMethod = new ArrayList<>();
    
        for(String c : mImports){
            List<Method> mthd = getClassMethods(c);
            
            List<Method> collect = mthd.stream().filter(
                    m -> m.getReturnType().toString().equals(type))
                    .collect(Collectors.toList());
            
            candidatesMethod.addAll(collect);
        }
        
        return candidatesMethod;
    }
    
    
     public  List<Constructor> getCandidateConstructors(String type) 
             throws ClassNotFoundException {
        List<Constructor> candidatesConstructor = new ArrayList<>();
        
        List<Constructor> cntc = getClassConstructors(type);
          
        candidatesConstructor.addAll(cntc);
        
        return candidatesConstructor;
    }
     
    public  List<Field> getClassFields(String cname) 
            throws ClassNotFoundException {
        List<Field> list = new ArrayList<>();
        
        Class c = Class.forName(cname);
        
        Field f[] = c.getFields();
        
        list.addAll(Arrays.asList(f));
        
        return list;
    }
    
    public  List<String> getClassFieldTypes(String cname) 
            throws ClassNotFoundException {
        List<String> list = getClassFields(cname).stream()
                .map(f -> f.getGenericType().getTypeName())
                .collect(Collectors.toList());
        
        return list;        
    }
    

    public  List<Method> getClassMethods(String cname) 
           throws ClassNotFoundException {
        List<Method> list = new ArrayList<>();

        Class c = Class.forName(cname);

        Method m[] = c.getDeclaredMethods();

        list.addAll(Arrays.asList(m));

        return list;
    }
    
    
    public  List<Constructor> getClassConstructors(String cname) 
           throws ClassNotFoundException {
       List<Constructor> list = new ArrayList<>();
       
       Class c = Class.forName(cname);
       
       Constructor ct[] = c.getDeclaredConstructors();
       
       list.addAll(Arrays.asList(ct));
       
       return list;
       
    }
    
    public List<Class> superTypes(String cname) throws ClassNotFoundException {
        List<Class> list = new ArrayList<>();
        
        Class c = Class.forName(cname);
        
        Class st = c.getSuperclass();      
               
        while(st != null) {
            list.add(st);
            c = st;
            st = c.getSuperclass();        
                       
        }            
       
        return list;    
    }
    
    public  List<Class> subTypes(String cname) throws ClassNotFoundException {
        List<Class> list = new ArrayList<>();              
        
        System.out.println(cname);
        
        Class c = Class.forName(cname);
        
        list.add(c);
        
        if(!cname.equals("java.lang.Object")) {        
            list.addAll(subTypes(c.getSuperclass().getName()));
        }    
        
        return list;         
    }
    
    public List<Class> subTypes2(String cname) throws ClassNotFoundException {
        List<Class> list = new ArrayList<>();
        
        Class c = Class.forName(cname);
        
        for(String cl : this.mImports) {
            List<Class> st = superTypes(cl);
            
            if(st.contains(c)){
                Class cla = Class.forName(cl);                
                list.add(cla);
            }            
        }
        
        return list;
    }
   
           
}
