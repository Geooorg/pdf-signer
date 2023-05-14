package de.gs.pdf.service

import de.gs.pdf.service.security.SignatureService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


class PdfFileWriter(
    val fileNames: List<File>, val code: String, val author: String, val uuid: UUID,
    val signatureService: SignatureService) {
    fun addTextAndDigitalSignature() {
        fileNames.forEach { file ->

            val outFile = destinationFileName(file)
            val dateTime = formattedDateTime()

            try {
                PDDocument.load(File(file.absolutePath)).use { document ->

                    val page = PDPage(PDRectangle.A4)
                    document.addPage(page)

                    PDPageContentStream(document, page).use { contentStream ->

                        contentStream.beginText()
                        contentStream.setFont(PDType1Font.TIMES_ROMAN, 10f)
                        contentStream.newLineAtOffset(50f, 700f)
                        contentStream.showText("Signatur: Code '$code' um ${dateTime}, UUID: $uuid")
                        contentStream.endText()

                        contentStream.beginText()
                        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12f)
                        contentStream.newLineAtOffset(50f, 680f)
                        contentStream.showText(author)
                        contentStream.endText()
                    }

                    val signedDocument = signatureService.signDocument(document)

                    signedDocument.save(outFile.absolutePath)
                    signedDocument.close()

                    println("${outFile.name} written successfully.")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun destinationFileName(file: File) =
            File(file.path.replace(file.name, ""), file.name.replace(".pdf", "") + "_signed.pdf")

    private fun formattedDateTime(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")
        return now.format(formatter)!!
    }
}