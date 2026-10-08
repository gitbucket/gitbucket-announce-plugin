# gitbucket-announce-plugin

This plugin enhances [GitBucket](https://github.com/gitbucket/gitbucket) by providing an announce mechanism.

## Features

### Global Announce

This feature allows to notify GitBucket users. It is available to _admin_ users via  the `System Administration` menu of GitBucket.

The global announce sends an email to all active users. For that, notifications and thus SMTP settings of the global GitBucket configuration have to be correctly filled in the `System Administration\System Settings\Notification email` section.

It is then possible, for example, to notify every user that an upgrade of the system is planned the next Monday at midday.

## Compatibility

Plugin version | GitBucket version
:--------------|:-----------------
1.15.x         | 4.48.x
1.14.x         | 4.35.x
1.13.x         | 4.33.x
1.12.x         | 4.32.x
1.11.x         | 4.26.x
1.10.x         | 4.21.x -> 4.25.x
1.9.x          | 4.19.x, 4.20.x
1.8.x          | 4.17.x, 4.18.x
1.7.x          | 4.16.x
1.6.x          | 4.10.x
1.5.x          | 4.3.x
1.4.x          | 4.0.0 -> 4.2.x
1.3.x          | 3.11.x, 3.12.x
1.2.x          | 3.11.x
1.1.x          | 3.7.x -> 3.10.x
1.0.x          | 3.6.x


## Usage

- Open a shell window at the root of the project, hit `sbt assembly`
- if you update `gitbucket-announce-plugin`, remove any existing previous copy of `gitbucket-announce-plugin` from GITBUCKET_HOME/plugins
- Copy target/scala-2.12/gitbucket-announce-plugin-assembly-X.X.X.jar into GITBUCKET_HOME/plugins
- Restart GitBucket

## Release Notes

### 1.15.0

- update to gitbucket 4.48.0 (`adminOnly` with a form became `adminOnlyWithForm`); requires GitBucket 4.48.0 or later
- [pullrequest-66](https://github.com/gitbucket/gitbucket-announce-plugin/pull/66) validate email addresses via `InternetAddress` instead of a hand-rolled regex

### 1.14.0

- [pullrequest-20](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/pull/21) update to gitbucket 4.35.0


### 1.13.0

- [pullrequest-19](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/pull/19) Support for extra mail address


### 1.12.0

- [pullrequest-18](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/pull/18) update to gitbucket 4.23.0 and Scala 2.13.0

### 1.11.0

- Bump sbt-gitbucket-plugin to 1.3.0 to be hosted by the [plugin registry](https://plugins.gitbucket-community.org/)

### 1.10.0

- update to gitbucket 4.22.0

### 1.9.0
- [pullrequest-16](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/pull/16) Bump to Scalatra 2.6 & Scala 2.12.4

### 1.8.0
- [issue-15](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/15) update to gitbucket 4.17

### 1.7.1
- fix database support problem when gitbucket works with other than H2

### 1.7.0
- update to gitbucket 4.16

### 1.6.0
- [issue-13](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/13) update to gitbucket 4.10

### 1.5.0
- [issue-12](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/12) adapt to new gitbucket version handling

### 1.4.0
- [issue-11](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/11) enhance plugin compatibility, runs with gitbucket 4.0.0

### 1.3
- [issue-10](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/10) allow sending to partial list of receivers, correct exception handling on email failure

### 1.2
- [issue-9](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/9) make the plugin compatible with gitbucket 3.11

### 1.1

- [issue-5](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/issues/5) use new `SystemSettings.useSMTP` property introduced in gitbucket 3.7
- [pullrequest-7](https://github.com/gitbucket-plugins/gitbucket-announce-plugin/pull/7) adapt markdown parser

### 1.0

- introduce gitbucket-announce-plugin
- global announce by email in `System Administration` menu
- depends on [gitbucket/gitbucket#861](https://github.com/gitbucket/gitbucket/pull/861)
