import java.io.File;



public class SSHCommon {

	/**
	 * @param DeployServer.xml 경로 (Local Path)
	 */
	public static String configDirectory = "C:\\Upload\\";
//	public static String configDirectory = "/WAS/DATA/ES/config/";
	
	/**
	 * @param 업로드된 파일 경로
	 *  if(SSHCommon.isServer)
	 *    	srcDirectory = "/WAS/DATA/ES/Deploy/" + input[2] + "/Upload/";
	 *     else
	 *     	srcDirectory = input[1];
	 */
	public static boolean isServerUpload = true;
	
	/**
	 * @param 업로드된 파일 경로
	 *  if(SSHCommon.isServer)
	 *    	srcDirectory = "/WAS/DATA/ES/Deploy/" + input[2] + "/Backup/";
	 *     else
	 *     	srcDirectory = input[1];
	 */
	public static boolean isServerBackup = true;
	
	/**
	 * @param 실제 배포 ear 파일 디렉토리
	 */
	public static String commonEarPath = null;
	
	/**
	 * @param 실제 배포 glue.properites 디렉토리
	 */
	public static String commonGluePath = null;
	
	/**
	 * @param 실제 배포 img 디렉토리
	 */
	public static String commonImgPath = null;
	
	/**
	 * @param 실제 배포 csss 디렉토리
	 */
	public static String commonCssPath = null;
	
	/**
	 * @param 실제 배포 Js 디렉토리
	 */
	public static String commonJsPath = null;
	
	
	/**
	 * @param parentPath
	 * 접속된 서버내 Backup 파일 삭제
	 */
	public static void deletefiles(String parentPath)
	{
		File file = new File(parentPath);
	    String[] fnameList = file.list();
	    int fCnt = fnameList.length;
	    String childPath = "";
	    
	    for(int i = 0; i < fCnt; i++) 
	    {
	      childPath = parentPath+"/"+fnameList[i];
	      File f = new File(childPath);
	      if( ! f.isDirectory()) {
	        f.delete();   //파일이면 바로 삭제
	      }
	   
	    }
	}
}
