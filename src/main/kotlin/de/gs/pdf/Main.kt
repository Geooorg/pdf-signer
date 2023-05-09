package de.gs.pdf

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.file
import de.gs.pdf.service.security.CertService
import de.gs.pdf.service.PdfFileWriter
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyFactory
import java.security.Security
import java.util.*
import kotlin.system.exitProcess


class Main : CliktCommand() {

    //    val files: String by option(help = "file list to sign").prompt("files")
//    private val files: List<String> by argument().multiple()
    private val code by argument().help("Text to add as code ")
    private val author by argument().help("Your name")
    private val keyDir by argument(help = "path to your private/public key directory")
    private val files by argument().file(mustExist = true).multiple().help("list of files to sign")

    override fun run() {

        val uuid = UUID.randomUUID()
        println("Generated UUID is: $uuid")

        val certService = CertService(KeyFactory.getInstance("RSA", "BC"), keyDir)
        certService.createKeyPair()

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

    initSecurity()

    Main().main(args)
}

private fun initSecurity() {
    Security.addProvider(BouncyCastleProvider())
}

