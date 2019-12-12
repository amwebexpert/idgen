import org.springframework.web.client.RestTemplate
import kotlin.concurrent.thread


class ConcurrencyTest {

}

fun main(args: Array<String>) {
    val uri = "http://localhost:8080/api/v1/new-id/myNamespace"
    val restTemplate = RestTemplate()

    val threads = mutableSetOf<Thread>()
    val sb = StringBuffer()
    var durations = mutableListOf<Long>()

    for (i in 1..20) {
        val thread = thread(start = true) {
            val startedAt = System.currentTimeMillis()
            val result = restTemplate.getForObject(uri, String::class.java)
            val duration = System.currentTimeMillis() - startedAt

            sb.append("${Thread.currentThread()} Result: $result. Took $duration ms \n")
            durations.add(duration)
        }

        threads.add(thread)
    }

    // Wait for all threads to complete
    threads.forEach { it.join() }

    // Compute some stats
    val total = durations.reduce { x, y -> x + y }
    println("-".repeat(100))
    println(sb)
    println("Average REST call duration: ${total / threads.size}")
    println("-".repeat(100))
}
