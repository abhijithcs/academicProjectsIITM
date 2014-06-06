import syntaxtree.*;
import visitor.*;

public class P4 {
   public static void main(String [] args) {
      try {
	Node root = new MiniIRParser(System.in).Goal();
	//System.out.println("Program parsed successfully");
	root.accept(new GJNoArguDepthFirst());
	}
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



