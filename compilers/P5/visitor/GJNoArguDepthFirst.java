package visitor;
import syntaxtree.*;
import java.util.*;

class RegisterData 
{
	int temp_index;
	int start, end;
	int flag;
	int register;  //CHANGE------> to String
	
	public void setIndex(int index)
	{
		temp_index=index;
		flag=0;
	}
	
	public void setStart(int start_value)
	{
		start=start_value;
	}
	
	public void setEnd(int end_value)
	{
		end=end_value;
	}
	
	public void setFlag()
	{
		flag=1;
	}
	
	public void setRegister(int value) //CHANGE------> to String
	{
		register=value;
	}
	
	public int getIndex()
	{
		return temp_index;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getEnd()
	{
		return end;
	}
	
	public int getFlag()
	{
		return flag;
	}
	
	public int getRegister()  //CHANGE------> to String
	{
		return register;
	}
}

class stack
{
	private int[] array;
	private int top;
	public stack()
	{
		array = new int[18];
		top=17;
		for(int i=0;i<18;i++)
		{
			array[i]=17-i;
		}
	}
	
	public int getRegister()
	{ 
		return array[top--];
	}
	public void freeRegister(int reg)
	{
		int temp,max,swap,swapj;
		array[++top]=reg;
	}
}

class endSort implements Comparator<RegisterData>{
    
    @Override
    public int compare(RegisterData o1, RegisterData o2) {
        return (o1.end<o2.end ? -1 : (o1.end==o2.end ? 0 : 1));
    }
}

class startSort implements Comparator<RegisterData>{
    
    @Override
    public int compare(RegisterData o1, RegisterData o2) {
        return (o1.start<o2.start ? -1 : (o1.start==o2.start ? 0 : 1));
    }
}

class indexSort implements Comparator<RegisterData>{
    
    @Override
    public int compare(RegisterData o1, RegisterData o2) {
        return (o1.temp_index<o2.temp_index ? -1 : (o1.temp_index==o2.temp_index ? 0 : 1));
    }
}

public class GJNoArguDepthFirst<R> implements GJNoArguVisitor<R>{

	//RegisterData[] rd = new RegisterData[200];
	public List<RegisterData> registerList=new ArrayList<RegisterData>();
	List<RegisterData> activeList=new ArrayList<RegisterData>();
	List<String> freeList=new ArrayList<String>();
	List<RegisterData> stack=new ArrayList<RegisterData>(); //-------->> SAMPLE
	stack freeRegisters=new stack();

	public int[] funArgList= new int[50];
	
    int funCount=0;
    int maxArg=0;
	
	int lineIndex=0;
	int arrayIndex=0;
	int end_update_flag=0;

	int argCountFlag=0;
	int argCount=0;
	
	
	
	//Variables for Linear Scan Algorithm
	int activeSize=0;
	int freeSize=18;
	
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
      n.f1.accept(this);
      
      funArgList[funCount]=maxArg;
      //System.out.print("\n**MAIN : "+maxArg);
      funCount++;
      maxArg=0;
      argCount=0;
      
      
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      
      Collections.sort(registerList, new startSort());
    
      //Removing Duplicates
      
      int listSize=arrayIndex;
      int s1,s2,e1,e2,min,max;
      
      for(int i=0;i<listSize;i++)
      {
    	  RegisterData r1= new RegisterData();
    	  r1=registerList.get(i);
    	  s1=r1.getStart();
    	  e1=r1.getEnd();
    	  
    	  for(int j=0;j<listSize;j++)
          {
    		  RegisterData r2= new RegisterData();
    		  r2=registerList.get(j);
    		  if(r1.getIndex()==r2.getIndex())
    		  {
    			  s2=r2.getStart();
    			  e2=r2.getEnd();
    			  
    			  if(s1<s2)
    			  	  r2.setStart(s1);
    			  else if(s1>s2)
    				  r1.setStart(s2);
    			  
    			  if(e1>e2)
    				  r2.setEnd(e1);
    			  else if(e1<e2)
    				  r1.setEnd(e2);
    		  }
          }    	  
      }
      
      int fakeIndex=2000;
      int duplicateCount=0;
      
      for(int i=0;i<listSize;i++)
      {
    	  RegisterData r1= new RegisterData();
    	  r1=registerList.get(i);
    	  
    	  for(int j=0;j<listSize;j++)
          {
    		  RegisterData r2= new RegisterData();
    		  r2=registerList.get(j);
    		  if(r1.getIndex()==r2.getIndex() && i!=j)
    		  {
    			  r2.setIndex(fakeIndex);
    			  duplicateCount++;
    			  fakeIndex++;
    		  }
          }    	  
      }
      
      Collections.sort(registerList, new indexSort());
      RegisterData rd= new RegisterData();
      int limit=listSize-1;
      for(int i=0;i<duplicateCount;i++)
      {
    	  registerList.remove(limit);
    	  limit--;
      }
      //Update listSize
      listSize=limit+1; 
      
     
 
 
      //PRINTING ---------> Remove this.   
     /* 
      RegisterData rd1=new RegisterData();
         
      int t,s,e;
      for(int i=0; i<listSize; i++)
      {
    	  rd1=registerList.get(i);
    	  t=rd1.getIndex();
    	  s=rd1.getStart();
    	  e=rd1.getEnd();
    	  System.out.println(t+" "+s+" "+e);      
      }
      */
      
      //LINEAR SCAN ALGORITHM
      RegisterData rd2=new RegisterData();
      Collections.sort(registerList, new startSort());
      
      for(int i=0;i<listSize;i++)
      {
    	  rd2=registerList.get(i);
    	  expireOldIntervals(i);
    	  if(activeSize==18)
    	  {
    		  spillAtInterval(i);
    	  }
    	  else
    	  {
	    	  rd2.setRegister(freeRegisters.getRegister());
	    	  activeList.add(rd2);
	    	  activeSize++;
	    	  Collections.sort(activeList, new endSort());
	      }
      }
   
      //TRIAL PRINT ---------------------------------------------->> Remove this.
     /* System.out.println("-----------------------------------------------------\n"); 
      RegisterData rd1=new RegisterData();
      int t,s,e;
      for(int i=0; i<listSize; i++)
      {
    	  rd1=registerList.get(i);
    	  t=rd1.getIndex();
    	  s=rd1.getStart();
    	  e=rd1.getEnd();
    	  System.out.println(t+" "+s+" "+e+" "+rd1.getRegister());
      }
    System.out.println("-----------------------------------------------------\n");
      */
      return _ret;
   }
   
   public void expireOldIntervals(int i)
   {
	   RegisterData rd2=new RegisterData();
	   RegisterData rd3=new RegisterData();
	  
	   Collections.sort(activeList, new endSort());
	   rd2=registerList.get(i);
	   
	   for(int j=0;j<activeSize;j++)
	   {
		   rd3=activeList.get(j);
		   if(rd3.getEnd()>=rd2.getStart())
		   {
			   return ;
		   }
		  
		   freeRegisters.freeRegister(rd3.getRegister());
		   activeList.remove(rd3);
		   activeSize--;
	   }
	   
   }
   
   public void spillAtInterval(int in)
   {
	   RegisterData spill=new RegisterData();
	   spill=activeList.get(activeSize);
	   
	   RegisterData i=new RegisterData();
	   i=registerList.get(in);
	   
	   if(spill.getEnd()>i.getEnd())
	   {
		   i.setRegister(spill.getRegister());
		   stack.add(spill);
		   activeList.remove(spill);
		   activeList.add(i);
		   Collections.sort(activeList, new endSort());
	   }
	   else
	   {
		   stack.add(i);
	   }
	   
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
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      funArgList[funCount]=maxArg;
      //System.out.print("\n**"+n.f0.f0.toString()+" : "+maxArg);
      funCount++;
      maxArg=0;
      argCount=0;
      
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
      lineIndex++;
      //System.out.println("Line: "+lineIndex); 
      n.f0.accept(this);      
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
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
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      end_update_flag=1;
      n.f1.accept(this);
      end_update_flag=0;
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
      end_update_flag=1;
	      n.f1.accept(this);
	      n.f2.accept(this);
	      n.f3.accept(this);
      end_update_flag=0;
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
      n.f1.accept(this);
	      RegisterData rd=new RegisterData();
	      rd.setIndex(Integer.parseInt(n.f1.f1.f0.toString()));
	      rd.setStart(lineIndex);
	      rd.setEnd(lineIndex);
	      registerList.add(arrayIndex,rd);
	      
	      
	      RegisterData rd1=new RegisterData();
	      for(int i=0;i<arrayIndex;i++)
	      {
	    	  rd1=registerList.get(i);
	    	  if(rd1.getIndex()==Integer.parseInt(n.f1.f1.f0.toString()))
	    	  {
	    		  rd1.setFlag();
	    	  }
	      }
	      
	      
	      arrayIndex++;
	      end_update_flag=1;
      n.f2.accept(this);
      	  end_update_flag=0;
      n.f3.accept(this);
      
      //RegisterData rdn=new RegisterData();
      //rdn=registerList.get(arrayIndex-1);
      //System.out.println("Index: "+rdn.getIndex()+" Start: "+rdn.getStart()+" End: "+rdn.getEnd());
                
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
	      RegisterData rd=new RegisterData();
	      rd.setIndex(Integer.parseInt(n.f1.f1.f0.toString()));
	      rd.setStart(lineIndex);
	      rd.setEnd(lineIndex);
	      registerList.add(arrayIndex,rd);
	     
	      
	      RegisterData rd1=new RegisterData();
	      for(int i=0;i<arrayIndex;i++)
	      {
	    	  rd1=registerList.get(i);
	    	  if(rd1.getIndex()==Integer.parseInt(n.f1.f1.f0.toString()))
	    	  {
	    		  rd1.setFlag();
	    	  }
	      }
	      	      
	      
	      arrayIndex++;	 
	      end_update_flag=1;
      n.f2.accept(this);
      	  end_update_flag=0;
     
      //RegisterData rdn=new RegisterData();
      //rdn=registerList.get(arrayIndex-1);
      //System.out.println("Index: "+rdn.getIndex()+" Start: "+rdn.getStart()+" End: "+rdn.getEnd());
      
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      n.f0.accept(this);
      end_update_flag=1;
      n.f1.accept(this);
      end_update_flag=0;
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
      end_update_flag=1;
      	lineIndex++;
      	n.f3.accept(this);      
      end_update_flag=0;
      n.f4.accept(this);
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
      
      argCountFlag=1;
    	  n.f3.accept(this);
    	  if(maxArg<argCount)
    		  maxArg=argCount;
    	  argCount=0;
      argCountFlag=0;
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);     
      n.f3.accept(this);    
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      n.f0.accept(this);
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
      return _ret;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n) {
      R _ret=null;
      
      
      if(argCountFlag==1)
      {
    	  argCount++;
    	  return _ret;
      }
      
      
      int index;
      
      n.f0.accept(this);
      n.f1.accept(this);
      
      
      index=Integer.parseInt(n.f1.f0.toString());
      if(end_update_flag==1)
      {
	      RegisterData rd=new RegisterData();
	      for(int i=0; i<arrayIndex; i++)
	      {
	    	  rd=registerList.get(i);
	    	  if(rd.getIndex()==index && rd.getFlag()==0)
	    	  {
	    		  int k=rd.getEnd();
	    		  rd.setEnd(lineIndex);
	    		  //System.out.println("*******end update TEMP "+rd.getIndex()+" from "+k+" to "+lineIndex);
	    		  break;
	    	  }
	    		  
	      }
      }
      
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

}
