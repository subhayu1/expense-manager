pipeline {
    // on github push, checkout code and build, tag, and push docker image to a private remote registry
    agent any
    environment {
        DOCKER_REGISTRY = "registry.ops.rimalholdings.internal:11000"
        REPO_NAME_EM_SERVICE = "git@github.com:subhayu1/expense-manager.git"
        IMAGE_TAG = "latest"
        IMAGE_NAME_EM_SERVICE = "em-service"
        GIT_BRANCH = "main"
    }
    triggers {
        githubPush()
    }
    stages {
        
        stage('Checkout EM_SERVICE') {
            steps {
                git url: REPO_NAME_EM_SERVICE, branch: 'main'
            }
        }
        stage('Build EM_SERVICE') {
            steps {
                script {
                    env.PATH = "${env.PATH}:/usr/local/bin"
                    docker.build("${DOCKER_REGISTRY}/${IMAGE_NAME_EM_SERVICE}:${IMAGE_TAG}", ".", "--platform linux/arm64")
                }
            }
        }
        stage('Push EM_SERVICE') {
            steps {
                script {
                    docker.image("${DOCKER_REGISTRY}/${IMAGE_NAME_EM_SERVICE}:${IMAGE_TAG}").push()
                }
            }
        }
    }
}