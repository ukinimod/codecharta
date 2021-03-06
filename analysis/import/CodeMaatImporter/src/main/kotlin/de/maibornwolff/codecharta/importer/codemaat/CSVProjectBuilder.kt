/*
 * Copyright (c) 2018, MaibornWolff GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of  nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.maibornwolff.codecharta.importer.codemaat

import com.univocity.parsers.csv.CsvParser
import com.univocity.parsers.csv.CsvParserSettings
import de.maibornwolff.codecharta.attributeTypes.AttributeTypes
import de.maibornwolff.codecharta.model.Edge
import de.maibornwolff.codecharta.model.Project
import de.maibornwolff.codecharta.model.ProjectBuilder
import de.maibornwolff.codecharta.translator.MetricNameTranslator
import mu.KotlinLogging
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class CSVProjectBuilder(
        projectName: String,
        private val pathSeparator: Char,
        private val csvDelimiter: Char,
        metricNameTranslator: MetricNameTranslator = MetricNameTranslator.TRIVIAL,
        attributeTypes: AttributeTypes = AttributeTypes(mutableMapOf())
) {
    private val logger = KotlinLogging.logger {}

    private val includeRows: (Array<String>) -> Boolean = { true }
    private val projectBuilder = ProjectBuilder(projectName)
            .withMetricTranslator(metricNameTranslator)
            .addAttributeTypes(attributeTypes)

    fun parseCSVStream(inStream: InputStream): ProjectBuilder {
        val parser = createParser(inStream)
        val header = CSVHeader(parser.parseNext())
        parseContent(parser, header)
        parser.stopParsing()
        return projectBuilder
    }

    fun build(): Project {
        return projectBuilder.build()
    }

    private fun parseContent(parser: CsvParser, header: CSVHeader) {
        var row = parser.parseNext()
        while (row != null) {
            if (includeRows(row)) {
                try {
                    val csvRow = CSVRow(row, header, pathSeparator)
                    val edge: Edge = csvRow.asEdge()
                    projectBuilder.insertEdge(edge)
                } catch (e: IllegalArgumentException) {
                    logger.warn { "Ignoring row due to ${e.message}" }
                }
            }
            row = parser.parseNext()
        }

    }

    private fun createParser(inStream: InputStream): CsvParser {
        val parserSettings = CsvParserSettings()
        parserSettings.format.delimiter = csvDelimiter

        val parser = CsvParser(parserSettings)
        parser.beginParsing(InputStreamReader(inStream, StandardCharsets.UTF_8))
        return parser
    }
}
