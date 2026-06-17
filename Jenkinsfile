pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    triggers {
        pollSCM('H/5 * * * *')   
    }

    environment {
        EMAIL_TO = "srengty@gmail.com"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Meyhieng/Final-Devops.git'
            }
        }

        stage('Clean Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Deploy with Ansible') {
            steps {
                sh 'ansible-playbook -i inventory deploy.yml'
            }
        }
    }

    post {

        success {
            echo 'BUILD SUCCESSFUL'

            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }

        failure {
            echo 'BUILD FAILED'

            mail to: "${EMAIL_TO}",
                 subject: "Jenkins Build FAILED - ${env.JOB_NAME}",
                 body: "Check here: ${env.BUILD_URL}"
        }
    }
}