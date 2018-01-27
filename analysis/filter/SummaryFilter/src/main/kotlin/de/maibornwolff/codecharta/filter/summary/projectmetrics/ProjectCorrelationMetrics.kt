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

package de.maibornwolff.codecharta.filter.summary.projectmetrics

import de.maibornwolff.codecharta.model.Node
import de.maibornwolff.codecharta.model.Project
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation
import java.util.*

class ProjectCorrelationMetrics {
    private fun getMetricNameList(nodes: MutableCollection<Node>): List<String> {
        return nodes
                .flatMap { m -> m.attributes.keys }
                .distinct()
                .filter { s -> !s.isEmpty() }
    }

    fun value(project: Project): Map<String, Number> {
        return computeCorrelationDescriptions(project.rootNode.leaves.values)
                .filter { x -> !java.lang.Double.isNaN(x.correlation) }
                .associateBy({ it.name }, { it.correlation })
    }

    private fun computeCorrelationDescriptions(nodes: MutableCollection<Node>): List<CorrelationDescription> {
        val metricNames = getMetricNameList(nodes)
        val correlationDescriptions = ArrayList<CorrelationDescription>()
        for (metric1 in metricNames) {
            metricNames
                    .filter { metric1 < it }
                    .forEach { correlationDescriptions.addAll(computeCorrelationDescriptions(nodes, metric1, it)) }
        }
        return correlationDescriptions
    }

    private fun computeCorrelationDescriptions(
            nodes: MutableCollection<Node>,
            metricNameX: String,
            metricNameY: String): List<CorrelationDescription> {

        val vcFilesSize = nodes.size

        val x = DoubleArray(vcFilesSize)
        val y = DoubleArray(vcFilesSize)

        var i = 0
        for (node in nodes) {
            if (node.attributes.contains(metricNameX) && node.attributes.contains(metricNameY)) {
                x[i] = node.attributes[metricNameX] as Double
                y[i] = node.attributes[metricNameY] as Double
                i++
            } else {
                x[i] = 0.0
                y[i] = 0.0
            }
        }
        val pearsonsCorrelation = PearsonsCorrelation().correlation(x, y)
        val spearmansCorrelation = SpearmansCorrelation().correlation(x, y)

        return Arrays.asList(
                CorrelationDescription("pearson", metricNameX, metricNameY, pearsonsCorrelation),
                CorrelationDescription("spearman", metricNameX, metricNameY, spearmansCorrelation)
        )
    }

    private class CorrelationDescription(private val correlationMetric: String, private val metric1: String, private val metric2: String, val correlation: Double) {
        val outputFormat = "%s, %s, %s, %f"
        val SEP = "-"
        val name: String
            get() = correlationMetric + SEP + metric1 + SEP + metric2

        override fun toString(): String {
            return String.format(outputFormat, correlationMetric, metric1, metric2, correlation)
        }
    }
}
