package de.gs.pdf.service.security

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.Signature
import java.security.cert.Certificate


class SignatureService(
    val keyPair: Pair<PrivateKey, Certificate>
) {

    fun signDocument(document: PDDocument): PDDocument {
        val signature = PDSignature()
        signature.signDate = java.util.Calendar.getInstance()
        signature.name = "Digital Signature"
        signature.location = "Location"
        signature.reason = "Reason"
        signature.contactInfo = "Contact Info"

        val signatureInterface = object : SignatureInterface {
            override fun sign(content: InputStream): ByteArray {
                try {
                   return signBytes(content.readAllBytes(), keyPair.first)
                } catch (e: GeneralSecurityException) {
                    throw IOException(e)
                }
            }
        }

        val options = createOptions(document)
        signature.setByteRange(IntArray(4) { 0 })
        document.addSignature(signature, signatureInterface, options)
        println("Added digital signature successfully.")

        return document
    }

    fun signBytes(content: ByteArray, privateKey: PrivateKey): ByteArray {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hash = messageDigest.digest(content)

        val sig: Signature = Signature.getInstance("SHA256withRSA")
        sig.initSign(privateKey)
        sig.update(hash)
        return sig.sign()
    }

    private fun createOptions(document: PDDocument): SignatureOptions {
        val options = SignatureOptions()
        options.page = document.numberOfPages
        options.preferredSignatureSize = SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2
        return options
    }


}