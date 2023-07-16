#!/bin/bash
echo "DOCKER STOP"
docker stop ilucic_payara_micro_aplikacija_2
echo "DOCKER REMOVE"
docker rm ilucic_payara_micro_aplikacija_2
echo "DOCKER PRIPREMI"
./scripts/pripremiSliku.sh
echo "DOCKER POKRENI"
./scripts/pokreniSliku.sh