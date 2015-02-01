package org.bumishakti.shell.commands;

import org.bumishakti.shell.RedmineServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * @author dbs
 *
 */
@Component
public class RedmineInitCommands implements CommandMarker{

	
	@Autowired RedmineServerManager redmineManager;
	
	@CliAvailabilityIndicator({"connect"})
	public boolean isAvailable() {
		//always available
		return true;
	}
	
	@CliAvailabilityIndicator({"disconnect"})
	public boolean isConnectAvailable() {
		return redmineManager.isRedmineConnected();
	}
	
	@CliCommand(value = "connect", help = "Connect to a Redmine instance")
	public String connect(
		@CliOption(key = { "uri" }, mandatory = false, help = "URL redmine instance with protocol, server, port", unspecifiedDefaultValue="http://redmine.org:80", specifiedDefaultValue="http://redmine.org:80") final String uri,
		@CliOption(key = { "objectsPerPage" }, mandatory = false, help = "number of objects (tasks, projects, users) will be requested from Redmine server in 1 request", unspecifiedDefaultValue="250", specifiedDefaultValue="250") final Integer objectsPerPage,
		@CliOption(key = { "apiAccessKey" }, mandatory = false, help = "REST API key", unspecifiedDefaultValue="d3086de8dc95f2208xx06864c278b2194d17095e", specifiedDefaultValue="hidden") final String apiAccessKey){		
		String user = "unknown";
		try {
			redmineManager.connect(uri, apiAccessKey, objectsPerPage);
			if(null == redmineManager)
				return String.format("Connection refused to [%s]",uri);
			user = redmineManager.getCurrentUser().getFullName();
		} catch (Exception e) {
			return String.format("Error %s", e.getMessage());
		}
		return String.format("Redmine user [%s] connected", user);
	}
	
	@CliCommand(value = "disconnect", help = "Disconnect this Redmine instance")
	public String disconnect(){
		try {
			if(!redmineManager.isRedmineConnected())
				throw new Exception(String.format("please connect to redmine instance first"));
			redmineManager.shutdown();
			return String.format("Redmine disconnected created");
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
}
