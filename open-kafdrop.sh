NODE_PORT=$(kubectl get --namespace kafka -o jsonpath="{.spec.ports[0].nodePort}" services kafdrop)
NODE_IP=$(kubectl get nodes --namespace kafka -o jsonpath="{.items[0].status.addresses[0].address}")
open "http://$NODE_IP:$NODE_PORT"
