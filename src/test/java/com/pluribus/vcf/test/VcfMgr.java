package com.pluribus.vcf.test;

import com.pluribus.vcf.helper.TestSetup;
import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import com.pluribus.vcf.helper.PageInfra;
import com.pluribus.vcf.helper.SwitchMethods;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFManagerPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VCFIaIndexPage;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

public class VcfMgr extends TestSetup{
	private VCFHomePage home1;
	private VCFManagerPage vcfMgr1;
	private VCFLoginPage login;
	private SwitchMethods cli;
	private String vcfUserName = "admin";
	
	@BeforeClass(alwaysRun = true)
	public void init() throws Exception{
		login = new VCFLoginPage(getDriver());
		home1 = new VCFHomePage(getDriver());
		vcfMgr1 = new VCFManagerPage(getDriver());
	}
	
	@Parameters({ "password" })
	@Test(groups = { "smoke", "regression" }, description = "Login to VCF as admin  and Change Password")
	public void loginAsAdmin(@Optional("test123") String password) throws Exception {
		login.firstlogin(vcfUserName, password);
		login.waitForLogoutButton();
		Thread.sleep(30000);
		login.logout();
		Thread.sleep(60000);
	}

	@Parameters({ "password" })
	@Test(groups = { "smoke", "regression" }, description = "Login to VCF as test123 After Password Change")
	public void loginAsTest123(@Optional("test123") String password) throws Exception {
		login.login(vcfUserName, password);
		login.waitForLogoutButton();
	}
	 
	@Parameters({"hostFile", "csvFile", "password","selectedPlaybook","gatewayIp","resetFabric"})
	@Test(groups={"smoke","regression"},dependsOnMethods={"loginTest123"},description="Configure playbook 1")
	public void vcfMgrConfig1(String hostFile, String csvFile, @Optional("test123") String password, String selectedPlaybook, String gatewayIp, @Optional("1")String resetFabric) throws Exception {
    	vcfMgr1.delAllSeedsVcfMgr();
		File file1 = new File(hostFile);
		if(file1.exists()) {
			if(vcfMgr1.launchZTP(hostFile,csvFile,password,selectedPlaybook,gatewayIp,Integer.parseInt(resetFabric))) {
				com.jcabi.log.Logger.info("vcfMgrconfig","Playbook "+ selectedPlaybook+" was configured successfully");
			} else {
				com.jcabi.log.Logger.error("vcfMgrconfig","Playbook "+ selectedPlaybook+ "was not configured successfully");
				throw new Exception ("Playbook "+ selectedPlaybook+ "was not configured successfully");
			}
		} else {
			com.jcabi.log.Logger.error("vcfMgrconfig", "File doesn't exist");
			throw new Exception("hostFile doesn't exist");
		}
	}
}
