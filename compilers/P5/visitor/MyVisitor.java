// Copy of GJNoArguDepthFirst

package visitor;
import syntaxtree.*;
import java.util.*;

public class MyVisitor<R> implements GJNoArguVisitor<R> {

   List<RegisterData> registerList=new ArrayList<RegisterData>();
   int listSize;
   int []funArgList;
   public MyVisitor(List<RegisterData> registerList, int[] funArgList){
	   this.registerList=registerList;
	   listSize=registerList.size();
	   this.funArgList=funArgList;
   }
	
   int tempIndexFlag=0;
   int callTempFlag=0;
   int aTypeIndex=0;
   int sTypeIndex=0;
   int callCheckFlag=0;
   int isCall=0;
   int maxArgs=0;
   int funIndex=0;
   int spillCheckFlag=0;
   int isSpillable=0;
   
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
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      n.f0.accept(this);
      
      System.out.print("\nMAIN [0][20]["+funArgList[funIndex]+"]\n");
      funIndex++;
      n.f1.accept(this);
      n.f2.accept(this);
      System.out.print("\nEND\n");
      n.f3.accept(this);
      n.f4.accept(this);

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
    * f4 -> StmtExp()
    */
   public R visit(Procedure n) {
      R _ret=null;
      int argCount;
      argCount=Integer.parseInt(n.f2.f0.toString());
      n.f0.accept(this);
     // n.f1.accept(this);
     // n.f2.accept(this);
     // n.f3.accept(this);
      System.out.print("["+argCount+"][20]["+funArgList[funIndex]+"]\n");
      funIndex++;
      for(int i=0;i<8;i++)
      {
    	  System.out.print("\n\tASTORE SPILLEDARG "+(i+10)+" s"+i);
      }
      System.out.print("\n");
      for(int i=0;i<4;i++)
      {
    	  System.out.print("\n\tMOVE s"+i+" a"+i);
      }
      System.out.print("\n");
      sTypeIndex=argCount; //************************************CHECK THIS
      n.f4.accept(this);
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
    */
   public R visit(Stmt n) {
      R _ret=null;
      System.out.print("\t");
      n.f0.accept(this);
      System.out.print("\n");
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("NOOP");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print(" ERROR ");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("CJUMP");
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("JUMP");
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("HSTORE");
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print("HLOAD");
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      
      callCheckFlag=1;	//Checking whether Exp() is CALL
      	n.f2.accept(this);
      callCheckFlag=0;
      
      if(isCall==1)		//Exp() is CALL
      {
	      n.f2.accept(this);
	      System.out.print("\n\tMOVE");
	      n.f1.accept(this);
	      System.out.print(" v0");
      }
      
      if(isCall==0)		//Exp() is "not" CALL
      {	      
	      System.out.print("MOVE");
	      n.f1.accept(this);
	      n.f2.accept(this);
      }
      
      isCall=0;			//Reset isCall
      
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      
      spillCheckFlag=1;
      n.f1.accept(this);
      spillCheckFlag=0;
      
      if(isSpillable==0)
      {
	      System.out.print("PRINT");
	      n.f1.accept(this);
      }
      if(isSpillable==1)
      {
    	  n.f1.accept(this);
	      System.out.print("\n\tPRINT v1");
	  }
      isSpillable=0;
      return _ret;
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   public R visit(StmtExp n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      System.out.print("\tMOVE v0");
      n.f3.accept(this);
      for(int i=0;i<8;i++)
      {
    	  System.out.print("\n\tALOAD s"+i+" SPILLEDARG "+(i+10));
      }
      System.out.print("\nEND");
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public R visit(Call n) {
      R _ret=null;
      
      if(callCheckFlag==1)
      {
    	  isCall=1;
    	  return _ret;
      }
    	  
      callTempFlag=1;
      	n.f3.accept(this);
      callTempFlag=0;
      aTypeIndex=0;
      
      System.out.print("\n\tCALL");
      n.f1.accept(this);
      n.f2.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      
      if(callCheckFlag==1)	//Do nothing if performing a CALL Check
    	  return _ret;
      
      n.f0.accept(this);
      System.out.print(" HALLOCATE");
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n) {
      R _ret=null;
      
      if(callCheckFlag==1)	//Do nothing if performing a CALL Check
    	  return _ret;
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    */
   public R visit(Operator n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.print(" "+n.f0.choice.toString());
      return _ret;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      
      if(callCheckFlag==1)	//Do nothing if performing a CALL Check
    	  return _ret;
            
      if(spillCheckFlag==1)
      {
    	  n.f0.accept(this);
    	  return _ret;
      }
    	  
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n) {
      R _ret=null;
      int temp_index,register_index=18;
      
      temp_index=Integer.parseInt(n.f1.f0.toString());
      RegisterData rd = new RegisterData();
      									
      if(spillCheckFlag==1)
      {
    	  if(temp_index>=4 && temp_index<=19)
    	  {
    		  isSpillable=1;
    	  }
    	  return _ret;
      }
      
      if(temp_index>=4 && temp_index<=19)
      {
    	  System.out.print("\n\tALOAD v1 SPILLEDARG "+(temp_index-4));
    	  
    	  if(callTempFlag==1)
    	  {
    		  System.out.println("\n\tMOVE a"+aTypeIndex+" v1");
    	  	  aTypeIndex++;
    	  }
    	  return _ret;
      }
      
      if(temp_index>=0 && temp_index<=19 && callTempFlag==0)		//Checking if Arguments (TEMP 0 -> TEMP 19)
      {
    	/*  if(sTypeIndex==4)
    		  sTypeIndex=0;
    	  
    	  System.out.print(" s"+sTypeIndex);
    	  sTypeIndex++;*/
    	  
    	   	  System.out.print(" s"+temp_index);
    	  
    	  return _ret;    	  
      }
      
      for(int i=0;i<listSize;i++)
      {
    	  rd=registerList.get(i);
    	  if(rd.getIndex()==temp_index && callTempFlag==0)
    	  {
    		  register_index=rd.getRegister();
    		  break;
    	  }
    	  
    	  if(temp_index>=0 && temp_index<=19 && callTempFlag==1)
    	  {
    		  if(aTypeIndex>=4)
    			  System.out.print("\n\tPASSARG "+(aTypeIndex-3)+" s"+temp_index);
    		  else
    			  System.out.print("\n\tMOVE a"+aTypeIndex+" s"+temp_index);
    		  
			  aTypeIndex++;
			  break;
    	  }
    	  
    	  if(rd.getIndex()==temp_index && callTempFlag==1)
    	  {
    		  if(aTypeIndex<4)
    		  {
    			  register_index=rd.getRegister();
    			  System.out.print("\n\tMOVE a"+aTypeIndex+" t"+register_index);
    			  aTypeIndex++;
    			  break;
    			  
    		  }
    		  else if(aTypeIndex>=4)
    		  {
    			  register_index=rd.getRegister();
	    		  System.out.print("\n\tPASSARG "+(aTypeIndex-3)+" t"+register_index);
	    		  aTypeIndex++;
	    		  break;
    		  }    		  		  
    	  }
      }
      
      if(callTempFlag==0)
      {
    	  n.f0.accept(this);
    	  tempIndexFlag=1;
    	  n.f1.accept(this);
    	  tempIndexFlag=0;
    	  if(register_index<=9)
    		  System.out.print(" t"+register_index); //****************************** Chang "t" to "s" for higher values
    	  else if(register_index>9)
    		  System.out.print(" s"+(register_index-6));
      }
      
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      
      if(spillCheckFlag==1)   	//Do nothing. Checking if Spillable
    	  return _ret;
      
      
      if(tempIndexFlag==0)
    	  System.out.print(" "+n.f0.toString());
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      n.f0.accept(this);
      
      if(spillCheckFlag==1)   	//Do nothing. Checking if Spillable
    	  return _ret;
      
      System.out.print(" "+n.f0.toString());
      return _ret;
   }

}
