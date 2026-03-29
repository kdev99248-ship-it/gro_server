#!/bin/bash
sudo yum install -y nano
sudo yum install -y htop
sudo yum install -y java-21-openjdk.x86_64
sudo dnf install epel-release -y
sudo dnf install p7zip p7zip-plugins -y
sudo dnf install httpd -y
sudo dnf install php php-cli php-mysqlnd php-fpm php-mbstring php-xml -y
sudo dnf install php-json -y



# start service
sudo systemctl enable --now httpd
systemctl status httpd