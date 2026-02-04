pipeline {
    agent none

    environment {
        IMAGE_NAME = "jatindocker623/unsent-api"
        VERSION = "1.0.0-SNAPSHOT"
    }

    stages {

        stage('Build with Maven') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:27'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh """
                  docker build -t ${IMAGE_NAME}:${VERSION} .
                """
            }
        }

        stage('Push Image to Docker Hub') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'master'
                }
            }
            agent {
                docker {
                    image 'docker:27'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                withCredentials([
                  usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                  )
                ]) {
                    sh """
                      echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                      docker push ${IMAGE_NAME}:${VERSION}
                      docker logout
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
    }
}