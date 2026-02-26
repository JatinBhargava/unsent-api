pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = "jatindocker623"
        DOCKER_PLATFORM = "linux/amd64"
    }

    stages {

        stage('Init') {
            when { expression { shouldRun("init") } }
            steps {
                sh 'java -version'
                sh './mvnw -version'
                sh 'docker version'
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

        stage('Setup Docker Buildx') {
            when { expression { shouldRun("docker") } }
            steps {
                sh '''
                  docker buildx create --use --name unsent-builder || true
                  docker buildx inspect --bootstrap
                '''
            }
        }

        stage('Build & Push Docker Images (Render)') {
            when { expression { shouldRun("push") } }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {

                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'

                    script {
                        def services = [
                            api: 'unsent-api',
                            listener: 'unsent-listener',
                            batch: 'unsent-batch'
                        ]

                        services.each { name, dir ->
                            echo "Building & pushing ${name} for Render (amd64)"

                            sh """
                              docker buildx build \
                                --platform ${DOCKER_PLATFORM} \
                                -t ${DOCKER_REGISTRY}/${name}:${IMAGE_TAG} \
                                -t ${DOCKER_REGISTRY}/${name}:latest \
                                --push \
                                ${dir}
                            """
                        }
                    }

                    sh 'docker logout'
                }
            }
        }
    }

    post {
        success {
            echo "✅ Render images published successfully for ${env.BRANCH_NAME}"
        }
        failure {
            echo "❌ Pipeline FAILED for ${env.BRANCH_NAME}"
        }
    }
}

def shouldRun(String stage) {
    switch (true) {
        case env.BRANCH_NAME.startsWith("feature/"):
            return ["init", "mvnDeploy"].contains(stage)

        case env.BRANCH_NAME == "develop":
            return ["init", "mvnDeploy", "docker", "push"].contains(stage)

        case env.BRANCH_NAME == "master":
            return ["init", "mvnDeploy", "docker", "push"].contains(stage)

        default:
            return false
    }
}