pipeline {
  agent any
  stages {
    stage('clean') {
      parallel {
        stage('clean') {
          steps {
            sh 'gradlew clean'
          }
        }
        stage('build') {
          steps {
            sh 'gradlew build'
          }
        }
      }
    }
  }
}