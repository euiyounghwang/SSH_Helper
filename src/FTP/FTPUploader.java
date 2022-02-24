package FTP;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import FTP.FTPUploader;

public class FTPUploader {
  FTPClient ftp = null;
  
  public FTPUploader(String host, String user, String pwd) throws Exception {
    this.ftp = new FTPClient();
    this.ftp.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(new PrintWriter(System.out)));
    this.ftp.connect(host);
    int reply = this.ftp.getReplyCode();
    if (!FTPReply.isPositiveCompletion(reply)) {
      this.ftp.disconnect();
      throw new Exception("Exception in connecting to FTP Server");
    } 
    this.ftp.login(user, pwd);
    this.ftp.setFileType(2);
    this.ftp.enterLocalPassiveMode();
  }
  
  public void uploadFile(String localFileFullName, String fileName, String hostDir) throws Exception {
    InputStream input = new FileInputStream(new File(localFileFullName));
    this.ftp.storeFile(String.valueOf(hostDir) + fileName, input);
  }
  
  public void disconnect() {
    if (this.ftp.isConnected())
      try {
        this.ftp.logout();
        this.ftp.disconnect();
      } catch (IOException f) {
        f.printStackTrace();
      }  
  }
}
