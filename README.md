# PricingHub

## Environment Setup

1. Start Minikube

       minikube config set memory 16324
       minikube config set cpus 6
       minikube -p pricinghub start
       minikube profile pricinghub
       minikube addons enable ingress

2. Execute the deployment bash file script.

        ./deployservices.sh

    This will create docker images and deploy the services to kubernetes


[![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=Pricing%20HUB%20operatorions&uri=https%3A%2F%2Fraw.githubusercontent.com%2Frafabene%2FPricingHub%2Fmain%2FInsomnia.json)