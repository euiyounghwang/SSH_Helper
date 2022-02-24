package SSH.ShellFTP;

import com.sshtools.j2ssh.SftpClient;

public class SSHSFTPClient 
{
	/**
     * GET / PUT ftp client
     */
    private SftpClient sftp;

   
    /**
     *  특정 서버(리눅스) FTP Client
    */
    public SSHSFTPClient(SftpClient sftp)
    {
    	this.sftp = sftp;
    }
    

    /**
     *  특정 서버(리눅스)에 파일 업로드
    */
    public boolean put(String path) throws Exception
    {
    	boolean rtn = false;

	    try
	    {
	    	if (sftp != null) {
	    			sftp.put(path);
	    			rtn = true;
	    	}

     } catch(Exception e) {
    	 System.out.println(e);
     }

     return rtn;
    }

    
    /**
     *  특정 서버(리눅스)에서 로컬 PC로 다운로드
     *  srcFile : 리눅스 서버
     *  destFile : 로컬PC
     *  
     *  example)
     *   boolean test5 = SSHClient.cd("/TOM/ECM_Delegate/");
	     boolean test6 = SSHClient.lcd("C:\\Users\\황 의영\\Desktop\\");            
	     boolean test7 = SSHClient.get("log4j.xml", "1.xml");
     */
    public boolean get(String srcFile, String destFile) throws Exception
    {

    	boolean rtn = false;

    	try {
    	 		if (sftp != null) {
    	 			if (destFile == null)
    	 					sftp.get(srcFile);
    	 			else
    	 					sftp.get(srcFile, destFile);
    	 		rtn = true;
    	 	}

    	} catch(Exception e) {
    		System.out.println(e);

    	}

    	return rtn;
    }

    
    /**
     *  로컬 PC 윈도우 디렉토리 지정
     *  example)
     *   boolean test5 = SSHClient.cd("/TOM/ECM_Delegate/");
	     boolean test6 = SSHClient.lcd("C:\\Users\\황 의영\\Desktop\\");            
	     boolean test7 = SSHClient.get("log4j.xml", "1.xml");
     */
    public boolean lcd(String path) throws Exception
    {

    	boolean rtn = false;

    	try {
    		if (sftp != null) {
    			sftp.lcd(path);

    		rtn = true;
    		}

    	} catch(Exception e) {
    		System.out.println(e);
     }
     	return rtn;
    }
    
    /**
     *  로컬 PC 윈도우 디렉토리 지정
     *  example)
     *   boolean test5 = SSHClient.cd("/TOM/ECM_Delegate/");
	     boolean test6 = SSHClient.lcd("C:\\Users\\황 의영\\Desktop\\");            
	     boolean test7 = SSHClient.get("log4j.xml", "1.xml");
     */
    public boolean rm(String path) throws Exception
    {

    	boolean rtn = false;

    	try {
    		if (sftp != null) {
    			sftp.rm(path);
   
    			rtn = true;
    		}

    	} catch(Exception e) {
    		System.out.println(e);
     }
     	return rtn;
    }

    
    /**
     *  특정 서버(리눅스)에서 로컬 PC로 다운로드
     *  srcFile : 리눅스 서버
     *  destFile : 로컬PC
     *  
     *  example)
     *   boolean test5 = SSHClient.cd("/TOM/ECM_Delegate/");
	     boolean test6 = SSHClient.lcd("C:\\Users\\황 의영\\Desktop\\");            
	     boolean test7 = SSHClient.get("log4j.xml", "1.xml");
     */
    public boolean cd(String path) throws Exception
    {

    	boolean rtn = false;
    	
    	try {
    		if (sftp != null) {
    			sftp.cd(path);
    		rtn = true;
    		}
    	} catch(Exception e) {
    		System.out.println(e);
    	}
    	return rtn;
    }


    /**
     *  특정 서버(리눅스)에서 'PWD' 명령어
     *  
    */
    public String pwd() throws Exception
    {

    	String rtnStr = null;

    	try {
    		if (sftp != null) {
    			rtnStr = sftp.pwd();
    		}

    	} catch(Exception e) {
    		System.out.println(e);

     }

     return rtnStr;
     
    }

    /**
     *  특정 서버(리눅스)에서 'CHMOD' 명령어
     *  
    */
    public boolean chmod(int permissions, String path) throws Exception
    {

    	boolean rtn = false;

    	try {
    		if (sftp != null) {
    			sftp.chmod(permissions, path);
    			rtn = true;
    		}
    	} catch(Exception e) {
    		System.out.println(e);
    	}

     return rtn;
    }

}
