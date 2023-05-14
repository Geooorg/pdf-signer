package de.gs.pdf

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate


class SignatureServiceTest() {

    @Test
    fun canReadKeystore() {
        // Laden Sie das PDF-Dokument, das Sie signieren möchten
//        val document = PDDocument.load(File("document.pdf"))

        val keystore = KeyStore.getInstance("PKCS12")
        val keystoreBytes = object {}.javaClass.getResource("/keystore.p12")?.readBytes()
        val keystorePassword = "".toCharArray()

        keystore.load(ByteArrayInputStream(keystoreBytes), keystorePassword)

        val alias = keystore.aliases().nextElement()
        val privateKey = keystore.getKey(alias, keystorePassword) as PrivateKey
        val certificate: Certificate = keystore.getCertificate(alias)

        assertThat(privateKey).isNotNull()
        assertThat(certificate).isNotNull()
    }
}

