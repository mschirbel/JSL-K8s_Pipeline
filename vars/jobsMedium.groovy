def call(Map args) {
  def DEFAULT_CONTAINERS = [
    containerTemplate(name: 'node', image: 'node:dubnium', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true)
  ]
  def DEFAULT_VOLUMES = [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
  def REGISTRY_CONFIG = [
    url: "https://hub.docker.com/repository/docker/marceloschirbel/jsl-medium",
    credentials: "90f8072d-4194-4c7e-807b-90e4a4135093"
  ]
  def name = args.name
  def label = "job-${name}-${UUID.randomUUID().toString()}".take(15)
  def tag = "${UUID.randomUUID().toString()}".take(5)
  def podContainers = DEFAULT_CONTAINERS + containers
  def podVolumes = DEFAULT_VOLUMES + volumes
  def imageName = "${registryRepository}:${tag}"

  podTemplate(label: label, containers: podContainers, volumes: podVolumes, serviceAccount: 'jenkins') {
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
                      docker
                        .build(imageName)
                        .push()
                    }
                  }
              }
          }   
    }
  }
}
