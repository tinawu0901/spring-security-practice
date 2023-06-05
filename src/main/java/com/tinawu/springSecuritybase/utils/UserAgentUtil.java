package com.tinawu.springSecuritybase.utils;

import eu.bitwalker.useragentutils.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class  UserAgentUtil {
    public static String composeDeviceContent(final String userAgentStr) {
        String device = userAgentStr;
        final UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
        // 裝置類型
        final DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
        if (!"Unknown".equals(deviceType.getName())) {
            final Browser browser = userAgent.getBrowser();
            // 瀏覽器名稱
            final String browserName = browser.getName();
            // 瀏覽器版本
            final Version browserVersion = userAgent.getBrowserVersion();
            // 作業系統
            final OperatingSystem os = userAgent.getOperatingSystem();
            device = deviceType + "," + os + " / " + browserName + "-" + browserVersion;
        }
        return device;
    }
    
    /**
     * 取得使用者真實IP位置
     * 
     * @param request 使用者請求
     * @return 使用者真實IP位置, String型別
     */
    public static String getIpAddr(HttpServletRequest request) {
    	
        String ip = request.getHeader("x-forwarded-for");
        System.out.println(ip);
		final String unknown = "unknown";
		if (!StringUtils.hasText(ip) || unknown.equalsIgnoreCase(ip)) {//自符串与指定的对象比较，不考虑大小写。	
		    ip = request.getHeader("Proxy-Client-IP");
		}
		if (!StringUtils.hasText(ip) || unknown.equalsIgnoreCase(ip)) {
		    ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!StringUtils.hasText(ip) || unknown.equalsIgnoreCase(ip)) {
		    ip = request.getRemoteAddr();
		}
		return ip;
    }
}
