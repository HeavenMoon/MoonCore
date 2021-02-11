package fr.heavenmoon.core.common.config;

public class PluginConfig
{
	private String serverName;
	
	private String serverType;
	
	private String fallback;
	
	private String serverWhitelist;
	
	public PluginConfig(String serverName, String serverType, String fallback, String serverWhitelist)
	{
		this.serverName = serverName;
		this.serverType = serverType;
		this.fallback = fallback;
		this.serverWhitelist = serverWhitelist;
	}
	
	public String getServerName()
	{
		return this.serverName;
	}
	
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	
	public String getServerType()
	{
		return this.serverType;
	}
	
	public void setServerType(String serverType)
	{
		this.serverType = serverType;
	}
	
	public String getFallback()
	{
		return this.fallback;
	}
	
	public void setFallback(String fallback)
	{
		this.fallback = fallback;
	}
	
	public String getServerWhitelist()
	{
		return this.serverWhitelist;
	}
	
	public void setServerWhitelist(String serverWhitelist)
	{
		this.serverWhitelist = serverWhitelist;
	}
	
	public boolean isValid()
	{
		return (this.serverName != null && !this.serverName.isEmpty() && this.serverType != null &&
				!this.serverType.isEmpty() && this.serverWhitelist != null && !this.serverWhitelist.isEmpty());
	}
	
	public String toString()
	{
		return "PluginConfig{serverName='" + this.serverName + "', serverType='" + this.serverType + "', fallback='" + this.fallback +
				"', serverWhitelist='" + this.serverWhitelist + "'}";
	}
}