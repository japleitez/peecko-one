#!/bin/bash
set -e

# System packages
dnf update -y
dnf install -y nodejs npm git amazon-cloudwatch-agent

# Fetch DB credentials from Secrets Manager
SECRET=$(aws secretsmanager get-secret-value --secret-id prod/api-db-credentials --query SecretString --output text)

DB_HOST=$(echo $SECRET | python3 -c "import sys,json; print(json.load(sys.stdin)['host'])")
DB_PASS=$(echo $SECRET | python3 -c "import sys,json; print(json.load(sys.stdin)['password'])")

# Write app env file (not committed to git)
cat > /opt/admin-app/.env <<EOF
NODE_ENV=production
DB_HOST=$DB_HOST
DB_PASSWORD=$DB_PASS
PORT=8080
EOF

# Install and start the app
cd /opt/admin-app
npm ci --omit=dev
npm run build

# Systemd service so the app restarts on crash
cat > /etc/systemd/system/admin-app.service <<EOF
[Unit]
Description=Fitness Admin App
After=network.target

[Service]
Type=simple
User=nodejs
WorkingDirectory=/opt/admin-app
ExecStart=/usr/bin/node dist/server.js
Restart=always
RestartSec=5
EnvironmentFile=/opt/admin-app/.env

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable admin-app
systemctl start admin-app

# Start CloudWatch agent
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c ssm:/fitness/cloudwatch-agent-config
