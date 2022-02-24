import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SSHCommonRead 
{
	public static String ServerIP = "127.0.0.1";
	public static String UserID = "account";
	public static String UserPW = "passwd";

	public static String ServerHost = ServerIP;
	
	public static String log = "";
	private boolean isMatched = false;
	
	/**
	 * @param args[0] Target Server Group ID 
	 * @param args[1] DeployServer.xml 경로
	 * @param args[2] Chain Code
	 * @param args[3] Command  (U : Upload, D : Deploy, L : Log)
	 * 
	 * 
	 * Source IP WAS 특정 경로의 파일 (/WAS/DATA/ES/Deploy/U40/Upload)을 Target IP WAS /WAS/DATA/ES/Deploy/U40/Upload로 복사
	 * 파일 업로드 시 Target IP WAS에 체인코드에 맞게 파일 업데이트
	 *
	 * String[] input = {"test", "C:\\Upload\\", "U40", "U"};
	 * 
	 */
	public boolean SaveAccountCache(String[] input)
	{
		 try
		 {
			 ClassLoader loader = Thread.currentThread().getContextClassLoader();

		      File fileUrl = new File(loader.getResource("").getFile());

		      //파일경로
		     File file = new File(input[1] + "DeployServer.xml");
		     
		     if(!file.exists() && !file.isDirectory())
		     {
		    	 	log = "\nFile Path Error...";
		    	 	return false;
		     }
		     
		     DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		     DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		     Document doc = docBuild.parse(file);
		     doc.getDocumentElement().normalize();

		     NodeList personlist =  doc.getDocumentElement().getChildNodes();
		     
		     
		     for (int i = 0; i < personlist.getLength(); i++) 
		     {
		     	 Node Group = personlist.item(i);
		     	 
		     	 if(Group.getNodeName().equals("Group"))
		     	 {
		     		 if (Group.getNodeType() == Node.ELEMENT_NODE) 
		     		 {
		     			if(input[3].equals("D"))
		     			{
		     				for (int j = 0; j < Group.getChildNodes().getLength(); j++) 
		     				{
		     					if(Group.getChildNodes().item(j).getNodeName().equals("Server"))
		     					{
		     						ServerIP = Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue();
//		     						System.out.println("Group Childs : " + Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue());
   						
		     						for (int k = 0; k < Group.getChildNodes().item(j).getChildNodes().getLength(); k++) 
		     						{
		     							if(Group.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals("User"))
		     							{
		     								UserID = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue();
		     								UserPW = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("pw").getNodeValue();
//		     								System.out.println("User ID : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue());
//		     								System.out.println("User PW : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("pw").getNodeValue());
		     							}
		     							
		     							if(Group.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals("Host"))
		     							{
		     								ServerHost = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue();
//		     								System.out.println("Host ID : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue());
		     							}
		     						}
		     					}
		     					
		     					 if(input[3].equals("D") && input[0].equals(ServerIP))
		     					 {
		     						isMatched = true;
		     						break;
		     					 }
		     				}
		     			}
		     			else
		     			{
			     			 if(Group.getAttributes().getNamedItem("id").getNodeValue().equals(input[0]))
			     			 {
	//		     				 System.out.println("Group att_id : " + Group.getAttributes().getNamedItem("id").getNodeValue());
	//		     				 System.out.println("Group att_name : " + Group.getAttributes().getNamedItem("name").getNodeValue());
			     				 
			     				isMatched = true;
			     				
			     				for (int j = 0; j < Group.getChildNodes().getLength(); j++) 
			     				{
			     					if(Group.getChildNodes().item(j).getNodeName().equals("Server"))
			     					{
			     						ServerIP = Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue();
	//		     						System.out.println("Group Childs : " + Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue());
	   						
			     						for (int k = 0; k < Group.getChildNodes().item(j).getChildNodes().getLength(); k++) 
			     						{
			     							if(Group.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals("User"))
			     							{
			     								UserID = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue();
			     								UserPW = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("pw").getNodeValue();
	//		     								System.out.println("User ID : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue());
	//		     								System.out.println("User PW : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("pw").getNodeValue());
			     							}
			     							
			     							if(Group.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals("Host"))
			     							{
			     								ServerHost = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue();
	//		     								System.out.println("Host ID : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue());
			     							}
			     						}
			     					}
			     				}
			     			 }
		     			 }
		     		 }
		     	 }
		     }
		 }
		 catch(Exception e)
		 {
			 System.out.println("isParsing : " + e.getMessage());
		 }
		 finally
		 {
			 
		 }	
		 
		 return isMatched;
	}
	
	
	/**
	 * @param args[0] Target Server IP
	
	 * IP를 통한 계정 추출
	 * 
	 */
	public boolean SaveAccountCacheCommand(String input)
	{
		 try
		 {
			 ClassLoader loader = Thread.currentThread().getContextClassLoader();

		      File fileUrl = new File(loader.getResource("").getFile());

		      //파일경로
		     File file = new File(SSHCommon.configDirectory + "DeployServer.xml");
		     
		     if(!file.exists() && !file.isDirectory())
		     {
		    	 	log = "\nFile Path Error...";
		    	 	return false;
		     }
		     
		     DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		     DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		     Document doc = docBuild.parse(file);
		     doc.getDocumentElement().normalize();

		     NodeList personlist =  doc.getDocumentElement().getChildNodes();
		     
		     
		     for (int i = 0; i < personlist.getLength(); i++) 
		     {
		     	 Node Group = personlist.item(i);
		     	 
		     	 if(Group.getNodeName().equals("Group"))
		     	 {
		     		 if (Group.getNodeType() == Node.ELEMENT_NODE) 
		     		 {
		     			for (int j = 0; j < Group.getChildNodes().getLength(); j++) 
	     				{
	     					if(Group.getChildNodes().item(j).getNodeName().equals("Server"))
	     					{
	     						if(Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue().equals(input));
	     						{
	     							ServerIP = Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue();
//		     						System.out.println("ServerIP : " + Group.getChildNodes().item(j).getAttributes().getNamedItem("id").getNodeValue());
		     						
	     							for (int k = 0; k < Group.getChildNodes().item(j).getChildNodes().getLength(); k++) 
		     						{
		     							if(Group.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals("User"))
		     							{
		     								UserID = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue();
		     								UserPW = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("pw").getNodeValue();
	//	     								System.out.println("User ID : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue());
	//	     								System.out.println("User PW : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("pw").getNodeValue());
		     							}
		     							
		     							if(Group.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals("Host"))
		     							{
		     								ServerHost = Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue();
	//	     								System.out.println("Host ID : " + Group.getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("id").getNodeValue());
		     							}
		     						}
	     						}
	     					}
	     					
	     					 if(input.equals(ServerIP))
	     					 {
	     						isMatched = true;
	     						break;
	     					 }
	     				}
		     		 }
		     	 }
		     }
		 }
		 catch(Exception e)
		 {
			 System.out.println("isParsing : " + e.getMessage());
		 }
		 finally
		 {
			 
		 }	
		 
		 return isMatched;
	}

}
