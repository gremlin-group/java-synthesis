
package br.edu.ifsc.javargtest;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.PrimitiveType;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

public class JRGOperator {
    private ClassTable mCT;
    
    private JRGBase mBase;
    
    private JRGCore mCore;
    
    private JRGStmt mStmt;
    
     public JRGOperator(ClassTable ct , JRGBase base, JRGCore core, JRGStmt stmt) {
        mCT = ct;
                
        mBase = base;
        
        mCore = core;
        
        mStmt = stmt;       
        
    }
     
    public Arbitrary<BinaryExpr> genOperationalExpression(){
        
        return Arbitraries.oneOf(genLogiExpression(),genArithExpression(),genRelaExpression());
        
    }
     
    public Arbitrary<BinaryExpr> genLogiExpression() {
        
        Arbitrary<Expression> e = mCore.genExpression(PrimitiveType.booleanType());
                      
        Arbitrary<Expression> ex = mCore.genExpression(PrimitiveType.booleanType());
                 
        return e.map(exp -> new BinaryExpr(genRelaExpression().sample(),genRelaExpression().sample(),genLogiOperator().sample()));
        
    }
    
            
    public Arbitrary<BinaryExpr> genArithExpression() {
        Arbitrary<PrimitiveType.Primitive> t = mBase.primitiveTypesMatematicos();
        
        Arbitrary<Expression> e = mCore.genExpression(ReflectParserTranslator
                .reflectToParserType(t.sample().toString()));
        
        Arbitrary<PrimitiveType.Primitive> tx = mBase.primitiveTypesMatematicos();  
        
        Arbitrary<Expression> ex = mCore.genExpression(ReflectParserTranslator
                .reflectToParserType(tx.sample().toString()));
                 
        return e.map(exp -> new BinaryExpr(exp,ex.sample(),genArithOperator().sample()));
        
    }
    
    public Arbitrary<BinaryExpr> genRelaExpression() {
        Arbitrary<PrimitiveType.Primitive> t = mBase.primitiveTypesMatematicos();
        
        Arbitrary<Expression> e = mCore.genExpression(ReflectParserTranslator
                .reflectToParserType(t.sample().toString()));
        
        Arbitrary<PrimitiveType.Primitive> tx = mBase.primitiveTypesMatematicos();  
        
        Arbitrary<Expression> ex = mCore.genExpression(ReflectParserTranslator
                .reflectToParserType(tx.sample().toString()));
                 
        return e.map(exp -> new BinaryExpr(exp,ex.sample(),genRelaOperator().sample()));
        
    }
    //Bo
    public Arbitrary<BinaryExpr.Operator> genLogiOperator() {             
        
        return Arbitraries.of(BinaryExpr.Operator.AND,BinaryExpr.Operator.OR,
                BinaryExpr.Operator.XOR);
    }
    
    //Au
    public Arbitrary<BinaryExpr.Operator> genRelaOperator() {
    
        return Arbitraries.of(BinaryExpr.Operator.EQUALS, 
                BinaryExpr.Operator.GREATER, BinaryExpr.Operator.GREATER_EQUALS,
                BinaryExpr.Operator.LESS, BinaryExpr.Operator.LESS_EQUALS, 
                BinaryExpr.Operator.NOT_EQUALS);
    }
    
    //Ma
    public Arbitrary<BinaryExpr.Operator> genArithOperator() {
    
        return Arbitraries.of(BinaryExpr.Operator.DIVIDE, 
                BinaryExpr.Operator.MULTIPLY, BinaryExpr.Operator.MINUS, 
                BinaryExpr.Operator.PLUS, BinaryExpr.Operator.REMAINDER);
        
    }
}