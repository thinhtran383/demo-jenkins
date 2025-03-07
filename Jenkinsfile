pipeline {
    agent any
    tools {
        maven 'maven_3_9_9'
    }
    environment {
        EC2_USER = 'ubuntu'
        SSH_KEY_CREDENTIAL_ID = 'aws-ec2-ssh-key'
        APP_DIR = '/home/ubuntu'
        JAR_NAME = 'RAG4j-0.0.1-SNAPSHOT.jar'
        EC2_DOMAIN = credentials('ec2-public-domain')
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/thinhtran383/RAG4j']])
            }
        }
        stage('Build Maven Project') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    sshagent([SSH_KEY_CREDENTIAL_ID]) {
                        sh('scp -o StrictHostKeyChecking=no target/${JAR_NAME} ${EC2_USER}@$EC2_DOMAIN:${APP_DIR}/${JAR_NAME}')
                    }
                }
            }
        }

        stage('Stop Previous Application') {
            steps {
                script {
                    sshagent([SSH_KEY_CREDENTIAL_ID]) {
                        sh('ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_DOMAIN} "pkill -f ${JAR_NAME}"')
                    }
                }
            }
        }

        stage('Run Jar on EC2') {
            steps {
                script {
                    sshagent([SSH_KEY_CREDENTIAL_ID]) {
                        sh('ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_DOMAIN} "nohup java -jar ${APP_DIR}/${JAR_NAME} > /dev/null 2>&1 &"')
                    }
                }
            }
        }
    }

    post {
        success {
            emailext(
                    to: 'thinhtran383.au@gmail.com',
                    subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: "The build was successful!\nCheck it at: ${env.BUILD_URL}"
            )
        }

        failure {
            emailext(
                    to: 'thinhtran383.au@gmail.com',
                    subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: "The build failed!\nCheck logs at: ${env.BUILD_URL}."
            )
        }
    }
}