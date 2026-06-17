pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                echo '── Building and testing on Windows ──'
                bat 'mvnw.cmd clean test'
            }
        }
    }
}