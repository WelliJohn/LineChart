pipeline {
  agent any
  stages {
    stage('sudo') {
      steps {
        sh 'chmod +x gradlew'
      }
    }
    stage('clean') {
      steps {
        sh './gradlew clean'
      }
    }
  }
}