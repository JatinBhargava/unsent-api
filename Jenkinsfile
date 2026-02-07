pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "jatindocker623"
    }
    stages {
        stage('Init') {
            when { expression { shouldRun("init") } }
            steps {
                sh 'java -version'
                sh './mvnw -version'
            }
        }
        stage('Build & Test (All Modules)') {
            when { expression { shouldRun("mvnDeploy") } }
            steps {
                sh './mvnw clean verify'
            }
        }
        stage('Resolve Version') {
            when { expression { shouldRun("docker") } }
            steps {
                script {
                    env.IMAGE_TAG = sh(
                        script: "./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout",
                        returnStdout: true
                    ).trim()
                    echo "Resolved version: ${IMAGE_TAG}"
                }
            }
        }
        stage('Build Docker Images') {
            when { expression { shouldRun("docker") } }
            steps {
                script {
                    def services = [
                        api: [
                            dir: 'unsent-api',
                            image: "${DOCKER_REGISTRY}/unsent-api"
                        ],
                        listener: [
                            dir: 'unsent-listener',
                            image: "${DOCKER_REGISTRY}/unsent-listener"
                        ],
                        batch: [
                            dir: 'unsent-batch',
                            image: "${DOCKER_REGISTRY}/unsent-batch"
                        ]
                    ]

                    services.each { name, svc ->
                        echo "Building image for ${name}"
                        sh """
                            docker build \
                              -t ${svc.image}:${IMAGE_TAG} \
                              ${svc.dir}
                        """
                    }
                }
            }
        }
        stage('Push Docker Images') {
            when { expression { shouldRun("push") } }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    script {
                        sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                        def images = [
                            "${DOCKER_REGISTRY}/unsent-api:${IMAGE_TAG}",
                            "${DOCKER_REGISTRY}/unsent-listener:${IMAGE_TAG}",
                            "${DOCKER_REGISTRY}/unsent-batch:${IMAGE_TAG}"
                        ]
                        images.each { img ->
                            sh "docker push ${img}"
                        }
                        sh 'docker logout'
                    }
                }
            }
        }
    }
    post {
        success {
            echo "Pipeline SUCCESS for branch: ${env.BRANCH_NAME}"
        }
        failure {
            echo "Pipeline FAILED for branch: ${env.BRANCH_NAME}"
        }
    }
}
def shouldRun(String stage) {
    switch (true) {
        case env.BRANCH_NAME.startsWith("feature/"):
            return ["init", "mvnDeploy"].contains(stage)

        case env.BRANCH_NAME == "develop":
            return ["init", "mvnDeploy", "docker"].contains(stage)

        case env.BRANCH_NAME == "master":
            return ["init", "mvnDeploy", "docker", "push"].contains(stage)

        default:
            return false
    }
}