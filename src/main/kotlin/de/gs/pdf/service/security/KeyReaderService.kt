package de.gs.pdf.service.security

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate


class KeyReaderService(
//    private val keyFactory: KeyFactory,
    private val keystoreFile: File,
    private val password: String
) {
    fun createKeyPair(): Pair<PrivateKey, Certificate> {
        val keystore = KeyStore.getInstance("PKCS12")
        val keystoreBytes = FileInputStream(keystoreFile).readAllBytes()
        val keystorePassword = password.toCharArray()

        keystore.load(ByteArrayInputStream(keystoreBytes), keystorePassword)

        val alias = keystore.aliases().nextElement()
        val privateKey = keystore.getKey(alias, keystorePassword) as PrivateKey
        val certificate: Certificate = keystore.getCertificate(alias)

        println("Loaded key pair successfully from $keystoreFile")
        return Pair(privateKey, certificate)
    }

//    private fun readPrivateKey(file: File): PrivateKey {
//        val pemReader = PemReader(InputStreamReader(FileInputStream(file)))
//
//        try {
//            // https://github.com/txedo/bouncycastle-rsa-pem-read/blob/master/src/main/java/me/txedo/security/PemFile.java
//            val content: ByteArray = pemReader.readPemObject().content
//            return keyFactory.generatePrivate(PKCS8EncodedKeySpec(content))
//        } finally {
//            pemReader.close()
//        }
//    }
//
//    private fun readPublicKey(file: File) =
//        CertificateFactory.getInstance("X.509")
//            .generateCertificate(ByteArrayInputStream(file.readBytes()))
//            .publicKey

}