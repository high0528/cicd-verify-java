pipeline {
    agent { label 'k8s-agent-java' }

    environment {
        DEERFLOW_WEBHOOK_URL = 'http://115.190.230.80:8001'
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                // 配置 Git 镜像代理，解决 K8s Pod 访问 GitHub 不稳定的问题
                sh 'git config --global url."https://ghfast.top/github.com/".insteadOf https://github.com/'
                sh 'git config --global url."https://ghfast.top/git@github.com:".insteadOf git@github.com:'
                
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/high0528/cicd-verify-java.git']],
                    extensions: [
                        [$class: 'CloneOption', timeout: 2, noTags: false, shallow: true, depth: 1]
                    ]
                ])
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
