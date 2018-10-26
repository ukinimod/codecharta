# SonarImporter

Generates visualisation data from SonarQube data through an API call to a SonarQube server.

## Usage

The command

> ccsh sonarimport \<url of server> \<projectBuilder id>

prints the visualisation data to stdout (or a file if option `-o <filename>` is given).

SonarImporter ignores the multi-module structure of sonar projects if the toggle `--merge-modules` is set.   

Further usage information may be retrieved through

> ccsh sonarimport -h
