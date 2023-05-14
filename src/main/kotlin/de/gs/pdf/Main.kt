package de.gs.pdf

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.file
import de.gs.pdf.service.security.KeyReaderService
import de.gs.pdf.service.PdfFileWriter
import de.gs.pdf.service.security.SignatureService
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*
import kotlin.system.exitProcess


class Main : CliktCommand() {

    //    val files: String by option(help = "file list to sign").prompt("files")
//    private val files: List<String> by argument().multiple()
    private val code by argument().help("Text to add as code")
    private val author by argument().help("Your name")
    private val keyStore by argument().file(mustExist = true).help("path to your keyStore.p12 file")
    private val password by argument(help = "your keyStore.p12 password. If empty, type \"\"")
    private val files by argument().file(mustExist = true).multiple().help("list of files to sign")

    override fun run() {

        val uuid = UUID.randomUUID()
        println("Generated UUID is: $uuid")
        // KeyFactory.getInstance("RSA", "BC"),

        val keyReaderService = KeyReaderService( keyStore, password)
        val keyPair = keyReaderService.createKeyPair()
        val signatureService = SignatureService(keyPair)

        val fileList = files.filter { f -> f.name.endsWith(".pdf", true) }
        val pdfReader = PdfFileWriter(fileList, code, author, uuid, signatureService)

        pdfReader.addTextAndDigitalSignature()
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

