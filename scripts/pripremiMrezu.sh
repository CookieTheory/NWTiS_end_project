#!/bin/bash
NETWORK=ilucic_mreza_1
sudo docker network create --subnet=200.20.0.0/16 $NETWORK
