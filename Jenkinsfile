pipeline {
  agent any
  stages {
    stage('clean') {
      parallel {
        stage('clean') {
          steps {
            sh 'gradle -v'
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