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
import java.lang.Exception
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class AxisStepTests extends ApmBasePipelineTest {
  String scriptName = 'vars/axis.groovy'

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()
  }

  @Test
  void test() throws Exception {
    def script = loadScript(scriptName)
    def v = script.call('foo', [1, 2])
    printCallStack()
    assertEquals(v.size(), 2)
    // Remember to convert GStringImpl to String before comparing it to a String
    // org.codehaus.groovy.runtime.GStringImpl<1> is not a java.lang.String<1>
    assertEquals(v[0].name.toString(), 'foo')
    assertEquals(v[0].value.toString(), '1')
    assertEquals(v[1].name.toString(), 'foo')
    assertEquals(v[1].value.toString(), '2')
    assertJobStatusSuccess()
  }

  @Test
  void testMissingName() throws Exception {
    def script = loadScript(scriptName)
    testMissingArgument('name', 'argument missing') {
      script.call(null, [])
    }
  }

  @Test
  void testMissingValues() throws Exception {
    def script = loadScript(scriptName)
    testMissingArgument('values', 'argument missing') {
      script.call('foo', null)
    }
  }
}
