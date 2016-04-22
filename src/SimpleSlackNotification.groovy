import com.dtolabs.rundeck.plugins.notification.NotificationPlugin
import groovy.json.JsonBuilder

def notify(Map configuration, String message) {
    String[] channels = configuration.channels.trim().split(/\s+/)
    channels.each { ch ->
        URL url = new URL(configuration.webhook_url)
        HttpURLConnection conn = url.openConnection()
        conn.setRequestMethod("POST")
        conn.addRequestProperty("Content-Type", "application/json")
        JsonBuilder builder = new JsonBuilder()
        builder {
            channel ch
            text message
            username configuration.username
        }
        conn.doOutput = true
        Writer writer = new OutputStreamWriter(conn.outputStream)
        builder.writeTo(writer)
        writer.flush()
        writer.close()
        conn.connect()

        int code = conn.responseCode
        println("Sent to Slack ${ch}: code=${code}")
    }
}

rundeckPlugin(NotificationPlugin) {
    title = "SimpleSlack"
    description = "Slack notification via incoming webhook"
    configuration {
        webhook_url title: "Webhook URL", description: "Incoming Webhook URL", scope: "Project", required: true
        channels title: "Channels", description: "Channels (space-separated)"
        username title: "Username", description: "Username"
    }

    onstart { Map execution, Map configuration ->
        notify(configuration, "${execution.user} *started* job <${execution.href}|${execution.job.name}>")
        true
    }

    onsuccess { Map execution, Map configuration ->
        notify(configuration, "<${execution.href}|${execution.job.name}> *succeeded* :sunny:")
        true
    }

    onfailure { Map execution, Map configuration ->
        notify(configuration, "<${execution.href}|${execution.job.name}> *failed* :umbrella:")
        true
    }
}

// vim: set et sw=4 sts=4:
