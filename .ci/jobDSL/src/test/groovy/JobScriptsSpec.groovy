// Licensed to Elasticsearch B.V. under one or more contributor
// license agreements. See the NOTICE file distributed with
// this work for additional information regarding copyright
// ownership. Elasticsearch B.V. licenses this file to you under
// the Apache License, Version 2.0 (the "License"); you may
// not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import groovy.util.FileNameFinder
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import hudson.CustomJenkinsRule

class JobScriptsSpec extends Specification {
    @Shared
    @ClassRule
    CustomJenkinsRule jenkinsRule = new CustomJenkinsRule()

    @Unroll
    def 'test folder script #file.name'(File file) {
        given:
        def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))
        def dslLoader = new DslScriptLoader(jobManagement)

        when:
        dslLoader.runScript(file.text)

        then:
        noExceptionThrown()

        where:
        file << new FileNameFinder().getFileNames('jobs', '**/folder.groovy')
                          .collect { new File(it) }
    }

    @Unroll
    def 'test script #file.name'(File file) {
        given:
        def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))
        def dslLoader = new DslScriptLoader(jobManagement)
        def fileFinder = new FileNameFinder()
        fileFinder.getFileNames('jobs', '**/folder.groovy').each {
          dslLoader.runScript(new File(it).text)
        }

        when:
        dslLoader.runScript(file.text)

        then:
        noExceptionThrown()

        where:
        file << new FileNameFinder().getFileNames('jobs', '**/*.groovy')
                          .findAll {!it.contains('folder.groovy')}
                          .collect { new File(it) }
    }
}
