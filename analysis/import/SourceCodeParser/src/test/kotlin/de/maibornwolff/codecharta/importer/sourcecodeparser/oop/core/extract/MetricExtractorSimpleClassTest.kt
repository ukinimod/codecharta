package de.maibornwolff.codecharta.importer.sourcecodeparser.oop.core.extract

import de.maibornwolff.codecharta.importer.sourcecodeparser.oop.`~res`.extractBaseFolder
import de.maibornwolff.codecharta.importer.sourcecodeparser.oop.core.intermediate.SourceCode
import de.maibornwolff.codecharta.importer.sourcecodeparser.oop.infrastructure.antlr.java.Api
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class MetricExtractorSimpleClassTest {

    @Test(expected = IndexOutOfBoundsException::class)
    @Throws(IOException::class)
    fun trying_index_0_results_in_exceptions_because_code_starts_at_line_1() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        metricExtractor[0]
    }

    @Test
    @Throws(IOException::class)
    fun trying_last_index_does_not_result_in_exception() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        metricExtractor[metricExtractor.rowCount()]
    }

    @Test
    @Throws(IOException::class)
    fun does_not_count_empty_line_as_real() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        Assertions.assertThat(metricExtractor[1].rloc).isEqualTo(1)
        Assertions.assertThat(metricExtractor[1].rlocWasIncremented).isTrue()
        Assertions.assertThat(metricExtractor[2].rloc).isEqualTo(1)
        Assertions.assertThat(metricExtractor[2].rlocWasIncremented).isFalse()
        Assertions.assertThat(metricExtractor[3].rloc).isEqualTo(2)
        Assertions.assertThat(metricExtractor[3].rlocWasIncremented).isTrue()
    }

    @Test
    @Throws(IOException::class)
    fun does_not_count_comment_line_as_real() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        Assertions.assertThat(metricExtractor[8].rloc).isEqualTo(3)
        Assertions.assertThat(metricExtractor[9].rloc).isEqualTo(4)
    }

    @Test
    @Throws(IOException::class)
    fun does_not_count_lines_with_only_a_bracket_as_real() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        Assertions.assertThat(metricExtractor[19].rloc).isEqualTo(10)
        Assertions.assertThat(metricExtractor[20].rloc).isEqualTo(10)
    }

    @Test
    @Throws(IOException::class)
    fun counts_all_lines_as_lines_of_code() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        Assertions.assertThat(metricExtractor.rowCount()).isEqualTo(43)
    }

    @Test
    @Throws(IOException::class)
    fun counts_only_lines_with_actual_value_as_real() {
        val resource = "$extractBaseFolder/java/SourceCodeSimple.java"
        val sourceCode = SourceCode(Files.readAllLines(Paths.get(javaClass.classLoader.getResource(resource)!!.toURI())))
        Api.addTagsToSource(sourceCode)

        val metricExtractor = MetricExtractor(sourceCode)

        Assertions.assertThat(metricExtractor[43].rloc).isEqualTo(25)
    }
}