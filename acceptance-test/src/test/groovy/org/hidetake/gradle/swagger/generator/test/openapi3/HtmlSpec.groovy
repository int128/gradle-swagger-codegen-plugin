package org.hidetake.gradle.swagger.generator.test.openapi3

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hidetake.gradle.swagger.generator.test.Fixture
import spock.lang.Specification

import static org.hidetake.gradle.swagger.generator.test.Fixture.cleanBuildDir
import static org.hidetake.gradle.swagger.generator.test.Fixture.setupFixture

class HtmlSpec extends Specification {

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(new File('./openapi-v3/html'))
            .withPluginClasspath()
            .forwardOutput()
        cleanBuildDir(runner)
    }

    def 'plugin should add default tasks into the project'() {
        given:
        runner.withArguments('--stacktrace', 'tasks')

        when:
        def result = runner.build()

        then:
        result.output.contains('generateSwaggerCode -')
        result.output.contains('generateSwaggerCodeHelp -')
    }

    def 'generateSwaggerCode task should generate HTML'() {
        given:
        setupFixture(runner, Fixture.YAML.petstore)
        runner.withArguments('--stacktrace', 'generateSwaggerCode')

        when:
        def result = runner.build()

        then:
        result.task(':generateSwaggerCode').outcome == TaskOutcome.NO_SOURCE
        result.task(':generateSwaggerCodePetstore').outcome == TaskOutcome.SUCCESS
        new File(runner.projectDir, 'build/swagger-code-petstore/index.html').exists()

        when:
        def rerunResult = runner.build()

        then:
        rerunResult.task(':generateSwaggerCode').outcome == TaskOutcome.NO_SOURCE
        rerunResult.task(':generateSwaggerCodePetstore').outcome == TaskOutcome.UP_TO_DATE
    }

    def 'generateSwaggerCodePetstoreHelp task should show help'() {
        given:
        runner.withArguments('--stacktrace', 'generateSwaggerCodePetstoreHelp')

        when:
        def result = runner.build()

        then:
        result.output.contains('CONFIG OPTIONS')
    }

}
