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

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertTrue

class GetBuildInfoJsonFilesStepTests extends BasePipelineTest {
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.WORKSPACE = "WS"
    env.JENKINS_URL = "http://jenkins.example.com:8080"
    binding.setVariable('env', env)

    helper.registerAllowedMethod("sh", [Map.class], { "OK" })
    helper.registerAllowedMethod("sh", [String.class], { "OK" })
    helper.registerAllowedMethod("log", [Map.class], {m -> println m.text})
    helper.registerAllowedMethod("readJSON", [Map.class], { m ->
      return readJSON(m)
    })
    helper.registerAllowedMethod("error", [String.class], {s ->
      printCallStack()
      throw new Exception(s)
    })
    helper.registerAllowedMethod("toJSON", [Map.class], { m ->
      def script = loadScript("vars/toJSON.groovy")
      return script.call(m)
    })
    helper.registerAllowedMethod("writeJSON", [Map.class], { "OK" })
    helper.registerAllowedMethod("readFile", [Map.class], { m ->
      File f = new File("src/test/resources/${m.file}")
      return f.getText()
    })
  }

  def readJSON(params){
    def jsonSlurper = new groovy.json.JsonSlurper()
    def jsonText = params.text
    if(params.file){
      File f = new File("src/test/resources/${params.file}")
      jsonText = f.getText()
    }
    return jsonSlurper.parseText(jsonText)
  }

  @Test
  void test() throws Exception {
    def script = loadScript("vars/getBuildInfoJsonFiles.groovy")
    script.call("http://jenkins.example.com/job/myJob", "1")
    printCallStack()
    assertJobStatusSuccess()
  }
}