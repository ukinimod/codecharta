/*
 * Copyright (c) 2017, MaibornWolff GmbH
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

package de.maibornwolff.codecharta.filter.summary

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.SystemExitException
import de.maibornwolff.codecharta.filter.mergefilter.SummaryFilter
import de.maibornwolff.codecharta.serialization.ProjectDeserializer
import de.maibornwolff.codecharta.serialization.ProjectSerializer
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

class ProjectSummaryFilterArgs(parser: ArgParser) {
    val sources by parser.positionalList("json file", 1..1) { File(this) }
}

fun main(args: Array<String>) {
    val filterArgs = ProjectSummaryFilterArgs(ArgParser(args))

    try {
        val inputStream = FileInputStream(filterArgs.sources[0].absoluteFile)
        val srcProject = ProjectDeserializer.deserializeProject(InputStreamReader(inputStream))

        val resultingProject = SummaryFilter(srcProject).filter()

        System.out.writer().use { ProjectSerializer.serializeProject(resultingProject, it) }

    } catch (e: SystemExitException) {
        System.err.writer().use { e.printUserMessage(it, "summary", 80) }
    }
}

