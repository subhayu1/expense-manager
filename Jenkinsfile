pipeline {
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
        stage('Extract Git Info') {
            steps {
                script {
                    env.GIT_COMMIT = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    env.GIT_BRANCH = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    env.GIT_URL = sh(script: 'git config --get remote.origin.url', returnStdout: true).trim()
                    env.GIT_COMMIT_MESSAGE = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()

                    // Check if git-info.txt exists, if not, create it and write git information
                    if (!fileExists('git-info.txt')) {
                        echo "File does not exist, creating git-info.txt"
                        sh 'touch git-info.txt'
                    }
                    echo "Writing information to git-info.txt"
                    writeFile file: 'git-info.txt', text: "GIT_COMMIT=${env.GIT_COMMIT}\n" +
                            "GIT_COMMIT_MESSAGE=${env.GIT_COMMIT_MESSAGE}\n" +
                            "GIT_BRANCH=${env.GIT_BRANCH}\n" +
                            "GIT_URL=${env.GIT_URL}"
                }
            }
        }
        stage('Build EM_SERVICE') {
            steps {
                script {
                    // Ensure docker is available in the path
                    env.PATH = "${env.PATH}:/usr/local/bin"
                    sh "docker build --platform=linux/arm64 -t ${DOCKER_REGISTRY}/${IMAGE_NAME_EM_SERVICE}:${IMAGE_TAG} ."
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
