package de.gs.pdf.service.security

import org.bouncycastle.util.io.pem.PemReader
import java.io.*
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec


class CertService(
    val keyFactory: KeyFactory,
    val keyFileDirectory: String
) {
    fun createKeyPair() {
        val privateKey = readPrivateKey(File(keyFileDirectory, "id_rsa"))
        val publicKey = readPublicKey(File(keyFileDirectory, "id_rsa.pub"))

        println("Read key pair successfully from $keyFileDirectory")
    }

    private fun readPrivateKey(file: File): PrivateKey {
        val pemReader = PemReader(InputStreamReader(FileInputStream(file)))

        try {
            // https://github.com/txedo/bouncycastle-rsa-pem-read/blob/master/src/main/java/me/txedo/security/PemFile.java
            val content: ByteArray = pemReader.readPemObject().content
            return keyFactory.generatePrivate(PKCS8EncodedKeySpec(content))
        } finally {
            pemReader.close()
        }
    }

    private fun readPublicKey(file: File) =
        CertificateFactory.getInstance("X.509")
            .generateCertificate(ByteArrayInputStream(file.readBytes()))
            .publicKey

}