# rundeck-simple-slack-notification
Simple Slack notification plugin for Rundeck.

![](https://gyazo.wanko.cc/fa3b95df571f1cc66bd16bb5586e1a04.png)

## Installation
```sh
cp src/SimpleSlackNotification.groovy $RDECK_BASE/libext/
```

## Configuration
Edit `$RDECK_BASE/projects/${project}/etc/project.properties`

```
project.plugin.Notification.SimpleSlackNotification.webhook_url=https://hooks.slack.com/services/...
```
