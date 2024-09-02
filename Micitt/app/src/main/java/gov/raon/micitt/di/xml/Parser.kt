package gov.raon.micitt.di.xml

import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class Parser {
    private lateinit var document: Document
    private val removedList = mutableListOf<Pair<String, String>>()

    fun parse(xmlContent: String) {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        var filteredXml = xmlContent

        if (xmlContent.contains("CDATA")) {
            filteredXml = xmlContent.substringAfter("<![CDATA[").substringBefore("]]>")
        }

        val inputStream = String(filteredXml.toByteArray(Charsets.UTF_8)).byteInputStream()

        try {
            document = builder.parse(inputStream)
        } catch (e: Exception) {
            e.toString()
            parse(editTag(xmlContent))
        }
    }

    fun getTable(document: Document, table: String): MutableList<Map<String, String>> {
        document.documentElement.normalize()
        val tableElements = document.getElementsByTagName(table)
        val tables = mutableListOf<Map<String, String>>()

        for (i in 0 until tableElements.length) {
            val element = tableElements.item(i) as Element
            val tableData = mutableMapOf<String, String>()

            val children = element.childNodes
            for (j in 0 until children.length) {
                val child = children.item(j)
                if (child is Element) {
                    tableData[child.tagName] = child.textContent
                }
            }

            tables.add(tableData)
        }

        return tables
    }

    fun getDocument() : Document{
        return this.document
    }

    private fun editTag(input: String): String {
        val tagRegex = """<(/?)(\w+)>""".toRegex()
        val tagNameMap = mutableMapOf<String, String>()

        val matches = tagRegex.findAll(input)
        for (match in matches) {
            val tagName = match.groupValues[2]

            if (tagName.any { char -> accentMap.containsKey(char) }) {
                val newTagName = removeAccent(tagName)
                if (!tagNameMap.containsKey(tagName)) {
                    tagNameMap[tagName] = newTagName
                    removedList.add(Pair(tagName, newTagName))
                }
            }
        }

        var result = input
        for ((oldTagName, newTagName) in tagNameMap) {
            result = result.replace("<$oldTagName>", "<$newTagName>")
            result = result.replace("</$oldTagName>", "</$newTagName>")
        }

        return result

    }

    /*        스페인어 발음기호          */
    private val accentChars = listOf('á', 'é', 'í', 'ó', 'ú', 'ñ', 'ü')
    private val accentMap = mapOf(
        'á' to 'a', 'é' to 'e', 'í' to 'i', 'ó' to 'o',
        'ú' to 'u', 'ñ' to 'n', 'ü' to 'u'
    )

    fun removeAccent(tagName: String): String {
        return tagName.map { char -> accentMap.getOrDefault(char, char) }.joinToString("")
    }
}