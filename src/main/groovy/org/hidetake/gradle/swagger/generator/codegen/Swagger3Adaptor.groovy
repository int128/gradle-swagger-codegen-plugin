package org.hidetake.gradle.swagger.generator.codegen
/**
 * An executor of Swagger Codegen V3.
 */
class Swagger3Adaptor implements Adaptor {
    static final String CLASS_NAME = 'io.swagger.codegen.v3.cli.SwaggerCodegen'

    @Override
    JavaExecOptions generate(GenerateOptions options) {
        def args = []
        args << 'generate'
        args << '-l' << options.language
        args << '-i' << options.inputFile
        args << '-o' << options.outputDir
        if (options.library) {
            args << '--library' << options.library
        }
        if (options.configFile) {
            args << '-c' << options.configFile
        }
        if (options.templateDir) {
            args << '-t' << options.templateDir
        }
        if (options.additionalProperties) {
            args << '--additional-properties' << options.additionalProperties.collect { key, value ->
                "$key=$value"
            }.join(',')
        }
        if (options.rawOptions) {
            args.addAll(options.rawOptions)
        }

        //TODO: configure logback log level
        def systemProperties = [:]
        if (options.systemProperties) {
            systemProperties.putAll(options.systemProperties)
        }

        new JavaExecOptions(
            classpath: Helper.findJARs(options.generatorFiles),
            args: args,
            main: CLASS_NAME,
            systemProperties: systemProperties,
        )
    }

    @Override
    JavaExecOptions help(HelpOptions options) {
        new JavaExecOptions(
            classpath: Helper.findJARs(options.generatorFiles),
            args: ['help', 'generate'],
            main: CLASS_NAME,
            systemProperties: [:],
        )
    }

    @Override
    JavaExecOptions configHelp(ConfigHelpOptions options) {
        new JavaExecOptions(
            classpath: Helper.findJARs(options.generatorFiles),
            args: ['config-help', '-l', options.language],
            main: CLASS_NAME,
            systemProperties: [:],
        )
    }
}
