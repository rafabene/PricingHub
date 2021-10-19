#Installing Kafka Operator
eval $(minikube -p pricinghub docker-env)
docker build -t alarmes-funcionais alarmes-funcionais/
kubectl apply -n pricinghub -f alarmes-funcionais/app.yaml
kubectl delete pods -l app=alarmes-funcionais -n pricinghub --grace-period 0 --force
kubectl delete pods -n pricinghub -l app=agendamento-pedidos --grace-period 0 --force