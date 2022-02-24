import com.sshtools.j2ssh.SshException;

import FTP.FTPDownloader;
import SSH.ShellCommand.ShellCommand;


public class SSHCommandTest {

	public static void main(String[] args) throws SshException {
		// TODO Auto-generated method stub
		
//		ShellCommand CommandObject = new ShellCommand("127.0.0.1", "root", "1");
//		
//		if(CommandObject.connect()) {
//			System.out.println(CommandObject.executeCommand("nohup /ES/elasticsearch-5.4.1/bin/restart.sh &"));
//		}
		

				
//		SSHUpload _Upload = new SSHUpload();
//		_Upload.run();
		
//		
//		SSHDownload _Download = new SSHDownload();
//		_Download.run();
		
		String Socket_Message = "ftp_download;ftp_download:/array5/image/ipspro/2008630_36.pdf";
		System.out.println("\nSocket_Message => " + Socket_Message);
		String _download_filelist = Socket_Message.split(";")[1];
		FTPDownloader ftpDownloader = null;
		boolean is_Success = false;
		try {
			try {
				ftpDownloader = new FTPDownloader("127.0.0.1", "account", "passwd");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < (_download_filelist.split(",")).length; i++) {
				System.out.println("\nDownloading Full Path for FileName.. ==> " + _download_filelist.split(",")[i]);
				String _Get_Dirname = _download_filelist.split(",")[i].substring(0,_download_filelist.split(",")[i].lastIndexOf('/') + 1);
				String _Get_Filename = _download_filelist.split(",")[i].substring(_download_filelist.split(",")[i].lastIndexOf('/') + 1);
				System.out.println("Downloading Path (dir) ==> " + _Get_Dirname + " => Path (name) ==> " + _Get_Filename);
				ftpDownloader.Run(_Get_Dirname, "C:/", _Get_Filename);
			}
			is_Success = true;
		} finally {
			ftpDownloader.disconnect();
		}
		
	}

}
