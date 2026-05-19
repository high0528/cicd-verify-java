pipeline {
    agent { label 'k8s-agent-java' }

    environment {
        DEERFLOW_WEBHOOK_URL = 'http://115.190.230.80:8001'
        APP_NAME = 'order-service'
        NAMESPACE = 'default'
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
                        userRemoteConfigs: [[url: env.GIT_URL, credentialsId: 'github-token']],
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

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "🚀 开始部署到 Kubernetes..."

                    sh "helm upgrade --install ${APP_NAME} ./charts " +
                       "-f values.yaml --namespace ${NAMESPACE} " +
                       "--wait --timeout 5m --atomic"
                }
            }
            post {
                always {
                    script {
                        echo "📊 收集部署后健康数据..."
                        sh 'sleep 30'

                        def healthCheck = sh(
                            script: "kubectl get pods -l app=${APP_NAME} -n ${NAMESPACE} --no-headers 2>/dev/null | awk '{print \\$1, \\$3}' | head -5 || echo 'N/A'",
                            returnStdout: true
                        ).trim()

                        def deployLog = currentBuild.rawBuild.getLog(300).join(' ').take(3000)

                        def payload = groovy.json.JsonOutput.toJson([
                            job_name    : env.JOB_NAME,
                            build_number: env.BUILD_NUMBER.toInteger(),
                            status      : currentBuild.currentResult,
                            branch      : env.BRANCH_NAME ?: 'main',
                            event_type  : 'deploy',
                            app_name    : APP_NAME,
                            namespace   : NAMESPACE,
                            health_data : healthCheck,
                            deploy_log  : deployLog
                        ])

                        writeFile file: '_deploy_payload.json', text: payload
                        sh 'curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H "Content-Type: application/json" -d @_deploy_payload.json || true'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                import groovy.json.JsonOutput
                def payload = JsonOutput.toJson([
                    job_name    : env.JOB_NAME,
                    build_number: env.BUILD_NUMBER.toInteger(),
                    status      : currentBuild.currentResult,
                    branch      : env.BRANCH_NAME ?: 'main'
                ])
                writeFile file: '_webhook_payload.json', text: payload
                sh 'curl -s -X POST ${DEERFLOW_WEBHOOK_URL}/webhooks/jenkins -H "Content-Type: application/json" -d @_webhook_payload.json || true'
            }
        }
    }
}