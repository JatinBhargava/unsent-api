pipeline {
    agent any

    environment {
        IMAGE_NAME = "jatindocker623/unsent-api"
        IMAGE_TAG  = "1.0.0-SNAPSHOT"
    }

    stages {

        stage('Init') {
            steps {
                echo "Running pipeline for branch: ${env.BRANCH_NAME}"
                sh 'java -version'
                sh 'mvn -version'
                sh 'docker version'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Build Docker Image') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'main'
                }
            }
            steps {
                sh """
                  docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Push Image to Docker Hub') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'main'
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                      echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                      docker push ${IMAGE_NAME}:${IMAGE_TAG}
                    """
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
        always {
            sh 'docker logout || true'
        }
    }
}