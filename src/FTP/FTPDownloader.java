package FTP;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;

public class FTPDownloader {
  private String server = null;
  
  private String user = null;
  
  private String pass = null;
  
  FTPClient ftpClient = null;
  
  public FTPDownloader(String host, String user, String pwd) throws Exception {
    this.server = host;
    this.user = user;
    this.pass = pwd;
  }
  
  public void Run(String srcdirectory, String destDirectory, String downloadFileNm) {
    int port = 21;
    this.ftpClient = new FTPClient();
    try {
      this.ftpClient.connect(this.server, port);
      this.ftpClient.login(this.user, this.pass);
      this.ftpClient.enterLocalPassiveMode();
      this.ftpClient.setFileType(2);
      String remoteFile1 = String.valueOf(srcdirectory) + downloadFileNm;
      File downloadFile1 = new File(String.valueOf(destDirectory) + downloadFileNm);
      OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
      boolean success = this.ftpClient.retrieveFile(remoteFile1, outputStream1);
      outputStream1.close();
      if (success)
        System.out.println("File #1(" + downloadFileNm + ") has been downloaded successfully."); 
    } catch (IOException ex) {
      System.out.println("Error: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      try {
        if (this.ftpClient.isConnected()) {
          this.ftpClient.logout();
          this.ftpClient.disconnect();
        } 
      } catch (IOException ex) {
        ex.printStackTrace();
      } 
    } 
  }
  
  public void disconnect() {
    if (this.ftpClient.isConnected())
      try {
        this.ftpClient.logout();
        this.ftpClient.disconnect();
      } catch (IOException f) {
        f.printStackTrace();
      }  
  }
}
