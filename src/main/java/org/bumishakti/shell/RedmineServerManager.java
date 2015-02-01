package org.bumishakti.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.taskadapter.redmineapi.RedmineCommunicationException;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TransportConfiguration;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Group;
import com.taskadapter.redmineapi.bean.Identifiable;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.IssuePriority;
import com.taskadapter.redmineapi.bean.IssueRelation;
import com.taskadapter.redmineapi.bean.IssueStatus;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.News;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.SavedQuery;
import com.taskadapter.redmineapi.bean.TimeEntry;
import com.taskadapter.redmineapi.bean.TimeEntryActivity;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;
import com.taskadapter.redmineapi.bean.Watcher;

/**
 * @author dbs
 *
 */
@Component
public class RedmineServerManager {
	
	private RedmineManager redmineManager;

	public RedmineServerManager() {
	}
	
	/**
	 * connect to a Redmine server instance
	 * @param uri
	 * @param apiAccessKey
	 * @param objectsPerPage
	 * @return
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 */
	public boolean connect(String uri, String apiAccessKey, int objectsPerPage) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException{
		TransportConfiguration tc = RedmineManagerFactory.createLongTermConfiguration(RedmineManagerFactory.createDefaultConnectionManager(), 60, 60);
		redmineManager = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey, tc);
		redmineManager.setObjectsPerPage(objectsPerPage);
		return isRedmineConnected();
	}
	/**
	 * @return
	 */
	public boolean isRedmineConnected() {
		return redmineManager != null;
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createIssue(java.lang.String, com.taskadapter.redmineapi.bean.Issue)
	 */
	public Issue createIssue(String projectKey, Issue issue)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createIssue(projectKey, issue);
	}

	/** 
	 * @see com.taskadapter.redmineapi.RedmineManager#getProjects()
	 */
	public List<Project> getProjects() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getProjects();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getIssuesBySummary(java.lang.String, java.lang.String)
	 */
	public List<Issue> getIssuesBySummary(String projectKey, String summaryField)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getIssuesBySummary(projectKey, summaryField);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getIssues(java.util.Map)
	 */
	public List<Issue> getIssues(Map<String, String> pParameters)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getIssues(pParameters);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getIssueById(java.lang.Integer, com.taskadapter.redmineapi.RedmineManager.INCLUDE[])
	 */
	public Issue getIssueById(Integer id, INCLUDE... include)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getIssueById(id, include);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getProjectByKey(java.lang.String)
	 */
	public Project getProjectByKey(String projectKey) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getProjectByKey(projectKey);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteProject(java.lang.String)
	 */
	public void deleteProject(String projectKey) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteProject(projectKey);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteIssue(java.lang.Integer)
	 */
	public void deleteIssue(Integer id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteIssue(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getIssues(java.lang.String, java.lang.Integer, com.taskadapter.redmineapi.RedmineManager.INCLUDE[])
	 */
	public List<Issue> getIssues(String projectKey, Integer queryId,
			INCLUDE... include) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getIssues(projectKey, queryId, include);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#update(com.taskadapter.redmineapi.bean.Identifiable)
	 */
	public void update(Identifiable obj) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.update(obj);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createProject(com.taskadapter.redmineapi.bean.Project)
	 */
	public Project createProject(Project project) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createProject(project);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getObjectsPerPage()
	 */
	public int getObjectsPerPage() throws RedmineCommunicationException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getObjectsPerPage();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#setObjectsPerPage(int)
	 */
	public void setObjectsPerPage(int pageSize) throws RedmineCommunicationException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.setObjectsPerPage(pageSize);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getUsers()
	 */
	public List<User> getUsers() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getUsers();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getUserById(java.lang.Integer)
	 */
	public User getUserById(Integer userId) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getUserById(userId);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getCurrentUser()
	 */
	public User getCurrentUser() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getCurrentUser();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createUser(com.taskadapter.redmineapi.bean.User)
	 */
	public User createUser(User user) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createUser(user);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteUser(java.lang.Integer)
	 */
	public void deleteUser(Integer userId) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteUser(userId);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getGroups()
	 */
	public List<Group> getGroups() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getGroups();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getGroupById(int)
	 */
	public Group getGroupById(int id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getGroupById(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getGroupByName(java.lang.String)
	 */
	public Group getGroupByName(String name) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getGroupByName(name);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createGroup(com.taskadapter.redmineapi.bean.Group)
	 */
	public Group createGroup(Group base) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createGroup(base);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteGroup(com.taskadapter.redmineapi.bean.Group)
	 */
	public void deleteGroup(Group base) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteGroup(base);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getTimeEntries()
	 */
	public List<TimeEntry> getTimeEntries() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getTimeEntries();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getTimeEntry(java.lang.Integer)
	 */
	public TimeEntry getTimeEntry(Integer id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getTimeEntry(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getTimeEntriesForIssue(java.lang.Integer)
	 */
	public List<TimeEntry> getTimeEntriesForIssue(Integer issueId)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getTimeEntriesForIssue(issueId);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createTimeEntry(com.taskadapter.redmineapi.bean.TimeEntry)
	 */
	public TimeEntry createTimeEntry(TimeEntry obj) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createTimeEntry(obj);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteTimeEntry(java.lang.Integer)
	 */
	public void deleteTimeEntry(Integer id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteTimeEntry(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getSavedQueries(java.lang.String)
	 */
	public List<SavedQuery> getSavedQueries(String projectKey)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getSavedQueries(projectKey);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getSavedQueries()
	 */
	public List<SavedQuery> getSavedQueries() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getSavedQueries();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createRelation(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public IssueRelation createRelation(Integer issueId, Integer issueToId,
			String type) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createRelation(issueId, issueToId, type);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteRelation(java.lang.Integer)
	 */
	public void deleteRelation(Integer id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteRelation(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteIssueRelations(com.taskadapter.redmineapi.bean.Issue)
	 */
	public void deleteIssueRelations(Issue redmineIssue)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteIssueRelations(redmineIssue);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteIssueRelationsByIssueId(java.lang.Integer)
	 */
	public void deleteIssueRelationsByIssueId(Integer id)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteIssueRelationsByIssueId(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getStatuses()
	 */
	public List<IssueStatus> getStatuses() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getStatuses();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createVersion(com.taskadapter.redmineapi.bean.Version)
	 */
	public Version createVersion(Version version) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createVersion(version);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteVersion(com.taskadapter.redmineapi.bean.Version)
	 */
	public void deleteVersion(Version version) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteVersion(version);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getVersions(int)
	 */
	public List<Version> getVersions(int projectID) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getVersions(projectID);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getVersionById(int)
	 */
	public Version getVersionById(int versionId) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getVersionById(versionId);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getCategories(int)
	 */
	public List<IssueCategory> getCategories(int projectID)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getCategories(projectID);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#createCategory(com.taskadapter.redmineapi.bean.IssueCategory)
	 */
	public IssueCategory createCategory(IssueCategory category)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.createCategory(category);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteCategory(com.taskadapter.redmineapi.bean.IssueCategory)
	 */
	public void deleteCategory(IssueCategory category) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteCategory(category);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getTrackers()
	 */
	public List<Tracker> getTrackers() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getTrackers();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getAttachmentById(int)
	 */
	public Attachment getAttachmentById(int attachmentID)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getAttachmentById(attachmentID);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#downloadAttachmentContent(com.taskadapter.redmineapi.bean.Attachment, java.io.OutputStream)
	 */
	public void downloadAttachmentContent(Attachment issueAttachment,
			OutputStream stream) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.downloadAttachmentContent(issueAttachment, stream);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#downloadAttachmentContent(com.taskadapter.redmineapi.bean.Attachment)
	 */
	public byte[] downloadAttachmentContent(Attachment issueAttachment)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.downloadAttachmentContent(issueAttachment);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#setLogin(java.lang.String)
	 */
	public void setLogin(String login) throws RedmineCommunicationException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.setLogin(login);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#setPassword(java.lang.String)
	 */
	public void setPassword(String password) throws RedmineCommunicationException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.setPassword(password);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getNews(java.lang.String)
	 */
	public List<News> getNews(String projectKey) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getNews(projectKey);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#shutdown()
	 */
	@PreDestroy
	public void shutdown() {
		if(!isRedmineConnected())
			return;
		redmineManager.shutdown();
		redmineManager = null;
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#uploadAttachment(java.lang.String, java.lang.String, byte[])
	 */
	public Attachment uploadAttachment(String fileName, String contentType,
			byte[] content) throws RedmineException, IOException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.uploadAttachment(fileName, contentType, content);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#uploadAttachment(java.lang.String, java.io.File)
	 */
	public Attachment uploadAttachment(String contentType, File content)
			throws RedmineException, IOException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.uploadAttachment(contentType, content);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#uploadAttachment(java.lang.String, java.lang.String, java.io.InputStream)
	 */
	public Attachment uploadAttachment(String fileName, String contentType,
			InputStream content) throws RedmineException, IOException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.uploadAttachment(fileName, contentType, content);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getRoles()
	 */
	public List<Role> getRoles() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getRoles();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getRoleById(int)
	 */
	public Role getRoleById(int id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getRoleById(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getIssuePriorities()
	 */
	public List<IssuePriority> getIssuePriorities() throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getIssuePriorities();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getTimeEntryActivities()
	 */
	public List<TimeEntryActivity> getTimeEntryActivities()
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getTimeEntryActivities();
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getMemberships(java.lang.String)
	 */
	public List<Membership> getMemberships(String project)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getMemberships(project);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getMemberships(com.taskadapter.redmineapi.bean.Project)
	 */
	public List<Membership> getMemberships(Project project)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getMemberships(project);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#addMembership(com.taskadapter.redmineapi.bean.Membership)
	 */
	public void addMembership(Membership membership) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.addMembership(membership);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#getMembership(int)
	 */
	public Membership getMembership(int id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		return redmineManager.getMembership(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteMembership(int)
	 */
	public void deleteMembership(int id) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteMembership(id);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#delete(com.taskadapter.redmineapi.bean.Membership)
	 */
	public void delete(Membership membership) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.delete(membership);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#addUserToGroup(com.taskadapter.redmineapi.bean.User, com.taskadapter.redmineapi.bean.Group)
	 */
	public void addUserToGroup(User user, Group group) throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.addUserToGroup(user, group);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#addWatcherToIssue(com.taskadapter.redmineapi.bean.Watcher, com.taskadapter.redmineapi.bean.Issue)
	 */
	public void addWatcherToIssue(Watcher watcher, Issue issue)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.addWatcherToIssue(watcher, issue);
	}

	/**
	 * @see com.taskadapter.redmineapi.RedmineManager#deleteWatcherFromIssue(com.taskadapter.redmineapi.bean.Watcher, com.taskadapter.redmineapi.bean.Issue)
	 */
	public void deleteWatcherFromIssue(Watcher watcher, Issue issue)
			throws RedmineException {
		if(!isRedmineConnected())
			throw new RedmineCommunicationException("Please First connect to Redmine instance");
		redmineManager.deleteWatcherFromIssue(watcher, issue);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return redmineManager.equals(obj);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return redmineManager.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if(!isRedmineConnected())
			return "Please First connect to Redmine instance";
		return redmineManager.toString();
	}

	
}
