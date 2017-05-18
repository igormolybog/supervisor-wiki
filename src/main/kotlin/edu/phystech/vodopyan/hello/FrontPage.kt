package edu.phystech.vodopyan.hello

import kotlinx.html.*
import java.io.*

fun TagConsumer<Writer>.frontPage(): Writer =
        html{
            head {
                title { +"WIKI о научных руководителях ФизТеха" }

                style {
                    cssClass("""
                            .flex-container {
                                display: -webkit-flex;
                                display: flex;
                                -webkit-flex-flow: row wrap;
                                flex-flow: row wrap;
                                text-align: center;
                            } """)

                    cssClass("""
                            .flex-container > * {
                                padding: 15px;
                                -webkit-flex: 1 100%;
                                flex: 1 100%;
                            } """)

                    cssClass("""
                            .article {
                                text-align: left;
                            }""")

                    cssClass("header {background: black;color:white;}")
                    cssClass("footer {background: #aaa;color:white;}")

                    cssClass(".nav {background:#eee;}")

                    cssClass("""
                            .nav ul {
                                list-style-type: none;
                                padding: 0;
                            }
                            .nav ul a {
                                text-decoration: none;
                            }""")

                    cssClass("""
                            @media all and (min-width: 768px) {
                                .nav {text-align:left;-webkit-flex: 1 auto;flex:1 auto;-webkit-order:1;order:1;}
                                .article {-webkit-flex:5 0px;flex:5 0px;-webkit-order:2;order:2;}
                                footer {-webkit-order:3;order:3;}
                            }""")
                }
            }

            body {

                unsafe {

                    +"""
                    <div class="flex-container">
                    <header>
                      <h1>City Gallery</h1>
                    </header>

                    <nav class="nav">
                    <ul>
                      <li><a href="#">London</a></li>
                      <li><a href="#">Paris</a></li>
                      <li><a href="#">Tokyo</a></li>
                    </ul>
                    </nav>

                    <article class="article">
                      <h1>London</h1>
                      <p>London is the capital city of England. It is the most populous city in the United Kingdom,
                      with a metropolitan area of over 13 million inhabitants.</p>
                      <p>Standing on the River Thames, London has been a major settlement for two millennia,
                      its history going back to its founding by the Romans, who named it Londinium.</p>
                      <p><strong>Resize this page to see that what happens!</strong></p>
                    </article>

                    <footer>Copyright &copy; W3Schools.com</footer>
                    </div>
                    """.trimIndent()
                }
            }
        }

fun STYLE.cssClass(body: String) = unsafe {raw("\n" + body.trimIndent() + "\n")}


val ex = """
<!DOCTYPE html>
<html>
<head>
<style>
.flex-container {
    display: -webkit-flex;
    display: flex;
    -webkit-flex-flow: row wrap;
    flex-flow: row wrap;
    text-align: center;
}

.flex-container > * {
    padding: 15px;
    -webkit-flex: 1 100%;
    flex: 1 100%;
}

.article {
    text-align: left;
}

header {background: black;color:white;}
footer {background: #aaa;color:white;}
.nav {background:#eee;}

.nav ul {
    list-style-type: none;
    padding: 0;
}
.nav ul a {
    text-decoration: none;
}

@media all and (min-width: 768px) {
    .nav {text-align:left;-webkit-flex: 1 auto;flex:1 auto;-webkit-order:1;order:1;}
    .article {-webkit-flex:5 0px;flex:5 0px;-webkit-order:2;order:2;}
    footer {-webkit-order:3;order:3;}
}
</style>
</head>



<body>

<div class="flex-container">
<header>
  <h1>City Gallery</h1>
</header>

<nav class="nav">
<ul>
  <li><a href="#">London</a></li>
  <li><a href="#">Paris</a></li>
  <li><a href="#">Tokyo</a></li>
</ul>
</nav>

<article class="article">
  <h1>London</h1>
  <p>London is the capital city of England. It is the most populous city in the United Kingdom,
  with a metropolitan area of over 13 million inhabitants.</p>
  <p>Standing on the River Thames, London has been a major settlement for two millennia,
  its history going back to its founding by the Romans, who named it Londinium.</p>
  <p><strong>Resize this page to see that what happens!</strong></p>
</article>

<footer>Copyright &copy; W3Schools.com</footer>
</div>

</body>
</html>
"""
