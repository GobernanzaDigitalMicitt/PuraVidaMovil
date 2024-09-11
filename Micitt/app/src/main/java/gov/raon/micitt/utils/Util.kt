package gov.raon.micitt.utils

import org.bson.internal.Base64
import java.security.DigestException
import java.security.MessageDigest
import android.content.Context
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {

    fun hashSHA256(msg: String): String? {
        val hash: ByteArray
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            hash = md.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of partial content");
        }

        return bytesToHex(hash)
    }

    private fun bytesToHex(byteArray: ByteArray): String {
        val hexChars = CharArray(byteArray.size * 2)
        val digits = "0123456789ABCDEF"
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = digits[v shr 4]
            hexChars[i * 2 + 1] = digits[v and 0xf]
        }
        return String(hexChars)
    }

    fun base64UrlDecode(input: String?): String {
        val base64String = input!!.replace('-', '+').replace('_', '/')

        val padding = 4 - (base64String.length % 4)
        val paddedBase64String = if (padding < 4) base64String + "=".repeat(padding) else base64String

        val decodedBytes = Base64.decode(paddedBase64String)
        return String(decodedBytes, Charsets.UTF_8)
    }

    fun base64UrlEncode(input: String): String {
        val encodedString = Base64.encode(input.toByteArray(Charsets.UTF_8))

        // Convert to Base64 URL format by replacing characters
        return encodedString.replace('+', '-').replace('/', '_').replace("=", "")
    }

    fun saveFile(context: Context, baseFileName: String, fileContents: String) {
        try {
            // 현재 날짜를 yyyyMMdd 형식으로 변환
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val todayDate = dateFormat.format(Date())

            // 파일 이름에 날짜 추가
            val fileName = "${baseFileName}_$todayDate.xml"

            // 파일을 내부 저장소에 생성하고 MODE_PRIVATE로 저장
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(fileContents.toByteArray())
            }
            println("File saved successfully to internal storage with name: $fileName")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readFile(context: Context, fileName: String): String? {
        return try {
            context.openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.fold("") { some, text -> "$some\n$text" }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getFilePath(context: Context, fileName: String): String {
        val file: File = context.getFileStreamPath(fileName)
        return file.absolutePath
    }

}