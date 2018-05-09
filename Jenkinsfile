 pipeline {

    agent none

     stages {

        //master机器执行编译打包上传
        stage('Checkout') {     
            
            agent {
                label 'master'
            }

            steps {

                echo 'start to check from svn server'

                checkout([$class: 'SubversionSCM', 
                        additionalCredentials: [], 
                        excludedCommitMessages: '', 
                        excludedRegions: '', 
                        excludedRevprop: '', 
                        excludedUsers: '', 
                        filterChangelog: false, 
                        ignoreDirPropChanges: false, 
                        includedRegions: '', 
                        locations: [[credentialsId: 'edefdbb4-828a-4c43-b6b3-20eec1d24b5c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://100.100.10.241/svn/code/trunk/aux-plat/auxplat-wechat-messager']], workspaceUpdater: [$class: 'UpdateUpdater']])

            }        
        }

        stage('Checkout to prod') {     
            
            agent {
                label 'prod'
            }

            steps {

                checkout([$class: 'SubversionSCM', 
                        additionalCredentials: [], 
                        excludedCommitMessages: '', 
                        excludedRegions: '', 
                        excludedRevprop: '', 
                        excludedUsers: '', 
                        filterChangelog: false, 
                        ignoreDirPropChanges: false, 
                        includedRegions: '', 
                        locations: [[credentialsId: 'edefdbb4-828a-4c43-b6b3-20eec1d24b5c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://100.100.10.241/svn/code/trunk/aux-plat/auxplat-wechat-messager']], workspaceUpdater: [$class: 'UpdateUpdater']])

            }        
        }

        stage('mvn build & test'){

            agent {
                label 'master'
            }

            steps {

                 echo 'start to mvn build'

                 sh "docker run -i --rm -v ${env.WORKSPACE}:/usr/src/workspace -v /root/.m2/repository/:/root/.m2/repository -v /opt/maven/settings.xml:/root/.m2/settings.xml -w /usr/src/workspace maven mvn clean package -U -Dmaven.test.skip=true"

            }
        }

        stage('build and push image'){

            agent {
                label 'master'
            }

            steps {

                echo 'build and push image'

                script {

                    docker.withRegistry('http://100.100.20.216/', 'd8eb1a49-93d7-49be-a15d-fa9361fb0169') {
                        docker.build('aux-jt/auxplat-wechat-messager').push('latest')
                    }
                }

                
            }
        }

        stage('deploy to test'){

            agent {
                label 'master'
            }
            
            steps {

                echo 'deploy to test'
                
                //sh "docker-compose -f docker-compose-dev.yml up -d"
                
                //sh '''CID=$(docker ps | grep auxplat-wechat-messager-test | awk \'{print $1}\')

                //if [ "$CID" != "" ];then
                //    docker rm -f $CID
                //fi'''

                //sh "docker run -d --name auxplat-wechat-messager-test -p 5120:5120 -e SPRING_PROFILES_ACTIVE=dev --expose=5120 -e EUREKA_INSTANCE_IP-ADDRESS=100.100.20.216  100.100.20.216/aux-jt/auxplat-wechat-messager"
            }
        }

        stage('deploy to prod'){

            agent {
                label 'prod'
            }

            steps {

                timeout(time: 8, unit: 'HOURS') {
                    input message: "请确认是否要将服务上线到正式系统？"
                }

                echo 'deploy to prod'

                sh "docker stack deploy --compose-file docker-compose.yml wechat"
            }
        }
    }
 }