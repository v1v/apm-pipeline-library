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
 Install Go and run some command in a pre-configured environment.

  withGoEnv(version: '1.14.2'){
    cmd(label: 'Go version', script: 'go version')
  }

   withGoEnv(version: '1.14.2', pkgs: [
       "github.com/magefile/mage",
       "github.com/elastic/go-licenser",
       "golang.org/x/tools/cmd/goimports",
   ]){
       cmd(label: 'Run mage',script: 'mage -version')
   }

}

*/
def call(Map args = [:], Closure body) {
  if (isUnix()) {
    withGoEnvUnix(args) {
      checkGoPath()
      body()
    }
  } else {
    withGoEnvWindows(args) {
      checkGoPath()
      body()
    }
  }
}

def checkGoPath(){
  if(fileExists(file: "${env.GOPATH}/go.mod")){
    log(level: 'WARN', text: "${env.GOPATH}/go.mod file exists, go.mod cannot be in the GOPATH, try to checkout your code into a folder.")
  }
}
