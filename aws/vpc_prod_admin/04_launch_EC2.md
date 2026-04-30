# Amazon Linux 2023, update for your region

aws ec2 run-instances \
--image-id ami-0de6934e87badb694 \  
--instance-type t3.small \
--subnet-id "$env:PRIVATE_ADMIN_SUBNET_1A" \
--security-group-ids $env:ADMIN_EC2_SG \
--iam-instance-profile Name=prod-admin-ec2-profile \
--no-associate-public-ip-address \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=prod-admin}]' \
--user-data file://ec2_admin_ec2_bootstrap.sh
