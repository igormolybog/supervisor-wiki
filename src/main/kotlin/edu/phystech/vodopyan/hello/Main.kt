package edu.phystech.vodopyan.hello

import com.zaxxer.hikari.*
import freemarker.cache.*
import org.jetbrains.ktor.application.*
import org.jetbrains.ktor.content.*
import org.jetbrains.ktor.features.*
import org.jetbrains.ktor.freemarker.*
import org.jetbrains.ktor.host.*
import org.jetbrains.ktor.http.*
import org.jetbrains.ktor.netty.*
import org.jetbrains.ktor.routing.*
import java.util.*
import kotlinx.html.stream.*
import org.jetbrains.ktor.response.*
import org.jetbrains.ktor.auth.*
import org.jetbrains.ktor.client.*
import java.util.concurrent.*

val DEBUG = if(true) System.currentTimeMillis().toString() else ""

val hikariConfig = HikariConfig().apply {
    jdbcUrl = System.getenv("JDBC_DATABASE_URL")
}

val dataSource = if (hikariConfig.jdbcUrl != null) HikariDataSource(hikariConfig) else HikariDataSource()


val html_utf8: ContentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)

fun Application.module() {
    install(DefaultHeaders)
    install(ConditionalHeaders)
    install(PartialContentSupport)

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(environment.classLoader, "templates")
    }

/*    install(StatusPages) {
        exception<Exception> { exception ->
            call.respond(FreeMarkerContent("example/error.ftl", exception, "", html_utf8))
        }
    }*/

    install(Routing) {
        serveClasspathResources("public")

        get("/") {
            call.respondRedirect("/supervisors")
        }


        get("/laboratories") {
            call.respondFreeMarker("laboratories.ftl")
        }

        route("/supervisors") {
            get("/") {
                val model = Data
                call.respondFreeMarker("supervisors.ftl", model)
            }

            for(sup in Data.supervisorsList) {
                route("/${sup.webname}") {

                    get {
                        call.respondFreeMarker("supervisor.ftl", sup)
                    }

//                    post("/comment") {
//                        println(call.request)
//                        println(call.attributes)
//                        println(call.parameters)
//                    }

                    get("/comment-form") {
                        call.respondFreeMarker("comment-form.ftl", model = sup)
                    }

                    get("/send-comment") {
                        call.respond("Спасибо за ваш комментарий!")

//                        val comment = call.parameters["comment"]!!


                        val model = HashMap<String, Any>()

                        dataSource.connection.use { connection ->
                            val rs = connection.createStatement().run {
                                executeUpdate("""INSERT INTO comments
                                                VALUES(${sup.id},
                                                '${sup.familyName}', '${sup.givenName}', '${sup.middleName}',
                                                'ИАД', 'art_intel', 'topicmap',
                                                'он кулл', 'не очень много но хватает', 'дофига всего',
                                                'задвигает телеги', 'его знают все', 'none',
                                                'Игорь', 'Молибог', 'Бакалавр', '9,2',
                                                'vk.com/igormolybog', 'igormolybog@gmail.com',
                                                'telegram', '2015-2017', 0, 0, 0);""")

                                executeQuery("SELECT tick FROM ticks")
                            }

                            val output = ArrayList<String>()
                            while (rs.next()) {
                                output.add("Read from DB: " + rs.getTimestamp("tick"))
                            }
                            model.put("results", output)
                        }

                        val etag = model.toString().hashCode().toString()
                        call.respond(FreeMarkerContent("example/db.ftl", model, etag, html_utf8))


                    }
                }
            }
        }

        route("/like") {
            get("/kotlinorg") {
                call.respondFreeMarker("example/kotlinorg.ftl")
            }

            get("flexbox") {
                call.respondFreeMarker("example/flexbox.ftl")
            }

            get("/heroku-example") {
                call.respondFreeMarker("example/herokuexample.ftl")
            }
        }

        route("/example") {
            get("hello") {
                call.respond("Hello World?")
            }

            get("error") {
                throw IllegalStateException("An invalid place to be …")
            }

            get("/index") {
                call.respondFreeMarker("example/index.ftl")
            }

            get("/db") {
                val model = HashMap<String, Any>()
                dataSource.connection.use { connection ->
                    val rs = connection.createStatement().run {
                        executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
                        executeUpdate("INSERT INTO ticks VALUES (now())")
                        executeQuery("SELECT tick FROM ticks")
                    }

                    val output = ArrayList<String>()
                    while (rs.next()) {
                        output.add("Read from DB: " + rs.getTimestamp("tick"))
                    }
                    model.put("results", output)
                }

                val etag = model.toString().hashCode().toString()
                call.respond(FreeMarkerContent("example/db.ftl", model, etag, html_utf8))
            }
        }
    }
}

suspend fun ApplicationCall.respondFreeMarker(ftl: String, model: Any = mapOf<String, Any>()) {
    val hash =  (ftl + model.toString() + DB.hashCode() + DEBUG).hashCode().toString()
    respond(FreeMarkerContent(ftl, model, hash, html_utf8))

    println(hash)
}




fun main(args: Array<String>) {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 5000
    val server = embeddedServer(Netty, port, reloadPackages = listOf("supervisor-wiki"), module = Application::module)
    server.start()
}


