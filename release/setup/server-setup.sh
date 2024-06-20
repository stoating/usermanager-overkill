#!/usr/bin/env bash

# this was taken and modified from biff
# this is not perfect, but it will get you pretty far

# setting for script
# -x: print commands
# -e: exit on error
set -x
set -e


echo waiting for apt to finish
while (ps aux | grep [a]pt); do
  sleep 3
done


# Dependencies
# Update your environment! Your server smells stale.
apt-get update
apt-get upgrade
# For your repository
apt-get -y install git
# For Firewall
apt-get -y install ufw
# For Certbot (Let's Encrypt) for HTTPS
apt-get -y install snapd
# For Nginx
apt-get -y install nginx


# Docker Setup
# https://docs.docker.com/engine/install/ubuntu/
# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates
sudo apt-get install curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update

# Install Docker
sudo apt-get install docker-ce
sudo apt-get install docker-ce-cli
sudo apt-get install containerd.io
sudo apt-get install docker-buildx-plugin
sudo apt-get install docker-compose-plugin

# Install Docker Compose
# https://docs.docker.com/compose/install/
sudo apt install docker-compose

# Create app user
# Copy the public key from the root user to the app user
useradd -m app
mkdir -m 700 -p /home/app/.ssh
cp /root/.ssh/authorized_keys /home/app/.ssh
chown -R app:app /home/app/.ssh


# Systemd service
cat > /etc/systemd/system/app.service << EOD
[Unit]
Description=app
StartLimitIntervalSec=500
StartLimitBurst=5

[Service]
User=app
Restart=on-failure
RestartSec=5s
WorkingDirectory=/home/app
ExecStart=/bin/sh -c "docker compose up -d"

[Install]
WantedBy=multi-user.target
EOD

# enable the service
systemctl enable app

# put the following in /etc/systemd/journald.conf
# this will make the logs persistent
cat > /etc/systemd/journald.conf << EOD
[Journal]
Storage=persistent
EOD

# apply the changes
systemctl restart systemd-journald

# put the following in /etc/sudoers.d/restart-app
# this delegates the ability to perform the listed functions to the app user
# without needing a password
cat > /etc/sudoers.d/restart-app << EOD
app ALL= NOPASSWD: /bin/systemctl reset-failed app.service
app ALL= NOPASSWD: /bin/systemctl restart app
app ALL= NOPASSWD: /usr/bin/systemctl reset-failed app.service
app ALL= NOPASSWD: /usr/bin/systemctl restart app
EOD

# set the permissions
# owner = read
# group = read
# other = none
chmod 440 /etc/sudoers.d/restart-app

# Firewall
ufw allow OpenSSH
ufw --force enable

# HTTPS
snap install core
snap refresh core
snap install --classic certbot

# create a symbolic link
# makes 'certbot' directly available from the command line
ln -s /snap/bin/certbot /usr/bin/certbot

# Nginx
rm /etc/nginx/sites-enabled/default
cat > /etc/nginx/sites-available/app << EOD
server {
    listen 80;
    listen [::]:80;
    server_name _;

    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

    location / {
        proxy_http_version 1.1;
        proxy_cache_bypass \$http_upgrade;

        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;

        proxy_pass http://localhost:8080;
    }
}
EOD

# create a symbolic link
# uses bash brace expansion to create two links
# /etc/nginx/sites-available/app -> /etc/nginx/sites-enabled/app
ln -s /etc/nginx/sites-{available,enabled}/app

# Firewall
ufw allow "Nginx Full"

# Let's encrypt
certbot --nginx

# App dependencies
# If you need to install additional packages for your app, you can do it here.
# apt-get -y install ...
