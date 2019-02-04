import java.io.*;
import java.util.*;

public class FireWall {
	private List<String> data;
	public FireWall() {};
	public FireWall(String filePath) {
		data = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				data.add(line);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean accept_packet(String direction, String protocal, Integer port, String ip_address) {
		if (!FireWall.checkingParameters(direction, protocal, port, ip_address)) {
			return false;
		}
		for (String entry : data) {
			String[] segments = entry.split(",");
			String dir = segments[0];
			String prot = segments[1];
			String ports = segments[2];
			String ip = segments[3];
			String portMin = null;
			String portMax = null;
			String ipMin = null;
			String ipMax = null;
			if (!direction.equals(dir)) {
				continue;
			}
			if (!protocal.equals(prot)) {
				continue;
			}
			if (ports.indexOf("-") != -1) {
				portMin = ports.split("-")[0];
				portMax = ports.split("-")[1];
				if (port < Integer.parseInt(portMin) || port > Integer.parseInt(portMax)) {
					continue;
				}
			}
			else {
				if (port < Integer.parseInt(ports) || port > Integer.parseInt(ports)) {
					continue;
				}
			}

			if (ip.indexOf("-") != -1) {
				ipMin = ip.split("-")[0];
				ipMax = ip.split("-")[1];

				String[] minSegs = ipMin.split(".");
				String[] maxSegs = ipMax.split(".");
				String[] ipSegs = ip_address.split(".");
				for (int i = 0; i < ipSegs.length; i++) {
					if (Integer.parseInt(ipSegs[i]) < Integer.parseInt(minSegs[i]) || Integer.parseInt(ipSegs[i]) > Integer.parseInt(maxSegs[i])) {
						continue;
					}
				}
			}
			else {
				if (!ip_address.equals(ip)) {
					continue;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean checkingParameters(String direction, String protocal, Integer port, String ip_address) {
		if (direction == null || protocal == null || port == null || ip_address == null) {
			return false;
		}
		if (!direction.equals("inbound") && !direction.equals("outbound")) {
			return false;
		}
		if (!protocal.equals("tcp") && !protocal.equals("udp")) {
			return false;
		}
		if (port < 1 || port > 65535) {
			return false;
		}
		if (!FireWall.isIpLegal(ip_address)) {
			return false;
		}
		return true;
	}

	public static boolean isIpLegal(String str) {
		if (str.charAt(0) == '.' || str.charAt(str.length() - 1) == '.') {
			return false;
		}

		String[] arr = str.split("\\.");
		if (arr.length != 4) {
			return false;
		}

		for (int i = 0; i < arr.length; i++) {
			if (arr[i].length() > 1 && arr[i].charAt(0) == '0') {
				return false;
			}

			for (int j = 0; j < arr[i].length(); j++) {
				if (arr[i].charAt(j) < '0' || arr[i].charAt(j) > '9') {
					return false;
				}
			}
		}
		for (int i = 0; i < arr.length; i++) {
			int tmp = Integer.parseInt(arr[i]);
			if (i == 0) {
				if (tmp < 1 || tmp > 255) {
					return false;
				}
			}
			else {
				if (tmp < 0 || tmp > 255) {
					return false;
				}
			}
		}
		return true;
	}
	public static void main(String[] args) {
		FireWall fw = new FireWall("/Users/cpwang/desktop/test.csv");
		System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
		System.out.println(fw.accept_packet("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(fw.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(fw.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
		System.out.println(fw.accept_packet("inbound", "udp", 24, "52.12.48.92"));
	}
}