pipeline {
    agent any


    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Build & Test') {
            steps {
                echo '── Building and testing (Maven Wrapper) ──'
                bat 'mvnw.cmd clean test' 
            }
        }
        stage('Deploy') {
            steps {
                echo '── Running Ansible deployment ──'
                bat 'echo "Ansible playbook execution simulated successfully."'
            }
        }
    }

    post {
        success {
            echo '── Build finished successfully! ──'
        }
        failure {
            echo '── Build failed - sending failure email ──'
            mail to: 'srengty@gmail.com',
                 subject: "❌ FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Build #${env.BUILD_NUMBER} failed. Console output: ${env.BUILD_URL}console"
        }
    }
}