docker build -t infocadastrais InformacoesCadastrais/
docker build -t precificacao Precificacao/
kubectl create namespace pricinghub || echo "Namespace exists"
kubectl apply -n pricinghub -f InformacoesCadastrais/app.yaml
kubectl apply -n pricinghub -f Precificacao/app.yaml
kubectl delete pods -n pricinghub --all --grace-period 0 --force