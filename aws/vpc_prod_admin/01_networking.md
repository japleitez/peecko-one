# Admin EC2 Networking

# Public subnets (for ALB and Bastion)

aws ec2 create-subnet --vpc-id "$env:VPC_ID" --cidr-block 10.0.3.0/24 --availability-zone eu-central-1a --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=prod-admin-public-az-a}]'
$env:PUBLIC_ADMIN_SUBNET_1A="subnet-0bb18b82cbfe2d93a"

aws ec2 create-subnet --vpc-id "$env:VPC_ID" --cidr-block 10.0.4.0/24 --availability-zone eu-central-1b --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=prod-admin-public-az-b}]'
$env:PUBLIC_ADMIN_SUBNET_1B="subnet-0b54cff2ae77ba7ac"

# Private app subnet (for Admin EC2)

aws ec2 create-subnet --vpc-id "$env:VPC_ID" --cidr-block 10.0.30.0/24 --availability-zone eu-central-1a --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=prod-admin-private-app-az-a}]'
$env:PRIVATE_ADMIN_SUBNET_1A="subnet-0f0f4fca552bbab08"

# Associate private subnets with private route table

aws ec2 associate-route-table --subnet-id "$env:PRIVATE_ADMIN_SUBNET_1A" --route-table-id "$env:PRIVATE_RT"

# Associate public subnets with public route table

aws ec2 associate-route-table --subnet-id "$env:PUBLIC_ADMIN_SUBNET_1A" --route-table-id "$env:PUBLIC_RT"
aws ec2 associate-route-table --subnet-id "$env:PUBLIC_ADMIN_SUBNET_1B" --route-table-id "$env:PUBLIC_RT"
