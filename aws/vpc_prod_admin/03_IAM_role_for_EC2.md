# Create the role

aws iam create-role --role-name prod-admin-ec2-role --assume-role-policy-document file://iam_admin_ec2_app_role.json

# Attach policies

aws iam attach-role-policy --role-name prod-admin-ec2-role --policy-arn arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore

aws iam attach-role-policy --role-name prod-admin-ec2-role --policy-arn arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy

# Inline policy for Secrets Manager access (scoped to your secret)

aws iam put-role-policy --role-name prod-admin-ec2-app-role --policy-name prod-admin-ec2-secrets-policy --policy-document file://iam_admin_ec2_app_secrets_policy.json

# Create instance profile and attach role

aws iam create-instance-profile --instance-profile-name prod-admin-ec2-profile

aws iam add-role-to-instance-profile --instance-profile-name prod-admin-ec2-profile --role-name prod-admin-ec2-role
