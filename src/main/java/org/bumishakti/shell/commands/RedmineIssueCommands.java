package org.bumishakti.shell.commands;

import java.util.HashMap;
import java.util.List;

import org.bumishakti.shell.RedmineServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.User;

/**
 * @author dbs
 *
 */
@Component
public class RedmineIssueCommands implements CommandMarker{

	@Autowired RedmineServerManager redmineManager;
	
	@CliAvailabilityIndicator({"iss list", "iss create", "iss update", "iss delete"})
	public boolean isIssueAvailable() {
		return redmineManager.isRedmineConnected();
	}

	@CliCommand(value = "iss list", help = "List all Redmine issues for a project")
	public String list(
			@CliOption(key = { "project" }, mandatory = false, help = "Project name", unspecifiedDefaultValue="test", specifiedDefaultValue="test") final String projectName){
		StringBuffer buf = new StringBuffer();
		try {
			if(!isIssueAvailable())
				throw new Exception(String.format("please connect to Redmine instance first"));
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
			HashMap<String, String> pParameters = new HashMap<String, String>();
			pParameters.put("status_id", "o");
			pParameters.put("project_id", project.getId().toString());
			List<Issue> issues = redmineManager.getIssues(pParameters);
			buf.append(String.format("%d %s(s):", issues.size(), Constants.REDMINE_ISSUE)).append("\n");
			for (Issue v : issues) {
				buf.append("\n")
				.append(Constants.REDMINE_ISSUE).append(" [").append(v.getStatusName()).append("]")
				.append(", subject: [").append(v.getSubject()).append("]")
				.append(", Assignee: [").append(v.getAssignee() != null ? v.getAssignee().getFullName() : Constants.REDMINE_NO_ASSIGNEE).append("]")
				.append("\n");
			}
			buf.append("\n").append("Number of ").append(Constants.REDMINE_ISSUE).append(": ").append(issues.size());
		} catch (Exception e) {
			buf.append("\n").append(e.getMessage());
		}
		return buf.toString();
	}
	
	@CliCommand(value = "iss author", help = "change issue author")
	public String changeAuthor(
			@CliOption(key = { "issue" }, mandatory = true, help = "issue id (number)") final String issueId,
			@CliOption(key = { "author" }, mandatory = true, help = "author id to change") final String strAuthor){
		StringBuffer buf = new StringBuffer("Search for ").append(issueId).append("\n");
		try {
			Integer is = NumberUtils.parseNumber(issueId, Integer.class);
			Issue issue = redmineManager.getIssueById(is, INCLUDE.journals);
			Integer authorId = NumberUtils.parseNumber(strAuthor, Integer.class);
			User user = redmineManager.getUserById(authorId);
			if(null != issue && null != user){
				issue.setAuthor(user);
				redmineManager.update(issue);
				buf.append("user: ").append(user.getFullName()).append(", ticket: [").append(issue.getSubject()).append("], status").append(issue.getStatusName());
			}
			else
				buf.append(Constants.REDMINE_ISSUE).append(" or user NOT found");
		} catch (RedmineException e) {
			buf.append("\n").append(e.getMessage());
		}
		return buf.toString();
	}
	
	@CliCommand(value = "iss find", help = "find an issue ticket")
	public String find(
			@CliOption(key = { "issue" }, mandatory = true, help = "issue id (number)") final String issueId){
		StringBuffer buf = new StringBuffer("Search for ").append(issueId).append("\n");
		try {
			Integer is = NumberUtils.parseNumber(issueId, Integer.class);
			Issue issue = redmineManager.getIssueById(is, INCLUDE.journals, INCLUDE.watchers, INCLUDE.relations);
			if(null != issue){
				buf.append("subject: [").append(issue.getSubject()).append("]\n")
				.append("status: [").append(issue.getStatusName()).append("]\n")
				.append("description: [").append(issue.getDescription()).append("]\n")
				.append("assignee: [").append(issue.getAssignee() != null ? issue.getAssignee().getFullName() : Constants.REDMINE_NO_ASSIGNEE).append("]\n")
				.append("author: [").append(issue.getAuthor() != null ? issue.getAuthor().getFullName() : Constants.REDMINE_NO_ASSIGNEE).append("]\n")
				.append("due date: [").append(issue.getDueDate()).append("]\n")
				.append("creation date: [").append(issue.getCreatedOn()).append("]\n")
				.append(Constants.REDMINE_PROJECT).append(": [").append(issue.getProject().getName()).append("]\n")
				.append(Constants.REDMINE_VERSION).append(": [").append(issue.getTargetVersion() != null ? issue.getTargetVersion().getName() : "NO ").append(Constants.REDMINE_VERSION).append("]\n")
				.append("tracker: [").append(issue.getTracker().getName()).append("]\n")
				;
			}
			else
				buf.append(Constants.REDMINE_ISSUE).append(" or user NOT found");
		} catch (RedmineException e) {
			buf.append("\n").append(e.getMessage());
		}
		return buf.toString();
	}
}
