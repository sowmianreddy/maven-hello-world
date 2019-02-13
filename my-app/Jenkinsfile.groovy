pipeline {
    agent any
        stages {
        stage ('Initialize') {
            steps {
                sh '''
                   export MAVEN_HOME=/opt/apache-maven-3.5.4/bin
                   export PATH=${MAVEN_HOME}:${PATH}
                    echo "PATH = ${PATH}"
                    echo "MAVEN_HOME = ${MAVEN_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                dir ("${env.WORKSPACE}/my-app") {
                sh '/opt/apache-maven-3.5.4/bin/mvn -Dmaven.test.failure.ignore=true install' 
            }
            }
            post {
                success {
                    steps {
                         dir ("${env.WORKSPACE}/my-app") {
                    sh '''
                 java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
                 '''
                         }
                    }
                  }
            }
        }
    }
}
