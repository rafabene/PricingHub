# PricingHub

## Environment Setup

1. Start Minikube

       minikube -p pricinghub start
       minikube profile pricinghub`

 
2. Point your docker client to minikube

       eval $(minikube -p pricinghub docker-env)

3. Execute the deployment bash file script.

        ./deployservices.sh

    This will create docker images and deploy the services to kubernetes