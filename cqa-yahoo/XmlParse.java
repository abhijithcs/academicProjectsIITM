import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParse {

	XmlParse()
	{
		
	}
	public String GetQuestion(File file) throws Exception
	{
		 String question="";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		 
		  NodeList nodeLst = doc.getElementsByTagName("Question");
	int s=0;
		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		  
		           Element fstElmnt = (Element) fstNode;
		      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("Subject");
		      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      NodeList fstNm = fstNmElmnt.getChildNodes();
		   //   System.out.println("Question4 : "  + ((Node) fstNm.item(0)).getNodeValue());
		      question= ((Node) fstNm.item(0)).getNodeValue().toString();
		    
		    
		      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("ChosenAnswer");
		      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		      NodeList lstNm = lstNmElmnt.getChildNodes();
		      if((lstNm.item(0))!=null)
		      {
		    	String answer= (lstNm.item(0)).getNodeValue().toString();
		    
		      }
		    }
		    return question;
	}
	public String GetAnswer(File file) throws Exception
	{
		String answer="";
		String question="";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		 
		  NodeList nodeLst = doc.getElementsByTagName("Question");
	int s=0;
		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		  
		           Element fstElmnt = (Element) fstNode;
		      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("Subject");
		      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      NodeList fstNm = fstNmElmnt.getChildNodes();
		     // System.out.println("Question4 : "  + ((Node) fstNm.item(0)).getNodeValue());
		      question= ((Node) fstNm.item(0)).getNodeValue().toString();
		    
		    
		      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("ChosenAnswer");
		      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		      NodeList lstNm = lstNmElmnt.getChildNodes();
		      if((lstNm.item(0))!=null)
		      {
		    	 answer= (lstNm.item(0)).getNodeValue().toString();
		    
		      }
		    }
		    return answer;
	}
}
