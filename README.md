# pdf-signer

A tiny tool written in Kotlin that can be used to 'sign' a PDF by adding a new page to an existing PDF file or a set of files.
That new page contains a UUID, the date and author. Furthermore a kind of 'secret word' can be passed.

## Build

`./gradlew shadowJar`

## Running the application

After building it, run
`java -jar build/libs/pdf-signer.jar "your secret" "Your name" /home/you/input1.pdf <other files>`

The 'signed' files will be written to `/home/you/input1_signed.pdf`.
Note that the file path needs to be passed as absolute path.