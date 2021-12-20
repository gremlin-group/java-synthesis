
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.EdgeCases;
import net.jqwik.api.Provide;
import net.jqwik.api.RandomGenerator;
import net.jqwik.api.arbitraries.IntegerArbitrary;

public class JRGStmt {
    private ClassTable mCT;
    
    private static final int FUEL_START = 10;
    
    private int mFuel;
    
    
    private JRGBase mBase;
    
    private Map<String,String> mCtx;
    
    private List<String> mValidNames;
    
    private JRGCore mCore;
    
    private JRGOperator mOperator; 
    
    public static final int MAX_STMT = 5;
    
    public JRGStmt(ClassTable ct , JRGBase base, JRGCore core) {
        mCT = ct;
                
        mBase = base;
        
        mCore = core;
        
        mOperator = new JRGOperator(mCT , mBase , mCore);
        
        mCtx =  new HashMap<String, String>();
        
        mCtx.put("b", "int");
        
        mCtx.put("a", "int");
        
        mCtx.put("c", "br.edu.ifsc.javargexamples.C");
        
        List<String> tempNames = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
        mValidNames = new LinkedList<>(tempNames);
        
        for(String l1 : tempNames) {
            for(String l2 : tempNames) {
                String letra = "";
                letra = l1 + l2;
                mValidNames.add(letra);
            }
        }
        
        mFuel = FUEL_START;
        
    }
    
    //ExpressionStmt
    @Provide
    public Arbitrary<VariableDeclarationExpr> genVarDecl() throws ClassNotFoundException {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclaration::inicio");       
        
        Arbitrary<PrimitiveType> pt = mBase.primitiveTypes().map(
                t -> new PrimitiveType(t));
        
        Arbitrary<Type> t = Arbitraries.oneOf(mBase.classOrInterfaceTypes(), pt);
       
        String v = Arbitraries.of(mValidNames).sample();
        
        Type tp = t.sample();
        
        mCtx.put(v, tp.asString());
        
        mValidNames.remove(v);        
                
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclaration::fim"); 
        
        //return t.map(tp -> new VariableDeclarationExpr(tp, v));
        return Arbitraries.just(new VariableDeclarationExpr(tp, v));
    }
    
   @Provide
    public  Arbitrary<Statement> genStatement() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatement::inicio");         
    
        try {
            if(mFuel > 0) {
                mFuel--;
                return Arbitraries.oneOf(genIfStmt(), genWhileStmt(),genVarDeclarationStmt(), genVarDeclStmt());
                
            }else {
                return Arbitraries.oneOf(genVarDeclarationStmt(), genVarDeclStmt());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JRGStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatement::fim");
        
        return  null;
    } 
    
    @Provide
    public Arbitrary<NodeList<Statement>> genStatementList() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatementList::inicio");       
        
        int n = Arbitraries.integers().between(1, MAX_STMT).sample();
        //List<Statement> exs =  new ArrayList<>();
        
        NodeList<Statement> nodes = new NodeList<>();
        for(int i = 0; i < n; i++) {
            nodes.add(genStatement().sample());
        }
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genStatementList::fim");
        
        return Arbitraries.just(nodes);
    }      
    
    @Provide
    public Arbitrary<BlockStmt> genBlockStmt() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genBlockStmt::inicio");       
        
        Arbitrary<NodeList<Statement>> l = genStatementList();
        
        BlockStmt b = new BlockStmt(l.sample());
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genBlockStmt::fim");
        
        return Arbitraries.just(b);
    }

    //ExpressionStmt
    @Provide
    public Arbitrary<VariableDeclarationExpr> genVarDeclAssign() throws ClassNotFoundException {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclarator::inicio");
        Arbitrary<PrimitiveType> pt = mBase.primitiveTypes().map(
                t -> new PrimitiveType(t));
        
        Arbitrary<Type> t = Arbitraries.oneOf(mBase.classOrInterfaceTypes(), pt);
        
        Type tp = t.sample();
        
        Arbitrary<Expression> e = mCore.genExpression(tp);       
       
        String v = Arbitraries.of(mValidNames).sample();
        
        mCtx.put(v, tp.asString());
        
        mValidNames.remove(v);
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genVarDeclarator::fim");
        
        return e.map(obj -> new VariableDeclarationExpr(
                new VariableDeclarator(tp, v, obj)));
    }  
    
     //
    @Provide
    public Arbitrary<VariableDeclarationExpr> genVarAssingStmt() throws ClassNotFoundException{
        String key = Arbitraries.of(mCtx.keySet()).sample();
        
        String value = mCtx.get(key);
        
        Type tp = ReflectParserTranslator
                .reflectToParserType(value);
        
        Arbitrary<Expression> e = mCore.genExpression(tp);
        
        return e.map(obj -> new VariableDeclarationExpr(
                new VariableDeclarator(tp, key, obj)));
    }
    
    @Provide
    public Arbitrary<IfStmt> genIfStmt() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genIfStmt::inicio");       
        
        Arbitrary<Expression> e = mCore.genExpression(PrimitiveType.booleanType());       
       
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genIfStmt::fim"); 
     
        return e.map(exp -> new IfStmt(exp, genBlockStmt().sample(), genBlockStmt().sample()));
    }
    
    @Provide
    public Arbitrary<WhileStmt> genWhileStmt() {
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genWhileStmt::inicio");       
                       
        Arbitrary<Expression> e = mCore.genExpression(PrimitiveType.booleanType());
        
        JRGLog.showMessage(JRGLog.Severity.MSG_XDEBUG, "genWhileStmt::fim"); 
        return e.map(exp -> new WhileStmt(exp,genBlockStmt().sample()));
    }   
    
    @Provide
    public Arbitrary<ExpressionStmt> genExpressionStmt(){
        //@TODO: Sortear o tipo aleatoriamente e passar para genExpression
        Arbitrary<PrimitiveType.Primitive> t = mBase.primitiveTypes();
         
        Arbitrary<Expression> e = mCore.genExpression(ReflectParserTranslator
                .reflectToParserType(t.sample().toString()));
        
        return e.map(exp -> new ExpressionStmt(exp));
    }
    
    @Provide
    public Arbitrary<ExpressionStmt> genVarDeclarationStmt() throws ClassNotFoundException{
         
        Arbitrary<VariableDeclarationExpr> e = genVarDeclAssign();
        
        return e.map(exp -> new ExpressionStmt(exp));
    }
    
    @Provide
    public Arbitrary<ExpressionStmt> genVarDeclStmt() throws ClassNotFoundException{
         
        Arbitrary<VariableDeclarationExpr> e = genVarDecl();
        
        return e.map(exp -> new ExpressionStmt(exp));
    }
    
    //@TODOS
    //Atribuição de váriavel genVarAssignStmt
    
    
    
}
