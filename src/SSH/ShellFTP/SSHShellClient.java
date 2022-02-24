package SSH.ShellFTP;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;


public class SSHShellClient {
	
	private Log log = LogFactory.getLog(this.getClass());    
	private SshClient ssh = null;    
	private SessionChannelClient session = null;    
	private String hostPrompt = null;        
	
	SSHSFTPClient ftpclient = null;
	private SftpClient sftp = null;
	    
	 /**
	  * @param server
	  * @param userid
	  * @param pwd
	  * @throws Exception
	  */
	 public SSHShellClient(String server, String userid, String password) throws Exception
	 {    
		 PasswordAuthenticationClient auth = null;    

	     try {
	            if (server == null || userid == null || password == null) {
	                System.out.println("Parameter is null!");
	            }

	     ssh = new SshClient();
	     ssh.setSocketTimeout(30000);
	     SshConnectionProperties properties = new SshConnectionProperties();
	     properties.setHost(server);
	     properties.setPort(22);
	     properties.setPrefPublicKey("ssh-dss");
	     // Connect to the host
//	     ssh.connect(properties, new AlwaysAllowingConsoleKnownHostsKeyVerification());
//	     ssh.connect(server, new ConsoleKnownHostsKeyVerification());
	     ssh.connect(server, new IgnoreHostKeyVerification());
//	     ssh.connect(server);

	// # 패스워드를 이용한 인증 접속        
	// ------------------------------------------------------------
	     PasswordAuthenticationClient authClient = new PasswordAuthenticationClient();
	     authClient.setUsername(userid);
	     authClient.setPassword(password);
	// ------------------------------------------------------------



	// # 패스워드 없이 공개키를 이용한 인증 접속        
	// ------------------------------------------------------------
	            // Initialize the authentication data.
//	            PublicKeyAuthenticationClient authClient = new PublicKeyAuthenticationClient();
//	            // 사용자 계정
//	            authClient.setUsername(userid);
//	            // id_rsa 파일 위치
//	            SshPrivateKeyFile file = SshPrivateKeyFile.parse(
//	                                          new File("/home/userid/.ssh/id_rsa"));
//	            SshPrivateKey privateKey = file.toPrivateKey(null);
//	            authClient.setKey( privateKey );    
	// ------------------------------------------------------------
	    int result = ssh.authenticate(authClient);

	    if (result != AuthenticationProtocolState.COMPLETE) {
	                throw new Exception("Login failed");
	     }    

		 // SSH 터미널 커맨드 실행용
		 session = ssh.openSessionChannel();
		 session.requestPseudoTerminal("vt100", 80, 25, 0, 0 , "");
		 session.startShell();
		     
		 sftp = ssh.openSftpClient();
		 ftpclient = new SSHSFTPClient(sftp);
		     
	     } catch (Exception e) {
	            e.printStackTrace();
	            log.error(e);
	            throw e;
	        }    
	  } 
	 
	 
	 /**
	  * @param server
	  * @throws Exception
	  */
	 public SSHShellClient(String server) throws Exception
	 {    
//		 String userid = "devuser";
//		 String password = "posco!23";
		 		 
		 String[] account = new SSHConfig().run();
		 
		 String userid = account[0];
		 String password = account[1];
		 
		 PasswordAuthenticationClient auth = null;    

	     try {
	            if (server == null) {
	                System.out.println("Parameter is null!");
	            }

	     ssh = new SshClient();
	     ssh.setSocketTimeout(3000);
	     SshConnectionProperties properties = new SshConnectionProperties();
	     properties.setHost(server);
	     properties.setPort(22);
	     properties.setPrefPublicKey("ssh-dss");
	  
	     // Connect to the host
	     ssh.connect(server, new IgnoreHostKeyVerification());

	     // # 패스워드를 이용한 인증 접속        
	     //	------------------------------------------------------------
	     PasswordAuthenticationClient authClient = new PasswordAuthenticationClient();
	     authClient.setUsername(userid);
	     authClient.setPassword(password);
	     // ------------------------------------------------------------



	// # 패스워드 없이 공개키를 이용한 인증 접속        
	// ------------------------------------------------------------
	            // Initialize the authentication data.
//	            PublicKeyAuthenticationClient authClient = new PublicKeyAuthenticationClient();
//	            // 사용자 계정
//	            authClient.setUsername(userid);
//	            // id_rsa 파일 위치
//	            SshPrivateKeyFile file = SshPrivateKeyFile.parse(
//	                                          new File("/home/userid/.ssh/id_rsa"));
//	            SshPrivateKey privateKey = file.toPrivateKey(null);
//	            authClient.setKey( privateKey );    
	// ------------------------------------------------------------
	    int result = ssh.authenticate(authClient);

	    if (result != AuthenticationProtocolState.COMPLETE) {
	                throw new Exception("Login failed");
	     }    

		 // SSH 터미널 커맨드 실행용
		 session = ssh.openSessionChannel();
		 session.requestPseudoTerminal("vt100", 80, 25, 0, 0 , "");
		 session.startShell();
		     
		 sftp = ssh.openSftpClient();
		 ftpclient = new SSHSFTPClient(sftp);
		     
	     } catch (Exception e) {
	            e.printStackTrace();
	            log.error(e);
	            throw e;
	        }    
	  }  
	 

   /**
	 *  특정 서버(리눅스)에서의 명령어 실행
    */
   /*    * 커맨드 실행 함수(ex : ls -l\n)    */
   public String exec(String cmd) throws Exception
   {
	   this.hostPrompt = ">";
	   
       StringBuffer returnValue = null;
       boolean promptReturned = false;
       byte[] buffer = null;
       OutputStream out = null;
       InputStream in = null;
       int read;
       String response = null;
       int i = 0;
       int loop = 0;
//       System.out.println("exec : \t" + cmd);
       try {
           if (session == null) {
	            log.error("Session is not connected!");
	            throw new Exception("Session is not connected!");
	        }
	            
	   out = session.getOutputStream();
//	   System.out.println("CMD >> " + cmd);
//	   cmd = cmd; // 행단위 명령을 위해 \n 추가
	   out.write(cmd.getBytes());
	   out.flush();
	   in = session.getInputStream();
	            buffer = new byte[255];
//	   buffer = new byte[1024];
	            returnValue = new StringBuffer(300);
//	   returnValue = new StringBuffer(4096);
	            
	   while(promptReturned == false && (read = in.read(buffer)) > 0) 
	   {
		   loop++;
	            	
		   response = new String(buffer, 0, read);
		   if (i == 1)
	       {
			   			returnValue.append(response.toString());
//	                    System.out.println(returnValue.toString());
	       }
	       if (!StringUtils.isEmpty(response) 
	    		   && response.indexOf(this.hostPrompt) >= 0) {
	                   ++i;
//	                   System.out.println("cmd$$ : \t" + cmd);
	                   if (i >= 2) {
	                        promptReturned = true;
	                   }
	       	}
//	       		log.info("output stream : " + loop);
        }
	   	   
	            
//	     	log.info("session.getExitCode() : " + session.getExitCode());
//	        log.error("returnValue : " + returnValue.toString());

       	} catch (Exception e) {
	            e.printStackTrace();
	            System.out.println(e);
	    }
	    
       return returnValue.toString();
   }

   /**
  	 *  SSH 세션 종료
   */
   public boolean isClosed() throws Exception
   {
        boolean rtn = true;
        try {

        	if (session != null){
        		rtn = session.isClosed();
        	}
	    
        	if(sftp != null){
             	sftp.isClosed();
             }
        	 
	        } catch(Exception e) {
	            e.printStackTrace();
	            log.error(e);
	        }
	        return rtn;
	    }

   
   /**
 	 *  SSH 세션 로그아웃
  */
   public boolean logout() throws Exception
   {
        boolean rtn = false;

        try {
            if (session != null) {
                session.getOutputStream().write("exit\n".getBytes());
                session.close();
            }
            
            if(sftp != null){
            	sftp.quit();
            }
            
            if (ssh != null)
                ssh.disconnect();
            rtn = true;
        } catch(Exception e) {
            e.printStackTrace();
            log.error(e);
   }
        return rtn;
   }

 /**
 *  getHostprompt
 */
   public String getHostprompt() {
       return this.hostPrompt;
   }
   
  /**
   *  setHostprompt
   */
   public void setHostprompt(String hostPrompt) {
        this.hostPrompt = hostPrompt;
   }
   
   /**
	 *  리눅스 서버 Path 경로 리턴값
	 */
	public boolean LinuxCD(String path) throws Exception {
		   return ftpclient.cd(path);
	}
	   
	
	/**
	*  로컬PC Path 경로 리턴값
	*/
	
	public boolean LinuxLCD(String path) throws Exception {
	   return ftpclient.lcd(path);
	}
	  
	
	/**
	 *  @param srcPath : 소스파일이 포함된 경로 (실행되는 서버)
	 *  로컬PC Path 경로 리턴값
	 
	 */
	public boolean GET(String srcPath, String decPath, String srcFile, String destFile) throws Exception {
		ftpclient.cd(srcPath);
		ftpclient.lcd(decPath);
		return ftpclient.get(srcFile, destFile);
	}
	

	/**
	 *  서버 업로드
	 */
	public boolean PUT(String srcPath, String decPath, String destFile) throws Exception {
		ftpclient.lcd(srcPath);
		ftpclient.cd(decPath);
		return ftpclient.put(destFile);
	}
	
	/**
	 *  서버 업로드
	 */
	public boolean Chmod(String path) throws Exception {
		return ftpclient.chmod(755, path);
	}
	
	   
	/**
	 *  로컬PC Path 경로 삭제
	 */
	public boolean RM(String Path) throws Exception {
		return ftpclient.rm(Path);
	}
}



class FTPInterface extends SSHShellClient
{
	public FTPInterface(String server, String userid, String password)
			throws Exception {
		super(server, userid, password);
		// TODO Auto-generated constructor stub
	}
	
	
}
