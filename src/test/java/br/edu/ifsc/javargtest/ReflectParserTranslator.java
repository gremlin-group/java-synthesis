/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.javargtest;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

/**
 *
 * @author samuel
 */
public class ReflectParserTranslator {
    
    public static Type reflectToParserType(String tname) {
        
        switch (tname.toLowerCase()) {
            case "int":
                return PrimitiveType.intType();
            case "float":
                return PrimitiveType.floatType();
            case "double":
                return PrimitiveType.doubleType();
            case "boolean":
                return PrimitiveType.booleanType();
            case "char":
                return PrimitiveType.charType();
            case "long":
                return PrimitiveType.longType();
            case "byte":
                return PrimitiveType.byteType();
            case "short":
                return PrimitiveType.shortType();   
            default:
                break;
        }
        
        return new ClassOrInterfaceType(null, tname);
    }
    
}
