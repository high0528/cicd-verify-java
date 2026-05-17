// Re-trigger #2 - 20260517_151942
// Auto-trigger rebuild - k8s-agent-java template added
pipeline {
    agent { label 'k8s-agent-java' }

    environment {
        DEERFLOW_WEBHOOK_URL = 'http://115.190.230.80:8001'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/high0528/cicd-verify-java.git'
            }
        }

        stage('Build') {
            steps {
                container('java') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                container('java') {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        success {
            sh "curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H 'Content-Type: application/json' -d '{\\\"job_name\\\":\\\"${env.JOB_NAME}\\\",\\\"build_number\\\":${env.BUILD_NUMBER},\\\"status\\\":\\\"SUCCESS\\\",\\\"branch\\\":\\\"${env.BRANCH_NAME ?: 'main'}\\\"}' || true"
        }
        failure {
            sh "curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H 'Content-Type: application/json' -d '{\\\"job_name\\\":\\\"${env.JOB_NAME}\\\",\\\"build_number\\\":${env.BUILD_NUMBER},\\\"status\\\":\\\"FAILURE\\\",\\\"branch\\\":\\\"${env.BRANCH_NAME ?: 'main'}\\\"}' || true"
        }
    }
}
