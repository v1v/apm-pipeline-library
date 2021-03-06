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

/**
It sets an environment var with a value passed as a parameter, it simplifies Declarative syntax

  setEnvVar('MY_ENV_VAR', 'value')

  it replaces the following code

  script {
    env.MY_ENV_VAR = 'value')
  }
*/

def call(String name, String value){
  env[name] = value
}

def call(String name, Boolean value){
  env[name] = Boolean.toString(value)
}
