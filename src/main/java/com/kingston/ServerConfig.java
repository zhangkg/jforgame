package com.kingston;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kingston.utils.XmlUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ServerConfig {
	
	private Logger logger = LoggerFactory.getLogger(ServerConfig.class.getSimpleName());

	private static ServerConfig instance = new ServerConfig();
	/** 服务器id */
	private int serverId;
	/** 服务器端口 */
	private int serverPort;
	/** 后台管理端口 */
	private int httpPort;
	/** 后台白名单模式 */
	private String[] whiteIpPattern;
	

	private ServerConfig() {}

	public static ServerConfig getInstance() {
		return instance;
	}

	public void initFromConfigFile() {
		String configFile = "server.xml";
		Element rootElement = XmlUtils.loadConfigRootElement(configFile);
		NodeList nodes = rootElement.getChildNodes();
		
		for (int i=0;i<nodes.getLength();i++) {
			Node node = nodes.item(i);
			if ("game-server".equals(node.getNodeName())) {
				NodeList subNodes = node.getChildNodes();
				for (int j=0;j<subNodes.getLength();j++) {
					if ("server_id".equals(subNodes.item(j).getNodeName())) {
						this.serverId = Integer.parseInt(subNodes.item(j).getTextContent());
					} else if ("server_port".equals(subNodes.item(j).getNodeName())) {
						this.serverPort = Integer.parseInt(subNodes.item(j).getTextContent());
					}
				}
			} else if ("http-server".equals(node.getNodeName())) {
				NodeList subNodes = node.getChildNodes();
				for (int j=0;j<subNodes.getLength();j++) {
					if ("http_port".equals(subNodes.item(j).getNodeName())) {
						this.httpPort = Integer.parseInt(subNodes.item(j).getTextContent());
					} else if ("white_ips".equals(subNodes.item(j).getNodeName())) {
						String[] ips = subNodes.item(j).getTextContent().split(";");
						this.whiteIpPattern = ips;
					}
				}
			}
		}
		
		logger.info("本服serverId为{},后台端口为{}", serverId, httpPort);
	}

	public int getServerId() {
		return serverId;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	public int getHttpPort() {
		return httpPort;
	}
	
	public boolean isInWhiteIps(String ip) {
		for (String pattern:this.whiteIpPattern) {
			if (ip.matches(pattern)) {
				return true;
			}
		}
		return false;
	}

}
