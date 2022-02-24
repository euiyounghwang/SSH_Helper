import java.io.File;

import SSH.ShellFTP.SSHShellClient;


public class SSHUpload 
{
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
	
	public void run()
	{
	   
	    String monitor = null, srcDirectory = null, srcBackup = null, destDirectory = null;
	    
	    srcDirectory="/home/euiyoung-hwang/ES/upload/";
	    StringBuffer results = new StringBuffer();
	   

//	    destDirectory = "/usr/local/lib/";
	    destDirectory = "/home/ES/elasticsearch-5.4.1/config/analysis/";
		
		
		File[] list = null;
		boolean[] ack_upload = null, real_upload = null;
		
		SSHShellClient SSHClient = null;
		
		try 
		{
			// 특정 서버 IP의 서버
//			boolean isCache = new SSHCommonRead().SaveAccountCache(input);
			
//			if(isCache)
//			{
			
			String[] serverlists = {"127.0.0.1","127.0.0.1"};
			for(int loop=0; loop < serverlists.length; loop++)
			{
				System.out.println("#############################################");
				System.out.println("Server IP : " + serverlists[loop]);
				System.out.println("UserID : " + new SSHCommonRead().UserID);
				System.out.println("UserPW : " + new SSHCommonRead().UserPW);
				System.out.println("\n\n");
				
				try
				{
					/**
					 * @param args[1] WAS Target IP Address
					 * @param args[2] WAS ID
					 * @param args[3] WAS Password
					 * 
					 * 목적지 서버에 파일 업로드 1단계 
					 */
					 SSHClient = new SSHShellClient(serverlists[loop], new SSHCommonRead().UserID, new SSHCommonRead().UserPW);
					
					 File f = new File(srcDirectory);
		 
					 if(!f.exists() || !f.isDirectory()){
					    System.out.println("올바르지 않은 디렉토리 입니다.");
					 }
		
					 list = f.listFiles();
					 ack_upload  = new boolean[list.length];
					 
					 for(int i=0; i < list.length;i++)
					 {
						 // 배포할 파일들을 temp 폴더에 저장
//						 if(list[i].getName().equals("jdk-8u131-linux-x64.tar.gz")) {
//							 System.out.println("Uploading...");
							 ack_upload[i] = SSHClient.PUT(srcDirectory, destDirectory, list[i].getName());
							 	System.out.println(list[i].getName() + " uploaded ..");
							 	
//							 	break;
//						 }
							 
					 }
				}
				catch (Exception e)		
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
				      
				    results.append("\n");
				    results.append("#########################################");
				    results.append("\n");
				    results.append("File Upload Result (" + serverlists[loop] + ")");
				    results.append("\n");
				    
				    for(int i=0; i < list.length;i++){
				    	results.append(list[i].getName() + " => " + ack_upload[i] + "\n");
				    }
				    results.append("\n");
				    System.out.println(results);
				}
			}
//			}
//			else
//			{
//				System.out.println("\n");
//				System.out.println("#############################################");
//				results.append("Warning : Different Argument for Uploading...");
//				results.append(new SSHCommonRead().log);
//			}
			
		}catch (Exception e)	
		{
			
		}

//		return results.toString();
	}
}
