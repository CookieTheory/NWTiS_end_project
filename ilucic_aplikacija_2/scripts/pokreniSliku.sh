#!/bin/bash
NETWORK=ilucic_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=ilucic_payara_micro_aplikacija_2 \
  --hostname=ilucic_payara_micro_aplikacija_2 \
  ilucic_payara_micro_aplikacija_2:6.2023.4 \
  --deploy /opt/payara/deployments/ilucic_aplikacija_2-1.0.0.war \
  --contextroot ilucic_aplikacija_2 \
  --noCluster &

wait
