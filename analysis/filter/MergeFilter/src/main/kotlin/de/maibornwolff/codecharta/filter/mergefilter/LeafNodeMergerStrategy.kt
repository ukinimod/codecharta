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

package de.maibornwolff.codecharta.filter.mergefilter

import de.maibornwolff.codecharta.model.MutableNode
import de.maibornwolff.codecharta.model.Path

/**
 * merges leafs according to the level of matching of their paths
 */
class LeafNodeMergerStrategy(private val addMisfittingNodes: Boolean, ignoreCase: Boolean = false) : NodeMergerStrategy {
    private val mergeConditionSatisfied: (MutableNode, MutableNode) -> Boolean

    init {
        mergeConditionSatisfied = if (ignoreCase) { n1: MutableNode, n2: MutableNode -> n1.name.toUpperCase() == n2.name.toUpperCase() }
        else { n1: MutableNode, n2: MutableNode -> n1.name == n2.name }
    }

    /**
     * merge list of nodeLists to single nodeList
     */
    override fun mergeNodeLists(lists: List<List<MutableNode>>): List<MutableNode> {
        return if (lists.isEmpty()) listOf()
        else lists.reduce { acc1, nodes ->
            nodes.fold(acc1, { acc2: List<MutableNode>, node: MutableNode ->
                acc2.flatMap { if (mergeConditionSatisfied(it, node)) merge(it, node) else listOf(it) }
            })
        }
    }


    /**
     * merge multiple nodes
     */
    private fun merge(vararg nodes: MutableNode): List<MutableNode> {
        val root = nodes[0].merge(nodes.asList())
        nodes.map { it.nodes }
                .reduce { total, next -> total.addAll(next) }
                .forEach { root.insertAt(Path(it.key.edgesList.dropLast(1)), it.value) }
        return listOf(root)
    }

    private fun replaceMisfittingPath(path: Path): Path {
        return when {
            addMisfittingNodes -> path
            else -> Path.TRIVIAL
        }
    }

    private fun Set<Path>.findFittingPathOrNull(path: Path): Path? {
        val matchingLeaf = this.filter { !it.isTrivial }.maxBy { path.fittingEdgesFromTailWith(it) } ?: path
        return when {
            path.fittingEdgesFromTailWith(matchingLeaf) == 0 -> null
            else -> matchingLeaf
        }
    }

    private fun Map<Path, MutableNode>.addAll(nodes: Map<Path, MutableNode>): Map<Path, MutableNode> {
        val newNodes = nodes.filterValues { it.isLeaf }
                .mapKeys { this.keys.findFittingPathOrNull(it.key) ?: replaceMisfittingPath(it.key) }
                .filterKeys { !it.isTrivial }
        val unchangedNodes = this.filterValues { it.isLeaf }.filterKeys { !newNodes.keys.contains(it) }

        return newNodes
                .plus(unchangedNodes)
                .mapValues {
                    if (this[it.key] == null) it.value else it.value.merge(listOf(this[it.key]!!))
                }
    }
}
