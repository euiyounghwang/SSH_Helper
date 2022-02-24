import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import SSH.ShellFTP.SSHShellClient;


public class SSHDeploy {
	
	/**
	 * @param args[0] Deploy Target Server 
	 * @param args[1] DeployServer.xml 경로
	 * @param args[2] Chain Code
	 * @param args[3] Command  (U : Upload, D : Deploy, L : Log)
	 * @param args[4] 백업유무(기존의 ear 파일등의 배포용 파일들)
	 * 
	 * String[] deploy = {"127.0.0.1", SSHCommon.configDirectory, "U40", "D", "T"};
	 * 
	 */
	
	public String run(String[] input)
	{
	   
	    String monitor = null, srcDirectory = null, srcBackup = null, destDirectory = null;
	    
	    StringBuffer results = new StringBuffer();
	   
	    if(SSHCommon.isServerUpload)
	    	srcDirectory = "/WAS/DATA/ES/Deploy/" + input[2] + "/Upload/";
	    else
	    	srcDirectory = input[1];
	    
	    if(SSHCommon.isServerBackup)
	    	srcBackup = "/WAS/DATA/ES/Deploy/" + input[2] + "/Backup/";
	    else
	    	srcBackup = "C:\\Backup\\";
	    
		destDirectory = "/WAS/DATA/ES/Deploy/" + input[2] + "/Upload/";
		
		
		File[] list = null;
		boolean[] ack_upload = null, real_upload = null;
		
		SSHShellClient SSHClient = null;
		
		try 
		{
			// 특정 서버 IP의 서버
			boolean isCache = new SSHCommonRead().SaveAccountCache(input);
			
			if(isCache)
			{
				System.out.println("#############################################");
				System.out.println("Server IP : " + new SSHCommonRead().ServerIP);
				System.out.println("UserID : " + new SSHCommonRead().UserID);
				System.out.println("UserPW : " + new SSHCommonRead().UserPW);
				System.out.println("ServerHost : " + new SSHCommonRead().ServerHost);
				System.out.println("\n\n");
				
				try
				{
					/**
				    * ########################################################################## 
					* @param 실제 Deploy 경로 설정
					* @param /WAS/APP/SCM/TFESA01/U40
					* ##########################################################################
					*/
				    SSHCommon.commonEarPath = "/WAS/APP/SCM/" + new SSHCommonRead().ServerHost + "/" + input[2] + "/";
				    SSHCommon.commonGluePath = "/WAS/APP/SCM/" + new SSHCommonRead().ServerHost + "/" + input[2] + "/WEB/properties/";
				    SSHCommon.commonCssPath = "/WAS/APP/SCM/" + new SSHCommonRead().ServerHost + "/" + input[2] + "/WEB/css/";
				    SSHCommon.commonImgPath = "/WAS/APP/SCM/" + new SSHCommonRead().ServerHost + "/" + input[2] + "/WEB/img/";
				    SSHCommon.commonJsPath = "/WAS/APP/SCM/" + new SSHCommonRead().ServerHost + "/" + input[2] + "/WEB/js/";
				    
				    
					/**
					 * @param args[1] WAS Target IP Address
					 * @param args[2] WAS ID
					 * @param args[3] WAS Password
					 * 
					 * 목적지 서버에 파일 업로드 1단계 
					 */
					 SSHClient = new SSHShellClient(new SSHCommonRead().ServerIP, 
							 						new SSHCommonRead().UserID,
							 						new SSHCommonRead().UserPW
							 						);
					
					 File f = new File(srcDirectory);
		 
					 if(!f.exists() || !f.isDirectory()){
					    System.out.println("올바르지 않은 디렉토리 입니다.");
					 }
		
					 list = f.listFiles();
				
					 real_upload = new boolean[list.length];
					 
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
					 Date date = new Date();
					 
					 
					 /**
					  * ##########################################################################
					 * @param 접속된 서버 및 원격 서버(Target 서버) Backup 파일 디렉토리 초기화
					 * 
					 *  SSHCommon.deletefiles(srcBackup); Source 서버내의 /Backup/폴더 Clear
					 * 
					 * ###########################################################################
					 */
					
					 // 로컬 경로내 기존 백업 파일 삭제
					 SSHCommon.deletefiles(srcBackup);
					
					 // 원격 파일 삭제
					 String str = SSHClient.exec("rm /WAS/DATA/ES/Deploy/" + input[2] + "/Backup/* \n");
					 System.out.println("rm Backup : " + str);
					 
					 /**
					  * ##########################################################################
					 */
					 
					 for(int i=0; i < list.length;i++)
					 {
						 // 실제 파일명에 맞는 경로로 업로드하기
						 if(list[i].getName().contains(input[2]))
						 {
							 boolean isEarOk = false;
							 
							 if(input[4].equals("T"))
							 {
								 SSHClient.GET(SSHCommon.commonEarPath,	srcBackup, list[i].getName(),sdf.format(date) + "_" + list[i].getName());
							 
								 /**
								  * ##########################################################################
								 * @param 원격 서버에 Backup 파일 업로드
								 * ###########################################################################
								 */
								 isEarOk = SSHClient.PUT(srcBackup, srcBackup, sdf.format(date) + "_" + list[i].getName());

							 }

							 if(isEarOk) 
							 {
								 /**
								  * ##########################################################################
								 * @param 실제 지정된 위치내 ear 파일 업로드/업데이트
								 * ###########################################################################
								 */
								 
								real_upload[i] = SSHClient.PUT(srcDirectory, SSHCommon.commonEarPath, list[i].getName());
								System.out.println(input[2] + " Upload : " + real_upload[i]);
							 }
						 }
						 
						 if(list[i].getName().contains("glue"))
						 {
							 boolean isGlueOk = false;
							 
							 if(input[4].equals("T"))
							 {
								 SSHClient.GET(SSHCommon.commonGluePath, srcBackup, list[i].getName(),sdf.format(date) + "_" + list[i].getName());
								
								 /**
								  * ##########################################################################
								 * @param 원격 서버에 Backup 파일 업로드
								 * ###########################################################################
								 */
								
								 isGlueOk = SSHClient.PUT(srcBackup, srcBackup, sdf.format(date) + "_" + list[i].getName());
							 }
								
							 
							 if(isGlueOk)
							 {
								 	/**
								 	 * ##########################################################################
								 	 * @param 실제 지정된 위치내 ear 파일 업로드/업데이트
								 	 * ###########################################################################
								 	 */
							 		real_upload[i] = SSHClient.PUT(srcDirectory, SSHCommon.commonGluePath, list[i].getName());
							 		System.out.println("glue.properties Upload : " + real_upload[i]);
							 }
						 }
						 
						 if(list[i].getName().contains("img"))
						 {
							 boolean isImgOk = false;
							 
							 if(input[4].equals("T"))
							 {
//								 SSHClient.GET(SSHCommon.commonImgPath, srcBackup, list[i].getName(),sdf.format(date) + "_" + list[i].getName());
								
								 /**
								  * ##########################################################################
								 * @param 원격 서버에 Backup 파일 업로드
								 * ###########################################################################
								 */

//								 isImgOk = SSHClient.PUT(srcBackup, srcBackup, sdf.format(date) + "_" + list[i].getName());
							 }
								
							 System.out.println("img Logic : " + list[i].getName());
						 		
//							 if(isImgOk)
//							 {
								 	/**
								 	 * ##########################################################################
								 	 * @param 원격 img 폴더내 기존 파일/디렉토리 전제 삭제
							 			DLCFCON1:/TOM> rm -r /TOM/Test/calender/
								 	 * ###########################################################################
								 	 */
							 		
							 		String[] Folder = RemoteManyFolderDelete(SSHCommon.commonImgPath);
							 		for(int lookup = 0; lookup < Folder.length; lookup++)
							 			System.out.println(SSHCommon.commonImgPath + Folder[lookup] + "/");
//							 			str = SSHClient.exec("rm -r " + SSHCommon.commonImgPath + Folder[lookup] + "/");
							 		
							 		
							 		/**
								 	 * ##########################################################################
								 	 * @param 실제 지정된 위치내 img 파일 업로드/업데이트
								 	 * ###########################################################################
								 	 */
							 		real_upload[i] = SSHClient.PUT(srcDirectory, SSHCommon.commonImgPath, list[i].getName());
							 		System.out.println("img Upload : " + real_upload[i]);
							 		
							 		

							 		/**
								 	 * ##########################################################################
								 	 * @param 새로 unzip
								 	 * ###########################################################################
								 	 */
							 		str = SSHClient.exec("cd " + SSHCommon.commonImgPath + " \n");
							 		str = SSHClient.exec("pwd \n");
							 		str = SSHClient.exec("unzip " + list[i].getName() + " \n");
//							 		str = SSHClient.exec("rm /WAS/APP/SCM/" + new SSHCommonRead().ServerHost + "/" + input[2] + "/WEB/img/" + list[i].getName() + " \n");
//							 }
						 }
						 
						 if(list[i].getName().contains("js"))
						 {
							 boolean isJsOk = false;
							 
							 if(input[4].equals("T"))
							 {
								 SSHClient.GET(SSHCommon.commonJsPath, srcBackup, list[i].getName(),sdf.format(date) + "_" + list[i].getName());
								
								 /**
								  * ##########################################################################
								 * @param 원격 서버에 Backup 파일 업로드
								 * ###########################################################################
								 */

								 isJsOk = SSHClient.PUT(srcBackup, srcBackup, sdf.format(date) + "_" + list[i].getName());
							 }
								
							 
							 if(isJsOk)
							 {
								 	/**
								 	 * ##########################################################################
								 	 * @param 원격 js폴더내 기존 파일/디렉토리 전제 삭제
							 			DLCFCON1:/TOM> rm -r /TOM/Test/calender/
								 	 * ###########################################################################
								 	 */
							 		
								 	str = SSHClient.exec("rm " + SSHCommon.commonJsPath + "* \n");
									
							 		/**
								 	 * ##########################################################################
								 	 * @param 실제 지정된 위치내 ear 파일 업로드/업데이트
								 	 * ###########################################################################
								 	 */
							 		real_upload[i] = SSHClient.PUT(srcDirectory, SSHCommon.commonJsPath, list[i].getName());
							 		System.out.println("js Upload : " + real_upload[i]);
							 		
							 		

							 		/**
								 	 * ##########################################################################
								 	 * @param 새로 unzip
								 	 * ###########################################################################
								 	 */
							 		str = SSHClient.exec("cd " + SSHCommon.commonJsPath + " \n");
							 		str = SSHClient.exec("pwd \n");
							 		str = SSHClient.exec("unzip " + list[i].getName() + " \n");
							 }
						 }
						 
						 if(list[i].getName().contains("css"))
						 {
							 boolean isCssOk = false;
							 
							 if(input[4].equals("T"))
							 {
								 SSHClient.GET(SSHCommon.commonCssPath, srcBackup, list[i].getName(),sdf.format(date) + "_" + list[i].getName());
								
								 /**
								  * ##########################################################################
								 * @param 원격 서버에 Backup 파일 업로드
								 * ###########################################################################
								 */

								 isCssOk = SSHClient.PUT(srcBackup, srcBackup, sdf.format(date) + "_" + list[i].getName());
							 }
								
							 
							 if(isCssOk)
							 {
								 	/**
								 	 * ##########################################################################
								 	 * @param 원격 css폴더내 기존 파일/디렉토리 전제 삭제
							 			DLCFCON1:/TOM> rm -r /TOM/Test/calender/
								 	 * ###########################################################################
								 	 */
							 		
								 	str = SSHClient.exec("rm " + SSHCommon.commonCssPath + "* \n");
									
							 		/**
								 	 * ##########################################################################
								 	 * @param 실제 지정된 위치내 ear 파일 업로드/업데이트
								 	 * ###########################################################################
								 	 */
							 		real_upload[i] = SSHClient.PUT(srcDirectory, SSHCommon.commonCssPath, list[i].getName());
							 		System.out.println("css Upload : " + real_upload[i]);
							 		
							 		

							 		/**
								 	 * ##########################################################################
								 	 * @param 새로 unzip
								 	 * ###########################################################################
								 	 */
							 		str = SSHClient.exec("cd " + SSHCommon.commonCssPath + " \n");
							 		str = SSHClient.exec("pwd \n");
							 		str = SSHClient.exec("unzip " + list[i].getName() + " \n");
							 }
						 }
					 }
				}
				catch (Exception e)		
				{
					  	 System.out.println("#########################################");
					     System.out.println(e);
					     monitor = e.toString();
					     results.append(monitor);
					     System.out.println("#########################################");
				} 
				finally
				{
					SSHClient.logout();
				    System.out.println("isClosed : " + SSHClient.isClosed());
				      
				    results.append("\n");
				    results.append("#########################################");
				    results.append("\n");
				    results.append("File Upload Result for Deploying");
				    results.append("\n");
				    
				    for(int i=0; i < list.length;i++){
				    	results.append(list[i].getName() + " => " + real_upload[i] + "\n");
				    }
				    results.append("\n");
				}
			}
			else
			{
				System.out.println("\n");
				System.out.println("#############################################");
				results.append("Warning : Different Argument for Deploying...");
				results.append(new SSHCommonRead().log);
			}
			
		}catch (Exception e)	
		{
			
		}

		return results.toString();
	}
	
	
	/**
 	 * ##########################################################################
 	 * @param 원격 img 폴더내 기존 파일/디렉토리 전제 삭제
			DLCFCON1:/TOM> rm -r /TOM/Test/calender/
 	 * ###########################################################################
 	 */
		
	private String[] RemoteManyFolderDelete(String Path)
	{
		System.out.println("RemoteManyFolderDelete : " + Path);
		File f = new File(Path);
		String[] dirList = null;
		 
		try
		{
			 if(!f.isDirectory()){
			    System.out.println("Not Correct Directory");
			 }

			 dirList = f.list();
			 
			   if(dirList != null) {
			      for(int i=0; i<dirList.length; i++) {
			         System.out.println(dirList[i]);
			   }
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			f = null;
		}
		
		return dirList;
	}
}


/**
 * @param args[0] WAS IP Address
 * @param args[1] WAS ID
 * @param args[2] WAS Password
 * @param args[3] HOST Name
 * @param args[4] ChainCode
 */
class SSHDeployService
{
	/**
	 * @param args[0] 10.132.12.74
	 * @param args[1] devuser
	 * @param args[2] posco!23
	 * @param args[3] TFESA01
	 * @param args[4] U71
	 */
	public void run(String[] args) throws Exception
	{
		String earnewfile = "";
	    String srcDirectory = "C:\\";
		String earFileDirectory = "/WAS/APP/SCM/" + args[3] + "/" + args[4] + "/";
		String propertiesDirectory = "/WAS/APP/SCM/" + args[3] + "/" + args[4] + "/WEB/properties/";
		
		if(args[4].equals("U7A0"))
			earnewfile = "U7A010APP";
		else
			earnewfile =  args[4];
				
		String ear_files = earnewfile + ".ear";
		String properties_files = "glue.properties";
		String tmp_earfiles = "/", tmp_properties = "/";
		
		String monitor = "";
		boolean ack = false;
		
		SSHShellClient SSHClient = null;
		
		// TODO Auto-generated method stub
		 try 
		 {
//			 jsclient.setHostprompt("> ");
	         
			 System.out.println(args[0]);
			 System.out.println(args[1]);
			 System.out.println(args[2]);
			 System.out.println(args[3]);
			 System.out.println(args[4]);
			 
			 Date date = new Date();
			 SSHClient = new SSHShellClient(args[0], args[1], args[2]);
			 
			 // #########################################################
	         // DOWNLOAD
			 
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			 tmp_earfiles =  sdf.format(date) + "_" + ear_files;
			 tmp_properties =  sdf.format(date) + "_" + properties_files;
	         
			 
			// DOWNLOAD properties
			 boolean ack_properties_down = SSHClient.GET(propertiesDirectory, 
	        		 						   srcDirectory,
	        		 						   properties_files,
	        		 						   tmp_properties); 
			 
			 // DOWNLOAD ear
			 boolean ack_ear_down = SSHClient.GET(earFileDirectory, 
	        		 						   srcDirectory,
	        		 						   ear_files,
	        		 						   tmp_earfiles); 
	         			 
	         if(ack_ear_down)
	         {
				 // #########################################################
		         
	        	 // UPLOAD backup properties files
//	        	 SSHClient.PUT(srcDirectory, propertiesDirectory, tmp_properties);
	        	 
	        	// UPLOAD backup ear files
//	        	 SSHClient.PUT(srcDirectory, earFileDirectory, tmp_earfiles);
	        	 
	        	 // UIpload properties File
		         boolean ack_properties_upload = SSHClient.PUT(srcDirectory,
		        		 						  propertiesDirectory,
		        		 						 properties_files); 
		         
	        	 // Upload Ear File
		         boolean ack_ear_upload = SSHClient.PUT(srcDirectory,
		        		 							 earFileDirectory,
		        		 							 ear_files); 
		        
		         if(ack_ear_upload)
		         {
		        	 ack = true;
		        	 
		        	 System.out.println("#########################################");
		        	 System.out.println("ack_upload : " + ack_ear_upload);
		        	 System.out.println("#########################################");
		        	 
	//				 #################################################################
	//				 DEPLOY
		        	 
//		        	 SSHClient.Chmod(ear_files);
			         
		        	 String strDeploy="", strUpload = "";
		        	 if(args[4].equals("U7A0")) 	
		        	 {
		        		 strDeploy = "deploy"; 
		        		 strUpload = "upload";
		        	 }
		        	 else
		        	 {
		        		 strDeploy = "Deploy"; 
		        		 strUpload = "Upload";
		        	 }
		        	 
					 String result = SSHClient.exec(earFileDirectory + strDeploy + args[4] + ".sh stop"+ "\n");
			         System.out.println("jsclient.exec :" + result);
			         
			         
			         result = SSHClient.exec(earFileDirectory + strUpload + args[4] + ".sh properties"+ "\n");
			         System.out.println("jsclient.exec :" + result);
			         
			         result = SSHClient.exec(earFileDirectory + strDeploy + args[4] + ".sh redeploy " + earnewfile + ".ear"+ "\n");
			         System.out.println("jsclient.exec :" + result);
			         
			         result = SSHClient.exec(earFileDirectory + strDeploy + args[4] + ".sh start"+ "\n");
			         System.out.println("jsclient.exec :" + result);
			     }
		         else
		         {
		        	 System.err.println("#########################################");
		        	 System.err.println("ack_upload : " + ack_ear_upload);
		        	 System.err.println("#########################################");
		         }
		         
		      // #########################################################
		         	     
		        // #########################################################
		         // DOWNLOAD
	//	         boolean ack_down = SSHClient.GET("/TOM/ECM_Delegate/dec_files/", 
	//						"C:\\Users\\황 의영\\Desktop\\테스트\\",
	//						"tmp_0900bf4b98cf9ff5_1408522211814.xlsx",
	//						"tmp_0900bf4b98cf9ff5_down.xlsx"); 
		         
		       // #########################################################
	         }
	          
		  }	catch (Exception e)		
		  {
			  	 System.out.println("#########################################");
			     System.out.println(e);
			     monitor = e.toString();
			     System.out.println("#########################################");
		  } 
		  finally
		  {
			  SSHClient.logout();
		      System.out.println("isClosed : " + SSHClient.isClosed());
		      
		      System.out.println("\n\n");
		      System.out.println("#########################################");
		      if(ack)		      System.out.println("SUCCESS~~~");
		      if(!ack)
		      {
		    	  System.out.println("Failed~~~");
		    	  System.out.println(monitor);
		      }
		      System.out.println("#########################################");
		  }
	}
}


class SSHCommandService
{
	public void run()
	{
//		SSHShellClient SSHClient = new SSHShellClient("10.132.14.144", "tomadmd", "posco123");
		// TODO Auto-generated method stub
		 try 
		 {

			 SSHShellClient jsclient = new SSHShellClient("10.132.12.74", "devuser", "posco!23");
//			 SSHShellClient jsclient = new SSHShellClient("10.132.16.32", "devuser", "qwer!234");
//			 SSHShellClient jsclient = new SSHShellClient("10.132.12.74");
			  
//	         StringBuffer shell = new StringBuffer("cd /TOM/Feeds/GESI_Feeder/\n");
//	         shell.append("f.sh WAS_SEARCH");
	         
			
//			 jsclient.setHostprompt("> ");
			 boolean ack_properties_upload = jsclient.PUT("C:\\",
					  "/WAS/DATA/ES/Deploy/U40/Upload/",
					 "U40.ear"); 
	         
	       
//	        
//	         jsclient.setHostprompt("> ");
//	         result = jsclient.exec("y");
//	         System.out.println("jsclient.exec :" + result);

//	         String result = jsclient.exec("nohup /WAS/APP/SCM/DESA01/U71/test.sh 1> /dev/null 2>&1 &");
//	         System.out.println("jsclient.exec :" + result);
	         
	         jsclient.logout();
	         jsclient.isClosed();
	         
//			 #################################################################
//			 DEPLOY
//			 String result = jsclient.exec("/WAS/APP/SCM/TFESA01/U40/DeployU40.sh stop"+ "\n");
//	         System.out.println("jsclient.exec :" + result);
//	         
//	         
//	         result = jsclient.exec("/WAS/APP/SCM/TFESA01/U40/DeployU40.sh redeploy U40.ear"+ "\n");
//	         System.out.println("jsclient.exec :" + result);
//	         
//	         result = jsclient.exec("/WAS/APP/SCM/TFESA01/U40/DeployU40.sh start"+ "\n");
//	         System.out.println("jsclient.exec :" + result);
//	         
//	         result = jsclient.exec("f.sh WAS_SEARCH");
//	         System.out.println("jsclient.exec :" + result);
	         
	         // #########################################################
	         // UPLOAD
//	         boolean test1 = jsclient.lcd("C:\\Users\\황 의영\\Desktop\\"); 
//	         boolean test2 = jsclient.cd("/TOM/ECM_Delegate/dec_files/");
////	         boolean test3 = jsclient.lcd("C:/");
//	         boolean test4 = jsclient.put("menu_codetable.xml");
	      // #########################################################
	         	     
	        // #########################################################
	         // DOWNLOAD
//	         boolean test5 = SSHClient.LinuxCD("/TOM/ECM_Delegate/");
//	         boolean test6 = SSHClient.LinuxLCD("C:\\Users\\황 의영\\Desktop\\"); 
//			 boolean test7 = SSHClient.GET("/TOM/ECM_Delegate/", "C:\\Users\\황 의영\\Desktop\\", "s.sh", "s.sh");
	       // #########################################################
	          
		  }	catch (Exception e)		
		  {
		         System.out.println(e);
		  } 
		  finally
		  {
//			  SSHClient.logout();
//		      System.out.println("isClosed : " + SSHClient.isClosed());
		  }
	}
}
