package ca.albertlockett.yarn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FFT Application Launcher class 
 * @author albert
 */
public class FFTAppClient {

	private static final Logger LOG = 
			LoggerFactory.getLogger(FFTAppClient.class);
	
	private static final String APP_JAR		= "fft-yarn.jar";
	private static final String APP_NAME	= "FFT_YARN";
	
	private YarnConfiguration conf;
	private YarnClient yarnClient;
	private ApplicationId appId;
	private FileSystem fs;
	
	/**
	 * Constructor
	 * @param args command line args
	 */
	public FFTAppClient(String[] args) throws IOException{
		
		// Create the configuration
		this.conf = new YarnConfiguration();
		
		// Start the YarnClient service
		this.yarnClient = YarnClient.createYarnClient();
		this.yarnClient.init(conf);
		this.yarnClient.start();
		
		// Configure filesystem
		this.fs = FileSystem.get(conf);
	}
	
	/**
	 * Launch the application
	 * @return true if the application ran, exception otherwise
	 * @throws IOException 
	 * @throws YarnException 
	 */
	public boolean run() throws YarnException, IOException {
		
		// register self with Resource Manager
		GetNewApplicationResponse appResponse = 
				this.yarnClient.createApplication().getNewApplicationResponse();
		this.appId = appResponse.getApplicationId();
				
		// create container launch context
		ContainerLaunchContext amClc = 
				Records.newRecord(ContainerLaunchContext.class);
				
		
		// 1. local resources
		LOG.info("Creating Local Resoucre: {}", APP_JAR);
		Map<String, LocalResource> localResources = 
				new HashMap<String, LocalResource>();
		
		// add jar to HDFS
		LOG.info("Uploading application jar file to HDFS");
		Path src = new Path(APP_JAR);
		String pathSuffix = APP_NAME + "/" + this.appId.getId() + "/app.jar";
		Path dst = new Path(this.fs.getHomeDirectory(), pathSuffix);
		fs.copyFromLocalFile(false, true, src, dst);
		FileStatus destStatus = fs.getFileStatus(dst);
		LOG.info("Application jar upload complete");
		
		// create local resource
		LocalResource jarResource = Records.newRecord(LocalResource.class);
		jarResource.setResource(ConverterUtils.getYarnUrlFromPath(dst));
		jarResource.setSize(destStatus.getBlockSize());
		jarResource.setTimestamp(destStatus.getModificationTime());
		jarResource.setType(LocalResourceType.FILE);
		jarResource.setVisibility(LocalResourceVisibility.APPLICATION);
		
		localResources.put("app.jar", jarResource);
		
		
		// 2. environment
		LOG.info("Configuring environment variables");
		Map<String, String> env = new HashMap<String, String>();
		
		// jar variables
		env.put("AMJARTIMESTAMP", Long.toString(jarResource.getTimestamp()));
		env.put("AMJARLEN", Long.toString(jarResource.getSize()));
		env.put("APPJARDST", dst.toUri().toString());
		
		// classpath
		StringBuilder classPathEnv = new StringBuilder();
		classPathEnv.append(File.pathSeparatorChar).append("./app.jar");
		for(String c : conf.getStrings(
				YarnConfiguration.YARN_APPLICATION_CLASSPATH,
				YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)
		){
			classPathEnv.append(File.pathSeparatorChar);
			classPathEnv.append(c.trim());
		}
		classPathEnv.append(File.pathSeparatorChar);
		classPathEnv.append(Environment.CLASSPATH.$());
		env.put("CLASSPATH", classPathEnv.toString());
		
		
		// 3. Commands
		LOG.info("Configuring launch commands");
		Vector<CharSequence> vargs = new Vector<CharSequence>(30);
		vargs.add(Environment.JAVA_HOME.$() + "/bin/java");
		vargs.add("ca.albertlockett.yarn.FFTAppMaster");
		vargs.add("1><LOG_DIR>/FFTAppClient.stdout");
		vargs.add("2><LOG_DIR>/FFTAppClient.stderr");
		StringBuilder command = new StringBuilder();
		for(CharSequence c : vargs){
			command.append(c);
			command.append(" ");
		}
		List<String> commands = new ArrayList<String>();
		commands.add(command.toString());
		
		
		// add components to CLC
		amClc.setLocalResources(localResources);
		amClc.setEnvironment(env);
		amClc.setCommands(commands);
		
		
		// Specify resources required for App Master container
		LOG.info("Creating Application Master resource request");
		Resource amResources = Records.newRecord(Resource.class);
		amResources.setMemory(512);
		
		// create the application submission context
		YarnClientApplication clientApp = this.yarnClient.createApplication();
		ApplicationSubmissionContext appSubmissionContext = 
				clientApp.getApplicationSubmissionContext();
		appSubmissionContext.setApplicationName(APP_NAME);
		appSubmissionContext.setAMContainerSpec(amClc);
		appSubmissionContext.setResource(amResources);
		
		// Submit application
		LOG.info("Submitting application master");
		yarnClient.submitApplication(appSubmissionContext);
		
		return true;
	}
	
	/**
	 * Launches the application
	 * @param args command line args
	 */
	public static void main(String[] args){
		
		FFTAppClient appClient;
		
		try {
			appClient = new FFTAppClient(args);
			boolean result = appClient.run();
			
			if (result == true){
				// Done to avoid compiler warning
				LOG.info("Application sucessfully submitted");
			}
			
		} catch (YarnException e) {
			LOG.error("SUCCESS: YarnException caught in main");
			LOG.error(e.getMessage());
			e.printStackTrace();
		
		} catch (IOException e) {
			LOG.error("SUCESS: IOException caught in main");
			LOG.error(e.getMessage());
			e.printStackTrace();
			
		}
		
	}
	
}
