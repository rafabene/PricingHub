#Installing Kafka Operator
sudo ls
echo "$(minikube ip) myminikube"| sudo tee -a /etc/hosts
mkdir download
wget  -P download -c https://github.com/strimzi/strimzi-kafka-operator/releases/download/0.26.0/strimzi-0.26.0.zip
unzip -o download/strimzi-0.26.0.zip -d download
sed -i '' 's/namespace: .*/namespace: kafka/' download/strimzi-0.26.0/install/cluster-operator/*RoleBinding*.yaml
kubectl create namespace pricinghub || echo "Namespace exists"
kubectl create namespace kafka || echo "Namespace exists"
kubectl apply -f download/strimzi-0.26.0/install/cluster-operator/020-RoleBinding-strimzi-cluster-operator.yaml -n kafka
kubectl apply -f download/strimzi-0.26.0/install/cluster-operator/031-RoleBinding-strimzi-cluster-operator-entity-operator-delegation.yaml -n kafka
kubectl apply -f download/strimzi-0.26.0/install/cluster-operator/ -n kafka
kubectl apply -f kafka/kafka-ephemeral-single.yaml -n kafka
helm upgrade -i kafdrop kafka/kafdrop -n kafka
echo "Aguardando cluster Kafka ficar pronto para poder prosseguir."
kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka
kubectl apply -f kafka/topics.yaml -n kafka

#Installing JMeter
wget -P download -c https://ftp.unicamp.br/pub/apache//jmeter/binaries/apache-jmeter-5.4.1.zip
unzip -o download/apache-jmeter-5.4.1.zip -d download

eval $(minikube -p pricinghub docker-env)
docker build -t infocadastrais infocadastrais/
docker build -t precificacao precificacao/
docker build -t recebepedidos recebepedidos/
docker build -t processador processador/
docker build -t agendamento-pedidos agendamento-pedidos/
docker build -t alarmes-funcionais alarmes-funcionais/
kubectl apply -n pricinghub -f infocadastrais/app.yaml
kubectl apply -n pricinghub -f precificacao/app.yaml
kubectl apply -n pricinghub -f recebepedidos/app.yaml
kubectl apply -n pricinghub -f processador/app.yaml
kubectl apply -n pricinghub -f agendamento-pedidos/app.yaml
kubectl apply -n pricinghub -f alarmes-funcionais/app.yaml
kubectl delete pods -n pricinghub --all --grace-period 0 --force