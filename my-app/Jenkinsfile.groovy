pipeline {
    agent any
    environment {
        DISABLE_AUTH = 'true'
        DB_ENGINE    = 'sqlite'
        GIT_DIR      =  '${env.HOME}/git_dir'
    }
        stages {
        stage ('Initialize') {
            steps {
                  sh '''
                    printenv
                    export MAVEN_HOME=/opt/apache-maven-3.5.4/bin
                    export PATH=${MAVEN_HOME}:${PATH}
                    echo "PATH = ${PATH}"
                    echo "MAVEN_HOME = ${MAVEN_HOME}"
                    mkdir ${GIT_DIR}
                '''
            }
        }

        stage ('Build') {
            steps {
                dir ("${env.WORKSPACE}/my-app") {
                    sh '/opt/apache-maven-3.5.4/bin/mvn -Dmaven.test.failure.ignore=true install' 
                }
            }
         }
         stage ('Run') {
            steps {       
                  dir ("${env.WORKSPACE}/my-app") {
                     sh ' java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App '
                  }
            }      
         }
        stage ('Git merge'){
            steps {
                dir ("${env.GIT_DIR}"){
                     checkout scm: [
                        $class: 'GitSCM', userRemoteConfigs: [
                            [
                                url: 'git@github.com:sowmianreddy/maven-hello-world.git',
                                credentialsId: '4d15fa75-11f4-4eaf-9f69-9f9ce2d5bdc5',
                                changelog: false,
                            ]
                        ],
                        branches: [
                            [
                             name: "refs/heads/${env.GIT_BRANCH}"
                            ]
                        ],
                        poll: false
                    ]
                    
                }
               dir ("${env.GIT_DIR}/maven-hello-world"){
                }
            }
            
         }
            
        }
    }
}
