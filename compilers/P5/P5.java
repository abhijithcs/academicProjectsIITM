import syntaxtree.*;
import visitor.*;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
        // System.out.println("\n***************************************** SUCCESS\n");
         GJNoArguDepthFirst depthfirst = new GJNoArguDepthFirst();
         root.accept(depthfirst); 
         
         	MyVisitor<String> mine = new MyVisitor<String>(depthfirst.registerList, depthfirst.funArgList);
         	root.accept(mine);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



