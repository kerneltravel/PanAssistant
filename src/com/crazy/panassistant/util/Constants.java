package com.crazy.panassistant.util;

/**
 * 常用的一些常量字符串
 * 
 * @author crazy_yang
 * @version 1.0.0
 */
public class Constants {

	public final static String mbRootPath = "/";

	// api key(来至百度应用管理)
	public static final String API_KEY = "Yh0qUTNIwqYjWtYInh1fFWft";

	// secret key(来至百度应用管理)
	public static final String SECRET_KEY = "LdvIwQRSlZkGb11pI6KB2V2st5KGUgG8";

	// 获取当前用户空间配额信息基础URL
	public static final String SPACE = "https://pcs.baidu.com/rest/2.0/pcs/quota";

	// 上传单个文件以及分片上传
	public static final String UPLOAD_SINGLE_FILE = "https://c.pcs.baidu.com/rest/2.0/pcs/file";

	// 下载单个文件
	public static final String DOWNLOAD_SINGLE_FILE = "https://d.pcs.baidu.com/rest/2.0/pcs/file";

	public String getAPI_KEY() {
		return API_KEY;
	}

	public String getSECRET_KEY() {
		return SECRET_KEY;
	}

}
