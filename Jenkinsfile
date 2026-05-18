pipeline {
    agent { label 'k8s-agent-java' }

    environment {
        DEERFLOW_WEBHOOK_URL = 'http://115.190.230.80:8001'
    }

    stages {
        stage('Checkout') {
            steps {
                retry(3) {
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[url: 'https://github.com/high0528/cicd-verify-java.git']],
                        extensions: [
                            [$class: 'CloneOption', timeout: 10, noTags: false, shallow: true, depth: 1]
                        ]
                    ])
                }
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
            script {
                def payload = '{"job_name":"' + env.JOB_NAME + '","build_number":' + env.BUILD_NUMBER + ',"status":"SUCCESS","branch":"' + (env.BRANCH_NAME ?: 'main') + '"}'
                sh "curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H 'Content-Type: application/json' -d \"${payload}\" || true"
            }
        }
        failure {
            script {
                def payload = '{"job_name":"' + env.JOB_NAME + '","build_number":' + env.BUILD_NUMBER + ',"status":"FAILURE","branch":"' + (env.BRANCH_NAME ?: 'main') + '"}'
                sh "curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H 'Content-Type: application/json' -d \"${payload}\" || true"
            }
        }
    }
}
