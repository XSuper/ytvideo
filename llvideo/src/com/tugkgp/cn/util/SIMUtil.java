package com.tugkgp.cn.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SIMUtil {

	/**
	 * 得到运营商 1移动2联通3电信
	 * 
	 * @param context
	 * @return
	 */
	public static int getProviders(Context context) {
		int type = 1;// 先默认为移动
		String net = getNetWork(context);
		List<String> infos = getNetWorkList(context);
		if (net == null || net.equals("WIFI")) {
			if (infos.size() > 1) {
				infos.remove("WIFI");
				net = infos.get(0);
				if (net.equals("3gwap") || net.equals("uniwap")
						|| net.equals("3gnet") || net.equals("uninet")) {
					type = 2;
				} else if (net.equals("cmnet") || net.equals("cmwap")) {
					type = 1;
				} else if (net.equals("ctnet") || net.equals("ctwap")) {
					type = 3;
				}
			} else {
				type = getProvidersName(context);
			}
		} else {
			if (net.equals("3gwap") || net.equals("uniwap")
					|| net.equals("3gnet") || net.equals("uninet")) {
				type = 2;
			} else if (net.equals("cmnet") || net.equals("cmwap")) {
				type = 1;
			} else if (net.equals("ctnet") || net.equals("ctwap")) {
				type = 3;
			}
		}
		return type;
	}

	/**
	 * 得到运营商 适用于单卡情况 1移动 2联通 3电信
	 * 
	 * @param context
	 * @return
	 */
	public static int getProvidersName(Context context) {
		int ProvidersName = 0;
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String operator = telephonyManager.getSimOperator();
			if (operator == null || operator.equals("")) {
				operator = telephonyManager.getSubscriberId();
			}
			if (operator == null || operator.equals("")) {
				Log.e("SIM", "未检测到sim卡信息!");
			}
			if (operator != null) {
				if (operator.startsWith("46000")
						|| operator.startsWith("46002")) {
					ProvidersName = 1;
				} else if (operator.startsWith("46001")) {
					ProvidersName = 2;
				} else if (operator.startsWith("46003")) {
					ProvidersName = 3;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProvidersName;
	}

	/**
	 * 得到网络列表
	 * 
	 * @param context
	 * @return
	 */
	public static List<String> getNetWorkList(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infos = cm.getAllNetworkInfo();
		List<String> list = new ArrayList<String>();
		if (infos != null) {
			for (int i = 0; i < infos.length; i++) {
				NetworkInfo info = infos[i];
				String name = null;
				if (info.getTypeName().equals("WIFI")) {
					name = info.getTypeName();
				} else {
					name = info.getExtraInfo();
				}
				if (name != null && list.contains(name) == false) {
					list.add(name);
				}
			}
		}
		return list;
	}

	public static String getNetWork(Context context) {
		String NOWNET = null;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			if (info.getTypeName().equals("WIFI")) {
				NOWNET = info.getTypeName();
			} else {
				NOWNET = info.getExtraInfo();// cmwap/cmnet/wifi/uniwap/uninet
			}
		}
		return NOWNET;
	}

	/**
	 * 获取ip
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		String ipaddress = "";

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()&& (inetAddress instanceof Inet4Address)) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return ipaddress;
	}

}
