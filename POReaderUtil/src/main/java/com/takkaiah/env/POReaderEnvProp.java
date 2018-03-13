package com.takkaiah.env;

public interface POReaderEnvProp
{
	// Config properties file name
	String configProperties = "conf/config.properties";
	
	// App Password
	String PasswordFile = "PasswordFile";
	
	// Database properties
	String HibernateConfFile = "HibernateConfFile";
    String DBUrl = "DBUrl";
    String DBUserName = "DBUserName";
    String DBPwd = "DBPwd";
    
    //Logger configuration file name
    String LoggerFileName = "LoggerFileName";

     //Email Server Details
    String EmailHost = "EmailHost";  
    String EmailPort = "EmailPort";
    String EmailStarttls = "EmailStarttls";
    String EmailAuth = "EmailAuth";  
    String EmailUserName =  "EmailUserName";
    String EmailPwd =  "EmailPwd";
    String EmailFrom =  "EmailFrom";
    
    // SMS Configuration details
    String SmsPwd="SmsPwd";
    
    //PO Output export details
    String XMLExportFolder = "XMLExportFolder";
    String XSLXExportFolder = "XSLXExportFolder";

	// Form Logo Properties
    String logoIcon = "logoIcon";
	String logoPath = "logoPath";
	String PwdResetLogoPath = "PwdResetLogoPath";
	String UserMgmtLogoPath = "UserMgmtLogoPath";
	String UserPermissionsLogoPath ="UserPermissionsLogoPath";
	String CustomerGrpLogoPath ="CustomerGrpLogoPath";
	String ItemCatLogoPath = "ItemCatLogoPath";
	String MRPTypeLogoPath = "MRPTypeLogoPath";
	String CustomerLogoPath = "CustomerLogoPath";
	String ItemMasterLogoPath ="ItemMasterLogoPath";
	String CustArticleCodeLogoPath = "CustArticleCodeLogoPath";
	String CustItemMappingLogoPath = "CustItemMappingLogoPath";
	String POReaderLogoPath="POReaderLogoPath";
	String ManualPOCheckLogoPath = "ManualPOCheckLogoPath";
	String CustItemsReportLogoPath="CustItemsReportLogoPath";
	
	// Button Properties
	String ButtonLogoAdd = "ButtonLogoAdd";
	String ButtonLogoUpdate = "ButtonLogoUpdate";
	String ButtonLogoDelete ="ButtonLogoDelete";
	String ButtonLogoReset = "ButtonLogoReset";
	String ButtonLogoSearch ="ButtonLogoSearch";
	String ButtonLogoOpen = "ButtonLogoOpen";;
	String ButtonLogoExtract = "ButtonLogoExtract";
	String ButtonLogoExport = "ButtonLogoExport";
	String ButtonLogoValidate = "ButtonLogoValidate";
	String ButtonLogoEmail = "ButtonLogoEmail";
	String ButtonLogoResetPwd="ButtonLogoResetPwd";
	String ButtonLogoSave="ButtonLogoSave";
	String ButtonLogoLogin="ButtonLogoLogin";
	
	// Menu Properties
	String MenuLogoExit = "MenuLogoExit";
	String MenuLogoChangePwd = "MenuLogoChangePwd";
	String MenuLogoUser = "MenuLogoUser";
	String MenuLogoUserPermissions = "MenuLogoUserPermissions";
	String MenuLogoCustGroup = "MenuLogoCustGroup";
	String MenuLogoCustomer = "MenuLogoCustomer";
	String MenuLogoItem = "MenuLogoItem";
	String MenuLogoItemGroup = "MenuLogoItemGroup";
	String MenuLogoMRPType = "MenuLogoMRPType";
	String MenuLogoCustArticleCode = "MenuLogoCustArticleCode";
	String MenuLogoCustMapping = "MenuLogoCustMapping";
	String MenuLogoPoReader = "MenuLogoPoReader";
	String MenuManualPOCheck = "MenuManualPOCheck";
	String MenuLogoPoReport = "MenuLogoPoReport";
	
  
}
