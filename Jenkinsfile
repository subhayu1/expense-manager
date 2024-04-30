pipeline {
    // on github push, checkout code and build, tag, and push docker image to a private remote registry
    agent any
    environment {
        DOCKER_REGISTRY = "registry.ops.rimalholdings.internal:11000"
        REPO_NAME_EM_SERVICE = "git@github.com:subhayu1/expense-manager.git"
        IMAGE_TAG = "latest"
        IMAGE_NAME_EM_SERVICE = "em-service"
        GIT_BRANCH = "main"
       // JWT_PUBLIC_KEY_BASE64 ="LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFnZFBwaFFRR3krb0tVUFRValppMQpYWnNoT1QxWmFRYU9QckJwSytCeUFlQlhCSk10dktNYkF1QWJITXhkRE5IVHo1QUphWFJYQ0Z6Zi9DNVpDNW1wCk9KK0F3djRKUXVDU3JkRjBWeU9tc1BuYnFwM2t5S3lUT0RRYWJVWWJvc1YxTVhKdEJveFhxOE94dHJXZUMzbkEKV3cwU1kyMUxEcEVJWWg0NHY0TEEyRjRoRUFwMkVjSmZ1YTRPalBFeXhsZ0JCTkJNYjg0UEwrc3pxV203NFg4cwpvelZ3ekozTmdlcUtMcnVzUFVZOWR2MjAwMXl0WGFJNmFHeFo0cmVNYWtsZmVKaWlXbGNDS3pZNGdQaXdoTVlLCkFlVks1bko4ekovZkl1S1ovdEVsc1FxUExTeG1ldnZ1aU9WVlhLcGhCdmF4MEhnQnZSblF3UmNhVkpGYzF2OUsKaVFJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg=="
      //  JWT_PRIVATE_KEY_BASE64 = "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2QUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktZd2dnU2lBZ0VBQW9JQkFRQ0IwK21GQkFiTDZncFEKOU5TTm1MVmRteUU1UFZscEJvNCtzR2tyNEhJQjRGY0VreTI4b3hzQzRCc2N6RjBNMGRQUGtBbHBkRmNJWE4vOApMbGtMbWFrNG40REMvZ2xDNEpLdDBYUlhJNmF3K2R1cW5lVElySk00TkJwdFJodWl4WFV4Y20wR2pGZXJ3N0cyCnRaNExlY0JiRFJKamJVc09rUWhpSGppL2dzRFlYaUVRQ25ZUndsKzVyZzZNOFRMR1dBRUUwRXh2emc4djZ6T3AKYWJ2aGZ5eWpOWERNbmMyQjZvb3V1Nnc5UmoxMi9iVFRYSzFkb2pwb2JGbml0NHhxU1Y5NG1LSmFWd0lyTmppQQorTENFeGdvQjVVcm1jbnpNbjk4aTRwbiswU1d4Q284dExHWjYrKzZJNVZWY3FtRUc5ckhRZUFHOUdkREJGeHBVCmtWelcvMHFKQWdNQkFBRUNnZ0VBQWtId3Vud0prTGZ4Qmp1ZHF4a3IxVFQrZ01mdTh6YzJsOE02VHZXa0VlR0kKYmRkUDJTc0diQmhBekhGRzBITmhJU2UrYkNlbVlFbzZLR0RYY2s3Um5SOFQ3b0Fib2Q2MUdrSHRTWnNnZWMwYwplMlRydnF3V2tzaUFLYVNKbER6bUJ1NDRGR0F5SVpYaGRCMklJaWV6dHptYzExOEdDanF1S0VPaGhiNEVzN1ozCitrOTVSelNjMlkzbVZ2cXBkOXg5bTdIS3RmMnMxTzZINUhnUjc2eFlTWmRJSFhqdFBsNGFMRkJyU0pSOHFMZSsKOVRvZ3ZiU3dIREwrdmhwNUdRUFVJY0FWU2w1TWNLTHhSZkVsWlhJek5TL1FmcEs1dnZWSmpqOUl6Nm1FT2lPdApPbENzV214MHFyanFwQTZuRkN5WXFkcWFybzJrNlVZMCtweWVOdzAzQVFLQmdRQzNHYTBOdFVjTFBvTzgvM2JZCjZDV2pTamxrTFZnNHJIZ1BVSzdLR3lHeWp5WkZXWmp3eXRGc1J1QWgwRzFRUzYyNUxkNU42QjV1d1Z5d3pEMFIKQW1VL0NVTUJvSUtmSVg5NWpvQmsrYW0rTDhtMzdVOXMyN09leGhaREtnbE11YmYxVEFkWUYySHZpakJ0bmJHRwp6U1dJa0NPN0s4YkJJYUFUREJMRDUzQWZpUUtCZ1FDMWhIeFV6a2kzN2RCS3JCeTMwUUkzZjlCTGRQSFIveXVpCmwzZlQ3WTZZdWplNTdGNXFRT3NkRlFSREUxcUxJRVUreWxlMjlybGlZVkQ4ZlZkUFBWYmFzaXZkVlFoWlIraDMKZTZDODJlaG1JT1RBSHo5WnpncHZCZERuSzkyVDZYSHBVV1ovNUhIWnFsMk5pR0E5QlNENG9RQ3Myb25hR0FMcgpXMWVUN1hnVEFRS0JnRkpkaVh4V0pKeEpSZWU2SDVOWmNyenV3Ynh2clhUcEdLVUREVzI3SU1CN0pxTWh0K3Z4CndKMXRrSnVGYi9jajVBVWVQaGRicUh6NEhLTW5iUHorejk2NFl0MFRnRTduT2JuTHdiUXFueEgvMjBVU3lwelUKaEF1VFhpWmVmWXAzTE9wNmhmODVuWU9zN1RZU0x1N056K21MdFpjWURUT0oxY0ZidUFGS1ZpVEpBb0dBUkF1KwpjQ3hFcG03SVByWDJyaEgwT3NuM1U5SXJOUVhyWGJCYWcxL3lyTjVpTlppZWJFYVozUFViKytrdGNDcFF5eEVmCmdMOU5EOHhiaDh1VkIyZC9QOHk3Rk9YamsxU1ptUEVIZU1SWGtyR1NEMU9uZEFzQmJrMUgydUZlMXl3VVA5cjgKQ2NXaHlHOU5VdGxrK25zSnVTcmlEZXArOHZzOVhPMkpEek5lYUFFQ2dZQXJBcFVJdzhLaVgvYit3SnpuemxFNApZSFg0Q2EyT3pzb2Vpb0FkdzlHcEx3UjFQaDliQ3hXdHZIbkJzaGY2dXB4UXFFRHdqWTkyZXpQRDcvUnpwY0wwCkkzQXhGQ3VYZlR6dDlQSERHRS9ZYzNUd0ZnQnd3dEdiQW5Ra2RiS21oODkyTTlwNU9teUgrVkQ0Vkp4OG5hZVcKbnU0djBQb20wNGUzdE5UMkVJYmRPdz09Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0K"
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
        stage(' Extract git info') {
            steps {
                script {
                    env.GIT_COMMIT = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    env.GIT_BRANCH = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    env.GIT_URL = sh(script: 'git config --get remote.origin.url', returnStdout: true).trim()
                    env.GIT_COMMIT_MESSAGE = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    //write to a file
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
                    //run health-check.sh script in the root directory
                    sh 'sh health-check.sh'

                }
                script {
                    env.PATH = "${env.PATH}:/usr/local/bin"
                    //docker.build("${DOCKER_REGISTRY}/${IMAGE_NAME_EM_SERVICE}:${IMAGE_TAG}", ".", "--platform linux/arm64")
                    sh "docker build --platform=linux/arm64 -t registry.ops.rimalholdings.internal:11000/em-service:latest ."
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