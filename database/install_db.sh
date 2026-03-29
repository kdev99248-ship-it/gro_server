#!/bin/bash

sudo dnf install -y dnf-plugins-core -y
sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo dnf install docker-ce docker-ce-cli containerd.io -y
sudo systemctl enable --now docker
sudo docker pull mysql:5.7
echo "Database install successfully!"
echo "Usage : ./start_db.sh"