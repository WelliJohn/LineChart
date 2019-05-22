pipeline {
  agent any
  stages {
    stage('clean') {
      parallel {
        stage('clean') {
          steps {
            sh 'gradle clean'
          }
        }
        stage('build') {
          steps {
            sh 'gradle build'
          }
        }
      }
    }
  }
}