# 1. Upload new jar from CI/CD

aws s3 cp admin-2.0.0.jar s3://prod-peecko-api-backup-435496007267-eu-central-1-an/admin.jar

# 2. SSH to instance (via Bastion or SSM Session Manager) and run:

sudo aws s3 cp s3://prod-peecko-api-backup-435496007267-eu-central-1-an/admin.jar /opt/peecko_admin/admin.jar

# 3. Change owner to ssm-user

sudo chown ssm-user:ssm-user /opt/peecko_admin/admin.jar

# 4. Graceful restart — Spring Boot drains in-flight requests during SIGTERM

sudo systemctl restart peecko_admin

# 5. Verify service status

sudo systemctl status peecko_admin
sudo journalctl -u peecko_admin -f

# 6. Verify version

https://admin-peecko.com/admin/
@2026
