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

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class GetBlueoceanTabURLStepTests extends ApmBasePipelineTest {

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()
    script = loadScript('vars/getBlueoceanTabURL.groovy')
  }

  @Test
  void testMissingArguments() throws Exception {
    try {
      script.call()
    } catch(e){
      //NOOP
    }
    printCallStack()
    assertTrue(assertMethodCallContainsPattern('error', 'getBlueoceanTabURL: Unsupported type'))
    assertJobStatusFailure()
  }

  @Test
  void testWrongTypeArgument() throws Exception {
    try {
      script.call('unknown')
    } catch(e){
      //NOOP
    }
    printCallStack()
    assertTrue(assertMethodCallContainsPattern('error', 'getBlueoceanTabURL: Unsupported type'))
    assertJobStatusFailure()
  }

  @Test
  void testSuccessWithTestsTab() throws Exception {
    def ret = script.call('tests')
    printCallStack()
    assertTrue(ret.contains("${env.BRANCH_NAME}/${env.BUILD_ID}/tests"))
    assertJobStatusSuccess()
  }

  @Test
  void testSuccessWithChangesTab() throws Exception {
    def ret = script.call('changes')
    printCallStack()
    assertTrue(ret.contains("${env.BRANCH_NAME}/${env.BUILD_ID}/changes"))
    assertJobStatusSuccess()
  }

  @Test
  void testSuccessWithPipelineTab() throws Exception {
    def ret = script.call('pipeline')
    printCallStack()
    assertTrue(ret.contains("${env.BRANCH_NAME}/${env.BUILD_ID}/pipeline"))
    assertJobStatusSuccess()
  }

  @Test
  void testSuccessWithArtifactsTab() throws Exception {
    def ret = script.call('artifacts')
    printCallStack()
    assertTrue(ret.contains("${env.BRANCH_NAME}/${env.BUILD_ID}/artifacts"))
    assertJobStatusSuccess()
  }

  @Test
  void testSuccessWithCoberturaTab() throws Exception {
    def ret = script.call('cobertura')
    printCallStack()
    assertTrue(ret.contains("${env.BRANCH_NAME}/${env.BUILD_ID}/cobertura"))
    assertJobStatusSuccess()
  }

  @Test
  void testSuccessWithGcsTab() throws Exception {
    def ret = script.call('gcs')
    printCallStack()
    assertTrue(ret.contains("${env.BRANCH_NAME}/${env.BUILD_ID}/gcsObjects"))
    assertJobStatusSuccess()
  }
}
