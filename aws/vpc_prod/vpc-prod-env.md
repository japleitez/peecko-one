# Resources IDs in Production environment

$env:VPC_ID="vpc-03e69010829f69220"
$env:PUBLIC_SUBNET_1A="subnet-0255945639b9abd5b"
$env:PUBLIC_SUBNET_1B="subnet-029a8d4991274990e"
$env:PRIVATE_SUBNET_1A="subnet-074b78068995f3a48"
$env:PRIVATE_SUBNET_1B="subnet-0299004642c71ffa6"
$env:DB_SUBNET_1A="subnet-0b5700678e8510999"
$env:DB_SUBNET_1B="subnet-0d4077e0ac8af73a1"
$env:IGW_ID="igw-0b6780a80ef55eacd"
$env:NAT_EIP="eipalloc-061479d185698a27b"
$env:NAT_GW_ID="nat-040e4d9884053de04"
$env:PUBLIC_RT="rtb-04f6798903bf6a38f"
$env:PRIVATE_RT="rtb-08990426383859f88"
$env:DB_RT="rtb-01beec46618e0501e"
$env:ALB_SG="sg-031342540a4bef66d"
$env:APP_SG="sg-067f9f3726458e934"
$env:RDS_SG="sg-0221e2a60dd31012c"
$env:DB_PASSWORD="K7mP9xL2vQ8rT5nB4wZ6cH3jF9gY1sA8eD2kL5pQ7rT9uV3xW6yZ8"
$env:RDS_ENDPOINT="prod-peecko-db.cvswuu602c9n.eu-central-1.rds.amazonaws.com"

# see all environment variables

Get-ChildItem Env:

# Filter for your specific one:

$env:NAT_GW_ID
Get-ChildItem Env:NAT_GW_ID
