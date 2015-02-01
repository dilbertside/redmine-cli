package org.bumishakti.shell.commands;

import java.util.ArrayList;
import java.util.List;

import org.bumishakti.shell.RedmineServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.User;

/**
 * @author dbs
 *
 */
@Component
public class RedmineUserCommands implements CommandMarker{

	@Autowired RedmineServerManager redmineManager;
	
	@CliAvailabilityIndicator({"usr list", "usr create", "usr update", "usr delete"})
	public boolean isIssueAvailable() {
		return redmineManager.isRedmineConnected();
	}

	@CliCommand(value = "usr list", help = "List all Redmine users")
	public String list(
			@CliOption(key = { "user" }, mandatory = false, help = "User name to search, by login, first name or last name, or id or email") final String userName){
		StringBuffer buf = new StringBuffer();
		try {
			if(!isIssueAvailable())
				throw new Exception(String.format("please connect to Redmine instance first"));
			List<User> users, usersDB = redmineManager.getUsers();
			if(null != userName){
				users = new ArrayList<>();
				for (User u : usersDB) {
					if( (u.getLogin().toLowerCase().contains(userName.toLowerCase()))
							|| (u.getMail().toLowerCase().contains(userName.toLowerCase())) 
							|| (u.getFirstName().toLowerCase().contains(userName.toLowerCase()))
							|| (u.getLastName().toLowerCase().contains(userName.toLowerCase()))
							|| (u.getId().toString().equals(userName))){
						users.add(u);
					}
				}
				if(users.isEmpty())
					return String.format("%s [%s] not found", Constants.REDMINE_USER, userName);
			}else
				users = usersDB;
			buf.append(String.format("%d %s(s):", users.size(), Constants.REDMINE_USER)).append("\n");
			for (User u : users) {
				buf.append("\n").append(printUserInfo(u)).append("\n");
			}
			buf.append("\n").append("Number of ").append(Constants.REDMINE_USER).append(": ").append(users.size());
		} catch (Exception e) {
			buf.append("\n").append(e.getMessage());
		}
		return buf.toString();
	}

	private String printUserInfo(User u) {
		StringBuffer buf = new StringBuffer();
		buf.append(Constants.REDMINE_USER).append(" [").append(u.getFullName()).append("]")
		.append(", Login: [").append(u.getLogin()).append("]")
		.append(", Id: [").append(u.getId()).append("]")
		.append(", Mail: [").append(u.getMail()).append("]")
		.append(", Last Login On: [").append(u.getLastLoginOn())
		.append("\n\tGroups:\n");
		for (Group g : u.getGroups()) {
			buf.append("\tgroup: ").append(g.getName()).append("\n");
		}
		buf.append("\n\tMemberships:\n");
		for (Membership m : u.getMemberships()) {
			buf.append("\tmembership: ").append(m.getProject().getName()).append("\n");
			buf.append("\n\t\tRoles:\n");
			for (Role r : m.getRoles()) {
				buf.append("\t\trole: ").append(r.getName()).append("\n");
			}
		}
		return buf.toString();
	}
}
