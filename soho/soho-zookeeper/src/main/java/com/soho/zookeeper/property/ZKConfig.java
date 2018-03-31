package com.soho.zookeeper.property;

/**
 * ZooKeeper配置链接属性对象信息
 * 
 * @author shadow
 * 
 */
public class ZKConfig {

	private String connectionUrl;
	private String aclAuth;
	private int sleepTimeMs;
	private int maxRetries;
	private int connectionTimeoutMs;
	private int sessionTimeoutMs;
	private String version;
	private String rootNode;
	private String groupNodes;
	private String enable;

	public ZKConfig() {
		maxRetries = 3;
		sessionTimeoutMs = 100000;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getAclAuth() {
		return aclAuth;
	}

	public void setAclAuth(String aclAuth) {
		this.aclAuth = aclAuth;
	}

	public int getSleepTimeMs() {
		return sleepTimeMs;
	}

	public void setSleepTimeMs(int sleepTimeMs) {
		this.sleepTimeMs = sleepTimeMs;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getConnectionTimeoutMs() {
		return connectionTimeoutMs;
	}

	public void setConnectionTimeoutMs(int connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
	}

	public int getSessionTimeoutMs() {
		return sessionTimeoutMs;
	}

	public void setSessionTimeoutMs(int sessionTimeoutMs) {
		this.sessionTimeoutMs = sessionTimeoutMs;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRootNode() {
		return rootNode;
	}

	public void setRootNode(String rootNode) {
		this.rootNode = rootNode;
	}

	public String getGroupNodes() {
		return groupNodes;
	}

	public void setGroupNodes(String groupNodes) {
		this.groupNodes = groupNodes;
	}

}
