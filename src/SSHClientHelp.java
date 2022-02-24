
public class SSHClientHelp 
{
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		
		String result = null;
		
//		new SSHCommandService().run(); // Command
//		new SSHDeployServiceU40().run();
//		new SSHDeployServiceU71().run();
//		new SSHDeployService().run(args);
		
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
		 */
		
//		String[] upload = {"dev", SSHCommon.configDirectory, "U40", "U"};
//		result = new SSHUpload().run(upload);
//		System.out.println(result);
		
		
		/**
		 * @param args[0] Deploy Target Server 
		 * @param args[1] DeployServer.xml 경로
		 * @param args[2] Chain Code
		 * @param args[3] Command  (U : Upload, D : Deploy, L : Log)
		 * @param args[4] 백업유무(기존의 ear 파일등의 배포용 파일들)
		 */
//		String[] deploy = {"127.0.0.1", SSHCommon.configDirectory, "U40", "D", "T"};
//		result = new SSHDeploy().run(deploy);
		
		

//		result = new SSHDirectCommand().run("127.0.0.1", "ls -al");
		result = new SSHDirectCommand().ExcuteCommand("ls -al");
		System.out.println(result);
	}
}
