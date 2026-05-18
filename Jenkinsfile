pipeline {
    agent { label 'k8s-agent-java' }

    environment {
        DEERFLOW_WEBHOOK_URL = 'http://115.190.230.80:8001'
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            options {
                timeout(time: 2, unit: 'MINUTES')
            }
            steps {
                retry(2) {
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[url: 'https://github.com/high0528/cicd-verify-java.git']],
                        extensions: [
                            [$class: 'CloneOption', timeout: 1, noTags: false, shallow: true, depth: 1]
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
        always {
            script {
                def payload = '{"job_name":"' + env.JOB_NAME + '","build_number":' + env.BUILD_NUMBER + ',"status":"' + currentBuild.currentResult + '","branch":"' + (env.BRANCH_NAME ?: 'main') + '"}'
                writeFile file: '_webhook_payload.json', text: payload
                sh 'curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H "Content-Type: application/json" -d @_webhook_payload.json || true'
            }
        }
    }
}
