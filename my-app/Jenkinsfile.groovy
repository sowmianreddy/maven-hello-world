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
                sh '/opt/apache-maven-3.5.4/bin/mvn -Dmaven.test.failure.ignore=true install' 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}
