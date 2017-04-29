node {
  env.PATH = "${tool 'Maven 3'}/bin:${env.PATH}"

  stage('Checkout') {
    checkout scm
  }

  stage('Build') {
    sh 'mvn clean package -DskipTests'
    if (env.BRANCH_NAME == 'master') {
      archiveArtifacts artifacts: '**/target/10poengsappen.jar', fingerprint: true
    }
  }

  stage('Test') {
    sh 'mvn test'
  }
}
