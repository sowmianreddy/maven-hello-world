pipeline {
        agent any 
  	  environment {
        	DISABLE_AUTH = 'true'
        	DB_ENGINE    = 'sqlite'
   	 }
        stages {
		stage ('Initialize') {
           		 steps {
                  		sh '''
                    		printenv
                    		#export MAVEN_HOME=/opt/apache-maven-3.6.0
				export MAVEN_HOME=/usr/share/apache-maven
                    		export PATH=${MAVEN_HOME}/bin:${PATH}
                    		echo "PATH = ${PATH}"
                    		echo "MAVEN_HOME = ${MAVEN_HOME}"
				export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64
				export PATH=$JAVA_HOME}/bin:$PATH
				echo "JAVA_HOME=${JAVA_HOME}"
				mvn -version                  
                		'''
            			}
        	}

        	stage ('Build') {
            		steps {
                		dir ("${env.WORKSPACE}/my-app") {
                    		// sh '/opt/apache-maven-3.5.4/bin/mvn -Dmaven.test.failure.ignore=true install' 
					sh 'mvn -version'
        				sh 'mvn clean '            		
					sh 'mvn -Dmaven.test.failure.ignore=true install' 
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
		stage ('build merge job') {
			steps {
           		 build 'demo-2'
			}
		}
        /*
        stage ('Git merge'){
          when {
               expression { return env.GIT_BRANCH != 'master'}
           }
              
            steps {
                 dir ("${env.HOME}/test-git-repo"){
                     checkout scm: [
                        $class: 'GitSCM', userRemoteConfigs: [
                            [
                               url: 'git@github.com:sowmianreddy/maven-hello-world.git', 
                                //url: 'https://github.com/sowmianreddy/maven-hello-world.git', 
                                credentialsId: '4d15fa75-11f4-4eaf-9f69-9f9ce2d5bdc5',
                                changelog: false,
                            ]
                        ],
                      
                        poll: false
                    ]
                    
                }
               dir ("${env.HOME}/test-git-repo"){
                   
                    sh '''
                        git branch
                        git checkout master
                        git add *
                        git merge origin/pipeline_ci
                        git commit -m "Merged develop branch to master"
                     '''
                }
            
            }
            
         }
        */
    }
}
