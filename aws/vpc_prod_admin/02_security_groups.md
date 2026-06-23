# Admin ALB security group

aws ec2 create-security-group --group-name prod-admin-alb-sg --description "Prod Admin ALB - HTTPS inbound from allowlisted IPs" --vpc-id "$env:VPC_ID"
$env:ADMIN_ALB_SG="sg-05570b42452695d4a"

# Allow HTTPS from your office/VPN CIDRs only, replace with your IP range

aws ec2 authorize-security-group-ingress --group-id "$env:ADMIN_ALB_SG" --protocol tcp --port 443 --cidr 0.0.0.0/24

# Admin EC2 security group

aws ec2 create-security-group --group-name prod-admin-ec2-sg --description "Prod Admin EC2 - inbound from ALB only" --vpc-id "$env:VPC_ID"
$env:ADMIN_EC2_SG="sg-0f7b5da5c25490514"

# Allow traffic ONLY from the ALB security group (not a CIDR)

aws ec2 authorize-security-group-ingress --group-id "$env:ADMIN_EC2_SG" --protocol tcp --port 8080 --source-group "$env:ADMIN_ALB_SG"

# Outbound: allow HTTPS to Secrets Manager, RDS port, and NAT

aws ec2 authorize-security-group-egress --group-id "$env:ADMIN_EC2_SG" --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-egress --group-id "$env:ADMIN_EC2_SG" --protocol tcp --port 5432 --source-group "$env:RDS_SG"
