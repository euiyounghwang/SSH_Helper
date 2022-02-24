import SSH.ShellFTP.SSHShellClient;


public class SSHDirectCommand 
{

	/**
	 * @param args[0] Target Server IP
	 */
	
	public String run(String input, String command)
	{
	   
	    String monitor = null, srcDirectory = null, srcBackup = null, destDirectory = null;
	    
	    StringBuffer results = new StringBuffer();
	   
	    SSHShellClient SSHClient = null;
		
		try 
		{
			// 특정 서버 IP의 서버
			boolean isCache = new SSHCommonRead().SaveAccountCacheCommand(input);
			
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
					
					 results.append(SSHClient.exec(command));
					 
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
				}
			}
			else
			{
				System.out.println("\n");
				System.out.println("#############################################");
				results.append("Warning : Error Command...");
				results.append(new SSHCommonRead().log);
			}
			
		}catch (Exception e)	
		{
			
		}

		return results.toString();
	}
	
	
	public String ExcuteCommand(String command)
	{
	   
	    String monitor = null, srcDirectory = null, srcBackup = null, destDirectory = null;
	    
	    StringBuffer results = new StringBuffer();
	   
	    SSHShellClient SSHClient = null;
		
		try 
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
				
				 results.append(SSHClient.exec(command));
				 
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
			}
			
		}catch (Exception e)	
		{
			
		}

		return results.toString();
	}
}
