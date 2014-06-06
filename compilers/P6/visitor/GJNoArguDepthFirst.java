package visitor;
import syntaxtree.*;
import java.util.*;

public class GJNoArguDepthFirst<R> implements GJNoArguVisitor<R> {

	//Flags
	int hallocateFlag=0;
	int hLoadFlag=0;
	int hstoreFlag=0;
	int spilledArgFlag=0;
	int operatorFlag=0;
	int labelFlag=1;
	int moveType=0;
	//0-Temp		//2-Label		//4-Binary Operator
	//1-Integer		//3-Hallocate	
	int moveCheck=0;
	int hAllocateCheck=0;
	int hAllocateType=0; 
	//0-Int		//1-Reg		//2-Label
	int binaryVisit=0;
	
	int subu_value=0;
	int fp_value=0;
	
	
	public R visit(NodeList n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return null;
   }

   public R visit(NodeSequence n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( Procedure() )*
    * f13 -> VariablePackingInfo()
    * f14 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      
      //Find subu_value
      int args1, args2,args3;
      args1=Integer.parseInt(n.f2.f0.toString());
      args2=Integer.parseInt(n.f5.f0.toString());
      args3=Integer.parseInt(n.f8.f0.toString());
      
      if(args3>4)
       	  subu_value = 4*((args3-4)+args2+1);
      else
    	  subu_value = 4*(args2+1);
         
      System.out.print("\n\t.text\n\t.globl\t\tmain");      
      n.f0.accept(this);
      System.out.print("\nmain:");
      
      System.out.print("\n\tmove $fp, $sp");
      System.out.print("\n\tsubu $sp, $sp, "+subu_value);
      System.out.print("\n\tsw $ra, -4($fp)\n");
            
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      
      System.out.print("\n\tlw $ra, -4($fp)");
      System.out.print("\n\taddu $sp, $sp, "+subu_value);
      System.out.print("\n\tj $ra\n");
     
      n.f12.accept(this);
      
      System.out.print("\n\t.text");
      System.out.print("\n\t.globl _halloc");
      System.out.print("\n_halloc:");
      System.out.print("\n\tli $v0, 9");
      System.out.print("\n\tsyscall");
      System.out.print("\n\tj $ra");
      System.out.print("\n");
      System.out.print("\n\t.text");
      System.out.print("\n\t.globl _print");
      System.out.print("\n_print:");
      System.out.print("\n\tli $v0, 1");
      System.out.print("\n\tsyscall");
      System.out.print("\n\tla $a0, newl");
      System.out.print("\n\tli $v0, 4");
      System.out.print("\n\tsyscall");
      System.out.print("\n\tj $ra");
      System.out.print("\n");
      System.out.print("\n\t.data");
      System.out.print("\n\t.align   0");
      System.out.print("\n\tnewl:    .asciiz \"\\n\"");
      System.out.print("\n\t.data");
      System.out.print("\n\t.align   0");
      System.out.print("\n\tstr_er:  .asciiz \" ERROR: abnormal termination\\n\"");
      
      n.f13.accept(this);
      n.f14.accept(this);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    */
   public R visit(Procedure n) {
      R _ret=null;
      
    //Find subu_value
      int args1, args2,args3;
      args1=Integer.parseInt(n.f2.f0.toString());
      args2=Integer.parseInt(n.f5.f0.toString());
      args3=Integer.parseInt(n.f8.f0.toString());
      
      if(args3>4)
       	  subu_value = 4*((args3-4)+args2+2);
      else
    	  subu_value = 4*(args2+2);

      fp_value=4*(args2+2)-8;
      
      System.out.print("\n\t.text\n\t.globl\t\t"+n.f0.f0.toString());      
      System.out.print("\n"+n.f0.f0.toString()+":\n");
      
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
                  
      System.out.print("\n\tsw $fp, -8($sp)");
      System.out.print("\n\tmove $fp, $sp");
      System.out.print("\n\tsubu $sp, $sp, "+subu_value);
      System.out.print("\n\tsw $ra, -4($fp)\n");  
      
      n.f10.accept(this);

      System.out.print("\n\tlw $ra, -4($fp)");
      System.out.print("\n\tlw $fp, "+fp_value+"($sp)");
      System.out.print("\n\taddu $sp, $sp, "+subu_value);
      System.out.print("\n\tj $ra\n");
      
      n.f11.accept(this);
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
    */
   
   public R visit(Stmt n) {
      R _ret=null;
	  labelFlag=0;
	      System.out.print("\t");
	      n.f0.accept(this);
	      System.out.print("\n");
      labelFlag=1;
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      System.out.print("nop");
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      System.out.print("beqz ");
      n.f0.accept(this);
      n.f1.accept(this);
      System.out.print(" ");
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      System.out.print("b ");
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("sw ");
      n.f3.accept(this);
      System.out.print(", ");
      hstoreFlag=1;
      n.f2.accept(this);
      hstoreFlag=0;
      System.out.print("(");
      n.f1.accept(this);
      System.out.print(")");
      
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("lw ");
      n.f1.accept(this);
      System.out.print(" ");
      hLoadFlag=1;
      	n.f3.accept(this);
      hLoadFlag=0;
      System.out.print("(");
      n.f2.accept(this);
      System.out.print(")");
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      
      moveCheck=1;
      n.f2.accept(this);
      moveCheck=0;
      
//moveType
//0-Temp		//2-Label		//4-Binary Operator
//1-Integer		//3-Hallocate
      
      if(moveType==0)
      {
    	  System.out.print("move ");
    	  n.f1.accept(this);
    	  System.out.print(" ");
    	  n.f2.accept(this);
      }
      
      if(moveType==1)
      {
    	  System.out.print("li ");
    	  n.f1.accept(this);
    	  System.out.print(" ");
    	  n.f2.accept(this);
      }
      
      if(moveType==2)
      {
    	  System.out.print("la ");
    	  n.f1.accept(this);
    	  System.out.print(" ");
    	  n.f2.accept(this);
      }
      
      if(moveType==3)
      {
    	  n.f2.accept(this);
    	  System.out.print("\n\tjal _halloc");
    	  System.out.print("\n\tmove ");
    	  n.f1.accept(this);
    	  System.out.print(" $v0");
    	  
      }
      
      if(moveType==4)
      {
    	  binaryVisit=1;
    	  	n.f2.accept(this);
    	  	n.f1.accept(this);
    	  	System.out.print(", ");
    	  binaryVisit=2;
    	  	n.f2.accept(this);
    	  binaryVisit=0;    	     	     	  
      }
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      System.out.print("move $a0 ");
      n.f1.accept(this);
      System.out.print("\n\tjal _print");
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
   public R visit(ALoadStmt n) {
      R _ret=null;
      System.out.print("lw ");
      n.f1.accept(this);
      System.out.print(", ");
      n.f2.accept(this);
      System.out.print("($sp)");
      return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   public R visit(AStoreStmt n) {
      R _ret=null;
      System.out.print("sw ");
      n.f2.accept(this);
      System.out.print(", ");
      n.f1.accept(this);
      System.out.print("($sp)");
      
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
   public R visit(PassArgStmt n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   public R visit(CallStmt n) {
      R _ret=null;
      System.out.print("jalr ");
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      
      if(moveCheck==1)
      {
    	  moveType=3; //3-Hallocate
    	  return _ret;    	  
      }
      
      
      hAllocateCheck=1;
      n.f1.accept(this);
      hAllocateCheck=0;
      //0-Int	//1-Reg		//2-Label     
      
      if(hAllocateType==0)
      {
	      System.out.print("li $a0 ");
	      
	      hallocateFlag=1;
	      n.f1.accept(this);
	      hallocateFlag=0;
	      return _ret;
      }
	        
      if(hAllocateType==1)
      {
	      System.out.print("move $a0 ");
	      n.f1.accept(this);
	      return _ret;
      }
      
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n) {
      R _ret=null;

      if(moveCheck==1)
      {
    	  moveType=4; //4-Binary Op.
    	  return _ret;    	  
      }
      
      if(binaryVisit==1)
      {
	      if(n.f0.f0.choice.toString()=="TIMES")
	      	  System.out.print("mul ");
	      if(n.f0.f0.choice.toString()=="MINUS")
	      	  System.out.print("sub ");
	      if(n.f0.f0.choice.toString()=="LT")
	      	  System.out.print("slt ");
	      if(n.f0.f0.choice.toString()=="PLUS")
	      	  System.out.print("add ");
	      
	      return _ret;
      }

      if(binaryVisit==2)
      {
    	  operatorFlag=1;
    	  n.f1.accept(this);
    	  System.out.print(", ");
          n.f2.accept(this);
          operatorFlag=0;
          
          return _ret;
          	
      }
            
      return _ret;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "BITOR"
    *       | "BITAND"
    *       | "LSHIFT"
    *       | "RSHIFT"
    *       | "BITXOR"
    */
   public R visit(Operator n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n) {
      R _ret=null;
      n.f0.accept(this);
      spilledArgFlag=1;
        	n.f1.accept(this);
      spilledArgFlag=0;
      return _ret;
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n) {
      R _ret=null;

      if(hAllocateCheck==1)
      {
    	  hAllocateType=1;
    	  return _ret;
      }
      
      
      if(moveCheck==1)
      {
    	  moveType=0; //0-Register
    	  return _ret;    	  
      }
      
      System.out.print("$"+n.f0.choice.toString());
      
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      
      if(spilledArgFlag==1)
      {
    	  System.out.print(4*(Integer.parseInt(n.f0.toString())));
    	  return _ret;
      }

      if(hAllocateCheck==1)
      {
    	  hAllocateType=0;
    	  return _ret;
      }
      
      if(moveCheck==1)
      {
    	  moveType=1; //1-Integer
    	  return _ret;    	  
      }
      
      
      if(hallocateFlag==1 || moveType==1 || hLoadFlag==1 || hstoreFlag==1 || operatorFlag==1)
      {
    	  System.out.print(n.f0.toString());
    	  return _ret;
      }
      
      
      
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;


      if(hAllocateCheck==1)
      {
    	  hAllocateType=2;
    	  return _ret;
      }
      
      if(labelFlag==1)
      {
    	  System.out.print(n.f0.toString()+":");
    	  return _ret;
    	  
      }
      
      
      if(moveCheck==1)
      {
    	  moveType=2; //2-Label
    	  return _ret;    	  
      }
      
      System.out.print(n.f0.toString());   //------->> "Solve \n issue."
      
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "// Number of  vars after packing ="
    * f1 -> IntegerLiteral()
    * f2 -> "; Number of Spilled vars ="
    * f3 -> IntegerLiteral()
    */
   public R visit(VariablePackingInfo n) {
      R _ret=null;
      int n1, n2;
      n1=Integer.parseInt(n.f1.f0.toString());
      n2=Integer.parseInt(n.f3.f0.toString());
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      
      return _ret;
   }

}
