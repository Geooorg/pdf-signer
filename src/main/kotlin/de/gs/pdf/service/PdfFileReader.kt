package de.gs.pdf.service

import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import java.io.File
import java.io.FileOutputStream


class PdfFileReader(val fileNames: List<File>, val code: String) {
    fun readFiles() {
        fileNames.forEach { file ->
            val outFile = File(file.path.replace(file.name, ""), file.name.replace(".pdf", "") + "_signed.pdf")
            println("Writing $outFile with code: '$code'")

            val reader = PdfReader(file.inputStream())
            val document = Document(PageSize.A4)
            val pdfCopy = PdfCopy(document, FileOutputStream(outFile))
//            val pdfStamper = PdfStamper(reader, FileOutputStream(outFile))
            document.open()

//            val writer = PdfWriter.getInstance(document, FileOutputStream(outFile))

            // copy existing pages
            for (i in 1..reader.numberOfPages) {
                println("Copying page ${pdfCopy.pageNumber} / ${reader.numberOfPages}")

                val page = pdfCopy.getImportedPage(reader, i)

                pdfCopy.addPage(page)
            }

            document.newPage()

            val font = Font(Font.FontFamily.TIMES_ROMAN, 10f)
            val paragraph = Paragraph(code, font)
            paragraph.alignment = Element.ALIGN_CENTER
            document.add(paragraph)
            document.add(paragraph)
            document.add(paragraph)

            println("Page numbers: ${pdfCopy.pageNumber}")

            pdfCopy.close()
            reader.close()
            document.close()


            // PdfReader reader = new PdfReader(src); PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest)); PdfContentByte canvas = stamper.getOverContent(1); ColumnText.showTextAligned(canvas,   Element.ALIGN_LEFT, new Phrase("Hello people!"), 36, 540, 0); stamper.close();
        }
    }
}