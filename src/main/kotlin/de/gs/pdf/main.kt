package de.gs.pdf

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.file
import de.gs.pdf.service.PdfFileWriter
import java.util.*
import kotlin.system.exitProcess

class Main : CliktCommand() {

    //    val cert: String by argument(help = "path to certificate file").
//    val files: String by option(help = "file list to sign").prompt("files")
//    private val files: List<String> by argument().multiple()
    val code by argument().help("Text to add as code ")
    val author by argument().help("Your name")
    val files by argument().file(mustExist = true).multiple().help("list of files to sign")

    override fun run() {

        val uuid = UUID.randomUUID()
        println(" Generated UUID is: $uuid")

        val fileList = files.filter { f -> f.name.endsWith(".pdf", true) }
        val pdfReader = PdfFileWriter(fileList, code, author, uuid)
        pdfReader.addTextSignature()

        // val signService = SignService()
    }

}

fun main(args: Array<String>) {
    println("Welcome to PDF Signer")

    if (args.isEmpty()) {
        println("Please pass your certificate as first argument and the file(s) to sign")
        exitProcess(1)
    }

    Main().main(args)
}

