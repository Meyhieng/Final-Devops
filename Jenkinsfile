pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        DEVELOPER_EMAIL = ''
        CC_EMAIL = 'srengty@gmail.com'
        GIT_REPO = 'https://github.com/YOUR_USERNAME/idcard-management.git'
        ANSIBLE_PLAYBOOK = 'ansible/deploy.yml'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '── Cloning repository ──'
                git branch: 'main',
                    url: "${GIT_REPO}"
                script {
                    DEVELOPER_EMAIL = sh(
                        script: "git log -1 --pretty=format:'%ae'",
                        returnStdout: true
                    ).trim()
                    echo "Last commit by: ${DEVELOPER_EMAIL}"
                }
            }
        }

        stage('Build') {
            steps {
                echo '── Building with Maven ──'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo '── Running Tests ──'
                sh './mvnw test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deploy with Ansible') {
            when {
                expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo '── Deploying via Ansible ──'
                sh "ansible-playbook ${ANSIBLE_PLAYBOOK}"
            }
        }
    }

    post {
        failure {
            echo '── Build FAILED — sending email ──'
            mail to: "${DEVELOPER_EMAIL}, ${CC_EMAIL}",
                 subject: "❌ Jenkins Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """
Build failed for job: ${env.JOB_NAME}
Build number: ${env.BUILD_NUMBER}
Branch: ${env.GIT_BRANCH}
Committed by: ${DEVELOPER_EMAIL}

Check console output:
${env.BUILD_URL}console

Regards,
Jenkins CI
"""
        }
        success {
            echo '── Build SUCCEEDED ──'
            mail to: "${DEVELOPER_EMAIL}, ${CC_EMAIL}",
                 subject: "✅ Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """
Build succeeded for job: ${env.JOB_NAME}
Build number: ${env.BUILD_NUMBER}

Check console output:
${env.BUILD_URL}console

Regards,
Jenkins CI
"""
        }
    }
}