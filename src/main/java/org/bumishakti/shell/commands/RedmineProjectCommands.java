package org.bumishakti.shell.commands;

import java.util.List;

import org.bumishakti.shell.RedmineServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import com.taskadapter.redmineapi.bean.Project;

/**
 * @author dbs
 *
 */
@Component
public class RedmineProjectCommands implements CommandMarker{

	@Autowired RedmineServerManager redmineManager;
	
	@CliAvailabilityIndicator({"proj list", "proj create", "proj update", "proj delete"})
	public boolean isProjectAvailable() {
		return redmineManager.isRedmineConnected();
	}

	@CliCommand(value = "proj list", help = "List all Redmine versions for a project")
	public String list(){
		StringBuffer buf = new StringBuffer(String.format("%s:",Constants.REDMINE_PROJECT)).append("\n");
		try {
			if(!isProjectAvailable())
				throw new Exception(String.format("please connect to redmine instance first"));
			List<Project> projects = redmineManager.getProjects();
			if(null == projects || projects.isEmpty())
				return String.format("No [%s] found", Constants.REDMINE_PROJECT);
			for (Project prj : projects) {
				buf.append("\n")
				.append(Constants.REDMINE_PROJECT).append(" [").append(prj.getName()).append("]")
				.append(", id: [").append(prj.getId()).append("]")
				.append(", identifier: [").append(prj.getIdentifier()).append("]")
				.append("\n");
			}
			buf.append("\n").append("Number of ").append(Constants.REDMINE_PROJECT).append(": ").append(projects.size());
		} catch (Exception e) {
			buf.append("\n").append(e.getMessage());
		}
		return buf.toString();
	}
}
