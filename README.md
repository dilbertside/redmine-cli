## redmine-cli
Java Redmine console helper to access remotely Redmine instance

It uses library from

[Redmine Java API](https://github.com/taskadapter/redmine-java-api#readme)

and

[Spring shell](https://github.com/spring-projects/spring-shell#readme)

##Features
* command history and command completion
* versions list
* issues list
* project list
* user list


##pre-requisites
You must have Redmine Enable REST API in Administration -> Settings -> Authentication cf http://www.redmine.org/projects/redmine/wiki/Rest_api#Authentication
and
You can find your Redmine API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.


##Get the source
```bash
git clone https://github.com/dilbertside/redmine-cli.git
```

## Build it
```bash
cd redmine-cli
mvn package
```

## Run it
```bash
java -jar target/redmineCLI-0.1.0.jar
```

In the CLI, type
```
connect --apiAccessKey <your api key> --uri https://domain.com:443
```
if successful, you should see:

Redmine user [user name] connected

Type another command:

```
proj list
iss list --project "<project name>"
```


##You are welcome to contribute to its development!





