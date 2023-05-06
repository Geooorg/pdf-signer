package de.gs.pdf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdfSignerApplication

fun main(args: Array<String>) {
	runApplication<PdfSignerApplication>(*args)
}
