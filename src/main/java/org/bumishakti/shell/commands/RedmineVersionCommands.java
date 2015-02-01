package org.bumishakti.shell.commands;

import java.util.Date;
import java.util.List;

import org.bumishakti.shell.RedmineServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Version;

/**
 * @author dbs
 *
 */
@Component
public class RedmineVersionCommands implements CommandMarker{

	
	@Autowired RedmineServerManager redmineManager;
	
	@CliAvailabilityIndicator({"tver list", "tver create", "tver update", "tver delete"})
	public boolean isVersionAvailable() {
		return redmineManager.isRedmineConnected();
	}
	
	@CliCommand(value = "tver create", help = "Create a Redmine target version")
	public String create(
			@CliOption(key = { "name" }, mandatory = true, help = "Version name") final String name,
			@CliOption(key = { "project" }, mandatory = false, help = "Project or Domain name", unspecifiedDefaultValue="test", specifiedDefaultValue="test") final String projectName,
			@CliOption(key = { "description" }, mandatory = false, help = "Version description", unspecifiedDefaultValue="no description", specifiedDefaultValue="no description") final String description,
			@CliOption(key = { "status" }, mandatory = false, help = "Version status with open,locked,closed", unspecifiedDefaultValue="open", specifiedDefaultValue="open") final String status,
			@CliOption(key = { "sharing" }, mandatory = false, help = "Version sharing with none,descendants,hierarchy,tree,system", unspecifiedDefaultValue="none", specifiedDefaultValue="none") final String sharing){
		Version versionDb;
		try {
			if(!isVersionAvailable())
				throw new Exception(String.format("please connect to redmine instance first"));
			List<Project> projects = redmineManager.getProjects();
			Project project = null;
			for (Project prj : projects) {
				if(prj.getName().equalsIgnoreCase(projectName)){
					project = prj;
					break;
				}
			}
			Version version = new Version(project, name);
			if(null == project)
				return String.format("%s [%s] not found", Constants.REDMINE_PROJECT, projectName);
			
			 List<Version> versions = redmineManager.getVersions(project.getId());
			 for (Version v : versions) {
				if(v.getName().equalsIgnoreCase(name)){
					return String.format("%s already [%s] created, nothing to do", Constants.REDMINE_VERSION, name);
				}
			 }
			
			version.setDescription(description);
			//version.setDueDate(null);
			version.setSharing(sharing);
			version.setStatus(status);
			version.setUpdatedOn(new Date());
			versionDb = redmineManager.createVersion(version);
		} catch (Exception e) {
			return String.format("Error [%s]", e.getMessage());
		}
		if(null == versionDb)
			return String.format("%s [%s] not created", Constants.REDMINE_VERSION, name);
		return String.format("%s [%s] created", Constants.REDMINE_VERSION, name);
	}

	@CliCommand(value = "tver update", help = "Update a Redmine target version")
	public String update(
			@CliOption(key = { "name" }, mandatory = true, help = "Version name") final String name,
			@CliOption(key = { "project" }, mandatory = false, help = "Project or Domain name", unspecifiedDefaultValue="test", specifiedDefaultValue="test") final String projectName,
			@CliOption(key = { "description" }, mandatory = false, help = "Version description", unspecifiedDefaultValue="no description") final String description,
			@CliOption(key = { "status" }, mandatory = false, help = "Version status with open,locked,closed") final String status,
			@CliOption(key = { "sharing" }, mandatory = false, help = "Version sharing with none,descendants,hierarchy,tree,system") final String sharing){
		try {
			if(!isVersionAvailable())
				throw new Exception(String.format("please connect to redmine instance first"));
			List<Project> projects = redmineManager.getProjects();
			Project project = null;
			for (Project prj : projects) {
				if(prj.getName().equalsIgnoreCase(projectName)){
					project = prj;
					break;
				}
			}
			Version version = null;
			if(null == project)
				return String.format("Project [%s] not found", projectName);
			
			 List<Version> versions = redmineManager.getVersions(project.getId());
			 for (Version v : versions) {
				if(v.getName().equalsIgnoreCase(name)){
					version = v;
					break;
				}
			 }
			 if(null == version)
					return String.format("%s [%s] for Domain [%s] not found created, nothing to do", Constants.REDMINE_VERSION, name, projectName);
			if(null != description)
				version.setDescription(description);
			if(null != description)
				version.setDueDate(null);
			if(null != sharing)
				version.setSharing(sharing);
			if(null != status)
				version.setStatus(status);
			version.setUpdatedOn(new Date());
			redmineManager.update(version);
		} catch (Exception e) {
			return String.format("Error [%s]", e.getMessage());
		}
		return String.format("Version [%s] updated", name);
	}
	
	@CliCommand(value = "tver delete", help = "Delete a Redmine target version")
	public String delete(
			@CliOption(key = { "name" }, mandatory = true, help = "Version name") final String name,
			@CliOption(key = { "project" }, mandatory = false, help = "Project or Domain name", unspecifiedDefaultValue="test", specifiedDefaultValue="test") final String projectName){
		try {
			if(!isVersionAvailable())
				throw new Exception(String.format("please connect to redmine instance first"));
			List<Project> projects = redmineManager.getProjects();
			Project project = null;
			for (Project prj : projects) {
				if(prj.getName().equalsIgnoreCase(projectName)){
					project = prj;
					break;
				}
			}
			Version version = null;
			if(null == project)
				return String.format("%s [%s] not found", Constants.REDMINE_PROJECT, projectName);
			
			 List<Version> versions = redmineManager.getVersions(project.getId());
			 for (Version v : versions) {
				if(v.getName().equalsIgnoreCase(name)){
					version = v;
					break;
				}
			 }
			if(null == version)
				return String.format("%s [%s] for %s [%s] not found created, nothing to do", Constants.REDMINE_VERSION, Constants.REDMINE_PROJECT, name, projectName);
			redmineManager.deleteVersion(version);
		} catch (Exception e) {
			return String.format("Error [%s]", e.getMessage());
		}
		return String.format("Version [%s] deleted", name);
	}
	
	@CliCommand(value = "tver list", help = "List all Redmine target versions for a project")
	public String list(
			@CliOption(key = { "project" }, mandatory = false, help = "Project or Domain name", unspecifiedDefaultValue="test", specifiedDefaultValue="test") final String projectName){
		StringBuffer buf = new StringBuffer(String.format("%s:",Constants.REDMINE_PROJECT)).append("\n");
		try {
			if(!isVersionAvailable())
				throw new Exception(String.format("please connect to redmine instance first"));
			List<Project> projects = redmineManager.getProjects();
			Project project = null;
			for (Project prj : projects) {
				if(prj.getName().equalsIgnoreCase(projectName)){
					project = prj;
					break;
				}
			}
			if(null == project)
				return String.format("%s [%s] not found", Constants.REDMINE_PROJECT, projectName);
			List<Version> versions = redmineManager.getVersions(project.getId());
			for (Version v : versions) {
				buf.append("\n")
				.append(Constants.REDMINE_VERSION).append(" [").append(v.getName()).append("]")
				.append(", status: [").append(v.getStatus()).append("]")
				.append(", sharing: [").append(v.getSharing()).append("]")
				.append("\n");
			}
			buf.append("\n").append("Number of ").append(Constants.REDMINE_VERSION).append(": ").append(versions.size());
		} catch (Exception e) {
			buf.append("\n").append(e.getMessage());
		}
		return buf.toString();
	}
}
