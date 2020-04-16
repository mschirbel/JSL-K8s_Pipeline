def call(Map args) {
  def containers = [
    containerTemplate(name: 'node', image: 'node', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true),
    containerTemplate(name: 'jnlp', image: 'jenkins/jnlp-slave:3.35-5-alpine', ttyEnabled: true)
  ]
  def REGISTRY_CONFIG = [
    url: "https://hub.docker.com/repository/docker/marceloschirbel/jsl-medium",
    credentials: "90f8072d-4194-4c7e-807b-90e4a4135093"
  ]
  def registryRepository = args.registry 
  def name = args.name
  def label = "job-${name}-${UUID.randomUUID().toString()}".take(15)
  def tag = "${UUID.randomUUID().toString()}".take(5)
  def imageName = "${registryRepository}:${tag}"

  podTemplate(label: label, containers: containers, serviceAccount: 'jenkins') {
    node(label) {
        stage('Checkout') {
          checkout scm
        }  
        container('node') {
            stage('Install Dependencies') {
              sh 'npm install'
            }
            stage('Build and publish image to Registry') {
                  container('docker') {
                    docker.withRegistry(REGISTRY_CONFIG.url, REGISTRY_CONFIG.credentials) {
                      docker.build(imageName).push()
                    }
                  }
              }
          }   
    }
  }
}
