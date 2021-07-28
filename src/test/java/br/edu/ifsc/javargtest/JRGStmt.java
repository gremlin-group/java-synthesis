
package br.edu.ifsc.javargtest;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Provide;

public class JRGStmt {
    private ClassTable mCT;
    
    private JRGBase mBase;
    
    private Map<String,String> mCtx;
    
    private List<String> mValidNames;
    
    private JRGCore mCore;
    
    private JRGOperator mOperator; 
    
    public JRGStmt(ClassTable ct , JRGBase base, JRGCore core, JRGOperator operator) {
        mCT = ct;
                
        mBase = base;
        
        mCore = core;
        
        mOperator = operator;
        
        mCtx =  new HashMap<String, String>() {{
            put("a", "int");
            put("b", "int");
            put("c", "br.edu.ifsc.javargexamples.C");
        }};

        mValidNames = Arrays.asList("a","b","c","d","e","f","g");
        
        
    }
    @Provide
    public Arbitrary<VariableDeclarationExpr> genVarDeclaration(Type t, String n) {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclaration::inicio");       
        
        Arbitrary<VariableDeclarator> e = genVarDeclarator(t,n);
       
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclaration::fim"); 
        
        return e.map(obj -> new VariableDeclarationExpr(obj));
    }
    
   @Provide
    public  Arbitrary<Statement> genStatement() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatement::inicio");       
        //Gerar todos o possiveis statement menos o block   
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatement::fim");   
        
        return Arbitraries.oneOf(genIfStmt(), genWhileStmt(), genExpressionStmt());
    } 
    
    @Provide
    public Arbitrary<NodeList<Statement>> genStatementList() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatementList::inicio");       
                       
        List<Statement> exs = (List<Statement>) genStatement();
        
        NodeList<Statement> nodes = new NodeList<>(exs);
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatementList::fim");
        
        return Arbitraries.just(nodes);
    }      
    
    @Provide
    public Arbitrary<BlockStmt> genBlockStmt(Type types) 
            throws ClassNotFoundException {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genBlockStmt::inicio");       
        
        List<Statement> stmt;               
        
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genBlockStmt::fim");
        
        return null;
    }
    
    @Provide
    public Arbitrary<VariableDeclarator> genVarDeclarator(Type t, String n) {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclarator::inicio");
        
        Arbitrary<Expression> e = mCore.genExpression(t);       
       
         
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclarator::fim");
        return e.map(obj -> new VariableDeclarator(t, n, obj));
    }    
    
    @Provide
    public Arbitrary<IfStmt> genIfStmt() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genIfStmt::inicio");       
        
        Arbitrary<Expression> e = mCore.genExpression(PrimitiveType.booleanType());       
                
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genIfStmt::fim"); 
     
        return e.map(exp -> new IfStmt(exp
                ,genStatement().sample(),genStatement().sample()));
        
    }
    
    @Provide
    public Arbitrary<WhileStmt> genWhileStmt() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genWhileStmt::inicio");       
                       
        Arbitrary<Expression> e = mCore.genExpression(PrimitiveType.booleanType());
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genWhileStmt::fim"); 
        return e.map(exp -> new WhileStmt(exp,genStatement().sample()));
    }   
    
    @Provide
    public Arbitrary<ExpressionStmt> genExpressionStmt(){
        //@TODO: Sortear o tipo aleatoriamente e passar para genExpression
        Arbitrary<PrimitiveType.Primitive> t = mBase.primitiveTypes();
         
        Arbitrary<Expression> e = mCore.genExpression(ReflectParserTranslator
                .reflectToParserType(t.sample().toString()));
        
        return e.map(exp -> new ExpressionStmt(exp));
    }
}
