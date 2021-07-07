#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)

STACK=polygbot
if [[ $# -eq 1 ]]; then
  STACK=$1
  echo "Deploying to stack $STACK"
fi

TEMPLATE=template.yml
if [ $1 ]; then
  if [ $1 = mvn ]; then
    TEMPLATE=template-mvn.yml
    mvn package
  fi
else
  gradle build -i
fi

aws cloudformation package --template-file $TEMPLATE --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
aws cloudformation deploy --template-file out.yml --stack-name $STACK --capabilities CAPABILITY_NAMED_IAM

# attach to different VPC
#aws cloudformation deploy --template-file out.yml --stack-name $STACK --capabilities CAPABILITY_NAMED_IAM --parameter-overrides vpcStackName=lambda-vpc secretName=lambda-db-password
