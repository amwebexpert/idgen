import org.springframework.web.client.RestTemplate
import java.net.URL
import kotlin.concurrent.thread

/**
 * IMPORTANT
 *
 * You must start the server before running this test :-)
 */
fun main() {
    val uri = URL("https://script.google.com/macros/s/AKfycbwogqvd7ohy7iNheL_vynArlLlBfy2V8fySk0iPXNN3tJ0dgWM/exec")
    // return url.openStream().reader().readText()

    val namespaces = arrayListOf("MyNamespace1", "AnotherNamespace", "ThisOneIsSimpler", "NS")
    val restTemplate = RestTemplate()

    val threads = mutableSetOf<Thread>()
    val sb = StringBuffer()
    val fullTestStartedAt = System.currentTimeMillis()

    for (i in 1..10) { // More than 10 causes problems (even 10 is too much :-/)
        val thread = thread(start = true) {
            val namespace = namespaces.shuffled().first()
            val url = "$uri?namespaceName=$namespace"

            val startedAt = System.currentTimeMillis()
            val result = restTemplate.getForObject(url, String::class.java)
            val duration = System.currentTimeMillis() - startedAt

            sb.append("${Thread.currentThread().name} \t Call duration: $duration ms ==> $result \n")
        }

        threads.add(thread)
    }

    // Wait for all threads to complete
    threads.forEach { it.join() }

    // Compute some stats
    val totalTime = System.currentTimeMillis() - fullTestStartedAt
    val threadsCount = threads.size

    println("-".repeat(100))
    println(sb)
    println("$threadsCount calls executed in $totalTime ms which means ${totalTime / threadsCount} ms per call")
    println("-".repeat(100))
}
