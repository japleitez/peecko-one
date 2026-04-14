# Production-Ready AWS REST API Deployment Guide

**Target Stack:**

- Java Runtime: Amazon Corretto 24
- Node.js 20 (for build tools)
- Nginx (reverse proxy)
- PostgreSQL (RDS)
- 40,000 registered users | 500 concurrent peak users | 2 daily peak periods (~1 hour each)

---

## PART 1: ARCHITECTURE DESIGN

### High-Level Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                    AWS Account (us-east-1)              │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────────────────────────────────────┐   │
│  │           VPC (10.0.0.0/16)                      │   │
│  │                                                  │   │
│  │  ┌─────────────────┐  ┌──────────────────────┐   │   │
│  │  │  Public Subnet  │  │  Private Subnet      │   │   │
│  │  │  (AZ-a)         │  │  (AZ-a)              │   │   │
│  │  │ 10.0.1.0/24     │  │  10.0.2.0/24         │   │   │
│  │  │                 │  │                      │   │   │
│  │  │ ┌─────────────┐ │  │ ┌────────────────┐   │   │   │
│  │  │ │ ALB         │ │  │ │ EC2 ASG        │   │   │   │
│  │  │ │ (HTTPS)     │ │  │ │ Java + Nginx   │   │   │   │
│  │  │ └─────────────┘ │  │ └────────────────┘   │   │   │
│  │  │                 │  │                      │   │   │
│  │  │ ┌─────────────┐ │  │                      │   │   │
│  │  │ │ NAT GW      │ │  │                      │   │   │
│  │  │ └─────────────┘ │  │                      │   │   │
│  │  │                 │  │                      │   │   │
│  │  │ ┌─────────────┐ │  │                      │   │   │
│  │  │ │ IGW         │ │  │                      │   │   │
│  │  │ └─────────────┘ │  │                      │   │   │
│  │  └─────────────────┘  └──────────────────────┘   │   │
│  │                                                  │   │
│  │  ┌──────────────────────────────────────────┐    │   │
│  │  │  Isolated Database Subnet (Multi-AZ)     │    │   │
│  │  │  10.0.3.0/24 + 10.0.4.0/24               │    │   │
│  │  │                                          │    │   │
│  │  │  ┌──────────────┐  ┌────────────────┐    │    │   │
│  │  │  │ RDS Primary  │  │ ElastiCache    │    │    │   │
│  │  │  │ PostgreSQL   │  │ Redis (opt)    │    │    │   │
│  │  │  └──────────────┘  └────────────────┘    │    │   │
│  │  └──────────────────────────────────────────┘    │   │
│  └──────────────────────────────────────────────────┘   │
│                                                         │
│  CloudWatch (Metrics, Logs, Alarms)                     │
│  S3 (Database backups, logs archival)                   │
│  ACM (SSL/TLS certificates)                             │
│  Route 53 (DNS, optional)                               │
│  IAM (Roles & Policies)                                 │
└─────────────────────────────────────────────────────────┘
```

---

## PART 2: COMPONENT JUSTIFICATION

### Why EC2 Auto-Scaling Group (Not ECS/Fargate)?

**Chosen: EC2 ASG**

**Rationale:**

- **Cost-efficient for 1-person team:** EC2 allows granular control with On-Demand and Spot instances. For predictable read-heavy workloads with known peak times, you can use Reserved Instances or Savings Plans.
- **Lower operational overhead than ECS:** You manage one ASG with a launch template. No additional container orchestration complexity.
- **Better for Java workloads:** JVM warmup and memory footprint are more predictable on dedicated instances.
- **Scaling simplicity:** Target Tracking policy on CPU/memory metrics is straightforward to configure and debug.

**Cost estimate:**

- t3.medium instances (~$0.0416/hour): 2 instances baseline, 4-6 during peak
- Monthly: ~$250-350 for compute (reserved instances reduce this by 30-40%)

### Why RDS PostgreSQL (Not Self-Managed DB)?

**Chosen: RDS Multi-AZ**

**Rationale:**

- **Automated backups & HA:** Multi-AZ failover is automatic. No ops work required.
- **Read-only replicas optional:** For read-heavy workloads, read replicas scale reads without scaling write capacity.
- **Managed patches:** AWS handles minor/major version updates during maintenance windows.
- **Single-person team friendly:** No need to manage replication, backups, or disaster recovery manually.

**Cost estimate:**

- db.t3.medium (2 vCPU, 4 GB RAM): ~$0.122/hour
- Multi-AZ (standby in different AZ): 2× compute cost
- Monthly: ~$180/month for Multi-AZ; ~$90/month for single-AZ (not recommended for production)
- Backups & storage (100 GB initial): ~$20-30/month

### Why ALB (Application Load Balancer)?

**Chosen: ALB**

**Rationale:**

- **Layer 7 (HTTP/HTTPS) routing:** Can route by path, hostname, headers (useful for future microservices).
- **Native ACM integration:** Free SSL/TLS termination.
- **Cost-effective:** ~$16/month + $0.006 per LCU (request count); for 500 concurrent users with ~5 req/min average, negligible.
- **Health checks:** Automatic removal of unhealthy instances.

### Why Nginx on EC2 (Not ALB+Direct)?

**Why both?** ALB + Nginx follows best practices:

- **ALB terminates HTTPS** (TLS offloading).
- **Nginx does reverse proxy, caching, gzip compression** within the instance.
- **Nginx also acts as a local load balancer** if you run multiple Java processes per instance (future optimization).

### Caching Strategy

**Initial phase: CloudFront (optional, or skip).**

- For a read-heavy API returning JSON, caching at the edge is not typically beneficial unless you have global users.
- **Skipping initially** to reduce cost complexity.

**Future optimization: ElastiCache Redis.**

- Once you've identified expensive queries or frequently-accessed read-only data (e.g., configuration, user profiles), use Redis.
- Redis node cost: ~$0.017/hour for cache.t3.micro; negligible.
- Dramatically improves response times for read-heavy workloads.

### Security Posture

- **VPC isolation:** Public subnet (ALB) and private subnet (EC2 instances).
- **Databases in isolated subnets:** No direct internet access.
- **Security groups:** Restrict traffic by protocol/port.
- **IAM roles:** EC2 assumes role for access to RDS, S3, CloudWatch (no hardcoded credentials).
- **Secrets Manager:** Store DB passwords; EC2 retrieves at startup.
- **HTTPS enforced:** ALB + ACM certificate.
- **No public EC2 access:** Jump host (bastion) only if needed, via Systems Manager Session Manager (no SSH key management).

---

## PART 3: STEP-BY-STEP IMPLEMENTATION GUIDE

### Prerequisites

- AWS Account with billing enabled
- AWS CLI v2 installed locally
- Your domain name (for Route 53 / ACM certificate)
- Java 24 runtime, Node.js 20, Nginx knowledge

### Phase 1: Network Setup (VPC, Subnets, Gateways)

#### Step 1.1: Create VPC

```bash
# Create VPC
aws ec2 create-vpc --cidr-block 10.0.0.0/16 --tag-specifications 'ResourceType=vpc,Tags=[{Key=Name,Value=prod-api-vpc}]'

# Note the VPC ID (e.g., vpc-abc123)
export VPC_ID="vpc-abc123"

# Enable DNS hostname resolution
aws ec2 modify-vpc-attribute --vpc-id $VPC_ID --enable-dns-hostnames
aws ec2 modify-vpc-attribute --vpc-id $VPC_ID --enable-dns-support
```

#### Step 1.2: Create Subnets

```bash
# Public Subnet (AZ-a)
aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.1.0/24 \
  --availability-zone us-east-1a \
  --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=public-subnet-1a}]'
export PUBLIC_SUBNET_1A="subnet-abc123"

# Public Subnet (AZ-b) for ALB redundancy
aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.1.1/24 \
  --availability-zone us-east-1b \
  --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=public-subnet-1b}]'
export PUBLIC_SUBNET_1B="subnet-xyz789"

# Private Subnet (AZ-a) for EC2
aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.2.0/24 \
  --availability-zone us-east-1a \
  --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=private-subnet-1a}]'
export PRIVATE_SUBNET_1A="subnet-pqr456"

# Private Subnet (AZ-b) for EC2
aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.2.1/24 \
  --availability-zone us-east-1b \
  --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=private-subnet-1b}]'
export PRIVATE_SUBNET_1B="subnet-stuvwx"

# Database Subnet (AZ-a)
aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.3.0/24 \
  --availability-zone us-east-1a \
  --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=db-subnet-1a}]'
export DB_SUBNET_1A="subnet-db123"

# Database Subnet (AZ-b)
aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.4.0/24 \
  --availability-zone us-east-1b \
  --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=db-subnet-1b}]'
export DB_SUBNET_1B="subnet-db456"
```

#### Step 1.3: Create Internet Gateway

```bash
# Create IGW
aws ec2 create-internet-gateway \
  --tag-specifications 'ResourceType=internet-gateway,Tags=[{Key=Name,Value=prod-igw}]'
export IGW_ID="igw-abc123"

# Attach to VPC
aws ec2 attach-internet-gateway --internet-gateway-id $IGW_ID --vpc-id $VPC_ID
```

#### Step 1.4: Create NAT Gateway (for EC2 outbound internet access)

```bash
# Allocate Elastic IP for NAT Gateway
aws ec2 allocate-address --domain vpc --tag-specifications 'ResourceType=elastic-ip,Tags=[{Key=Name,Value=nat-gateway-eip}]'
export NAT_EIP="eipalloc-abc123"

# Create NAT Gateway in public subnet
aws ec2 create-nat-gateway \
  --subnet-id $PUBLIC_SUBNET_1A \
  --allocation-id $NAT_EIP \
  --tag-specifications 'ResourceType=nat-gateway,Tags=[{Key=Name,Value=prod-nat-gw}]'
export NAT_GW_ID="nat-abc123"

# Wait for NAT Gateway to be available (check status in AWS Console or CLI)
aws ec2 describe-nat-gateways --nat-gateway-ids $NAT_GW_ID --query 'NatGateways[0].State'
```

#### Step 1.5: Create and Configure Route Tables

```bash
# Public Route Table
aws ec2 create-route-table \
  --vpc-id $VPC_ID \
  --tag-specifications 'ResourceType=route-table,Tags=[{Key=Name,Value=public-rt}]'
export PUBLIC_RT="rtb-abc123"

# Route public traffic to IGW
aws ec2 create-route --route-table-id $PUBLIC_RT --destination-cidr-block 0.0.0.0/0 --gateway-id $IGW_ID

# Associate public subnets with public route table
aws ec2 associate-route-table --subnet-id $PUBLIC_SUBNET_1A --route-table-id $PUBLIC_RT
aws ec2 associate-route-table --subnet-id $PUBLIC_SUBNET_1B --route-table-id $PUBLIC_RT

# Private Route Table
aws ec2 create-route-table \
  --vpc-id $VPC_ID \
  --tag-specifications 'ResourceType=route-table,Tags=[{Key=Name,Value=private-rt}]'
export PRIVATE_RT="rtb-xyz789"

# Route private traffic to NAT Gateway
# Note: nat-gateway-id is obtained from the NAT GW creation, e.g., nat-1234567890abcdef0
aws ec2 create-route --route-table-id $PRIVATE_RT --destination-cidr-block 0.0.0.0/0 --nat-gateway-id nat-1234567890abcdef0

# Associate private subnets with private route table
aws ec2 associate-route-table --subnet-id $PRIVATE_SUBNET_1A --route-table-id $PRIVATE_RT
aws ec2 associate-route-table --subnet-id $PRIVATE_SUBNET_1B --route-table-id $PRIVATE_RT

# Database Route Table (no outbound internet)
aws ec2 create-route-table \
  --vpc-id $VPC_ID \
  --tag-specifications 'ResourceType=route-table,Tags=[{Key=Name,Value=db-rt}]'
export DB_RT="rtb-db123"

# Associate DB subnets (no route to IGW/NAT)
aws ec2 associate-route-table --subnet-id $DB_SUBNET_1A --route-table-id $DB_RT
aws ec2 associate-route-table --subnet-id $DB_SUBNET_1B --route-table-id $DB_RT
```

---

### Phase 2: Security Groups

#### Step 2.1: Create Security Groups

```bash
# ALB Security Group
aws ec2 create-security-group \
  --group-name alb-sg \
  --description "Security group for ALB" \
  --vpc-id $VPC_ID \
  --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=alb-sg}]'
export ALB_SG="sg-alb123"

# EC2 Security Group
aws ec2 create-security-group \
  --group-name app-sg \
  --description "Security group for EC2 instances" \
  --vpc-id $VPC_ID \
  --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=app-sg}]'
export APP_SG="sg-app123"

# RDS Security Group
aws ec2 create-security-group \
  --group-name rds-sg \
  --description "Security group for RDS" \
  --vpc-id $VPC_ID \
  --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=rds-sg}]'
export RDS_SG="sg-rds123"
```

#### Step 2.2: Configure Ingress/Egress Rules

```bash
# ALB: Accept HTTP (80) and HTTPS (443) from internet
aws ec2 authorize-security-group-ingress \
  --group-id $ALB_SG \
  --protocol tcp --port 80 --cidr 0.0.0.0/0

aws ec2 authorize-security-group-ingress \
  --group-id $ALB_SG \
  --protocol tcp --port 443 --cidr 0.0.0.0/0

# ALB: Allow outbound to EC2 app servers
aws ec2 authorize-security-group-egress \
  --group-id $ALB_SG \
  --protocol tcp --port 8080 --destination-group $APP_SG

# EC2 App: Accept from ALB on port 8080 (Nginx/Java)
aws ec2 authorize-security-group-ingress \
  --group-id $APP_SG \
  --protocol tcp --port 8080 --source-group $ALB_SG

# EC2 App: Allow outbound to RDS on 5432
aws ec2 authorize-security-group-egress \
  --group-id $APP_SG \
  --protocol tcp --port 5432 --destination-group $RDS_SG

# EC2 App: Allow outbound to ElastiCache (if using Redis)
aws ec2 authorize-security-group-egress \
  --group-id $APP_SG \
  --protocol tcp --port 6379 --destination-group $REDIS_SG

# RDS: Accept from EC2 on port 5432
aws ec2 authorize-security-group-ingress \
  --group-id $RDS_SG \
  --protocol tcp --port 5432 --source-group $APP_SG
```

---

### Phase 3: Database Setup (RDS PostgreSQL)

#### Step 3.1: Create DB Subnet Group

```bash
aws rds create-db-subnet-group \
  --db-subnet-group-name prod-db-subnet-group \
  --db-subnet-group-description "Subnet group for RDS" \
  --subnet-ids $DB_SUBNET_1A $DB_SUBNET_1B \
  --tag-specifications "ResourceType:DBSubnetGroup,Tags=[{Key=Name,Value=prod-db-sg}]"
```

#### Step 3.2: Create RDS PostgreSQL Instance (Multi-AZ)

```bash
# Generate random DB master password (store securely)
export DB_PASSWORD=$(openssl rand -base64 32)
echo "DB Password: $DB_PASSWORD" > /tmp/db-password.txt  # Store safely!

aws rds create-db-instance \
  --db-instance-identifier prod-api-db \
  --db-instance-class db.t3.medium \
  --engine postgres \
  --engine-version 16.1 \
  --master-username postgres \
  --master-user-password "$DB_PASSWORD" \
  --db-name api_db \
  --allocated-storage 100 \
  --storage-type gp3 \
  --storage-encrypted \
  --multi-az \
  --vpc-security-group-ids $RDS_SG \
  --db-subnet-group-name prod-db-subnet-group \
  --backup-retention-period 30 \
  --preferred-backup-window "03:00-04:00" \
  --preferred-maintenance-window "sun:04:00-sun:05:00" \
  --enable-cloudwatch-logs-exports postgresql \
  --deletion-protection \
  --tag-specifications "ResourceType:DBInstance,Tags=[{Key=Name,Value=prod-api-db}]"

# Wait for DB to be available (5-10 minutes)
aws rds describe-db-instances --db-instance-identifier prod-api-db --query 'DBInstances[0].DBInstanceStatus'

# Get the RDS endpoint
aws rds describe-db-instances --db-instance-identifier prod-api-db --query 'DBInstances[0].Endpoint.Address'
# e.g., prod-api-db.c9akciq32.us-east-1.rds.amazonaws.com
export RDS_ENDPOINT="prod-api-db.c9akciq32.us-east-1.rds.amazonaws.com"
```

#### Step 3.3: Store DB Credentials in Secrets Manager

```bash
aws secretsmanager create-secret \
  --name prod/api-db-credentials \
  --description "PostgreSQL database credentials" \
  --secret-string "{\"username\":\"postgres\",\"password\":\"$DB_PASSWORD\",\"host\":\"$RDS_ENDPOINT\",\"port\":5432,\"dbname\":\"api_db\"}"
```

---

### Phase 4: IAM Roles and Policies

#### Step 4.1: Create EC2 Instance Role

```bash
# Create IAM role for EC2
aws iam create-role \
  --role-name ec2-app-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "ec2.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  }' \
  --tags Key=Name,Value=ec2-app-role

# Create instance profile
aws iam create-instance-profile --instance-profile-name ec2-app-profile
aws iam add-role-to-instance-profile --instance-profile-name ec2-app-profile --role-name ec2-app-role

# Attach managed policies
aws iam attach-role-policy \
  --role-name ec2-app-role \
  --policy-arn arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy

aws iam attach-role-policy \
  --role-name ec2-app-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore  # For Session Manager access

# Create custom policy for Secrets Manager access
aws iam put-role-policy \
  --role-name ec2-app-role \
  --policy-name ec2-secrets-policy \
  --policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": "secretsmanager:GetSecretValue",
        "Resource": "arn:aws:secretsmanager:us-east-1:ACCOUNT_ID:secret:prod/api-db-credentials-*"
      }
    ]
  }'

# Create policy for S3 backups
aws iam put-role-policy \
  --role-name ec2-app-role \
  --policy-name ec2-s3-backups \
  --policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": ["s3:PutObject", "s3:GetObject"],
        "Resource": "arn:aws:s3:::prod-api-backups/*"
      }
    ]
  }'
```

---

### Phase 5: EC2 & Auto-Scaling Setup

#### Step 5.1: Create EC2 Launch Template

```bash
# Create user data script (runs on instance startup)
cat > /tmp/user-data.sh << 'EOF'
#!/bin/bash
set -e

# Update system
yum update -y
yum install -y \
  amazon-corretto-24-devel \
  nginx \
  nodejs \
  aws-cli \
  jq \
  CloudWatch \
  amazon-cloudwatch-agent

# Get DB credentials from Secrets Manager
aws secretsmanager get-secret-value \
  --secret-id prod/api-db-credentials \
  --query SecretString \
  --output text > /tmp/db-creds.json

DB_HOST=$(jq -r '.host' /tmp/db-creds.json)
DB_USER=$(jq -r '.username' /tmp/db-creds.json)
DB_PASS=$(jq -r '.password' /tmp/db-creds.json)
DB_NAME=$(jq -r '.dbname' /tmp/db-creds.json)

# Create application directories
mkdir -p /opt/api-app
cd /opt/api-app

# Download and deploy your Java application (example)
# Replace with your actual deployment method (S3, CodeDeploy, GitHub, etc.)
aws s3 cp s3://prod-api-builds/app-latest.jar ./app.jar

# Create systemd service for Java application
cat > /etc/systemd/system/api-app.service << 'SVCEOF'
[Unit]
Description=Java REST API Application
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/api-app
Environment="DB_HOST=$DB_HOST"
Environment="DB_USER=$DB_USER"
Environment="DB_PASS=$DB_PASS"
Environment="DB_NAME=$DB_NAME"
ExecStart=/usr/bin/java \
  -Xmx512m \
  -Ddb.host=$DB_HOST \
  -Ddb.user=$DB_USER \
  -Ddb.password=$DB_PASS \
  -Ddb.name=$DB_NAME \
  -Dserver.port=8080 \
  -jar app.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
SVCEOF

chmod 644 /etc/systemd/system/api-app.service
systemctl daemon-reload
systemctl enable api-app
systemctl start api-app

# Configure Nginx as reverse proxy
cat > /etc/nginx/conf.d/api-proxy.conf << 'NGXEOF'
upstream java_app {
    server 127.0.0.1:8080;
}

server {
    listen 8080;
    server_name _;

    client_max_body_size 10M;

    # Gzip compression for responses
    gzip on;
    gzip_types text/plain application/json;
    gzip_min_length 1000;

    # Proxy settings
    location / {
        proxy_pass http://java_app;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 30s;
        proxy_connect_timeout 5s;
    }

    # Health check endpoint
    location /health {
        access_log off;
        return 200 "healthy\n";
        add_header Content-Type text/plain;
    }
}
NGXEOF

# Enable and start Nginx
systemctl enable nginx
systemctl start nginx

# Configure CloudWatch agent (optional, for metrics)
cat > /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json << 'CWEOF'
{
  "metrics": {
    "namespace": "ProdAPI",
    "metrics_collected": {
      "cpu": {
        "measurement": [{"name": "cpu_usage_idle", "rename": "CPU_IDLE", "unit": "Percent"}],
        "metrics_collection_interval": 60
      },
      "disk": {
        "measurement": [{"name": "disk_used_percent", "rename": "DISK_USED", "unit": "Percent"}],
        "resources": ["/"]
      },
      "mem": {
        "measurement": [{"name": "mem_used_percent", "rename": "MEM_USED", "unit": "Percent"}]
      }
    }
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/var/log/nginx/access.log",
            "log_group_name": "/aws/ec2/prod-api/nginx-access",
            "log_stream_name": "{instance_id}"
          },
          {
            "file_path": "/opt/api-app/application.log",
            "log_group_name": "/aws/ec2/prod-api/app-logs",
            "log_stream_name": "{instance_id}"
          }
        ]
      }
    }
  }
}
CWEOF

/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
  -a fetch-config \
  -m ec2 \
  -s \
  -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json

echo "EC2 instance initialization complete"
EOF

# Base64 encode user data
USER_DATA_B64=$(base64 -w 0 /tmp/user-data.sh)

# Create launch template
aws ec2 create-launch-template \
  --launch-template-name prod-api-template \
  --version-description "Java app with Nginx" \
  --launch-template-data "{
    \"ImageId\": \"ami-0c55b159cbfafe1f0\",
    \"InstanceType\": \"t3.medium\",
    \"IamInstanceProfile\": {
      \"Name\": \"ec2-app-profile\"
    },
    \"SecurityGroupIds\": [\"$APP_SG\"],
    \"UserData\": \"$USER_DATA_B64\",
    \"TagSpecifications\": [
      {
        \"ResourceType\": \"instance\",
        \"Tags\": [{\"Key\": \"Name\", \"Value\": \"prod-api-instance\"}]
      }
    ],
    \"Monitoring\": {
      \"Enabled\": true
    }
  }"
```

**Note:** Get the AMI ID for Amazon Linux 2 (or your preferred OS):

```bash
# Get latest Amazon Linux 2 AMI
aws ec2 describe-images \
  --owners amazon \
  --filters "Name=name,Values=amzn2-ami-hvm-*" \
  --query 'sort_by(Images, &CreationDate)[-1].[ImageId,Name]'
```

#### Step 5.2: Create Auto-Scaling Group

```bash
aws autoscaling create-auto-scaling-group \
  --auto-scaling-group-name prod-api-asg \
  --launch-template LaunchTemplateName=prod-api-template,Version='$Latest' \
  --min-size 2 \
  --max-size 6 \
  --desired-capacity 2 \
  --default-cooldown 300 \
  --vpc-zone-identifier "$PRIVATE_SUBNET_1A,$PRIVATE_SUBNET_1B" \
  --target-group-arns "arn:aws:elasticloadbalancing:us-east-1:ACCOUNT_ID:targetgroup/prod-api/abc123" \
  --health-check-type ELB \
  --health-check-grace-period 300 \
  --tags "Key=Name,Value=prod-api-instance,PropagateAtLaunch=true"

# Create Target Tracking Scaling Policy (CPU-based)
aws autoscaling put-scaling-policy \
  --auto-scaling-group-name prod-api-asg \
  --policy-name cpu-target-tracking \
  --policy-type TargetTrackingScaling \
  --target-tracking-configuration "{
    \"TargetValue\": 70.0,
    \"PredefinedMetricSpecification\": {
      \"PredefinedMetricType\": \"ASGAverageCPUUtilization\"
    },
    \"ScaleOutCooldown\": 60,
    \"ScaleInCooldown\": 300
  }"
```

---

### Phase 6: Load Balancer Setup

#### Step 6.1: Create Application Load Balancer

```bash
aws elbv2 create-load-balancer \
  --name prod-api-alb \
  --subnets $PUBLIC_SUBNET_1A $PUBLIC_SUBNET_1B \
  --security-groups $ALB_SG \
  --scheme internet-facing \
  --type application \
  --ip-address-type ipv4 \
  --tags "Key=Name,Value=prod-api-alb"

export ALB_ARN="arn:aws:elasticloadbalancing:us-east-1:ACCOUNT_ID:loadbalancer/app/prod-api-alb/abc123"
export ALB_DNS="prod-api-alb-123456.us-east-1.elb.amazonaws.com"
```

#### Step 6.2: Create Target Group

```bash
aws elbv2 create-target-group \
  --name prod-api-tg \
  --protocol HTTP \
  --port 8080 \
  --vpc-id $VPC_ID \
  --target-type instance \
  --health-check-enabled \
  --health-check-protocol HTTP \
  --health-check-path /health \
  --health-check-interval-seconds 30 \
  --health-check-timeout-seconds 5 \
  --healthy-threshold-count 2 \
  --unhealthy-threshold-count 3 \
  --matcher HttpCode=200

export TARGET_GROUP_ARN="arn:aws:elasticloadbalancing:us-east-1:ACCOUNT_ID:targetgroup/prod-api-tg/abc123"
```

#### Step 6.3: Create Listeners (HTTP → HTTPS redirect)

```bash
# HTTP listener (redirect to HTTPS)
aws elbv2 create-listener \
  --load-balancer-arn $ALB_ARN \
  --protocol HTTP \
  --port 80 \
  --default-actions Type=redirect,RedirectConfig="{Protocol=HTTPS,Port=443,StatusCode=HTTP_301}"

# HTTPS listener (requires certificate)
# First, request or import a certificate in ACM
aws elbv2 create-listener \
  --load-balancer-arn $ALB_ARN \
  --protocol HTTPS \
  --port 443 \
  --certificates CertificateArn=arn:aws:acm:us-east-1:ACCOUNT_ID:certificate/abc123 \
  --default-actions Type=forward,TargetGroupArn=$TARGET_GROUP_ARN
```

---

### Phase 7: SSL/TLS Certificate (ACM)

#### Step 7.1: Request Certificate in ACM

```bash
# If you have a domain (e.g., api.example.com)
aws acm request-certificate \
  --domain-name api.example.com \
  --subject-alternative-names "*.api.example.com" \
  --validation-method DNS \
  --options CertificateTransparencyLoggingPreference=ENABLED

export CERT_ARN="arn:aws:acm:us-east-1:ACCOUNT_ID:certificate/abc123"

# Verify the certificate (follow email or DNS validation instructions)
# Once validated, it's ready for ALB attachment
```

#### Step 7.2: Update Listener with Certificate

```bash
aws elbv2 modify-listener \
  --listener-arn "arn:aws:elasticloadbalancing:us-east-1:ACCOUNT_ID:listener/app/prod-api-alb/abc123/abc123" \
  --certificates CertificateArn=$CERT_ARN
```

---

### Phase 8: DNS Setup (Route 53)

#### Step 8.1: Create Hosted Zone (if new domain)

```bash
aws route53 create-hosted-zone \
  --name example.com \
  --caller-reference "$(date +%s)"

export HOSTED_ZONE_ID="Z123456"
```

#### Step 8.2: Create A Record pointing to ALB

```bash
# Create JSON file for change batch
cat > /tmp/change-batch.json << EOF
{
  "Changes": [
    {
      "Action": "CREATE",
      "ResourceRecordSet": {
        "Name": "api.example.com",
        "Type": "A",
        "AliasTarget": {
          "HostedZoneId": "Z35SXDOTRQ7X7K",
          "DNSName": "$ALB_DNS",
          "EvaluateTargetHealth": true
        }
      }
    }
  ]
}
EOF

aws route53 change-resource-record-sets \
  --hosted-zone-id $HOSTED_ZONE_ID \
  --change-batch file:///tmp/change-batch.json
```

---

### Phase 9: Monitoring & Alarms

#### Step 9.1: Create CloudWatch Alarms

```bash
# CPU Utilization Alarm (triggers if avg > 80%)
aws cloudwatch put-metric-alarm \
  --alarm-name prod-api-high-cpu \
  --alarm-description "Alert when CPU exceeds 80%" \
  --metric-name CPUUtilization \
  --namespace AWS/EC2 \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --evaluation-periods 2 \
  --dimensions Name=AutoScalingGroupName,Value=prod-api-asg \
  --alarm-actions arn:aws:sns:us-east-1:ACCOUNT_ID:prod-alerts

# Target Group Unhealthy Hosts Alarm
aws cloudwatch put-metric-alarm \
  --alarm-name prod-api-unhealthy-hosts \
  --alarm-description "Alert when unhealthy target hosts detected" \
  --metric-name UnHealthyHostCount \
  --namespace AWS/ApplicationELB \
  --statistic Maximum \
  --period 60 \
  --threshold 1 \
  --comparison-operator GreaterThanOrEqualToThreshold \
  --dimensions Name=TargetGroup,Value=targetgroup/prod-api-tg/abc123 \
                Name=LoadBalancer,Value=app/prod-api-alb/abc123 \
  --alarm-actions arn:aws:sns:us-east-1:ACCOUNT_ID:prod-alerts

# RDS Database CPU Alarm
aws cloudwatch put-metric-alarm \
  --alarm-name prod-db-high-cpu \
  --alarm-description "Alert when RDS CPU > 75%" \
  --metric-name CPUUtilization \
  --namespace AWS/RDS \
  --statistic Average \
  --period 300 \
  --threshold 75 \
  --comparison-operator GreaterThanThreshold \
  --dimensions Name=DBInstanceIdentifier,Value=prod-api-db \
  --alarm-actions arn:aws:sns:us-east-1:ACCOUNT_ID:prod-alerts

# ALB Target Response Time
aws cloudwatch put-metric-alarm \
  --alarm-name prod-alb-slow-response \
  --alarm-description "Alert when target response > 1 second" \
  --metric-name TargetResponseTime \
  --namespace AWS/ApplicationELB \
  --statistic Average \
  --period 300 \
  --threshold 1.0 \
  --comparison-operator GreaterThanThreshold \
  --dimensions Name=LoadBalancer,Value=app/prod-api-alb/abc123 \
  --alarm-actions arn:aws:sns:us-east-1:ACCOUNT_ID:prod-alerts
```

#### Step 9.2: Create SNS Topic for Alerts

```bash
aws sns create-topic --name prod-alerts
export SNS_TOPIC_ARN="arn:aws:sns:us-east-1:ACCOUNT_ID:prod-alerts"

# Subscribe email
aws sns subscribe \
  --topic-arn $SNS_TOPIC_ARN \
  --protocol email \
  --notification-endpoint your-email@example.com
```

---

### Phase 10: Backups and Disaster Recovery

#### Step 10.1: Enable RDS Automated Backups (Already in RDS creation)

- 30-day retention period set in RDS creation
- Automatic backups stored in S3
- Point-in-time recovery available

#### Step 10.2: Create S3 Bucket for Manual Backups

```bash
aws s3 mb s3://prod-api-backups --region us-east-1

# Enable versioning
aws s3api put-bucket-versioning \
  --bucket prod-api-backups \
  --versioning-configuration Status=Enabled

# Enable encryption
aws s3api put-bucket-encryption \
  --bucket prod-api-backups \
  --server-side-encryption-configuration '{
    "Rules": [
      {
        "ApplyServerSideEncryptionByDefault": {
          "SSEAlgorithm": "AES256"
        }
      }
    ]
  }'

# Lifecycle policy (archive after 90 days, delete after 1 year)
aws s3api put-bucket-lifecycle-configuration \
  --bucket prod-api-backups \
  --lifecycle-configuration file:///tmp/lifecycle.json
```

#### Step 10.3: Manual Database Backup Script

Create a Lambda function or cron job on EC2 to trigger snapshots:

```bash
#!/bin/bash
# Script to create manual RDS snapshot and export to S3

SNAPSHOT_ID="manual-backup-$(date +%Y%m%d-%H%M%S)"

# Create snapshot
aws rds create-db-snapshot \
  --db-instance-identifier prod-api-db \
  --db-snapshot-identifier $SNAPSHOT_ID

# Wait for snapshot to complete
echo "Waiting for snapshot to complete..."
aws rds wait db-snapshot-available --db-snapshot-identifier $SNAPSHOT_ID

echo "Snapshot $SNAPSHOT_ID completed"
```

---

## PART 4: SCALING & OPTIMIZATION

### Horizontal Scaling (Auto-Scaling)

The ASG automatically scales based on CPU utilization:

- **Scale-out:** When CPU > 70% for 1 minute, add 1-2 instances (max 6)
- **Scale-in:** When CPU < 40% for 5 minutes, remove instances (min 2)

### Vertical Scaling

If you need more power, increase instance type in launch template:

```bash
# t3.medium → t3.large (double vCPU/RAM)
aws ec2 create-launch-template-version \
  --launch-template-name prod-api-template \
  --source-version 1 \
  --launch-template-data '{"InstanceType":"t3.large"}'

# Update ASG to use new version
aws autoscaling update-auto-scaling-group \
  --auto-scaling-group-name prod-api-asg \
  --launch-template LaunchTemplateName=prod-api-template,Version='$Latest'

# Terminate old instances to force replacement
aws autoscaling start-instance-refresh \
  --auto-scaling-group-name prod-api-asg
```

### Future Optimizations

#### 1. Read Replicas (RDS)

For read-heavy workloads, create a read replica:

```bash
aws rds create-db-instance-read-replica \
  --db-instance-identifier prod-api-db-read-replica \
  --source-db-instance-identifier prod-api-db \
  --db-instance-class db.t3.medium
```

Update your app to read from the replica (round-robin or by query type).

#### 2. ElastiCache Redis

For frequently-accessed data (user profiles, configs):

```bash
# Create Redis cluster
aws elasticache create-cache-cluster \
  --cache-cluster-id prod-api-redis \
  --engine redis \
  --cache-node-type cache.t3.micro \
  --engine-version 7.0 \
  --num-cache-nodes 1 \
  --vpc-security-group-ids $REDIS_SG \
  --cache-subnet-group-name prod-cache-subnet

# In your Java app, add Redis client (Jedis, Lettuce)
# Cache read-heavy queries:
# 1. Check cache (Redis)
# 2. If miss, query DB
# 3. Store in cache (TTL: 5-15 minutes)
```

**Cost:** cache.t3.micro ≈ $0.017/hour (~$12/month)

#### 3. CloudFront (CDN)

For static assets or cacheable API responses:

```bash
aws cloudfront create-distribution \
  --distribution-config file:///tmp/cloudfront-config.json
```

**Not recommended initially** unless you have global users.

#### 4. Increase Java Heap Size

If you're handling large payloads:

```bash
# In user data, update JVM args
ExecStart=/usr/bin/java \
  -Xmx1g \
  -Xms512m \
  ...
```

Monitor GC pauses in CloudWatch.

---

## PART 5: COST BREAKDOWN (Monthly)

### Baseline Configuration (2 EC2 + RDS Multi-AZ)

| Component     | Instance Type  | Cost/Month   | Notes                                                |
| ------------- | -------------- | ------------ | ---------------------------------------------------- |
| EC2 (Compute) | 2× t3.medium   | $60–80       | On-Demand; use Reserved Instances for -30%           |
| ALB           | 1× ALB         | $16          | Plus $0.006 per LCU (~$10–20 for typical workload)   |
| RDS (Compute) | db.t3.medium   | $180         | Multi-AZ (2× base cost); includes backups            |
| RDS (Storage) | 100 GB gp3     | $10          | EBS gp3 storage                                      |
| NAT Gateway   | 1× NAT GW      | $32          | $0.045/hour + data transfer                          |
| Data Transfer | Outbound       | $10–30       | Depends on app traffic volume                        |
| CloudWatch    | Logs + Metrics | $5–15        | Minimal for small deployments                        |
| S3 (Backups)  | 100 GB stored  | $2–5         | Standard storage; lifecycle to Glacier after 90 days |
| **Total**     |                | **$315–378** |                                                      |

### Cost Optimization Strategies

1. **Reserved Instances (RI):**

   - Commit to 1-year on EC2/RDS → 30% discount
   - 2× t3.medium RI: ~$40–50/month (vs. $60/month)
   - RDS RI: ~$120/month (vs. $180)
   - **Savings: ~$130/month (35%)**

2. **Spot Instances (ASG):**

   - Use Spot for 50% of ASG capacity
   - Fallback to On-Demand if Spot unavailable
   - Savings: ~$25–30/month additional

3. **Single-AZ RDS (not recommended for production):**

   - ~$90/month instead of $180
   - **Trade-off:** No automatic failover during AZ outage

4. **Downsize Initial Setup:**

   - db.t3.small (~$60/month) instead of .medium
   - Scale up as needed

5. **Right-size based on metrics:**
   - Monitor CloudWatch metrics
   - If CPU never exceeds 20%, downsize instances

### Production Setup Cost (Optimized)

| Item                | Cost      |
| ------------------- | --------- |
| EC2 (RI 1-year)     | $50       |
| ALB                 | $25       |
| RDS RI + storage    | $130      |
| NAT + Data Transfer | $40       |
| Monitoring          | $10       |
| S3 + misc           | $10       |
| **Total/Month**     | **~$265** |

---

## PART 6: MONITORING & LOGGING STRATEGY

### CloudWatch Dashboards

Create a custom dashboard in CloudWatch:

```bash
aws cloudwatch put-dashboard \
  --dashboard-name prod-api-dashboard \
  --dashboard-body file:///tmp/dashboard.json
```

Monitor:

- EC2 CPU, Memory, Network (per instance & ASG aggregate)
- ALB request count, target response time, HTTP errors
- RDS CPU, connections, database size, replication lag
- Application logs (errors, slow requests)

### Application Logging

Configure your Java app to log to CloudWatch:

```java
// Using log4j2 or SLF4J
Logger logger = LoggerFactory.getLogger(MyController.class);
logger.info("Request received: path={}, duration={}ms", path, duration);
logger.error("Database error", exception);
```

View logs in CloudWatch Logs:

```bash
aws logs tail /aws/ec2/prod-api/app-logs --follow
```

### Query Logs

Enable PostgreSQL slow query logging:

```sql
-- Connect to RDS
psql -h $RDS_ENDPOINT -U postgres -d api_db

-- Enable logging
ALTER SYSTEM SET log_statement = 'all';
ALTER SYSTEM SET log_min_duration_statement = 1000;  -- Log queries > 1 second
SELECT pg_reload_conf();
```

View in CloudWatch Logs: `/aws/rds/instance/prod-api-db/postgresql`

---

## PART 7: SECURITY BEST PRACTICES

### Network Isolation

- ✅ ALB in public subnet only
- ✅ EC2 in private subnet (no direct internet access)
- ✅ RDS in isolated subnet with no NAT/IGW route
- ✅ All inter-component traffic via security groups

### Secrets Management

Store sensitive data in AWS Secrets Manager, not in code:

```java
// Retrieve at application startup
SecretsManagerClient client = SecretsManagerClient.builder().region(Region.US_EAST_1).build();

GetSecretValueRequest request = GetSecretValueRequest.builder().secretId("prod/api-db-credentials").build();

GetSecretValueResponse response = client.getSecretValue(request);

String secret = response.secretString(); // JSON with credentials

```

### IAM Least Privilege

EC2 role has access to:

- ✅ Secrets Manager (read only, specific secret)
- ✅ S3 backups bucket (put/get objects)
- ✅ CloudWatch (write metrics/logs)
- ✅ Systems Manager Session Manager (remote access without SSH keys)

**No access to:**

- ❌ Other S3 buckets
- ❌ EC2 termination/modification
- ❌ Database snapshot operations

### Encryption

- ✅ RDS: Encrypted at rest (AWS KMS)
- ✅ Secrets Manager: Encrypted (AWS KMS)
- ✅ S3 backups: Server-side encryption (AES-256)
- ✅ ALB to EC2: HTTPS termination at ALB; HTTP internally (trusted VPC)
- ✅ EC2 to RDS: Network isolation (security groups only)

### SSL/TLS Certificates

- ✅ ACM manages certificate renewal (automatic)
- ✅ ALB enforces HTTPS (HTTP→HTTPS redirect)
- ✅ TLS 1.2+ enforced

---

## PART 8: DEPLOYMENT & CI/CD (Optional)

### Manual Deployment

1. Build JAR locally or in a CI/CD pipeline
2. Upload to S3
3. Update launch template user data with new S3 URI
4. Trigger instance refresh:

```bash
aws autoscaling start-instance-refresh \
  --auto-scaling-group-name prod-api-asg
```

### Automated CI/CD (GitHub Actions / GitLab CI)

Example GitHub Actions workflow:

```yaml
name: Build and Deploy

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Upload to S3
        run: aws s3 cp target/app.jar s3://prod-api-builds/app-${{ github.sha }}.jar
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
      - name: Update Launch Template
        run: |
          aws ec2 create-launch-template-version \
            --launch-template-name prod-api-template \
            --source-version '$Latest' \
            --launch-template-data '{"UserData":"LS0gVXBkYXRlZCBUS0ZSVQ=="}'
      - name: Trigger Instance Refresh
        run: |
          aws autoscaling start-instance-refresh \
            --auto-scaling-group-name prod-api-asg
```

---

## PART 9: TROUBLESHOOTING

### Instance is not starting

```bash
# Check system logs
aws ec2 get-console-output --instance-id i-1234567890abcdef0

# Check CloudWatch logs
aws logs tail /aws/ec2/prod-api/app-logs --follow
```

### Instances are unhealthy (target group)

```bash
# Check health check failures in ALB
aws elbv2 describe-target-health --target-group-arn $TARGET_GROUP_ARN

# Manually SSH into instance (via Systems Manager)
aws ssm start-session --target i-1234567890abcdef0

# Check Nginx
curl http://localhost:8080/health
systemctl status nginx
systemctl status api-app

# View logs
tail -f /var/log/nginx/error.log
tail -f /opt/api-app/application.log
```

### Database connection errors

```bash
# Verify RDS is running
aws rds describe-db-instances --db-instance-identifier prod-api-db --query 'DBInstances[0].DBInstanceStatus'

# Test connection from EC2 instance
psql -h $RDS_ENDPOINT -U postgres -d api_db

# Check security group allows EC2 → RDS
aws ec2 describe-security-groups --group-ids $RDS_SG
```

### Auto-scaling not triggering

```bash
# Check scaling policies
aws autoscaling describe-policies --auto-scaling-group-name prod-api-asg

# Check current metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/EC2 \
  --metric-name CPUUtilization \
  --dimensions Name=AutoScalingGroupName,Value=prod-api-asg \
  --statistics Average \
  --start-time $(date -u -d '1 hour ago' +%Y-%m-%dT%H:%M:%S) \
  --end-time $(date -u +%Y-%m-%dT%H:%M:%S) \
  --period 300
```

---

## PART 10: CHECKLISTS

### Pre-Launch Verification

- [ ] VPC created with proper CIDR blocks
- [ ] Public/Private/Database subnets in 2+ AZs
- [ ] IGW and NAT GW deployed
- [ ] Security groups configured (ingress/egress rules)
- [ ] RDS PostgreSQL running (Multi-AZ)
- [ ] Secrets Manager with DB credentials
- [ ] EC2 launch template with user data script
- [ ] Auto-Scaling Group min=2, max=6
- [ ] ALB deployed and registered with target group
- [ ] SSL certificate in ACM
- [ ] Route 53 A record pointing to ALB
- [ ] CloudWatch alarms configured
- [ ] SNS topic for alerts
- [ ] S3 bucket for backups created
- [ ] IAM roles and policies attached

### Post-Launch Validation

- [ ] ALB returns HTTP 200 for /health
- [ ] Target group shows both instances healthy
- [ ] HTTPS certificate valid (check browser)
- [ ] CloudWatch metrics showing traffic
- [ ] RDS backups running
- [ ] Scaling policy working (monitor for 1 hour)
- [ ] Logs appearing in CloudWatch Logs
- [ ] Email alerts working for SNS

---

## CONCLUSION

This design is optimized for:

- ✅ **Low operational overhead:** Managed services (ALB, RDS, ASG)
- ✅ **Cost efficiency:** ~$265–380/month for production setup
- ✅ **High availability:** Multi-AZ for EC2 and RDS
- ✅ **Scalability:** Auto-scaling handles 500 concurrent users
- ✅ **Security:** Network isolation, encryption, IAM least privilege

**For a 1-person team:**

- Use AWS Console for quick sanity checks
- Automation (CloudFormation/Terraform) for repeatable deployments
- CloudWatch alarms to notify of issues
- AWS Systems Manager Session Manager for remote access (no SSH key management)

**Next steps:**

1. Create AWS account and set up billing alerts
2. Follow Phases 1–6 above (network → EC2)
3. Deploy your Java application using the user data script
4. Monitor in CloudWatch for 24 hours
5. Implement CI/CD pipeline for future deployments

Good luck! 🚀
