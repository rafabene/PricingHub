# PricingHub

## Environment Setup

1. Start Minikube

       minikube config set memory 16324
       minikube config set cpus 6
       minikube -p pricinghub start
       minikube profile pricinghub`

2. Execute the deployment bash file script.

        ./deployservices.sh

    This will create docker images and deploy the services to kubernetes