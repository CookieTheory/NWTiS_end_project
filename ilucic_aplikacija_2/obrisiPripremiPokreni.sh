#!/bin/bash
echo "DOCKER STOP"
sudo docker stop ilucic_payara_micro_aplikacija_2
echo "DOCKER REMOVE"
sudo docker rm ilucic_payara_micro_aplikacija_2
echo "DOCKER PRIPREMI"
sudo ./pripremiSliku.sh
echo "DOCKER POKRENI"
sudo ./pokreniSliku.sh
