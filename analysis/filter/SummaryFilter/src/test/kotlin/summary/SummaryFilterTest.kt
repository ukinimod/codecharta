package summary

import de.maibornwolff.codecharta.filter.mergefilter.SummaryFilter
import de.maibornwolff.codecharta.serialization.ProjectDeserializer
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import java.io.InputStreamReader

class SummaryFilterTest {
    @Test
    fun merging_different_projects_should_return_merged_project() {
        // given
        val srcProject = ProjectDeserializer.deserializeProject(InputStreamReader(this.javaClass.classLoader.getResourceAsStream("test.json")))

        // when
        val project = SummaryFilter(srcProject).filter()

        // then
        Assert.assertThat(project.rootNode.attributes, CoreMatchers.notNullValue())
    }
}