pipeline {
    agent any
    environment {
        IMAGE_NAME = "jatindocker623/unsent-api"
    }
    stages {
        stage('Init') {
            when { expression { shouldRun("init") } }
            steps {
                sh 'java -version'
                sh './mvnw -version'
            }
        }
        stage('Build & Test & Package') {
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
                }
            }
        }
        stage('Build Docker Image') {
            when { expression { shouldRun("docker") } }
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }
        stage('Push Docker Image') {
            when { expression { shouldRun("push") } }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        docker logout
                    """
                }
            }
        }
    }
    post {
        success { echo "Pipeline SUCCESS for ${env.BRANCH_NAME}" }
        failure { echo "Pipeline FAILED for ${env.BRANCH_NAME}" }
    }
}
def shouldRun(String stage) {
    switch (true) {
        case env.BRANCH_NAME.startsWith("feature/"):
            return ["init", "mvnDeploy"].contains(stage)
        case env.BRANCH_NAME == "develop":
            return ["init", "mvnDeploy","docker","push"].contains(stage)
        case env.BRANCH_NAME == "master":
            return ["init", "mvnDeploy","docker","push"].contains(stage)
        default:
            return false
    }
}